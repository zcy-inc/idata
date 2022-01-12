package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dto.job.JobInfoExecuteDetailDto;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoMyDao {
    JobInfoExecuteDetailDto selectJobInfoExecuteDetail(Long id, String env);
}
