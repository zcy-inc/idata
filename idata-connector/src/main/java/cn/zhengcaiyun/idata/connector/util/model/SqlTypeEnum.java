package cn.zhengcaiyun.idata.connector.util.model;

public enum SqlTypeEnum {
    CREATE_TABLE,
    CREATE_TABLE_AS_SELECT,
    CREATE_TABLE_AS_LIKE,
    DROP_TABLE,

    //DML
//    QUERY,
    SELECT,
    //    DELETE,
//    UPDATE,
    INSERT_VALUES,
    INSERT_SELECT,

    TRUNCATE_TABLE,
//    MULTI_INSERT,

    UNKOWN;

    SqlTypeEnum() {
    }


}
