package cn.zhengcaiyun.idata.label.dto;

import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleLayerDto;

import java.util.List;
import java.util.Objects;

/**
 * @description: 对象标签dto类
 * @author: yangjianhua
 * @create: 2021-06-21 15:30
 **/
public class LabObjectLabelDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 主体类型
     */
    private String objectType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 文件夹id
     */
    private Long folderId;

    /**
     * 标签规则，json格式
     */
    private List<LabelRuleLayerDto> ruleLayers;

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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public List<LabelRuleLayerDto> getRuleLayers() {
        return ruleLayers;
    }

    public void setRuleLayers(List<LabelRuleLayerDto> ruleLayers) {
        this.ruleLayers = ruleLayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabObjectLabelDto that = (LabObjectLabelDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(nameEn, that.nameEn) && Objects.equals(objectType, that.objectType) && Objects.equals(remark, that.remark) && Objects.equals(version, that.version) && Objects.equals(folderId, that.folderId) && Objects.equals(ruleLayers, that.ruleLayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, nameEn, objectType, remark, version, folderId, ruleLayers);
    }

    @Override
    public String toString() {
        return "LabObjectLabelDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", objectType='" + objectType + '\'' +
                ", remark='" + remark + '\'' +
                ", version=" + version +
                ", folderId=" + folderId +
                ", ruleLayers=" + ruleLayers +
                "} " + super.toString();
    }
}
