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
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDynamicSqlSupport.DI_JOB_CONTENT;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 13:48
 **/
@Repository
public class DIJobContentRepoImpl implements DIJobContentRepo {

    private final DIJobContentDao diJobContentDao;
    private final JobPublishRecordRepo jobPublishRecordRepo;

    @Autowired
    public DIJobContentRepoImpl(DIJobContentDao diJobContentDao,
                                JobPublishRecordRepo jobPublishRecordRepo) {
        this.diJobContentDao = diJobContentDao;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
    }

    @Override
    public Long save(DIJobContent content) {
        int ret = diJobContentDao.insertSelective(content);
        if (ret <= 0) return null;
        return content.getId();
    }

    @Override
    public Boolean update(DIJobContent content) {
        diJobContentDao.updateByPrimaryKeySelective(content);
        return Boolean.TRUE;
    }

    @Override
    public Integer newVersion(Long jobId) {
        Optional<DIJobContent> optional = diJobContentDao.selectOne(dsl -> dsl.where(DI_JOB_CONTENT.jobId, isEqualTo(jobId))
                .orderBy(DI_JOB_CONTENT.version.descending())
                .limit(1));
        return optional.isEmpty() ? 1 : optional.get().getVersion() + 1;
    }

    @Override
    public Boolean updateEditable(Long id, EditableEnum editable, String operator) {
        diJobContentDao.update(dsl -> dsl.set(DI_JOB_CONTENT.editable).equalTo(editable.val)
                .set(DI_JOB_CONTENT.editor).equalTo(operator)
                .where(DI_JOB_CONTENT.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    public Optional<DIJobContent> query(Long jobId, Integer version) {
        return diJobContentDao.selectOne(dsl -> dsl.where(DI_JOB_CONTENT.jobId, isEqualTo(jobId),
                and(DI_JOB_CONTENT.version, isEqualTo(version)),
                and(DI_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    @Transactional
    public Boolean submit(DIJobContent content, JobPublishRecord publishRecord, String operator) {
        if (publishRecord.getId() == null) {
            // 第一次提交
            updateEditable(content.getId(), EditableEnum.NO, operator);
            publishRecord.setPublishStatus(PublishStatusEnum.SUBMITTED.val);
            jobPublishRecordRepo.save(publishRecord);
        } else {
            // 归档后再次提交
            JobPublishRecord submitStatus = new JobPublishRecord();
            submitStatus.setId(publishRecord.getId());
            submitStatus.setPublishStatus(PublishStatusEnum.SUBMITTED.val);
            submitStatus.setEditor(operator);
            submitStatus.setSubmitRemark(publishRecord.getSubmitRemark());
            jobPublishRecordRepo.update(submitStatus);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<DIJobContent> queryList(Long jobId) {
        return diJobContentDao.select(dsl -> dsl.where(DI_JOB_CONTENT.jobId, isEqualTo(jobId),
                        and(DI_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(DI_JOB_CONTENT.version));
    }

    @Override
    public List<DIJobContent> queryList(String destTable) {
        return diJobContentDao.select(dsl -> dsl.where(DI_JOB_CONTENT.destTable, isEqualTo(destTable),
                        and(DI_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(DI_JOB_CONTENT.version));
    }

    @Override
    public long countByDataSource(Long dataSourceId) {
        return diJobContentDao.count(dsl -> dsl.where(DI_JOB_CONTENT.del, isEqualTo(DeleteEnum.DEL_NO.val),
                and(DI_JOB_CONTENT.srcDataSourceId, isEqualTo(dataSourceId), or(DI_JOB_CONTENT.destDataSourceId, isEqualTo(dataSourceId))))
        );
    }
}
