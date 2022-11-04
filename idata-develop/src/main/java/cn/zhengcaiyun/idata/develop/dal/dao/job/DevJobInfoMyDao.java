package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dto.job.JobInfoExecuteDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface DevJobInfoMyDao {

    JobInfoExecuteDetailDto selectJobInfoExecuteDetail(Long id, String env);

    String selectDestFileType(Long id, String env);
}
