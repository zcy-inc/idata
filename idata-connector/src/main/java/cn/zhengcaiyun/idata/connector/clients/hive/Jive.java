package cn.zhengcaiyun.idata.connector.clients.hive;


import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
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

    public void test() throws SQLException {

        ResultSet res = getClient().createStatement().executeQuery("show create table dws.tmp_sync_hive");
        List<ColumnInfoDto> tableColumns = extraColumnsInfo(res);
//        System.out.println(tableColumns.size());
    }

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
}
