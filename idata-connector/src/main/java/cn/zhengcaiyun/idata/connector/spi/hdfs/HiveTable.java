package cn.zhengcaiyun.idata.connector.spi.hdfs;

/**
 * author:zheng
 * Date:2022/6/23
 */
public class HiveTable {
    private String tableName;

    private String comment;
    private Long createTime;
    private Long modifyTime;
    private Long blockSize;
    private boolean partitioned;

    public HiveTable() {
    }

    public HiveTable(String tableName, Long createTime, Long modifyTime, Long blockSize) {
        this.tableName = tableName;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.blockSize = blockSize;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Long blockSize) {
        this.blockSize = blockSize;
    }

    public boolean isPartitioned() {
        return partitioned;
    }

    public void setPartitioned(boolean partitioned) {
        this.partitioned = partitioned;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
