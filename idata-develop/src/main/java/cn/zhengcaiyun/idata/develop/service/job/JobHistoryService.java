package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;

import java.util.List;

public interface JobHistoryService {

    /**
     * 批量upsert
     * @param devJobHistoryList
     */
    void batchUpsert(List<DevJobHistory> devJobHistoryList);

}
