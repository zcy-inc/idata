package cn.zhengcaiyun.idata.portal.model.response.job;

import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.ScriptMergeSqlParamDto;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

public class DIJobContentResponse {

    private Long id;
    private Long jobId;
    private Integer editable;
    private Integer version;
    /**
     * 作业类型code：DI/BACK_FLOW
     *
     */
    private String jobTypeCode;

    /**
     * jobType枚举，历史保留
     */
    private JobTypeEnum jobType;

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
     * 数据来源-database
     */
    private String srcDbName;

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
     *   补充迁移旧版Idata的di_query字段。用于复杂sql或函数sql，解决除了单表的简单列映射之外的场景。
     */
    private String srcQuery;

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

    /**
     *   多分片写入（并行度）
     */
    private Integer destShardingNum;

    /**
     *   单次批量写入数据条数
     */
    private Long destBulkNum;

    /**
     *   merge_sql的参数
     */
    private ScriptMergeSqlParamDto scriptMergeSqlParamDto;

    /**
     *   目标库中间件的内置属性
     */
    private Map<String, String> destPropertyMap;


    public String getSrcDbName() {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName) {
        this.srcDbName = srcDbName;
    }

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

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
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

    public String getSrcQuery() {
        return srcQuery;
    }

    public void setSrcQuery(String srcQuery) {
        this.srcQuery = srcQuery;
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

    public Integer getDestShardingNum() {
        return destShardingNum;
    }

    public void setDestShardingNum(Integer destShardingNum) {
        this.destShardingNum = destShardingNum;
    }

    public Long getDestBulkNum() {
        return destBulkNum;
    }

    public void setDestBulkNum(Long destBulkNum) {
        this.destBulkNum = destBulkNum;
    }

    public ScriptMergeSqlParamDto getScriptMergeSqlParamDto() {
        return scriptMergeSqlParamDto;
    }

    public void setScriptMergeSqlParamDto(ScriptMergeSqlParamDto scriptMergeSqlParamDto) {
        this.scriptMergeSqlParamDto = scriptMergeSqlParamDto;
    }

    public Map<String, String> getDestPropertyMap() {
        return destPropertyMap;
    }

    public void setDestPropertyMap(Map<String, String> destPropertyMap) {
        this.destPropertyMap = destPropertyMap;
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public String getJobTypeCode() {
        return jobTypeCode;
    }

    public void setJobTypeCode(String jobTypeCode) {
        this.jobTypeCode = jobTypeCode;
    }
}