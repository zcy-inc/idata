package cn.zhengcaiyun.idata.portal.model.request.udf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("udf新增请求")
public class UdfAddRequest {
    /**
     * Database Column Remarks:
     *   '函数名称'
     */
    @ApiModelProperty("函数名称")
    @NotEmpty(message = "udfName 不能为空")
    private String udfName;

    /**
     * Database Column Remarks:
     *   函数类型
     */
    @ApiModelProperty("函数类型")
    @NotEmpty(message = "udfType 不能为空")
    private String udfType;

    /**
     * Database Column Remarks:
     *   文件名称
     */
    @ApiModelProperty("文件名称")
    @NotEmpty(message = "fileName 不能为空")
    private String fileName;

    /**
     * 自动推荐文件名称 当为true时候，fileName无效
     */
    private boolean autoRecommendFileName = true;

    /**
     * Database Column Remarks:
     *   hdfs文件路径
     */
    @ApiModelProperty("hdfs文件路径")
    @NotEmpty(message = "hdfsPath 不能为空")
    private String hdfsPath;

    /**
     * Database Column Remarks:
     *   返回类型
     */
    @ApiModelProperty("返回类型")
    @NotEmpty(message = "returnType 不能为空")
    private String returnType;

    /**
     * Database Column Remarks:
     *   返回值
     */
    @ApiModelProperty("返回值")
    private String returnSample;

    /**
     * Database Column Remarks:
     *   目标文件夹
     */
    @ApiModelProperty("目标文件夹")
    @NotNull(message = "folderId 不能为空")
    private Long folderId;

    /**
     * Database Column Remarks:
     *   描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * Database Column Remarks:
     *   命令格式
     */
    @ApiModelProperty("命令格式")
    private String commandFormat;

    /**
     * Database Column Remarks:
     *   示例
     */
    @ApiModelProperty("示例")
    private String udfSample;

    /**
     * java类名称（JavaFunction、JavaUDAF）或module（PythonFunction）
     */
    private String sourceName;

    /**
     *   是否是全局类型函数，1：是，0：否
     */
    private Integer globalFun = 1;

    public String getUdfName() {
        return udfName;
    }

    public void setUdfName(String udfName) {
        this.udfName = udfName;
    }

    public String getUdfType() {
        return udfType;
    }

    public void setUdfType(String udfType) {
        this.udfType = udfType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnSample() {
        return returnSample;
    }

    public void setReturnSample(String returnSample) {
        this.returnSample = returnSample;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommandFormat() {
        return commandFormat;
    }

    public void setCommandFormat(String commandFormat) {
        this.commandFormat = commandFormat;
    }

    public String getUdfSample() {
        return udfSample;
    }

    public void setUdfSample(String udfSample) {
        this.udfSample = udfSample;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getGlobalFun() {
        return globalFun;
    }

    public void setGlobalFun(Integer globalFun) {
        this.globalFun = globalFun;
    }

    public boolean isAutoRecommendFileName() {
        return autoRecommendFileName;
    }

    public void setAutoRecommendFileName(boolean autoRecommendFileName) {
        this.autoRecommendFileName = autoRecommendFileName;
    }
}
