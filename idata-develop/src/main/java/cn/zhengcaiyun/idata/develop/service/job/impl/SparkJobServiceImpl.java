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
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSpark;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.SparkJobRepo;
import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobContentDto;
import cn.zhengcaiyun.idata.develop.service.job.SparkJobService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-11-19 下午4:12
 */

@Service
public class SparkJobServiceImpl implements SparkJobService {

    @Autowired
    private JobInfoRepo jobInfoRepo;
    @Autowired
    private SparkJobRepo sparkJobRepo;
    @Autowired
    private HdfsService hdfsService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public SparkJobContentDto save(SparkJobContentDto sparkJobDto, String operator) throws IOException {
        checkArgument(sparkJobDto.getJobId() != null, "作业Id不能为空");
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(sparkJobDto.getJobId());
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        if (JobTypeEnum.SPARK_JAR.getCode().equals(jobInfoOptional.get().getJobType())) {
            checkArgument(StringUtils.isNotEmpty(sparkJobDto.getMainClass()), "SparkJar类型作业执行类不能为空");
        }

        Integer version = sparkJobDto.getVersion();
        boolean startNewVersion = false;
        if (Objects.nonNull(version)) {
            DevJobContentSpark existJobContentSpark = sparkJobRepo.query(sparkJobDto.getJobId(), version);
            checkArgument(existJobContentSpark != null, "作业不存在或已删除");
            SparkJobContentDto existSparkJob = find(sparkJobDto.getJobId(), version);

            // 不可修改且跟当前版本不一致才新生成版本
            if (existSparkJob.getEditable().equals(EditableEnum.NO.val) && !sparkJobDto.equals(existSparkJob)) {
                startNewVersion = true;
                if (JobTypeEnum.SPARK_PYTHON.equals(sparkJobDto.getJobType())
                        && !sparkJobDto.getPythonResource().equals(existSparkJob.getPythonResource())) {
                    sparkJobDto.setResourceHdfsPath(hdfsService.uploadFileToResource(
                            new ByteArrayInputStream(sparkJobDto.getPythonResource().getBytes()), jobInfoOptional.get().getName() + ".py"));
                }
            }
            else {
                if (existJobContentSpark.getEditable().equals(EditableEnum.YES.val)) {
                    if (JobTypeEnum.SPARK_PYTHON.equals(sparkJobDto.getJobType())
                            && !existSparkJob.getPythonResource().equals(sparkJobDto.getPythonResource())) {
                        hdfsService.modifyFile(existSparkJob.getResourceHdfsPath(), sparkJobDto.getPythonResource());
                    }
                    DevJobContentSpark jobContentSpark = PojoUtil.copyOne(sparkJobDto, DevJobContentSpark.class,
                            "appArguments", "mainClass", "resourceHdfsPath");
                    jobContentSpark.setId(existJobContentSpark.getId());
                    jobContentSpark.setEditor(operator);
                    sparkJobRepo.update(jobContentSpark);
                }
            }
        }
        else {
            if (JobTypeEnum.SPARK_PYTHON.equals(sparkJobDto.getJobType())) {
                sparkJobDto.setResourceHdfsPath(hdfsService.uploadFileToResource(
                        new ByteArrayInputStream(sparkJobDto.getPythonResource().getBytes()), jobInfoOptional.get().getName() + ".py"));
            }
            startNewVersion = true;
        }

        if (startNewVersion) {
            DevJobContentSpark jobContentSpark = PojoUtil.copyOne(sparkJobDto, DevJobContentSpark.class,
                    "jobId", "resourceHdfsPath", "mainClass");
            version = sparkJobRepo.newVersion(sparkJobDto.getJobId());
            jobContentSpark.setVersion(version);
            jobContentSpark.setAppArguments(ObjectUtils.isNotEmpty(sparkJobDto.getAppArguments())
                    ? sparkJobDto.getAppArguments() : Lists.newArrayList());
            jobContentSpark.setEditable(EditableEnum.YES.val);
            jobContentSpark.setCreator(operator);
            sparkJobRepo.add(jobContentSpark);
        }

        return find(sparkJobDto.getJobId(), version);
    }

    @Override
    public SparkJobContentDto find(Long jobId, Integer version) {
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");
        DevJobContentSpark jobContentSpark = sparkJobRepo.query(jobId, version);
        checkArgument(jobContentSpark != null, "SPARK作业不存在");
        SparkJobContentDto echoSparkJob = PojoUtil.copyOne(jobContentSpark, SparkJobContentDto.class);
        echoSparkJob.setJobType(JobTypeEnum.valueOf(jobInfoOptional.get().getJobType()));
        if (JobTypeEnum.SPARK_PYTHON.equals(echoSparkJob.getJobType())) {
            String output;
            try {
                ByteArrayOutputStream sos = new ByteArrayOutputStream();
                hdfsService.readFile(echoSparkJob.getResourceHdfsPath(), sos);
                output = sos.toString();
                sos.close();
            } catch (IOException e) {
                int end = (e.getMessage() + "").indexOf("\n");
                throw new RuntimeException((e.getMessage() + "").substring(0, end > 0 ? end : Integer.MAX_VALUE));
            }
            echoSparkJob.setPythonResource(output);
        }
        return echoSparkJob;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return hdfsService.uploadFileToResource(file.getInputStream(), file.getOriginalFilename());
    }
}
