package cn.zhengcaiyun.idata.develop.dto.job.di;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobContentDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业版本号
     */
    private Integer version;
    /**
     * 数据来源-数据源类型
     */
    private String srcDataSourceType;

    /**
     * 数据来源-数据源id
     */
    private Long srcDataSourceId;

    /**
     * 数据来源-读取模式，all：全量，incremental：增量
     */
    private String srcReadMode;

    /**
     * 数据来源-过滤条件
     */
    private String srcReadFilter;

    /**
     * 数据来源-切分键
     */
    private String srcReadShardKey;
    /**
     * 数据去向-数据源类型
     */
    private String destDataSourceType;

    /**
     * 数据去向-数据源id
     */
    private Long destDataSourceId;

    /**
     * 数据去向-数仓表id
     */
    private Long destTableId;

    /**
     * 数据去向-写入模式，override，upsert
     */
    private String srcWriteMode;

    /**
     * 数据去向-写入前语句
     */
    private String destBeforeWrite;

    /**
     * 数据去向-写入后语句
     */
    private String destAfterWrite;

    /**
     * 数据来源-表
     */
    private String srcTables;
    /**
     * 数据来源-字段信息
     */
    private List<MappingColumnDto> srcCols;
    /**
     * 数据去向-字段信息
     */
    private List<MappingColumnDto> destCols;
    /**
     * 作业内容hash，查询作业内容时返回，保存新的作业内容时需将该值一起提交，用于判断作业内容是否有变更
     */
    private String contentHash;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSrcDataSourceType() {
        return srcDataSourceType;
    }

    public void setSrcDataSourceType(String srcDataSourceType) {
        this.srcDataSourceType = srcDataSourceType;
    }

    public Long getSrcDataSourceId() {
        return srcDataSourceId;
    }

    public void setSrcDataSourceId(Long srcDataSourceId) {
        this.srcDataSourceId = srcDataSourceId;
    }

    public String getSrcReadMode() {
        return srcReadMode;
    }

    public void setSrcReadMode(String srcReadMode) {
        this.srcReadMode = srcReadMode;
    }

    public String getSrcReadFilter() {
        return srcReadFilter;
    }

    public void setSrcReadFilter(String srcReadFilter) {
        this.srcReadFilter = srcReadFilter;
    }

    public String getSrcReadShardKey() {
        return srcReadShardKey;
    }

    public void setSrcReadShardKey(String srcReadShardKey) {
        this.srcReadShardKey = srcReadShardKey;
    }

    public String getDestDataSourceType() {
        return destDataSourceType;
    }

    public void setDestDataSourceType(String destDataSourceType) {
        this.destDataSourceType = destDataSourceType;
    }

    public Long getDestDataSourceId() {
        return destDataSourceId;
    }

    public void setDestDataSourceId(Long destDataSourceId) {
        this.destDataSourceId = destDataSourceId;
    }

    public Long getDestTableId() {
        return destTableId;
    }

    public void setDestTableId(Long destTableId) {
        this.destTableId = destTableId;
    }

    public String getSrcWriteMode() {
        return srcWriteMode;
    }

    public void setSrcWriteMode(String srcWriteMode) {
        this.srcWriteMode = srcWriteMode;
    }

    public String getDestBeforeWrite() {
        return destBeforeWrite;
    }

    public void setDestBeforeWrite(String destBeforeWrite) {
        this.destBeforeWrite = destBeforeWrite;
    }

    public String getDestAfterWrite() {
        return destAfterWrite;
    }

    public void setDestAfterWrite(String destAfterWrite) {
        this.destAfterWrite = destAfterWrite;
    }

    public String getSrcTables() {
        return srcTables;
    }

    public void setSrcTables(String srcTables) {
        this.srcTables = srcTables;
    }

    public List<MappingColumnDto> getSrcCols() {
        return srcCols;
    }

    public void setSrcCols(List<MappingColumnDto> srcCols) {
        this.srcCols = srcCols;
    }

    public List<MappingColumnDto> getDestCols() {
        return destCols;
    }

    public void setDestCols(List<MappingColumnDto> destCols) {
        this.destCols = destCols;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }
}