package cn.zhengcaiyun.idata.portal.controller.open;

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoExecuteDetailDto;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/p0/job")
public class OpenJobController {

    @Autowired
    private JobInfoService jobInfoService;

    /**
     * 获取作业详细信息，用于任务执行参数
     * @param jobId 作业id
     * @return
     */
    @GetMapping("/execute-detail")
    public RestResult<JobInfoExecuteDetailDto> getJobInfoDetail(@RequestParam("jobId") Long jobId,
                                                                @RequestParam("env") String env) {
        if (EnvEnum.getEnum(env).isEmpty()) {
            return RestResult.error("env is invalid : " + env, "invalid params");
        }
        return RestResult.success(jobInfoService.getJobInfoExecuteDetail(jobId, env));
    }


}
