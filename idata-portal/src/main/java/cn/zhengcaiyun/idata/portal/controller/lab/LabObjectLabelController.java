package cn.zhengcaiyun.idata.portal.controller.lab;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.label.dto.LabObjectLabelDto;
import cn.zhengcaiyun.idata.label.service.LabObjectLabelService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 标签controller
 * @author: yangjianhua
 * @create: 2021-06-22 14:34
 **/
@RestController
@RequestMapping(path = "/p1/lab")
public class LabObjectLabelController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LabObjectLabelService objectLabelService;

    @PostMapping("/objectLabel")
    public RestResult<Long> createLabel(@RequestBody LabObjectLabelDto labelDto, HttpServletRequest request) {
        return RestResult.success(objectLabelService.createLabel(labelDto, tokenService.getNickname(request)));
    }

    @PutMapping("/objectLabel")
    public RestResult<Long> editLabel(@RequestBody LabObjectLabelDto labelDto, HttpServletRequest request) {
        return RestResult.success(objectLabelService.editLabel(labelDto, tokenService.getNickname(request)));
    }

    @GetMapping("/objectLabel/{id}")
    public RestResult<LabObjectLabelDto> getLabel(@PathVariable Long id) {
        return RestResult.success(objectLabelService.getLabel(id));
    }

    @DeleteMapping("/objectLabel/{id}")
    public RestResult<Boolean> deleteLabel(@PathVariable("id") Long id, HttpServletRequest request) {
        return RestResult.success(objectLabelService.deleteLabel(id, tokenService.getNickname(request)));
    }

}
