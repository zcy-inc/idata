package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.job.JobDependencyService;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.portal.model.response.dag.JobTreeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("任务依赖")
@RestController
@RequestMapping("/p1/dev/jobs/dependency")
public class JobDependencyController {

    @Autowired
    private JobDependencyService jobDependencyService;

    @Autowired
    private JobScheduleManager jobScheduleManager;

    @Autowired
    private JobHistoryService jobHistoryService;


    @ApiOperation(value = "查询job")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchName", value = "searchName", required = false, dataType = "String")
    })
    @GetMapping("list")
    public RestResult<List<JobInfo>> getJobInfo(@RequestParam(value = "searchName", required = false) String searchName,
                                                @RequestParam("env") String env,
                                                @RequestParam(value = "jobId") Long jobId) {
        List<JobInfo> list = jobDependencyService.getDependencyJob(searchName, jobId, env);
        return RestResult.success(list);
    }

    /**
     * 任务最后运行时间
     * @param jobId
     * @return
     */
    @GetMapping("/{id}/latestRuntime")
    public RestResult<String> getLatestRuntime(@PathVariable("id") Long jobId) {
        String dateTime = jobHistoryService.getLatestRuntime(jobId);
        return RestResult.success(dateTime);
    }

    @ApiOperation("加载树")
    @GetMapping("/{id}/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "preLevel", value = "上游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "nextLevel", value = "下游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "searchJobId", value = "搜索任务id", dataType = "Long", required = true)
    })
    public RestResult<JobTreeResponse> tree(@PathVariable("id") Long jobId,
                                            @RequestParam("env") String env,
                                            @RequestParam("preLevel") Integer preLevel,
                                            @RequestParam("nextLevel") Integer nextLevel,
                                            @RequestParam(value = "searchJobId", required = false) Long searchJobId) {
        Tuple2<JobTreeNodeDto, JobTreeNodeDto> tuple2 = jobDependencyService.loadTree(jobId, env, preLevel, nextLevel, searchJobId);
        JobTreeResponse response = new JobTreeResponse();
        JobTreeNodeDto prev = tuple2.e1;
        JobTreeNodeDto next = tuple2.e2;

        JobTreeResponse.JobNode rPrev = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(prev, rPrev);
        JobTreeResponse.JobNode rNext = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(next, rNext);

        //设置层级
        response.setPrevLevel(prev.getLevel());
        response.setNextLevel(next.getLevel());

        if (CollectionUtils.isNotEmpty(rPrev.getChildren())) {
            response.getChildren().addAll(rPrev.getChildren());
        }
        if (CollectionUtils.isNotEmpty(rNext.getChildren())) {
            response.getChildren().addAll(rNext.getChildren());
        }
        response.setJobId(rPrev.getJobId());
        response.setJobName(rPrev.getJobName());

        return RestResult.success(response);
    }

    @ApiOperation("查看作业日志")
    @GetMapping("/{id}/running/log")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "lineNum", value = "查看行数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "skipLineNum", value = "跳过行数", dataType = "String", required = false)
    })
    public RestResult<String> getRunningLog(@PathVariable(value = "id", required = true) Long jobId,
                                            @RequestParam(value = "env", required = true) String env,
                                            @RequestParam(value = "taskId", required = true) Integer taskId,
                                            @RequestParam(value = "lineNum", required = false, defaultValue = "100") Integer lineNum,
                                            @RequestParam(value = "skipLineNum", required = false, defaultValue = "0")Integer skipLineNum) {
        String log = jobScheduleManager.queryJobRunningLog(jobId, env, taskId, lineNum, skipLineNum);
        return RestResult.success(log);
    }

    @ApiOperation("重跑作业")
    @GetMapping("/{id}/rerun")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "runPost", value = "是否重跑下游", dataType = "Boolean", required = true)
    })
    public RestResult<Boolean> rerun(@PathVariable(value = "id", required = true) Long jobId,
                                            @RequestParam(value = "env", required = true) String env,
                                            @RequestParam(value = "runPost", required = false, defaultValue = "false") boolean runPost) {
        jobScheduleManager.runJob(jobId, env, runPost);
        return RestResult.success(true);
    }

}
