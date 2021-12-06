package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.portal.model.request.IdRequest;
import cn.zhengcaiyun.idata.portal.model.request.PageWrapper;
import cn.zhengcaiyun.idata.portal.schedule.JobSchedule;
import cn.zhengcaiyun.idata.portal.util.PageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("查看任务历史")
    @PostMapping("/page")
    public RestResult<Page<DevJobHistory>> pagingJobHistory(@RequestBody PageWrapper<IdRequest> pageWrapper) {
        PageInfo<DevJobHistory> pageInfo = jobHistoryService.pagingJobHistory(pageWrapper.getCondition().getId(), pageWrapper.getPageNum(), pageWrapper.getPageSize());
        return RestResult.success(PageUtil.covertMine(pageInfo));
    }

    @ApiOperation("手动触发脚本")
    @PostMapping("/script")
    public RestResult<Boolean> pullSparkSqlJobHistory() {
        jobSchedule.pullSparkSqlJobHistory();
        return RestResult.success(true);
    }

}
