package cn.zhengcaiyun.idata.user.manager;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthPolicyRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthResourceRepo;
import cn.zhengcaiyun.idata.user.dto.auth.AuthResourceDto;
import cn.zhengcaiyun.idata.user.dto.auth.ext.AuthPolicyExtDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthEntryManager {

    private final AuthPolicyRepo authPolicyRepo;
    private final AuthResourceRepo authResourceRepo;

    @Autowired
    public AuthEntryManager(AuthPolicyRepo authPolicyRepo,
                            AuthResourceRepo authResourceRepo) {
        this.authPolicyRepo = authPolicyRepo;
        this.authResourceRepo = authResourceRepo;
    }

    public List<AuthPolicyExtDto> getAuthPolicyDtoList(Long authId) {
        List<AuthPolicy> authPolicies = authPolicyRepo.queryListByAuthId(authId);
        Map<Long, List<AuthResourceDto>> policyResourceMap = getAuthResourceDtoList(authId);
        return authPolicies.stream().map(authPolicy -> {
            AuthPolicyExtDto authPolicyExtDto = (AuthPolicyExtDto) AuthPolicyExtDto.from(authPolicy);
            authPolicyExtDto.setAuthResourceList(policyResourceMap.get(authPolicy.getId()));
            return authPolicyExtDto;
        }).collect(Collectors.toList());
    }

    public Map<Long, List<AuthResourceDto>> getAuthResourceDtoList(Long authId) {
        List<AuthResource> authResources = authResourceRepo.queryListByAuthId(authId);
        return authResources.stream()
                .map(AuthResourceDto::from)
                .collect(Collectors.groupingBy(AuthResourceDto::getPolicyRecordId));
    }

}
