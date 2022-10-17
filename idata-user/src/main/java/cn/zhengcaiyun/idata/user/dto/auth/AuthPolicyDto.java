package cn.zhengcaiyun.idata.user.dto.auth;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.List;

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
    private AuthEffectEnum effect;

    /**
     * 授权操作：read：读，write：写
     */
    private List<AuthActionEnum> actionList;

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

    public AuthEffectEnum getEffect() {
        return effect;
    }

    public void setEffect(AuthEffectEnum effect) {
        this.effect = effect;
    }

    public List<AuthActionEnum> getActionList() {
        return actionList;
    }

    public void setActionList(List<AuthActionEnum> actionList) {
        this.actionList = actionList;
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
        dto.setActionList(JSON.parseArray(authPolicy.getActions(), AuthActionEnum.class));
        return dto;
    }

    public AuthPolicy toModel() {
        AuthPolicy authPolicy = new AuthPolicy();
        BeanUtils.copyProperties(this, authPolicy);
        authPolicy.setActions(JSON.toJSONString(this.actionList));
        return authPolicy;
    }
}
