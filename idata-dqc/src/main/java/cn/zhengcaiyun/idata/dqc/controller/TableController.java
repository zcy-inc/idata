package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.model.common.Result;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * author:zheng
 * Date:2022/6/17
 */
@Controller
public class TableController {
    private static final Logger logger = LoggerFactory.getLogger(TableController.class);

    @Autowired
    private TableService tableService;

    @RequestMapping("/getTables")
    @ResponseBody
    public Result<Set<HiveTable>> getTables(String tableName, Integer limit, String editTable) {
        Set<HiveTable> tableList = tableService.getTableList(tableName, limit);

        if (StringUtils.isNotEmpty(editTable)) {
            tableList.addAll(tableService.getTableList(tableName, limit));
        }
        return Result.successResult(tableList);
    }

    @RequestMapping("/table/getColumns")
    @ResponseBody
    public Result<List<Column>> getColumns(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            return Result.failureResult("表名不能为空");
        }
        String[] arr = tableName.split("\\.");
        if (arr.length != 2) {
            return Result.failureResult("表名错误");
        }

        try {
            List<Column> colList = tableService.getColumns(arr[0], arr[1]);
            return Result.successResult(colList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.failureResult(e.getMessage());
        }

    }

    @RequestMapping("/table/getOwners")
    @ResponseBody
    public Result getOwner(String tableName) {
        String[] arr = tableName.split("\\.");
        if (arr.length != 2) {
            return Result.failureResult("表名错误");
        }
        return Result.successResult(tableService.getOwners(arr[0], arr[1]));
    }
}
