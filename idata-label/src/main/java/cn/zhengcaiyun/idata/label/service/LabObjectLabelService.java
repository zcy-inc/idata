package cn.zhengcaiyun.idata.label.service;

import cn.zhengcaiyun.idata.label.dto.LabObjectLabelDto;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;

/**
 * @description: 对象标签service
 * @author: yangjianhua
 * @create: 2021-06-23 09:34
 **/
public interface LabObjectLabelService {
    /**
     * 创建标签
     *
     * @param labelDto
     * @param operator
     * @return
     */
    Long createLabel(LabObjectLabelDto labelDto, String operator);

    /**
     * 编辑标签
     *
     * @param labelDto
     * @param operator
     * @return
     */
    Long editLabel(LabObjectLabelDto labelDto, String operator);

    /**
     * 获取标签
     *
     * @param id
     * @return
     */
    LabObjectLabelDto getLabel(Long id);

    /**
     * 删除标签
     *
     * @param id
     * @param operator
     * @return
     */
    Boolean deleteLabel(Long id, String operator);

    /**
     * 查询标签计算数据
     *
     * @param id
     * @param layerId
     * @return
     */
    LabelQueryDataDto queryLabelResultData(Long id, Long layerId, Integer limit, Integer offset);
}
