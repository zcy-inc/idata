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

package cn.zhengcaiyun.idata.develop.dal.repo.job.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDynamicSqlSupport.DI_STREAM_JOB_CONTENT;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-06-24 14:10
 **/
@Repository
public class DIStreamJobContentRepoImpl implements DIStreamJobContentRepo {

    private final DIStreamJobContentDao diStreamJobContentDao;

    @Autowired
    public DIStreamJobContentRepoImpl(DIStreamJobContentDao diStreamJobContentDao) {
        this.diStreamJobContentDao = diStreamJobContentDao;
    }

    @Override
    public Long save(DIStreamJobContent content) {
        int ret = diStreamJobContentDao.insertSelective(content);
        if (ret <= 0) return null;
        return content.getId();
    }

    @Override
    public Boolean update(DIStreamJobContent content) {
        diStreamJobContentDao.updateByPrimaryKeySelective(content);
        return Boolean.TRUE;
    }

    @Override
    public Integer newVersion(Long jobId) {
        Optional<DIStreamJobContent> optional = diStreamJobContentDao.selectOne(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId))
                .orderBy(DI_STREAM_JOB_CONTENT.version.descending())
                .limit(1));
        return optional.isEmpty() ? 1 : optional.get().getVersion() + 1;
    }

    @Override
    public Boolean updateEditable(Long id, EditableEnum editable, String operator) {
        diStreamJobContentDao.update(dsl -> dsl.set(DI_STREAM_JOB_CONTENT.editable).equalTo(editable.val)
                .set(DI_STREAM_JOB_CONTENT.editor).equalTo(operator)
                .where(DI_STREAM_JOB_CONTENT.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Optional<DIStreamJobContent> query(Long jobId, Integer version) {
        return diStreamJobContentDao.selectOne(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId),
                and(DI_STREAM_JOB_CONTENT.version, isEqualTo(version)),
                and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Optional<DIStreamJobContent> queryLatest(Long jobId) {
        return diStreamJobContentDao.selectOne(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId),
                        and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(DI_STREAM_JOB_CONTENT.version.descending())
                .limit(1));
    }

    @Override
    public List<DIStreamJobContent> queryList(Long jobId) {
        return diStreamJobContentDao.select(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId),
                        and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(DI_STREAM_JOB_CONTENT.version));
    }

    @Override
    public List<DIStreamJobContent> queryList(List<Long> ids) {
        return diStreamJobContentDao.select(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.id, isIn(ids),
                and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<DIStreamJobContent> queryByDataSource(Long srcDataSourceId, Long destDataSourceId) {
        return diStreamJobContentDao.select(dsl -> dsl.where(DI_STREAM_JOB_CONTENT.srcDataSourceId, isEqualTo(srcDataSourceId),
                and(DI_STREAM_JOB_CONTENT.destDataSourceId, isEqualTo(destDataSourceId)),
                and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Set<Long> queryJobIdByDataSource(Long srcDataSourceId, Long destDataSourceId) {
        SelectStatementProvider selectStatement = selectDistinct(DI_STREAM_JOB_CONTENT.jobId)
                .from(DI_STREAM_JOB_CONTENT)
                .where(DI_STREAM_JOB_CONTENT.srcDataSourceId, isEqualTo(srcDataSourceId),
                        and(DI_STREAM_JOB_CONTENT.destDataSourceId, isEqualTo(destDataSourceId)),
                        and(DI_STREAM_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return diStreamJobContentDao.selectMany(selectStatement)
                .stream()
                .map(DIStreamJobContent::getJobId)
                .collect(Collectors.toSet());
    }
}
