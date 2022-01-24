package cn.zhengcaiyun.idata.develop.dal.dao.job;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class JobPublishRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    public static final JobPublishRecord jobPublishRecord = new JobPublishRecord();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.id")
    public static final SqlColumn<Long> id = jobPublishRecord.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.del")
    public static final SqlColumn<Integer> del = jobPublishRecord.del;

    /**
     * Database Column Remarks:
     *   创建者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.creator")
    public static final SqlColumn<String> creator = jobPublishRecord.creator;

    /**
     * Database Column Remarks:
     *   创建时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.create_time")
    public static final SqlColumn<Date> createTime = jobPublishRecord.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.editor")
    public static final SqlColumn<String> editor = jobPublishRecord.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.edit_time")
    public static final SqlColumn<Date> editTime = jobPublishRecord.editTime;

    /**
     * Database Column Remarks:
     *   作业id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.job_id")
    public static final SqlColumn<Long> jobId = jobPublishRecord.jobId;

    /**
     * Database Column Remarks:
     *   作业内容id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.job_content_id")
    public static final SqlColumn<Long> jobContentId = jobPublishRecord.jobContentId;

    /**
     * Database Column Remarks:
     *   作业内容版本号
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.job_content_version")
    public static final SqlColumn<Integer> jobContentVersion = jobPublishRecord.jobContentVersion;

    /**
     * Database Column Remarks:
     *   作业类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.job_type_code")
    public static final SqlColumn<String> jobTypeCode = jobPublishRecord.jobTypeCode;

    /**
     * Database Column Remarks:
     *   数仓分层
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.dw_layer_code")
    public static final SqlColumn<String> dwLayerCode = jobPublishRecord.dwLayerCode;

    /**
     * Database Column Remarks:
     *   环境
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.environment")
    public static final SqlColumn<String> environment = jobPublishRecord.environment;

    /**
     * Database Column Remarks:
     *   发布状态，1：待发布，2：已发布，4：已驳回，9：已归档
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.publish_status")
    public static final SqlColumn<Integer> publishStatus = jobPublishRecord.publishStatus;

    /**
     * Database Column Remarks:
     *   提交备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.submit_remark")
    public static final SqlColumn<String> submitRemark = jobPublishRecord.submitRemark;

    /**
     * Database Column Remarks:
     *   审批人
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.approve_operator")
    public static final SqlColumn<String> approveOperator = jobPublishRecord.approveOperator;

    /**
     * Database Column Remarks:
     *   审批时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.approve_time")
    public static final SqlColumn<Date> approveTime = jobPublishRecord.approveTime;

    /**
     * Database Column Remarks:
     *   审批备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_job_publish_record.approve_remark")
    public static final SqlColumn<String> approveRemark = jobPublishRecord.approveRemark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_publish_record")
    public static final class JobPublishRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> jobId = column("job_id", JDBCType.BIGINT);

        public final SqlColumn<Long> jobContentId = column("job_content_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> jobContentVersion = column("job_content_version", JDBCType.INTEGER);

        public final SqlColumn<String> jobTypeCode = column("job_type_code", JDBCType.VARCHAR);

        public final SqlColumn<String> dwLayerCode = column("dw_layer_code", JDBCType.VARCHAR);

        public final SqlColumn<String> environment = column("environment", JDBCType.VARCHAR);

        public final SqlColumn<Integer> publishStatus = column("publish_status", JDBCType.INTEGER);

        public final SqlColumn<String> submitRemark = column("submit_remark", JDBCType.VARCHAR);

        public final SqlColumn<String> approveOperator = column("approve_operator", JDBCType.VARCHAR);

        public final SqlColumn<Date> approveTime = column("approve_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> approveRemark = column("approve_remark", JDBCType.VARCHAR);

        public JobPublishRecord() {
            super("dev_job_publish_record");
        }
    }
}