package cn.zhengcaiyun.idata.user.dto.auth.ext;

import cn.zhengcaiyun.idata.user.dto.auth.AuthPolicyDto;
import cn.zhengcaiyun.idata.user.dto.auth.AuthResourceDto;

import java.util.List;

/**
 * 授权策略（规则）
 */
public class AuthPolicyExtDto extends AuthPolicyDto {
    /**
     * 授权资源
     */
    private List<AuthResourceDto> authResourceList;

    public List<AuthResourceDto> getAuthResourceList() {
        return authResourceList;
    }

    public void setAuthResourceList(List<AuthResourceDto> authResourceList) {
        this.authResourceList = authResourceList;
    }
}
