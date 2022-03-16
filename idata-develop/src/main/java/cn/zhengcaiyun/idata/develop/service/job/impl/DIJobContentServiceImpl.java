/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.develop.constant.enums.DiConfigModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.SrcReadModeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.ScriptMergeSqlParamDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
@Service
public class DIJobContentServiceImpl implements DIJobContentService {

    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;

    @Autowired
    public DIJobContentServiceImpl(DIJobContentRepo diJobContentRepo,
                                   JobInfoRepo jobInfoRepo) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
    }

    @Override
    public DIJobContentContentDto save(Long jobId, DIJobContentContentDto contentDto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkJobContent(contentDto);
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        // 判断目标表是否在其他job中已经存在
        String destTable = contentDto.getDestTable();
        checkArgument(unUsedDestTable(destTable, jobId), "目标表已经被其他作业使用");

        Integer version = contentDto.getVersion();

        // query/mergeSql封装
        String jobType = jobInfoOptional.get().getJobType();
        assembleQueryAndMergeSql(contentDto, jobType);


        boolean startNewVersion = true;
        //更新
        if (Objects.nonNull(version)) {
            Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
            checkArgument(jobContentOptional.isPresent(), "作业版本不存在或已删除");
            DIJobContent existJobContent = jobContentOptional.get();
            if (existJobContent.getEditable().equals(EditableEnum.YES.val)) {
                // 版本可以编辑，直接覆盖
                startNewVersion = false;
                contentDto.setId(existJobContent.getId());
                contentDto.setJobId(jobId);
                contentDto.resetEditor(operator);
                DIJobContent updateContent = contentDto.toModel();
                updateContent.setEditable(EditableEnum.YES.val);
                diJobContentRepo.update(updateContent);
            }
        }

        // 保存
        if (startNewVersion) {
            // 版本为空或不可编辑，新增版本
            version = diJobContentRepo.newVersion(jobId);
            contentDto.setId(null);
            contentDto.setJobId(jobId);
            contentDto.setVersion(version);
            contentDto.setOperator(operator);
            DIJobContent newVersionContent = contentDto.toModel();
            newVersionContent.setEditable(EditableEnum.YES.val);
            diJobContentRepo.save(newVersionContent);
        }

        return get(jobId, version);
    }

    /**
     * 封装query和mergesql
     *
     * @param contentDto
     */
    private void assembleQueryAndMergeSql(DIJobContentContentDto contentDto, String jobType) {
        // 数据DI并且是增量模式拼mergeSql
        boolean buildMergeSql = StringUtils.containsIgnoreCase(jobType, "DI") && StringUtils.equalsIgnoreCase(contentDto.getSrcReadMode(), SrcReadModeEnum.INC.value);
        String destTable = contentDto.getDestTable();
        String srcTables = contentDto.getSrcTables();
        String srcReadFilter = contentDto.getSrcReadFilter();
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType());
        switch (DiConfigModeEnum.getByValue(contentDto.getConfigMode())) {
            case VISIBLE:
                // 设置SrcQuery
                List<MappingColumnDto> srcCols = contentDto.getSrcCols();
                String srcQuery = generateSrcQuery(srcCols, srcReadFilter, srcTables);
                contentDto.setSrcQuery(srcQuery);

                // 设置MergeSql
                List<String> visColumnList = srcCols.stream().map(e -> e.getName()).collect(Collectors.toList());
                List<String> visKeyColumnList = srcCols.stream().filter(e -> (e.getPrimaryKey() != null && e.getPrimaryKey())).map(e -> e.getName()).collect(Collectors.toList());
                String visKeys = "id";
                if (CollectionUtils.isNotEmpty(visKeyColumnList)) {
                    visKeys = StringUtils.join(visKeyColumnList, ",");
                }
                if (buildMergeSql) {
                    String mergeSql = generateMergeSql(visColumnList, visKeys, srcTables, destTable, dataSourceTypeEnum, 3);
                    contentDto.setMergeSql(mergeSql);
                }
                break;
            case SCRIPT:
                // 设置SrcQuery
                String scriptSelectColumns = contentDto.getScriptSelectColumns();
                String scriptQuery = generateScriptQuery(scriptSelectColumns, srcReadFilter, srcTables);
                contentDto.setScriptQuery(scriptQuery);

                // 设置ScriptMergeSql
                ScriptMergeSqlParamDto scriptMergeSqlParamDto = contentDto.getScriptMergeSqlParamDto();
                int days = 3;
                if (scriptMergeSqlParamDto != null && scriptMergeSqlParamDto.getRecentDays() != null) {
                    days = scriptMergeSqlParamDto.getRecentDays();
                }
                if (buildMergeSql && StringUtils.isNotEmpty(scriptSelectColumns)) {
                    List<String> scriptColumnList = Arrays.asList(scriptSelectColumns.split(","));
                    String scriptMergeSql = generateMergeSql(scriptColumnList, contentDto.getScriptKeyColumns(), srcTables, destTable, dataSourceTypeEnum, days);
                    contentDto.setScriptMergeSql(scriptMergeSql);
                }
                break;
        }
    }

    private String generateScriptQuery(String selectColumns, String srcReadFilter, String srcTables) {
        String scriptQuery = String.format("select %s from %s ", selectColumns, srcTables);
        if (StringUtils.isNotEmpty(srcReadFilter)) {
            scriptQuery += (" where " + srcReadFilter);
        }
        return scriptQuery;
    }

    private String generateSrcQuery(List<MappingColumnDto> mappingColumnList, String srcReadFilter, String srcTables) {
        List<String> columns = mappingColumnList
                .stream()
                .filter(e -> e.getMappedColumn() != null)
                .map(e -> {
                    String name = e.getName();
                    String mappingSql = e.getMappingSql();
                    return StringUtils.isNotEmpty(mappingSql) ? mappingSql : name;
                }).collect(Collectors.toList());

        String selectColumns = StringUtils.join(columns, ",");
        String srcQuery = String.format("select %s from %s ", selectColumns, srcTables);
        if (StringUtils.isNotEmpty(srcReadFilter)) {
            srcQuery += (" where " + srcReadFilter);
        }
        return srcQuery;
    }

    @Override
    public DIJobContentContentDto get(Long jobId, Integer version) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");

        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        return DIJobContentContentDto.from(jobContentOptional.get());
    }

    @Override
    public String generateMergeSql(List<String> columnList, String keyColumns, String sourceTable, String destTable, DataSourceTypeEnum typeEnum, int days) throws IllegalArgumentException {
        checkArgument(destTable.split("\\.").length == 2, "生成mergeSql的destTable必须带上库名: " + destTable);
        String tmpTableParam = "src." + destTable.split("\\.")[1] + "_pt";
        // 匹配规则：例如 "tableName[1-2]"
        String regex1 = "(\\w+)\\[(\\d+-\\d+)\\]";
        // 匹配规则：例如 "212tableName2,223tableName2"
        String regex2 = "(\\d+(\\w+)[,]{0,1})+";

        // 是否涉及多张表（分区表）
        boolean isMulPartitionParam = false;
        if (typeEnum == DataSourceTypeEnum.mysql && (ReUtil.isMatch(regex1, sourceTable) || ReUtil.isMatch(regex2, sourceTable))) {
            isMulPartitionParam = true;
        }

        // 筛选的列名
        String columnsParam = StringUtils.join(columnList, ", ");
        // 筛选的带函数的列名
        String alisColumns = StringUtils.join(columnList.stream().map(e -> "t1." + e).collect(Collectors.toList()), "\n,");
        //生成keyCondition，key连接表，例如"t1.id=t2.id"
        List<String> keyColumnList = Arrays.asList(keyColumns.split(",")).stream().map(e -> "t1." + e + "=t2." + e).collect(Collectors.toList());
        String keyConditionParam = StringUtils.join(keyColumnList, " and ");
        //生成keyCondition，key连接表，例如"t2.id=null"
        List<String> whereKeyConditionList = Arrays.asList(keyColumns.split(",")).stream().map(e -> "t2." + e + "=null").collect(Collectors.toList());
        String whereKeyConditionParam = StringUtils.join(keyColumnList, " and ");

        String mergeSqlTemplate = "";
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/merge_sql_template2.sql");) {
            byte[] buff = new byte[1024];
            int btr = 0;
            while ((btr = inputStream.read(buff)) != -1) {
                mergeSqlTemplate += new String(buff, 0, btr, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException("模版文件解析失败");
        }

        // 通过SpEL解析
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("isMulPartition", isMulPartitionParam);
        context.setVariable("destTable", destTable);
        context.setVariable("columns", columnsParam);
        context.setVariable("tmpTable", tmpTableParam);
        context.setVariable("alisColumns", alisColumns);
        context.setVariable("keyCondition", keyConditionParam);
        context.setVariable("whereKeyConditionParam", whereKeyConditionParam);
        context.setVariable("days", days);
        context.setVariable("br", "\n");

        Expression expression = parser.parseExpression(mergeSqlTemplate, new TemplateParserContext());
        return expression.getValue(context, String.class);
    }

    private void checkJobContent(DIJobContentContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源编号为空");
        if (contentDto.getJobType() == JobTypeEnum.DI_BATCH || contentDto.getJobType() == JobTypeEnum.DI_STREAM) {
            checkArgument(StringUtils.isNotBlank(contentDto.getSrcReadMode()), "读取模式为空");
        }
        checkArgument(StringUtils.isNotBlank(contentDto.getDestDataSourceType()), "目标数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "目标数据源编号为空");
        if (!StringUtils.equalsIgnoreCase(contentDto.getDestDataSourceType(), DriverTypeEnum.Kafka.name())) {
            checkArgument(StringUtils.isNotBlank(contentDto.getDestTable()), "目标数据表为空");
        }
        checkArgument(StringUtils.isNotBlank(contentDto.getDestWriteMode()), "写入模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcTables()), "来源数据表为空");
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getSrcCols()), "来源数据表字段为空");   数据迁移完后需要取消注释
//todo        checkArgument(ObjectUtils.isNotEmpty(contentDto.getDestCols()), "目标数据表字段为空");  数据迁移完后需要取消注释

        List<MappingColumnDto> mappingColumnDtoList = contentDto.getSrcCols().stream()
                .filter(columnDto -> Objects.nonNull(columnDto.getMappedColumn()))
                .collect(Collectors.toList());
//todo        checkArgument(ObjectUtils.isNotEmpty(mappingColumnDtoList), "映射字段为空");  数据迁移完后需要取消注释
    }

    private boolean unUsedDestTable(String destTable, Long excludeJobId) {
        List<DIJobContent> diJobContents = diJobContentRepo.queryList(destTable);
        if (ObjectUtils.isEmpty(diJobContents)) return true;

        Set<Long> jobIdSet = diJobContents.stream()
                .map(DIJobContent::getJobId)
                .distinct()
                .filter(job_id -> !job_id.equals(excludeJobId))
                .collect(Collectors.toSet());
        return ObjectUtils.isEmpty(jobIdSet);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        List<String> visKeyColumnList = list.stream().collect(Collectors.toList());
    }
}
