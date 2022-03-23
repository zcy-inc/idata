package cn.zhengcaiyun.idata.system.service.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.system.dal.dao.SysResourceDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysResourceDynamicSqlSupport;
import cn.zhengcaiyun.idata.system.dal.model.SysResource;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.system.service.SysResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author zhanqian
 * @date 2022/3/22 3:07 PM
 * @description
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Autowired
    private SysResourceDao sysResourceDao;

    @Override
    public List<FolderTreeNodeDto> generateFolderTreeNode(Map<String, Integer> permissionMap) {
        // 另个数据源构建树
        List<SysResource> list = sysResourceDao.select(dsl -> dsl.where(SysResourceDynamicSqlSupport.sysResource.del, isEqualTo(DeleteEnum.DEL_NO.val)));
        Map<String, Long> map = list.stream().collect(Collectors.toMap(SysResource::getResourceCode, SysResource::getId));
        List<FolderTreeNodeDto> rawList = list.stream().map(e -> {
            FolderTreeNodeDto item = new FolderTreeNodeDto();
            item.setCid(e.getResourceCode());
            item.setFolderId(String.valueOf(e.getId()));
            item.setType(e.getResourceType());
            item.setName(e.getResourceName());
            Long pId = map.getOrDefault(e.getParentCode(), 0L);
            if (pId != null) {
                item.setParentId(String.valueOf(pId));
            }
            item.setFeatureCode(e.getResourceCode());
            item.setParentCode(e.getParentCode());
            item.setFilePermission(permissionMap.getOrDefault(item.getType() + item.getFolderId(), 0));
            return item;
        }).collect(Collectors.toList());

        List<FolderTreeNodeDto> nodeDtoList = generateTree(rawList);
        return nodeDtoList;
    }

    /**
     * 生成树
     * @param list
     * @return
     */
    private List<FolderTreeNodeDto> generateTree(List<FolderTreeNodeDto> list) {
        //创建一个map用于存放 <类型id，将类型对象封装成的 ModelTypeVo 对象>
        List<FolderTreeNodeDto> trees = new ArrayList<>();
        Map<String, FolderTreeNodeDto> map = list.stream().collect(Collectors.toMap(FolderTreeNodeDto::getFolderId, e -> e));

        for (FolderTreeNodeDto dto : list) {
            String parentId = dto.getParentId();

            // 顶级分类
            if ("0".equals(parentId)) {
                trees.add(dto);
                continue;
            }

            if (map.containsKey(parentId)) {
                FolderTreeNodeDto pElem = map.get(parentId);
                if (pElem.getChildren() != null) {
                    pElem.getChildren().add(dto);
                } else {
                    List<FolderTreeNodeDto> children = new ArrayList<>();
                    children.add(dto);
                    pElem.setChildren(children);
                }
            }
        }
        return trees;
    }

}
