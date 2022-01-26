package cn.zhengcaiyun.idata.portal.config;

import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkState;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Configuration
public class ClientConfiguration {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Bean
    public HivePool getHivePool() {
        SysConfig hiveConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("hive-info"))).orElse(null);
        checkState(Objects.nonNull(hiveConfig), "数据源连接信息不正确");
        String jdbcUrl = hiveConfig.getValueOne();
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setJdbc(jdbcUrl);
        return new HivePool(connectInfo);
    }

}
