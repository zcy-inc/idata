package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dto.JobDependencyDto;
import cn.zhengcaiyun.idata.develop.dto.job.CycleJobDependencyDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;

import java.util.List;

public interface JobDependencyService {

    /**
     * 加载先继节点的树数据，只构建包含id的
     * @param jobId
     * @param env
     * @param prevLevel
     * @param nextLevel
     * @param searchJobId 搜索的任务id
     */
    Tuple2<JobTreeNodeDto, JobTreeNodeDto> loadTree(Long jobId, String env, Integer prevLevel, Integer nextLevel, Long searchJobId);

    /**
     * 获取jobId依赖相关的job列表，searchName作为过滤条件
     * @param searchName
     * @param jobId
     * @param env
     * @return
     */
    List<JobInfo> getDependencyJob(String searchName, Long jobId, String env);

    /**
     * 是否存在循环依赖
     * @param jobId
     * @param env
     * @param extraList
     * @return
     */
    CycleJobDependencyDto isCycleDependency(Long jobId, String env, List<JobDependencyDto> extraList);
}
