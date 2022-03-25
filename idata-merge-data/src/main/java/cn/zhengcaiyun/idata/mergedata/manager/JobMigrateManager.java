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

package cn.zhengcaiyun.idata.mergedata.manager;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.develop.constant.enums.DiConfigModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import cn.zhengcaiyun.idata.develop.dto.job.JobArgumentDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.ScriptMergeSqlParamDto;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import cn.zhengcaiyun.idata.mergedata.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.mergedata.dto.JobMigrationDto;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.util.DatasourceTool;
import cn.zhengcaiyun.idata.mergedata.util.JobMigrationContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 10:27
 **/
@Component
public class JobMigrateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMigrateManager.class);

    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobPublishRecordService jobPublishRecordService;
    @Autowired
    private DIJobContentService diJobContentService;
    @Autowired
    private JobExecuteConfigService jobExecuteConfigService;
    @Autowired
    private JobContentCommonService jobContentCommonService;
    @Autowired
    private JobPublishRecordRepo jobPublishRecordRepo;
    @Autowired
    private JobTableService jobTableService;
    @Autowired
    private KylinJobService kylinJobService;
    @Autowired
    private SqlJobService sqlJobService;
    @Autowired
    private ScriptJobService scriptJobService;
    @Autowired
    private SparkJobService sparkJobService;
    @Autowired
    private OldIDataDao oldIDataDao;
    @Autowired
    private HdfsService hdfsService;
    @Autowired
    private JobUdfService jobUdfService;
    @Autowired
    private JobExecuteConfigRepo jobExecuteConfigRepo;
    @Autowired
    private DIJobContentRepo diJobContentRepo;
    @Autowired
    private KylinJobRepo kylinJobRepo;
    @Autowired
    private ScriptJobRepo scriptJobRepo;
    @Autowired
    private SparkJobRepo sparkJobRepo;
    @Autowired
    private SqlJobRepo sqlJobRepo;

    @Transactional(rollbackFor = Exception.class)
    public List<MigrateResultDto> migrateJob(EnvEnum envEnum, JobInfoDto jobInfoDto, JobConfigCombinationDto configCombinationDto,
                                             Operator jobOperator, JobMigrationDto migrationDto) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        try {
            // 保存job基本信息
            Long newJobId = jobInfoDto.getId();
            if (newJobId == null) {
                newJobId = jobInfoService.addJob(jobInfoDto, jobOperator);
            }

            // 根据各环境下的配置判断是否已迁移过
            Optional<JobExecuteConfig> existJobConfigOptional = jobExecuteConfigRepo.query(newJobId, envEnum.name());
            if (existJobConfigOptional.isPresent()) {
                LOGGER.warn("### ### 作业[{}]已存在，不需要再次迁移*****************!!!!!!!!!!!!!", jobInfoDto.getName());
                return resultDtoList;
            }

            // 保存job配置信息
            JobConfigCombinationDto combinationDto = jobExecuteConfigService.save(newJobId, envEnum.name(), configCombinationDto, jobOperator);
            // 保存job内容信息
            Operator contentOperator = getContentOperator(migrationDto.getOldJobContent());
            Integer contentVersion = migrateContentInfo(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
            if (Objects.nonNull(contentVersion)) {
                // 提交作业内容
                jobContentCommonService.submit(newJobId, contentVersion, envEnum.name(), "迁移自动提交", contentOperator);
                // 发布作业
                Optional<JobPublishRecord> publishRecordOptional = jobPublishRecordRepo.query(newJobId, contentVersion, envEnum.name());
                if (publishRecordOptional.isPresent() && publishRecordOptional.get().getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val)) {
                    jobPublishRecordService.approve(publishRecordOptional.get().getId(), "迁移自动发布", contentOperator);
                }
            } else {
                LOGGER.warn("*** *** migrateContentInfo of newJob:{}, oldJob:{} failed.", newJobId, migrationDto.getOldJobId());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有操作权限");
        }
        return resultDtoList;
    }

    private Integer migrateContentInfo(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                       Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        Integer contentVersion;
        switch (jobInfoDto.getJobType()) {
            case DI_BATCH:
                contentVersion = migrateDIContent(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
                break;
            case SQL_SPARK:
                contentVersion = migrateSQLContent(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
                break;
            case SPARK_PYTHON:
            case SPARK_JAR:
                contentVersion = migrateSparkContent(newJobId, jobInfoDto, migrationDto, contentOperator);
                break;
            case SCRIPT_PYTHON:
            case SCRIPT_SHELL:
                contentVersion = migrateScriptContent(newJobId, jobInfoDto, migrationDto, contentOperator);
                break;
            case KYLIN:
                contentVersion = migrateKylinContent(newJobId, jobInfoDto, migrationDto, contentOperator);
                break;
            case BACK_FLOW:
                contentVersion = migrateDorisBackFlowContent(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
            default:
                contentVersion = null;
        }
        return contentVersion;
    }

    private Integer migrateDIContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                     Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        DIJobContentContentDto contentDto = new DIJobContentContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        Long old_source_id = oldJobContent.getLong("source_id");
        String old_source_type = oldJobContent.getString("source_type");
        String old_source_table = oldJobContent.getString("source_table");
        String old_source_table_pk = oldJobContent.getString("source_table_pk");
        String old_source_sql = oldJobContent.getString("source_sql");
        Boolean old_is_recreate = oldJobContent.getBoolean("is_recreate");
        // sql类型抽数作业需要标识出来，迁移后重新配置
        if ("sql".equals(old_source_type)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("需处理：旧作业[%s]的source_type:是[%s]，需要修改后重迁作业", migrationDto.getOldJobId().toString(), old_source_type), oldJobContent.toJSONString()));
            return null;
        }
        if (StringUtils.isBlank(old_source_table)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("需处理：旧作业[%s]的source_table为空，需要修改后重迁或者迁移完重新配置DI作业", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }

        Optional<DataSource> srcDataSourceOptional = DatasourceTool.findDatasource(old_source_id, JobMigrationContext.getDataSourceListIfPresent());
        if (!srcDataSourceOptional.isPresent()) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("需处理：旧DI作业[%s]未找到迁移后的来源数据源，迁移后需人工处理", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
        DataSource srcDataSource = srcDataSourceOptional.get();
        // 数据来源-数据源类型
        contentDto.setSrcDataSourceType(srcDataSource.getType());
        // 数据来源-数据源id
        contentDto.setSrcDataSourceId(srcDataSource.getId());
        // 数据来源-读取模式，all：全量，incremental：增量
        if ("tableName".equals(old_source_type)) {
            contentDto.setSrcReadMode("all");
        } else {
            contentDto.setSrcReadMode("incremental");
        }
        // 数据来源-过滤条件
        contentDto.setSrcReadFilter("");
        // 数据来源-切分键
        contentDto.setSrcReadShardKey(StringUtils.defaultString(old_source_table_pk));
        // 数据来源-分片数量（并行度）
        contentDto.setSrcShardingNum(1);

        // 目标数据源
        Long old_dest_id = migrationDto.getOldJobConfig().getLong("target_id");
        Optional<DataSource> destDataSourceOptional = DatasourceTool.findDatasource(old_dest_id, JobMigrationContext.getDataSourceListIfPresent());
        if (!destDataSourceOptional.isPresent()) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需处理：旧DI作业[%s]未找到迁移后的目标数据源，迁移后需人工处理", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
        DataSource destDataSource = destDataSourceOptional.get();
        // 数据去向-数据源类型
        contentDto.setDestDataSourceType(destDataSource.getType());
        // 数据去向-数据源id
        contentDto.setDestDataSourceId(destDataSource.getId());
        // 数据去向-数仓表id
        JSONArray targetTableJsonArray = migrationDto.getOldJobInfo().getJSONArray("target_tables");
        String[] target_tables = Objects.isNull(targetTableJsonArray) ? null : targetTableJsonArray.toArray(new String[0]);
        // 已切换新规则表名，迁移逻辑如下
        if (target_tables != null && target_tables.length > 0 && StringUtils.isNotBlank(target_tables[0])) {
            contentDto.setDestTable(target_tables[0].trim());
        } else {
            contentDto.setDestTable(parseDestTable(srcDataSource, old_source_table));
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需确认：旧作业[%s]:[%s]的旧目标表为空，已自动生成新规则表名[%s]，请确认",
                    migrationDto.getOldJobId().toString(), jobInfoDto.getName(), contentDto.getDestTable()), oldJobContent.toJSONString()));
        }

        // 数据去向-写入模式，init: 重建表，override：覆盖表
        if (Objects.equals(Boolean.TRUE, old_is_recreate)) {
            contentDto.setDestWriteMode("init");
        } else {
            if ("increment".equalsIgnoreCase(old_source_type)) {
                contentDto.setDestWriteMode("append");
            } else {
                contentDto.setDestWriteMode("overwrite");
            }
        }

        // 数据去向-写入前语句
        contentDto.setDestBeforeWrite("");
        // 数据去向-写入后语句
        contentDto.setDestAfterWrite("");
        // 数据来源-表
        contentDto.setSrcTables(old_source_table);

        // 根据旧数据确定使用 可视化 或 脚本模式
        String di_table_info = oldJobContent.getString("di_table_info");
        Properties diTableProps;
        try {
            diTableProps = getDITableInfo(di_table_info);
        } catch (IOException ex) {
            LOGGER.warn("parse di_table_info to properties error", ex);
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的di_table_info字段不合法",
                    migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }

        contentDto.setConfigMode(DiConfigModeEnum.SCRIPT.value);

        contentDto.setScriptSelectColumns("");
        contentDto.setScriptKeyColumns("");
        contentDto.setSrcReadFilter("");
        contentDto.setSrcCols(Lists.newArrayList());
        contentDto.setDestCols(Lists.newArrayList());

        if ("increment".equalsIgnoreCase(old_source_type)) {
            // 增量
            if (diTableProps.containsKey("di.query")) {
                // 优先使用 di.query
                String di_query = diTableProps.getProperty("di.query");
                if (StringUtils.isBlank(di_query)) {
                    resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的di_query字段不合法",
                            migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                    return null;
                }
                di_query = di_query.trim().toLowerCase();
                String src_columns = di_query.substring(di_query.indexOf("select ") + 6, di_query.indexOf(" from ")).trim();
                int where_idx = di_query.indexOf(" where ");
                String src_where = null;
                if (where_idx > 0) {
                    src_where = di_query.substring(where_idx + 6).trim();
                }

                contentDto.setScriptSelectColumns(src_columns);
                contentDto.setScriptKeyColumns("id");
                ScriptMergeSqlParamDto scriptMergeSqlParamDto = new ScriptMergeSqlParamDto();
                scriptMergeSqlParamDto.setRecentDays(3);
                contentDto.setScriptMergeSqlParamDto(scriptMergeSqlParamDto);
                if (StringUtils.isNotBlank(src_where)) {
                    contentDto.setSrcReadFilter(src_where);
                }

            } else {
                if (diTableProps.containsKey("di.columns")) {
                    // di.column
                    String di_columns = diTableProps.getProperty("di.columns");
                    String di_condition = diTableProps.getProperty("di.condition");
                    if (StringUtils.isBlank(di_columns)) {
                        resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的di_columns字段不合法",
                                migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                        return null;
                    }
                    if (StringUtils.isBlank(di_condition)) {
                        resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]是增量抽数，但di.condition配置为空，迁移后需要处理",
                                migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                        contentDto.setSrcReadFilter("");
                    } else {
                        contentDto.setSrcReadFilter(di_condition.trim());
                    }
                    contentDto.setScriptSelectColumns(di_columns.trim());

                    contentDto.setScriptKeyColumns("id");
                    ScriptMergeSqlParamDto scriptMergeSqlParamDto = new ScriptMergeSqlParamDto();
                    scriptMergeSqlParamDto.setRecentDays(3);
                    contentDto.setScriptMergeSqlParamDto(scriptMergeSqlParamDto);
                } else {
                    resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的增量抽数配置不合法",
                            migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                    return null;
                }
            }
            resultDtoList.add(new MigrateResultDto("migrateIncrementDIContent", String.format("迁移增量DI作业：旧DI增量作业[%s]自动生成merge sql，需要确认生成结果是否正确",
                    migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
        } else {
            // 全量
            if (diTableProps.containsKey("di.columns") || diTableProps.containsKey("di.query")) {
                // 脚本模式
                if (diTableProps.containsKey("di.query")) {
                    // 优先使用 di.query
                    String di_query = diTableProps.getProperty("di.query");
                    if (StringUtils.isBlank(di_query)) {
                        resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的di_query字段不合法",
                                migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                        return null;
                    }
                    di_query = di_query.trim().toLowerCase();
                    String src_columns = di_query.substring(di_query.indexOf("select ") + 6, di_query.indexOf(" from ")).trim();
                    int where_idx = di_query.indexOf(" where ");
                    String src_where = null;
                    if (where_idx > 0) {
                        src_where = di_query.substring(where_idx + 6).trim();
                    }

                    contentDto.setScriptSelectColumns(src_columns);
//                    contentDto.setScriptKeyColumns("id");
//                    ScriptMergeSqlParamDto scriptMergeSqlParamDto = new ScriptMergeSqlParamDto();
//                    scriptMergeSqlParamDto.setRecentDays(3);
//                    contentDto.setScriptMergeSqlParamDto(scriptMergeSqlParamDto);
                    if (StringUtils.isNotBlank(src_where)) {
                        contentDto.setSrcReadFilter(src_where);
                    }

                } else {
                    // di.column
                    String di_columns = diTableProps.getProperty("di.columns");
                    if (StringUtils.isBlank(di_columns)) {
                        resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移DI作业内容报错：旧DI作业[%s]的di_columns 字段不合法",
                                migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                        return null;
                    }
                    contentDto.setScriptSelectColumns(di_columns.trim());
                }
            } else {
                // 可视化模式
                // 数据来源-字段信息
                String final_src_table = old_source_table;
                if (old_source_table.indexOf("[") > 0 && old_source_table.indexOf("]") > 0) {
                    // zcy_component_timeline_t_[0-63]
                    String base_table_name = old_source_table.substring(0, old_source_table.indexOf("["));
                    String table_idx_begin = old_source_table.substring(old_source_table.indexOf("[") + 1, old_source_table.lastIndexOf("-"));
                    final_src_table = base_table_name + table_idx_begin;
                }

                List<MappingColumnDto> columnDtoList = jobTableService.getTableColumn(DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType()), srcDataSource.getId(), final_src_table);
                List<MappingColumnDto> srcCols = new ArrayList<>();
                List<MappingColumnDto> destCols = new ArrayList<>();
                if (CollectionUtils.isEmpty(columnDtoList)) {
                    resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需处理：旧作业[%s]未获取到来源表字段信息，需手动重新获取", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                } else {
                    srcCols = columnDtoList;
                    columnDtoList.stream().forEach(srcColumnDto -> {
                        MappingColumnDto destColumnDto = new MappingColumnDto();
                        destColumnDto.setName(srcColumnDto.getName());
                        destColumnDto.setDataType(srcColumnDto.getDataType());
                        destColumnDto.setPrimaryKey(srcColumnDto.getPrimaryKey());
                        srcColumnDto.setMappedColumn(destColumnDto);
                        destCols.add(destColumnDto);
                    });
                }
                contentDto.setSrcCols(srcCols);
                contentDto.setDestCols(destCols);
                contentDto.setConfigMode(DiConfigModeEnum.VISIBLE.value);
            }
        }

        DIJobContentContentDto saveContent = diJobContentService.save(newJobId, contentDto, contentOperator);
        return saveContent.getVersion();
    }

    private Properties getDITableInfo(String di_table_info) throws IOException {
        Properties properties = new Properties();
        if (StringUtils.isNotBlank(di_table_info)) {
            try (ByteArrayInputStream input = new ByteArrayInputStream(di_table_info.getBytes("UTF-8"))) {
                properties.load(input);
            } catch (UnsupportedEncodingException ex) {
                LOGGER.warn("parse di_table_info error", ex);
            }
        }
        return properties;
    }

    private Integer migrateSQLContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                      Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        // 真线和预发是否同一个版本
        JSONArray oldEnvJsonArray = migrationDto.getOldJobContent().getJSONArray("status");
        List<String> oldEnvs = Lists.newArrayList(oldEnvJsonArray.toArray(new String[0]));
        boolean contentSameVersion = false;
        if (oldEnvs.size() == 2) {
            if (oldEnvs.contains("prod") && oldEnvs.contains("staging")) {
                contentSameVersion = true;
            }
        }
        if (contentSameVersion) {
            // 查询另一个环境是否已保存作业内容
            List<DevJobContentSql> contentSqlList = sqlJobRepo.queryList(newJobId);
            if (!CollectionUtils.isEmpty(contentSqlList)) {
                // 直接返回版本号，不需要再次保存作业内容
                return contentSqlList.get(0).getVersion();
            }
        }

        SqlJobContentDto contentDto = new SqlJobContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        contentDto.setJobId(newJobId);
        Map<String, String> udfOldMappingNewIdMap = jobUdfService.load().stream()
                .collect(Collectors.toMap(jobUdf -> jobUdf.getUdfName().split("#_")[0], jobUdf -> jobUdf.getId().toString()));
        // 修改目标表表名
        String oldSourceSql = oldJobContent.getString("source_sql");
        // 仅岛端需改写ods表名
