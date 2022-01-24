package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;

import java.util.List;

public interface JobUdfService {

    /**
     * 根据id删除udf
     * @param id
     * @return
     */
    Boolean delete(Long id) throws IllegalAccessException;

    /**
     * 新增
     * @param udf
     * @return
     */
    Long add(DevJobUdf udf);

    /**
     * 更新
     * @param udf
     * @return
     */
    Boolean update(DevJobUdf udf);

    /**
     * 根据id
     * @param id
     * @return
     */
    DevJobUdf findById(Long id);

    /**
     * 加载下来列表
     * @return
     */
    List<DevJobUdf> load();
}
