package cn.zhengcaiyun.idata.operation.service;

import cn.hutool.core.date.DateTime;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;

import java.util.Date;

public interface DashboardService {

    /**
     * 获取DS总的作业数
     * @param environment
     * @return
     */
    Integer getDsTotalJob(String environment);

    /**
     * 获取今日DS调度作业情况概览
     * @param environment
     * @return
     */
    JobStatisticDto getDsTodayJobOverview(String environment);

    /**
     * 获取今日DS调度作业情况概览
     * @param environment
     * @param startTime
     * @param endTime \
     * @return
     */
    JobStatisticDto getDsJobOverview(String environment, Date startTime, Date endTime);

    /**
     * 获取成功、失败率折线图
     * @param environment
     * @param startDate
     * @param endDate
     * @param scope
     */
    SfStackedLineDto getJobSfStackedLine(String environment, DateTime startDate, Date endDate, String scope);

    /**
     * 获取Yarn总任务数
     */
    Integer getYarnTotalJob();

    /**
     * 获取今日YARN调度作业情况概览
     * @return
     */
    JobStatisticDto getYarnTodayJobOverview();
}
