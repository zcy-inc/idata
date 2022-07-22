package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.model.common.PageResult;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTableQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import cn.zhengcaiyun.idata.dqc.service.MonitorTableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据质量被监控的表(DqcMonitorTable)表控制层
 *
 * @author zheng
 * @since 2022-06-28 11:01:50
 */
@RestController
@RequestMapping("/monitorTable")
public class MonitorTableController {
    @Resource
    private MonitorTableService monitorTableService;

    @Autowired
    private TableService tableService;

    @RequestMapping("/add")
    public Result<MonitorTableVO> add(@RequestBody MonitorTableVO vo) {
        if (vo.getBaselineId() == null) {
            return Result.failureResult("基线id不能为空");
        }
        if (StringUtils.isEmpty(vo.getTableName())) {
            return Result.failureResult("表名不能为空");
        }
        String[] arr = vo.getTableName().split("\\.");
        if (arr.length != 2) {
            return Result.failureResult("表名错误");
        }

        HiveTable hiveTable = tableService.getTable(arr[0], arr[1]);
        if (hiveTable == null) {
            return Result.failureResult("表不存在");
        }
        if (hiveTable.getPartitioned() && StringUtils.isEmpty(vo.getPartitionExpr())) {
            return Result.failureResult("该表为分区表，未填写分区信息");
        }

        monitorTableService.insert(vo);

        return Result.successResult(vo);
    }

    @RequestMapping("/update")
    public Result<Boolean> update(@RequestBody MonitorTableVO vo) {
        return Result.successResult(monitorTableService.update(vo));
    }

    @RequestMapping("/del/{id}/{isBaseline}")
    public Result<Boolean> delById(@PathVariable("id") Long id,@PathVariable("isBaseline")Boolean isBaseline) {
        return monitorTableService.delById(id, isBaseline);
    }

    @RequestMapping("/getByPage")
    public Result<PageResult<MonitorTableVO>> getByPage(MonitorTableQuery query) {
        return Result.successResult(monitorTableService.getByPage(query));
    }

    @RequestMapping("/get/{id}")
    public Result<MonitorTableVO> getById(@PathVariable("id") Long id) {
        return Result.successResult(monitorTableService.getById(id));
    }

    @RequestMapping("/tables/get/{baselineId}")
    public Result getByBaselineId(@PathVariable("baselineId") Long baselineId) {
        return Result.successResult(monitorTableService.getByBaselineId(baselineId));
    }

}

