package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import cn.zhengcaiyun.idata.develop.dal.query.JobOutputQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOutputMyDao {

    /**
     * 通用查询单条记录
     * @param query
     * @return
     */
    JobOutput queryOne(JobOutputQuery query);
}