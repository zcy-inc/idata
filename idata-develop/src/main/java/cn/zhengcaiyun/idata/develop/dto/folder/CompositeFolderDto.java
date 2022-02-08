package cn.zhengcaiyun.idata.develop.dto.folder;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import org.springframework.beans.BeanUtils;

/**
 * @description: 复合文件夹
 * @author: yangjianhua
 * @create: 2021-09-15 16:07
 **/
public class CompositeFolderDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 功能型：FUNCTION，普通型：FOLDER
     */
    private String type;

    /**
     * 文件夹所属业务功能：DESIGN, DESIGN.TABLE, DESIGN.LABEL, DESIGN.ENUM, DAG, DI, DEV, DEV.JOB
     */
    private String belong;

    /**
     * 父文件夹编号，第一级文件夹父编号为0
     */
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public static CompositeFolderDto from(CompositeFolder folder) {
        CompositeFolderDto dto = new CompositeFolderDto();
        BeanUtils.copyProperties(folder, dto);
        return dto;
    }

    public CompositeFolder toModel() {
        CompositeFolder folder = new CompositeFolder();
        BeanUtils.copyProperties(this, folder);
        return folder;
    }
}