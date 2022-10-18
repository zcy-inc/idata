package cn.zhengcaiyun.idata.user.service.auth.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthEntry;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthPolicy;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthEntryRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthPolicyRepo;
import cn.zhengcaiyun.idata.user.dal.repo.auth.AuthResourceRepo;
import cn.zhengcaiyun.idata.user.dto.auth.AuthResourceDto;
import cn.zhengcaiyun.idata.user.dto.auth.TableAuthResourceDto;
import cn.zhengcaiyun.idata.user.dto.auth.ext.AuthEntryExtDto;
import cn.zhengcaiyun.idata.user.dto.auth.ext.AuthPolicyExtDto;
import cn.zhengcaiyun.idata.user.manager.AuthEntryManager;
import cn.zhengcaiyun.idata.user.service.auth.AuthEntryService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class AuthEntryServiceImpl implements AuthEntryService {

    private final AuthEntryRepo authEntryRepo;
    private final AuthPolicyRepo authPolicyRepo;
    private final AuthResourceRepo authResourceRepo;
    private final AuthEntryManager authEntryManager;

    @Autowired
    public AuthEntryServiceImpl(AuthEntryRepo authEntryRepo,
                                AuthPolicyRepo authPolicyRepo,
                                AuthResourceRepo authResourceRepo,
                                AuthEntryManager authEntryManager) {
        this.authEntryRepo = authEntryRepo;
        this.authPolicyRepo = authPolicyRepo;
        this.authResourceRepo = authResourceRepo;
        this.authEntryManager = authEntryManager;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long addAuthEntry(AuthEntryExtDto authEntryExtDto, Operator operator) {
        checkAuthEntryParam(authEntryExtDto);
        authEntryExtDto.setOperator(operator);
        AuthEntry authEntry = authEntryExtDto.toModel();
        Long authId = authEntryRepo.save(authEntry);
        checkArgument(Objects.nonNull(authId), "授权保存失败");

        List<AuthPolicyExtDto> authPolicyList = authEntryExtDto.getAuthPolicyList();
        addAuthPolicy(authId, authPolicyList, operator);
        return authId;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean editAuthEntry(Long id, AuthEntryExtDto authEntryExtDto, Operator operator) {
        checkArgument(Objects.nonNull(id), "授权记录编号为空");
        checkAuthEntryParam(authEntryExtDto);
        authEntryExtDto.setId(id);
        authEntryExtDto.resetEditor(operator);
        AuthEntry authEntry = authEntryExtDto.toModel();
        authEntryRepo.update(authEntry);
        authPolicyRepo.deleteByAuthId(id);
        authResourceRepo.deleteByAuthId(id);

        List<AuthPolicyExtDto> authPolicyList = authEntryExtDto.getAuthPolicyList();
        addAuthPolicy(id, authPolicyList, operator);
        return Boolean.TRUE;
    }

    @Override
    public AuthEntryExtDto getAuthEntry(Long id) {
        checkArgument(Objects.nonNull(id), "授权记录编号为空");
        Optional<AuthEntry> authEntryOptional = authEntryRepo.query(id);
        checkArgument(authEntryOptional.isPresent(), "授权记录不存在或已删除");
        AuthEntry authEntry = authEntryOptional.get();
        AuthEntryExtDto authEntryExtDto = AuthEntryExtDto.from(authEntry);

        List<AuthPolicyExtDto> authPolicyList = authEntryManager.getAuthPolicyDtoList(id);
        authEntryExtDto.setAuthPolicyList(authPolicyList);
        return authEntryExtDto;
    }

    @Override
    public AuthEntryExtDto getAuthEntry(AuthSubjectTypeEnum subjectType, String subjectId) {
        checkArgument(Objects.nonNull(subjectType), "授权记录编号为空");
        checkArgument(StringUtils.isNotBlank(subjectId), "授权记录编号为空");
        Optional<AuthEntry> authEntryOptional = authEntryRepo.query(subjectType.name(), subjectId);
        if (authEntryOptional.isEmpty()) {
            return null;
        }
        AuthEntry authEntry = authEntryOptional.get();
        AuthEntryExtDto authEntryExtDto = AuthEntryExtDto.from(authEntry);

        List<AuthPolicyExtDto> authPolicyList = authEntryManager.getAuthPolicyDtoList(authEntry.getId());
        authEntryExtDto.setAuthPolicyList(authPolicyList);
        return authEntryExtDto;
    }

    private void addAuthPolicy(Long authId, List<AuthPolicyExtDto> authPolicyList, Operator operator) {
        for (AuthPolicyExtDto authPolicyExtDto : authPolicyList) {
            addAuthPolicy(authId, authPolicyExtDto, operator);
        }
    }

    private void addAuthPolicy(Long authId, AuthPolicyExtDto authPolicyExtDto, Operator operator) {
        authPolicyExtDto.setAuthRecordId(authId);
        authPolicyExtDto.setOperator(operator);
        AuthPolicy authPolicy = authPolicyExtDto.toModel();
        Long policyId = authPolicyRepo.save(authPolicy);
        checkArgument(Objects.nonNull(policyId), "授权保存失败");

        List<AuthResourceDto> authResourceList = authPolicyExtDto.getAuthResourceList();
        addAuthResource(authId, policyId, authPolicyExtDto.getResourceType(), authResourceList, operator);
    }

    private void addAuthResource(Long authId, Long policyId, AuthResourceTypeEnum resourceType, List<AuthResourceDto> authResourceList, Operator operator) {
        List<AuthResource> authResources = authResourceList.stream().map(authResourceDto -> {
            authResourceDto.setAuthRecordId(authId);
            authResourceDto.setPolicyRecordId(policyId);
            authResourceDto.setResourceType(resourceType);
            authResourceDto.setOperator(operator);
            return authResourceDto.toModel();
        }).collect(Collectors.toList());
        authResourceRepo.save(authResources);
    }

    private void checkAuthEntryParam(AuthEntryExtDto authEntryExtDto) {
        checkArgument(StringUtils.isNotBlank(authEntryExtDto.getSubjectId()), "授权主体为空");
        checkArgument(Objects.nonNull(authEntryExtDto.getSubjectType()), "授权主体类型为空");

        List<AuthPolicyExtDto> authPolicyList = authEntryExtDto.getAuthPolicyList();
        checkArgument(!CollectionUtils.isEmpty(authPolicyList), "授权策略/规则为空");
        for (AuthPolicyExtDto authPolicyExtDto : authPolicyList) {
            checkAuthPolicy(authPolicyExtDto);
        }
    }

    private void checkAuthPolicy(AuthPolicyExtDto authPolicyExtDto) {
        checkArgument(Objects.nonNull(authPolicyExtDto.getEffect()), "授权作用为空");
        checkArgument(!CollectionUtils.isEmpty(authPolicyExtDto.getActionList()), "授权操作为空");
        checkArgument(Objects.nonNull(authPolicyExtDto.getResourceType()), "授权资源类型不合法");

        List<AuthResourceDto> authResourceList = authPolicyExtDto.getAuthResourceList();
        checkArgument(!CollectionUtils.isEmpty(authResourceList), "授权资源为空");
        for (AuthResourceDto authResourceDto : authResourceList) {
            checkAuthResource(authPolicyExtDto.getResourceType(), authResourceDto);
        }
    }

    private void checkAuthResource(AuthResourceTypeEnum resourceType, AuthResourceDto authResourceDto) {
        checkArgument(StringUtils.isNotBlank(authResourceDto.getResources()), "授权资源为空");

        try {
            if (AuthResourceTypeEnum.tables == resourceType) {
                TableAuthResourceDto tableAuthResourceDto = JSON.parseObject(authResourceDto.getResources(), TableAuthResourceDto.class);
                checkArgument(Objects.nonNull(tableAuthResourceDto), "授权资源不合法");
                checkArgument(StringUtils.isNotBlank(tableAuthResourceDto.getDb()), "授权资源-数据库不合法");
                List<String> tables = tableAuthResourceDto.getTables();
                checkArgument(!CollectionUtils.isEmpty(tables), "授权资源-表不合法");
                for (String table : tables) {
                    checkArgument(StringUtils.isNotBlank(table), "授权资源-表不合法");
                }
            } else {
                throw new IllegalArgumentException("授权资源类型不合法，解析失败");
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("授权资源不合法，解析失败");
        }
    }
}
