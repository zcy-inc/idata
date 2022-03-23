package cn.zhengcaiyun.idata.system.service;

import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;

import java.util.List;
import java.util.Map;

public interface SysResourceService {

    /**
     * 生成适配之前代码结构的资源树
     * @return
     */
    List<FolderTreeNodeDto> generateFolderTreeNode(Map<String, Integer> permissionMap);
}
