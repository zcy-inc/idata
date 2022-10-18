package cn.zhengcaiyun.idata.user.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.user.condition.GroupCondition;
import cn.zhengcaiyun.idata.user.dal.model.Group;
import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.dal.repo.GroupRepo;
import cn.zhengcaiyun.idata.user.dal.repo.GroupUserRelationRepo;
import cn.zhengcaiyun.idata.user.dal.repo.UserRepo;
import cn.zhengcaiyun.idata.user.dto.GroupCombinedDto;
import cn.zhengcaiyun.idata.user.dto.GroupDto;
import cn.zhengcaiyun.idata.user.dto.GroupUserRelationDto;
import cn.zhengcaiyun.idata.user.dto.UserSimpleDto;
import cn.zhengcaiyun.idata.user.service.GroupService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepo groupRepo;
    private final GroupUserRelationRepo groupUserRelationRepo;

    private final UserRepo userRepo;

    @Autowired
    public GroupServiceImpl(GroupRepo groupRepo,
                            GroupUserRelationRepo groupUserRelationRepo,
                            UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.groupUserRelationRepo = groupUserRelationRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Page<GroupCombinedDto> paging(GroupCondition condition) {
        Page<Group> groupPage = groupRepo.paging(condition);
        List<Group> groupList = groupPage.getContent();
        if (ObjectUtils.isEmpty(groupList)) {
            return Page.empty();
        }
        List<Long> groupIds = groupList.stream()
                .map(Group::getId)
                .collect(Collectors.toList());
        Map<Long, List<GroupUserRelation>> userRelationMap = getUserRelationMap(groupIds);

        List<GroupCombinedDto> dtoList = groupList.stream().map(group -> {
            List<GroupUserRelation> relationList = userRelationMap.get(group.getId());
            Map<Long, UacUser> userMap = queryUsers(group, relationList);
            List<UserSimpleDto> relatedUsers = buildRelationUserDto(relationList, userMap);

            GroupCombinedDto dto = GroupCombinedDto.from(group);
            dto.setRelatedUsers(relatedUsers);
            dto.setOwnerName(getGroupOwnerName(group.getOwnerId(), userMap));
            return dto;
        }).collect(Collectors.toList());

        return Page.newOne(dtoList, groupPage.getTotal());
    }

    private Map<Long, List<GroupUserRelation>> getUserRelationMap(List<Long> groupIds) {
        return groupUserRelationRepo.queryByGroup(groupIds)
                .stream()
                .collect(Collectors.groupingBy(GroupUserRelation::getGroupId));
    }

    @Override
    @Transactional
    public Long addGroup(GroupCombinedDto dto, Operator operator) {
        checkGroupInfo(dto);
        Optional<Group> existGroupOptional = groupRepo.queryByName(dto.getName());
        checkArgument(existGroupOptional.isEmpty(), "已存在同名用户组");

        dto.setOperator(operator);
        Group group = dto.toModel();
        Long groupId = groupRepo.save(group);
        checkArgument(Objects.nonNull(groupId), "新建用户组失败");

        List<UserSimpleDto> relatedUsers = dto.getRelatedUsers();
        addOrUpdateGroupUserRelation(groupId, relatedUsers, operator);
        return groupId;
    }

    @Override
    @Transactional
    public Boolean editGroup(Long id, GroupCombinedDto dto, Operator operator) {
        checkArgument(Objects.nonNull(id), "用户组id为空");
        checkGroupInfo(dto);
        Optional<Group> groupOptional = groupRepo.query(id);
        checkArgument(groupOptional.isPresent(), "用户组不存在或已删除");

        Optional<Group> sameNameGroupOptional = groupRepo.queryByName(dto.getName());
        if (sameNameGroupOptional.isPresent()) {
            checkArgument(Objects.equals(id, sameNameGroupOptional.get().getId()), "已存在同名用户组");
        }

        dto.setId(id);
        dto.resetEditor(operator);
        Group group = dto.toModel();
        groupRepo.update(group);

        List<UserSimpleDto> relatedUsers = dto.getRelatedUsers();
        addOrUpdateGroupUserRelation(id, relatedUsers, operator);
        return Boolean.TRUE;
    }

    @Override
    public GroupCombinedDto getGroup(Long id) {
        checkArgument(Objects.nonNull(id), "用户组id为空");
        Optional<Group> groupOptional = groupRepo.query(id);
        checkArgument(groupOptional.isPresent(), "用户组不存在或已删除");
        Group group = groupOptional.get();
        List<GroupUserRelation> relationList = groupUserRelationRepo.queryByGroup(id);

        Map<Long, UacUser> userMap = queryUsers(group, relationList);
        List<UserSimpleDto> relatedUsers = buildRelationUserDto(relationList, userMap);

        GroupCombinedDto dto = GroupCombinedDto.from(group);
        dto.setRelatedUsers(relatedUsers);
        dto.setOwnerName(getGroupOwnerName(group.getOwnerId(), userMap));
        return dto;
    }

    @Override
    @Transactional
    public Boolean deleteGroup(Long id, Operator operator) {
        checkArgument(Objects.nonNull(id), "用户组id为空");
        Optional<Group> groupOptional = groupRepo.query(id);
        checkArgument(groupOptional.isPresent(), "用户组不存在或已删除");

        groupRepo.delete(id, operator.getNickname());
        groupUserRelationRepo.delete(id, operator.getNickname());
        return Boolean.TRUE;
    }

    @Override
    public List<GroupDto> getGroupList() {
        List<Group> groupList = groupRepo.queryList(new GroupCondition());
        return groupList.stream()
                .map(GroupDto::from)
                .collect(Collectors.toList());
    }

    private void addOrUpdateGroupUserRelation(Long groupId, List<UserSimpleDto> relatedUsers, Operator operator) {
        List<GroupUserRelation> relationList = null;
        if (!CollectionUtils.isEmpty(relatedUsers)) {
            List<GroupUserRelationDto> relationDtoList = buildRelation(groupId, relatedUsers);
            relationList = relationDtoList.stream().map(relationDto -> {
                relationDto.setOperator(operator);
                return relationDto.toModel();
            }).collect(Collectors.toList());
        }
        groupUserRelationRepo.saveWithOverride(groupId, relationList);
    }

    private void checkGroupInfo(GroupCombinedDto dto) {
        checkArgument(StringUtils.isNotBlank(dto.getName()), "组名称为空");
        checkArgument(Objects.nonNull(dto.getOwnerId()), "组责任人为空");
        List<UserSimpleDto> relatedUsers = dto.getRelatedUsers();
        if (CollectionUtils.isEmpty(relatedUsers)) return;

        for (UserSimpleDto userSimpleDto : relatedUsers) {
            checkArgument(Objects.nonNull(userSimpleDto.getId()), "成员id为空");
        }
    }

    private List<GroupUserRelationDto> buildRelation(Long groupId, List<UserSimpleDto> userSimpleDtoList) {
        return userSimpleDtoList.stream()
                .map(userSimpleDto -> this.buildRelation(groupId, userSimpleDto))
                .collect(Collectors.toList());
    }

    private GroupUserRelationDto buildRelation(Long groupId, UserSimpleDto userSimpleDto) {
        GroupUserRelationDto relationDto = new GroupUserRelationDto();
        relationDto.setGroupId(groupId);
        relationDto.setUserId(userSimpleDto.getId());
        return relationDto;
    }

    private Map<Long, UacUser> queryUsers(Group group, List<GroupUserRelation> relationList) {
        if (CollectionUtils.isEmpty(relationList)) return Maps.newHashMap();

        List<Long> userIds = relationList.stream()
                .map(GroupUserRelation::getUserId)
                .collect(Collectors.toList());
        userIds.add(group.getOwnerId());
        return queryUsers(userIds);
    }

    private Map<Long, UacUser> queryUsers(List<Long> userIds) {
        Map<Long, UacUser> userMap = Maps.newHashMap();
        List<UacUser> uacUserList = userRepo.query(userIds);
        if (CollectionUtils.isEmpty(uacUserList)) return userMap;

        uacUserList.stream().forEach(uacUser -> {
            userMap.put(uacUser.getId(), uacUser);
        });
        return userMap;
    }

    private String getGroupOwnerName(Long ownerId, Map<Long, UacUser> userMap) {
        UacUser groupOwner = userMap.get(ownerId);
        if (Objects.nonNull(groupOwner)) {
            return groupOwner.getNickname();
        }
        return null;
    }

    private List<UserSimpleDto> buildRelationUserDto(List<GroupUserRelation> relationList, Map<Long, UacUser> userMap) {
        if (CollectionUtils.isEmpty(relationList)) {
            return Lists.newArrayList();
        }
        return relationList.stream()
                .map(GroupUserRelation::getUserId)
                .map(relationUserId -> {
                    UserSimpleDto userSimpleDto = new UserSimpleDto();
                    userSimpleDto.setId(relationUserId);
                    if (Objects.nonNull(userMap)) {
                        UacUser uacUser = userMap.get(relationUserId);
                        if (Objects.nonNull(uacUser)) {
                            userSimpleDto.setNickname(uacUser.getNickname());
                        }
                    }
                    return userSimpleDto;
                }).collect(Collectors.toList());
    }

}
