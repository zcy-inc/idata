package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.portal.model.request.IdRequest;
import cn.zhengcaiyun.idata.portal.model.request.PageWrapper;
import cn.zhengcaiyun.idata.portal.util.PageUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("历史作业")
@RestController
@RequestMapping("/p1/dev/jobs")
public class JobHistoryController {

    @Autowired
    private JobHistoryService jobHistoryService;

    @ApiOperation("查看任务历史")
    @GetMapping("/history")
    public RestResult<Page<DevJobHistory>> pagingJobHistory(PageWrapper<IdRequest> pageWrapper) {
        PageInfo<DevJobHistory> pageInfo = jobHistoryService.pagingJobHistory(pageWrapper.getCondition().getId(), pageWrapper.getPageNum(), pageWrapper.getPageSize());
        return RestResult.success(PageUtil.covertMine(pageInfo));
    }
}
