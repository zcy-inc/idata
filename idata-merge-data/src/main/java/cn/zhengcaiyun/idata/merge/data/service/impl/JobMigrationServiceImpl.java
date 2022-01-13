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

package cn.zhengcaiyun.idata.merge.data.service.impl;

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
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.manager.JobMigrateManager;
import cn.zhengcaiyun.idata.merge.data.service.JobMigrationService;
import cn.zhengcaiyun.idata.merge.data.util.*;
import com.alibaba.fastjson.JSONObject;
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

    @Override
    public List<MigrateResultDto> migrate() {
        LOGGER.info("*** *** 开始作业数据迁移... ... *** ***");
        List<MigrateResultDto> resultDtoList = new ArrayList<>();
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
            migrateJobNode(jobGraph, oldJobId, migratedOldJobs, resultDtoList);
        }

        LOGGER.info("*** *** 作业迁移结束 *** *** 计划迁移作业数[{}]，已存在作业数[{}]", migrationDtoList.size(), existJobs.size());
        JobMigrationContext.clear();
        return resultDtoList;
    }

    private void migrateJobNode(MutableGraph<Long> jobGraph, Long oldJobId, Set<Long> migratedOldJobs, List<MigrateResultDto> resultDtoList) {
        if (migratedOldJobs.contains(oldJobId))
            return;
        // 获取上游节点
        Set<Long> predecessors = jobGraph.predecessors(oldJobId);
        if (!CollectionUtils.isEmpty(predecessors)) {
            // 先迁移上游节点
            for (Long prev_job_id : predecessors) {
                migrateJobNode(jobGraph, prev_job_id, migratedOldJobs, resultDtoList);
            }
        }
        // 迁移节点
        migrateJobData(oldJobId, resultDtoList);
        migratedOldJobs.add(oldJobId);
    }

    private void migrateJobData(Long oldJobId, List<MigrateResultDto> resultDtoList) {
        JobMigrationDto migrationDto = JobMigrationContext.getJobMigrationDtoIfPresent(oldJobId);
        JSONObject oldJobInfo = migrationDto.getOldJobInfo();
        JSONObject oldJobConfig = migrationDto.getOldJobConfig();
        JSONObject oldJobContent = migrationDto.getOldJobContent();
        String jobName = oldJobInfo.getString("job_name");
        JobInfo existJob = JobMigrationContext.getExistJobIfPresent(jobName);
        if (existJob != null) {
            LOGGER.info("### ### 作业[{}]已存在，不需要再迁移", existJob.getName());
            return;
        }

        LOGGER.info("*** *** 开始迁移作业[{}]#[{}]", oldJobId, jobName);
        JobInfoDto jobInfoDto = buildJobBaseInfo(oldJobId, oldJobInfo, oldJobContent);
        String old_owner = oldJobInfo.getString("owner_id");
        String old_creator = oldJobInfo.getString("creator");
        String old_editor = oldJobInfo.getString("editor");
        String nickname = StringUtils.isNotEmpty(old_creator) ? old_creator : StringUtils.isNotEmpty(old_editor) ? old_editor : old_owner;
        Operator jobOperator = new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();

        JobConfigCombinationDto configCombinationDto = buildJobConfig(oldJobId, oldJobInfo, oldJobConfig, oldJobContent);
        List<MigrateResultDto> resultList = jobMigrateManager.migrateJob(jobInfoDto, configCombinationDto, jobOperator, migrationDto);
        resultDtoList.addAll(resultList);
        LOGGER.info("### ### 结束迁移作业[{}]#[{}]", oldJobId, jobName);
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
            Long[] prev_job_ids = jobConfig.getJSONArray("dependent_job_ids").toArray(new Long[0]);
            if (prev_job_ids == null || prev_job_ids.length == 0) {
                outJobGraph.addNode(job_id);
            } else {
                for (Long prev_job_id : prev_job_ids) {
                    outJobGraph.putEdge(prev_job_id, job_id);
                }
            }
        });

    }

    private List<JSONObject> fetchOldJobInfo() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_name", "job_type", "target_tables", "description", "owner_id", "folder_id", "layer");
        String filter = "is_del = false and job_type in ('SCRIPT', 'SPARK', 'DI', 'KYLIN', 'SQL')";
        return oldIDataDao.queryList("metadata.job_info", columns, filter);
    }

    private JobInfoDto buildJobBaseInfo(Long oldJobId, JSONObject jobInfoJson, JSONObject oldJobContentJson) {
        JobInfoDto dto = new JobInfoDto();
        dto.setName(jobInfoJson.getString("job_name"));
        dto.setDwLayerCode(DWLayerCodeMapTool.getCodeEnum(jobInfoJson.getString("job_type")));
        dto.setRemark(jobInfoJson.getString("description"));
        // 获取所属folderId
        Optional<CompositeFolder> folderOptional = FolderTool.findFolder(oldJobId, JobMigrationContext.getFolderListIfPresent());
        checkArgument(folderOptional.isPresent(), "旧作业[%s]未找到迁移后的文件夹");
        dto.setFolderId(folderOptional.get().getId());

        String oldType = jobInfoJson.getString("job_type");
        JobTypeEnum jobType;
        if ("DI".equalsIgnoreCase(oldType)) {
            jobType = JobTypeEnum.DI_BATCH;
        } else if ("KYLIN".equalsIgnoreCase(oldType)) {
            jobType = JobTypeEnum.KYLIN;
        } else if ("SPARK".equalsIgnoreCase(oldType)) {
            if ("Jar".equals(oldJobContentJson.getString("app_type"))) {
                jobType = JobTypeEnum.SPARK_JAR;
            }
            else {
                jobType = JobTypeEnum.SPARK_PYTHON;
            }
        } else if ("SCRIPT".equalsIgnoreCase(oldType)) {
            List<JSONObject> fileResource = fetchOldResource(Long.valueOf(oldJobContentJson.getString("resource_id")));
            String fileResourceType = fileResource.get(0).getString("resource_type");
            if ("Python".equals(fileResourceType)) {
                jobType = JobTypeEnum.SCRIPT_PYTHON;
            }
            else if ("Shell".equals(fileResourceType)) {
                jobType = JobTypeEnum.SCRIPT_SHELL;
            }
        } else {
            jobType = JobTypeEnum.SQL_SPARK;
        }
        return dto;
    }

    private List<JSONObject> fetchOldJobConfig() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "sandbox", "dependent_job_ids", "alarm_level", "executor_memory", "driver_memory", "executor_cores", "target_id", "dag_id");
        String filter = "is_del = false and sandbox = prod";
        return oldIDataDao.queryList("metadata.job_config", columns, filter);
    }

    private JobConfigCombinationDto buildJobConfig(Long oldJobId, JSONObject jobInfoJson, JSONObject configJson,
                                                   JSONObject oldJobContent) {
        JobConfigCombinationDto combinationDto = new JobConfigCombinationDto();

        JobExecuteConfigDto executeConfigDto = new JobExecuteConfigDto();
        executeConfigDto.setEnvironment(EnvEnum.prod.name());
        Integer oldDagId = configJson.getInteger("dag_id");
        Optional<DAGInfo> dagInfoOptional = DagTool.findDag(oldDagId, JobMigrationContext.getDAGIfPresent());
        checkArgument(dagInfoOptional.isPresent(), "旧作业[%s]未找到迁移后的DAG", oldJobId);
        executeConfigDto.setSchDagId(dagInfoOptional.get().getId());
        executeConfigDto.setSchRerunMode("always");
        // 调度配置-超时时间，单位：秒
//        executeConfigDto.setSchTimeOut();
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
        executeConfigDto.setExecDriverMem(parseExecMem(configJson.getString("driver_memory")));
        // 运行配置-执行器内存
        executeConfigDto.setExecWorkerMem(parseExecMem(configJson.getString("executor_memory")));
        // 作业运行状态（环境级），0：暂停运行；1：恢复运行
        executeConfigDto.setRunningState(RunningStateEnum.pause.val);
        combinationDto.setExecuteConfig(executeConfigDto);

        List<JobDependenceDto> dependencies = new ArrayList<>();
        Long[] prev_old_job_ids = configJson.getJSONArray("dependent_job_ids").toArray(new Long[0]);
        if (prev_old_job_ids != null && prev_old_job_ids.length > 0) {
            for (Long prev_old_job_id : prev_old_job_ids) {
                JobMigrationDto prevMigrationDto = JobMigrationContext.getJobMigrationDtoIfPresent(prev_old_job_id);
                checkArgument(!Objects.isNull(prevMigrationDto), "旧作业[%s]未找到依赖的旧作业[%s]", oldJobId, prev_old_job_id);
                String prev_job_name = prevMigrationDto.getOldJobInfo().getString("job_name");
                JobInfo prevJobInfo = JobMigrationContext.getExistJobIfPresent(prev_job_name);
                if (Objects.isNull(prevJobInfo)) {
                    List<JobInfo> prevJobs = jobInfoRepo.queryJobInfoByName(prev_job_name);
                    checkArgument(prevJobs.size() > 0, "旧作业[%s]未找到依赖的迁移后的作业[%s]", oldJobId, prev_job_name);
                    prevJobInfo = prevJobs.get(0);
                    JobMigrationContext.putExistJob(prev_job_name, prevJobInfo);
                }

                Optional<JobExecuteConfig> prevConfigOptional = jobExecuteConfigRepo.query(prevJobInfo.getId(), EnvEnum.prod.name());
                checkArgument(prevConfigOptional.isPresent() && !Objects.isNull(prevConfigOptional.get().getSchDagId()),
                        "旧作业[%s]未找到依赖的迁移后的作业[%s]未配置prod调度配置", oldJobId, prev_job_name);

                JobDependenceDto dependenceDto = new JobDependenceDto();
                dependenceDto.setEnvironment(EnvEnum.prod.name());
                dependenceDto.setPrevJobId(prevJobInfo.getId());
                dependenceDto.setPrevJobDagId(prevConfigOptional.get().getSchDagId());
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

        return combinationDto;
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
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "source_id", "source_type", "source_table", "source_table_pk", "source_sql", "external_tables", "udf_ids", "version", "status", "save_mode", "before_sqls", "after_sqls", "source_tbl_time_col", "di_table_info", "merge_sql", "is_recreate", "version_comment");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}'";
        return oldIDataDao.queryList("metadata.sql_job", columns, filter);
    }

    private List<JSONObject> fetchOldKylinJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "cube_name", "start_time", "end_time", "build_type", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}'";
        return oldIDataDao.queryList("metadata.kylin_job", columns, filter);
    }

    private List<JSONObject> fetchOldSparkJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "app_type", "resource_id", "main_class", "app_arguments", "dependent_resource_ids", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause,prod}'";
        return oldIDataDao.queryList("metadata.spark_job", columns, filter);
    }

    private List<JSONObject> fetchOldScriptJobContent() {
        List<String> columns = Lists.newArrayList("id", "creator", "editor", "job_id", "resource_id", "script_arguments", "status");
        String filter = "del = false and (status = '{prod}' or status = '{staging,prod}' or status = '{staging_pause," +
                "prod}'";
        return oldIDataDao.queryList("metadata.script_job", columns, filter);
    }

    private List<JSONObject> fetchOldResource(Long resourceId) {
        List<String> columns = Lists.newArrayList("resource_type", "hdfs_path");
        String filter = "del = false and id = " + resourceId;
        return oldIDataDao.queryList("metadata.file_resource", columns, filter);
    }

}
