package cn.zhengcaiyun.idata.datasource.api.dto;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;

public class DataSourceDetailDto {

    private DataSourceTypeEnum dataSourceTypeEnum;

    private String name; //数据源名称

    private String dbName;

    private String host;

    private Integer port;

    private String jdbcUrl;

    private String userName;

    private String password;

    private DriverTypeEnum driverTypeEnum;

    public DataSourceTypeEnum getDataSourceTypeEnum() {
        return dataSourceTypeEnum;
    }

    public void setDataSourceTypeEnum(DataSourceTypeEnum dataSourceTypeEnum) {
        this.dataSourceTypeEnum = dataSourceTypeEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DriverTypeEnum getDriverTypeEnum() {
        return driverTypeEnum;
    }

    public void setDriverTypeEnum(DriverTypeEnum driverTypeEnum) {
        this.driverTypeEnum = driverTypeEnum;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}