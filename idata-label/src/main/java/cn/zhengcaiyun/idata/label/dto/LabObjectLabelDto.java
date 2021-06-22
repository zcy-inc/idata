package cn.zhengcaiyun.idata.label.dto;

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
    private String rules;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabObjectLabelDto that = (LabObjectLabelDto) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(nameEn, that.nameEn) && objectType.equals(that.objectType) && Objects.equals(remark, that.remark) && version.equals(that.version) && folderId.equals(that.folderId) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, nameEn, objectType, remark, version, folderId, rules);
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
                ", rules='" + rules + '\'' +
                "} " + super.toString();
    }
}
