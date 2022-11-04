package cn.zhengcaiyun.idata.connector.jdbc.model;

public enum DatasourceDriver {
    MYSQL("com.mysql.jdbc.Driver"),
    PRESTO("com.facebook.presto.jdbc.PrestoDriver"),
    POSTPRESQL("org.postgresql.Driver"),
    CLICKHOUSE("ru.yandex.clickhouse.ClickHouseDriver");

    String driver;

    public String getDriver() {
        return driver;
    }

    DatasourceDriver(String driver) {
        this.driver = driver;
    }
}
