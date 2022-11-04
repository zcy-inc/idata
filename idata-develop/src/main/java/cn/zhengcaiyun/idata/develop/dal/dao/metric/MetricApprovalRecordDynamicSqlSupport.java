package cn.zhengcaiyun.idata.develop.dal.dao.metric;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class MetricApprovalRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    public static final MetricApprovalRecord METRIC_APPROVAL_RECORD = new MetricApprovalRecord();

    /**
     * Database Column Remarks:
     *   主键
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.id")
    public static final SqlColumn<Long> id = METRIC_APPROVAL_RECORD.id;

    /**
     * Database Column Remarks:
     *   是否删除，0否，1是
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.del")
    public static final SqlColumn<Integer> del = METRIC_APPROVAL_RECORD.del;

    /**
     * Database Column Remarks:
     *   创建/提交者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.creator")
    public static final SqlColumn<String> creator = METRIC_APPROVAL_RECORD.creator;

    /**
     * Database Column Remarks:
     *   创建/提交时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.create_time")
    public static final SqlColumn<Date> createTime = METRIC_APPROVAL_RECORD.createTime;

    /**
     * Database Column Remarks:
     *   修改者
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.editor")
    public static final SqlColumn<String> editor = METRIC_APPROVAL_RECORD.editor;

    /**
     * Database Column Remarks:
     *   修改时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.edit_time")
    public static final SqlColumn<Date> editTime = METRIC_APPROVAL_RECORD.editTime;

    /**
     * Database Column Remarks:
     *   数据指标id
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_id")
    public static final SqlColumn<String> metricId = METRIC_APPROVAL_RECORD.metricId;

    /**
     * Database Column Remarks:
     *   数据指标名称
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_name")
    public static final SqlColumn<String> metricName = METRIC_APPROVAL_RECORD.metricName;

    /**
     * Database Column Remarks:
     *   数据指标类型
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.metric_tag")
    public static final SqlColumn<String> metricTag = METRIC_APPROVAL_RECORD.metricTag;

    /**
     * Database Column Remarks:
     *   数据域
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_domain")
    public static final SqlColumn<String> bizDomain = METRIC_APPROVAL_RECORD.bizDomain;

    /**
     * Database Column Remarks:
     *   业务过程
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.biz_process")
    public static final SqlColumn<String> bizProcess = METRIC_APPROVAL_RECORD.bizProcess;

    /**
     * Database Column Remarks:
     *   审批状态，1：待审批，2：已审批，3：已撤回，4：已驳回
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approval_status")
    public static final SqlColumn<Integer> approvalStatus = METRIC_APPROVAL_RECORD.approvalStatus;

    /**
     * Database Column Remarks:
     *   提交备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.submit_remark")
    public static final SqlColumn<String> submitRemark = METRIC_APPROVAL_RECORD.submitRemark;

    /**
     * Database Column Remarks:
     *   审批人
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_operator")
    public static final SqlColumn<String> approveOperator = METRIC_APPROVAL_RECORD.approveOperator;

    /**
     * Database Column Remarks:
     *   审批时间
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_time")
    public static final SqlColumn<Date> approveTime = METRIC_APPROVAL_RECORD.approveTime;

    /**
     * Database Column Remarks:
     *   审批备注
     */
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: dev_metric_approval_record.approve_remark")
    public static final SqlColumn<String> approveRemark = METRIC_APPROVAL_RECORD.approveRemark;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_metric_approval_record")
    public static final class MetricApprovalRecord extends AliasableSqlTable<MetricApprovalRecord> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> del = column("del", JDBCType.TINYINT);

        public final SqlColumn<String> creator = column("creator", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> editor = column("editor", JDBCType.VARCHAR);

        public final SqlColumn<Date> editTime = column("edit_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> metricId = column("metric_id", JDBCType.VARCHAR);

        public final SqlColumn<String> metricName = column("metric_name", JDBCType.VARCHAR);

        public final SqlColumn<String> metricTag = column("metric_tag", JDBCType.VARCHAR);

        public final SqlColumn<String> bizDomain = column("biz_domain", JDBCType.VARCHAR);

        public final SqlColumn<String> bizProcess = column("biz_process", JDBCType.VARCHAR);

        public final SqlColumn<Integer> approvalStatus = column("approval_status", JDBCType.INTEGER);

        public final SqlColumn<String> submitRemark = column("submit_remark", JDBCType.VARCHAR);

        public final SqlColumn<String> approveOperator = column("approve_operator", JDBCType.VARCHAR);

        public final SqlColumn<Date> approveTime = column("approve_time", JDBCType.TIMESTAMP);

        public final SqlColumn<String> approveRemark = column("approve_remark", JDBCType.VARCHAR);

        public MetricApprovalRecord() {
            super("dev_metric_approval_record", MetricApprovalRecord::new);
        }
    }
}