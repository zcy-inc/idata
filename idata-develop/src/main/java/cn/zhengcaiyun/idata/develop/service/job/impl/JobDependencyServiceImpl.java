package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.zhengcaiyun.idata.commons.dto.Tuple2;
import cn.zhengcaiyun.idata.develop.constant.enums.JobStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobDependenceRepo;
import cn.zhengcaiyun.idata.develop.dto.JobDependencyDto;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.JobRunOverviewDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobTreeNodeDto;
import cn.zhengcaiyun.idata.develop.manager.JobScheduleManager;
import cn.zhengcaiyun.idata.develop.service.job.JobDependencyService;
import cn.zhengcaiyun.idata.develop.service.job.JobInfoService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobDependencyServiceImpl implements JobDependencyService {

    @Autowired
    private JobScheduleManager jobScheduleManager;

    @Resource
    private JobDependenceRepo jobDependenceRepo;

    @Autowired
    private JobInfoService jobInfoService;


    @Override
    public Tuple2<JobTreeNodeDto, JobTreeNodeDto> loadTree(Long jobId, String env, Integer prevLevel, Integer nextLevel, Long searchJobId) {
        // 获取jobId的可达性分析
        Set<Long> accessIdSet = new HashSet<>();
        List<JobDependencyDto> list = getAccessJobDependency(env, jobId, accessIdSet);
        // 封装相关的nameMap
        Map<Long, String> nameMap = jobInfoService.getNameMapByIds(accessIdSet);

        Multimap<Long, Long> nextMap = ArrayListMultimap.create();
        Multimap<Long, Long> prevMap = ArrayListMultimap.create();
        for (JobDependencyDto elem : list) {
            nextMap.put(elem.getPrevJobId(), elem.getJobId());
            prevMap.put(elem.getJobId(), elem.getPrevJobId());
        }

        Tuple2<JobTreeNodeDto, Integer> prev = getNodeTree(jobId, prevLevel, prevMap, nameMap, searchJobId);
        JobTreeNodeDto prevTree = prev.e1;
        prevLevel = prev.e2;
        prevTree.setLevel(prevLevel);

        Tuple2<JobTreeNodeDto, Integer> next = getNodeTree(jobId, nextLevel, nextMap, nameMap, searchJobId);
        JobTreeNodeDto nextTree = next.e1;
        nextLevel = next.e2;
        nextTree.setLevel(nextLevel);

        List<JobRunOverviewDto> records = jobScheduleManager.getJobLatestRecords(env, 3000);
        Map<Long, JobRunOverviewDto> runInfoMap = Maps.newHashMap();
        records.forEach(e -> {
            // 例如 di_test111__005096
            String jobIdStr = ReUtil.get(".*__(\\d*)", e.getName(), 1);
            if (NumberUtil.isNumber(jobIdStr)) {
                runInfoMap.put(Long.parseLong(jobIdStr), e);
            }
        });
        assembleDSInfo(prevTree, runInfoMap, "prev");
        assembleDSInfo(nextTree, runInfoMap, "next");

        return new Tuple2<>(prevTree, nextTree);
    }

    /**
     * 递归-将DS信息封装在树节点中
     * @param tree
     */
    private void assembleDSInfo(JobTreeNodeDto tree, Map<Long, JobRunOverviewDto> runInfoMap, String relation) {
        if (tree == null) {
            return;
        }
        Long jobId = tree.getJobId();
        if (runInfoMap.containsKey(jobId)) {
            JobRunOverviewDto dto = runInfoMap.get(jobId);
            tree.setJobStatus(JobStatusEnum.getValueByDsCode(dto.getState()));
            tree.setLastRunTime(dto.getStartTime());
            tree.setTaskId(dto.getId());
        }
        tree.setRelation(relation);
        if (CollectionUtils.isNotEmpty(tree.getNextList())) {
            tree.getNextList().forEach(e -> assembleDSInfo(e, runInfoMap, relation));
        }
    }

    /**
     * jobId可达性分析，筛选出集合
     * @param env 环境
     * @param jobId
     * @param nameMap
     * @return
     */
    private List<JobDependencyDto> getAccessJobDependency(String env, Long jobId, Set<Long> accessIdSet) {
        List<JobDependencyDto> list = jobDependenceRepo.queryJobs(env);
        Multimap<Long, Long> nextMap = ArrayListMultimap.create();
        Multimap<Long, Long> prevMap = ArrayListMultimap.create();
        for (JobDependencyDto elem : list) {
            nextMap.put(elem.getPrevJobId(), elem.getJobId());
            prevMap.put(elem.getJobId(), elem.getPrevJobId());
        }

        //获取所有前继可达ids
        Queue<Long> queue = Lists.newLinkedList();
        queue.add(jobId);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Long qJobId = queue.poll();
                accessIdSet.add(qJobId);
                queue.addAll(prevMap.get(qJobId));
            }
        }
        //获取所有后继可达ids
        queue = Lists.newLinkedList();
        queue.add(jobId);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Long qJobId = queue.poll();
                accessIdSet.add(qJobId);
                queue.addAll(nextMap.get(qJobId));
            }
        }



        return list.stream().filter(e -> accessIdSet.contains(e.getJobId())).collect(Collectors.toList());
    }

    /**
     * 利用BFS构造一颗N叉树
     * @param jobId 任务id
     * @param level 用户设定的层数
     * @param map job之间关联关系
     * @return
     */
    private Tuple2<JobTreeNodeDto, Integer> getNodeTree(Long jobId, Integer level, Multimap<Long, Long> map, Map<Long, String> nameMap, Long searchJobId) {
        // 带迭代次数条件的广度优先搜索BFS
        Queue<Long> referenceQueue = Lists.newLinkedList();
        referenceQueue.add(jobId);
        JobTreeNodeDto treeNodeDto = new JobTreeNodeDto(jobId, nameMap.get(jobId));
        Map<Long, JobTreeNodeDto> nodeMap = new HashMap<>();
        nodeMap.put(jobId, treeNodeDto);

        // 返回的实际层数
        int max = level;
        // 是否受限于用于设定的层数
        boolean limited = Objects.isNull(searchJobId);

        int loop = 0;
        Set<Long> leafNodes = new HashSet<>();
        while (!referenceQueue.isEmpty() && (!limited || loop < level)) {
            int size = referenceQueue.size();
            for (int i = 0; i < size; i++) {
                Long qJobId = referenceQueue.poll();
                // 队列中的id
                JobTreeNodeDto qNode = nodeMap.get(qJobId);
                // 记录的id对应的后续直接jobIds
                List<Long> sJobIdList = (List<Long>) map.get(qJobId);

                // sJobIdList节点中是否包含命中的
                boolean containHighLight = false;
                for (Long sJobId : sJobIdList) {
                    String jobName = nameMap.get(sJobId);
                    JobTreeNodeDto sNode = new JobTreeNodeDto(sJobId, jobName);
                    boolean highLight = sJobId.equals(searchJobId);
                    sNode.setHighLight(highLight);
                    qNode.addNextListElem(sNode);
                    nodeMap.put(sJobId, sNode);

                    containHighLight = highLight | containHighLight;
                }
                // 如果命中超过用户设定的默认层数，更新最大层级记录最新的叶子节点
                if (containHighLight && max < loop) {
                    max = loop;
                    leafNodes.clear();
                    leafNodes.addAll(sJobIdList);
                }
                referenceQueue.addAll(sJobIdList);
            }
            loop++;
        }

        if (max > level && CollectionUtils.isNotEmpty(leafNodes)) {
            //遍历后叶子节点,清理所有不展示节点
            leafNodes.forEach(elemId -> nodeMap.get(elemId).setNextList(null));
        }

        return new Tuple2<>(treeNodeDto, max);
    }

}
