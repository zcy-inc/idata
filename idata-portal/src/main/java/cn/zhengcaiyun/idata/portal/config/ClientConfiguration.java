package cn.zhengcaiyun.idata.portal.config;

import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
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

    @Autowired
    private SystemConfigApi systemConfigApi;

    @Bean
    public HivePool getHivePool() {
        ConfigDto systemConfigByKey = systemConfigApi.getSystemConfigByKey("hive-info");
        String jdbcUrl = systemConfigByKey.getValueOne().get("hive-info").getConfigValue();
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setJdbc(jdbcUrl);
        return null;
    }

}
