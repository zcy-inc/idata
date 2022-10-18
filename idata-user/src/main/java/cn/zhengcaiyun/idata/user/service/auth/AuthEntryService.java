package cn.zhengcaiyun.idata.user.service.auth;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;
import cn.zhengcaiyun.idata.user.dto.auth.ext.AuthEntryExtDto;

public interface AuthEntryService {
    Long addAuthEntry(AuthEntryExtDto authEntryExtDto, Operator operator);

    Boolean editAuthEntry(Long id, AuthEntryExtDto authEntryExtDto, Operator operator);

    AuthEntryExtDto getAuthEntry(Long id);

    /**
     * @param subjectType 授权主体类型，users：用户，groups：用户组，apps：应用
     * @param subjectId   授权主体唯一标识
     * @return
     */
    AuthEntryExtDto getAuthEntry(AuthSubjectTypeEnum subjectType, String subjectId);

}
