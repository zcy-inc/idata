package cn.zhengcaiyun.idata.portal.model.response.dev;

import java.util.List;

/**
 * @author zhanqian
 * @date 2022/6/16 5:22 PM
 * @description
 */
public class CompareInfoResponse {

    /**
     * 变更列信息
     */
    private List<ChangeContentInfo> changeContentInfoList;

    public static class ChangeContentInfo {

        /**
         * idata列名
         */
        private String columnNameBefore;

        /**
         * 更新内容
         */
        private String changeDescription;

        /**
         * Hive更新后字段名称
         */
        private String columnNameAfter;

        public String getChangeDescription() {
            return changeDescription;
        }

        public void setChangeDescription(String changeDescription) {
            this.changeDescription = changeDescription;
        }

        public String getColumnNameBefore() {
            return columnNameBefore;
        }

        public void setColumnNameBefore(String columnNameBefore) {
            this.columnNameBefore = columnNameBefore;
        }

        public String getColumnNameAfter() {
            return columnNameAfter;
        }

        public void setColumnNameAfter(String columnNameAfter) {
            this.columnNameAfter = columnNameAfter;
        }
    }

    public List<ChangeContentInfo> getChangeContentInfoList() {
        return changeContentInfoList;
    }

    public void setChangeContentInfoList(List<ChangeContentInfo> changeContentInfoList) {
        this.changeContentInfoList = changeContentInfoList;
    }

}
