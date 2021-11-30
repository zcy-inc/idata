package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobHistoryServiceImpl implements JobHistoryService {

    @Autowired
    private DevJobHistoryDao devJobHistoryDao;


    @Override
    public void batchUpsert(List<DevJobHistory> devJobHistoryList) {

    }
}
