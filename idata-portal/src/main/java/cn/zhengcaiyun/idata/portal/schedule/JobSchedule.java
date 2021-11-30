package cn.zhengcaiyun.idata.portal.schedule;

import cn.hutool.core.date.DateUtil;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.ResourceManageService;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.AppResourceDetail;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void pullJobHistory() {
        List<AppResourceDetail> appResourceDetailList = yarnService.loadAppResourceDetail(DateUtil.yesterday().getTime(), DateUtil.date().getTime());
        List<DevJobHistory> devJobHistoryList = null;
        jobHistoryService.batchUpsert(devJobHistoryList);
    }

}
