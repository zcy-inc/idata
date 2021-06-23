package cn.zhengcaiyun.idata.portal.controller.lab;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.label.dto.LabFolderDto;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;
import cn.zhengcaiyun.idata.label.service.LabFolderService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * lab-folder-api
 * 文件夹接口
 *
 * @description: 提供文件夹crud接口，文件树查询接口
 * @author: yangjianhua
 * @create: 2021-06-22 14:34
 **/
@RestController
@RequestMapping(path = "/p1/lab")
public class LabFolderController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LabFolderService folderService;

    /**
     * 创建文件夹
     *
     * @param labFolderDto 文件夹dto
     * @param request
     * @return {@link RestResult}
     */
    @PostMapping("/labFolder")
    public RestResult<Long> createFolder(@RequestBody LabFolderDto labFolderDto, HttpServletRequest request) {
        return RestResult.success(folderService.createFolder(labFolderDto, tokenService.getNickname(request)));
    }

    /**
     * 编辑文件夹
     *
     * @param labFolderDto 文件夹dto
     * @param request
     * @return {@link RestResult}
     */
    @PutMapping("/labFolder")
    public RestResult<Long> editFolder(@RequestBody LabFolderDto labFolderDto, HttpServletRequest request) {
        return RestResult.success(folderService.editFolder(labFolderDto, tokenService.getNickname(request)));
    }

    /**
     * 查询文件夹
     *
     * @param id 文件夹id
     * @return {@link RestResult}
     */
    @GetMapping("/labFolder/{id}")
    public RestResult<LabFolderDto> getFolder(@PathVariable Long id) {
        return RestResult.success(folderService.getFolder(id));
    }

    /**
     * 删除文件夹
     *
     * @param id      文件夹id
     * @param request
     * @return {@link RestResult}
     */
    @DeleteMapping("/labFolder/{id}")
    public RestResult<Boolean> deleteFolder(@PathVariable("id") Long id, HttpServletRequest request) {
        return RestResult.success(folderService.deleteFolder(id, tokenService.getNickname(request)));
    }

    /**
     * 查询文件夹列表
     *
     * @param belong 所属业务标识，如：数据标签为lab
     * @return {@link RestResult}
     */
    @GetMapping("/labFolders")
    public RestResult<List<LabFolderDto>> getFolders(@RequestParam(value = "belong") String belong) {
        return RestResult.success(folderService.getFolders(belong));
    }

    /**
     * 查询文件树
     *
     * @param belong 所属业务标识，如：数据标签为lab
     * @return {@link RestResult}
     */
    @GetMapping("/labFolder/tree")
    public RestResult<List<LabFolderTreeNodeDto>> getFolderTree(@RequestParam(value = "belong") String belong) {
        return RestResult.success(folderService.getFolderTree(belong));
    }

}
