package cn.zhengcaiyun.idata.operation.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.develop.constant.enums.DsJobStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.TaskIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskCountDto;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TaskIntegrator taskIntegrator;

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Override
    public Integer getDsTotalJob(String environment) {
        List<TaskCountDto> list = taskIntegrator.getTaskCountGroupState(environment, null, null);
        return list.stream().mapToInt(TaskCountDto::getCount).sum();
    }

    @Override
    public JobStatisticDto getDsTodayJobOverview(String environment) {
        return getDsJobOverview(environment, DateUtil.beginOfDay(new Date()), null);
    }

    @Override
    public JobStatisticDto getDsJobOverview(String environment, Date startTime, Date endTime) {
        List<TaskCountDto> list = taskIntegrator.getTaskCountGroupState(environment, startTime, endTime);
        HashMap<DsJobStatusEnum, Integer> map = Maps.newHashMap();
        list.forEach(e -> {
            DsJobStatusEnum dsJobStatusEnum = DsJobStatusEnum.getByDsEnumCode(e.getTaskStateType());
            map.put(dsJobStatusEnum, map.getOrDefault(dsJobStatusEnum, 0) + e.getCount());
        });

        JobStatisticDto dto = new JobStatisticDto();
        dto.setFailure(map.getOrDefault(DsJobStatusEnum.FAIL, 0));
        dto.setOther(map.getOrDefault(DsJobStatusEnum.OTHER, 0));
        dto.setRunning(map.getOrDefault(DsJobStatusEnum.RUNNING, 0));
        dto.setReady(map.getOrDefault(DsJobStatusEnum.READY, 0));
        dto.setSuccess(map.getOrDefault(DsJobStatusEnum.SUCCESS, 0));
        return dto;
    }

    @Override
    public SfStackedLineDto getJobSfStackedLine(String environment, DateTime startDate, Date endDate, String scope) {
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        // x轴
        List<String> xAxis = new ArrayList<>();
        // y轴-成功率
        List<String> successSubList = new ArrayList<>();
        // y轴-失败率
        List<String> failSubList = new ArrayList<>();

        for (int i = 0; i <= betweenDay; i++) {
            DateTime dateTime = DateUtil.offsetDay(startDate, i);
            JobStatisticDto dsOverviewDto = getStatisticByScope(scope, environment, DateUtil.beginOfDay(dateTime).toJdkDate(), DateUtil.endOfDay(dateTime).toJdkDate());

            xAxis.add(dateTime.toDateStr());

            BigDecimal fail = NumberUtil.round(dsOverviewDto.getFailure() / dsOverviewDto.getSum(), 2);
            BigDecimal success = NumberUtil.round(dsOverviewDto.getSuccess() / dsOverviewDto.getSum(), 2);
            successSubList.add(fail.toString() + "%");
            failSubList.add(success.toString() + "%");
        }

        SfStackedLineDto dto = new SfStackedLineDto();
        dto.setxAxis(xAxis);
        dto.setyAxisFailList(failSubList);
        dto.setyAxisSuccessList(successSubList);
        return dto;
    }

    /**
     * 根据scope获取统计数据
     */
    private JobStatisticDto getStatisticByScope(String scope, String environment, Date startTime, Date endTime) {
        if (StringUtils.equalsIgnoreCase(scope, "ds")) {
            return getDsJobOverview(environment, startTime, endTime);
        }
        if (StringUtils.equalsIgnoreCase(scope, "yarn")) {
            return getYarnJobOverview(startTime, endTime);
        }
        throw new GeneralException("error scope :" + scope);
    }

    @Override
    public Integer getYarnTotalJob() {
        ClusterMetricsDto clusterMetricsDto = resourceManagerService.fetchClusterMetrics();
        Integer failedJobs = clusterMetricsDto.getFailedJobs() == null ? 0 : clusterMetricsDto.getFailedJobs();
        Integer submittedJobs = clusterMetricsDto.getSubmittedJobs() == null ? 0 : clusterMetricsDto.getSubmittedJobs();
        Integer completedJobs = clusterMetricsDto.getCompletedJobs() == null ? 0 : clusterMetricsDto.getCompletedJobs();
        Integer killedJobs = clusterMetricsDto.getKilledJobs() == null ? 0 : clusterMetricsDto.getKilledJobs();
        Integer runningJobs = clusterMetricsDto.getRunningJobs() == null ? 0 : clusterMetricsDto.getRunningJobs();
        Integer pendingJobs = clusterMetricsDto.getPendingJobs() == null ? 0 : clusterMetricsDto.getPendingJobs();
        return failedJobs + submittedJobs + completedJobs + killedJobs + runningJobs + pendingJobs;
    }

    @Override
    public JobStatisticDto getYarnTodayJobOverview() {
        return getYarnJobOverview(DateUtil.beginOfDay(new Date()), null);
    }

    private JobStatisticDto getYarnJobOverview(Date startTime, Date endTime) {
        List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(DateUtil.toLocalDateTime(startTime), DateUtil.toLocalDateTime(endTime));
        HashMap<YarnJobStatusEnum, Integer> map = Maps.newHashMap();
        list.forEach(e -> {
            YarnJobStatusEnum dsJobStatusEnum = YarnJobStatusEnum.getByYarnEnumCode(e.getState());
            map.put(dsJobStatusEnum, map.getOrDefault(dsJobStatusEnum, 0) + 1);
        });

        JobStatisticDto dto = new JobStatisticDto();
        dto.setFailure(map.getOrDefault(DsJobStatusEnum.FAIL, 0));
        dto.setOther(map.getOrDefault(DsJobStatusEnum.OTHER, 0));
        dto.setRunning(map.getOrDefault(DsJobStatusEnum.RUNNING, 0));
        dto.setReady(map.getOrDefault(DsJobStatusEnum.READY, 0));
        dto.setSuccess(map.getOrDefault(DsJobStatusEnum.SUCCESS, 0));
        return dto;
    }

}









