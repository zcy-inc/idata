package cn.zhengcaiyun.idata.commons.enums;

public enum DriverTypeEnum {
    MySQL("mysql","com.mysql.jdbc.Driver"),
    Doris("mysql","com.mysql.jdbc.Driver"),
    Hive("hive2","org.apache.hive.jdbc.HiveDriver"),
    Postgres("postgresql","org.postgresql.Driver");

    private String urlType;
    private String driverName;

    DriverTypeEnum(String urlType, String driverName) {
        this.urlType = urlType;
        this.driverName = driverName;
    }

    public String getUrlType() {
        return urlType;
    }

    public String getDriverName() {
        return driverName;
    }

    public static DriverTypeEnum of (String urlType) {
        for (DriverTypeEnum type : DriverTypeEnum.values()) {
            if (type.urlType.equals(urlType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid type : " + urlType);
    }
}
