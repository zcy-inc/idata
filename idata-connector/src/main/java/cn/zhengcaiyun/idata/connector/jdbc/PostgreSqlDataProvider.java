package cn.zhengcaiyun.idata.connector.jdbc;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceParam;
import cn.zhengcaiyun.idata.connector.jdbc.model.IdataException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PostgreSqlDataProvider extends JdbcDataProvider {
    public List<Column> getColumns(DatasourceParam datasource, String database, String tableName) throws IdataException {
        return this.getColumns(datasource, null, database, tableName);
    }

    @Override
    public List<Column> getColumns(DatasourceParam datasource, String catalog, String database, String tableName) throws IdataException {
        if (StringUtils.isNotEmpty(catalog)) {
            catalog = "\"" + catalog + "\"";
        }
        database = "\"" + database + "\"";
        tableName = "\"" + tableName + "\"";
        return super.getColumns(datasource, catalog, database, tableName);
    }
}
