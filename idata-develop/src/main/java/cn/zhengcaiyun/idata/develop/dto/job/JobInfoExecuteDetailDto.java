package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.EngineTypeEnum;
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
         *   数据去向-写入模式，init: 新建表，overwrite: 覆盖表，append：追加表
         * @see cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum
         */
        private DestWriteModeEnum destWriteMode; // isRecreate
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

        public DestWriteModeEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(DestWriteModeEnum destWriteMode) {
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
         *   数据去向-目标表
         */
        private String destTable; // targetTableName

        /**
         *   数据去向-写入模式，overwrite，upsert
         */
        private DestWriteModeEnum destWriteMode; // saveMode

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

        public String getDestTable() {
            return destTable;
        }

        public void setDestTable(String destTable) {
            this.destTable = destTable;
        }

        public DestWriteModeEnum getDestWriteMode() {
            return destWriteMode;
        }

        public void setDestWriteMode(DestWriteModeEnum destWriteMode) {
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
}
