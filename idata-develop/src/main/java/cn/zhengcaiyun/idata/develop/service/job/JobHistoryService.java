package cn.zhengcaiyun.idata.develop.service.job;

import cn.hutool.core.date.DateTime;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface JobHistoryService {

    /**
     * 批量upsert
     *
     * @param devJobHistoryList
     */
    void batchUpsert(List<DevJobHistory> devJobHistoryList);

    /**
     * 分页任务历史
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<DevJobHistory> pagingJobHistory(Long id, Integer pageNum, Integer pageSize);

    /**
     * topN分组耗时duration
     * @param startDate
     * @param endDate
     * @param top
     * @return
     */
    List<JobHistoryDto> topDuration(DateTime startDate, DateTime endDate, int top);

    /**
     * topN分组耗时cpu memory
     * @param startDate
     * @param endDate
     * @param top
     * @return
     */
    List<JobHistoryDto> topResource(DateTime startDate, DateTime endDate, int top);
}