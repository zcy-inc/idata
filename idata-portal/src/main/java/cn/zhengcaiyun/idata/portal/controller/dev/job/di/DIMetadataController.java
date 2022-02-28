package cn.zhengcaiyun.idata.portal.controller.dev.job.di;


import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.portal.model.response.NameValueResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DI元数据
 *
 * @description:
 * @author: zhanqian
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/di/meta")
public class DIMetadataController {


    /**
     * 作业类型
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum DI_BATCH/DI_STREAM/BACK_FLOW
     * @return
     */
    @GetMapping("/job-type")
    public RestResult<List<NameValueResponse<String>>> loadJobType() {
        //此处硬编码，原始数据是一个字段存两种信息，目前无法扩展，后续需要梳理枚举整合进去，目前无法融入到 JobTypeEnum
        List<NameValueResponse<String>> list = new ArrayList<>();
        list.add(new NameValueResponse<>("数据回流", "BACK_FLOW"));
        list.add(new NameValueResponse<>("数据抽取", "DI"));
        return RestResult.success(list);
    }

    /**
     * 同步方式
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum DI_BATCH/DI_STREAM/BACK_FLOW
     * @return
     */
    @GetMapping("/sync-mode")
    public RestResult<List<NameValueResponse<String>>> loadSyncMode(@RequestParam("jobType") String jobType) {
        // 此处硬编码，原始数据是一个字段存两种信息，目前无法扩展，后续需要梳理枚举整合进去，目前无法融入到JobTypeEnum
        List<NameValueResponse<String>> list = new ArrayList<>();
        list.add(new NameValueResponse<>("离线", "BATCH"));
        if (StringUtils.equals(jobType, "DI")) {
            list.add(new NameValueResponse<>("实时", "STREAM"));
        }
        return RestResult.success(list);
    }

}
