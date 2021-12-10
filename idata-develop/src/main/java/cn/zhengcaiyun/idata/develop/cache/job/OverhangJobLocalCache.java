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

package cn.zhengcaiyun.idata.develop.cache.job;


import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.job.OverhangJobDto;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:52
 **/
@Component
public class OverhangJobLocalCache {

    private static final Logger logger = LoggerFactory.getLogger(OverhangJobLocalCache.class);

    private final LoadingCache<String, OverhangJobCacheValue> cache = Caffeine.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(Duration.ofMinutes(60))
            .build(key -> load(key));

    private final JobInfoRepo jobInfoRepo;
    private final JobDependenceRepo jobDependenceRepo;

    @Autowired
    public OverhangJobLocalCache(JobInfoRepo jobInfoRepo, JobDependenceRepo jobDependenceRepo) {
        this.jobInfoRepo = jobInfoRepo;
        this.jobDependenceRepo = jobDependenceRepo;
    }

    private OverhangJobCacheValue load(String key) {
        List<JobInfo> jobInfoList = jobInfoRepo.queryJobInfo(new JobInfoCondition());
        if (CollectionUtils.isEmpty(jobInfoList))
            return new OverhangJobCacheValue(Lists.newLinkedList());

        List<JobDependence> jobDependenceList = jobDependenceRepo.queryJobs();
        Set<Long> dependedJobIdSet;
        if (!CollectionUtils.isEmpty(jobDependenceList)) {
            dependedJobIdSet = Sets.newHashSet();
        } else {
            dependedJobIdSet = jobDependenceList.stream()
                    .map(JobDependence::getPrevJobId)
                    .collect(Collectors.toSet());
        }
        List<OverhangJobDto> overhangJobDtoList = jobInfoList.stream()
                .filter(jobInfo -> !dependedJobIdSet.contains(jobInfo.getId()))
                .map(OverhangJobDto::from)
                .collect(Collectors.toList());
        return new OverhangJobCacheValue(overhangJobDtoList);
    }

    public Optional<OverhangJobCacheValue> getOverhangJobs() {
        try {
            return Optional.ofNullable(cache.get("overhang-job"));
        } catch (Exception ex) {
            logger.warn("get OverhangJobCacheValue failed. ex: {}.", Throwables.getStackTraceAsString(ex));
        }
        return Optional.empty();
    }
}
