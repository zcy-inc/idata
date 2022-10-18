package cn.zhengcaiyun.idata.develop.util;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.user.auth.AuthRet;
import cn.zhengcaiyun.idata.user.auth.AuthenticateService;
import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class TablePermissionChecker {

    public static final Set<String> DW_DBS = Sets.newHashSet("ODS", "DIM", "DWD", "DWS", "ADS", "TAG");

    private final AuthenticateService authenticateService;

    @Autowired
    public TablePermissionChecker(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    public void checkTableReadPermission(Operator operator, List<String> tables) {
        checkTablePermission(operator, tables, AuthActionEnum.read);
    }

    public void checkTableWritePermission(Operator operator, List<String> tables) {
        checkTablePermission(operator, tables, AuthActionEnum.write);
    }

    public void checkTablePermission(Operator operator, List<String> tables, AuthActionEnum actionEnum) {
        if (CollectionUtils.isEmpty(tables)) return;

        List<String> needCheckTables = tables.stream().filter(table -> {
            String[] tblArray = table.split("\\.");
            if (tblArray.length == 2) {
                return DW_DBS.contains(tblArray[0].toUpperCase());
            }
            return false;
        }).collect(Collectors.toList());

        AuthRet authRet = authenticateService.authenticate(operator, AuthResourceTypeEnum.tables, needCheckTables, actionEnum);
        checkArgument(authRet.allowed(), authRet.getDenyMsg());
    }
}
