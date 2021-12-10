package cn.zhengcaiyun.idata.operation.service;

import cn.zhengcaiyun.idata.operation.bean.dto.JobDsOverviewDto;

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
    JobDsOverviewDto getDsTodayJobOverview(String environment);
}
