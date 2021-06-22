package cn.zhengcaiyun.idata.label.service;

import cn.zhengcaiyun.idata.label.dto.LabFolderDto;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;

import java.util.List;

/**
 * @description: 文件夹service
 * @author: yangjianhua
 * @create: 2021-06-21 15:38
 **/
public interface LabFolderService {
    /**
     * 创建文件夹
     *
     * @param labFolderDto
     * @param operator
     * @returns
     */
    Long createFolder(LabFolderDto labFolderDto, String operator);

    /**
     * 编辑文件夹
     *
     * @param labFolderDto
     * @param operator
     * @return
     */
    Long editFolder(LabFolderDto labFolderDto, String operator);

    /**
     * 获取指定文件夹
     *
     * @param id
     * @return
     */
    LabFolderDto getFolder(Long id);

    /**
     * 删除文件夹
     *
     * @param id
     * @param operator
     * @return
     */
    Boolean deleteFolder(Long id, String operator);

    /**
     * 获取文件夹list
     *
     * @return
     */
    List<LabFolderDto> getFolders(String belong);

    /**
     * 获取文件树
     *
     * @param belong
     * @return
     */
    List<LabFolderTreeNodeDto> getFolderTree(String belong);
}
