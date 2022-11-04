package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class DIStreamJobTableDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    public static final DIStreamJobTable DI_STREAM_JOB_TABLE = new DIStreamJobTable();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.id")
    public static final SqlColumn<Long> id = DI_STREAM_JOB_TABLE.id;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_id")
    public static final SqlColumn<Long> jobId = DI_STREAM_JOB_TABLE.jobId;

    /**
     * Database Column Remarks:
     *   作业内容id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_id")
    public static final SqlColumn<Long> jobContentId = DI_STREAM_JOB_TABLE.jobContentId;

    /**
     * Database Column Remarks:
     *   作业内容版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.job_content_version")
    public static final SqlColumn<Integer> jobContentVersion = DI_STREAM_JOB_TABLE.jobContentVersion;

    /**
     * Database Column Remarks:
     *   数据来源-表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.src_table")
    public static final SqlColumn<String> srcTable = DI_STREAM_JOB_TABLE.srcTable;

    /**
     * Database Column Remarks:
     *   数据去向-表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.dest_table")
    public static final SqlColumn<String> destTable = DI_STREAM_JOB_TABLE.destTable;

    /**
     * Database Column Remarks:
     *   是否分表
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.sharding")
    public static final SqlColumn<Integer> sharding = DI_STREAM_JOB_TABLE.sharding;

    /**
     * Database Column Remarks:
     *   是否强制初始化，0：否，1：是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.force_init")
    public static final SqlColumn<Integer> forceInit = DI_STREAM_JOB_TABLE.forceInit;

    /**
     * Database Column Remarks:
     *   表cdc配置
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_content_di_stream_table.table_cdc_props")
    public static final SqlColumn<String> tableCdcProps = DI_STREAM_JOB_TABLE.tableCdcProps;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di_stream_table")
    public static final class DIStreamJobTable extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Long> jobContentId = column("job_content_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> jobContentVersion = column("job_content_version", JDBCType.INTEGER);

        public final SqlColumn<String> srcTable = column("src_table", JDBCType.VARCHAR);

        public final SqlColumn<String> destTable = column("dest_table", JDBCType.VARCHAR);

        public final SqlColumn<Integer> sharding = column("sharding", JDBCType.INTEGER);

        public final SqlColumn<Integer> forceInit = column("force_init", JDBCType.INTEGER);

        public final SqlColumn<String> tableCdcProps = column("table_cdc_props", JDBCType.LONGVARCHAR);

        public DIStreamJobTable() {
            super("dev_job_content_di_stream_table");
        }
    }
}