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

package cn.zhengcaiyun.idata.merge.data.manager;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobArgumentDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobConfigCombinationDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.util.DatasourceTool;
import cn.zhengcaiyun.idata.merge.data.util.JobMigrationContext;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

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

    @Transactional(rollbackFor = Exception.class)
    public List<MigrateResultDto> migrateJob(JobInfoDto jobInfoDto, JobConfigCombinationDto configCombinationDto,
                                             Operator jobOperator, JobMigrationDto migrationDto) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        try {
            // 保存job基本信息
            Long newJobId = jobInfoService.addJob(jobInfoDto, jobOperator);
            // 保存job配置信息
            JobConfigCombinationDto combinationDto = jobExecuteConfigService.save(newJobId, EnvEnum.prod.name(), configCombinationDto, jobOperator);
            // 保存job内容信息
            Operator contentOperator = getContentOperator(migrationDto.getOldJobContent());
            Integer contentVersion = migrateContentInfo(newJobId, jobInfoDto, migrationDto, contentOperator, resultDtoList);
            if (Objects.nonNull(contentVersion)) {
                // 提交作业内容
                jobContentCommonService.submit(newJobId, contentVersion, EnvEnum.prod.name(), "迁移自动提交", contentOperator);
                // 发布作业
                Optional<JobPublishRecord> publishRecordOptional = jobPublishRecordRepo.query(newJobId, contentVersion, EnvEnum.prod.name());
                if (publishRecordOptional.isPresent() && publishRecordOptional.get().getPublishStatus().equals(PublishStatusEnum.SUBMITTED.val)) {
                    jobPublishRecordService.approve(publishRecordOptional.get().getId(), "迁移自动发布", contentOperator);
                }
            } else {
                LOGGER.warn("*** *** migrateContentInfo of newJob:{},oldJob:{} failed.", newJobId, migrationDto.getOldJobId());
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
                contentVersion = migrateSQLContent(newJobId, jobInfoDto, migrationDto, contentOperator);
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
        // 岛端的 sql类型抽数作业需要标识出来，迁移后重新配置
        if (!"tableName".equals(old_source_type)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需处理：旧作业[%s]的source_type:[%s]是（非全量抽数），迁移完需要重新配置", migrationDto.getOldJobId().toString(), old_source_type), oldJobContent.toJSONString()));
            return null;
        }
        if (StringUtils.isBlank(old_source_table)) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需处理：旧作业[%s]的source_table为空，迁移完需要重新配置", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }
        if (old_source_table.indexOf("[") > 0) {
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需处理：旧作业[%s]的source_table为多表格式，暂不支持", migrationDto.getOldJobId().toString()), oldJobContent.toJSONString()));
            return null;
        }

        Optional<DataSource> srcDataSourceOptional = DatasourceTool.findDatasource(old_source_id, JobMigrationContext.getDataSourceListIfPresent());
        checkArgument(srcDataSourceOptional.isPresent(), "旧DI作业[%s]未找到迁移后的来源数据源", migrationDto.getOldJobId());
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

        Optional<DataSource> destDataSourceOptional = DatasourceTool.findDatasource(old_source_id, JobMigrationContext.getDataSourceListIfPresent());
        checkArgument(destDataSourceOptional.isPresent(), "旧DI作业[%s]未找到迁移后的目标数据源", migrationDto.getOldJobId().toString());
        DataSource destDataSource = destDataSourceOptional.get();
        // 数据去向-数据源类型
        contentDto.setDestDataSourceType(destDataSource.getType());
        // 数据去向-数据源id
        contentDto.setDestDataSourceId(destDataSource.getId());
        // 数据去向-数仓表id
        String[] target_tables = migrationDto.getOldJobInfo().getJSONArray("target_tables").toArray(new String[0]);
        //todo 已切换新规则表名，迁移逻辑如下
//        if (target_tables != null && target_tables.length > 0 && StringUtils.isNotBlank(target_tables[0])) {
//            contentDto.setDestTable(target_tables[0]);
//        } else {
//            contentDto.setDestTable(parseDestTable(srcDataSource, old_source_table));
//        }

        // 未切换新规则表名，迁移逻辑如下，统一使用新命名替换
        String newDestTable = parseDestTable(srcDataSource, old_source_table);
        if (target_tables != null && target_tables.length > 0 && StringUtils.isNotBlank(target_tables[0])) {
            String oldDestTable = target_tables[0];
            resultDtoList.add(new MigrateResultDto("migrateDIContent", String.format("迁移后需确认：旧作业[%s]:[%s]的旧目标表[%s]已自动改为新规则表名[%s]，需确认是否修改正确",
                    migrationDto.getOldJobId().toString(), jobInfoDto.getName(), oldDestTable, newDestTable), oldJobContent.toJSONString()));
        }
        contentDto.setDestTable(newDestTable);

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

        // 数据来源-字段信息
        List<MappingColumnDto> columnDtoList = jobTableService.getTableColumn(DataSourceTypeEnum.valueOf(contentDto.getSrcDataSourceType()), srcDataSource.getId(), old_source_table);
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

        DIJobContentContentDto saveContent = diJobContentService.save(newJobId, contentDto, contentOperator);
        return saveContent.getVersion();
    }

    private Integer migrateSQLContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto, Operator contentOperator) {
        SqlJobContentDto contentDto = new SqlJobContentDto();

        JSONObject oldJobContent = migrationDto.getOldJobContent();
        contentDto.setJobId(newJobId);
        Map<String, String> udfOldMappingNewIdMap = jobUdfService.load().stream()
                .collect(Collectors.toMap(jobUdf -> jobUdf.getUdfName().split("#_")[0], jobUdf -> jobUdf.getId().toString()));
        // 修改目标表表名
        String oldSourceSql = oldJobContent.getString("source_sql");
        contentDto.setSourceSql(changeTargetTblNameSql(oldSourceSql));
        if (oldJobContent.containsKey("udf_ids")) {
            String[] oldUdfIdArr = oldJobContent.getJSONArray("udf_ids").toArray(new String[0]);
            if (oldUdfIdArr != null && oldUdfIdArr.length > 0) {
                contentDto.setUdfIds(String.join(",", oldUdfIdArr));
            }
        }

        SqlJobContentDto sqlJobContentDto = sqlJobService.save(contentDto, contentOperator.getNickname());
        return sqlJobContentDto.getVersion();
    }

    // TODO 回流需靠job_config的target_id判断，暂将回流作业统一放入SQL作业中
    private Integer migrateBackFlowContent(Long newJobId, JobInfoDto jobInfoDto, JobMigrationDto migrationDto) {
        return null;
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
        return "ods.ods_" + dbName + "_" + sourceTable;
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
        List<String> sqlList = Arrays.stream(sourceSql.split(" ")).filter(t -> t.contains("ods_") && t.contains(".sync_"))
                .collect(Collectors.toList());
        Map<String, String> tblNameMap = new HashMap<>();
        for (String str : sqlList) {
            List<String> strSplitList = Arrays.stream(str.split("ods_|.sync_")).filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
            tblNameMap.put(str, "ods.ods_" + strSplitList.get(0) + "_" + strSplitList.get(1));
        }
        for (Map.Entry<String, String> values : tblNameMap.entrySet()) {
            sourceSql = sourceSql.replace(values.getKey(), values.getValue());
        }
        return sourceSql;
    }

    public static void main(String[] args) {

        String sql = "select id from ods_db_item.sync_zcy_item_copy_detail " +
                "left join ods_db_boss.sync_zcy_item_copy_detail " +
                "on ods_db_item.sync_zcy_item_copy_detail.id = ods_db_boss.sync_zcy_item_copy_detail.id";
//        String sql = "select * from ods_db_item.sync_zcy_item_copy_detail";
        String[] sqls = sql.split(" ");
        List<String> sqlList = Arrays.stream(sqls).filter(t -> t.contains("ods_") && t.contains(".sync_"))
                .collect(Collectors.toList());
        Map<String, String> tblNameMap = new HashMap<>();
        for (String str : sqlList) {
            List<String> strSplitList = Arrays.stream(str.split("ods_|.sync_")).filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
            tblNameMap.put(str, "ods.ods_" + strSplitList.get(0) + "_" + strSplitList.get(1));
        }
        for (Map.Entry<String, String> values : tblNameMap.entrySet()) {
            sql = sql.replace(values.getKey(), values.getValue());
        }
        System.out.println(sql);
    }
}
