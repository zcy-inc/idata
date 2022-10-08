package cn.zhengcaiyun.idata.develop.service.table;


import cn.hutool.db.PageResult;
import cn.zhengcaiyun.idata.develop.dal.model.TableSibshipVO;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.query.TableSibshipQuery;
import cn.zhengcaiyun.idata.develop.dto.table.TableSibship;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author zheng
 * @since 2022-09-29 10:36:31
 */
public interface TableSibshipService {

    TableSibshipVO getByTableName(String tableName, Integer upper, Integer lower);

    Set<TableSibshipVO> getJobs(String tableName);

    void sibParse(Long jobId, Integer jobVersion, String jobTypeCode, String environment, String nickname);

    List<JobPublishRecord> getPublishedJobs(Integer startIndex);
}
