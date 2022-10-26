package cn.zhengcaiyun.idata.develop.dto.table;

import java.util.Date;

/**
 * 表血缘关系(TableSibship)实体类
 *
 * @author makejava
 * @since 2022-09-29 10:36:27
 */
public class TableSibship  {
    /**
     * 主键
     */
    private Long id;
    /**
     * 作业id
     */
    private Long jobId;
    /**
     * 输出库
     */
    private String db;
    /**
     * 输出表
     */
    private String tableName;
    /**
     * 输入库
     */
    private String sourceDb;
    /**
     * 输入表
     */
    private String sourceTableName;
    /**
     * 是否删除(1:是,0:否)
     */
    private Integer del;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String editor;
    /**
     * 修改时间
     */
    private Date editTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSourceDb() {
        return sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}

