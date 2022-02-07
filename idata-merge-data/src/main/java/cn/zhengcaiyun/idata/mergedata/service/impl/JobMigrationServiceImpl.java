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

package cn.zhengcaiyun.idata.mergedata.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.*;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.mergedata.dal.dao.MigrateResultDao;
import cn.zhengcaiyun.idata.mergedata.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.mergedata.dto.JobMigrationDto;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.manager.JobMigrateManager;
import cn.zhengcaiyun.idata.mergedata.service.JobMigrationService;
import cn.zhengcaiyun.idata.mergedata.util.*;
import cn.zhengcaiyun.idata.mergedata.util.*;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-06 16:09
 **/
@Service
public class JobMigrationServiceImpl implements JobMigrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMigrationServiceImpl.class);

    @Autowired
    private OldIDataDao oldIDataDao;
    @Autowired
    private DAGRepo dagRepo;
    @Autowired
    private CompositeFolderRepo compositeFolderRepo;
    @Autowired
    private DataSourceRepo dataSourceRepo;
    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private JobExecuteConfigRepo jobExecuteConfigRepo;
    @Autowired
    private JobMigrateManager jobMigrateManager;
    @Autowired
    private MigrateResultDao migrateResultDao;

    @Override
    public List<MigrateResultDto> migrate() {
        LOGGER.info("*** *** 开始作业数据迁移... ... *** ***");
        // 只迁移真线已发布的版本作业
        // 获取旧版IData所有作业数据
        List<JobMigrationDto> migrationDtoList = Lists.newArrayList();
        MutableGraph<Long> jobGraph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .expectedNodeCount(5000)
                .build();
        fetchOldJobData(migrationDtoList, jobGraph);
        // 获取已迁移的文件夹、数据源和DAG数据
        List<CompositeFolder> folderList = compositeFolderRepo.queryGeneralFolder();
        List<DataSource> dataSourceList = dataSourceRepo.queryDataSource(new DataSourceCondition(), 10000, 0);
        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo();
        // 循环迁移数据
        Set<Long> migratedOldJobs = Sets.newHashSet();
        // 获取新版IData已有作业数据，多次执行迁移时用于过滤已经迁移完成的作业
        List<JobInfo> existJobs = jobInfoRepo.queryJobInfo(new JobInfoCondition());

        // 部分信息放到线程 ThreadLocal 中，避免太多传参
        JobMigrationContext.setJobMigrationList(migrationDtoList);
        JobMigrationContext.setFolderList(folderList);
        JobMigrationContext.setDataSourceList(dataSourceList);
        JobMigrationContext.setExistJobList(existJobs);
        JobMigrationContext.setDAGList(dagInfoList);

        for (JobMigrationDto migrationDto : migrationDtoList) {
            Long oldJobId = migrationDto.getOldJobId();
            migrateJobNode(jobGraph, oldJobId, migratedOldJobs);
        }

        LOGGER.info("*** *** 作业迁移结束 *** *** 本次迁移实际处理作业数[{}]，作业[{}]", JobMigrationContext.countHandledJobs(), Joiner.on(',').join(JobMigrationContext.getHandledJobs()));
        JobMigrationContext.clear();
        return Lists.newArrayList();
    }

    private void migrateJobNode(MutableGraph<Long> jobGraph, Long oldJobId, Set<Long> migratedOldJobs) {
        if (migratedOldJobs.contains(oldJobId))
            return;
        // 获取上游节点
        Set<Long> predecessors = jobGraph.predecessors(oldJobId);
        if (!CollectionUtils.isEmpty(predecessors)) {
            // 先迁移上游节点
            for (Long prev_job_id : predecessors) {
                migrateJobNode(jobGraph, prev_job_id, migratedOldJobs);
            }
        }
        // 迁移节点
        List<MigrateResultDto> resultDtoList;
        boolean isSuc = true;
        try {
            resultDtoList = migrateJobData(oldJobId);
        } catch (Exception ex) {
            resultDtoList = new ArrayList<>();
            resultDtoList.add(new MigrateResultDto("migrateJobNode", String.format("迁移失败：旧作业[%s]迁移失败，原因：%s", oldJobId, ex.getMessage()), ""));
            isSuc = false;
        }
        if (!CollectionUtils.isEmpty(resultDtoList)) {
            saveMigrateResult(resultDtoList);
        }
        if (isSuc)
            migratedOldJobs.add(oldJobId);
    }

    private void saveMigrateResult(List<MigrateResultDto> resultDtoList) {
        for (MigrateResultDto resultDto : resultDtoList) {
            migrateResultDao.insertSelective(resultDto.toModel());
        }
    }

    private List<MigrateResultDto> migrateJobData(Long oldJobId) {
        List<MigrateResultDto> resultDtoList = new ArrayList<>();
        JobMigrationDto migrationDto = JobMigrationContext.getJobMigrationDtoIfPresent(oldJobId);
        if (Objects.isNull(migrationDto)) {
            resultDtoList.add(new MigrateResultDto("fetchMigrateJobData", String.format("确认是否处理：旧作业[%s]数据不合法，如没有配置DAG等。", oldJobId), ""));
            LOGGER.warn("### ### 作业[{}]数据不合法，不迁移", oldJobId);
            return resultDtoList;
        }
        JSONObject oldJobInfo = migrationDto.getOldJobInfo();
        JSONObject oldJobConfig = migrationDto.getOldJobConfig();
        JSONObject oldJobContent = migrationDto.getOldJobContent();
        String jobName = oldJobInfo.getString("job_name");
        JobInfo existJob = JobMigrationContext.getExistJobIfPresent(jobName);
        if (existJob != null) {
            LOGGER.warn("### ### 作业[{}]已存在，不需要再迁移", existJob.getName());
            return resultDtoList;
        }

        LOGGER.info("*** *** 开始迁移作业[{}]#[{}]", oldJobId, jobName);
        JobInfoDto jobInfoDto = buildJobBaseInfo(oldJobId, oldJobInfo, oldJobContent);
        String old_owner = oldJobInfo.getString("owner_id");
        String old_creator = oldJobInfo.getString("creator");
        String old_editor = oldJobInfo.getString("editor");
        String nickname = StringUtils.isNotEmpty(old_creator) ? old_creator : StringUtils.isNotEmpty(old_editor) ? old_editor : old_owner;
        Operator jobOperator = new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();

        Optional<JobConfigCombinationDto> configCombinationDtoOptional = buildJobConfig(oldJobId, oldJobInfo, oldJobConfig, oldJobContent, resultDtoList);
        if (configCombinationDtoOptional.isEmpty()) {
            LOGGER.warn("### ### 作业[{}]配置错误，不能迁移", existJob.getName());
            return resultDtoList;
        }
        List<MigrateResultDto> resultList = jobMigrateManager.migrateJob(jobInfoDto, configCombinationDtoOptional.get(), jobOperator, migrationDto);
        resultDtoList.addAll(resultList);
        JobMigrationContext.addHandledJob(jobInfoDto.getName());
        LOGGER.info("### ### 结束迁移作业[{}]#[{}]，本次已处理作业计数：[{}]", oldJobId, jobName, JobMigrationContext.countHandledJobs());
        return resultDtoList;
    }

    private void fetchOldJobData(List<JobMigrationDto> outMigrationDtoList, MutableGraph<Long> outJobGraph) {
        // 获取旧版IData所有作业数据
        List<JSONObject> oldJobInfoList = fetchOldJobInfo();
        // 获取旧版IData所有作业配置
        List<JSONObject> oldJobConfigList = fetchOldJobConfig();
        // 获取旧版IData所有作业内容
        List<JSONObject> oldJobContentList = fetchOldJobContent();

        Map<Long, List<JSONObject>> oldJobConfigMap = oldJobConfigList.stream()
                .collect(Collectors.groupingBy(jsonObject -> jsonObject.getLong("job_id")));

        Map<Long, List<JSONObject>> oldJobContentMap = oldJobContentList.stream()
                .collect(Collectors.groupingBy(jsonObject -> jsonObject.getLong("job_id")));

        oldJobInfoList.stream().forEach(oldJobInfoJson -> {
            Long jobId = oldJobInfoJson.getLong("id");
            // 没有作业内容，不迁移
            if (!oldJobContentMap.containsKey(jobId))
                return;

            if (!oldJobConfigMap.containsKey(jobId))
                return;

            JobMigrationDto migrationDto = new JobMigrationDto();
            migrationDto.setOldJobId(jobId);
            migrationDto.setOldJobInfo(oldJobInfoJson);
            migrationDto.setOldJobConfig(oldJobConfigMap.get(jobId).get(0));
            migrationDto.setOldJobContent(oldJobContentMap.get(jobId).get(0));
            outMigrationDtoList.add(migrationDto);
        });

        oldJobConfigList.stream().forEach(jobConfig -> {
            Long job_id = jobConfig.getLong("job_id");
            String[] prev_job_ids = jobConfig.getJSONArray("dependent_job_ids").toArray(new String[0]);
            if (prev_job_ids == null || prev_job_ids.length == 0) {
                outJobGraph.addNode(job_id);
            } else {
                for (String prev_job_id : prev_job_ids) {
                    outJobGraph.putEdge(Long.parseLong(prev_job_id), job_id);
                }
            }
        });

    }

    private List<JSONObject> fetchOldJobInfo() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_name", "job_type", "target_tables", "description", "owner_id", "folder_id", "layer");
        String filter = "del = false and job_type in ('SCRIPT', 'SPARK', 'DI', 'KYLIN', 'SQL')";
