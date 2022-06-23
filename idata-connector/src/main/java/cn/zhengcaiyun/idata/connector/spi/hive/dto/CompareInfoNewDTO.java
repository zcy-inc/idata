package cn.zhengcaiyun.idata.connector.spi.hive.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CompareInfoNewDTO {

    // 相比hive多的字段
    private List<MoreColumnInfo> moreList = new ArrayList<>();

    // 相比hive少的字段
    private List<LessColumnInfo> lessList = new ArrayList<>();

    // 相比hive不同的字段
    private List<ChangeColumnInfo> differentList = new ArrayList<>();

    public List<MoreColumnInfo> getMoreList() {
        return moreList;
    }

    public void setMoreList(List<MoreColumnInfo> moreList) {
        this.moreList = moreList;
    }

    public List<LessColumnInfo> getLessList() {
        return lessList;
    }

    public void setLessList(List<LessColumnInfo> lessList) {
        this.lessList = lessList;
    }

    public List<ChangeColumnInfo> getDifferentList() {
        return differentList;
    }

    public void setDifferentList(List<ChangeColumnInfo> differentList) {
        this.differentList = differentList;
    }

    public static class LessColumnInfo {
        public LessColumnInfo() {
        }

        public LessColumnInfo(String hiveColumnName, String hiveColumnType, String hiveColumnComment) {
            this.hiveColumnName = hiveColumnName;
            this.hiveColumnType = hiveColumnType;
            this.hiveColumnComment = hiveColumnComment;
        }

        private Long columnId;

        private String columnName;

        private String hiveColumnName;

        private String hiveColumnType;

        private String hiveColumnComment;

        private Integer hiveColumnIndex;

        private boolean isHivePartition;

        public Long getColumnId() {
            return columnId;
        }

        public void setColumnId(Long columnId) {
            this.columnId = columnId;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getHiveColumnName() {
            return hiveColumnName;
        }

        public void setHiveColumnName(String hiveColumnName) {
            this.hiveColumnName = hiveColumnName;
        }

        public String getHiveColumnType() {
            return hiveColumnType;
        }

        public void setHiveColumnType(String hiveColumnType) {
            this.hiveColumnType = hiveColumnType;
        }

        public String getHiveColumnComment() {
            return hiveColumnComment;
        }

        public void setHiveColumnComment(String hiveColumnComment) {
            this.hiveColumnComment = hiveColumnComment;
        }

        public boolean isHivePartition() {
            return isHivePartition;
        }

        public void setHivePartition(boolean hivePartition) {
            isHivePartition = hivePartition;
        }

        public Integer getHiveColumnIndex() {
            return hiveColumnIndex;
        }

        public void setHiveColumnIndex(Integer hiveColumnIndex) {
            this.hiveColumnIndex = hiveColumnIndex;
        }
    }

    public static class MoreColumnInfo {
        public MoreColumnInfo() {
        }

        public MoreColumnInfo(String columnName, String columnType, String columnComment) {
            this.columnName = columnName;
            this.columnType = columnType;
            this.columnComment = columnComment;
        }

        private Long columnId;

        private String columnName;

        private String columnType;

        private String columnComment;

        private Integer columnIndex;

        private boolean isPartition;

        public Long getColumnId() {
            return columnId;
        }

        public void setColumnId(Long columnId) {
            this.columnId = columnId;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }

        public boolean isPartition() {
            return isPartition;
        }

        public void setPartition(boolean partition) {
            isPartition = partition;
        }

        public Integer getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(Integer columnIndex) {
            this.columnIndex = columnIndex;
        }
    }

    public static class ChangeColumnInfo {

        private Long columnId;

        private String columnName;

        private String columnType;

        private String columnComment;

        private Integer columnIndex;

        private boolean isPartition;

        private String hiveColumnName;

        private String hiveColumnType;

        private String hiveColumnComment;

        private Integer hiveColumnIndex;

        private boolean isHivePartition;

        private String changeDescription;

        public Long getColumnId() {
            return columnId;
        }

        public void setColumnId(Long columnId) {
            this.columnId = columnId;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }

        public String getHiveColumnName() {
            return hiveColumnName;
        }

        public void setHiveColumnName(String hiveColumnName) {
            this.hiveColumnName = hiveColumnName;
        }

        public String getHiveColumnType() {
            return hiveColumnType;
        }

        public void setHiveColumnType(String hiveColumnType) {
            this.hiveColumnType = hiveColumnType;
        }

        public String getHiveColumnComment() {
            return hiveColumnComment;
        }

        public void setHiveColumnComment(String hiveColumnComment) {
            this.hiveColumnComment = hiveColumnComment;
        }

        public boolean isPartition() {
            return isPartition;
        }

        public void setPartition(boolean partition) {
            isPartition = partition;
        }

        public boolean isHivePartition() {
            return isHivePartition;
        }

        public void setHivePartition(boolean hivePartition) {
            isHivePartition = hivePartition;
        }

        public Integer getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(Integer columnIndex) {
            this.columnIndex = columnIndex;
        }

        public Integer getHiveColumnIndex() {
            return hiveColumnIndex;
        }

        public void setHiveColumnIndex(Integer hiveColumnIndex) {
            this.hiveColumnIndex = hiveColumnIndex;
        }

        /**
         * 排除分区属性的字段不同
         * @return
         */
        public String getChangeDescription() {
            StringBuilder stringBuilder = new StringBuilder("字段更新\n");
            if (!StringUtils.equals(columnName, hiveColumnName)) {
                stringBuilder.append("字段名称:" + hiveColumnName + " 改为 " + columnName);
            }
            if (!StringUtils.equals(columnType, hiveColumnType)) {
                stringBuilder.append("字段类型:" + hiveColumnType + " 改为 " + columnType);
            }
            if (isPartition != isHivePartition) {
                stringBuilder.append("字段分区属性:" + isHivePartition + " 改为 " + isPartition);
            }
            if (!StringUtils.equals(columnComment, hiveColumnComment)) {
                stringBuilder.append("字段注释属性:" + hiveColumnComment + " 改为 " + columnComment);
            }
            return stringBuilder.toString();
        }
    }
}
