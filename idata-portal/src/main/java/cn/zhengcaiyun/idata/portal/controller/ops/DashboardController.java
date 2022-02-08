package cn.zhengcaiyun.idata.portal.controller.ops;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryGanttDto;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryTableGanttDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankResourceConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankTimeConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import cn.zhengcaiyun.idata.portal.model.request.PageWrapper;
import cn.zhengcaiyun.idata.portal.model.request.ops.JobHistoryGanttRequest;
import cn.zhengcaiyun.idata.portal.model.request.ops.JobHistoryRequest;
import cn.zhengcaiyun.idata.portal.model.request.ops.JobStateRequest;
import cn.zhengcaiyun.idata.portal.model.response.StackedLineChartResponse;
import cn.zhengcaiyun.idata.portal.model.response.NameValueResponse;
import cn.zhengcaiyun.idata.portal.model.response.ops.*;
import cn.zhengcaiyun.idata.portal.util.PageUtil;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.framework.qual.Unused;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping("/p1/ops/dashboard")
public class DashboardController {

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private JobHistoryService jobHistoryService;

    @Autowired
    private UserAccessService userAccessService;

    private final String OPS_DASHBOARD_ACCESS_CODE = "F_MENU_OPS_DASHBOARD";
    private final String JOB_HISTORY_ACCESS_CODE = "F_MENU_JOB_HISTORY";

    /**
     * 当前cpu的使用情况
     * @return
     */
    @GetMapping("/usageOverview")
    public RestResult<ResourceUsageResponse> usageOverview() {
        ClusterMetricsDto clusterMetricsDto = resourceManagerService.fetchClusterMetrics();
        ResourceUsageResponse response = new ResourceUsageResponse();
        BeanUtils.copyProperties(clusterMetricsDto, response);

        BigDecimal memUsageRate = NumberUtil.div(response.getAllocatedMem(), response.getTotalMem(), 4);
        response.setMemUsageRate(NumberUtil.round(NumberUtil.mul(memUsageRate, 100), 2) + "%");

        BigDecimal vCoreUsageRate = NumberUtil.div(response.getAllocatedVCores(), response.getTotalVCores(), 4);
        response.setvCoreUsageRate(NumberUtil.round(NumberUtil.mul(vCoreUsageRate, 100), 2) + "%");
        return RestResult.success(response);
    }

    /**
     * DS调度情况 看板
     * @return
     */
    @GetMapping("/jobSchedule/overview")
    public RestResult<JobOverviewResponse> jobDsOverview() throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), OPS_DASHBOARD_ACCESS_CODE)) {
            throw new IllegalAccessException("没有运维看板权限");
        }
        JobStatisticDto jobOverview = dashboardService.getDsTodayJobOverview(EnvEnum.prod.name());
        JobOverviewResponse response = new JobOverviewResponse();
        BeanUtils.copyProperties(jobOverview, response);

        // 设置总数
