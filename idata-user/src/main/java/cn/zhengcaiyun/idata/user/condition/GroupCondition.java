package cn.zhengcaiyun.idata.user.condition;

import cn.zhengcaiyun.idata.commons.pojo.PageCondition;

public class GroupCondition extends PageCondition {

    /**
     * 名称（模糊搜索）
     */
    private String namePattern;

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }
}
