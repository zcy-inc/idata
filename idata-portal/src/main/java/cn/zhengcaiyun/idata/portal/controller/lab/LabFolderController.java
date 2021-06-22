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
 * @description: 文件夹controller
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

    @PostMapping("/labFolder")
    public RestResult<Long> createFolder(@RequestBody LabFolderDto labFolderDto, HttpServletRequest request) {
        return RestResult.success(folderService.createFolder(labFolderDto, tokenService.getNickname(request)));
    }

    @PutMapping("/labFolder")
    public RestResult<Long> editFolder(@RequestBody LabFolderDto labFolderDto, HttpServletRequest request) {
        return RestResult.success(folderService.editFolder(labFolderDto, tokenService.getNickname(request)));
    }

    @GetMapping("/labFolder/{id}")
    public RestResult<LabFolderDto> getFolder(@PathVariable Long id) {
        return RestResult.success(folderService.getFolder(id));
    }

    @DeleteMapping("/labFolder/{id}")
    public RestResult<Boolean> deleteFolder(@PathVariable("id") Long id, HttpServletRequest request) {
        return RestResult.success(folderService.deleteFolder(id, tokenService.getNickname(request)));
    }

    @GetMapping("/labFolders")
    public RestResult<List<LabFolderDto>> getFolders(@RequestParam(value = "belong") String belong) {
        return RestResult.success(folderService.getFolders(belong));
    }

    @GetMapping("/labFolder/tree")
    public RestResult<List<LabFolderTreeNodeDto>> getFolderTree(@RequestParam(value = "belong") String belong) {
        return RestResult.success(folderService.getFolderTree(belong));
    }

}
