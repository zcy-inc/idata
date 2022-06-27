//package cn.zhengcaiyun.dqc.config;
//
//import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
//import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
//import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
//import cn.zhengcaiyun.idata.system.dto.ConfigDto;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//@Configuration
//public class ClientConfiguration {
//    @Autowired
//    private SystemConfigApi systemConfigApi;
//
//    @Bean
//    public HivePool getHivePool() {
//        ConfigDto systemConfigByKey = systemConfigApi.getSystemConfigByKey("hive-info");
//        String jdbcUrl = systemConfigByKey.getValueOne().get("hive-info").getConfigValue();
//        ConnectInfo connectInfo = new ConnectInfo();
//        connectInfo.setJdbc(jdbcUrl);
//
//        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
//        genericObjectPoolConfig.setTestOnBorrow(true);
//        genericObjectPoolConfig.setTestOnReturn(true);
//        genericObjectPoolConfig.setTestOnCreate(true);
//        return new HivePool(connectInfo, genericObjectPoolConfig);
//    }
//
//}
