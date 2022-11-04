package cn.zhengcaiyun.idata.user.dto.auth;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableAuthResourceDto {
    public static final String SYMBOL_ALL_TABLES = "*";
    /**
     * 库
     */
    private String db;
    /**
     * 表
     */
    private List<String> tables;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public Boolean includeAllTables() {
        if (CollectionUtils.isEmpty(tables)) {
            return Boolean.FALSE;
        }
        return tables.contains(SYMBOL_ALL_TABLES);
    }
}
