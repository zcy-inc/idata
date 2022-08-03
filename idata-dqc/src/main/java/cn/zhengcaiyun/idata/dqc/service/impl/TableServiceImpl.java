package cn.zhengcaiyun.idata.dqc.service.impl;

import cn.hutool.core.lang.Assert;
import cn.zhengcaiyun.idata.connector.jdbc.PostgreSqlDataProvider;
import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceDriver;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceParam;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.dao.TableDao;
import cn.zhengcaiyun.idata.dqc.model.enums.SysLabelCodeEnum;
import cn.zhengcaiyun.idata.dqc.model.vo.LableVO;
import cn.zhengcaiyun.idata.dqc.model.vo.TableVO;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * author:zheng
 * Date:2022/6/22
 */
@Service
public class TableServiceImpl implements TableService {
    @Autowired
    private HdfsService hdfsService;

    @Value("${pg.datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${pg.datasource.username}")
    private String username;

    @Value("${pg.datasource.password}")
    private String password;

    @Autowired
    private TableDao tableDao;

    /**
     * 获取表写入时间、大小
     *
     * @param fullTableName
     * @param partition
     * @return
     */
    @Override
    public HiveTable getTableInfo(String fullTableName, String partition) {
        String[] arr = fullTableName.split("\\.");
        Assert.isFalse(arr.length != 2, "表名错误");
        return this.getTableInfo(arr[0], arr[1], partition);
    }

    /**
     * hdfs:/hive/tmp.db/test_partitioned/year=2022/month=6/day=22
     *
     * @param databaseName
     * @param tableName
     * @param partition    year=2022/month=6/day=22
     * @return
     */
    @Override
    public HiveTable getTableInfo(String databaseName, String tableName, String partition) {
        String hdfsPath = "/hive/" + databaseName + ".db/" + tableName;
        if (StringUtils.isNotEmpty(partition)) {
            partition = partition.replaceAll(",", "/");
            hdfsPath += "/" + partition;
        }
        return hdfsService.getHiveTableInfo(tableName, hdfsPath);
    }

    @Override
    public List<HiveTable> getMetastoreTableList(String tableName) {
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
    public HiveTable getMetastoreTable(String database, String tableName) {
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

    public Long getTableCount(String database, String tableName) {
        PostgreSqlDataProvider postgreSqlDataProvider = new PostgreSqlDataProvider();
        DatasourceParam postpreDatasource = new DatasourceParam(DatasourceDriver.POSTPRESQL, username, password, jdbcUrl);

        String sql = String.format("select a.\"TBL_NAME\",c.\"NAME\",b.\"PARAM_VALUE\" num\n" +
                "from  hive.public.\"TBLS\"  a\n" +
                "          join hive.public.\"TABLE_PARAMS\" b on a.\"TBL_ID\" = b.\"TBL_ID\"\n" +
                "          join hive.public.\"DBS\" c on a.\"DB_ID\"=c.\"DB_ID\"\n" +
                "where b.\"PARAM_KEY\" = 'numFiles'\n" +
                "  and c.\"NAME\"='%s' and a.\"TBL_NAME\"='%s'", database, tableName);
        List<Map<String, Object>> list = postgreSqlDataProvider.query(postpreDatasource, sql);
        if (CollectionUtils.isEmpty(list)) {
            return 0L;
        }
        Map<String, Object> map = list.get(0);

        return Long.parseLong(map.get("num").toString());
    }

    @Override
    public Set<String> getOwners(String database, String tableName) {
        Set<String> set = new HashSet<>();
        set.addAll(tableDao.getPwOwners(database, tableName));
        set.addAll(tableDao.getDwOwners(database, tableName));
        return set;
    }

    @Override
    public List<Column> getMetastoreColumns(String database, String tableName) throws IdataException {
        PostgreSqlDataProvider postgreSqlDataProvider = new PostgreSqlDataProvider();
        DatasourceParam postpreDatasource = new DatasourceParam(DatasourceDriver.POSTPRESQL, username, password, jdbcUrl);

        String sql = String.format("select  c.\"COMMENT\",c.\"COLUMN_NAME\" ,c.\"TYPE_NAME\" \n" +
                "from hive.public.\"TBLS\" t\n" +
                "inner join hive.public.\"DBS\" d on t.\"DB_ID\"=d.\"DB_ID\"\n" +
                "inner join hive.public.\"SDS\" s on t.\"SD_ID\" = s.\"SD_ID\"\n" +
                "inner join hive.public.\"COLUMNS_V2\" c on c.\"CD_ID\" = s.\"CD_ID\"\n" +
                "where d.\"NAME\" ='%s' and t.\"TBL_NAME\"='%s'", database, tableName);
        List<Map<String, Object>> list = postgreSqlDataProvider.query(postpreDatasource, sql);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Column> colList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Column col = new Column((String) map.get("COLUMN_NAME"), (String) map.get("TYPE_NAME"), (String) map.get("COMMENT"));
            colList.add(col);
        }
        return colList;
    }

    @Override
    public Set<HiveTable> getTableList(String tableName, Integer limit) {
        if (limit == null) {
            limit = 100;
        }
        Set<HiveTable> tableSet = new HashSet<>();

        List<TableVO> tableList = tableDao.getTables(tableName, limit);
        for (TableVO tableVO : tableList) {
            List<LableVO> lableList = tableVO.getLableList();
            for (LableVO label : lableList) {
                if (SysLabelCodeEnum.TBL_COMMENT_LABEL.getLabelCode().equals(label.getLabelCode())) {
                    tableVO.setTableAlias(label.getLabelParamValue());
                } else if (SysLabelCodeEnum.PARTITION_COL_LABEL.getLabelCode().equals(label.getLabelCode())) {
                    tableVO.setPartitioned("true".equals(label.getLabelParamValue()) ? true : false);
                } else if (SysLabelCodeEnum.DB_NAME_LABEL.getLabelCode().equals(label.getLabelCode())) {
                    tableVO.setTableName(label.getLabelParamValue() + "." + tableVO.getTableName());
                }
            }
            if (StringUtils.isEmpty(tableVO.getTableAlias())) {
                tableVO.setTableAlias(tableName);
            }
            HiveTable hiveTable = new HiveTable();
            hiveTable.setComment(tableVO.getTableAlias());
            hiveTable.setTableName(tableVO.getTableName());
            hiveTable.setPartitioned(tableVO.getPartitioned() == null ? false : tableVO.getPartitioned());
            tableSet.add(hiveTable);
        }
        return tableSet;
    }

    @Override
    public HiveTable getPartitionTable(String database, String tableName) {
        TableVO tableVO = tableDao.getPartitionTable(database, tableName);
        if (tableVO == null) {
            return null;
        }

        HiveTable hiveTable = new HiveTable();
        hiveTable.setTableName(database + "." + tableName);
        hiveTable.setPartitioned(tableVO.getPartitioned());
        return hiveTable;
    }


    @Override
    public List<Column> getColumns(String database, String tableName) throws IdataException {
        return tableDao.getColumns(database, tableName);
    }

}
