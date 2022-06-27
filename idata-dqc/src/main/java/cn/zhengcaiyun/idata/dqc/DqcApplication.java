package cn.zhengcaiyun.idata.dqc;


import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.system.IDataSystem;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * 数据质量启动入口
 */
@SpringBootApplication(scanBasePackages={"cn.zhengcaiyun.idata"})
@MapperScan({"cn.zhengcaiyun.idata.*.dal.dao","cn.zhengcaiyun.idata.dqc.dao"})
@EnableScheduling
public class DqcApplication {
    private static final Logger log = LoggerFactory.getLogger(DqcApplication.class);

    public static void main( String[] args ) {
        SpringApplication.run(DqcApplication.class, args);
        log.info("Dqc server started successfully...");
    }

}
