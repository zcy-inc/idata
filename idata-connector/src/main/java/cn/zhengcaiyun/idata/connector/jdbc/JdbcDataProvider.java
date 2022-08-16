package cn.zhengcaiyun.idata.connector.jdbc;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceDriver;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceParam;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDataProvider.class);

    public static Map<String, DataSource> dsMap = Maps.newConcurrentMap();

    public static final int SQL_QUERY_TIMEOUT_SECOND = 30;

    private static final int DS_CONN_MAX_WAIT = 3000;

    private static final int DS_CONN_RETRY_NUM = 3;

    private static final int DS_CONN_MAX_ACTIVE = 50;

    private static final int DS_CONN_INIT_ACTIVE = 30;

    public static Map<String, String> datasourceMap = new HashMap<>();

    static {
        for (DatasourceDriver driver : DatasourceDriver.values()) {
            datasourceMap.put(driver.name(), driver.getDriver());
        }
    }

    private DataSource getSource(DatasourceParam datasource, boolean pooled) throws Exception {
        Map<String, String> conf = new HashMap<>();

        conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, datasourceMap.get(datasource.getType()));
        conf.put(DruidDataSourceFactory.PROP_USERNAME, datasource.getUsername());
        conf.put(DruidDataSourceFactory.PROP_PASSWORD, datasource.getPassword());
        conf.put(DruidDataSourceFactory.PROP_URL, datasource.getJdbcUrl());

        if (pooled) {
            conf.put(DruidDataSourceFactory.PROP_MAXACTIVE, String.valueOf(DS_CONN_MAX_ACTIVE));
            conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, String.valueOf(DS_CONN_INIT_ACTIVE));
        } else {
            conf.put(DruidDataSourceFactory.PROP_MAXACTIVE, "1");
            conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "1");
        }

        DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
        druidDS.setMaxWait(DS_CONN_MAX_WAIT);
        druidDS.setKeepAlive(true);
        druidDS.setBreakAfterAcquireFailure(true);
        druidDS.setConnectionErrorRetryAttempts(DS_CONN_RETRY_NUM);

        return druidDS;
    }

    private Connection getConn(DatasourceParam datasource) throws Exception {
        Connection conn = null;
        DataSource ds = dsMap.get(datasource.getJdbcUrl());
        if (ds == null) {
            ds = getSource(datasource, true);
            dsMap.put(datasource.getJdbcUrl(), ds);
        }
        conn = ds.getConnection();
        return conn;
    }

    public List<Column> getColumns(DatasourceParam datasource, String catalog, String database, String tableName) throws IdataException {
        Map<String, String> commentMap = Maps.newHashMap();
        Map<String, String> typeMap = Maps.newHashMap();


        String sql = "SELECT * FROM ";
        if (StringUtils.isNotEmpty(catalog)) {
            sql += catalog + ".";
        }
        sql += database + "." + tableName + " LIMIT 0";
        logger.warn("Get Column sql:" + sql);

        List<Column> columns = Lists.newArrayList();

        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConn(datasource);

            ResultSet rs = null;
            if (StringUtils.isNotEmpty(catalog)) {
                rs = conn.getMetaData().getColumns(catalog, database, tableName, null);
            } else {
                rs = conn.getMetaData().getColumns(null, database, tableName, null);
            }

            while (rs.next()) {
                String comment = (String) rs.getObject("REMARKS");
                String columnName = (String) rs.getObject("COLUMN_NAME");
                String typeName = (String) rs.getObject("TYPE_NAME");

                if (StringUtils.isNotBlank(columnName) && StringUtils.isNotBlank(comment)) {
                    commentMap.put(columnName, comment);
                }

                if (StringUtils.isNotBlank(columnName) && StringUtils.isNotBlank(typeName)) {
                    typeMap.put(columnName, typeName);
                }
            }

            stat = conn.prepareStatement(sql);
            ResultSetMetaData metaData = stat.executeQuery().getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                int index = i + 1;

                String columnNameType = metaData.getColumnTypeName(index);
                String columnLabel = metaData.getColumnLabel(index);
                String comment = commentMap.get(columnLabel);
                Column column = new Column(columnLabel, columnNameType, comment);
                columns.add(column);

            }
        } catch (Exception e) {
            logger.error("get view(" + sql + ") column types error", e);
            throw new IdataException(e.getMessage());
        } finally {
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }
        return columns;
    }

    public List<Map<String, Object>> query(DatasourceParam datasource, String sql) {
        logger.warn("Query sql:" + sql);

        Statement stat = null;
        Connection conn = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;

        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            conn = getConn(datasource);
            stat = conn.createStatement();
            stat.setQueryTimeout(SQL_QUERY_TIMEOUT_SECOND);
            resultSet = stat.executeQuery(sql);
            metaData = resultSet.getMetaData();
            int size = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    String value = resultSet.getString(i + 1);
                    String col = metaData.getColumnName(i + 1);
                    map.put(col, value);
                }
                resList.add(map);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(stat);
            JdbcUtils.closeConnection(conn);
        }

        return resList;
    }


}
