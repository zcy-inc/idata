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

package cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.StreamJobInstanceStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.opt.stream.StreamJobInstanceDao;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobInstanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.opt.stream.StreamJobInstanceDynamicSqlSupport.STREAM_JOB_INSTANCE;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 16:02
 **/
@Repository
public class StreamJobInstanceRepoImpl implements StreamJobInstanceRepo {

    private final StreamJobInstanceDao streamJobInstanceDao;

    @Autowired
    public StreamJobInstanceRepoImpl(StreamJobInstanceDao streamJobInstanceDao) {
        this.streamJobInstanceDao = streamJobInstanceDao;
    }

    @Override
    public Page<StreamJobInstance> paging(StreamJobInstanceCondition condition, PageParam pageParam) {
        long total = count(condition);
        List<StreamJobInstance> recordList = null;
        if (total > 0) {
            recordList = queryList(condition, pageParam.getLimit(), pageParam.getOffset());
        }
        return Page.newOne(recordList, total);
    }

    @Override
    public List<StreamJobInstance> queryList(StreamJobInstanceCondition condition, long limit, long offset) {
        return streamJobInstanceDao.select(dsl -> dsl.where(
                        STREAM_JOB_INSTANCE.jobId, isEqualToWhenPresent(condition.getJobId()),
                        and(STREAM_JOB_INSTANCE.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                        and(STREAM_JOB_INSTANCE.environment, isEqualToWhenPresent(condition.getEnvironment())),
                        and(STREAM_JOB_INSTANCE.jobTypeCode, isEqualToWhenPresent(condition.getJobType())),
                        and(STREAM_JOB_INSTANCE.owner, isEqualToWhenPresent(condition.getOwner())),
                        and(STREAM_JOB_INSTANCE.status, isInWhenPresent(condition.getStatusList())),
                        and(STREAM_JOB_INSTANCE.jobName, isLikeWhenPresent(condition.getJobNamePattern()).map(MybatisHelper::appendWildCards)),
                        and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val))
                ).orderBy(STREAM_JOB_INSTANCE.editTime.descending())
                .limit(limit).offset(offset));
    }

    @Override
    public long count(StreamJobInstanceCondition condition) {
        return streamJobInstanceDao.count(dsl -> dsl.where(
                STREAM_JOB_INSTANCE.jobId, isEqualToWhenPresent(condition.getJobId()),
                and(STREAM_JOB_INSTANCE.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                and(STREAM_JOB_INSTANCE.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(STREAM_JOB_INSTANCE.jobTypeCode, isEqualToWhenPresent(condition.getJobType())),
                and(STREAM_JOB_INSTANCE.owner, isEqualToWhenPresent(condition.getOwner())),
                and(STREAM_JOB_INSTANCE.status, isInWhenPresent(condition.getStatusList())),
                and(STREAM_JOB_INSTANCE.jobName, isLikeWhenPresent(condition.getJobNamePattern()).map(MybatisHelper::appendWildCards)),
                and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ));
    }

    @Override
    public List<StreamJobInstance> queryList(StreamJobInstanceCondition condition) {
        return streamJobInstanceDao.select(dsl -> dsl.where(
                STREAM_JOB_INSTANCE.jobId, isEqualToWhenPresent(condition.getJobId()),
                and(STREAM_JOB_INSTANCE.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                and(STREAM_JOB_INSTANCE.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(STREAM_JOB_INSTANCE.jobTypeCode, isEqualToWhenPresent(condition.getJobType())),
                and(STREAM_JOB_INSTANCE.owner, isEqualToWhenPresent(condition.getOwner())),
                and(STREAM_JOB_INSTANCE.status, isInWhenPresent(condition.getStatusList())),
                and(STREAM_JOB_INSTANCE.jobName, isLikeWhenPresent(condition.getJobNamePattern()).map(MybatisHelper::appendWildCards)),
                and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ).orderBy(STREAM_JOB_INSTANCE.id.descending()));
    }

    @Override
    public Long save(StreamJobInstance instance) {
        int ret = streamJobInstanceDao.insertSelective(instance);
        if (ret <= 0) return null;
        return instance.getId();
    }

    @Override
    public Boolean update(StreamJobInstance instance) {
        streamJobInstanceDao.updateByPrimaryKeySelective(instance);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateStatus(List<Long> ids, StreamJobInstanceStatusEnum statusEnum, String operator) {
        streamJobInstanceDao.update(dsl -> dsl.set(STREAM_JOB_INSTANCE.status).equalTo(statusEnum.val)
                .set(STREAM_JOB_INSTANCE.editor).equalTo(operator)
                .where(STREAM_JOB_INSTANCE.id, isIn(ids)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateStatus(Long id, StreamJobInstanceStatusEnum statusEnum) {
        streamJobInstanceDao.update(dsl -> dsl.set(STREAM_JOB_INSTANCE.status).equalTo(statusEnum.val)
                .where(STREAM_JOB_INSTANCE.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateRunParam(Long id, String runParams, String operator) {
        streamJobInstanceDao.update(dsl -> dsl.set(STREAM_JOB_INSTANCE.runParams).equalTo(runParams)
                .set(STREAM_JOB_INSTANCE.editor).equalTo(operator)
                .where(STREAM_JOB_INSTANCE.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Optional<StreamJobInstance> query(Long id) {
        Optional<StreamJobInstance> optional = streamJobInstanceDao.selectByPrimaryKey(id);
        if (optional.isEmpty()) return optional;

        if (DeleteEnum.DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public List<StreamJobInstance> queryList(Long jobId, Integer version, String env, List<Integer> instanceStatusList) {
        return streamJobInstanceDao.select(dsl -> dsl.where(STREAM_JOB_INSTANCE.jobId, isEqualTo(jobId),
                        and(STREAM_JOB_INSTANCE.jobContentVersion, isEqualTo(version)),
                        and(STREAM_JOB_INSTANCE.environment, isEqualTo(env)),
                        and(STREAM_JOB_INSTANCE.status, isIn(instanceStatusList)),
                        and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(STREAM_JOB_INSTANCE.id.descending()));
    }

    @Override
    public List<StreamJobInstance> queryList(Long jobId, String env, List<Integer> instanceStatusList) {
        return streamJobInstanceDao.select(dsl -> dsl.where(STREAM_JOB_INSTANCE.jobId, isEqualTo(jobId),
                        and(STREAM_JOB_INSTANCE.environment, isEqualTo(env)),
                        and(STREAM_JOB_INSTANCE.status, isIn(instanceStatusList)),
                        and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(STREAM_JOB_INSTANCE.id.descending()));
    }

    @Override
    public List<StreamJobInstance> queryList(Long jobId, String env, StreamJobInstanceStatusEnum statusEnum) {
        return streamJobInstanceDao.select(dsl -> dsl.where(STREAM_JOB_INSTANCE.jobId, isEqualTo(jobId),
                        and(STREAM_JOB_INSTANCE.environment, isEqualTo(env)),
                        and(STREAM_JOB_INSTANCE.status, isEqualTo(statusEnum.val)),
                        and(STREAM_JOB_INSTANCE.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(STREAM_JOB_INSTANCE.id.descending()));
    }
}
