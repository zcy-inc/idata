package cn.zhengcaiyun.idata.connector.spi.resouce.manage;

import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.AppResourceDetail;
import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.ClusterMetricsResponse;

import java.util.List;

public interface ResourceManageService {

    /**
     *
     * @param finishTimeBegin 开始的任务结束时间（ms）
     * @param finishTimeEnd 结束的任务结束时间（ms）
     * @return
     */
    List<AppResourceDetail> loadAppResourceDetailList(Long finishTimeBegin, Long finishTimeEnd);

    /**
     * 查看资源管理器集群情况
     * @return
     */
    ClusterMetricsResponse clusterMetrics();
}
