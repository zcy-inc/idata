package cn.zhengcaiyun.idata.user.auth.entity;

import java.util.Set;

public class TableResourceEntity implements ResourceEntity {

    public static final String SYMBOL_ALL_TABLES = "*";
    /**
     * 库
     */
    private String db;
    /**
     * 表
     */
    private Set<String> tables;

    public TableResourceEntity(String db, Set<String> tables) {
        this.db = db;
        this.tables = tables;
    }

    @Override
    public boolean verifyResource(String resourceIdentifier) {
        String resource = resourceIdentifier.toLowerCase();
        if (tables.contains(resource)) {
            return true;
        } else if (tables.contains(SYMBOL_ALL_TABLES)) {
            return resource.startsWith(db + ".");
        }
        return false;
    }
}
