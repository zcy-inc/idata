package cn.zhengcaiyun.idata.connector.spi.hive.dto;

import java.util.ArrayList;
import java.util.List;

public class SyncHiveDTO {

    public SyncHiveDTO() {
    }

    public SyncHiveDTO(boolean firstSync) {
        this.firstSync = firstSync;
    }

    private boolean firstSync;

    private List<String> warningList = new ArrayList<>();

    private CompareInfoDTO compareInfoDTO;

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }

    public boolean isFirstSync() {
        return firstSync;
    }

    public void setFirstSync(boolean firstSync) {
        this.firstSync = firstSync;
    }

    public CompareInfoDTO getCompareInfoDTO() {
        return compareInfoDTO;
    }

    public void setCompareInfoDTO(CompareInfoDTO compareInfoDTO) {
        this.compareInfoDTO = compareInfoDTO;
    }
}
