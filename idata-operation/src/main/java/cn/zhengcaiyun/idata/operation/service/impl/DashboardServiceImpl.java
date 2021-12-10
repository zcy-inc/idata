package cn.zhengcaiyun.idata.operation.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.TaskIntegrator;
import cn.zhengcaiyun.idata.develop.integration.schedule.dolphin.dto.TaskCountDto;
import cn.zhengcaiyun.idata.operation.bean.dto.JobDsOverviewDto;
import cn.zhengcaiyun.idata.operation.service.DashboardService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final HashMap<String, String> DS_STATE_MAPPING = Maps.newHashMap();

    static {
        DS_STATE_MAPPING.put("SUBMITTED_SUCCESS", "other");
    }

    @Autowired
    private TaskIntegrator taskIntegrator;

    @Override
    public Integer getDsTotalJob(String environment) {
        List<TaskCountDto> list = taskIntegrator.getTaskCountGroupState(environment, null, null);
        return list.stream().mapToInt(TaskCountDto::getCount).sum();
    }

    @Override
    public JobDsOverviewDto getDsTodayJobOverview(String environment) {
        List<TaskCountDto> list = taskIntegrator.getTaskCountGroupState(environment, DateUtil.beginOfDay(new Date()), null);
        list.forEach(e -> {
            HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
        });
        return null;
    }
}
