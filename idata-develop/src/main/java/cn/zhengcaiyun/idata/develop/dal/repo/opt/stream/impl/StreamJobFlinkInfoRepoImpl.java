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
import cn.zhengcaiyun.idata.develop.dal.dao.opt.stream.StreamJobFlinkInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobFlinkInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobFlinkInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.opt.stream.StreamJobFlinkInfoDynamicSqlSupport.STREAM_JOB_FLINK_INFO;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 16:05
 **/
@Repository
public class StreamJobFlinkInfoRepoImpl implements StreamJobFlinkInfoRepo {

    private final StreamJobFlinkInfoDao streamJobFlinkInfoDao;

    @Autowired
    public StreamJobFlinkInfoRepoImpl(StreamJobFlinkInfoDao streamJobFlinkInfoDao) {
        this.streamJobFlinkInfoDao = streamJobFlinkInfoDao;
    }

    @Override
    public Long save(StreamJobFlinkInfo info) {
        int ret = streamJobFlinkInfoDao.insertSelective(info);
        if (ret <= 0) return null;
        return info.getId();
    }

    @Override
    public Boolean delete(Long jobId, String env, String secondaryId) {
        streamJobFlinkInfoDao.update(dsl -> dsl.set(STREAM_JOB_FLINK_INFO.del).equalTo(DeleteEnum.DEL_YES.val)
                .where(STREAM_JOB_FLINK_INFO.jobId, isEqualTo(jobId),
                        and(STREAM_JOB_FLINK_INFO.environment, isEqualTo(env)),
                        and(STREAM_JOB_FLINK_INFO.secondaryId, isEqualToWhenPresent(secondaryId)),
                        and(STREAM_JOB_FLINK_INFO.del, isEqualTo(DeleteEnum.DEL_NO.val))));
        return Boolean.TRUE;
    }

    @Override
    public List<StreamJobFlinkInfo> queryList(Long jobId, String env, String secondaryId) {
        return streamJobFlinkInfoDao.select(dsl -> dsl.where(STREAM_JOB_FLINK_INFO.jobId, isEqualTo(jobId),
                and(STREAM_JOB_FLINK_INFO.environment, isEqualTo(env)),
                and(STREAM_JOB_FLINK_INFO.secondaryId, isEqualToWhenPresent(secondaryId)),
                and(STREAM_JOB_FLINK_INFO.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }
}
