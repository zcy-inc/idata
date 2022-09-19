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

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivySessionDto;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.SqlJobRepo;
import cn.zhengcaiyun.idata.develop.dto.job.sql.DryRunDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.FlinkSqlJobExtendConfigDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobExternalTableDto;
import cn.zhengcaiyun.idata.develop.service.job.SqlJobService;
import cn.zhengcaiyun.idata.develop.util.FlinkSqlUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-11-22 上午10:38
 */

@Service
public class SqlJobServiceImpl implements SqlJobService {

    @Value("${dev.job.dryRunPyFile}")
    private String LIVY_DRY_RUN_PY_FILE;

    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private SqlJobRepo sqlJobRepo;
    @Autowired
    private LivyService livyService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public SqlJobContentDto save(SqlJobContentDto sqlJobDto, String operator) {
        checkArgument(sqlJobDto.getJobId() != null, "作业Id不能为空");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(sqlJobDto.getJobId());
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        if (CollectionUtils.isNotEmpty(sqlJobDto.getExtTables())) {
            checkExtTables(sqlJobDto.getExtTables());
        }

        Integer version = sqlJobDto.getVersion();
        boolean startNewVersion = false;
        if (Objects.nonNull(version)) {
            DevJobContentSql existJobContentSql = sqlJobRepo.query(sqlJobDto.getJobId(), version);
            checkArgument(existJobContentSql != null, "作业不存在或已删除");
            SqlJobContentDto existSqlJob = SqlJobContentDto.from(existJobContentSql);

            // 不可修改且跟当前版本不一致才新生成版本
            if (existSqlJob.getEditable().equals(EditableEnum.NO.val) && !sqlJobDto.equals(existSqlJob)) {
                startNewVersion = true;
            } else {
                if (existJobContentSql.getEditable().equals(EditableEnum.YES.val)) {
                    DevJobContentSql jobContentSql = sqlJobDto.toModel();
                    jobContentSql.setId(existJobContentSql.getId());
                    jobContentSql.setEditor(operator);
                    sqlJobRepo.update(jobContentSql);
                }
            }
        } else {
            startNewVersion = true;
        }

        if (startNewVersion) {
            DevJobContentSql jobContentSql = PojoUtil.copyOne(sqlJobDto, DevJobContentSql.class,
                    "jobId", "sourceSql", "udfIds");
            if (Objects.nonNull(sqlJobDto.getExtConfig())) {
                jobContentSql.setExtendConfigs(new Gson().toJson(sqlJobDto.getExtConfig()));
            }
            if (Objects.nonNull(sqlJobDto.getExtTables())) {
                jobContentSql.setExternalTables(JSON.toJSONString(sqlJobDto.getExtTables()));
            } else {
                jobContentSql.setExternalTables("");
            }
            version = sqlJobRepo.newVersion(sqlJobDto.getJobId());
            jobContentSql.setVersion(version);
            jobContentSql.setEditable(EditableEnum.YES.val);
            jobContentSql.setCreator(operator);
            sqlJobRepo.add(jobContentSql);
        }

        return find(sqlJobDto.getJobId(), version);
    }

    @Override
    public SqlJobContentDto find(Long jobId, Integer version) {
        DevJobContentSql jobContentSql = sqlJobRepo.query(jobId, version);
        checkArgument(jobContentSql != null, "作业不存在");
        return SqlJobContentDto.from(jobContentSql);
    }

    @Override
    public String generateFlinkSqlTemplate(List<FlinkSqlJobExtendConfigDto.FlinkDataSourceConfigDto> flinkSourceConfigs,
                                           List<FlinkSqlJobExtendConfigDto.FlinkDataSourceConfigDto> flinkSinkConfigs) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(flinkSourceConfigs)) {
            sqlBuilder.append("-- source table template ").append("\n");
            flinkSourceConfigs.stream()
                    .forEach(config -> sqlBuilder.append(FlinkSqlUtil.generateTemplate(config.getDataSourceType(), StringUtils.isBlank(config.getDataSourceUDCode()) ? config.getDataSourceId().toString() : config.getDataSourceUDCode(), false))
                            .append("\n\n"));
        }
        if (CollectionUtils.isNotEmpty(flinkSinkConfigs)) {
            sqlBuilder.append("-- sink table template ").append("\n");
            flinkSinkConfigs.stream()
                    .forEach(config -> sqlBuilder.append(FlinkSqlUtil.generateTemplate(config.getDataSourceType(), StringUtils.isBlank(config.getDataSourceUDCode()) ? config.getDataSourceId().toString() : config.getDataSourceUDCode(), true))
                            .append("\n\n"));
        }
        sqlBuilder.append("-- business code ").append("\n");
        return sqlBuilder.toString();
    }

    @Override
    public LivySessionDto sqlJobDryRun(DryRunDto dryRunDto) {
        checkArgument(dryRunDto.getJobId() != null, "作业ID不能为空");
        checkArgument(dryRunDto.getJobVersion() != null, "作业版本不能为空");
        JobInfo existJobInfo = jobInfoRepo.queryJobInfo(dryRunDto.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("作业不存在"));
        DevJobContentSql existSqlJob = sqlJobRepo.query(dryRunDto.getJobId(), dryRunDto.getJobVersion());
        checkArgument(existSqlJob != null, "作业不存在");

        List<String> args = Arrays.asList(dryRunDto.getJobId().toString(), dryRunDto.getJobVersion().toString());
        return livyService.createBatches(LIVY_DRY_RUN_PY_FILE, args);
    }

    private void checkExtTables(List<SqlJobExternalTableDto> extTables) {
        for (SqlJobExternalTableDto externalTableDto : extTables) {
            checkArgument(StringUtils.isNotBlank(externalTableDto.getDataSourceType()), "外部表 - 数据源类型为空");
            checkArgument(Objects.nonNull(externalTableDto.getDataSourceId()), "外部表 - 数据源为空");
            List<SqlJobExternalTableDto.ExternalTableInfo> tables = externalTableDto.getTables();
            checkArgument(CollectionUtils.isNotEmpty(tables), "外部表 - 表配置为空");
            for (SqlJobExternalTableDto.ExternalTableInfo tableInfo : tables) {
                checkArgument(StringUtils.isNotBlank(tableInfo.getTableName()), "外部表 - 表名为空");
                checkArgument(tableInfo.getTableName().indexOf(".") > 0, "外部表 - 表名未包含库名");
                checkArgument(StringUtils.isNotBlank(tableInfo.getTableAlias()), "外部表 - 表别名为空");
            }
        }
    }

}
