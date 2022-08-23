package cn.zhengcaiyun.idata.portal.model.response.job;

import java.util.List;

/**
 * @author zhanqian
 * @date 2022/8/8 10:15 AM
 * @description
 */
public class DIJobDatasourceResponse {

    /**
     * 来源数据类型
     */
    private List<String> fromList;

    /**
     * 外部表数据类型
     */
    private List<String> externalList;

    /**
     * 去向数据类型
     */
    private List<String> destList;

    public List<String> getFromList() {
        return fromList;
    }

    public void setFromList(List<String> fromList) {
        this.fromList = fromList;
    }

    public List<String> getDestList() {
        return destList;
    }

    public void setDestList(List<String> destList) {
        this.destList = destList;
    }

    public List<String> getExternalList() {
        return externalList;
    }

    public void setExternalList(List<String> externalList) {
        this.externalList = externalList;
    }
}
