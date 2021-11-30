package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
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


}