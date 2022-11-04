package cn.zhengcaiyun.idata.portal.controller.uac;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dto.auth.ext.AuthEntryExtDto;
import cn.zhengcaiyun.idata.user.service.auth.AuthEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * UAC-Authorization
 *
 * @author yangjianhua
 * @description: 用户组管理
 **/
@RestController
@RequestMapping(path = "/p1/uac/authorizations")
public class AuthorizationController {
    private final AuthEntryService authEntryService;

    @Autowired
    public AuthorizationController(AuthEntryService authEntryService) {
        this.authEntryService = authEntryService;
    }

    /**
     * 新增授权
     *
     * @param dto
     * @return
     */
    @PostMapping()
    public RestResult<AuthEntryExtDto> addAuthEntry(@RequestBody AuthEntryExtDto dto) {
        Long id = authEntryService.addAuthEntry(dto, OperatorContext.getCurrentOperator());
        if (Objects.isNull(id)) return RestResult.error("授权失败", "");
        return RestResult.success(authEntryService.getAuthEntry(id));
    }

    /**
     * 修改授权
     *
     * @param id  授权编号
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    public RestResult<AuthEntryExtDto> addAuthEntry(@PathVariable("id") Long id, @RequestBody AuthEntryExtDto dto) {
        authEntryService.editAuthEntry(id, dto, OperatorContext.getCurrentOperator());
        return RestResult.success(authEntryService.getAuthEntry(id));
    }

    /**
     * 根据编号查询授权
     *
     * @param id 授权编号
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<AuthEntryExtDto> getAuthEntry(@PathVariable("id") Long id) {
        return RestResult.success(authEntryService.getAuthEntry(id));
    }

    /**
     * 根据主体查询授权
     *
     * @param subjectType 授权主体类型，users：用户，groups：用户组，apps：应用
     * @param subjectId   授权主体唯一标识
     * @return
     */
    @GetMapping("/subjectTypes/{subjectType}/subjectIds/{subjectId}")
    public RestResult<AuthEntryExtDto> getAuthEntry(@PathVariable("subjectType") AuthSubjectTypeEnum subjectType,
                                                    @PathVariable("subjectId") String subjectId) {
        return RestResult.success(authEntryService.getAuthEntry(subjectType, subjectId));
    }

}
