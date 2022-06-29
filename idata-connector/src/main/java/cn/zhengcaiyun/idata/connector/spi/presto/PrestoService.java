package cn.zhengcaiyun.idata.connector.spi.presto;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PrestoService {

    public QueryResultDto previewTable(String jdbcUrl, String dbName, String tblName) {
        return queryTable(jdbcUrl, dbName, tblName, null, null, null);
    }

    public QueryResultDto queryTable(String jdbcUrl, String dbName, String tblName, String[] columns, Long limit,
                                      Long offset) {
        String columnNames = columns != null && columns.length > 0
                ? String.join(",", Arrays.asList(columns)) : "*";
        limit = limit != null ? limit : 10L;
        offset = offset != null ? offset : 0;

        QueryResultDto queryResultDto = new QueryResultDto();
        List<ColumnInfoDto> meta = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "presto", null);
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery("select " + columnNames + " from \"" + dbName + "\".\""
                     + tblName + "\"" +  " offset " + offset + " limit " + limit)) {
            int columnCount = res.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                ColumnInfoDto columnDto = new ColumnInfoDto();
                String colName = res.getMetaData().getColumnName(i);
                columnDto.setColumnName(colName);
                columnDto.setColumnType(res.getMetaData().getColumnTypeName(i));
                meta.add(columnDto);
            }
            while (res.next()) {
                List<String> record = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    record.add(res.getString(i));
                }
                data.add(record);
            }
        } catch (SQLException e) {
            throw new ExecuteSqlException("查询失败", e);
        }
        queryResultDto.setMeta(meta);
        queryResultDto.setData(data);
        return queryResultDto;
    }

}