//        String echoSourceSql = changeTargetTblNameSql(oldSourceSql);
//        if (echoSourceSql.contains("!ERROR!")) {
//            LOGGER.warn("******SQL作业迁移有误，错误作业ID：" + newJobId);
//            resultDtoList.add(new MigrateResultDto("migrateSqlContent",
//                    String.format("需处理：旧作业[%s]的source_sql改写失败，需要修改后重迁或者迁移完重新修改Sql作业",
//                            migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
//            return null;
//        }
//        contentDto.setSourceSql(echoSourceSql);
        contentDto.setSourceSql(oldSourceSql);
        if (oldJobContent.containsKey("udf_ids")) {
            JSONArray udfIdJsonArray = oldJobContent.getJSONArray("udf_ids");
            String[] oldUdfIdArr = Objects.isNull(udfIdJsonArray) ? null : udfIdJsonArray.toArray(new String[0]);
            if (oldUdfIdArr != null && oldUdfIdArr.length > 0) {
                contentDto.setUdfIds(String.join(",", oldUdfIdArr));
            }
        }

        SqlJobContentDto sqlJobContentDto = sqlJobService.save(contentDto, contentOperator.getNickname());
        return sqlJobContentDto.getVersion();
    }

    // 暂将非doris的回流作业统一放入SQL作业中
    private Integer migrateBackFlowContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
    }

    private Integer migrateDorisBackFlowContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto,
                                                Operator contentOperator, List<MigrateResultDto> resultDtoList) {
        // 真线和预发是否同一个版本
        JSONArray oldEnvJsonArray = migrationDto.getOldJobContent().getJSONArray("status");
        List<String> oldEnvs = Lists.newArrayList(oldEnvJsonArray.toArray(new String[0]));
        boolean contentSameVersion = false;
        if (oldEnvs.size() == 2) {
            if (oldEnvs.contains("prod") && oldEnvs.contains("staging")) {
                contentSameVersion = true;
            }
        }
        if (contentSameVersion) {
            // 查询另一个环境是否已保存作业内容
            List<DIJobContent> contentList = diJobContentRepo.queryList(newJobId);
            if (!CollectionUtils.isEmpty(contentList)) {
                // 直接返回版本号，不需要再次保存作业内容
                return contentList.get(0).getVersion();
            }
        }

        DIJobContentContentDto contentDto = new DIJobContentContentDto();
        contentDto.setConfigMode(DiConfigModeEnum.VISIBLE.value);

        JSONObject oldJobContent = migrationDto.getOldJobContent();

        Long hive_data_source_id = 54L;
        Optional<DataSource> srcDataSourceOptional = DatasourceTool.findDatasource(hive_data_source_id, JobMigrationContext.getDataSourceListIfPresent());
        if (!srcDataSourceOptional.isPresent()) {
            resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("需处理：旧DI作业[%s]未找到迁移后的来源Hive数据源，迁移后需人工处理", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
        DataSource srcDataSource = srcDataSourceOptional.get();
        // 数据来源-数据源类型
        contentDto.setSrcDataSourceType(srcDataSource.getType());
        // 数据来源-数据源id
        contentDto.setSrcDataSourceId(srcDataSource.getId());
        // 数据来源-读取模式，all：全量
        contentDto.setSrcReadMode("");
        // 数据来源-过滤条件
        contentDto.setSrcReadFilter("");
        // 数据来源-切分键
        contentDto.setSrcReadShardKey("");
        // 数据来源-分片数量（并行度）
        contentDto.setSrcShardingNum(1);

        // 目标数据源
        Long old_dest_id = migrationDto.getOldJobConfig().getLong("target_id");
        Optional<DataSource> destDataSourceOptional = DatasourceTool.findDatasource(old_dest_id, JobMigrationContext.getDataSourceListIfPresent());
        if (!destDataSourceOptional.isPresent()) {
            resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("迁移后需处理：旧DI作业[%s]未找到迁移后的目标数据源，迁移后需人工处理", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
        DataSource destDataSource = destDataSourceOptional.get();
        // 数据去向-数据源类型
        contentDto.setDestDataSourceType(destDataSource.getType());
        // 数据去向-数据源id
        contentDto.setDestDataSourceId(destDataSource.getId());
        // 数据去向-数仓表id
        JSONArray targetTableJsonArray = migrationDto.getOldJobInfo().getJSONArray("target_tables");
        String[] target_tables = Objects.isNull(targetTableJsonArray) ? null : targetTableJsonArray.toArray(new String[0]);
        // 已切换新规则表名，迁移逻辑如下
        if (target_tables != null && target_tables.length > 0 && StringUtils.isNotBlank(target_tables[0])) {
            contentDto.setDestTable(target_tables[0].trim());
        } else {
            resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("迁移后需处理：旧Doris回流作业[%s]目标表为空，迁移后需人工处理", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }

        // 数据去向-写入模式，init: 重建表，override：覆盖表
        String save_mode = Strings.emptyToNull(oldJobContent.getString("save_mode"));
        save_mode = StringUtils.defaultString(save_mode, "OVERWRITE");
        contentDto.setDestWriteMode(save_mode.toUpperCase());

        // 数据去向-写入前语句
        contentDto.setDestBeforeWrite("");
        // 数据去向-写入后语句
        contentDto.setDestAfterWrite("");
        contentDto.setDestBulkNum(200L);
        contentDto.setDestShardingNum(1);

        String old_introduce_condition = migrationDto.getOldJobConfig().getString("introduce_condition");
        if (StringUtils.isBlank(old_introduce_condition)) {
            resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("需处理：旧Doris回流作业[%s]的introduce_condition字段为空", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }

        Map<String, String> destPropMap = Maps.newHashMap();
        Splitter.on(",").trimResults().omitEmptyStrings()
                .splitToList(old_introduce_condition)
                .stream().forEach(temp_condition -> {
                    List<String> cond_array = Splitter.on("=").trimResults().omitEmptyStrings().splitToList(temp_condition);
                    destPropMap.put(cond_array.get(0), cond_array.get(1));
                });
//        contentDto.setDestPropertyMap(destPropMap);

        String old_source_table = destPropMap.get("sourceTable");
        String old_introduce_columns = migrationDto.getOldJobConfig().getString("introduce_columns");
        String old_source_sql = oldJobContent.getString("source_sql");
        if (StringUtils.isBlank(old_source_sql)) {
            resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("需处理：旧Doris回流作业[%s]的source_sql字段为空", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
//        old_introduce_columns = old_introduce_columns.replace("\\n", " ").trim();
        List<String> destCols = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(old_introduce_columns);
        old_source_sql = old_source_sql.trim().replace("\\n", " ");
        old_source_sql = old_source_sql.trim().replace("\n", " ");
        old_source_sql = old_source_sql.trim().replace("\\t", " ");
        old_source_sql = old_source_sql.trim().replace("\t", " ");

        List<String> srcCols = Lists.newArrayList();
        if (StringUtils.isNotBlank(old_source_table)) {
            for (String destCol : destCols) {
                srcCols.add(destCol);
            }
        } else {
            String src_columns_str = old_source_sql.substring(old_source_sql.indexOf("select ") + 6, old_source_sql.indexOf("from "));
//        src_columns_str = src_columns_str.replace("\\n", " ").trim();
            Splitter.on(",").trimResults().omitEmptyStrings()
                    .splitToList(src_columns_str)
                    .stream().forEach(temp_column -> {
                        String column = temp_column;
                        if (temp_column.indexOf(".") > 0) {
                            column = column.substring(temp_column.indexOf(".") + 1);
                        }
                        if (column.indexOf("--") > 0) {
                            column = column.substring(0, column.indexOf("--"));
                        }
                        srcCols.add(column.trim());
                    });
            if (!org.apache.commons.collections.CollectionUtils.isEqualCollection(srcCols, destCols)) {
                resultDtoList.add(new MigrateResultDto("migrateDorisContent", String.format("需处理：旧Doris回流作业[%s]的回流字段解析失败", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
                return null;
            }
        }

        List<MappingColumnDto> srcColumns = new ArrayList<>();
        List<MappingColumnDto> destColumns = new ArrayList<>();
        for (int i = 0; i < srcCols.size(); i++) {
            MappingColumnDto srcColumnDto = new MappingColumnDto();
            srcColumnDto.setName(srcCols.get(i));
            srcColumnDto.setDataType("");
            srcColumnDto.setPrimaryKey(false);

            MappingColumnDto destColumnDto = new MappingColumnDto();
            destColumnDto.setName(destCols.get(i));
            destColumnDto.setDataType("");
            destColumnDto.setPrimaryKey(false);

            srcColumnDto.setMappedColumn(destColumnDto);

            srcColumns.add(srcColumnDto);
            destColumns.add(destColumnDto);
        }
        contentDto.setSrcCols(srcColumns);
        contentDto.setDestCols(destColumns);

        if (StringUtils.isBlank(old_source_table)) {
            String old_src_tbl = old_source_sql.substring(old_source_sql.indexOf("from ") + 5);
            if (old_src_tbl.indexOf("where ") > 0) {
                old_src_tbl = old_src_tbl.substring(0, old_src_tbl.indexOf("where "));
            }
            old_source_table = old_src_tbl.trim();
        }
        // 数据来源-表
        contentDto.setSrcTables(old_source_table);

        if (old_source_sql.indexOf("where ") > 0) {
            contentDto.setSrcReadFilter(old_source_sql.substring(old_source_sql.indexOf("where ") + 6).trim());
        }

        DIJobContentContentDto saveContent = diJobContentService.save(newJobId, contentDto, contentOperator);
        return saveContent.getVersion();
    }


    private Integer migrateKylinContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto, Operator contentOperator) {
        KylinJobDto contentDto = new KylinJobDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        contentDto.setJobId(newJobId);
        contentDto.setCubeName(oldJobContent.getString("cube_name"));
        contentDto.setBuildType(oldJobContent.getString("build_type"));
        if (StringUtils.isNotEmpty(oldJobContent.getString("start_time"))) {
            contentDto.setStartTime(changeStringToDate(oldJobContent.getString("start_time")));
        }
        if (StringUtils.isNotEmpty(oldJobContent.getString("end_time"))) {
            contentDto.setEndTime(changeStringToDate(oldJobContent.getString("end_time")));
        }

        KylinJobDto kylinJobDto = kylinJobService.save(contentDto, contentOperator.getNickname());
        return kylinJobDto.getVersion();
    }

    private Integer migrateSparkContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto, Operator contentOperator) {
        SparkJobContentDto contentDto = new SparkJobContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        contentDto.setJobId(newJobId);
        contentDto.setJobType(jobInfoDto.getJobType());
        contentDto.setMainClass(oldJobContent.getString("main_class"));
        String oldFileResourceId = oldJobContent.getString("resource_id");
        String hdfsPath = fetchOldResource(Long.valueOf(oldFileResourceId)).get(0).getString("hdfs_path");
        String oldAppType = oldJobContent.getString("app_type");
        // 校验hdfs文件是否存在
        String output;
        try {
            ByteArrayOutputStream sos = new ByteArrayOutputStream();
            hdfsService.readFile(hdfsPath, sos);
            output = sos.toString();
            sos.close();
            // 文件不存在打印日志，传入hdfs路径为空串
            if (StringUtils.isEmpty(output) || "NULL".equals(output.toUpperCase())) {
                LOGGER.info("Spark作业同步失败，HDFS文件有误，作业ID：[{}]，作业名称：{}", jobInfoDto.getId(), jobInfoDto.getName());
                contentDto.setResourceHdfsPath("wrong hdfs path");
            } else {
                if ("Jar".equals(oldAppType)) {
                    contentDto.setResourceHdfsPath(hdfsPath);
                } else {
                    contentDto.setPythonResource(output);
                }
            }
        } catch (IOException e) {
            int end = (e.getMessage() + "").indexOf("\n");
            throw new RuntimeException((e.getMessage() + "").substring(0, end > 0 ? end : Integer.MAX_VALUE));
        }
        if (StringUtils.isNotEmpty(oldJobContent.getString("app_arguments"))) {
            List<String> oldAppArguments = Arrays.asList(oldJobContent.getString("app_arguments").split(" "));
            List<JobArgumentDto> appArgumentList = oldAppArguments.stream().map(oldAppArgument -> {
                JobArgumentDto argumentDto = new JobArgumentDto();
                argumentDto.setArgumentValue(oldAppArgument);
                return argumentDto;
            }).collect(Collectors.toList());
            contentDto.setAppArguments(appArgumentList);
        }

        SparkJobContentDto sparkJobContentDto = new SparkJobContentDto();
        try {
            sparkJobContentDto = sparkJobService.save(contentDto, contentOperator.getNickname());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sparkJobContentDto.getVersion();
    }

    private Integer migrateScriptContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto, Operator contentOperator) {
        ScriptJobContentDto contentDto = new ScriptJobContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        contentDto.setJobId(newJobId);
        String oldFileResourceId = oldJobContent.getString("resource_id");
        String hdfsPath = fetchOldResource(Long.valueOf(oldFileResourceId)).get(0).getString("hdfs_path");
        // 校验hdfs文件是否存在
        String output;
        try {
            ByteArrayOutputStream sos = new ByteArrayOutputStream();
            hdfsService.readFile(hdfsPath, sos);
            output = sos.toString();
            sos.close();
            // 文件不存在打印日志，传入hdfs路径为空串
            if (StringUtils.isEmpty(output) || "NULL".equals(output.toUpperCase())) {
                LOGGER.info("Script作业同步失败，HDFS文件有误，作业ID：[{}]，作业名称：{}", jobInfoDto.getId(), jobInfoDto.getName());
                contentDto.setSourceResource("script wrong");
            } else {
                contentDto.setSourceResource(output);
            }
        } catch (IOException e) {
            int end = (e.getMessage() + "").indexOf("\n");
            throw new RuntimeException((e.getMessage() + "").substring(0, end > 0 ? end : Integer.MAX_VALUE));
        }
        if (StringUtils.isNotEmpty(oldJobContent.getString("script_arguments"))) {
            List<String> oldScriptArguments = Arrays.asList(oldJobContent.getString("script_arguments").split(" "));
            List<JobArgumentDto> scriptArgumentList = oldScriptArguments.stream().map(oldAppArgument -> {
                JobArgumentDto argumentDto = new JobArgumentDto();
                argumentDto.setArgumentValue(oldAppArgument);
                return argumentDto;
            }).collect(Collectors.toList());
            contentDto.setScriptArguments(scriptArgumentList);
        }

        ScriptJobContentDto scriptJobContentDto = scriptJobService.save(contentDto, contentOperator.getNickname());
        return scriptJobContentDto.getVersion();
    }

    private String parseDestTable(DataSource srcDataSource, String sourceTable) {
        DbConfigDto dbConfigDto = getDbConfigDto(srcDataSource);
        String dbName = dbConfigDto.getDbName();
        String tableName = sourceTable;
        if (DataSourceTypeEnum.postgresql.name().equals(srcDataSource.getType())) {
            int dot_idx = sourceTable.indexOf(".");
            if (dot_idx > 0) {
                tableName = sourceTable.substring(dot_idx + 1);
            }
        }
        return "ods.ods_" + dbName + "_" + tableName;
    }

    private DbConfigDto getDbConfigDto(DataSource srcDataSource) {
        DataSourceDto dto = DataSourceDto.from(srcDataSource);
        return dto.getDbConfigList().stream()
                .filter(dbConfigDto -> dbConfigDto.getEnv() == EnvEnum.prod)
                .findFirst().get();
    }

    private Operator getContentOperator(JSONObject oldJobContent) {
        String old_creator = oldJobContent.getString("creator");
        String old_editor = oldJobContent.getString("editor");
        String nickname = StringUtils.isNotEmpty(old_creator) ? old_creator : old_editor;
        return new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();
    }

    private Date changeStringToDate(String dateTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateTimeStr);
        } catch (Exception ignore) {
        }
        return date;
    }

    private List<JSONObject> fetchOldResource(Long resourceId) {
        List<String> columns = Lists.newArrayList("resource_type", "hdfs_path");
        String filter = "del = false and id = " + resourceId;
        return oldIDataDao.queryList("metadata.file_resource", columns, filter);
    }

    private String changeTargetTblNameSql(String sourceSql) {
        // 兼容空格、注释、逗号和换行符
        List<String> sqlList = Arrays.stream(sourceSql.split(" |,|--|\n")).filter(t -> t.contains("ods_") && t.contains(".sync_"))
                .collect(Collectors.toList());
        Map<String, String> tblNameMap = new HashMap<>();
        for (String str : sqlList) {
            List<String> strSplitList = Arrays.stream(str.split("ods_|\\.sync_"))
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
            if (strSplitList.size() == 2) {
                tblNameMap.put(str, "ods.ods_" + strSplitList.get(0) + "_" + strSplitList.get(1));
            } else if (strSplitList.size() == 3) {
                tblNameMap.put(str, "ods.ods_" + strSplitList.get(0) + "_" + strSplitList.get(1) + "ods_" + strSplitList.get(2));
            } else {
                tblNameMap.put(str, "!ERROR!");
            }
        }
        for (Map.Entry<String, String> values : tblNameMap.entrySet()) {
            sourceSql = sourceSql.replace(values.getKey(), values.getValue());
        }
        return sourceSql;
    }

    public static void main(String[] args) {
        String sql = "with s_ca as (\n" +
                "select category_id, district_code, concat('{', categorys ,'}') as ca_json  \n" +
                "  from (\n" +
                "    select category_id, district_code, explode(split(replace(replace(purchase_category, '[{'), '}]'), '\\\\},\\\\{' )) as categorys\n" +
                "      from ods_db_fixed_universal.sync_tenant_open\n" +
                "     where status = 1\n" +
                "))\n" +
                "\n" +
                "select a.id, \n" +
                "       a.layer,\n" +
                "       c.category_id, \n" +
                "       d.district_code, \n" +
                "       get_json_object(d.ca_json, '$.id')   as node_id,\n" +
                "       get_json_object(d.ca_json, '$.code') as ca_code,\n" +
                "       get_json_object(d.ca_json, '$.name') as ca_name,\n" +
                "       ''                                   as is_central\n" +
                "  from ods_db_item.sync_parana_items a \n" +
                "  join ods_db_agreement.sync_ag_protocol_goods_mapper b \n" +
                "    on a.id = b.goods_id\n" +
                "  join ods_db_fixed_universal.sync_service_item c \n" +
                "    on b.bid_id = c.id\n" +
                "  join s_ca d \n" +
                "    on c.category_id = d.category_id\n" +
                " where a.layer = 125\n" +
                "\n" +
                " union all\n" +
                " \n" +
                "select a.id, \n" +
                "       a.layer, \n" +
                "       a.category_id, \n" +
                "       a.district_code, \n" +
                "       d.id                                 as node_id, \n" +
                "       d.code                               as ca_code, \n" +
                "       d.name                               as ca_name, \n" +
                "       d.type_code                          as is_central\n" +
                "  from dim.dim_item_sales_district a \n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatlog_t b \n" +
                "    on a.district_code = b.district_code and b.year = year(current_date())\n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatalog_map_t c \n" +
                "    on b.id = c.gp_catalog_id and a.category_id = c.prd_catalog_node_id and c.is_deprecated = 0\n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatalog_node_t d \n" +
                "    on c.gp_catalog_node_id = d.node_id and d.is_deprecated = 0\n" +
                " where a.layer != 125\n" +
                " \n" +
                " union all\n" +
                " \n" +
                "select a.id, \n" +
                "       a.layer, \n" +
                "       a.category_id, \n" +
                "       a.district_code, \n" +
                "       d.id                                 as node_id, \n" +
                "       d.code                               as ca_code, \n" +
                "       d.name                               as ca_name, \n" +
                "       d.type_code                          as is_central\n" +
                "  from dim.dim_item_sales_district a \n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatlog_t b \n" +
                "    on a.district_code = b.district_code and b.year = year(current_date())\n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatalog_map_t c \n" +
                "    on b.id = c.gp_catalog_id and a.category_id = c.prd_catalog_node_id and c.is_deprecated = 0\n" +
                "  left join ods_db_gpcatalog.sync_zcy_gpcatalog_node_t d \n" +
                "    on c.gp_catalog_node_id = d.node_id and d.is_deprecated = 0\n" +
                " where a.layer is null\n";
//        String sql = "ON cs.org_id = gr.grade_org_id\n" +
//                "WHERE gr.actual_grade_score < 0";
        String[] sqls = sql.split(" |,|--|\n");
        List<String> sqlList = Arrays.stream(sqls).filter(t -> t.contains("ods_") && t.contains(".sync_"))
                .collect(Collectors.toList());
        Map<String, String> tblNameMap = new HashMap<>();
        for (String str : sqlList) {
            List<String> strSplitList = Arrays.stream(str.split("ods_|\\.sync_"))
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
            String newTblName = "ods.ods_" + strSplitList.get(0) + "_" + strSplitList.get(1);
            if (strSplitList.size() == 2) {
                tblNameMap.put(str, newTblName);
            } else if (strSplitList.size() == 3) {
                tblNameMap.put(str, newTblName + "ods_" + strSplitList.get(2));
            }
        }
        for (Map.Entry<String, String> values : tblNameMap.entrySet()) {
            sql = sql.replace(values.getKey(), values.getValue());
        }
        System.out.println(sql);
    }
}
