package cn.zhengcaiyun.idata.portal.controller.dev.job;


import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.EngineTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.portal.model.response.NameValueResponse;
import cn.zhengcaiyun.idata.portal.model.response.job.DIJobDatasourceResponse;
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
@RequestMapping(path = "/p1/dev/jobs")
public class MetadataController {

    /**
     * 作业类型
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum DI_BATCH/DI_STREAM/BACK_FLOW
     * @return
     */
    @GetMapping("/di/meta/job-type")
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
    @GetMapping("/di/meta/sync-mode")
    public RestResult<List<NameValueResponse<String>>> loadSyncMode(@RequestParam("jobType") String jobType) {
        // 此处硬编码，原始数据是一个字段存两种信息，目前无法扩展，后续需要梳理枚举整合进去，目前无法融入到JobTypeEnum
        List<NameValueResponse<String>> list = new ArrayList<>();
        list.add(new NameValueResponse<>("离线", "BATCH"));
        if (StringUtils.equals(jobType, "DI")) {
            list.add(new NameValueResponse<>("实时", "STREAM"));
        }
        return RestResult.success(list);
    }

    /**
     * 加载执行引擎
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum DI_BATCH/DI_STREAM/BACK_FLOW
     * @return
     */
    @GetMapping("/engine-type")
    public RestResult<List<String>> loadEngineType(@RequestParam("jobType") String jobType) {
        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobType).get();
        switch (jobTypeEnum) {
            case DI_BATCH:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.SQOOP.name(), EngineTypeEnum.SPARK.name(), EngineTypeEnum.SPARK3.name()}));
            case DI_STREAM:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.FLINK.name()}));
            case BACK_FLOW:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.SQOOP.name(), EngineTypeEnum.SPARK.name(), EngineTypeEnum.SPARK3.name(), EngineTypeEnum.STARROCKS.name()}));
            case KYLIN:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.KYLIN.name()}));
            case SQL_FLINK:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.FLINK.name()}));
            case SQL_SPARK:
            case SPARK_PYTHON:
            case SPARK_JAR:
                return RestResult.success(Arrays.asList(new String[]{EngineTypeEnum.SPARK.name(), EngineTypeEnum.SPARK3.name()}));
        }
        return RestResult.success(new ArrayList<>());
    }

    /**
     * 加载数据源
     * @see cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum DI_BATCH/DI_STREAM/BACK_FLOW
     * @return
     */
    @GetMapping("/meta/datasource-type")
    public RestResult<DIJobDatasourceResponse> loadDIDatasourceType(@RequestParam("jobType") String jobType) {
        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobType).get();
        DIJobDatasourceResponse response = new DIJobDatasourceResponse();
        switch (jobTypeEnum) {
            case DI_STREAM:
                response.setFromList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name()}));
                response.setDestList(Arrays.asList(new String[]{DataSourceTypeEnum.starrocks.name(), DataSourceTypeEnum.kafka.name()}));
                break;
            case DI_BATCH:
                response.setFromList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name(), DataSourceTypeEnum.postgresql.name()}));
                response.setDestList(Arrays.asList(new String[]{DataSourceTypeEnum.hive.name()}));
                break;
            case BACK_FLOW:
                response.setFromList(Arrays.asList(new String[]{DataSourceTypeEnum.hive.name()}));
                response.setDestList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name(), DataSourceTypeEnum.postgresql.name(),
                        DataSourceTypeEnum.elasticsearch.name(), DataSourceTypeEnum.starrocks.name(), DataSourceTypeEnum.kafka.name()}));
                break;
            case SQL_SPARK:
                response.setExternalList(Arrays.asList(new String[]{DataSourceTypeEnum.starrocks.name()}));
                response.setDestList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name(), DataSourceTypeEnum.postgresql.name(), DataSourceTypeEnum.hive.name(), DataSourceTypeEnum.presto.name(),
                        DataSourceTypeEnum.kylin.name(), DataSourceTypeEnum.phoenix.name(), DataSourceTypeEnum.elasticsearch.name(), DataSourceTypeEnum.mssql.name(), DataSourceTypeEnum.kafka.name(),
                        DataSourceTypeEnum.starrocks.name(), DataSourceTypeEnum.csv.name(), }));
                break;
            case SQL_FLINK:
                response.setFromList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name(), DataSourceTypeEnum.postgresql.name(), DataSourceTypeEnum.kafka.name(), DataSourceTypeEnum.starrocks.name()}));
                response.setDestList(Arrays.asList(new String[]{DataSourceTypeEnum.mysql.name(), DataSourceTypeEnum.postgresql.name(), DataSourceTypeEnum.kafka.name(), DataSourceTypeEnum.starrocks.name()}));
                break;
            case SPARK_PYTHON:
            case SPARK_JAR:
            case SCRIPT_PYTHON:
            case SCRIPT_SHELL:
            case KYLIN:
                response.setExternalList(new ArrayList<>());
                response.setFromList(Arrays.asList(new String[]{DataSourceTypeEnum.hive.name()}));
                response.setDestList(DataSourceTypeEnum.getAllNames());
                break;
        }
        return RestResult.success(response);
    }

}
