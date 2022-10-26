package cn.zhengcaiyun.idata.portal.model.request.map;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zhanqian
 * @date 2022/9/29 1:57 PM
 * @description
 */
public class UserFavoriteRequest {

    /**
     *   实体记录唯一标识：数仓表（table）时传id，数据指标（indicator）时传code
     */
    @NotEmpty
    private String entityCode;

    /**
     *   实体记录数据源：数仓表（table） or 数据指标（indicator）
     */
    @NotEmpty
    private String entitySource;

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntitySource() {
        return entitySource;
    }

    public void setEntitySource(String entitySource) {
        this.entitySource = entitySource;
    }
}
