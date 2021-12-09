package cn.zhengcaiyun.idata.portal.controller;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.portal.model.response.UploadResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(path = "/p1/hdfs")

public class HdfsController {

    private static Logger logger = LoggerFactory.getLogger(HdfsController.class);

    @Autowired
    private HdfsService hdfsService;

    /**
     * 文件上传至HDFS
     * @param uploadFile /tmp/shiyin/udf/1638185598687_AD密码重置指导.docx
     * @return
     * @throws IOException
     */
    @ApiOperation("文件上传至HDFS-UDF路径下")
    @PostMapping("/upload/udf")
    public RestResult<UploadResponse> upload(@RequestParam("uploadFile") MultipartFile uploadFile) throws IOException {
        if (null == uploadFile) {
            throw new IllegalArgumentException("上传失败，无法找到文件！");
        }
        String hdfsFilePath = hdfsService.uploadFileToUDF(uploadFile.getInputStream(), uploadFile.getOriginalFilename());
        UploadResponse response = new UploadResponse();
        response.setRelativePath(hdfsFilePath);
        String name = uploadFile.getName();
        response.setFileName(name);
        return RestResult.success(response);
    }

}
