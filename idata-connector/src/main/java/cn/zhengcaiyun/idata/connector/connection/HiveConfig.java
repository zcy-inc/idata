//package cn.zhengcaiyun.idata.connector.connection;
//
//import org.apache.tomcat.jdbc.pool.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import org.apache.tomcat.jdbc.pool.DataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//@Configuration
//public class HiveConfig {
//    private static final Logger logger = LoggerFactory.getLogger(HiveConfig.class);
//
//    @Autowired
//    private Environment env;
//
//    @Bean(name = "hiveJdbcDataSource")
//    @Qualifier("hiveJdbcDataSource")
//    public DataSource dataSource() {
//        DataSource dataSource = new DataSource();
//        dataSource.setUrl("jdbc:hive2://172.29.108.184:10000/default");
//        dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
////        dataSource.setUsername(env.getProperty("hive.username"));
////        dataSource.setPassword(env.getProperty("hive.password"));
//        logger.debug("Hive DataSource");
//        return dataSource;
//    }
//
//    @Bean(name = "hiveJdbcTemplate")
//    public JdbcTemplate hiveJdbcTemplate(@Qualifier("hiveJdbcDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}