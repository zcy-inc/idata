package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.hutool.core.io.FileUtil;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.develop.service.job.JobUdfService;
import cn.zhengcaiyun.idata.portal.model.request.udf.UdfAddRequest;
import cn.zhengcaiyun.idata.portal.model.request.udf.UdfUpdateRequest;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.mortbay.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Api("")
@RestController
@RequestMapping(path = "/p1/dev")
public class JobUdfController {

    private static Logger logger = LoggerFactory.getLogger(JobUdfController.class);

    @Autowired
    private HdfsService hdfsService;
    @Autowired
    private JobUdfService udfService;
    @Autowired
    private DevJobUdfDao devJobUdfDao;
    @Autowired
    private DevAccessService devAccessService;

    @ApiOperation("加载某个UDF详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "udf id", dataType = "Long", required = true)
    })
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
    public RestResult<DevJobUdf> add(@RequestBody @Valid UdfAddRequest udfAddRequest) throws IllegalAccessException {
        DevJobUdf udf = new DevJobUdf();
        BeanUtils.copyProperties(udfAddRequest, udf);
        devAccessService.checkAddAccess(OperatorContext.getCurrentOperator().getId(), udf.getFolderId());

        udf.setCreator(OperatorContext.getCurrentOperator().getNickname());
        udf.setDel(DeleteEnum.DEL_NO.val);
        udf.setCreateTime(new Date());
        String hdfsPath = udfAddRequest.getHdfsPath();
        udf.setHdfsPath(hdfsService.getHdfsPrefix() + hdfsPath);
        Long id = udfService.add(udf);
        return RestResult.success(udfService.findById(id));
    }

    @ApiOperation("更新UDF")
    @PutMapping("/udf")
    public RestResult<DevJobUdf> update(@RequestBody UdfUpdateRequest udfUpdateRequest) throws IllegalAccessException {
        Long id = udfUpdateRequest.getId();
        DevJobUdf udf = devJobUdfDao.selectByPrimaryKey(id).orElseThrow(() -> new IllegalArgumentException("数据不存在"));
        Long originFolderId = udf.getFolderId();

        BeanUtils.copyProperties(udfUpdateRequest, udf);
        devAccessService.checkUpdateAccess(OperatorContext.getCurrentOperator().getId(), originFolderId, udf.getFolderId());

        udf.setEditor(OperatorContext.getCurrentOperator().getNickname());
        udf.setEditTime(new Date());
        udf.setDel(DeleteEnum.DEL_NO.val);
        String hdfsPath = udfUpdateRequest.getHdfsPath();
        udf.setHdfsPath(hdfsService.getHdfsPrefix() + hdfsPath);
        udfService.update(udf);
        return RestResult.success(udfService.findById(id));
    }

    @ApiOperation("删除UDF")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "udf id", dataType = "Long", required = true)
    })
    @DeleteMapping("/udf/{id}")
    public RestResult<Boolean> deleteFolder(@PathVariable("id") Long id) throws IllegalAccessException {
        return RestResult.success(udfService.delete(id));
    }


    /**
     * 文件下载
     * @param  hdfs://nameservice1/tmp/shiyin/udf/1638185598687_AD密码重置指导.docx
     * @return
     * @throws IOException
     */
    @ApiOperation("文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "udf id", dataType = "Long", required = true)
    })
    @GetMapping("/udf/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") Long id) throws IOException {
        DevJobUdf udf = udfService.findById(id);
        String path = udf.getHdfsPath();
        if (!hdfsService.checkUdfPath(path)) {
            throw new GeneralException("不可访问其他路径！" + path + "不合法");
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        ByteArrayOutputStream sos = new ByteArrayOutputStream();
        hdfsService.readFile(path, sos);
        byte[] bytes = sos.toByteArray();
        sos.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; fileName=\"%s\"", URLEncoder.encode(fileName, "UTF-8")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Language", "UTF-8");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
