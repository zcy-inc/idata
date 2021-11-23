package cn.zhengcaiyun.idata.connector.clients.hive.util;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JiveUtil {


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
    public static List<ColumnInfoDto> extraColumnsInfo(ResultSet res) throws SQLException {
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
}
