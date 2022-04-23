package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.util.JiveUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Jive是模仿Jedis写的，在维护此类时；
 * 1. 在管理可close类资源的时候，请注意不要写以下方式：此方式会释放connection（调用connection的close()），导致connection不可复用
 * try (Connection connection = this.getClient(); Statement statement = connection.createStatement();
 * ResultSet res = statement.executeQuery(String.format("show create table `%s`.%s", dbName, tableName)))
 */
public class Jive extends BinaryJive {

    public Jive(final String jdbcUrl, final String username, final String password) throws SQLException {
        super(jdbcUrl, username, password);
    }

    protected HivePoolAbstract dataSource = null;

    public void setDataSource(HivePool hivePool) {
        this.dataSource = hivePool;
    }

    /**
     * 判断表是否存在
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public boolean exist(String dbName, String tableName) {
        boolean exist = false;
        try (ResultSet res = this.getClient().createStatement().executeQuery(String.format("show tables in `%s` like '%s'", dbName, tableName))) {
            while (!exist && res.next()) {
                String hiveTableName = res.getString(1);
                exist = StringUtils.equals(hiveTableName, tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("exist fail!");
        }
        return exist;
    }

    /**
     * 重命名
     *
     * @param dbName     库名
     * @param sourceName 原表名
     * @param targetName 目标表名
     * @return
     */
    public boolean rename(String dbName, String sourceName, String targetName) {
        try (Statement statement = this.getClient().createStatement()) {
            statement.execute(String.format("alter table `%s`.%s rename to `%s`.%s", dbName, sourceName, dbName, targetName));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("rename fail!");
        }
        return exist(dbName, targetName);
    }

    /**
     * 新建表
     *
     * @param ddl
     * @return
     */
    public boolean create(String ddl) {
        try (Statement statement = this.getClient().createStatement()) {
            statement.execute(ddl);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("create fail!");
        }
        MetadataInfo metadataInfo = JiveUtil.parseMetadataInfo(ddl);
        return exist(metadataInfo.getDbName(), metadataInfo.getTableName());
    }

    /**
     * 获取创建表DDL
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public String getCreateTableSql(String dbName, String tableName) {
        try (Statement statement = this.getClient().createStatement();
             ResultSet res = statement.executeQuery(String.format("show create table `%s`.%s", dbName, tableName))) {
            StringBuilder createSql = new StringBuilder();
            while (res.next()) {
                createSql.append(res.getString(1)).append("\n");
            }
            return createSql.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("getCreateTableSql fail!");
        }
    }

    /**
     * 新增列
     *
     * @param dbName
     * @param tableName
     * @param addColumns
     * @return
     */
    public boolean addColumns(String dbName, String tableName, Set<ColumnInfoDto> addColumns) {
        if (CollectionUtils.isEmpty(addColumns)) {
            return false;
        }

        String sql = JiveUtil.assembleHiveAddColumnSQL(addColumns, dbName, tableName);
        boolean success;
        try (Statement statement = this.getClient().createStatement()) {
            statement.execute(sql);
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
            throw new GeneralException("addColumns fail!");
        }
        return success;
    }

    /**
     * 修改列属性
     *
     * @param dbName
     * @param tableName
     * @param sourceColumnName 原列名
     * @param targetColumnName 修改后列名
     * @param columnType       列类型
     * @param columnComment    列描述
     * @return
     */
    public boolean changeColumn(String dbName, String tableName, String sourceColumnName, String targetColumnName, String columnType, String columnComment) {
        boolean success;
        try (Statement statement = this.getClient().createStatement()) {
            System.out.println(String.format("alter table `%s`.%s change %s %s %s comment '%s'", dbName, tableName, sourceColumnName, targetColumnName, columnType, columnComment));
            statement.execute(String.format("alter table `%s`.%s change %s %s %s comment '%s'", dbName, tableName, sourceColumnName, targetColumnName, columnType, columnComment));
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("changeColumn fail!");
        }
        return success;
    }

    public List<String> getTableNameList(String dbName) {
        List<String> tableList = new ArrayList<>();
        try (Statement statement = this.getClient().createStatement()) {
            statement.execute("use " + dbName);
            try (ResultSet rs = statement.executeQuery("show tables")) {
                while (rs.next()) {
                    tableList.add(rs.getString("tab_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("getTableNameList fail!");
        }
        return tableList;
    }

    public List<String> getDbNameList() {
        List<String> dbList = new ArrayList<>();
        try (Statement statement = this.getClient().createStatement();
             ResultSet rs = statement.executeQuery("show databases")) {
            while (rs.next()) {
                dbList.add(rs.getString("database_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException("getDbNameList fail!");
        }
        return dbList;
    }

    public Boolean testConnection() {
        try (Statement statement = this.getClient().createStatement();
             ResultSet rs = statement.executeQuery("select 1")) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取元数据信息
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public MetadataInfo getMetadataInfo(String dbName, String tableName) {
        String ddl = getCreateTableSql(dbName, tableName);
        return JiveUtil.parseMetadataInfo(ddl);
    }

    /**
     * 获取普通列（非分区列）的元数据信息
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public List<ColumnInfoDto> getColumnMetaInfo(String dbName, String tableName) {
        MetadataInfo metadataInfo = getMetadataInfo(dbName, tableName);
        List<ColumnInfoDto> list = metadataInfo.getColumnList().stream().map(e -> {
            ColumnInfoDto dto = new ColumnInfoDto();
            dto.setColumnType(e.getColumnType());
            dto.setColumnName(e.getColumnName());
            dto.setColumnComment(e.getColumnComment());
            return dto;
        }).collect(Collectors.toList());

        return list;
    }

    /**
     * 获取表大小
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public String getTableSize(String dbName, String tableName) {
        MetadataInfo metadataInfo = getMetadataInfo(dbName, tableName);

        boolean isPartitionTable = metadataInfo.isPartitionTable();
        String sql = isPartitionTable ? String.format("DESCRIBE FORMATTED %s.%s", dbName, tableName) : String.format("show tblproperties %s.%s(\"totalSize\")", dbName, tableName);

        try (Statement statement = this.getClient().createStatement();
             ResultSet res = statement.executeQuery(sql)) {
            long bytes = -1;
            if (isPartitionTable) {
                while (res.next()) {
                    if (res.getString(2) != null && res.getString(2).contains("totalSize")) {
                        bytes = Long.parseLong(res.getString(3).trim());
                    }
                }
            } else if (res.next()) {
                bytes = res.getLong(1);
            }
            return JiveUtil.beautifySize(bytes);
        } catch (Exception e) {
            throw new ExecuteSqlException("查询失败", e);
        }
    }

    @Override
    public void close() {
        if (dataSource != null) {
            HivePoolAbstract pool = this.dataSource;
            this.dataSource = null;
            pool.returnResource(this);
        } else {
            super.close();
        }
    }

    @Deprecated
    @SuppressWarnings("just for test")
    public void test() throws SQLException {
        getColumnMetaInfo("dws", "tmp_sync_hive");
    }
}
