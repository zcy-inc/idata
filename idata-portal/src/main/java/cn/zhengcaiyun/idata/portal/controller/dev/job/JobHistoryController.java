package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.job.JobAnotherHistoryDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.portal.model.request.IdRequest;
import cn.zhengcaiyun.idata.portal.model.request.PageWrapper;
import cn.zhengcaiyun.idata.portal.model.response.ops.JobHistoryResponse;
import cn.zhengcaiyun.idata.portal.schedule.JobSchedule;
import cn.zhengcaiyun.idata.portal.util.PageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("历史作业")
@RestController
@RequestMapping("/p1/dev/jobs/history")
public class JobHistoryController {

    @Autowired
    private JobHistoryService jobHistoryService;

    @Autowired
    private JobSchedule jobSchedule;

    @Autowired
    private JobInfoService jobInfoService;

    @ApiOperation("查看任务历史")
    @PostMapping("/page")
    public RestResult<Page<JobHistoryResponse>> pagingJobHistory(@RequestBody PageWrapper<IdRequest> pageWrapper) {
        Long jobId = pageWrapper.getCondition().getId();
        JobInfoDto jobInfo = jobInfoService.getJobInfo(jobId);

        PageInfo<DevJobHistory> pageInfo = jobHistoryService.pagingJobHistoryByJobId(jobId, pageWrapper.getPageNum(), pageWrapper.getPageSize());
        PageInfo<JobHistoryResponse> pageResponse = PageUtil.convertType(pageInfo, e -> {
            JobHistoryResponse response = new JobHistoryResponse();
            BeanUtils.copyProperties(e, response);
            response.setJobName(jobInfo.getName());
            response.setBusinessLogsUrl(jobHistoryService.getBusinessLogUrl(e.getApplicationId(), e.getFinalStatus(), e.getState()));

            return response;
        });
        return RestResult.success(PageUtil.covertMine(pageResponse));
    }

    @ApiOperation("手动触发脚本")
    @PostMapping("/script")
    public RestResult<Boolean> pullSparkSqlJobHistory() {
        jobSchedule.pullSparkSqlJobHistoryDay();
        return RestResult.success(true);
    }

    @GetMapping("/anotherPage")
    public RestResult<Page<JobAnotherHistoryDto>> pagingJobHistory(@RequestParam Long jobId,
                                                                   @RequestParam String environment,
                                                                   @RequestParam Integer pageNo,
                                                                   @RequestParam Integer pageSize) {
        return RestResult.success(jobHistoryService.pagingJobHistory(jobId, environment, pageNo, pageSize));
    }

}
