package cn.zhengcaiyun.idata.connector.spi.resouce.manage;

import cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.AppResourceDetail;

import java.util.List;

public interface ResourceManageService {

    /**
     *
     * @param finishTimeBegin 开始的任务结束时间（ms）
     * @param finishTimeEnd 结束的任务结束时间（ms）
     * @return
     */
    List<AppResourceDetail> loadAppResourceDetail(Long finishTimeBegin, Long finishTimeEnd);

}
