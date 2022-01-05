package cn.zhengcaiyun.idata.develop.service.job;

import cn.hutool.core.date.DateTime;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryGanttDto;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryTableGanttDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
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
    PageInfo<DevJobHistory> pagingJobHistoryByJobId(Long jobId, Integer pageNum, Integer pageSize);

    /**
     * topN分组耗时duration
     * @param startDate
     * @param endDate
     * @param top
     * @return
     */
    List<JobHistoryDto> topDuration(DateTime startDate, DateTime endDate, int top);

    /**
     * topN分组耗时cpu memory
     * @param startDate
     * @param endDate
     * @param top
     * @return
     */
    List<JobHistoryDto> topResource(DateTime startDate, DateTime endDate, int top);

    /**
     * 作业历史分页
     * @param startDateBegin
     * @param startDateEnd
     * @param finishDateBegin
     * @param finishDateEnd
     * @param jobName
     * @param jobStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<JobHistoryDto> pagingJobHistory(String startDateBegin, String startDateEnd, String finishDateBegin,
                                             String finishDateEnd, String jobName, List<String> finalStatusList,
                                             List<String> stateList, Integer pageNum, Integer pageSize);

    /**
     * 作业历史查询甘特图
     * @param startDate
     * @param layerCode
     * @param dagId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<JobHistoryGanttDto> pagingGanttJobHistory(String startDate, String layerCode, Long dagId, Integer pageNum, Integer pageSize);

    /**
     * 任务最后运行时间
     * @param jobId
     * @return
     */
    String getLatestRuntime(Long jobId);

    /**
     * 业务方需要的日志地址逻辑
     * http://bigdata-master3.cai-inc.com:8088/cluster/app/application_1636461038777_141467     killed  failed finished
     * http://bigdata-master3.cai-inc.com:8088/proxy/application_1636461038777_145072/       running
     */
    String getBusinessLogUrl(String applicationId, String finalStatus, String state);

    /**
     * 甘特图返回变量格式转换并进行内部排序
     * @param list
     * @return
     */
    List<JobHistoryTableGanttDto> transform(List<JobHistoryGanttDto> list);

}