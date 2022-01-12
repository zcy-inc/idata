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

package cn.zhengcaiyun.idata.merge.data.util;

import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.merge.data.dto.JobMigrationDto;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 当前操作人信息
 * @author: yangjianhua
 * @create: 2021-07-12 14:55
 **/
public class JobMigrationContext {
    private static final ThreadLocal<Map<Long, JobMigrationDto>> jobMigrationTl = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, JobInfo>> existJobTl = new ThreadLocal<>();
    private static final ThreadLocal<List<CompositeFolder>> folderTl = new ThreadLocal<>();
    private static final ThreadLocal<List<DataSource>> dataSourceTl = new ThreadLocal<>();
    private static final ThreadLocal<List<DAGInfo>> dagTl = new ThreadLocal<>();

    public static void setJobMigrationList(List<JobMigrationDto> migrationDtoList) {
        Map<Long, JobMigrationDto> migrationDtoMap = Maps.newHashMap();
        for (JobMigrationDto dto : migrationDtoList) {
            migrationDtoMap.put(dto.getOldJobId(), dto);
        }
        jobMigrationTl.set(migrationDtoMap);
    }

    public static JobMigrationDto getJobMigrationDtoIfPresent(Long jobId) {
        return Optional.ofNullable(jobMigrationTl.get().get(jobId)).orElse(null);
    }

    public static void setExistJobList(List<JobInfo> existJobs) {
        Map<String, JobInfo> jobMap = Maps.newHashMap();
        for (JobInfo jobInfo : existJobs) {
            jobMap.put(jobInfo.getName(), jobInfo);
        }
        existJobTl.set(jobMap);
    }

    public static void putExistJob(String name, JobInfo jobInfo) {
        existJobTl.get().put(name, jobInfo);
    }

    public static JobInfo getExistJobIfPresent(String jobName) {
        return Optional.ofNullable(existJobTl.get().get(jobName)).orElse(null);
    }

    public static void setFolderList(List<CompositeFolder> folderList) {
        folderTl.set(folderList);
    }

    public static List<CompositeFolder> getFolderListIfPresent() {
        return Optional.ofNullable(folderTl.get()).orElse(null);
    }

    public static void setDAGList(List<DAGInfo> dagInfoList) {
        dagTl.set(dagInfoList);
    }

    public static List<DAGInfo> getDAGIfPresent() {
        return Optional.ofNullable(dagTl.get()).orElse(null);
    }

    public static void setDataSourceList(List<DataSource> dataSourceList) {
        dataSourceTl.set(dataSourceList);
    }

    public static List<DataSource> getDataSourceListIfPresent() {
        return Optional.ofNullable(dataSourceTl.get()).orElse(null);
    }

    public static void clear() {
        jobMigrationTl.remove();
        folderTl.remove();
        dataSourceTl.remove();
        existJobTl.remove();
    }

}
