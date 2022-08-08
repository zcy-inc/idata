package cn.zhengcaiyun.idata.connector.common.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateFactory {

    public static JdbcTemplate getJdbcTemplate(String jdbcUrl, String username, String password) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        return new JdbcTemplate(new HikariDataSource(hikariConfig));
    }


}
