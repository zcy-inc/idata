package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EngineTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.SrcReadModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.WriteModeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

public class JobInfoExecuteDetailDto {

    /**
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum
     */
    private JobTypeEnum jobTypeEnum; // appType

    @JsonIgnore
    private String jobType;
    /**
     * driver节点内存
     */
    private String driverMemory;
    /**
     * worker节点内存
     */
    private String executorMemory;
    /**
     * 核数
     */
    private int executorCores;
    /**
     * Yarn集群提交队列
     */
    private String execQueue;
    /**
     * 作业执行引擎，可传SPARK/SQOOP/KYLIN
     */
    private EngineTypeEnum execEngine;
    /**
     * 扩展配置参数，数据库json string格式，转化为confProp
     */
    private String extProperties;
    /**
     * 扩展参数配置
     */
    private Map<String, String> confProp;

    public static class DiJobDetailsDto extends JobInfoExecuteDetailDto {
        public DiJobDetailsDto() {

        }

        public DiJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        // 连接信息
        private String srcDataType;
        private String srcJdbcUrl;// jdbcUrlPath
        private String srcUsername;// username
        private String srcPassword;// password
        private DriverTypeEnum srcDriverType;// driverType
        private String srcDbName; //dbName
        /**
         * 数据来源-来源表
         */
        private String srcTables;//sourceTableName
        /**
         * 数据去向-目标表
         */
        private String destTable;//带库名 targetTableName

        /**
         * 数据去向-写入模式，init: 新建表，overwrite: 覆盖表，append：追加表
         * //         * @see cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum
         *
         * @see WriteModeEnum.DiEnum
         */
        private WriteModeEnum.DiEnum destWriteMode; // isRecreate
        /**
         * 数据来源-读取模式，all：全量，incremental：增量
         */
        private SrcReadModeEnum srcReadMode;
        /**
         * 数据来源-切分键 srcShardingNum>1 必填
         */
        private String srcReadShardKey;
        /**
         * 数据来源-分片数量（并行度） 必填
         */
        private Integer srcShardingNum;
        /**
         * TODO
         */
        private String mergeSql;
        /**
         * 数据来源-过滤条件
         */
        private String srcReadFilter; // diCondition
        /**
         * 替代 srcReadFilter + columns
         */
        private String diQuery;
        /**
         * 数据来源-字段信息
         */
        private List<MappingColumnDto> srcCols; // diColumns

        public String getSrcDataType() {
            return srcDataType;
        }

        public void setSrcDataType(String srcDataType) {
            this.srcDataType = srcDataType;
        }

        public String getSrcJdbcUrl() {
            return srcJdbcUrl;
        }

        public void setSrcJdbcUrl(String srcJdbcUrl) {
            this.srcJdbcUrl = srcJdbcUrl;
        }

        public String getSrcUsername() {
            return srcUsername;
        }

        public void setSrcUsername(String srcUsername) {
            this.srcUsername = srcUsername;
        }

        public String getSrcPassword() {
            return srcPassword;
        }

        public void setSrcPassword(String srcPassword) {
            this.srcPassword = srcPassword;
        }

        public List<MappingColumnDto> getSrcCols() {
            return srcCols;
        }

        public void setSrcCols(List<MappingColumnDto> srcCols) {
            this.srcCols = srcCols;
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

        public Integer getSrcShardingNum() {
            return srcShardingNum;
        }

        public void setSrcShardingNum(Integer srcShardingNum) {
            this.srcShardingNum = srcShardingNum;
        }

        public String getDestTable() {
            return destTable;
        }

        public void setDestTable(String destTable) {
            this.destTable = destTable;
        }

        public WriteModeEnum.DiEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(WriteModeEnum.DiEnum destWriteMode) {
            this.destWriteMode = destWriteMode;
        }

        public String getSrcTables() {
            return srcTables;
        }

        public void setSrcTables(String srcTables) {
            this.srcTables = srcTables;
        }


        public String getMergeSql() {
            return mergeSql;
        }

        public void setMergeSql(String mergeSql) {
            this.mergeSql = mergeSql;
        }

        public String getDiQuery() {
            return diQuery;
        }

        public void setDiQuery(String diQuery) {
            this.diQuery = diQuery;
        }

        public DriverTypeEnum getSrcDriverType() {
            return srcDriverType;
        }

        public void setSrcDriverType(DriverTypeEnum srcDriverType) {
            this.srcDriverType = srcDriverType;
        }

        public String getSrcDbName() {
            return srcDbName;
        }

        public void setSrcDbName(String srcDbName) {
            this.srcDbName = srcDbName;
        }

        public SrcReadModeEnum getSrcReadMode() {
            return srcReadMode;
        }

        public void setSrcReadMode(SrcReadModeEnum srcReadMode) {
            this.srcReadMode = srcReadMode;
        }
    }

