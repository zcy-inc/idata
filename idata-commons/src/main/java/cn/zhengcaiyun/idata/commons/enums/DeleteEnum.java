package cn.zhengcaiyun.idata.commons.enums;

/**
 * @description: 记录删除状态枚举
 * @author: yangjianhua
 * @create: 2021-06-22 15:14
 **/
public enum DeleteEnum {
    DEL_NO(0, "否"),
    DEL_YES(1, "是");

    public final int val;
    public final String desc;

    DeleteEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

}
