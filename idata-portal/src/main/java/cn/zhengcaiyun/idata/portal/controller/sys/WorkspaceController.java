package cn.zhengcaiyun.idata.portal.controller.sys;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.dal.dao.WorkspaceDao;
import cn.zhengcaiyun.idata.system.dal.model.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

@RestController
@RequestMapping("/p1/workspaces")
public class WorkspaceController {

    @Autowired
    private WorkspaceDao workspaceDao;

    @GetMapping("/list")
    public RestResult<List<Workspace>> getWorkSpaces() {
        List<Workspace> workspaceList = workspaceDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1)));
        return RestResult.success(workspaceList);
    }

}