    public static class BackFlowDetailDto extends JobInfoExecuteDetailDto {
        /**
         * 作业sql内容，里面包含srcTable以及逻辑
         */
        private String srcSql;//sourceSql;
        private WriteModeEnum.BackFlowEnum destWriteMode; //DestWriteModeEnum destWriteMode;
        private List<JobUdfDto> udfList; // udfs; 老版本使用，新版本不会关联
        private DriverTypeEnum destDriverType;//;targetDriverType;
        private String destUrlPath;//targetUrlPath;
        private String destUserName;//username;
        private String destPassword;//password;
        private String destTable;//targetTableName;
        private boolean destDRDS = false; // 是否是分布式数据库
        private String srcTable;
        private String destColumnNames; // dbColumnNames src数据源的列
        private String updateKey; //新版本
        private Integer parallelism; //新版本
        private Map<String, String> destPropMap; //其余属性定义map

        public BackFlowDetailDto() {
        }

        public BackFlowDetailDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        public boolean isDestDRDS() {
            return destDRDS;
        }

        public void setDestDRDS(boolean destDRDS) {
            this.destDRDS = destDRDS;
        }

        public String getUpdateKey() {
            return updateKey;
        }

        public void setUpdateKey(String updateKey) {
            this.updateKey = updateKey;
        }

        public WriteModeEnum.BackFlowEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(WriteModeEnum.BackFlowEnum destWriteMode) {
            this.destWriteMode = destWriteMode;
        }

        public String getDestColumnNames() {
            return destColumnNames;
        }

        public void setDestColumnNames(String destColumnNames) {
            this.destColumnNames = destColumnNames;
        }

        public String getSrcSql() {
            return srcSql;
        }

        public void setSrcSql(String srcSql) {
            this.srcSql = srcSql;
        }

        public String getDestTable() {
            return destTable;
        }

        public void setDestTable(String destTable) {
            this.destTable = destTable;
        }

        public String getDestUrlPath() {
            return destUrlPath;
        }

        public void setDestUrlPath(String destUrlPath) {
            this.destUrlPath = destUrlPath;
        }

        public DriverTypeEnum getDestDriverType() {
            return destDriverType;
        }

        public void setDestDriverType(DriverTypeEnum destDriverType) {
            this.destDriverType = destDriverType;
        }

        public List<JobUdfDto> getUdfList() {
            return udfList;
        }

        public void setUdfList(List<JobUdfDto> udfList) {
            this.udfList = udfList;
        }

        public String getSrcTable() {
            return srcTable;
        }

        public void setSrcTable(String srcTable) {
            this.srcTable = srcTable;
        }

        public String getDestUserName() {
            return destUserName;
        }

        public void setDestUserName(String destUserName) {
            this.destUserName = destUserName;
        }

        public String getDestPassword() {
            return destPassword;
        }

        public void setDestPassword(String destPassword) {
            this.destPassword = destPassword;
        }

        public Map<String, String> getDestPropMap() {
            return destPropMap;
        }

        public void setDestPropMap(Map<String, String> destPropMap) {
            this.destPropMap = destPropMap;
        }

        public Integer getParallelism() {
            return parallelism;
        }

