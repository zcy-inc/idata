package cn.zhengcaiyun.idata.portal.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("文件上传返回体")
public class UploadResponse {

    @ApiModelProperty("相对路径")
    private String relativePath;

    @ApiModelProperty("文件名称")
    private String fileName;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
