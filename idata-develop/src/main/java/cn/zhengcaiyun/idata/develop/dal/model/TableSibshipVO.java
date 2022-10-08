package cn.zhengcaiyun.idata.develop.dal.model;

import java.util.List;
import java.util.Objects;

public class TableSibshipVO {
    private String tableName;
    private String relation; //prev,next
    private List<TableSibshipVO> children;
    private String jobName;
    private Long jobId;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableSibshipVO> getChildren() {
        return children;
    }

    public void setChildren(List<TableSibshipVO> children) {
        this.children = children;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableSibshipVO that = (TableSibshipVO) o;
        return Objects.equals(tableName, that.tableName) && Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, jobId);
    }
}
