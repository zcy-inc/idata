package cn.zhengcaiyun.idata.dqc.controller;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.dqc.model.entity.User;
import cn.zhengcaiyun.idata.dqc.service.UserService;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import cn.zhengcaiyun.idata.dqc.model.common.BaseQuery;
import cn.zhengcaiyun.idata.dqc.model.common.Result;

import java.util.List;

/**
 * 用户表(User)表控制层
 *
 * @author zheng
 * @since 2022-07-15 17:34:00
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("getList")
    public Result<List<User>> getList(String name) {
        List<User> userList = userService.getList(name);
        User dwDuty = new User();
        dwDuty.setNickname(RuleUtils.DW_DUTY);
        userList.add(dwDuty);
        return Result.successResult(userList);
    }

    @RequestMapping("getCur")
    public Result<User> getCur() {
        Operator operator = OperatorContext.getCurrentOperator();
        User user = new User();
        user.setNickname(operator.getNickname());
        return Result.successResult(user);
    }


}

