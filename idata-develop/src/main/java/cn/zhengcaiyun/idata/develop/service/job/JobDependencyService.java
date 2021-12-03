package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;

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
}