        public void setParallelism(Integer parallelism) {
            this.parallelism = parallelism;
        }
    }

    public static class SqlJobDetailsDto extends JobInfoExecuteDetailDto {
        public SqlJobDetailsDto() {

        }

        public SqlJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        /**
         * 数据去向-目标表
         */
        private String destTable; // targetTableName

        /**
         * 数据去向-写入模式，overwrite，upsert
         */
        private WriteModeEnum.SqlEnum destWriteMode; // saveMode

        /**
         * 数据来源表主键(写入模式为upsert时必填)
         */
        private String jobTargetTablePk;

        /**
         * 是否开启小文件合并
         */
        private Boolean isOpenMergeFile;
        /**
         * 数据来源SQL
         */
        private String sourceSql; // sourceSql
        /**
         * 函数
         */
        private List<JobUdfDto> udfList; // udfs

        private List<ExternalTableDto> externalTableList;

        public String getDestTable() {
            return destTable;
        }

        public void setDestTable(String destTable) {
            this.destTable = destTable;
        }

        public WriteModeEnum.SqlEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(WriteModeEnum.SqlEnum destWriteMode) {
            this.destWriteMode = destWriteMode;
        }

        public String getJobTargetTablePk() {
            return jobTargetTablePk;
        }

        public void setJobTargetTablePk(String jobTargetTablePk) {
            this.jobTargetTablePk = jobTargetTablePk;
        }

        public Boolean getOpenMergeFile() {
            return isOpenMergeFile;
        }

        public void setOpenMergeFile(Boolean openMergeFile) {
            isOpenMergeFile = openMergeFile;
        }

        public List<JobUdfDto> getUdfList() {
            return udfList;
        }

        public void setUdfList(List<JobUdfDto> udfList) {
            this.udfList = udfList;
        }

        public String getSourceSql() {
            return sourceSql;
        }

        public void setSourceSql(String sourceSql) {
            this.sourceSql = sourceSql;
        }

        public List<ExternalTableDto> getExternalTableList() {
            return externalTableList;
        }

        public void setExternalTableList(List<ExternalTableDto> externalTableList) {
            this.externalTableList = externalTableList;
        }

        public static class ExternalTableDto {
            /**
             * StarRocks
             */
            private String extSrcType;
            private String extSrcUrl;
            private String extSrcUsername;
            private String extSrcPassword;

            private List<String> extSrcTables;

            public String getExtSrcType() {
                return extSrcType;
            }

            public void setExtSrcType(String extSrcType) {
                this.extSrcType = extSrcType;
            }

            public String getExtSrcUrl() {
                return extSrcUrl;
            }

            public void setExtSrcUrl(String extSrcUrl) {
                this.extSrcUrl = extSrcUrl;
            }

            public String getExtSrcUsername() {
                return extSrcUsername;
            }

            public void setExtSrcUsername(String extSrcUsername) {
                this.extSrcUsername = extSrcUsername;
            }

            public String getExtSrcPassword() {
                return extSrcPassword;
            }

            public void setExtSrcPassword(String extSrcPassword) {
                this.extSrcPassword = extSrcPassword;
            }

            public List<String> getExtSrcTables() {
                return extSrcTables;
            }

            public void setExtSrcTables(List<String> extSrcTables) {
                this.extSrcTables = extSrcTables;
            }
        }
    }

    public static class SparkJobDetailsDto extends JobInfoExecuteDetailDto {
        public SparkJobDetailsDto() {

        }

        public SparkJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        private String resourceHdfsPath; // resourceHdfsPath
        private String appArguments; // appArguments
        private String mainClass; //mainClass

        private List<String> dependResHdfsPaths; // 目前缺少

        public String getResourceHdfsPath() {
            return resourceHdfsPath;
        }

        public void setResourceHdfsPath(String resourceHdfsPath) {
            this.resourceHdfsPath = resourceHdfsPath;
        }

        public String getAppArguments() {
            return appArguments;
        }

        public void setAppArguments(String appArguments) {
            this.appArguments = appArguments;
        }

