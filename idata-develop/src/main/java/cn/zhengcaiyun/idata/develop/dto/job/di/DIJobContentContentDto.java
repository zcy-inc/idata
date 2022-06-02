package cn.zhengcaiyun.idata.develop.dto.job.di;

import cn.zhengcaiyun.idata.commons.dto.general.KeyValuePair;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import cn.zhengcaiyun.idata.develop.util.JobVersionHelper;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class DIJobContentContentDto extends JobContentBaseDto {
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
     * 数据来源-分片数量（并行度）
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
     * 数据来源-表模式
     */
    private DITableFashionConfig srcTableConfig;

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
     * 补充迁移旧版Idata的di_query字段。用于复杂sql或函数sql，解决除了单表的简单列映射之外的场景。
     */
    private String srcQuery;

    /**
     * 来源为kafka数据类型的topic
     */
    private String srcTopic;

    /**
     * 目标为kafka的数据类的topic
     */
    private String destTopic;

    /**
     * 脚本模式，作用同可视化src_columns
     */
    private String scriptSelectColumns;

    /**
     * 脚本模式，同可视化src_columns中primaryKey字段
     */
    private String scriptKeyColumns;

    /**
     * 脚本模式，同可视化merge_sql
     */
    private String scriptMergeSql;

    /**
     * 脚本模式，同可视化src_query
     */
    private String scriptQuery;

    /**
     * 配置模式，1：可视化模式，2：脚本模式
     */
    private Integer configMode;

    /**
     * 多分片写入（并行度）
     */
    private Integer destShardingNum;

    /**
     * 单次批量写入数据条数
     */
    private Long destBulkNum;

    /**
     * merge_sql的参数
     */
    private ScriptMergeSqlParamDto scriptMergeSqlParamDto;

    /**
     * 目标库中间件的内置属性
     */
    private List<KeyValuePair<String, String>> destPropertyMap;

    public String getSrcDbName() {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName) {
        this.srcDbName = srcDbName;
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

    public List<KeyValuePair<String, String>> getDestPropertyMap() {
        return destPropertyMap;
    }

    public void setDestPropertyMap(List<KeyValuePair<String, String>> destPropertyMap) {
        this.destPropertyMap = destPropertyMap;
    }

    public DITableFashionConfig getSrcTableConfig() {
        return srcTableConfig;
    }

    public void setSrcTableConfig(DITableFashionConfig srcTableConfig) {
        this.srcTableConfig = srcTableConfig;
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
        if (StringUtils.isNotEmpty(content.getScriptMergeSqlParam())) {
            dto.setScriptMergeSqlParamDto(JSON.parseObject(content.getScriptMergeSqlParam(), ScriptMergeSqlParamDto.class));
        }
        if (StringUtils.isNotEmpty(content.getDestProperties())) {
            List<KeyValuePair<String, String>> mapList = new Gson().fromJson(content.getDestProperties(), new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType());
            dto.setDestPropertyMap(mapList);
        }
        dto.setVersionDisplay(JobVersionHelper.getVersionDisplay(content.getVersion(), content.getCreateTime()));

        DITableFashionConfig tableFashionConfig = new DITableFashionConfig();
        tableFashionConfig.setInputMode(content.getSrcTablesFashion());
        if (!"E".equals(content.getSrcTablesFashion())
                || !content.getSrcTables().contains("[")
                || !content.getSrcTables().contains("]")
                || !content.getSrcTables().contains("-")) {
            tableFashionConfig.setRawTable(content.getSrcTables());
        } else {
            String tableIdxStr = content.getSrcTables().substring(content.getSrcTables().indexOf("[") + 1, content.getSrcTables().indexOf("]"));
            List<String> tableIdxList = Splitter.on("-").trimResults().omitEmptyStrings().splitToList(tableIdxStr);
            if (tableIdxList.size() != 2) {
                tableFashionConfig.setRawTable(content.getSrcTables());
            } else {
                tableFashionConfig.setRawTable(content.getSrcTables().substring(0, content.getSrcTables().indexOf("[")));
                tableFashionConfig.setTableIdxBegin(Integer.parseInt(tableIdxList.get(0)));
                tableFashionConfig.setTableIdxEnd(Integer.parseInt(tableIdxList.get(1)));
            }
        }
        dto.setSrcTableConfig(tableFashionConfig);

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
        if (ObjectUtils.isNotEmpty(this.scriptMergeSqlParamDto)) {
            content.setScriptMergeSqlParam(JSON.toJSONString(this.scriptMergeSqlParamDto));
        } else {
            content.setScriptMergeSqlParam("");
        }
        if (ObjectUtils.isNotEmpty(this.destPropertyMap)) {
            content.setDestProperties(JSON.toJSONString(this.destPropertyMap));
        } else {
            content.setDestProperties("");
        }

        if (!Objects.isNull(this.srcTableConfig)) {
            if (!Objects.equals("E", this.srcTableConfig.getInputMode())
                    || Objects.isNull(this.srcTableConfig.getTableIdxBegin())
                    || Objects.isNull(this.srcTableConfig.getTableIdxEnd())) {
                if (StringUtils.isBlank(content.getSrcTables())) {
                    content.setSrcTables(this.srcTableConfig.getRawTable());
                }
            } else {
                content.setSrcTables(this.srcTableConfig.getRawTable() + "[" + this.srcTableConfig.getTableIdxBegin() + "-" + this.srcTableConfig.getTableIdxEnd() + "]");
            }
            content.setSrcTablesFashion(this.srcTableConfig.getInputMode());
        }

        return content;
    }
}