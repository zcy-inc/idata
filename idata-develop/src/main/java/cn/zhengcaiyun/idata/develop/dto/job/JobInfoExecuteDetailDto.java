package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

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
    private String execDriverMem;
    /**
     * worker节点内存
     */
    private String execWorkerMem;
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
    private String execEngine;

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
         *   数据来源-来源表
         */
        private String srcTables;//sourceTableName
        /**
         *   数据去向-目标表
         */
        private String destTable;//带库名 targetTableName
        /**
         *   数据去向-写入模式，init: 新建表，override: 覆盖表
         */
        private String destWriteMode; // isRecreate
        /**
         *   数据来源-切分键
         */
        private String srcReadShardKey;
        /**
         *   数据来源-分片数量（并行度）
         */
        private Integer srcShardingNum;
        /**
         *   TODO
         */
        private String mergeSql;
        /**
         *   数据来源-过滤条件
         */
        private String srcReadFilter; // diCondition
        /**
         * TODO
         */
        private String diQuery;
        /**
         * 数据来源-字段信息
         */
        private List<MappingColumnDto> srcCols; // diColumns

//        private String jdbcUrlPath;
//        private String username;
//        private String password;
//        private String sourceTableName;
//        private String targetTableName;//带库名
//        private Boolean isRecreate; //destWriteMode
//        private String diCondition; // srcReadFilter
//        private String diColumns; // srcCols
//        private String diQuery;
//        private String mergeSql;
//        private DiModeType diMode;
//        private String dbName;
//        private SourceDriverType driverType;

        // 字段
//        /**
//         * 数据去向-字段信息
//         */
//        @JsonIgnore
//        private List<MappingColumnDto> destCols;
//        /**
//         *   数据来源-数据源类型
//         */
//        @JsonIgnore
//        private String srcDataSourceType;
//        /**
//         *   数据来源-数据源id
//         */
//        @JsonIgnore
//        private Long srcDataSourceId;
//        /**
//         *   数据来源-读取模式，all：全量，incremental：增量
//         */
//        @JsonIgnore
//        private String srcReadMode;
//        /**
//         *   数据去向-数据源类型
//         */
//        @JsonIgnore
//        private String destDataSourceType;
//        /**
//         *   数据去向-数据源id
//         */
//        @JsonIgnore
//        private Long destDataSourceId;
//        /**
//         *   数据去向-写入前语句
//         */
//        @JsonIgnore
//        private String destBeforeWrite;
//        /**
//         *   数据去向-写入后语句
//         */
//        @JsonIgnore
//        private String destAfterWrite;

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

        public String getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(String destWriteMode) {
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
    }

    public static class SqlJobDetailsDto extends JobInfoExecuteDetailDto {
        public SqlJobDetailsDto() {

        }
        public SqlJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        /**
         *   数据去向-数据源类型
         */
        private String destDataSourceType;

        /**
         *   数据去向-目标表
         */
        private String destTable; // targetTableName

        /**
         *   数据去向-写入模式，overwrite，upsert
         */
        private String destWriteMode; // saveMode

        /**
         *   数据来源表主键(写入模式为upsert时必填)
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
        private List<DevJobUdf> udfList; // udfs

        public String getDestDataSourceType() {
            return destDataSourceType;
        }

        public void setDestDataSourceType(String destDataSourceType) {
            this.destDataSourceType = destDataSourceType;
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

        public List<DevJobUdf> getUdfList() {
            return udfList;
        }

        public void setUdfList(List<DevJobUdf> udfList) {
            this.udfList = udfList;
        }

        public String getSourceSql() {
            return sourceSql;
        }

        public void setSourceSql(String sourceSql) {
            this.sourceSql = sourceSql;
        }
    }

    public static class SparkJobDetailsDto extends JobInfoExecuteDetailDto {
        public SparkJobDetailsDto() {

        }
        public SparkJobDetailsDto(JobInfoExecuteDetailDto parent) {
            BeanUtils.copyProperties(parent, this);
        }

        private String resourceHdfsPath; // resourceHdfsPath
        private List<JobArgumentDto> appArguments; // appArguments
        private String mainClass; //mainClass

        private List<String> dependResHdfsPaths; // 目前缺少

        public String getResourceHdfsPath() {
            return resourceHdfsPath;
        }

        public void setResourceHdfsPath(String resourceHdfsPath) {
            this.resourceHdfsPath = resourceHdfsPath;
        }

        public List<JobArgumentDto> getAppArguments() {
            return appArguments;
        }

        public void setAppArguments(List<JobArgumentDto> appArguments) {
            this.appArguments = appArguments;
        }

        public String getMainClass() {
            return mainClass;
        }

        public void setMainClass(String mainClass) {
            this.mainClass = mainClass;
        }
    }

    public static class KylinDetailJob extends JobInfoExecuteDetailDto {
        public KylinDetailJob() {

        }
        public KylinDetailJob(JobInfoExecuteDetailDto parent) {
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
         * 执行参数
         */
        private List<JobArgumentDto> scriptArguments; // scriptArguments
        /**
         * JobTypeEnum.language
         */
        private String scriptLanguage;

        public String getSourceResource() {
            return sourceResource;
        }

        public void setSourceResource(String sourceResource) {
            this.sourceResource = sourceResource;
        }

        public List<JobArgumentDto> getScriptArguments() {
            return scriptArguments;
        }

        public void setScriptArguments(List<JobArgumentDto> scriptArguments) {
            this.scriptArguments = scriptArguments;
        }

        public String getScriptLanguage() {
            return scriptLanguage;
        }

        public void setScriptLanguage(String scriptLanguage) {
            this.scriptLanguage = scriptLanguage;
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

    public String getExecDriverMem() {
        return execDriverMem;
    }

    public void setExecDriverMem(String execDriverMem) {
        this.execDriverMem = execDriverMem;
    }

    public String getExecWorkerMem() {
        return execWorkerMem;
    }

    public void setExecWorkerMem(String execWorkerMem) {
        this.execWorkerMem = execWorkerMem;
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

    public String getExecEngine() {
        return execEngine;
    }

    public void setExecEngine(String execEngine) {
        this.execEngine = execEngine;
    }
}
