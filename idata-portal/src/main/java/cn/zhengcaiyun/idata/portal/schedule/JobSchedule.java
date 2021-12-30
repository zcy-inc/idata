package cn.zhengcaiyun.idata.portal.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.ResourceManageService;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.AppResourceDetail;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobSchedule {

    private static final String JOB_HISTORY_DAY_CRON = "0 0 8 * * ?";
    private static final String JOB_HISTORY_HOUR_CRON = "0 0/30 9-12 * * ?";

//    @Autowired
//    @Qualifier("yarnService")
//    private ResourceManageService yarnService;

    @Autowired
    private JobHistoryService jobHistoryService;

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Scheduled(cron = JOB_HISTORY_DAY_CRON)
    public void pullSparkSqlJobHistoryDay() {
        LocalDateTime endTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        LocalDateTime startTime = endTime.minusDays(1);
        pullJobHistory(startTime, endTime);
    }

    @Scheduled(cron = JOB_HISTORY_HOUR_CRON)
    public void pullSparkSqlJobHistoryHourly() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(3);
        pullJobHistory(startTime, endTime);
    }

    private void pullJobHistory(LocalDateTime startTime, LocalDateTime endTime) {

        List<ClusterAppDto> clusterAppDtoList = resourceManagerService.fetchClusterApps(startTime, endTime, null);
        List<DevJobHistory> list = clusterAppDtoList.stream().map(e -> {
                    DevJobHistory devJobHistory = new DevJobHistory();
                    devJobHistory.setJobId(e.getJobId());
                    devJobHistory.setStartTime(Date.from(e.getStartedTime().atZone(ZoneId.systemDefault()).toInstant()));
                    devJobHistory.setFinishTime(Date.from(e.getFinishedTime().atZone(ZoneId.systemDefault()).toInstant()));
                    devJobHistory.setDuration(e.getElapsedTime());
                    devJobHistory.setFinalStatus(e.getFinalStatus());
                    devJobHistory.setState(e.getState());
                    Long vcoreSeconds = e.getVcoreSeconds();
                    Double elapsedTime = e.getElapsedTime().doubleValue();
                    devJobHistory.setAvgVcores(NumberUtil.round(vcoreSeconds/elapsedTime*1000, 2).doubleValue());
                    devJobHistory.setAvgMemory(e.getMemorySeconds() / (e.getElapsedTime() / 1000));
                    devJobHistory.setApplicationId(e.getAppId());
                    devJobHistory.setUser(e.getUser());
                    devJobHistory.setAmContainerLogsUrl(e.getAmContainerLogs());

                    return devJobHistory;
                })
                .collect(Collectors.toList());
        jobHistoryService.batchUpsert(list);
    }


//    @Scheduled(cron = JOB_HISTORY_CRON)
//    @Deprecated
//    public void pullSparkSqlJobHistory() {
//        List<AppResourceDetail> appResourceDetailList = yarnService.loadAppResourceDetailList(DateUtil.yesterday().getTime(), DateUtil.date().getTime());
//        List<DevJobHistory> devJobHistoryList = appResourceDetailList
//                .stream()
//                .filter(e -> ReUtil.isMatch("SparkSQL-[p/s]-\\d*", e.getName()))
//                .map(e -> {
//                    DevJobHistory devJobHistory = new DevJobHistory();
//                    devJobHistory.setApplicationId(e.getId());
//
//                    // 抽取出SparkSQL-[p/s]-xxxx  xxxx(jobId)
//                    // di 、 kylin 、 script 、 spark 、sql
//                    String jobIdStr = ReUtil.get("SparkSQL-[p/s]-(\\d*)", e.getName(), 1);
//                    devJobHistory.setJobId(Long.valueOf(jobIdStr));
//                    devJobHistory.setStartTime(new Timestamp(e.getStartedTime()));
//                    devJobHistory.setFinishTime(new Timestamp(e.getFinishedTime()));
//                    devJobHistory.setFinalStatus(e.getFinalStatus());
//                    devJobHistory.setDuration(e.getElapsedTime());
//                    devJobHistory.setAvgMemory(e.getMemorySeconds() / (e.getElapsedTime() / 1000));
//                    Long vcoreSeconds = e.getVcoreSeconds();
//                    Double elapsedTime = e.getElapsedTime().doubleValue();
//                    devJobHistory.setAvgVcores(NumberUtil.round(vcoreSeconds/elapsedTime*1000, 2).doubleValue());
//                    return devJobHistory;
//                })
//                .collect(Collectors.toList());
//        jobHistoryService.batchUpsert(devJobHistoryList);
//    }


}
