package cn.zhengcaiyun.idata.commons.enums;

public enum DriverTypeEnum {

    MySQL("mysql","com.mysql.jdbc.Driver", DataSourceTypeEnum.mysql),
    Doris("mysql","com.mysql.jdbc.Driver", DataSourceTypeEnum.doris),
    DorisSql("mysql","com.mysql.jdbc.Driver", DataSourceTypeEnum.doris),
    Hive("hive2","org.apache.hive.jdbc.HiveDriver", DataSourceTypeEnum.hive),
    Postgres("postgresql","org.postgresql.Driver", DataSourceTypeEnum.postgresql),
    Phoenix("phoenix","", DataSourceTypeEnum.phoenix),
    Kafka("kafka","", DataSourceTypeEnum.kafka),
    Elasticsearch("elasticsearch","", DataSourceTypeEnum.elasticsearch),
    MSSQL("mssql","com.microsoft.sqlserver.jdbc.SQLServerDriver", DataSourceTypeEnum.mssql);

    public String urlType;
    public String driverName;
    public DataSourceTypeEnum dataSourceTypeEnum;

    DriverTypeEnum(String urlType, String driverName, DataSourceTypeEnum dataSourceTypeEnum) {
        this.urlType = urlType;
        this.driverName = driverName;
        this.dataSourceTypeEnum = dataSourceTypeEnum;
    }

    public static DriverTypeEnum ofDataSourceTypeEnum (DataSourceTypeEnum dataSourceTypeEnum) {
        for (DriverTypeEnum type : DriverTypeEnum.values()) {
            if (type.dataSourceTypeEnum == dataSourceTypeEnum) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid type : " + dataSourceTypeEnum);
    }
}