        public String getMainClass() {
            return mainClass;
        }

        public void setMainClass(String mainClass) {
            this.mainClass = mainClass;
        }
    }

    public static class KylinDetailJobDto extends JobInfoExecuteDetailDto {
        public KylinDetailJobDto() {

        }

        public KylinDetailJobDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        private String cubeName;
        private String buildType;

        public String getCubeName() {
            return cubeName;
        }

        public void setCubeName(String cubeName) {
            this.cubeName = cubeName;
        }

        public String getBuildType() {
            return buildType;
        }

        public void setBuildType(String buildType) {
            this.buildType = buildType;
        }
    }

    public static class ScriptJobDetailsDto extends JobInfoExecuteDetailDto {
        public ScriptJobDetailsDto() {

        }

        public ScriptJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        /**
         * 脚本资源内容
         */
        private String sourceResource; // resourceHdfsPath

        /**
         * 脚本参数 空格隔开]
         */
        private String scriptArguments; // scriptArguments

        public String getSourceResource() {
            return sourceResource;
        }

        public void setSourceResource(String sourceResource) {
            this.sourceResource = sourceResource;
        }

        public String getScriptArguments() {
            return scriptArguments;
        }

        public void setScriptArguments(String scriptArguments) {
            this.scriptArguments = scriptArguments;
        }
    }

    public static class FlinkSqlJobDetailsDto extends JobInfoExecuteDetailDto {

        public FlinkSqlJobDetailsDto() {

        }

        public FlinkSqlJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        /**
         * 作业版本
         */
        private String jobVersion;
        /**
         * 数据来源SQL
         */
        private String sourceSql; // sourceSql
        /**
         * 函数
         */
        private List<JobUdfDto> udfList; // udfs
        /**
         * 作业私有信息
         */
        private Map<String, String> jobPrivacyProp;
        /**
         * 是否已发布作业，未发布作业走调试流程
         */
        private Boolean published;
        /**
         * flink版本
         */
        private String flinkVersion;

        public String getJobVersion() {
            return jobVersion;
        }

        public void setJobVersion(String jobVersion) {
            this.jobVersion = jobVersion;
        }

        public String getSourceSql() {
            return sourceSql;
        }

        public void setSourceSql(String sourceSql) {
            this.sourceSql = sourceSql;
        }

        public List<JobUdfDto> getUdfList() {
            return udfList;
        }

        public void setUdfList(List<JobUdfDto> udfList) {
            this.udfList = udfList;
        }

        public Map<String, String> getJobPrivacyProp() {
            return jobPrivacyProp;
        }

        public void setJobPrivacyProp(Map<String, String> jobPrivacyProp) {
            this.jobPrivacyProp = jobPrivacyProp;
        }

        public Boolean getPublished() {
            return published;
        }

        public void setPublished(Boolean published) {
            this.published = published;
        }

        public String getFlinkVersion() {
            return flinkVersion;
        }

        public void setFlinkVersion(String flinkVersion) {
            this.flinkVersion = flinkVersion;
        }
    }

    public static final class FlinkCDCJobDetailDto extends JobInfoExecuteDetailDto {

        public FlinkCDCJobDetailDto() {
        }

        public FlinkCDCJobDetailDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        private String srcDbType;
        private String srcHost;
        private Integer srcPort;
        private String srcUsername;
        private String srcPassword;
        private String srcDbName;

        /**
         * pgsql抽数时
         */
        private String srcSchema;

        private List<CDCTableConfig> srcTableConfigs;

        private Integer tableNum;

        /**
         * cdc目标数据源，目前用字符串，后续可以改成和HTool一直的枚举
         * StarRocks, Kafka, Iceberg
         */
        private String destType;
        private List<HostAndPort> destHostPorts;
        private String destUsername;
        private String destPassword;

        private WriteModeEnum.DiEnum destWriteMode;

        private String jobVersion;

        private Boolean published;

        public static final class CDCTableConfig {
            /**
             * 可以传正则表达式：table_*，
             * 或单表名：table_1，
             * 或分表：table_[0-9]
             */
            private String tablePattern;
            private Map<String, String> tableProp;

