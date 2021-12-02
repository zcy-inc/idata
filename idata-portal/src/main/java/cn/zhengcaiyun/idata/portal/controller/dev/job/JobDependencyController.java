package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.job.JobDependencyService;
import cn.zhengcaiyun.idata.portal.model.response.dag.JobTreeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("任务依赖")
@RestController
@RequestMapping("/p1/dev/jobs/dependency")
public class JobDependencyController {

    @Autowired
    private JobDependencyService jobDependencyService;

    @ApiOperation("加载树")
    @GetMapping("/{id}/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "jobId 任务id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "env", value = "环境", dataType = "String", required = true),
            @ApiImplicitParam(name = "preLevel", value = "上游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "nextLevel", value = "下游层数", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "name", value = "搜索任务名", dataType = "String", required = true)
    })
    public RestResult<JobTreeResponse> tree(@PathVariable("id") Long jobId,
                                            @RequestParam("env") String env,
                                            @RequestParam("preLevel") Integer preLevel,
                                            @RequestParam("nextLevel") Integer nextLevel,
                                            @RequestParam("name") String name) {
        Tuple2<JobTreeNodeDto, JobTreeNodeDto> tuple2 = jobDependencyService.loadTree(jobId, env, preLevel, nextLevel);
        JobTreeResponse response = new JobTreeResponse();
        JobTreeNodeDto prev = tuple2.e1;
        JobTreeNodeDto next = tuple2.e2;

        JobTreeResponse.JobNode rPrev = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(prev, rPrev);
        JobTreeResponse.JobNode rNext = new JobTreeResponse.JobNode();
        BeanUtils.copyProperties(next, rNext);

        response.setParents(rPrev.getNextList());
        response.setChildren(rNext.getNextList());
        response.setJobId(rPrev.getJobId());
        response.setJobName(rPrev.getJobName());

        return RestResult.success(response);
    }

//        http://ds.bigdata.cai-inc.com/dolphinscheduler/ui/#/projects/task-instance?pageSize=10&pageNo=1&searchVal=2344_ods.ods_db_charge_charge_business_register&processInstanceId=&host=&stateType=&startDate=&endDate=&executorName=
//        http://ds.bigdata.cai-inc.com/dolphinscheduler/projects/staging/task-instance/list-paging?pageSize=10&pageNo=1&searchVal=2344_ods.ods_db_charge_charge_business_register&processInstanceId=&host=&stateType=&startDate=&endDate=&executorName=&_t=0.1005426913490517

}
