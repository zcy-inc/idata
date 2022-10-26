package cn.zhengcaiyun.idata.user.dto.auth.ext;

import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dto.auth.AuthPolicyDto;
import cn.zhengcaiyun.idata.user.dto.auth.AuthResourceDto;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

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

    public static AuthPolicyExtDto from(AuthPolicy authPolicy) {
        AuthPolicyExtDto dto = new AuthPolicyExtDto();
        BeanUtils.copyProperties(authPolicy, dto);
        dto.setActionList(JSON.parseArray(authPolicy.getActions(), AuthActionEnum.class));
        dto.setEffect(AuthEffectEnum.valueOf(authPolicy.getEffect()));
        dto.setResourceType(AuthResourceTypeEnum.valueOf(authPolicy.getResourceType()));
        return dto;
    }
}
