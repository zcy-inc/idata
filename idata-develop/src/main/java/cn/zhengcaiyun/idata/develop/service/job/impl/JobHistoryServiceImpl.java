package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.zhengcaiyun.idata.develop.constant.enums.YarnJobStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobHistory;
import cn.zhengcaiyun.idata.develop.dto.JobHistoryGanttDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobHistoryDto;
import cn.zhengcaiyun.idata.develop.service.job.JobHistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobHistoryDynamicSqlSupport.devJobHistory;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Service
public class JobHistoryServiceImpl implements JobHistoryService {

    @Autowired
    private DevJobHistoryDao devJobHistoryDao;

    @Autowired
    private DevJobHistoryMyDao devJobHistoryMyDao;

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
            jobHistoryGanttDto.getSerialData().add(data);

            map.put(jobId, jobHistoryGanttDto);
        });
        pageInfo.setList((List)map.values());

        return pageInfo;
    }

}
