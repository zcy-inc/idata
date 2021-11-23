package cn.zhengcaiyun.idata.connector.clients.hive;


import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.clients.hive.util.JiveUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Jive extends BinaryJive {

    public Jive(final String jdbcUrl, final String username, final String password) throws SQLException {
        super(jdbcUrl, username, password);
    }

    protected HivePoolAbstract dataSource = null;

    public void setDataSource(HivePool hivePool) {
        this.dataSource = hivePool;
    }

    /**
     * 获取列的元数据信息
     * @param dbName
     * @param tableName
     * @return
     */
    public List<ColumnInfoDto> getColumnMetaInfo(String dbName, String tableName) throws SQLException {
        ResultSet res = this.getClient().createStatement().executeQuery("show create table `" + dbName + "." + tableName + "`");
        return JiveUtil.extraColumnsInfo(res);
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
        ResultSet res = getClient().createStatement().executeQuery("show create table dws.tmp_sync_hive");
        List<ColumnInfoDto> tableColumns = JiveUtil.extraColumnsInfo(res);
    }

}
