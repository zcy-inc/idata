package cn.zhengcaiyun.idata.develop.dal.repo.job.impl;

import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlMyDao;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobContentSqlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JobContentSqlRepoImpl implements JobContentSqlRepo {

    @Autowired
    private DevJobContentSqlMyDao devJobContentSqlMyDao;

    @Override
    public Boolean ifBindUDF(Long udfId) {
        return devJobContentSqlMyDao.existOne(udfId) > 0;
    }
}
