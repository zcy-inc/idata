package cn.zhengcaiyun.idata.develop.dto.job.di;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Generated;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobContentContentDto extends JobContentBaseDto {
    /**
     * 作业版本号描述
     */
    private String versionDisplay;
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
     *   数据来源-分片数量（并行度）
     */
    private Integer srcShardingNum;

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
    private String destTable;

    /**
     * 数据去向-写入模式，init: 重建表，override：覆盖表
     */
    private String destWriteMode;

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

    public String getVersionDisplay() {
        return versionDisplay;
    }

    public void setVersionDisplay(String versionDisplay) {
        this.versionDisplay = versionDisplay;
    }

    public Integer getSrcShardingNum() {
        return srcShardingNum;
    }

    public void setSrcShardingNum(Integer srcShardingNum) {
        this.srcShardingNum = srcShardingNum;
    }

    public static DIJobContentContentDto from(DIJobContent content) {
        DIJobContentContentDto dto = new DIJobContentContentDto();
        BeanUtils.copyProperties(content, dto);

        if (StringUtils.isNotBlank(content.getSrcColumns())) {
            dto.setSrcCols(JSON.parseArray(content.getSrcColumns(), MappingColumnDto.class));
        }
        if (StringUtils.isNotBlank(content.getDestColumns())) {
            dto.setDestCols(JSON.parseArray(content.getDestColumns(), MappingColumnDto.class));
        }
        dto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));
        return dto;
    }

    public DIJobContent toModel() {
        DIJobContent content = new DIJobContent();
        BeanUtils.copyProperties(this, content);

        if (ObjectUtils.isNotEmpty(this.srcCols)) {
            content.setSrcColumns(JSON.toJSONString(this.srcCols));
        } else {
            content.setSrcColumns("");
        }
        if (ObjectUtils.isNotEmpty(this.destCols)) {
            content.setDestColumns(JSON.toJSONString(this.destCols));
        } else {
            content.setDestColumns("");
        }
        return content;
    }
}