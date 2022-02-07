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
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

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
    private DataSourceService dataSourceService;

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
        checkArgument(unUsedDestTable(contentDto.getDestTable(), jobId), "目标表已经被其他作业使用");

        Integer version = contentDto.getVersion();

        // 可视化的query无法手写，自动生成
        String srcQuery = generateSrcQuery(contentDto.getSrcCols(), contentDto.getSrcReadFilter(), contentDto.getSrcTables());
        contentDto.setSrcQuery(srcQuery);

        boolean startNewVersion = true;
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

    private String generateSrcQuery(List<MappingColumnDto> mappingColumnList, String condition, String srcTables) {
        boolean generate = mappingColumnList.stream().anyMatch(e -> StringUtils.isNotEmpty(e.getMappingSql()));
        if (!generate) {
            return null;
        }

        List<String> columns = mappingColumnList.stream().map(e -> {
            String name = e.getName();
            String mappingSql = e.getMappingSql();
            return StringUtils.isNotEmpty(mappingSql) ? mappingSql : name;
        }).collect(Collectors.toList());

        String selectColumns = StringUtils.join(columns, ",");
        StringBuffer stringBuffer = new StringBuffer("select " + selectColumns);
        stringBuffer.append(" from " + srcTables);
        if (StringUtils.isNotEmpty(condition)) {
            stringBuffer.append(" where " + condition);
        }
        return stringBuffer.toString();
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
    public String generateMergeSql(String selectColumns, String keyColumns, String sourceTable, String destTable, DestWriteModeEnum diMode, DriverTypeEnum typeEnum) throws IllegalArgumentException {
        String tmpTable = "src." + destTable.split("\\.")[1] + "_pt";
        // 匹配规则：例如 "tableName[1-2]"
        String regex1 = "(\\w+)\\[(\\d+-\\d+)\\]";
        // 匹配规则：例如 "212tableName2,223tableName2"
        String regex2 = "(\\d+(\\w+)[,]{0,1})+";

        // 是否涉及多张表（分区表）
        boolean isMulPartition = false;
        if (typeEnum == DriverTypeEnum.MySQL) {
            if (ReUtil.isMatch(regex1, sourceTable)) {
                isMulPartition = true;
                // 抽取基表名称
                sourceTable = ReUtil.get(regex1, sourceTable, 1);
            } else if (ReUtil.isMatch(regex2, sourceTable)) {
                isMulPartition = true;
                // 抽取基表名称
                sourceTable = ReUtil.get(regex2, sourceTable, 2);
            }
        }
        return buildMergeSql(isMulPartition, tmpTable, destTable, selectColumns, keyColumns, sourceTable);
    }

    /**
     * 生成merge_sql
     * @param isMulPartition 是否是分区表
     * @param tmpTable 临时表名称（带库名）
     * @param destTable 目标表名称（带库名）
     * @param selectColumns 筛选的列
     * @param keyColumns 主键列
     * @param sourceTable 源库
     * @return
     */
    private String buildMergeSql(boolean isMulPartition, String tmpTable, String destTable, String selectColumns, String keyColumns, String sourceTable) {
//        String[] columns = dataSourceService.getDBTableColumns(dataSourceId, sourceTable);
        List<String> columnList = Arrays.asList(selectColumns.split(","));
        // 筛选的列名
        String selectStr = StringUtils.join(columnList, "\n,") + "\n[,num]";
        // 筛选的带函数的列名
        String selectCoalesceStr = StringUtils.join(columnList.stream().map(e -> "coalesce(t1." + e + ", t2." + e + ") " + e).collect(Collectors.toList()), "\n,")
                + "\n[,coalesce(t1.num, t2.num) num]";
        //key连接表，例如"t1.id=t2.id"
        List<String> keyColumnList = Arrays.asList(keyColumns.split(",")).stream().map(e -> "t1." + e + "=t2." + e).collect(Collectors.toList());
        String keyCondition = StringUtils.join(keyColumnList, " and ");

        String templateSQL = "" +
                "[" +
                "set hive.exec.dynamic.partition=true;\n" +
                "set hive.exec.dynamic.partition.mode=nonstrict;\n" +
                "set hive.exec.max.dynamic.partitions.pernode=1000;" +
                "]\n" +

                "alter table %tmpTable drop if exists partition(pt<'${day-3d}');\n" +

                "insert overwrite table %tmpTable partition(pt='${day}' [,num]) \n" +
                "select %selectCoalesceStr" +
                "from \n" +
                "(select \n" +
                "%selectStr" +
                "from %hiveTable) t1 \n" +
                "full join \n" +
                "(select \n %selectStr" +
                "from %tmpTable where pt='${day-1d}') t2 \n" +
                "on %keyCondition [and t1.num=t2.num];\n" +

                "insert overwrite table %hiveTable [partition(num)]\n" +
                "select \n" +
                "%selectStr" +
                "from %tmpTable where pt='${day}';";
        String mergeSql = templateSQL
                .replaceAll("%tmpTable", tmpTable)
                .replaceAll("%hiveTable", destTable)
                .replaceAll("%selectStr", selectStr)
                .replaceAll("%selectCoalesceStr", selectCoalesceStr)
                .replaceAll("%keyCondition", keyCondition);
        if (isMulPartition) {
            //去掉'['和'
            return mergeSql.replaceAll("\\]", "").replaceAll("\\[", "");
        }
        // 去掉[]以及内部的内容
        return mergeSql.replaceAll("\\[[^]]*\\]", "");
    }

    private void checkJobContent(DIJobContentContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcReadMode()), "读取模式为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestDataSourceType()), "目标数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "目标数据源编号为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestTable()), "目标数据表为空");
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

}
