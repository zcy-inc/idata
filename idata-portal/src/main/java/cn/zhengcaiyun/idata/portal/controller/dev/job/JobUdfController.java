package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.service.job.JobUdfService;
import cn.zhengcaiyun.idata.portal.model.request.udf.UdfAddRequest;
import cn.zhengcaiyun.idata.portal.model.request.udf.UdfUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Api("")
@RestController
@RequestMapping(path = "/p1/dev")
public class JobUdfController {

    private static Logger logger = LoggerFactory.getLogger(JobUdfController.class);

    @Autowired
    private HdfsService hdfsService;
    @Autowired
    private JobUdfService udfService;

    @ApiOperation("加载某个UDF详情")
    @GetMapping("udf/{id}")
    public RestResult<DevJobUdf> findById(@PathVariable("id") Long id) {
        return RestResult.success(udfService.findById(id));
    }

    @ApiOperation("加载UDF列表")
    @GetMapping("udfs")
    public RestResult<List<DevJobUdf>> load() {
        return RestResult.success(udfService.load());
    }

    @ApiOperation("新增UDF")
    @PostMapping("/udf")
    public RestResult<Long> add(@RequestBody @Valid UdfAddRequest udfAddRequest) {
        DevJobUdf udf = new DevJobUdf();
        BeanUtils.copyProperties(udfAddRequest, udf);
        udf.setCreator(OperatorContext.getCurrentOperator().getNickname());
        udf.setDel(DeleteEnum.DEL_NO.val);
        udf.setCreateTime(new Date());
        String hdfsPath = udfAddRequest.getHdfsPath();
        udf.setHdfsPath(hdfsService.getHdfsPrefix() + hdfsPath);
        return RestResult.success(udfService.add(udf));
    }

    @ApiOperation("更新UDF")
    @PutMapping("/udf")
    public RestResult<Boolean> update(@RequestBody UdfUpdateRequest udfUpdateRequest) {
        DevJobUdf udf = new DevJobUdf();
        BeanUtils.copyProperties(udfUpdateRequest, udf);
        udf.setEditor(OperatorContext.getCurrentOperator().getNickname());
        udf.setEditTime(new Date());
        udf.setDel(DeleteEnum.DEL_NO.val);
        return RestResult.success(udfService.update(udf));
    }

    @ApiOperation("删除UDF")
    @DeleteMapping("/udf/{id}")
    public RestResult<Boolean> deleteFolder(@PathVariable("id") Long id) {
        return RestResult.success(udfService.delete(id));
    }


    /**
     * 文件下载
     * @param  hdfs://nameservice1/tmp/shiyin/udf/1638185598687_AD密码重置指导.docx
     * @return
     * @throws IOException
     */
    @ApiOperation("文件下载")
    @GetMapping("/udf/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") Long id) throws IOException {
        DevJobUdf udf = udfService.findById(id);
        String path = udf.getHdfsPath();
        if (!hdfsService.checkUdfPath(path)) {
            throw new GeneralException("不可访问其他路径！" + path + "不合法");
        }
        String filename = udf.getFileName();
        FSDataInputStream inputStream = hdfsService.open(path);
        return downloadFile(inputStream, filename);
    }

    /**
     * 浏览器下载
     * @param in
     * @param fileName
     * @return
     */
    public ResponseEntity<InputStreamResource> downloadFile(InputStream in, String fileName) {
        try {
            byte[] testBytes = new byte[in.available()];
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Language", "UTF-8");
            //最终这句，让文件内容以流的形式输出
            return ResponseEntity.ok().headers(headers).contentLength(testBytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(in));
        } catch (IOException e) {
            logger.info("downfile is error" + e.getMessage());
        }
        logger.info("file is null" + fileName);
        return null;
    }
}
