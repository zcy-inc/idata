package cn.zhengcaiyun.idata.operation.service.impl;

import cn.hutool.core.date.*;
import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.develop.constant.enums.DsJobStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.TaskIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskCountDto;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankResourceConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankTimeConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TaskIntegrator taskIntegrator;

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Autowired
    private JobHistoryService jobHistoryService;

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

    @Override
    public List<RankTimeConsumeDto> rankConsumeTime(DateTime startDate, DateTime endDate, int top) {
        DateTime today = DateUtil.date();
        List<RankTimeConsumeDto> rankTimeConsumeDtoList = new ArrayList<>();

        // 小于当天
        boolean pastTime = DateUtil.compare(startDate, today) < 0;
        if (pastTime) {
            // 包含过去的日期 ———— 查数据库
            List<JobHistoryDto> devJobHistoryList = jobHistoryService.topDuration(startDate, endDate, top);
            if (CollectionUtils.isNotEmpty(devJobHistoryList)) {
                rankTimeConsumeDtoList.addAll(devJobHistoryList.stream()
                        .map(e -> {
                            RankTimeConsumeDto dto = new RankTimeConsumeDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList()));
            }
        }

        // TODO：此处当天实时查询取出的数据已经被分组过了，所以如果一天内执行多次的话，平均时间会不准（当天短时间的任务被忽略掉）
        boolean isToday = DateUtil.isSameDay(today, endDate);
        if (isToday) {
            //包含查询当天的 ———— 实时查当天yarn
            LocalDateTime startLocalDate = DateUtil.beginOfDay(today).toLocalDateTime();
            LocalDateTime endLocalDate = DateUtil.endOfDay(today).toLocalDateTime();
            List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(startLocalDate, endLocalDate, EnvEnum.prod);
            Map<Long, Optional<ClusterAppDto>> map = list.stream().collect(Collectors.groupingBy(ClusterAppDto::getJobId, Collectors.maxBy(Comparator.comparingLong(ClusterAppDto::getElapsedTime))));

            map.forEach((k, v) -> {
                if (v.isPresent()) {
                    ClusterAppDto clusterAppDto = v.get();
                    RankTimeConsumeDto dto = new RankTimeConsumeDto();
                    dto.setDuration(clusterAppDto.getElapsedTime());
                    dto.setAvgDuration(clusterAppDto.getElapsedTime().doubleValue());
                    dto.setJobId(clusterAppDto.getJobId());
                    dto.setApplicationId(clusterAppDto.getAppId());
                    String startTimeStr = LocalDateTimeUtil.format(clusterAppDto.getStartedTime(), DatePattern.NORM_DATETIME_FORMATTER);
                    dto.setStartTime(DateUtil.parse(startTimeStr, DatePattern.NORM_DATETIME_PATTERN));
                    String finishTimeStr = LocalDateTimeUtil.format(clusterAppDto.getFinishedTime(), DatePattern.NORM_DATETIME_FORMATTER);
                    dto.setFinishTime(DateUtil.parse(finishTimeStr, DatePattern.NORM_DATETIME_PATTERN));
                    rankTimeConsumeDtoList.add(dto);
                }
            });
        }

        // 此处是优化，如果时间段不同时满足既包含当天也包含过去日期的，直接返回，不需要后续的再次内存中聚合计算
        if (!isToday || !pastTime) {
           return rankTimeConsumeDtoList;
        }

        // 再一次分组，确保jobId数据不重复，取出top的duration
        Map<Long, Optional<RankTimeConsumeDto>> map = rankTimeConsumeDtoList
                .stream()
                .collect(Collectors.groupingBy(RankTimeConsumeDto::getJobId, Collectors.maxBy(Comparator.comparingLong(RankTimeConsumeDto::getDuration))));
        // 再一次分组，重新计算jobId的avg_duration
        Map<Long, Double> avgDurationMap = rankTimeConsumeDtoList.stream().collect(Collectors.groupingBy(RankTimeConsumeDto::getJobId, Collectors.averagingLong(RankTimeConsumeDto::getDuration)));
        // 对Map(key/value)排序后取TopN
        List<RankTimeConsumeDto> rankTimeConsumeDtos = map.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().get().getDuration() >= e1.getValue().get().getDuration() ? 1 : -1)
                .map(entry -> entry.getValue().get())
                .limit(top)
                .collect(Collectors.toList());

        //重新设置avg_duration
        rankTimeConsumeDtos.forEach(e -> e.setAvgDuration(avgDurationMap.get(e.getJobId())));

        return rankTimeConsumeDtos;
    }

    @Override
    public List<RankResourceConsumeDto> rankConsumeResource(DateTime startDate, DateTime endDate, int top) {
        DateTime today = DateUtil.date();
        List<RankResourceConsumeDto> rankResourceConsumeDtoList = new ArrayList<>();

        // 小于当天
        boolean pastTime = DateUtil.compare(startDate, today) < 0;
        if (pastTime) {
            // 包含过去的日期 ———— 查数据库
            List<JobHistoryDto> devJobHistoryList = jobHistoryService.topResource(startDate, endDate, top);
            if (CollectionUtils.isNotEmpty(devJobHistoryList)) {
                rankResourceConsumeDtoList.addAll(devJobHistoryList.stream()
                        .map(e -> {
                            RankResourceConsumeDto dto = new RankResourceConsumeDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList()));
            }
        }

        boolean isToday = DateUtil.isSameDay(today, endDate);
        if (isToday) {
            //包含查询当天的 ———— 实时查当天yarn
            LocalDateTime startLocalDate = DateUtil.beginOfDay(today).toLocalDateTime();
            LocalDateTime endLocalDate = DateUtil.endOfDay(today).toLocalDateTime();
            List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(startLocalDate, endLocalDate, EnvEnum.prod);
            Map<Long, Optional<ClusterAppDto>> map = list.stream().collect(Collectors.groupingBy(ClusterAppDto::getJobId, Collectors.maxBy(Comparator.comparingLong(ClusterAppDto::getElapsedTime))));

            map.forEach((k, v) -> {
                if (v.isPresent()) {
                    ClusterAppDto clusterAppDto = v.get();
                    RankResourceConsumeDto dto = new RankResourceConsumeDto();

                    Long vcoreSeconds = clusterAppDto.getVcoreSeconds();
                    Double elapsedTime = clusterAppDto.getElapsedTime().doubleValue();
                    dto.setAvgVcores(NumberUtil.round(vcoreSeconds/elapsedTime*1000, 2).doubleValue());
                    dto.setAvgMemory(clusterAppDto.getMemorySeconds() / (clusterAppDto.getElapsedTime() / 1000));
                    dto.setJobId(clusterAppDto.getJobId());
                    dto.setApplicationId(clusterAppDto.getAppId());
                    String startTimeStr = LocalDateTimeUtil.format(clusterAppDto.getStartedTime(), DatePattern.NORM_DATETIME_FORMATTER);
                    dto.setStartTime(DateUtil.parse(startTimeStr, DatePattern.NORM_DATETIME_PATTERN));
                    String finishTimeStr = LocalDateTimeUtil.format(clusterAppDto.getFinishedTime(), DatePattern.NORM_DATETIME_FORMATTER);
                    dto.setFinishTime(DateUtil.parse(finishTimeStr, DatePattern.NORM_DATETIME_PATTERN));
                    rankResourceConsumeDtoList.add(dto);
                }
            });
        }

        // 此处是优化，如果时间段不同时满足既包含当天也包含过去日期的，直接返回，不需要后续的再次内存中聚合计算
        if (!isToday || !pastTime) {
            return rankResourceConsumeDtoList;
        }

        // 再一次分组，确保jobId数据不重复，取出top的avg_mem
        Map<Long, Optional<RankResourceConsumeDto>> map = rankResourceConsumeDtoList
                .stream()
                .collect(Collectors.groupingBy(RankResourceConsumeDto::getJobId, Collectors.maxBy(Comparator.comparingLong(RankResourceConsumeDto::getAvgMemory))));
        // 对Map(key/value)排序后取TopN
        List<RankResourceConsumeDto> rankTimeConsumeDtos = map.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().get().getAvgMemory() >= e1.getValue().get().getAvgMemory() ? 1 : -1)
                .map(entry -> entry.getValue().get())
                .limit(top)
                .collect(Collectors.toList());

        return rankTimeConsumeDtos;
    }

    private JobStatisticDto getYarnJobOverview(Date startTime, Date endTime) {
        List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(DateUtil.toLocalDateTime(startTime), DateUtil.toLocalDateTime(endTime), EnvEnum.prod);
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









