package cn.zhengcaiyun.idata.user.dto.auth.ext;

import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import cn.zhengcaiyun.idata.user.dto.auth.AuthEntryDto;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 授权记录
 */
public class AuthEntryExtDto extends AuthEntryDto {
    /**
     * 授权策略
     */
    private List<AuthPolicyExtDto> authPolicyList;

    public List<AuthPolicyExtDto> getAuthPolicyList() {
        return authPolicyList;
    }

    public void setAuthPolicyList(List<AuthPolicyExtDto> authPolicyList) {
        this.authPolicyList = authPolicyList;
    }

    public static AuthEntryExtDto from(AuthEntry authEntry) {
        AuthEntryExtDto dto = new AuthEntryExtDto();
        BeanUtils.copyProperties(authEntry, dto);
        dto.setSubjectType(AuthSubjectTypeEnum.valueOf(authEntry.getSubjectType()));
        return dto;
    }
}
