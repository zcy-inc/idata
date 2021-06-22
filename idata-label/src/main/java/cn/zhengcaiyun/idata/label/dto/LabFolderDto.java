package cn.zhengcaiyun.idata.label.dto;

import java.util.Objects;

/**
 * @description: 文件夹dto类
 * @author: yangjianhua
 * @create: 2021-06-21 15:23
 **/
public class LabFolderDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父文件夹编号，第一级文件夹父编号为0
     */
    private Long parentId;
    /**
     * 所属业务标识
     */
    private String belong;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabFolderDto that = (LabFolderDto) o;
        return id.equals(that.id) && name.equals(that.name) && parentId.equals(that.parentId) && belong.equals(that.belong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, parentId, belong);
    }

    @Override
    public String toString() {
        return "LabFolderDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", belong='" + belong + '\'' +
                "} " + super.toString();
    }
}
