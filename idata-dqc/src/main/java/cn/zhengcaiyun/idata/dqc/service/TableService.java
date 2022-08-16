package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;

import java.util.List;
import java.util.Set;

/**
 * author:zheng
 * Date:2022/6/22
 */
public interface TableService {
    HiveTable getTableInfo(String fullTableName, String partition);

    HiveTable getTableInfo(String databaseName, String tableName, String partition);

    List<HiveTable> getMetastoreTableList(String tableName);
    Set<HiveTable> getTableList(String tableName, Integer limit);

    HiveTable getMetastoreTable(String database, String tableName);
    HiveTable getPartitionTable(String database, String tableName);

    List<Column> getMetastoreColumns(String database, String tableName) throws IdataException;
    List<Column> getColumns(String database, String tableName) throws IdataException;

    Long getTableCount(String database, String tableName);
    Set<String> getOwners(String database, String tableName);

}
