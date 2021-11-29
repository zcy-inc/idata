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
package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentKylin;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.KylinJobRepo;
import cn.zhengcaiyun.idata.develop.dto.job.kylin.KylinJobDto;
import cn.zhengcaiyun.idata.develop.service.job.KylinJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-11-18 下午7:58
 */

@Service
public class KylinJobServiceImpl implements KylinJobService {

    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private KylinJobRepo kylinJobRepo;

    @Override
    public KylinJobDto save(KylinJobDto kylinJobDto, String operator) {
        checkArgument(kylinJobDto.getJobId() != null, "作业Id不能为空");
        checkArgument(StringUtils.isNotEmpty(kylinJobDto.getCubeName()), "cube名称不能为空");
        // TODO 修改为查枚举并校验枚举
        checkArgument(StringUtils.isNotEmpty(kylinJobDto.getBuildType()), "构建类型不能为空");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(kylinJobDto.getJobId());
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        Integer version = kylinJobDto.getVersion();
        boolean startNewVersion = false;
        if (Objects.nonNull(version)) {
            DevJobContentKylin existJobContentKylin = kylinJobRepo.query(kylinJobDto.getJobId(), version);
            checkArgument(existJobContentKylin != null, "作业不存在或已删除");
            KylinJobDto existKylinJob = PojoUtil.copyOne(existJobContentKylin, KylinJobDto.class);
            // 不可修改且跟当前版本不一致才新生成版本
            if (existKylinJob.getEditable().equals(EditableEnum.NO.val) && !kylinJobDto.equals(existKylinJob)) {
                startNewVersion = true;
            }
            else {
                if (existKylinJob.getEditable().equals(EditableEnum.YES.val)) {
                    DevJobContentKylin jobContentKylin = PojoUtil.copyOne(kylinJobDto, DevJobContentKylin.class);
                    jobContentKylin.setId(existJobContentKylin.getId());
                    jobContentKylin.setEditor(operator);
                    kylinJobRepo.update(jobContentKylin);
                }
            }
        }
        else {
            startNewVersion = true;
        }

        if (startNewVersion) {
            DevJobContentKylin jobContentKylin = PojoUtil.copyOne(kylinJobDto, DevJobContentKylin.class,
                    "jobId", "cubeName", "buildType", "startTime", "endTime");
            version = kylinJobRepo.newVersion(kylinJobDto.getJobId());
            jobContentKylin.setVersion(version);
            jobContentKylin.setEditable(EditableEnum.YES.val);
            jobContentKylin.setCreator(operator);
            kylinJobRepo.add(jobContentKylin);
        }

        return find(kylinJobDto.getJobId(), version);
    }

    @Override
    public KylinJobDto find(Long jobId, Integer version) {
        DevJobContentKylin jobContentKylin = kylinJobRepo.query(jobId, version);
        checkArgument(jobContentKylin != null, "作业不存在");
        return PojoUtil.copyOne(jobContentKylin, KylinJobDto.class);
    }

}