//        String filter = "del = false and job_type in ('DI')";
        return oldIDataDao.queryListWithCustom("metadata.job_info", columns, filter);
    }

    private JobInfoDto buildJobBaseInfo(Long oldJobId, JSONObject jobInfoJson, JSONObject oldJobContentJson) {
        JobInfoDto dto = new JobInfoDto();
        dto.setName(jobInfoJson.getString("job_name"));
        dto.setDwLayerCode(DWLayerCodeMapTool.getCodeEnum(jobInfoJson.getString("layer")));
        dto.setRemark(jobInfoJson.getString("description"));
        // 获取所属folderId
        Long oldFolderId = jobInfoJson.getLong("folder_id");
        String oldType = jobInfoJson.getString("job_type");
        checkArgument(Objects.nonNull(oldFolderId), "旧作业[%s]所属文件夹为空", oldJobId);
        String module = "DI".equalsIgnoreCase(oldType) ? "DI" : null;
        Optional<CompositeFolder> folderOptional = FolderTool.findFolder(oldFolderId, JobMigrationContext.getFolderListIfPresent(), module);
        checkArgument(folderOptional.isPresent(), "旧作业[%s]未找到迁移后的文件夹", oldJobId);
        dto.setFolderId(folderOptional.get().getId());

        JobTypeEnum jobType = null;
        if ("DI".equalsIgnoreCase(oldType)) {
            jobType = JobTypeEnum.DI_BATCH;
        } else if ("KYLIN".equalsIgnoreCase(oldType)) {
            jobType = JobTypeEnum.KYLIN;
        } else if ("SPARK".equalsIgnoreCase(oldType)) {
            if ("Jar".equals(oldJobContentJson.getString("app_type"))) {
                jobType = JobTypeEnum.SPARK_JAR;
            } else {
                jobType = JobTypeEnum.SPARK_PYTHON;
            }
        } else if ("SCRIPT".equalsIgnoreCase(oldType)) {
            List<JSONObject> fileResource = fetchOldResource(Long.valueOf(oldJobContentJson.getString("resource_id")));
            String fileResourceType = fileResource.get(0).getString("resource_type");
            if ("Python".equals(fileResourceType)) {
                jobType = JobTypeEnum.SCRIPT_PYTHON;
            } else if ("Shell".equals(fileResourceType)) {
                jobType = JobTypeEnum.SCRIPT_SHELL;
            }
        } else {
            jobType = JobTypeEnum.SQL_SPARK;
        }
        dto.setJobType(jobType);
        return dto;
    }

    private List<JSONObject> fetchOldJobConfig() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "sandbox", "dependent_job_ids", "alarm_level", "executor_memory", "driver_memory", "executor_cores", "target_id", "dag_id");
        String filter = "del = false and sandbox = 'prod' and dag_id is not null";
        return oldIDataDao.queryListWithCustom("metadata.job_config", columns, filter);
    }

    private Optional<JobConfigCombinationDto> buildJobConfig(Long oldJobId, JSONObject jobInfoJson, JSONObject configJson,
                                                             JSONObject oldJobContent, List<MigrateResultDto> resultDtoList) {
        JobConfigCombinationDto combinationDto = new JobConfigCombinationDto();

        JobExecuteConfigDto executeConfigDto = new JobExecuteConfigDto();
        executeConfigDto.setEnvironment(EnvEnum.prod.name());
        Integer oldDagId = configJson.getInteger("dag_id");
        if (Objects.isNull(oldDagId)) {
            resultDtoList.add(new MigrateResultDto("buildJobConfig", String.format("无法迁移：旧作业[%s]依赖DAG为空", oldJobId), configJson.toJSONString()));
            return Optional.empty();
        }
        Optional<DAGInfo> dagInfoOptional = DagTool.findDag(oldDagId, JobMigrationContext.getDAGIfPresent());
        checkArgument(dagInfoOptional.isPresent(), "旧作业[%s]未找到迁移后的DAG", oldJobId);
        executeConfigDto.setSchDagId(dagInfoOptional.get().getId());
        executeConfigDto.setSchRerunMode("always");
        // 调度配置-超时时间，单位：秒
        executeConfigDto.setSchTimeOut(60 * 60);
        // 调度配置-是否空跑，0否，1是
//        executeConfigDto.setSchDryRun();
        // 运行配置-队列
        executeConfigDto.setExecQueue(ExecuteQueueEnum.OFFLINE.code);
        // 运行配置-告警等级
        executeConfigDto.setExecWarnLevel(parseWarnLevel(configJson.getString("alarm_level")));
        //调度配置-超时策略，alarm：超时告警，fail：超时失败
        executeConfigDto.setSchTimeOutStrategy(JobTimeOutStrategyEnum.ALARM.code);
        //调度配置-优先级，1：低，2：中，3：高
        executeConfigDto.setSchPriority(JobPriorityEnum.middle.val);
        // 运行配置-驱动器内存
        executeConfigDto.setExecDriverMem(MoreObjects.firstNonNull(parseExecMem(configJson.getString("driver_memory")), 3));
        // 运行配置-执行器内存
        executeConfigDto.setExecWorkerMem(MoreObjects.firstNonNull(parseExecMem(configJson.getString("executor_memory")), 4));
        // 作业运行状态（环境级），0：暂停运行；1：恢复运行
        executeConfigDto.setRunningState(RunningStateEnum.pause.val);
        executeConfigDto.setExecEngine(EngineTypeEnum.SQOOP.name());
        combinationDto.setExecuteConfig(executeConfigDto);

        List<JobDependenceDto> dependencies = new ArrayList<>();
        String[] prev_old_job_ids = configJson.getJSONArray("dependent_job_ids").toArray(new String[0]);
        if (prev_old_job_ids != null && prev_old_job_ids.length > 0) {
            for (String prev_old_job_id_str : prev_old_job_ids) {
                Long prev_old_job_id = Long.parseLong(prev_old_job_id_str);
                JobMigrationDto prevMigrationDto = JobMigrationContext.getJobMigrationDtoIfPresent(prev_old_job_id);
                if (Objects.isNull(prevMigrationDto)) {
                    resultDtoList.add(new MigrateResultDto("buildJobConfig", String.format("确认是否处理：旧作业[%s]未找到依赖的旧作业[%s]", oldJobId, prev_old_job_id), configJson.toJSONString()));
                    continue;
                }
                String prev_job_name = prevMigrationDto.getOldJobInfo().getString("job_name");
                JobInfo prevJobInfo = JobMigrationContext.getExistJobIfPresent(prev_job_name);
                if (Objects.isNull(prevJobInfo)) {
                    List<JobInfo> prevJobs = jobInfoRepo.queryJobInfoByName(prev_job_name);
                    checkArgument(prevJobs.size() > 0, "旧作业[%s]未找到依赖的迁移后的作业[%s]", oldJobId, prev_job_name);
                    prevJobInfo = prevJobs.get(0);
                    JobMigrationContext.putExistJob(prev_job_name, prevJobInfo);
                }

                Optional<JobExecuteConfig> prevJobConfigOptional = jobExecuteConfigRepo.query(prevJobInfo.getId(), EnvEnum.prod.name());
                checkArgument(prevJobConfigOptional.isPresent() && !Objects.isNull(prevJobConfigOptional.get().getSchDagId()),
                        "旧作业[%s]依赖的迁移后的作业[%s]未配置prod调度配置", oldJobId, prev_job_name);

                JobDependenceDto dependenceDto = new JobDependenceDto();
                dependenceDto.setEnvironment(EnvEnum.prod.name());
                dependenceDto.setPrevJobId(prevJobInfo.getId());
                dependenceDto.setPrevJobDagId(prevJobConfigOptional.get().getSchDagId());
            }
        }
        combinationDto.setDependencies(dependencies);

        // 只有sql作业需要配置作业输出
        String oldType = jobInfoJson.getString("job_type");
        if ("SQL".equalsIgnoreCase(oldType)) {
            JobOutputDto outputDto = new JobOutputDto();
            outputDto.setEnvironment(EnvEnum.prod.name());
            Optional<DataSource> dataSourceOptional = DatasourceTool.findDatasource(configJson.getLong("target_id"), JobMigrationContext.getDataSourceListIfPresent());
            checkArgument(dataSourceOptional.isPresent(), "旧作业[%s]未找到迁移后的数据源", oldJobId);
            DataSource dataSource = dataSourceOptional.get();
            outputDto.setDestDataSourceType(dataSource.getType());
            outputDto.setDestDataSourceId(dataSource.getId());
            String[] target_tables = jobInfoJson.getJSONArray("target_tables").toArray(new String[0]);
            checkArgument(target_tables != null && target_tables.length > 0, "旧SQL作业[%s]目标表名为空", oldJobId);
            outputDto.setDestTable(target_tables[0]);
            String save_mode = oldJobContent.getString("save_mode");
            save_mode = StringUtils.defaultString(save_mode, "OVERWRITE");
            outputDto.setDestWriteMode(save_mode.toLowerCase());
            String source_table_pk = oldJobContent.getString("source_table_pk");
            outputDto.setJobTargetTablePk(source_table_pk);
            combinationDto.setOutput(outputDto);
        }

        return Optional.of(combinationDto);
    }

    private Integer parseExecMem(String men) {
        if (StringUtils.isBlank(men))
            return null;
        String men_num_str = men.substring(0, men.indexOf("G"));
        if (StringUtils.isBlank(men_num_str))
            return null;
        try {
            return Integer.parseInt(men_num_str);
        } catch (Exception ex) {
            return null;
        }
    }

    private String parseWarnLevel(String oldLevel) {
        if ("High".equalsIgnoreCase(oldLevel))
            return "ALARM_LEVEL_HIGH:ENUM_VALUE";
        if ("Low".equalsIgnoreCase(oldLevel))
            return "ALARM_LEVEL_LOW:ENUM_VALUE";
        return "ALARM_LEVEL_MEDIUM:ENUM_VALUE";
    }

    private List<JSONObject> fetchOldJobContent() {
        List<JSONObject> list = Lists.newArrayList();
        list.addAll(fetchOldDIAndSQLAndBackFlowJobContent());
        list.addAll(fetchOldSparkJobContent());
        list.addAll(fetchOldKylinJobContent());
        list.addAll(fetchOldScriptJobContent());
        return list;
    }

    private List<JSONObject> fetchOldDIAndSQLAndBackFlowJobContent() {
//        List<String> columns_for_new = Lists.newArrayList("id", "creator", "editor", "job_id", "source_id", "source_type", "source_table", "source_table_pk", "source_sql", "external_tables", "array_to_json(udf_ids) as udf_ids", "version", "array_to_json(status) as status", "save_mode", "before_sqls", "after_sqls", "source_tbl_time_col", "di_table_info", "merge_sql", "is_recreate", "version_comment");
        List<String> columns_for_old = Lists.newArrayList("id", "creator", "editor", "job_id", "source_id", "source_type", "source_table", "source_table_pk", "source_sql", "external_tables", "udf_ids", "version", "status", "save_mode", "before_sqls", "after_sqls", "source_tbl_time_col");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}')";
        return oldIDataDao.queryListWithCustom("metadata.sql_job", columns_for_old, filter);
    }

    private List<JSONObject> fetchOldKylinJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "cube_name", "start_time", "end_time", "build_type", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}')";
        return oldIDataDao.queryListWithCustom("metadata.kylin_job", columns, filter);
    }

    private List<JSONObject> fetchOldSparkJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "app_type", "resource_id", "main_class", "app_arguments", "dependent_resource_ids", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}')";
        return oldIDataDao.queryListWithCustom("metadata.spark_job", columns, filter);
    }

    private List<JSONObject> fetchOldScriptJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "resource_id", "script_arguments", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause," +
                "prod}')";
        return oldIDataDao.queryListWithCustom("metadata.script_job", columns, filter);
    }

    private List<JSONObject> fetchOldResource(Long resourceId) {
        List<String> columns = Lists.newArrayList("resource_type", "hdfs_path");
        String filter = "del = false and id = " + resourceId;
        return oldIDataDao.queryList("metadata.file_resource", columns, filter);
    }

}
