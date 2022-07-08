package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;

import java.util.List;

/**
 * author:zheng
 * Date:2022/6/22
 */
public interface HiveTableService {
    void getPartition(String partition);

    HiveTable getTableInfo(String databaseName, String tableName, String partition);

    List<HiveTable> getTableList(String tableName);

    HiveTable getTable(String database, String tableName);

    List<Column> getColumns(String database, String tableName) throws IdataException;
}
