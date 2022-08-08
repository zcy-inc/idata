package cn.zhengcaiyun.idata.develop.dto.job;


import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import org.springframework.beans.BeanUtils;

/**
 * @author zhanqian
 * @date 2022/4/1 3:50 PM
 * @description
 */
public class JobUdfDto {

    private Long id;

    /**
     *   '函数名称'
     */
    private String udfName;

    /**
     *   函数类型
     */
    private String udfType;

    /**
     *   文件名称
     */
    private String fileName;

    /**
     *   hdfs文件路径
     */
    private String hdfsPath;

    /**
     *   返回类型
     */
    private String returnType;

    /**
     *   返回值
     */
    private String returnSample;

    /**
     *   命令格式
     */
    private String commandFormat;

    /**
     *   java类名称（JavaFunction、JavaUDAF）或module（PythonFunction）|沐泽|2019-12-19
     */
    private String sourceName;

    /**
     *   是否是全局类型函数，1：是，0：否
     */
    private Boolean globalFun;

    public static JobUdfDto fromModel(DevJobUdf devJobUdf) {
        JobUdfDto dto = new JobUdfDto();
        BeanUtils.copyProperties(devJobUdf, dto);
        if (devJobUdf.getGlobalFun() == 0) {
            dto.setGlobalFun(false);
        } else {
            dto.setGlobalFun(true);
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCommandFormat() {
        return commandFormat;
    }

    public void setCommandFormat(String commandFormat) {
        this.commandFormat = commandFormat;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Boolean getGlobalFun() {
        return globalFun;
    }

    public void setGlobalFun(Boolean globalFun) {
        this.globalFun = globalFun;
    }
}
