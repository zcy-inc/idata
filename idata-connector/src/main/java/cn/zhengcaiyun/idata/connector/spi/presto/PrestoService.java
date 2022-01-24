package cn.zhengcaiyun.idata.connector.spi.presto;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestoService {

    public QueryResultDto previewTable(String jdbcUrl, String dbName, String tblName) {
        QueryResultDto queryResultDto = new QueryResultDto();
        List<ColumnInfoDto> meta = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "presto", null);
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery("select * from \"" + dbName + "\".\""
                     + tblName + "\"" + "limit 10")) {
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
