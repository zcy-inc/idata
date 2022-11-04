package cn.zhengcaiyun.idata.connector.jdbc.model;

import cn.zhengcaiyun.idata.connector.jdbc.model.DatasourceDriver;

public class DatasourceParam {
    private DatasourceDriver type;
    private String username;
    private String password;
    private String jdbcUrl;

    public DatasourceParam(DatasourceDriver type, String username, String password, String jdbcUrl) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.jdbcUrl = jdbcUrl;
    }

    public DatasourceDriver getType() {
        return type;
    }

    public void setType(DatasourceDriver type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
