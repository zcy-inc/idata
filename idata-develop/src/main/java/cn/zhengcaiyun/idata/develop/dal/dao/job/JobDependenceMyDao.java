package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dto.JobDependencyDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobDependenceMyDao {

    @Select("<script>" +
            "select job_id, prev_job_id, name, environment from" +
            "(" +
            "  select job_id, prev_job_id, environment from dev_job_dependence where del = 0 and environment = #{env}" +
            ") t1 left join (select id, name from dev_job_info) t2" +
            " on t1.job_id = t2.id" +
            "</script>")
    List<JobDependencyDto> queryJobs(String env);
}
