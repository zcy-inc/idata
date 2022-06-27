package cn.zhengcaiyun.idata.connector.spi.hdfs;

/**
 * author:zheng
 * Date:2022/6/23
 */
public class HdfsFileInfo {
    private Long createTime;
    private Long modifyTime;
    private Long blockSize;

    public HdfsFileInfo(Long createTime, Long modifyTime, Long blockSize) {
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.blockSize = blockSize;
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
}
