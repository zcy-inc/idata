package cn.zhengcaiyun.idata.connector.spi.hive;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.Jive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
public class HiveService implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HiveService.class);

    @Autowired
    private HivePool hivePool;

    public List<ColumnInfoDto> getHiveTableColumns(String dbName, String tableName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.getColumnMetaInfo(dbName, tableName);
        }
    }

    public MetadataInfo getMetadataInfo(String dbName, String tableName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.getMetadataInfo(dbName, tableName);
        }
    }

    public boolean create(String ddl) {
        try (Jive jive = hivePool.getResource()) {
            return jive.create(ddl);
        }
    }

    public String getTableSize(String dbName, String tableName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.getTableSize(dbName, tableName);
        }
    }

    /**
     * 判断表是否存在
     * @param dbName
     * @param tableName
     * @return
     */
    public boolean existHiveTable(String dbName, String tableName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.exist(dbName, tableName);
        }
    }

    /**
     * 重命名
     * @param dbName
     * @param sourceName
     * @param targetName
     */
    public boolean rename(String dbName, String sourceName, String targetName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.rename(dbName, sourceName, targetName);
        }
    }

    public String getCreateTableSql(String dbName, String tableName) {
        try (Jive jive = hivePool.getResource()) {
            return jive.getCreateTableSql(dbName, tableName);
        }
    }

    public boolean changeColumn(String dbName, String tableName, String sourceColumnName, String targetColumnName, String columnType, String columnComment) {
        try (Jive jive = hivePool.getResource()) {
            return jive.changeColumn(dbName, tableName, sourceColumnName, targetColumnName, columnType, columnComment);
        }
    }

    /**
     * 新增列
     * @param dbName
     * @param tableName
     * @param columns
     */
    public boolean addColumns(String dbName, String tableName, Set<ColumnInfoDto> columns) {
        if (CollectionUtils.isEmpty(columns)) {
            return false;
        }
        try (Jive jive = hivePool.getResource()) {
            return jive.addColumns(dbName, tableName, columns);
        }
    }

}
