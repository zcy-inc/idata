package cn.zhengcaiyun.idata.user.auth;

import cn.zhengcaiyun.idata.user.auth.entity.PolicyEntity;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.service.auth.AuthEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyEngine {
    private final AuthEntryService authEntryService;

    @Autowired
    public PolicyEngine(AuthEntryService authEntryService) {
        this.authEntryService = authEntryService;
    }

    public AuthEffectEnum verify(AuthReq authReq, AuthContext context, List<PolicyEntity> policyList) {
        return AuthEffectEnum.deny;
    }
}
