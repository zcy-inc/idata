package cn.zhengcaiyun.idata.portal.schedule;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import cn.zhengcaiyun.idata.develop.util.MyBeanUtils;
import cn.zhengcaiyun.idata.portal.model.response.job.JobLifeResponse;
import org.apache.hadoop.mapreduce.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanqian
 * @date 2022/9/28 10:23 AM
 * @description 作业生命周期
 */
@Component
public class JobLifecycleSchedule {

    private static final String COLUMN_SECURITY_CRON = "0 0 10 * * ?";

    @Autowired
    private JobInfoService jobInfoService;

//    @Scheduled(cron = COLUMN_SECURITY_CRON)
    public void checkJobLifecycle() {
        DateTime beginDate = DateUtil.beginOfDay(new Date());
        DateTime endDate = DateUtil.endOfDay(new Date());

        // 获取当前需要邮件通知的作业
        DateTime beginDate1 = DateUtil.offset(beginDate, DateField.DAY_OF_MONTH, 1);
        DateTime endDate1 = DateUtil.offset(endDate, DateField.DAY_OF_MONTH, 1);
        DateTime beginDate7 = DateUtil.offset(beginDate, DateField.DAY_OF_MONTH, 7);
        DateTime endDate7 = DateUtil.offset(endDate, DateField.DAY_OF_MONTH, 7);
        DateTime beginDate21 = DateUtil.offset(beginDate, DateField.DAY_OF_MONTH, 21);
        DateTime endDate21 = DateUtil.offset(endDate, DateField.DAY_OF_MONTH, 21);

        List<JobLifeResponse> list = new ArrayList<>();
        List<JobInfo> subList1 = jobInfoService.checkAlarmJobList(beginDate1.toJdkDate(), endDate1.toJdkDate());
        list.addAll(MyBeanUtils.copyList(subList1, JobLifeResponse.class));

        List<JobInfo> subList7 = jobInfoService.checkAlarmJobList(beginDate7.toJdkDate(), endDate7.toJdkDate());
        list.addAll(MyBeanUtils.copyList(subList7, JobLifeResponse.class));

        List<JobInfo> subList21 = jobInfoService.checkAlarmJobList(beginDate21.toJdkDate(), endDate21.toJdkDate());
        list.addAll(MyBeanUtils.copyList(subList21, JobLifeResponse.class));


    }

    public static void main(String[] args) {
        System.out.println();
    }
}
