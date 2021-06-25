package cn.zhengcaiyun.idata.label.compute.query;


import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.ConnectionDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.WideTableDataDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 从itable copy过来，暂时先这样，后续需要做一个独立模块时再调整
 *
 * @author shiyin(沐泽)
 * @date 2020/6/15 15:32
 */

public interface Query {
    Connection getConnection(ConnectionDto connectionDto) throws SQLException;

    List<String> getSchemas(ConnectionDto connectionDto) throws SQLException;

    List<String> getTables(ConnectionDto connectionDto, String schemaName) throws SQLException;

    List<ColumnDto> getColumns(ConnectionDto connectionDto, String schemaName, String tableName) throws SQLException;

    WideTableDataDto query(ConnectionDto connectionDto, String selectSql) throws SQLException;
}
