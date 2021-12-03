package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dto.JobDependencyDTO;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.job.JobDependencyService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobDependencyServiceImpl implements JobDependencyService {

    @Resource
    private JobDependenceRepo jobDependenceRepo;

    @Override
    public Tuple2<JobTreeNodeDto, JobTreeNodeDto> loadTree(Long jobId, String env, Integer preLevel, Integer nextLevel) {
        // 获取jobId的可达性分析
        List<JobDependencyDTO> list = getAccessJobDependency(env, jobId);

        Map<Long, String> nameMap = new HashMap<>();
        Multimap<Long, Long> nextMap = ArrayListMultimap.create();
        Multimap<Long, Long> prevMap = ArrayListMultimap.create();
        for (JobDependencyDTO elem : list) {
            nameMap.put(elem.getJobId(), elem.getName());
            nextMap.put(elem.getPrevJobId(), elem.getJobId());
            prevMap.put(elem.getJobId(), elem.getPrevJobId());
        }

        JobTreeNodeDto nextTree = getNodeTree(jobId, nextLevel, nextMap, nameMap);
        JobTreeNodeDto prevTree = getNodeTree(jobId, preLevel, prevMap, nameMap);

        return new Tuple2<>(prevTree, nextTree);
    }

    /**
     * jobId可达性分析，筛选出集合
     * @param env 环境
     * @param jobId
     * @return
     */
    private List<JobDependencyDTO> getAccessJobDependency(String env, Long jobId) {
        List<JobDependencyDTO> list = jobDependenceRepo.queryJobs(env);
        Multimap<Long, Long> nextMap = ArrayListMultimap.create();
        Multimap<Long, Long> prevMap = ArrayListMultimap.create();
        for (JobDependencyDTO elem : list) {
            nextMap.put(elem.getPrevJobId(), elem.getJobId());
            prevMap.put(elem.getJobId(), elem.getPrevJobId());
        }

        //获取所有可达ids
        Set<Long> accessIdSet = new HashSet<>();
        Queue<Long> queue = Lists.newLinkedList();
        queue.add(jobId);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Long qJobId = queue.poll();
                accessIdSet.add(qJobId);
                queue.addAll(nextMap.get(qJobId));
                queue.addAll(prevMap.get(qJobId));
            }
        }
        return list.stream().filter(e -> accessIdSet.contains(e.getJobId())).collect(Collectors.toList());
    }

    /**
     * 利用BFS构造一颗N叉树
     * @param jobId
     * @param level
     * @param map
     * @return
     */
    private JobTreeNodeDto getNodeTree(Long jobId, Integer level, Multimap<Long, Long> map, Map<Long, String> nameMap) {
        // 带迭代次数条件的广度优先搜索BFS
        Queue<Long> referenceQueue = Lists.newLinkedList();
        referenceQueue.add(jobId);
        JobTreeNodeDto treeNodeDto = new JobTreeNodeDto(jobId, nameMap.get(jobId));
        Map<Long, JobTreeNodeDto> nodeMap = new HashMap<>();
        nodeMap.put(jobId, treeNodeDto);

        int loop = 0;
        while (!referenceQueue.isEmpty() && loop < level) {
            int size = referenceQueue.size();
            for (int i = 0; i < size; i++) {
                Long qJobId = referenceQueue.poll();
                // 队列中的id
                JobTreeNodeDto qNode = nodeMap.get(qJobId);
                // 记录的id对应的后续直接jobIds
                Collection<Long> sJobIdList = map.get(qJobId);
                sJobIdList.stream().forEach(sJobId -> {
                    JobTreeNodeDto sNode = new JobTreeNodeDto(sJobId, nameMap.get(sJobId));
                    qNode.addNextListElem(sNode);
                    nodeMap.put(sJobId, sNode);
                });
                referenceQueue.addAll(sJobIdList);
            }
            loop++;
        }

        return treeNodeDto;
    }

}
