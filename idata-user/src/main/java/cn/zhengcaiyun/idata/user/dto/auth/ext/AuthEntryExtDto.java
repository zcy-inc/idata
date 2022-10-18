package cn.zhengcaiyun.idata.user.dto.auth.ext;

import cn.zhengcaiyun.idata.user.dto.auth.AuthEntryDto;

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
}
