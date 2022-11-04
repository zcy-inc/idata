package cn.zhengcaiyun.idata.develop.dto.opt.stream;

import java.util.List;

public class StreamJobStartParamConfig {
    /**
     * 是否需要显示可初始化表集合，为true时，显示 forceInitTableList ，为 false 时，显示初始化开关
     */
    private Boolean needInitTable;
    /**
     * 可初始化表集合
     */
    private List<String> forceInitTableList;

    public Boolean getNeedInitTable() {
        return needInitTable;
    }

    public void setNeedInitTable(Boolean needInitTable) {
        this.needInitTable = needInitTable;
    }

    public List<String> getForceInitTableList() {
        return forceInitTableList;
    }

    public void setForceInitTableList(List<String> forceInitTableList) {
        this.forceInitTableList = forceInitTableList;
    }
}
