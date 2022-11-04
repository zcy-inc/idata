package cn.zhengcaiyun.idata.user.dto.auth;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import org.springframework.beans.BeanUtils;

public class AuthEntryDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 授权主体唯一标识
     */
    private String subjectId;

    /**
     * 授权主体类型，users：用户，groups：用户组，apps：应用
     */
    private AuthSubjectTypeEnum subjectType;

    /**
     * 备注
     */
    private String remark = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public AuthSubjectTypeEnum getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(AuthSubjectTypeEnum subjectType) {
        this.subjectType = subjectType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static AuthEntryDto from(AuthEntry authEntry) {
        AuthEntryDto dto = new AuthEntryDto();
        BeanUtils.copyProperties(authEntry, dto);
        dto.setSubjectType(AuthSubjectTypeEnum.valueOf(authEntry.getSubjectType()));
        return dto;
    }

    public AuthEntry toModel() {
        AuthEntry authEntry = new AuthEntry();
        BeanUtils.copyProperties(this, authEntry);
        authEntry.setSubjectType(this.subjectType.name());
        return authEntry;
    }
}
