package cn.zhengcaiyun.idata.operation.service.impl;

import cn.hutool.core.date.*;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.PaginationInMemory;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterMetricsDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.develop.constant.enums.DsJobStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.TaskIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.JobRunOverviewDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskCountDto;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
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
    private JobScheduleManager jobScheduleManager;

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Autowired
    private JobHistoryService jobHistoryService;

    @Autowired
    private JobInfoService jobInfoService;

    @Override
    public Integer getDsTotalJob(String environment) {
        List<TaskCountDto> list = taskIntegrator.getTaskCountGroupState(environment, null, null);
        return list.stream().mapToInt(TaskCountDto::getCount).sum();
    }

    @Override
    public JobStatisticDto getDsTodayJobOverview(String environment) {
        return getDsJobOverview(environment, DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date()));
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

            xAxis.add(dateTime.toString("MM-dd"));

            Integer sum = dsOverviewDto.getSum();
            if (sum == 0) {
                failSubList.add("0%");
                successSubList.add("0%");
                continue;
            }
            BigDecimal fail = NumberUtil.div(dsOverviewDto.getFailure(), sum, 4);
            failSubList.add(NumberUtil.round(NumberUtil.mul(fail, 100), 2) + "%");

            BigDecimal success = NumberUtil.div(dsOverviewDto.getSuccess(), sum, 4);
            successSubList.add(NumberUtil.round(NumberUtil.mul(success, 100), 2) + "%");
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
    public List<RankTimeConsumeDto> rankConsumeTime(DateTime startDate, DateTime endDate, int top) throws NoSuchFieldException {
        DateTime today = DateUtil.date();
        List<RankTimeConsumeDto> pastRankTimeConsumeDtoList = new ArrayList<>();

        // 包含过去
        boolean pastTime = DateUtil.compare(startDate, today) < 0;
        // 包含当天
        boolean isToday = DateUtil.isSameDay(today, endDate);
        if (pastTime) {
            // 包含过去的日期 ———— 查数据库
            List<JobHistoryDto> devJobHistoryList = jobHistoryService.topDuration(startDate, endDate, top);
            if (CollectionUtils.isNotEmpty(devJobHistoryList)) {
                pastRankTimeConsumeDtoList.addAll(devJobHistoryList.stream()
                        .map(e -> {
                            RankTimeConsumeDto dto = new RankTimeConsumeDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList()));
            }
        }
        // 如果仅包含过去，直接返回
        if (pastTime && !isToday) {
            return pastRankTimeConsumeDtoList;
        }

        List<RankTimeConsumeDto> todayRankTimeConsumeDtoList = new ArrayList<>();
        // TODO：此处当天实时查询取出的数据已经被分组过了，所以如果一天内执行多次的话，平均时间会不准（当天短时间的任务被忽略掉）
        if (isToday) {
            //包含查询当天的 ———— 实时查当天yarn
            LocalDateTime startLocalDate = DateUtil.beginOfDay(today).toLocalDateTime();
            LocalDateTime endLocalDate = DateUtil.endOfDay(today).toLocalDateTime();
            List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(startLocalDate, endLocalDate, EnvEnum.prod);
            Map<Long, Optional<ClusterAppDto>> map = list.stream().collect(Collectors.groupingBy(ClusterAppDto::getJobId, Collectors.maxBy(Comparator.comparingLong(ClusterAppDto::getElapsedTime))));

            List<RankTimeConsumeDto> tmpList = new ArrayList<>();
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
                    dto.setAmContainerLogsUrl(clusterAppDto.getAmContainerLogs());
                    tmpList.add(dto);
                }
            });
            todayRankTimeConsumeDtoList = tmpList.stream().sorted((e1, e2) -> e2.getDuration() >= e1.getDuration() ? 1 : -1).limit(top).collect(Collectors.toList());
        }

        // 如果仅包含当天，直接返回
        if (isToday && !pastTime) {
            // 排序再
           return todayRankTimeConsumeDtoList;
        }

        // 再一次分组，确保jobId数据不重复，取出top的duration
        List<RankTimeConsumeDto> rankTimeConsumeDtoList = new ArrayList<>();
        rankTimeConsumeDtoList.addAll(pastRankTimeConsumeDtoList);
        rankTimeConsumeDtoList.addAll(todayRankTimeConsumeDtoList);

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

        jobInfoService.fillJobName(rankTimeConsumeDtos, RankTimeConsumeDto.class, "jobId", "jobName");
        return rankTimeConsumeDtos;
    }

    @Override
    public List<RankResourceConsumeDto> rankConsumeResource(DateTime startDate, DateTime endDate, int top) throws NoSuchFieldException {
        DateTime today = DateUtil.date();
        List<RankResourceConsumeDto> pastRankResourceConsumeDtoList = new ArrayList<>();

        // 包含过去
        boolean pastTime = DateUtil.compare(startDate, today) < 0;
        // 包含当天
        boolean isToday = DateUtil.isSameDay(today, endDate);
        if (pastTime) {
            // 包含过去的日期 ———— 查数据库
            List<JobHistoryDto> devJobHistoryList = jobHistoryService.topResource(startDate, endDate, top);
            if (CollectionUtils.isNotEmpty(devJobHistoryList)) {
                pastRankResourceConsumeDtoList.addAll(devJobHistoryList.stream()
                        .map(e -> {
                            RankResourceConsumeDto dto = new RankResourceConsumeDto();
                            BeanUtils.copyProperties(e, dto);
                            return dto;
                        }).collect(Collectors.toList()));
            }
        }
        // 如果仅包含过去，直接返回
        if (pastTime && !isToday) {
            return pastRankResourceConsumeDtoList;
        }

        List<RankResourceConsumeDto> todayRankResourceConsumeDtoList = new ArrayList<>();
        if (isToday) {
            //包含查询当天的 ———— 实时查当天yarn
            LocalDateTime startLocalDate = DateUtil.beginOfDay(today).toLocalDateTime();
            LocalDateTime endLocalDate = DateUtil.endOfDay(today).toLocalDateTime();
            List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(startLocalDate, endLocalDate, EnvEnum.prod);
            Map<Long, Optional<ClusterAppDto>> map = list.stream().collect(Collectors.groupingBy(ClusterAppDto::getJobId, Collectors.maxBy(Comparator.comparingLong(ClusterAppDto::getMemorySeconds))));

            List<RankResourceConsumeDto> tmpList = new ArrayList<>();
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
                    dto.setAmContainerLogsUrl(clusterAppDto.getAmContainerLogs());
                    tmpList.add(dto);
                }
            });
            todayRankResourceConsumeDtoList = tmpList.stream().sorted((e1, e2) -> e2.getAvgMemory() >= e1.getAvgMemory() ? 1 : -1).limit(top).collect(Collectors.toList());
        }

        // 如果仅包含当天，直接返回
        if (!isToday || !pastTime) {
            return todayRankResourceConsumeDtoList;
        }

        // 再一次分组，确保jobId数据不重复，取出top的avg_mem
        List<RankResourceConsumeDto> rankResourceConsumeDtoList = new ArrayList<>();
        rankResourceConsumeDtoList.addAll(pastRankResourceConsumeDtoList);
        rankResourceConsumeDtoList.addAll(todayRankResourceConsumeDtoList);

        Map<Long, Optional<RankResourceConsumeDto>> map = rankResourceConsumeDtoList
                .stream()
                .collect(Collectors.groupingBy(RankResourceConsumeDto::getJobId, Collectors.maxBy(Comparator.comparingLong(RankResourceConsumeDto::getAvgMemory))));
        // 对Map(key/value)排序后取TopN
        List<RankResourceConsumeDto> rankResourceConsumeDtos = map.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().get().getAvgMemory() >= e1.getValue().get().getAvgMemory() ? 1 : -1)
                .map(entry -> entry.getValue().get())
                .limit(top)
                .collect(Collectors.toList());

        jobInfoService.fillJobName(rankResourceConsumeDtos, RankResourceConsumeDto.class, "jobId", "jobName");
        return rankResourceConsumeDtos;
    }

    @Override
    public Page<JobHistoryDto> pageYarnJob(Integer state, Integer pageNum, Integer pageSize) throws NoSuchFieldException {
        LocalDateTime startTime = DateUtil.beginOfDay(new Date()).toLocalDateTime();
        LocalDateTime endTime = DateUtil.endOfDay(new Date()).toLocalDateTime();
        List<ClusterAppDto> clusterAppDtoList = resourceManagerService.fetchClusterApps(startTime, endTime, null);
        List<String> codeList = YarnJobStatusEnum.getCodesByValue(state);
        List<JobHistoryDto> dtoList = clusterAppDtoList
                .stream()
                .filter(e -> codeList.contains(StringUtils.upperCase(e.getState())))
                .map(e -> {
                    JobHistoryDto dto = new JobHistoryDto();
                    dto.setJobId(e.getJobId());
                    dto.setFinalStatus(e.getState());
                    dto.setAmContainerLogsUrl(e.getAmContainerLogs());
                    return dto;
                }).collect(Collectors.toList());
        Page<JobHistoryDto> page = PaginationInMemory.of(dtoList).paging(PageParam.of(pageNum, pageSize));
        jobInfoService.fillJobName(page.getContent(), JobHistoryDto.class, "jobId", "jobName");
        return page;
    }

    @Override
    public Page<JobHistoryDto> pageJobSchedule(String env, Integer state, Integer pageNum, Integer pageSize) throws NoSuchFieldException {
        List<JobRunOverviewDto> jobLatestRecordList = jobScheduleManager.getJobLatestRecords(env, 5000);
        List<String> stringList = DsJobStatusEnum.getDsEnumCodeByValue(state);
        List<JobHistoryDto> dtoList = jobLatestRecordList
                .stream()
                .filter(e -> stringList.contains(StringUtils.upperCase(e.getState()))
                        && (e.getEndTime() != null && DateUtil.parse(e.getEndTime()).after(DateUtil.beginOfDay(new Date()))))
                .map(e -> {
                    JobHistoryDto dto = new JobHistoryDto();
                    dto.setJobId(e.getJobId());
                    dto.setJobInstanceId(e.getId());
                    dto.setFinalStatus(e.getState());
                    return dto;
                }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dtoList)) {
            Page<JobHistoryDto> page = new Page<>();
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setPages(0);
            page.setContent(new ArrayList<>());
            return page;
        }
        Page<JobHistoryDto> page = PaginationInMemory.of(dtoList).paging(PageParam.of(pageNum, pageSize));
        jobInfoService.fillJobName(page.getContent(), JobHistoryDto.class, "jobId", "jobName");
        return page;
    }

    private JobStatisticDto getYarnJobOverview(Date startTime, Date endTime) {
        List<ClusterAppDto> list = resourceManagerService.fetchClusterApps(DateUtil.toLocalDateTime(startTime), DateUtil.toLocalDateTime(endTime), EnvEnum.prod);
        HashMap<YarnJobStatusEnum, Integer> map = Maps.newHashMap();
        list.forEach(e -> {
            YarnJobStatusEnum dsJobStatusEnum = YarnJobStatusEnum.getByYarnEnumCode(e.getState());
            map.put(dsJobStatusEnum, map.getOrDefault(dsJobStatusEnum, 0) + 1);
        });

        JobStatisticDto dto = new JobStatisticDto();
        dto.setFailure(map.getOrDefault(YarnJobStatusEnum.FAIL, 0));
        dto.setOther(map.getOrDefault(YarnJobStatusEnum.OTHER, 0));
        dto.setRunning(map.getOrDefault(YarnJobStatusEnum.RUNNING, 0));
        dto.setReady(map.getOrDefault(YarnJobStatusEnum.PENDING, 0));
        dto.setSuccess(map.getOrDefault(YarnJobStatusEnum.SUCCESS, 0));
        return dto;
    }

}







