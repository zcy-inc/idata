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
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentScript;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.ScriptJobRepo;
import cn.zhengcaiyun.idata.develop.dto.job.script.ScriptJobDto;
import cn.zhengcaiyun.idata.develop.service.job.ScriptJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-11-19 下午3:25
 */

@Service
public class ScriptJobServiceImpl implements ScriptJobService {

    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private ScriptJobRepo scriptJobRepo;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ScriptJobDto save(ScriptJobDto scriptJobDto, String operator) {
        checkArgument(scriptJobDto.getJobId() != null, "作业Id不能为空");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(scriptJobDto.getJobId());
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        Integer version = scriptJobDto.getVersion();
        boolean startNewVersion = false;
        if (Objects.nonNull(version)) {
            DevJobContentScript existJobContentScript = scriptJobRepo.query(scriptJobDto.getJobId(), version);
            checkArgument(existJobContentScript != null, "作业不存在或已删除");
            ScriptJobDto existScriptJob = PojoUtil.copyOne(existJobContentScript, ScriptJobDto.class);

            // TODO 测试一致性
            // 不可修改且跟当前版本不一致才新生成版本
            if (existScriptJob.getEditable().equals(EditableEnum.NO.val) && !scriptJobDto.equals(existScriptJob)) {
                startNewVersion = true;
            }
            else {
                if (existJobContentScript.getEditable().equals(EditableEnum.YES.val)) {
                    DevJobContentScript jobContentScript = PojoUtil.copyOne(scriptJobDto, DevJobContentScript.class);
                    jobContentScript.setId(existJobContentScript.getId());
                    jobContentScript.setEditor(operator);
                    scriptJobRepo.update(jobContentScript);
                }
            }
        }

        if (startNewVersion) {
            DevJobContentScript jobContentScript = PojoUtil.copyOne(scriptJobDto, DevJobContentScript.class,
                    "jobId", "sourceResource", "scriptArguments");
            version = scriptJobRepo.newVersion(scriptJobDto.getJobId());
            jobContentScript.setVersion(version);
            jobContentScript.setEditable(EditableEnum.YES.val);
            jobContentScript.setCreator(operator);
            scriptJobRepo.add(jobContentScript);
        }

        return find(scriptJobDto.getJobId(), version);
    }

    @Override
    public ScriptJobDto find(Long jobId, Integer version) {
        DevJobContentScript jobContentScript = scriptJobRepo.query(jobId, version);
        checkArgument(jobContentScript != null, "作业不存在");
        return PojoUtil.copyOne(jobContentScript, ScriptJobDto.class);
    }
}
