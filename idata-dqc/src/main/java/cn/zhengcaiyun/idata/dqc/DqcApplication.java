package cn.zhengcaiyun.idata.dqc;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 数据质量启动入口
 */
@SpringBootApplication(scanBasePackages = {"cn.zhengcaiyun.idata"})
@MapperScan({"cn.zhengcaiyun.idata.*.dal.dao", "cn.zhengcaiyun.idata.dqc.dao"})
@EnableScheduling
public class DqcApplication {
    private static final Logger log = LoggerFactory.getLogger(DqcApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DqcApplication.class, args);
        log.info("Dqc server started successfully...");
    }

}
