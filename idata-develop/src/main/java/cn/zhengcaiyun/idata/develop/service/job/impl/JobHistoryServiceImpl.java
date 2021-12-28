package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.resourcemanager.ResourceManagerService;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryGanttDto;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryTableGanttDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDynamicSqlSupport.devJobHistory;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDynamicSqlSupport.startTime;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Service
public class JobHistoryServiceImpl implements JobHistoryService {

    @Value("${yarn.resourceManagerUri}")
    private String YARN_RM_URI;

    @Autowired
    private DevJobHistoryDao devJobHistoryDao;

    @Autowired
    private DevJobHistoryMyDao devJobHistoryMyDao;

    @Autowired
    private ResourceManagerService resourceManagerService;

    @Override
    public void batchUpsert(List<DevJobHistory> devJobHistoryList) {
        devJobHistoryMyDao.batchUpsert(devJobHistoryList);
    }

    @Override
    public PageInfo<DevJobHistory> pagingJobHistoryByJobId(Long jobId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        var builder = select(devJobHistory.allColumns()).from(devJobHistory).where(devJobHistory.jobId, isEqualTo(jobId));
        List<DevJobHistory> list = devJobHistoryDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        PageInfo<DevJobHistory> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<JobHistoryDto> topDuration(DateTime startDate, DateTime endDate, int top) {
        List<JobHistoryDto> list = devJobHistoryMyDao.topDurationGroupByJobId(DateUtil.format(startDate, "yyyy-MM-dd"), DateUtil.format(endDate, "yyyy-MM-dd"), top);
        return list;
    }

    @Override
    public List<JobHistoryDto> topResource(DateTime startDate, DateTime endDate, int top) {
        List<JobHistoryDto> list = devJobHistoryMyDao.topResourceGroupByJobId(DateUtil.format(startDate, "yyyy-MM-dd"), DateUtil.format(endDate, "yyyy-MM-dd"), top);
        return list;
    }

    @Override
    public PageInfo<JobHistoryDto> pagingJobHistory(String startDateBegin, String startDateEnd, String finishDateBegin, String finishDateEnd, String jobName, List<String> jobStatusList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobHistoryDto> list = devJobHistoryMyDao.selectList(startDateBegin, startDateEnd, finishDateBegin, finishDateEnd, jobName, jobStatusList);
        PageInfo<JobHistoryDto> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<JobHistoryGanttDto> pagingGanttJobHistory(String startDate, String layerCode, Long dagId, Integer pageNum, Integer pageSize) {
        String endDate = DateUtil.offsetDay(DateUtil.parse(startDate, DatePattern.NORM_DATE_PATTERN), 1).toDateStr();

        //先筛选出ids，是因为甘特图一个jobbIdkennel显示
        PageHelper.startPage(pageNum, pageSize);
        List<Long> jobIdList = devJobHistoryMyDao.selectGanttIdList(startDate, endDate, layerCode, dagId);
        if (CollectionUtils.isEmpty(jobIdList)) {
            return new PageInfo<>();
        }
        PageInfo<Long> idPageInfo = new PageInfo<>(jobIdList);

        PageInfo<JobHistoryGanttDto> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages(idPageInfo.getPages());
        pageInfo.setTotal(idPageInfo.getTotal());

        List<JobHistoryDto> list = devJobHistoryMyDao.selectGanttList(startDate, endDate, jobIdList);
        Map<Long, JobHistoryGanttDto> map = new HashMap<>();
        list.forEach(e -> {
            Long jobId = e.getJobId();
            JobHistoryGanttDto jobHistoryGanttDto = map.getOrDefault(jobId, new JobHistoryGanttDto());

            JobHistoryGanttDto.yAxis yAxis = new JobHistoryGanttDto.yAxis();
            yAxis.setJobId(jobId);
            yAxis.setJobName(e.getJobName());
            jobHistoryGanttDto.getSerialYAxis().add(yAxis);

            JobHistoryGanttDto.Data data = new JobHistoryGanttDto.Data();
            BeanUtils.copyProperties(e, data);
            data.setBusinessStatus(YarnJobStatusEnum.getValueByYarnEnumCode(e.getFinalStatus()));
            data.setBusinessLogsUrl(getBusinessLogUrl(e.getApplicationId(), e.getFinalStatus()));
            jobHistoryGanttDto.getSerialData().add(data);

            map.put(jobId, jobHistoryGanttDto);
        });
        pageInfo.setList(new ArrayList<>(map.values()));

        return pageInfo;
    }

    @Override
    public String getLatestRuntime(Long jobId) {
        Date startTime = devJobHistoryMyDao.getLatestRuntime(jobId);
        if (startTime != null) {
            return DateUtil.date(startTime).toString();
        }
        return "";
    }

    @Override
    public String getBusinessLogUrl(String applicationId, String status) {
        if (StringUtils.isEmpty(applicationId)) {
            return null;
        }

        String defaultUrl = YARN_RM_URI + "/cluster/app/" + applicationId;
        YarnJobStatusEnum enumCode = YarnJobStatusEnum.getByYarnEnumCode(status);
        if (enumCode == null) {
            return defaultUrl;
        }

        switch (enumCode) {
            case SUCCESS:
            case FAIL:
                return defaultUrl;
        }

        ClusterAppDto clusterAppDto = resourceManagerService.queryAppId(applicationId);
        String confirmStatus = clusterAppDto.getFinalStatus();
        if (clusterAppDto == null && StringUtils.isEmpty(confirmStatus)) {
            return defaultUrl;
        }
        enumCode = YarnJobStatusEnum.getByYarnEnumCode(confirmStatus);
        switch (enumCode) {
            case SUCCESS:
            case FAIL:
                return defaultUrl;
            case RUNNING:
                return YARN_RM_URI + "/proxy/" + applicationId;
            case OTHER:
            case PENDING:
                return "";
        }

        return defaultUrl;
    }

    @Override
    public List<JobHistoryTableGanttDto> transform(List<JobHistoryGanttDto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        Map<Long, JobHistoryTableGanttDto> map = new HashMap<>();

        for (JobHistoryGanttDto elem : list) {
            for (JobHistoryGanttDto.yAxis yAxis : elem.getSerialYAxis()) {
                Long jobId = yAxis.getJobId();
                JobHistoryTableGanttDto tableGanttDto = new JobHistoryTableGanttDto();
                tableGanttDto.setJobId(jobId);
                tableGanttDto.setJobName(yAxis.getJobName());

                map.put(jobId, tableGanttDto);
            }
            for (JobHistoryGanttDto.Data data : elem.getSerialData()) {
                Long jobId = data.getJobId();
                if (map.containsKey(jobId)) {
                    JobHistoryTableGanttDto.Data copy = new JobHistoryTableGanttDto.Data();
                    BeanUtils.copyProperties(data, copy);
                    Date finishTime = copy.getFinishTime();
                    if (finishTime == null) {
                        finishTime = DateUtil.date().toJdkDate();
                    }
                    if (copy.getStartTime() != null) {
                        copy.setDuration(DateUtil.between(finishTime, copy.getStartTime(), DateUnit.MS));
                    }
                    copy.setBusinessStatus(YarnJobStatusEnum.getValueByYarnEnumCode(copy.getFinalStatus()));
                    map.get(jobId).getChildren().add(copy);
                }
            }
        }
        return new ArrayList<>(map.values());
    }

}
