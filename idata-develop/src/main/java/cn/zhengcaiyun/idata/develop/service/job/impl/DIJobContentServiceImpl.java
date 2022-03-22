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
import cn.zhengcaiyun.idata.develop.helper.rule.DIRuleHelper;
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
        DIRuleHelper.checkJobContent(contentDto);

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
        Integer configMode = contentDto.getConfigMode();
        String srcReadFilter = contentDto.getSrcReadFilter();
        List<MappingColumnDto> srcCols = contentDto.getSrcCols();
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType());

        // 是否设置
        boolean buildMergeSql = DIRuleHelper.buildMergeSql(jobType, contentDto.getSrcReadMode());
        boolean buildQuery = DIRuleHelper.buildQuery(jobType, configMode, srcReadFilter, srcCols, dataSourceTypeEnum);

        String destTable = contentDto.getDestTable();
        String srcTables = contentDto.getSrcTables();
        switch (DiConfigModeEnum.getByValue(configMode)) {
            case VISIBLE:

                // 设置SrcQuery
                if (buildQuery) {
                    String srcQuery = DIRuleHelper.generateSrcQuery(srcCols, srcReadFilter, srcTables);
                    contentDto.setSrcQuery(srcQuery);
                }

                // 设置MergeSql
                if (buildMergeSql) {
                    List<String> visColumnList = srcCols.stream().map(e -> e.getName()).collect(Collectors.toList());
                    List<String> visKeyColumnList = srcCols.stream().filter(e -> e.getPrimaryKey()).map(e -> e.getName()).collect(Collectors.toList());
                    String visKeys = "id";
                    if (CollectionUtils.isNotEmpty(visKeyColumnList)) {
                        visKeys = StringUtils.join(visKeyColumnList, ",");
                    }
                    String mergeSql = DIRuleHelper.generateMergeSql(visColumnList, visKeys, srcTables, destTable, dataSourceTypeEnum, 3);
                    contentDto.setMergeSql(mergeSql);
                }
                break;
            case SCRIPT:
                String scriptSelectColumns = contentDto.getScriptSelectColumns();

                // 设置SrcQuery
                if (buildQuery) {
                    String scriptQuery = DIRuleHelper.generateScriptQuery(scriptSelectColumns, srcReadFilter, srcTables);
                    contentDto.setScriptQuery(scriptQuery);
                }

                if (buildMergeSql && StringUtils.isNotEmpty(scriptSelectColumns)) {
                    // 设置ScriptMergeSql
                    ScriptMergeSqlParamDto scriptMergeSqlParamDto = contentDto.getScriptMergeSqlParamDto();
                    int days = 3;
                    if (scriptMergeSqlParamDto != null && scriptMergeSqlParamDto.getRecentDays() != null) {
                        days = scriptMergeSqlParamDto.getRecentDays();
                    }
                    List<String> scriptColumnList = Arrays.asList(scriptSelectColumns.split(","));
                    String scriptKeys = "id";
                    if (StringUtils.isNotEmpty(contentDto.getScriptKeyColumns())) {
                        scriptKeys = contentDto.getScriptKeyColumns();
                    }
                    String scriptMergeSql = DIRuleHelper.generateMergeSql(scriptColumnList, scriptKeys, srcTables, destTable, dataSourceTypeEnum, days);
                    contentDto.setScriptMergeSql(scriptMergeSql);
                }
                break;
        }
    }

    @Override
    public DIJobContentContentDto get(Long jobId, Integer version) {
        checkArgument(Objects.nonNull(jobId), "作业编号为空");
        checkArgument(Objects.nonNull(version), "作业版本号为空");

        Optional<DIJobContent> jobContentOptional = diJobContentRepo.query(jobId, version);
        checkArgument(jobContentOptional.isPresent(), "作业版本不存在");
        return DIJobContentContentDto.from(jobContentOptional.get());
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

}
