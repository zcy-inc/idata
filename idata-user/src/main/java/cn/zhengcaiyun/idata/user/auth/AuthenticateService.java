package cn.zhengcaiyun.idata.user.auth;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.user.auth.entity.PolicyEntity;
import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthEntryRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthPolicyRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthResourceRepo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuthenticateService {

    private final AuthEntryRepo authEntryRepo;
    private final AuthPolicyRepo authPolicyRepo;
    private final AuthResourceRepo authResourceRepo;
    private final PolicyEngine policyEngine;
    private final GroupUserRelationRepo groupUserRelationRepo;

    @Autowired
    public AuthenticateService(AuthEntryRepo authEntryRepo,
                               AuthPolicyRepo authPolicyRepo,
                               AuthResourceRepo authResourceRepo,
                               PolicyEngine policyEngine,
                               GroupUserRelationRepo groupUserRelationRepo) {
        this.authEntryRepo = authEntryRepo;
        this.authPolicyRepo = authPolicyRepo;
        this.authResourceRepo = authResourceRepo;
        this.policyEngine = policyEngine;
        this.groupUserRelationRepo = groupUserRelationRepo;
    }

    public AuthEffectEnum authenticate(Operator subjectOperator, AuthResourceTypeEnum resourceType, List<String> resourceIdentifiers,
                                       AuthActionEnum actionType) {
        AuthEffectEnum authResult = verify(AuthSubjectTypeEnum.users, subjectOperator.getId().toString(),
                resourceType, resourceIdentifiers, actionType);
        if (AuthEffectEnum.undecided != authResult) {
            return authResult;
        }

        List<Long> groupIdList = groupUserRelationRepo.queryByUser(subjectOperator.getId())
                .stream()
                .map(GroupUserRelation::getGroupId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIdList)) {
            for (Long groupId : groupIdList) {
                authResult = verify(AuthSubjectTypeEnum.groups, groupId.toString(),
                        resourceType, resourceIdentifiers, actionType);
                if (AuthEffectEnum.undecided != authResult) {
                    return authResult;
                }
            }
        }
        return AuthEffectEnum.deny;
    }

    private AuthEffectEnum verify(AuthSubjectTypeEnum subjectType, String subjectIdentifier,
                                  AuthResourceTypeEnum resourceType, List<String> resourceIdentifiers,
                                  AuthActionEnum actionType) {
        Optional<AuthEntry> authEntryOptional = authEntryRepo.query(subjectType.name(), subjectIdentifier);
        AuthEffectEnum authResult = AuthEffectEnum.undecided;
        if (authEntryOptional.isEmpty()) {
            return authResult;
        }

        AuthEntry authEntry = authEntryOptional.get();
        List<PolicyEntity> policyEntityList = getPolicyEntity(authEntry.getId());
        if (CollectionUtils.isEmpty(policyEntityList)) {
            return authResult;
        }

        AuthReq authReq = AuthReq.AuthReqBuilder.newAuthReq()
                .setSubject(AuthSubjectTypeEnum.users, subjectIdentifier)
                .setResources(resourceType, resourceIdentifiers)
                .setAction(actionType)
                .build();
        return policyEngine.verify(authReq, new AuthContext(), policyEntityList);
    }

    public List<PolicyEntity> getPolicyEntity(Long authId) {
        List<AuthPolicy> authPolicies = authPolicyRepo.queryListByAuthId(authId);
        List<AuthResource> authResources = authResourceRepo.queryListByAuthId(authId);
        Map<Long, List<String>> policyResourceMap = authResources.stream()
                .collect(Collectors.groupingBy(AuthResource::getPolicyRecordId, Collectors.mapping(AuthResource::getResources, Collectors.toList())));
        return authPolicies.stream().map(authPolicy -> {
            PolicyEntity policyEntity = new PolicyEntity();
            policyEntity.setEffect(AuthEffectEnum.valueOf(authPolicy.getEffect()));
            policyEntity.setActions(Sets.newHashSet(JSON.parseArray(authPolicy.getActions(), AuthActionEnum.class)));
            policyEntity.setResourceType(AuthResourceTypeEnum.valueOf(authPolicy.getResourceType()));
            policyEntity.setResources(policyResourceMap.get(authPolicy.getId()));
            return policyEntity;
        }).collect(Collectors.toList());
    }

}
