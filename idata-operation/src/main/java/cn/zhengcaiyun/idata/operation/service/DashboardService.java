package cn.zhengcaiyun.idata.operation.service;

import cn.hutool.core.date.DateTime;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.operation.bean.dto.JobStatisticDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankResourceConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.RankTimeConsumeDto;
import cn.zhengcaiyun.idata.operation.bean.dto.SfStackedLineDto;

import java.util.Date;
import java.util.List;

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

    /**
     * 作业耗时TOP N
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param top TOP N
     */
    List<RankTimeConsumeDto> rankConsumeTime(DateTime startDate, DateTime endDate, int top) throws NoSuchFieldException;

    /**
     * 作业耗资源 TOP N
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param top TOP N
     */
    List<RankResourceConsumeDto> rankConsumeResource(DateTime startDate, DateTime endDate, int top) throws NoSuchFieldException;

    /**
     * yarn作业分页
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<JobHistoryDto> pageYarnJob(Integer state, Integer pageNum, Integer pageSize) throws NoSuchFieldException;

    /**
     * ds作业分页　
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<JobHistoryDto> pageJobSchedule(String env, Integer state, Integer pageNum, Integer pageSize) throws NoSuchFieldException;
}
