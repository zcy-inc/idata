package cn.zhengcaiyun.idata.portal.controller.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dal.model.TableSibshipVO;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dto.table.TableSibship;
import cn.zhengcaiyun.idata.develop.service.table.TableSibshipService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * @author zheng
 * @since 2022-09-29 10:36:23
 */
@RestController
@RequestMapping("/p1/tableSibship")
public class TableSibshipController {
    @Autowired
    private TableSibshipService tableSibshipService;

    @RequestMapping("/get/{tableName}")
    public RestResult<TableSibshipVO> getByTableName(@PathVariable String tableName, Integer upper, Integer lower) {
        if (upper == null) {
            upper = 2;
        } else if (upper > 5) {
            upper = 5;
        }
        if (lower == null) {
            lower = 2;
        } else if (lower > 5) {
            lower = 5;
        }
        return RestResult.success(tableSibshipService.getByTableName(tableName, upper, lower));
    }

    @RequestMapping("/add")
    public RestResult<TableSibship> add(Long jobId, Integer version) {
        tableSibshipService.sibParse(jobId, version, "SQL_SPARK", "prod", "元宿");
        return RestResult.success();
    }

    //todo  初始化历史数据，只上线后调用一次
    @RequestMapping("/init")
    public RestResult<Boolean> init() {
        int page = 0;
        while (true) {
            List<JobPublishRecord> jobList = tableSibshipService.getPublishedJobs(page * 20);
            if (jobList.size() == 0) {
                return RestResult.success(true);
            }

            for (JobPublishRecord job : jobList) {
                tableSibshipService.sibParse(job.getJobId(), job.getJobContentVersion(), "SQL_SPARK", "prod", "系统");
            }
            page++;
        }
    }

    @RequestMapping("/job/get/{tableName}")
    public RestResult<Set<TableSibshipVO>> getJobs(@PathVariable String tableName) {
        return RestResult.success(tableSibshipService.getJobs(tableName));
    }

}

