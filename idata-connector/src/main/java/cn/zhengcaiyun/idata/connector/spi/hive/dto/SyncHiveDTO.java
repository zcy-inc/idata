package cn.zhengcaiyun.idata.connector.spi.hive.dto;

import java.util.List;

public class SyncHiveDTO {

    private List<String> warningList;

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }
}
