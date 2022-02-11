package cn.zhengcaiyun.idata.portal.model.request.job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GenerateMergeSqlRequest {

    /**
     * 筛选列，字符串列，逗号换行分隔
     */
    @NotEmpty(message = "selectColumns 不能为空")
    private String selectColumns;

    /**
     * 主键id，支持组合，逗号分隔
     */
    @NotEmpty(message = "keyColumns 不能为空")
    private String keyColumns;

    /**
     * 数据源抽取table名字，支持多表，格式如下
     * 1. "tableName[1-2]"
     * 2. "212tableName2,223tableName2"
     */
    @NotEmpty(message = "sourceTable 不能为空")
    private String sourceTable;

    /**
     * 写入的table名字，仅限hive表
     */
    @NotEmpty(message = "destTable 不能为空")
    private String destTable;

    /**
     * 写入模式 init/overwrite/append;
     */
    @NotEmpty(message = "destWriteMode 不能为空")
    private String destWriteMode;

    /**
     * 目标表驱动类型mysql/hive2/postgresql
     */
    @NotEmpty(message = "driverType 不能为空")
    private String driverType;

    /**
     * 近天数，默认3
     */
    private Integer days = 3;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(String selectColumns) {
        this.selectColumns = selectColumns;
    }

    public String getKeyColumns() {
        return keyColumns;
    }

    public void setKeyColumns(String keyColumns) {
        this.keyColumns = keyColumns;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public String getDestWriteMode() {
        return destWriteMode;
    }

    public void setDestWriteMode(String destWriteMode) {
        this.destWriteMode = destWriteMode;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    /**
     * 将换行符和空格去除
     */
    public void formatColumns() {
        this.keyColumns = this.keyColumns.replaceAll("\n", "").replaceAll(" ", "");
    }
}
