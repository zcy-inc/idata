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

package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobExtraOperateResult;
import cn.zhengcaiyun.idata.develop.service.job.JobExtraOperationService;
import cn.zhengcaiyun.idata.portal.model.request.job.JobBatchOperationExtReq;
import cn.zhengcaiyun.idata.portal.model.request.job.JobBatchOperationReq;
import cn.zhengcaiyun.idata.portal.util.ExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Job-Extra-Operation-Controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-06 09:41
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/")
public class JobExtraOperationController {

    private final JobExtraOperationService jobExtraOperationService;

    @Autowired
    public JobExtraOperationController(JobExtraOperationService jobExtraOperationService) {
        this.jobExtraOperationService = jobExtraOperationService;
    }


    /**
     * 复制作业
     *
     * @param batchOperationReq 请求对象
     * @return
     */
    @PostMapping("/copy")
    public RestResult<List<JobExtraOperateResult>> copyJob(@RequestBody JobBatchOperationExtReq batchOperationReq) {
        return RestResult.success(jobExtraOperationService.copyJobTo(batchOperationReq.getJobIds(), batchOperationReq.getDestFolderId(), OperatorContext.getCurrentOperator()));
    }

    /**
     * 导出作业
     *
     * @param batchOperationReq 请求对象
     * @return
     */
    @PostMapping("/export")
    public void exportJob(@RequestBody JobBatchOperationReq batchOperationReq,
                          HttpServletResponse response) {
        String jobJson = jobExtraOperationService.exportJobJson(batchOperationReq.getJobIds());
        ExportUtil.writeToTxt(response, jobJson, "export_job");
    }

    /**
     * 导入作业
     *
     * @param file
     * @param destFolderId 目标文件夹
     * @return
     */
    @PostMapping("/import")
    public RestResult<List<JobExtraOperateResult>> importJob(@RequestPart MultipartFile file,
                                                             @RequestParam Long destFolderId) {
        checkArgument(!file.isEmpty(), "上传的文件不能为空");
        checkArgument(file.getSize() > 0, "上传的文件大小需要大于0kb");
        String originFileName = file.getOriginalFilename();
        checkArgument(StringUtils.isNotBlank(originFileName), "文件名称不能为空");
        try {
            String jobJson = new String(file.getBytes(), Charset.forName("utf-8"));
            return RestResult.success(jobExtraOperationService.importJob(jobJson, destFolderId, OperatorContext.getCurrentOperator()));
        } catch (IOException ex) {
            throw new GeneralException("上传文件失败", ex);
        }
    }

}
