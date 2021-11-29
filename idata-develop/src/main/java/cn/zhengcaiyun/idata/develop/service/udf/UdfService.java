package cn.zhengcaiyun.idata.develop.service.udf;

import cn.zhengcaiyun.idata.develop.dal.model.DevJobUdf;

public interface UdfService {

    /**
     * 根据id删除udf
     * @param id
     * @return
     */
    Boolean delete(Long id);

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
}
