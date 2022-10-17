package cn.zhengcaiyun.idata.user.auth;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.user.auth.entity.PolicyEntity;
import cn.zhengcaiyun.idata.user.auth.entity.ResourceEntity;
import cn.zhengcaiyun.idata.user.auth.entity.TableResourceEntity;
import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthEntryRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthPolicyRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthResourceRepo;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${user.auth.switch:false}")
    private Boolean authSwitch;

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

    public AuthRet authenticate(Operator subjectOperator, AuthResourceTypeEnum resourceType, List<String> resourceIdentifiers,
                                AuthActionEnum actionType) {
        if (BooleanUtils.isNotTrue(authSwitch)) {
            return AuthRet.allow();
        }

        List<String> denyResources = Lists.newArrayList();
        List<PolicyEntity> userPolicyList = null;
        List<PolicyEntity> groupPolicyList = null;
        for (String resourceIdentifier : resourceIdentifiers) {
            if (userPolicyList == null) {
                userPolicyList = getPolicyEntity(AuthSubjectTypeEnum.users, subjectOperator.getId().toString());
            }
            AuthEffectEnum authResult = verify(AuthSubjectTypeEnum.users, subjectOperator.getId().toString(),
                    resourceType, resourceIdentifier, actionType, userPolicyList);
            if (AuthEffectEnum.allow == authResult) {
                continue;
            } else if (AuthEffectEnum.deny == authResult) {
                denyResources.add(resourceIdentifier);
                continue;
            }

            if (groupPolicyList == null) {
                List<String> groupIdList = groupUserRelationRepo.queryByUser(subjectOperator.getId())
                        .stream()
                        .map(groupUserRelation -> groupUserRelation.getGroupId().toString())
                        .collect(Collectors.toList());
                groupPolicyList = getPolicyEntity(AuthSubjectTypeEnum.groups, groupIdList.toArray(new String[0]));
            }
            authResult = verify(AuthSubjectTypeEnum.groups, subjectOperator.getId().toString(),
                    resourceType, resourceIdentifier, actionType, userPolicyList);
            if (AuthEffectEnum.deny == authResult || AuthEffectEnum.undecided == authResult) {
                denyResources.add(resourceIdentifier);
            }
        }
        return AuthRet.of(denyResources);
    }

    private AuthEffectEnum verify(AuthSubjectTypeEnum subjectType, String subjectIdentifier,
                                  AuthResourceTypeEnum resourceType, String resourceIdentifier,
                                  AuthActionEnum actionType, List<PolicyEntity> policyList) {
        if (CollectionUtils.isEmpty(policyList)) {
            return AuthEffectEnum.undecided;
        }
        AuthReq authReq = AuthReq.AuthReqBuilder.newAuthReq()
                .setSubject(subjectType, subjectIdentifier)
                .setResources(resourceType, resourceIdentifier)
                .setAction(actionType)
                .build();
        return policyEngine.verify(authReq, policyList);
    }

    private List<PolicyEntity> getPolicyEntity(AuthSubjectTypeEnum subjectType, String... subjectIdentifiers) {
        List<PolicyEntity> policyEntityList = Lists.newArrayList();
        for (String subjectIdentifier : subjectIdentifiers) {
            Optional<AuthEntry> authEntryOptional = authEntryRepo.query(subjectType.name(), subjectIdentifier);
            if (authEntryOptional.isEmpty()) {
                continue;
            }

            AuthEntry authEntry = authEntryOptional.get();
            policyEntityList.addAll(getPolicyEntity(authEntry.getId()));
        }
        return policyEntityList;
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
            List<ResourceEntity> resourceEntityList = Lists.newArrayList();
            if (AuthResourceTypeEnum.tables == policyEntity.getResourceType()) {
                List<String> resourceJsonList = policyResourceMap.get(authPolicy.getId());
                for (String resourceJson : resourceJsonList) {
                    TableResourceEntity resourceEntity = JSON.parseObject(resourceJson, TableResourceEntity.class);
                    resourceEntityList.add(resourceEntity);
                }
            }
            policyEntity.setResources(resourceEntityList);
            return policyEntity;
        }).collect(Collectors.toList());
    }

}
