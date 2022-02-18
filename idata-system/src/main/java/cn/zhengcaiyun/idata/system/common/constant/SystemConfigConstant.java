package cn.zhengcaiyun.idata.system.common.constant;

public interface SystemConfigConstant {
    // htool-config
    String KEY_HTOOL_CONFIG = "htool-config";
    String HTOOL_CONFIG_YARN_ADDR = "yarn.addr";

    // livy-config
    String KEY_LIVY_CONFIG = "livy-config";
    String LIVY_CONFIG_URL = "url";
    String LIVY_CONFIG_SESSION_MAX = "livy.sessionMax";

    // other-config
    String KEY_OTHER_CONFIG = "other-config";
    String OTHER_CONFIG_HDFS_NAMESERVICES = "hdfs.nameservices";
    String OTHER_CONFIG_HDFS_NN1 = "hdfs.nn1";
    String OTHER_CONFIG_HDFS_NN2 = "hdfs.nn2";
    String OTHER_CONFIG_HDFS_USER = "hdfs.user";
}
