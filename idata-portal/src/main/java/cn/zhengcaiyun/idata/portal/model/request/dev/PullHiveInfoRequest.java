package cn.zhengcaiyun.idata.portal.model.request.dev;

import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;

import java.util.List;

/**
 * @author zhanqian
 * @date 2022/6/21 11:42 AM
 * @description
 */
public class PullHiveInfoRequest {

    private String dbName;

    private String tableName;

    private List<ColumnInfoDto> columnInfoDtoList;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnInfoDto> getColumnInfoDtoList() {
        return columnInfoDtoList;
    }

    public void setColumnInfoDtoList(List<ColumnInfoDto> columnInfoDtoList) {
        this.columnInfoDtoList = columnInfoDtoList;
    }
}
