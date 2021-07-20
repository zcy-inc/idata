package cn.zhengcaiyun.idata.connector.spi.hive;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.util.HiveSqlUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class HiveService {

    public String getCreateTableSql(String jdbcUrl, String dbName, String tableName) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery("show create table `" + dbName + "." + tableName + "`")) {
            StringBuilder createSql = new StringBuilder();
            while (res.next()) {
                createSql.append(res.getString(1)).append("\n");
            }
            return createSql.toString();
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Table not found")) {
                throw new IllegalArgumentException(e.getMessage().substring(e.getMessage().indexOf("Table not found")));
            } else {
                throw new ExecuteSqlException("查询失败", e);
            }
        }
    }

    public String getTableSize(String jdbcUrl, String dbName, String tblName) {
        String ddl = getCreateTableSql(jdbcUrl, dbName, tblName);
        Map<String, Object> metadataMap = HiveSqlUtil.getCreateTableInfo(ddl);
        List<Map<String, String>> metadataPartitionColumnList = metadataMap.get("partitionColumns") != null ?
                (List<Map<String, String>>) metadataMap.get("partitionColumns") : new ArrayList<>();
        String partitionColumns = StringUtils.join(metadataPartitionColumnList.stream().map(partitionColumnsMap -> partitionColumnsMap.get("colName"))
                .collect(Collectors.toList()), ",");
        String analyzeSql;
        if (isNotEmpty(partitionColumns)) {
            analyzeSql = String.format("ANALYZE TABLE %s.%s PARTITION(%s) COMPUTE STATISTICS NOSCAN", dbName, tblName, partitionColumns);
        } else {
            analyzeSql = String.format("ANALYZE TABLE %s.%s COMPUTE STATISTICS NOSCAN", dbName, tblName);
        }
//        String sql1 = "DESCRIBE FORMATTED dwd.dwd_browse_click_item_log_di";
        String sql;
        // spark.sql.statistics.totalSize
        if (isNotEmpty(partitionColumns)) {
            sql = String.format("DESCRIBE FORMATTED %s.%s", dbName, tblName);
        } else {
            sql = String.format("show tblproperties %s.%s(\"totalSize\")", dbName, tblName);
        }
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "root", "");
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(analyzeSql);
            ResultSet res = stmt.executeQuery(sql);
            long bytes = -1;
            if (isNotEmpty(partitionColumns)) {
                while (res.next()) {
                    if (res.getString(2) != null && res.getString(2).contains("totalSize")) {
                        bytes = Long.parseLong(res.getString(3).trim());
                    }
                }
            } else {
                if (res.next()) {
                    bytes = res.getLong(1);
                }
            }
            if (bytes < 0) {
                return null;
            } else if (bytes < 1024 * 1024) {
                return (bytes / 1024) + "KB";
            } else if (bytes < 1024 * 1024 * 1024) {
                return (bytes / 1024 / 1024) + "MB";
            } else {
                return (bytes / 1024 / 1024 / 1024) + "GB";
            }
        } catch (Exception e) {
            throw new ExecuteSqlException("查询失败", e);
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
//        System.out.println(new HiveService().previewTable("dwd", "dwd_browse_click_item_log_di"));
        System.out.println(System.currentTimeMillis() - start);
    }
}
