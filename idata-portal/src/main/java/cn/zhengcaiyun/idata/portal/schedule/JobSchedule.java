package cn.zhengcaiyun.idata.portal.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobSchedule {

    @Value("${dev.job.pull-yarn.cron}")
    private static final String JOB_HISTORY_CRON = "0 30 8 * * ?";

    @Autowired
    @Qualifier("yarnService")
    private ResourceManageService yarnService;

    @Autowired
    private JobHistoryService jobHistoryService;

    @Scheduled(cron = JOB_HISTORY_CRON)
    public void pullSparkSqlJobHistory() {
        List<AppResourceDetail> appResourceDetailList = yarnService.loadAppResourceDetailList(DateUtil.yesterday().getTime(), DateUtil.date().getTime());
        List<DevJobHistory> devJobHistoryList = appResourceDetailList
                .stream()
                .filter(e -> ReUtil.isMatch("SparkSQL-[p/s]-\\d*", e.getName()))
                .map(e -> {
                    DevJobHistory devJobHistory = new DevJobHistory();
                    devJobHistory.setApplicationId(e.getId());

                    // 抽取出SparkSQL-[p/s]-xxxx  xxxx(jobId)
                    String jobIdStr = ReUtil.get("SparkSQL-[p/s]-(\\d*)", e.getName(), 1);
                    devJobHistory.setJobId(Long.valueOf(jobIdStr));
                    devJobHistory.setStartTime(new Timestamp(e.getStartedTime()));
                    devJobHistory.setFinishTime(new Timestamp(e.getFinishedTime()));
                    devJobHistory.setFinalStatus(e.getFinalStatus());
                    devJobHistory.setDuration(e.getElapsedTime());
                    devJobHistory.setAvgMemory(e.getMemorySeconds() / (e.getElapsedTime() / 1000));
                    Long vcoreSeconds = e.getVcoreSeconds();
                    Double elapsedTime = e.getElapsedTime().doubleValue();
                    devJobHistory.setAvgVcores(NumberUtil.round(vcoreSeconds/elapsedTime*1000, 2).doubleValue());
                    return devJobHistory;
                })
                .collect(Collectors.toList());
        jobHistoryService.batchUpsert(devJobHistoryList);
    }

//    public static void main(String[] args) {
//        String s = "SparkSQL-q-1234";
//        System.out.println(StringUtils.startsWith(s, "SparkSQL-[]-"));
//
//        boolean isMatch = ReUtil.isMatch("SparkSQL-[p/s]-\\d*", s);
//        System.out.println(isMatch);
//
//        String resultDelFirst = ReUtil.get("SparkSQL-[p/s]-(\\d*)", s, 1);
//        System.out.println(resultDelFirst);
//    }


}
