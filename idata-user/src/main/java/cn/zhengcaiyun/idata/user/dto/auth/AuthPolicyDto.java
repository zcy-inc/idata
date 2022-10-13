package cn.zhengcaiyun.idata.user.dto.auth;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.user.constant.enums.AuthActionTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import org.springframework.beans.BeanUtils;

public class AuthPolicyDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 授权记录id
     */
    private Long authRecordId;

    /**
     * 授权作用：allow：允许，deny：拒绝
     */
    private AuthEffectTypeEnum effectType;

    /**
     * 授权操作：read：读，write：写
     */
    private AuthActionTypeEnum actionType;

    /**
     * 资源类型：tables：表
     */
    private AuthResourceTypeEnum resourceType;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthRecordId() {
        return authRecordId;
    }

    public void setAuthRecordId(Long authRecordId) {
        this.authRecordId = authRecordId;
    }

    public AuthEffectTypeEnum getEffectType() {
        return effectType;
    }

    public void setEffectType(AuthEffectTypeEnum effectType) {
        this.effectType = effectType;
    }

    public AuthActionTypeEnum getActionType() {
        return actionType;
    }

    public void setActionType(AuthActionTypeEnum actionType) {
        this.actionType = actionType;
    }

    public AuthResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(AuthResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static AuthPolicyDto from(AuthPolicy authPolicy) {
        AuthPolicyDto dto = new AuthPolicyDto();
        BeanUtils.copyProperties(authPolicy, dto);
        return dto;
    }

    public AuthPolicy toModel() {
        AuthPolicy authPolicy = new AuthPolicy();
        BeanUtils.copyProperties(this, authPolicy);
        return authPolicy;
    }
}
