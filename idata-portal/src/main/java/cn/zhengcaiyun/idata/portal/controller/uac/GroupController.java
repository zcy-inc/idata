package cn.zhengcaiyun.idata.portal.controller.uac;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.dto.general.SingleIdPair;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.user.condition.GroupCondition;
import cn.zhengcaiyun.idata.user.dto.GroupCombinedDto;
import cn.zhengcaiyun.idata.user.dto.GroupDto;
import cn.zhengcaiyun.idata.user.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UAC-Group
 *
 * @author yangjianhua
 * @description: 用户组管理
 **/
@RestController
@RequestMapping(path = "/p1/uac/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 分页查询用户组
     *
     * @param condition 查询条件
     * @return
     */
    @PostMapping("/paging")
    public RestResult<Page<GroupCombinedDto>> paging(@RequestBody GroupCondition condition) {
        return RestResult.success(groupService.paging(condition));
    }

    /**
     * 新建用户组
     *
     * @param dto 用户组内容
     * @return
     */
    @PostMapping()
    public RestResult<GroupCombinedDto> addGroup(@RequestBody GroupCombinedDto dto) {
        Long id = groupService.addGroup(dto, OperatorContext.getCurrentOperator());
        if (Objects.isNull(id)) return RestResult.error("新建用户组失败", "");
        return RestResult.success(groupService.getGroup(id));
    }

    /**
     * 编辑用户组
     *
     * @param id  用户组 id
     * @param dto 用户组内容
     * @return
     */
    @PutMapping("/{id}")
    public RestResult<GroupCombinedDto> editGroup(@PathVariable("id") Long id, @RequestBody GroupCombinedDto dto) {
        groupService.editGroup(id, dto, OperatorContext.getCurrentOperator());
        return RestResult.success(groupService.getGroup(id));
    }

    /**
     * 获取用户组
     *
     * @param id 用户组 id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<GroupCombinedDto> getGroup(@PathVariable("id") Long id) {
        return RestResult.success(groupService.getGroup(id));
    }

    /**
     * 删除用户组
     *
     * @param id 用户组 id
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> deleteGroup(@PathVariable("id") Long id) {
        return RestResult.success(groupService.deleteGroup(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 获取用户组下拉列表
     *
     * @return
     */
    @GetMapping("/KeyValList")
    public RestResult<List<SingleIdPair<String>>> getGroupKeyValList() {
        List<GroupDto> groupDtoList = groupService.getGroupList();
        List<SingleIdPair<String>> kvList = groupDtoList.stream()
                .map(groupDto -> new SingleIdPair<String>(groupDto.getId().toString(), groupDto.getName()))
                .collect(Collectors.toList());
        return RestResult.success(kvList);
    }

}
