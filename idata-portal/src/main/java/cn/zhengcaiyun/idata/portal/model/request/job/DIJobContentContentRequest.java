package cn.zhengcaiyun.idata.portal.model.request.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobContentContentRequest extends JobContentBaseDto {
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

    /**
     * DI作业增量模式的mergeSql
     */
    private String mergeSql;

    /**
     *   数据流向，IN-数据集成、OUT-数据回流
     */
    private String direct;

    /**
     *   来源为kafka数据类型的topic
     */
    private String srcTopic;

    /**
     *   目标为kafka的数据类的topic
     */
    private String destTopic;

    /**
     *   脚本模式，作用同可视化src_columns
     */
    private String scriptSelectColumns;

    /**
     *   脚本模式，同可视化src_columns中primaryKey字段
     */
    private String scriptKeyColumns;

    /**
     *   脚本模式，同可视化merge_sql
     */
    private String scriptMergeSql;

    /**
     *   脚本模式，同可视化src_query
     */
    private String scriptQuery;

    /**
     *   配置模式，1：可视化模式，2：脚本模式
     */
    private Integer configMode;

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

    public String getMergeSql() {
        return mergeSql;
    }

    public void setMergeSql(String mergeSql) {
        this.mergeSql = mergeSql;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getSrcTopic() {
        return srcTopic;
    }

    public void setSrcTopic(String srcTopic) {
        this.srcTopic = srcTopic;
    }

    public String getDestTopic() {
        return destTopic;
    }

    public void setDestTopic(String destTopic) {
        this.destTopic = destTopic;
    }

    public String getScriptSelectColumns() {
        return scriptSelectColumns;
    }

    public void setScriptSelectColumns(String scriptSelectColumns) {
        this.scriptSelectColumns = scriptSelectColumns;
    }

    public String getScriptKeyColumns() {
        return scriptKeyColumns;
    }

    public void setScriptKeyColumns(String scriptKeyColumns) {
        this.scriptKeyColumns = scriptKeyColumns;
    }

    public String getScriptMergeSql() {
        return scriptMergeSql;
    }

    public void setScriptMergeSql(String scriptMergeSql) {
        this.scriptMergeSql = scriptMergeSql;
    }

    public String getScriptQuery() {
        return scriptQuery;
    }

    public void setScriptQuery(String scriptQuery) {
        this.scriptQuery = scriptQuery;
    }

    public Integer getConfigMode() {
        return configMode;
    }

    public void setConfigMode(Integer configMode) {
        this.configMode = configMode;
    }
}