//        Integer total = dashboardService.getDsTotalJob(EnvEnum.prod.name());
        response.setTotal(response.calcTotal());

        //设置饼图
        response.getNameValueResponseList().add(new NameValueResponse<>("running", response.getRunning()));
        response.getNameValueResponseList().add(new NameValueResponse<>("failure", response.getFailure()));
        response.getNameValueResponseList().add(new NameValueResponse<>("success", response.getSuccess()));
        response.getNameValueResponseList().add(new NameValueResponse<>("other", response.getOther()));
        response.getNameValueResponseList().add(new NameValueResponse<>("ready", response.getReady()));

        return RestResult.success(response);
    }

    /**
     * ds作业分页
     * state 1：队列 2：运行中 6：失败  7：成功  -1：其他
     * @return
     */
    @PostMapping("/page/jobSchedule")
    public RestResult<Page<DsJobSummaryResponse>> pageJobSchedule(@RequestBody PageWrapper<JobStateRequest> pageWrapper)
            throws NoSuchFieldException {
        // 只查真线
        String env = EnvEnum.prod.name();
        Page<JobHistoryDto> pageJobSchedule = dashboardService.pageJobSchedule(env, pageWrapper.getCondition().getState(),
                pageWrapper.getPageNum(), pageWrapper.getPageSize());
        Page<DsJobSummaryResponse> responsePage = PageUtil.convertType(pageJobSchedule, s -> {
            DsJobSummaryResponse response = new DsJobSummaryResponse();
            response.setJobId(s.getJobId());
            response.setJobStatus(s.getFinalStatus());
            response.setJobName(s.getJobName());
            response.setTaskId(s.getJobInstanceId());
            response.setEnvironment(env);
            return response;
        });
        return RestResult.success(responsePage);
    }

    /**
     * ds调度情况堆叠折线图
     * @param scope : 作用域 yarn/ds
     * @return
     */
    @GetMapping("/job/stackLine")
    public RestResult<StackedLineChartResponse> dsJobScheduleLine(@RequestParam("scope") String scope) {
        Date today = DateUtil.beginOfDay(new Date());

        //近7天
        DateTime startDate = DateUtil.offsetDay(today, -6);
        SfStackedLineDto dsJobLine = dashboardService.getJobSfStackedLine(EnvEnum.prod.name(), startDate, today, scope);

        // 封装x轴
        StackedLineChartResponse response = new StackedLineChartResponse();
        response.setxAxis(dsJobLine.getxAxis());

        // 封装成功率和失败率两条y轴
        StackedLineChartResponse.YAxis yAxis = new StackedLineChartResponse.YAxis();
        yAxis.setName("成功率");
        yAxis.setyAxis(dsJobLine.getyAxisSuccessList());
        response.getyAxisList().add(yAxis);
        StackedLineChartResponse.YAxis yAxis2 = new StackedLineChartResponse.YAxis();
        yAxis2.setName("失败");
        yAxis2.setyAxis(dsJobLine.getyAxisFailList());
        response.getyAxisList().add(yAxis2);

        return RestResult.success(response);
    }

    /**
     * YARN调度情况 看板
     * @return
     */
    @GetMapping("/yarn/overview")
    public RestResult<JobOverviewResponse> jobYarnOverview() {
        JobStatisticDto jobOverview = dashboardService.getYarnTodayJobOverview();
        JobOverviewResponse response = new JobOverviewResponse();
        BeanUtils.copyProperties(jobOverview, response);

//        Integer totalJob = dashboardService.getYarnTotalJob();
        response.setTotal(response.calcTotal());

        //设置饼图
        response.getNameValueResponseList().add(new NameValueResponse<>("running", response.getRunning()));
        response.getNameValueResponseList().add(new NameValueResponse<>("failure", response.getFailure()));
        response.getNameValueResponseList().add(new NameValueResponse<>("success", response.getSuccess()));
        response.getNameValueResponseList().add(new NameValueResponse<>("other", response.getOther()));
        response.getNameValueResponseList().add(new NameValueResponse<>("ready", response.getReady()));

        return RestResult.success(response);
    }

    /**
     * yarn作业分页
     * @param
     * @return
     */
    @PostMapping("/page/yarn")
    public RestResult<Page<YarnJobSummaryResponse>> pageYarnJob(@RequestBody PageWrapper<JobStateRequest> pageWrapper)
            throws NoSuchFieldException {
        Integer state = pageWrapper.getCondition().getState();
        Page<JobHistoryDto> pageYarnJob = dashboardService.pageYarnJob(state, pageWrapper.getPageNum(), pageWrapper.getPageSize());
        Page<YarnJobSummaryResponse> responsePage = PageUtil.convertType(pageYarnJob, s -> {
            YarnJobSummaryResponse response = new YarnJobSummaryResponse();
            response.setJobId(s.getJobId());

            String jobStatus = s.getFinalStatus();
            if (StringUtils.equalsIgnoreCase(jobStatus, "UNDEFINED")) {
                jobStatus = s.getState();
            }
            response.setJobStatus(jobStatus);
            response.setJobName(s.getJobName());
            response.setAmContainerLogsUrl(s.getAmContainerLogsUrl());
            response.setBusinessLogsUrl(jobHistoryService.getBusinessLogUrl(s.getApplicationId(), s.getFinalStatus(), s.getState()));
            return response;
        });
        return RestResult.success(responsePage);
    }

    /**
     * 作业耗时TOP10
     * @param recentDays 1：当日 7：近7天 30；近30天
     * @return
     */
    @GetMapping("/rank/timeConsume")
    public RestResult<List<RankTimeConsumeResponse>> timeConsumeRank(@RequestParam("recentDays") Integer recentDays) throws NoSuchFieldException {
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        DateTime startDate = DateUtil.offsetDay(today, -recentDays + 1);
        List<RankTimeConsumeDto> rankTimeConsumeDtos = dashboardService.rankConsumeTime(startDate, today, 10);

        List<RankTimeConsumeResponse> responseList = new ArrayList<>();
        for (RankTimeConsumeDto dto : rankTimeConsumeDtos) {
            RankTimeConsumeResponse response = new RankTimeConsumeResponse();
            BeanUtils.copyProperties(dto, response);
            response.setBusinessLogsUrl(jobHistoryService.getBusinessLogUrl(dto.getApplicationId(), null, "FINISHED"));
            responseList.add(response);
        }

        return RestResult.success(responseList);
    }

    /**
     * 作业耗资源TOP10
     * @param recentDays 1：当日 7：近7天 30；近30天
     * @return
     */
    @GetMapping("/rank/resourceConsume")
    public RestResult<List<RankResourceConsumeResponse>> resourceConsumeRank(@RequestParam("recentDays") Integer recentDays) throws NoSuchFieldException {
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        DateTime startDate = DateUtil.offsetDay(today, -recentDays + 1);
        List<RankResourceConsumeDto> rankTimeConsumeDtos = dashboardService.rankConsumeResource(startDate, today, 10);

        List<RankResourceConsumeResponse> responseList = new ArrayList<>();
        for (RankResourceConsumeDto dto : rankTimeConsumeDtos) {
            RankResourceConsumeResponse response = new RankResourceConsumeResponse();
            BeanUtils.copyProperties(dto, response);
            response.setBusinessLogsUrl(jobHistoryService.getBusinessLogUrl(dto.getApplicationId(), null, "FINISHED"));
            responseList.add(response);
        }

        return RestResult.success(responseList);
    }

    /**
     * 作业历史查询
     * @param pageWrapper
     * @return
     */
    @PostMapping("/page/jobHistory")
    public RestResult<Page<JobHistoryResponse>> jobHistory(@RequestBody PageWrapper<JobHistoryRequest> pageWrapper) throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), JOB_HISTORY_ACCESS_CODE)) {
            throw new IllegalAccessException("没有作业历史查看权限");
        }
        JobHistoryRequest condition = pageWrapper.getCondition();

        List<String> finalStatusList = null;
        List<String> stateList = null;
        if (condition.getJobStatus() != null) {
            YarnJobStatusEnum statusEnum = YarnJobStatusEnum.getByValue(condition.getJobStatus());
            finalStatusList = Arrays.asList(statusEnum.finalStatus);
            stateList = Arrays.asList(statusEnum.states);
        }

        PageInfo<JobHistoryDto> pageInfo = jobHistoryService.pagingJobHistory(condition.getStartDateBegin(), condition.getStartDateEnd(),
                condition.getFinishDateBegin(), condition.getFinishDateEnd(), condition.getJobName(), finalStatusList, stateList,
                pageWrapper.getPageNum(), pageWrapper.getPageSize());

        PageInfo<JobHistoryResponse> responsePageInfo = PageUtil.convertType(pageInfo, s -> {
            JobHistoryResponse response = new JobHistoryResponse();
            BeanUtils.copyProperties(s, response);
            // MB转GB
            response.setAvgMemory(response.getAvgMemory()/1024);
            // 状态
            response.setBusinessStatus(YarnJobStatusEnum.getValueByStateAndFinalStatus(s.getState(), s.getFinalStatus()));
            return response;
        });

        return RestResult.success(PageUtil.covertMine(responsePageInfo));
    }

    /**
     * 作业历史查询甘特图
     * @param pageWrapper
     * @return
     */
    @PostMapping("/page/gantt/jobHistory")
    public RestResult<Page<JobHistoryTableGanttResponse>> ganttJobHistory(@RequestBody PageWrapper<JobHistoryGanttRequest> pageWrapper) {

        JobHistoryGanttRequest condition = pageWrapper.getCondition();
        PageInfo<JobHistoryGanttDto> pageInfo = jobHistoryService.pagingGanttJobHistory(condition.getStartDate(), condition.getLayerCode(),
                    condition.getDagId(), pageWrapper.getPageNum(), pageWrapper.getPageSize());

//        PageInfo<JobHistoryGanttResponse> responsePageInfo = PageUtil.convertType(pageInfo, s -> {
//            JobHistoryGanttResponse response = new JobHistoryGanttResponse();
//            BeanUtils.copyProperties(s, response);
//            return response;
//        });
//        return RestResult.success(PageUtil.covertMine(responsePageInfo));

        List<JobHistoryTableGanttDto> list = jobHistoryService.transform(pageInfo.getList());

        List<JobHistoryTableGanttResponse> responseList = list.stream()
                .map(e -> {
                    JobHistoryTableGanttResponse response = new JobHistoryTableGanttResponse();
                    BeanUtils.copyProperties(e, response);
                    return response;
                })
                .collect(Collectors.toList());

        PageInfo<JobHistoryTableGanttResponse> responsePageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, responsePageInfo);
        responsePageInfo.setList(responseList);
        return RestResult.success(PageUtil.covertMine(responsePageInfo));
    }


}
