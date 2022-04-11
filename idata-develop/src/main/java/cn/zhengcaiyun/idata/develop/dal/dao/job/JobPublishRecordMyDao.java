package cn.zhengcaiyun.idata.develop.dal.dao.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.*;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPublishRecordMyDao {

    DIJobContent getPublishedDiJobContent(Long jobId, String env);

    DevJobContentSql getPublishedSqlJobContent(Long jobId, String env);

    DevJobContentSpark getPublishedSparkJobContent(Long jobId, String env);

    DevJobContentKylin getPublishedKylinJobContent(Long jobId, String env);

    DevJobContentScript getPublishedScriptJobContent(Long jobId, String env);


}
