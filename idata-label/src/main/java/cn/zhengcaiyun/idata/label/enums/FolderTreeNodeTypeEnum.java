package cn.zhengcaiyun.idata.label.enums;

/**
 * @description: 文件夹树节点类型
 * @author: yangjianhua
 * @create: 2021-06-22 11:13
 **/
public enum FolderTreeNodeTypeEnum {
    FOLDER("FOLDER", "文件夹"),
    LABEL("LABEL", "数据标签"),
    ;

    private String code;
    private String desc;

    FolderTreeNodeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