            public String getTablePattern() {
                return tablePattern;
            }

            public void setTablePattern(String tablePattern) {
                this.tablePattern = tablePattern;
            }

            public Map<String, String> getTableProp() {
                return tableProp;
            }

            public void setTableProp(Map<String, String> tableProp) {
                this.tableProp = tableProp;
            }
        }

        public String getSrcDbType() {
            return srcDbType;
        }

        public void setSrcDbType(String srcDbType) {
            this.srcDbType = srcDbType;
        }

        public String getSrcHost() {
            return srcHost;
        }

        public void setSrcHost(String srcHost) {
            this.srcHost = srcHost;
        }

        public Integer getSrcPort() {
            return srcPort;
        }

        public void setSrcPort(Integer srcPort) {
            this.srcPort = srcPort;
        }

        public String getSrcUsername() {
            return srcUsername;
        }

        public void setSrcUsername(String srcUsername) {
            this.srcUsername = srcUsername;
        }

        public String getSrcPassword() {
            return srcPassword;
        }

        public void setSrcPassword(String srcPassword) {
            this.srcPassword = srcPassword;
        }

        public String getSrcDbName() {
            return srcDbName;
        }

        public void setSrcDbName(String srcDbName) {
            this.srcDbName = srcDbName;
        }

        public List<CDCTableConfig> getSrcTableConfigs() {
            return srcTableConfigs;
        }

        public void setSrcTableConfigs(List<CDCTableConfig> srcTableConfigs) {
            this.srcTableConfigs = srcTableConfigs;
        }

        public String getDestType() {
            return destType;
        }

        public void setDestType(String destType) {
            this.destType = destType;
        }

        public List<HostAndPort> getDestHostPorts() {
            return destHostPorts;
        }

        public void setDestHostPorts(List<HostAndPort> destHostPorts) {
            this.destHostPorts = destHostPorts;
        }

        public String getDestUsername() {
            return destUsername;
        }

        public void setDestUsername(String destUsername) {
            this.destUsername = destUsername;
        }

        public String getDestPassword() {
            return destPassword;
        }

        public void setDestPassword(String destPassword) {
            this.destPassword = destPassword;
        }

        public String getJobVersion() {
            return jobVersion;
        }

        public void setJobVersion(String jobVersion) {
            this.jobVersion = jobVersion;
        }

        public String getSrcSchema() {
            return srcSchema;
        }

        public void setSrcSchema(String srcSchema) {
            this.srcSchema = srcSchema;
        }

        public WriteModeEnum.DiEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(WriteModeEnum.DiEnum destWriteMode) {
            this.destWriteMode = destWriteMode;
        }

        public Integer getTableNum() {
            return tableNum;
        }

        public void setTableNum(Integer tableNum) {
            this.tableNum = tableNum;
        }

        public Boolean getPublished() {
            return published;
        }

        public void setPublished(Boolean published) {
            this.published = published;
        }
    }

    public static class HostAndPort {
        private String host;
        private Integer port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public JobTypeEnum getJobTypeEnum() {
        return jobTypeEnum;
    }

    public void setJobTypeEnum(JobTypeEnum jobTypeEnum) {
        this.jobTypeEnum = jobTypeEnum;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public int getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(int executorCores) {
        this.executorCores = executorCores;
    }

    public String getExecQueue() {
        return execQueue;
    }

    public void setExecQueue(String execQueue) {
        this.execQueue = execQueue;
    }

    public EngineTypeEnum getExecEngine() {
        return execEngine;
    }

    public void setExecEngine(EngineTypeEnum execEngine) {
        this.execEngine = execEngine;
    }

    public String getExtProperties() {
        return extProperties;
    }

    public void setExtProperties(String extProperties) {
        this.extProperties = extProperties;
    }

    public Map<String, String> getConfProp() {
        return confProp;
    }

    public void setConfProp(Map<String, String> confProp) {
        this.confProp = confProp;
    }
}
