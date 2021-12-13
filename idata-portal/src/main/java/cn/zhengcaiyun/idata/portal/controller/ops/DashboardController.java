package cn.zhengcaiyun.idata.portal.controller.ops;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankResourceConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankTimeConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import cn.zhengcaiyun.idata.portal.model.response.StackedLineChartResponse;
import cn.zhengcaiyun.idata.portal.model.response.NameValueResponse;
import cn.zhengcaiyun.idata.portal.model.response.ops.RankResourceConsumeResponse;
import cn.zhengcaiyun.idata.portal.model.response.ops.RankTimeConsumeResponse;
import cn.zhengcaiyun.idata.portal.model.response.ops.JobOverviewResponse;
import cn.zhengcaiyun.idata.portal.model.response.ops.ResourceUsageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/p1/ops/dashboard")
public class DashboardController {

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Autowired
    private DashboardService dashboardService;

    /**
     * 当前cpu的使用情况
     * @return
     */
    @GetMapping("/usageOverview")
    public RestResult<ResourceUsageResponse> usageOverview() {
        ClusterMetricsDto clusterMetricsDto = resourceManagerService.fetchClusterMetrics();
        ResourceUsageResponse response = new ResourceUsageResponse();
        BeanUtils.copyProperties(clusterMetricsDto, response);

        BigDecimal memUsageRate = NumberUtil.round(response.getAllocatedMem() / response.getTotalMem(), 2);
        response.setMemUsageRate(memUsageRate.doubleValue());

        BigDecimal vCoreUsageRate = NumberUtil.round(response.getAllocatedVCores() / response.getTotalVCores(), 2);
        response.setvCoreUsageRate(vCoreUsageRate.doubleValue());
        return RestResult.success(response);
    }

    /**
     * DS调度情况 看板
     * @return
     */
    @GetMapping("/jobSchedule/overview")
    public RestResult<JobOverviewResponse> jobDsOverview() {
        JobStatisticDto jobOverview = dashboardService.getDsTodayJobOverview(EnvEnum.prod.name());
        JobOverviewResponse response = new JobOverviewResponse();
        BeanUtils.copyProperties(jobOverview, response);

        // 设置总数
        Integer total = dashboardService.getDsTotalJob(EnvEnum.prod.name());
        response.setTotal(total);

        //设置饼图
        response.getNameValueResponseList().add(new NameValueResponse<>("total", response.getTotal()));
        response.getNameValueResponseList().add(new NameValueResponse<>("running", response.getRunning()));
        response.getNameValueResponseList().add(new NameValueResponse<>("failure", response.getFailure()));
        response.getNameValueResponseList().add(new NameValueResponse<>("success", response.getSuccess()));
        response.getNameValueResponseList().add(new NameValueResponse<>("other", response.getOther()));
        response.getNameValueResponseList().add(new NameValueResponse<>("ready", response.getReady()));

        return RestResult.success(response);
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

        Integer totalJob = dashboardService.getYarnTotalJob();
        response.setTotal(totalJob);

        //设置饼图
        response.getNameValueResponseList().add(new NameValueResponse<>("total", response.getTotal()));
        response.getNameValueResponseList().add(new NameValueResponse<>("running", response.getRunning()));
        response.getNameValueResponseList().add(new NameValueResponse<>("failure", response.getFailure()));
        response.getNameValueResponseList().add(new NameValueResponse<>("success", response.getSuccess()));
        response.getNameValueResponseList().add(new NameValueResponse<>("other", response.getOther()));
        response.getNameValueResponseList().add(new NameValueResponse<>("ready", response.getReady()));

        return RestResult.success(response);
    }

    /**
     * 作业耗时TOP10
     * @param recentDays 1：当日 7：近7天 30；近30天
     * @return
     */
    @GetMapping("/rank/timeConsume")
    public RestResult<List<RankTimeConsumeResponse>> timeConsumeRank(@RequestParam("recentDays") Integer recentDays) {
        DateTime today = DateUtil.date();
        DateTime startDate = DateUtil.offsetDay(today, -recentDays + 1);
        List<RankTimeConsumeDto> rankTimeConsumeDtos = dashboardService.rankConsumeTime(startDate, today, 10);

        List<RankTimeConsumeResponse> responseList = new ArrayList<>();
        for (RankTimeConsumeDto dto : rankTimeConsumeDtos) {
            RankTimeConsumeResponse response = new RankTimeConsumeResponse();
            BeanUtils.copyProperties(dto, response);
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
    public RestResult<List<RankResourceConsumeResponse>> resourceConsumeRank(@RequestParam("recentDays") Integer recentDays) {
        DateTime today = DateUtil.date();
        DateTime startDate = DateUtil.offsetDay(today, -recentDays + 1);
        List<RankResourceConsumeDto> rankTimeConsumeDtos = dashboardService.rankConsumeResource(startDate, today, 10);

        List<RankResourceConsumeResponse> responseList = new ArrayList<>();
        for (RankResourceConsumeDto dto : rankTimeConsumeDtos) {
            RankResourceConsumeResponse response = new RankResourceConsumeResponse();
            BeanUtils.copyProperties(dto, response);
            responseList.add(response);
        }

        return RestResult.success(responseList);
    }


}
