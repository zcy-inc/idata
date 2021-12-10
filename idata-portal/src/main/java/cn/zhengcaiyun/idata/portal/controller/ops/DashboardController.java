package cn.zhengcaiyun.idata.portal.controller.ops;

import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import cn.zhengcaiyun.idata.portal.model.response.opr.JobDsOverviewResponse;
import cn.zhengcaiyun.idata.portal.model.response.opr.JobYarnOverviewResponse;
import cn.zhengcaiyun.idata.portal.model.response.opr.ResourceUsageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/p1/opr/monitor")
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
    @GetMapping("/jobScheduleOverview")
    public RestResult<JobDsOverviewResponse> jobDsOverview() {
        Integer total = dashboardService.getDsTotalJob(EnvEnum.prod.name());
        JobDsOverviewResponse response = new JobDsOverviewResponse();
        response.setTotal(total);

        dashboardService.getDsTodayJobOverview(EnvEnum.prod.name());

        return null;
    }

    /**
     * YARN调度情况 看板
     * @return
     */
    @GetMapping("/yarnOverview")
    public RestResult<JobYarnOverviewResponse> jobYarnOverview() {
        return null;
    }

}
