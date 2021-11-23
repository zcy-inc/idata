package cn.zhengcaiyun.idata.connector.spi.hive;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.api.impl.MetadataQueryApiImpl;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.Jive;
import cn.zhengcaiyun.idata.connector.util.HiveSqlUtil;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class HiveService implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HiveService.class);

    /**
     * HiveService初始化后赋值
     */
    private Statement stmt;
    /**
     * 连接不关闭，复用
     */
    private Connection conn; // 351 41 1560ms

    private Connection jive2;//500

    private HivePool hivePool;//590 38 1794ms

    @Autowired
    private SysConfigDao sysConfigDao;

    @PostConstruct
    public void init() {
        SysConfig hiveConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("hive-info"))).orElse(null);
        checkState(Objects.nonNull(hiveConfig), "数据源连接信息不正确");
        String jdbcUrl = hiveConfig.getValueOne();
        try {
            long s1 = System.currentTimeMillis();
            conn = DriverManager.getConnection(jdbcUrl);
            stmt = conn.createStatement();
            long s2 = System.currentTimeMillis();
            System.out.println("init:" + (s2 - s1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setJdbc("jdbc:hive2://172.29.108.184:10000/default");
        hivePool = new HivePool(connectInfo);
//        jive2 = hivePool.getResource().getClient();
    }

    public List<ColumnInfoDto> getHiveTableColumns(String dbName, String tableName) {
        Jive jive = null;
        try {
            long s1 = System.currentTimeMillis();
            jive = hivePool.getResource();
//            ResultSet res = conn.createStatement().executeQuery("show create table `" + dbName + "." + tableName + "`");
//            ResultSet res = stmt.executeQuery("show create table `" + dbName + "." + tableName + "`");
//            ResultSet res = jive2.createStatement().executeQuery("show create table `" + dbName + "." + tableName + "`");
            ResultSet res = jive.getClient().createStatement().executeQuery("show create table `" + dbName + "." + tableName + "`");
            long s2 = System.currentTimeMillis();
            System.out.println(s2 - s1);
            List<ColumnInfoDto> tableColumns = extraColumnsInfo(res);

            long s3 = System.currentTimeMillis();
            System.out.println(s3 - s2);
            System.out.println(jive);
            return tableColumns;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Table not found")) {
                throw new IllegalArgumentException(e.getMessage().substring(e.getMessage().indexOf("Table not found")));
            } else {
                throw new ExecuteSqlException("查询失败", e);
            }
        } finally {
            long s1 = System.currentTimeMillis();
            jive.close();
            long s2 = System.currentTimeMillis();
            System.out.println("close:" + (s2 - s1));
        }

    }

    /**
     * 拼接出的sql例子如下
     * CREATE EXTERNAL TABLE `dws.tmp_sync_rename`(
     *   `id` bigint,
     *   `age` bigint COMMENT '年龄',
     *   `male` bigint COMMENT '性别',
     *   `address` string COMMENT '地址',
     *   `hobby` string COMMENT 'null')
     * COMMENT '非分区同步hive测试表'
     * PARTITIONED BY (
     *   `name` string COMMENT '名称')
     * ROW FORMAT SERDE
     *   'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
     * STORED AS INPUTFORMAT
     *   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
     * OUTPUTFORMAT
     *   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'
     * LOCATION
     *   'hdfs://nameservice1/hive/dws.db/tmp_sync_rename'
     * TBLPROPERTIES (
     *   'spark.sql.create.version'='2.4.0-cdh6.3.2',
     *   'spark.sql.sources.schema.numPartCols'='1',
     *   'spark.sql.sources.schema.numParts'='1',
     *   'spark.sql.sources.schema.part.0'='{"type":"struct","fields":[{"name":"id","type":"long","nullable":true,"metadata":{}},{"name":"age","type":"long","nullable":true,"metadata":{"comment":"年龄"}},{"name":"male","type":"long","nullable":true,"metadata":{"comment":"性别"}},{"name":"address","type":"string","nullable":true,"metadata":{"comment":"地址"}},{"name":"hobby","type":"string","nullable":true,"metadata":{"comment":"null"}},{"name":"name","type":"string","nullable":true,"metadata":{"comment":"名称"}}]}',
     *   'spark.sql.sources.schema.partCol.0'='name',
     *   'transient_lastDdlTime'='1637562297')
     * 从创建表DDL中抽取字段信息
     * @param res
     * @return
     */
    private List<ColumnInfoDto> extraColumnsInfo(ResultSet res) throws SQLException {
        List<ColumnInfoDto> tableColumns = new ArrayList<>();
        // 去掉第一行"CREATE ..." 直接拿字段
        res.next();
        String sql;
        // 列循环停止标志
        boolean stop = false;
        // 不以")"结尾则一直是列
        while (res.next() && !stop) {
            stop = StringUtils.endsWith(sql = res.getString(1).trim(), ")");

            String[] metas = sql.split(" ");
            ColumnInfoDto columnInfoDto = new ColumnInfoDto();
            String columnName = metas[0].replace("`", "");
            columnInfoDto.setColumnName(columnName);
            String columnType = metas[1].replace(")", "");
            columnInfoDto.setColumnType(columnType);
            if (metas.length >= 4) {
                String comment = metas[3].replace(")", "");
                columnInfoDto.setColumnComment(comment);
            }
            tableColumns.add(columnInfoDto);
        }
        return tableColumns;
    }

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
