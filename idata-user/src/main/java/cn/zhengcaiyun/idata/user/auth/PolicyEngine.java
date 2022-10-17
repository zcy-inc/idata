package cn.zhengcaiyun.idata.user.auth;

import cn.zhengcaiyun.idata.user.auth.entity.PolicyEntity;
import cn.zhengcaiyun.idata.user.auth.entity.ResourceEntity;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.service.auth.AuthEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PolicyEngine {
    private final AuthEntryService authEntryService;

    @Autowired
    public PolicyEngine(AuthEntryService authEntryService) {
        this.authEntryService = authEntryService;
    }

    public AuthEffectEnum verify(AuthReq authReq, List<PolicyEntity> policyList) {
        return verify(authReq, null, policyList);
    }

    public AuthEffectEnum verify(AuthReq authReq, AuthContext context, List<PolicyEntity> policyList) {
        if (CollectionUtils.isEmpty(policyList)) {
            return AuthEffectEnum.undecided;
        }
        Map<AuthEffectEnum, List<PolicyEntity>> effectPolicyListMap = policyList.stream().collect(Collectors.groupingBy(PolicyEntity::getEffect));
        List<PolicyEntity> denyPolicyList = effectPolicyListMap.get(AuthEffectEnum.deny);
        for (PolicyEntity policyEntity : denyPolicyList) {
            if (AuthEffectEnum.deny == verify(authReq, context, policyEntity)) {
                return AuthEffectEnum.deny;
            }
        }
        List<PolicyEntity> allowPolicyList = effectPolicyListMap.get(AuthEffectEnum.allow);
        for (PolicyEntity policyEntity : allowPolicyList) {
            if (AuthEffectEnum.allow == verify(authReq, context, policyEntity)) {
                return AuthEffectEnum.allow;
            }
        }
        return AuthEffectEnum.undecided;
    }

    private AuthEffectEnum verify(AuthReq authReq, AuthContext context, PolicyEntity policyEntity) {
        AuthEffectEnum authEffect = AuthEffectEnum.undecided;
        if (!policyEntity.getActions().contains(authReq.getActionType())) {
            return authEffect;
        }

        if (policyEntity.getResourceType() != authReq.getResourceType()) {
            return authEffect;
        }

        List<ResourceEntity> resources = policyEntity.getResources();
        for (ResourceEntity resourceEntity : resources) {
            if (resourceEntity.verifyResource(authReq.getResourceIdentifier())) {
                return policyEntity.getEffect();
            }
        }
        return authEffect;
    }
}
