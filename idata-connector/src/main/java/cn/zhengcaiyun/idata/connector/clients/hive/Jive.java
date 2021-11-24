package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.util.JiveUtil;
import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import cn.zhengcaiyun.idata.connector.util.CreateTableListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


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
        }
        return exist;
    }

    /**
     * 获取创建表DDL
     * @param dbName
     * @param tableName
     * @return
     */
    public String getCreateTableSql(String dbName, String tableName) {
        try (ResultSet res = this.getClient().createStatement().executeQuery(String.format("show create table `%s`.%s", dbName, tableName))) {
            StringBuilder createSql = new StringBuilder();
            while (res.next()) {
                createSql.append(res.getString(1)).append("\n");
            }
            return createSql.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取元数据信息
     * @param dbName
     * @param tableName
     * @return
     */
    public MetadataInfo getMetadataInfo(String dbName, String tableName) {
        String ddl = getCreateTableSql(dbName, tableName);
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(ddl), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        CreateTableListener listener = new CreateTableListener();
        walker.walk(listener, parser.statement());
        return listener.metadataInfo;
    }

    /**
     * 获取普通列（非分区列）的元数据信息
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
     * @param dbName
     * @param tableName
     * @return
     */
    public String getTableSize(String dbName, String tableName) {
        MetadataInfo metadataInfo = getMetadataInfo(dbName, tableName);

        boolean isPartitionTable = metadataInfo.isPartitionTable();
        String sql = isPartitionTable ? String.format("DESCRIBE FORMATTED %s.%s", dbName, tableName) : String.format("show tblproperties %s.%s(\"totalSize\")", dbName, tableName);

        try (ResultSet res = this.getClient().createStatement().executeQuery(sql)) {
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
