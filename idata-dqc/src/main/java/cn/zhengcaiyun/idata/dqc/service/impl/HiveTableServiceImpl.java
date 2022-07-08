package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.zhengcaiyun.idata.connector.jdbc.PostgreSqlDataProvider;
import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceDriver;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceParam;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;
import cn.zhengcaiyun.idata.dqc.service.HiveTableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:zheng
 * Date:2022/6/22
 */
@Service
public class HiveTableServiceImpl implements HiveTableService {
    @Autowired
    private HdfsService hdfsService;

    @Value("${pg.datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${pg.datasource.username}")
    private String username;

    @Value("${pg.datasource.password}")
    private String password;

    @Override
    public void getPartition(String partition) {
        String[] arr = new String[]{"yyyy", "yyyy-MM", "yyyy/MM", "yyyyMM",
                "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd"};

    }

    /**
     * hdfs:/hive/tmp.db/test_partitioned/year=2022/month=6/day=22
     * @param databaseName
     * @param tableName
     * @param partition year=2022/month=6/day=22
     * @return
     */
    @Override
    public HiveTable getTableInfo(String databaseName, String tableName, String partition) {
        String hdfsPath = "/hive/" + databaseName + ".db/" + tableName ;
        if(StringUtils.isNotEmpty(partition)){
            hdfsPath += "/" + partition;
        }
        return hdfsService.getHiveTableInfo(tableName, hdfsPath);
    }

    @Override
    public List<HiveTable> getTableList(String tableName) {
        PostgreSqlDataProvider postgreSqlDataProvider = new PostgreSqlDataProvider();
        DatasourceParam postpreDatasource = new DatasourceParam(DatasourceDriver.POSTPRESQL, username, password, jdbcUrl);

        String sql = "select distinct concat(d.\"NAME\",'.',t.\"TBL_NAME\") table_name ,p.\"PKEY_NAME\" is not null is_partition\n" +
                "from hive.public.\"TBLS\" t\n" +
                "left join hive.public.\"DBS\" d on t.\"DB_ID\"=d.\"DB_ID\"\n" +
                "left join hive.public.\"PARTITION_KEYS\" p on t.\"TBL_ID\"=p.\"TBL_ID\"\n";
        if (StringUtils.isNotEmpty(tableName)) {
            sql += "where t.\"TBL_NAME\" like '%" + tableName + "%'";
        }

        List<Map<String, Object>> list = postgreSqlDataProvider.query(postpreDatasource, sql);
        List<HiveTable> tableList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            HiveTable table = new HiveTable();
            table.setTableName((String) map.get("table_name"));
            table.setPartitioned(map.get("is_partition").equals("t") ? true : false);
            tableList.add(table);
        }

        return tableList;
    }

    @Override
    public HiveTable getTable(String database, String tableName) {
        PostgreSqlDataProvider postgreSqlDataProvider = new PostgreSqlDataProvider();
        DatasourceParam postpreDatasource = new DatasourceParam(DatasourceDriver.POSTPRESQL, username, password, jdbcUrl);

        String sql = "select distinct concat(d.\"NAME\",'.',t.\"TBL_NAME\") table_name ,p.\"PKEY_NAME\" is not null is_partition\n" +
                "from hive.public.\"TBLS\" t\n" +
                "left join hive.public.\"DBS\" d on t.\"DB_ID\"=d.\"DB_ID\"\n" +
                "left join hive.public.\"PARTITION_KEYS\" p on t.\"TBL_ID\"=p.\"TBL_ID\"\n" +
                "where d.\"NAME\"='" + database + "' and  t.\"TBL_NAME\"='" + tableName + "'";
        List<Map<String, Object>> list = postgreSqlDataProvider.query(postpreDatasource, sql);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, Object> map = list.get(0);
        HiveTable table = new HiveTable();
        table.setTableName((String) map.get("table_name"));
        table.setPartitioned(map.get("is_partition").equals("t") ? true : false);

        return table;
    }

    @Override
    public List<Column> getColumns(String database, String tableName) throws IdataException {
        PostgreSqlDataProvider postgreSqlDataProvider = new PostgreSqlDataProvider();
        DatasourceParam postpreDatasource = new DatasourceParam(DatasourceDriver.POSTPRESQL, username, password, jdbcUrl);

        String sql = String.format("select  c.\"COMMENT\",c.\"COLUMN_NAME\" ,c.\"TYPE_NAME\" \n" +
                "from hive.public.\"TBLS\" t\n" +
                "inner join hive.public.\"DBS\" d on t.\"DB_ID\"=d.\"DB_ID\"\n" +
                "inner join hive.public.\"SDS\" s on t.\"SD_ID\" = s.\"SD_ID\"\n" +
                "inner join hive.public.\"COLUMNS_V2\" c on c.\"CD_ID\" = s.\"CD_ID\"\n" +
                "where d.\"NAME\" ='%s' and t.\"TBL_NAME\"='%s'",database,tableName);
        List<Map<String, Object>> list = postgreSqlDataProvider.query(postpreDatasource, sql);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Column> colList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Column col = new Column((String) map.get("COLUMN_NAME"),(String) map.get("TYPE_NAME"),(String) map.get("COMMENT"));
            colList.add(col);
        }
        return colList;
    }
}
