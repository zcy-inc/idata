//package cn.zhengcaiyun.idata.connector.connection;
//
//import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
//import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.LinkedList;
//import java.util.Objects;
//
//import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
//import static com.google.common.base.Preconditions.checkState;
//import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
//
//@Component
//public class MyHiveConnectionPool {
//
//    @Autowired
//    private SysConfigDao sysConfigDao;
//
//    // 存储连接池
//    private LinkedList<Connection> connections = new LinkedList<>();
//
//    public MyHiveConnectionPool() {
//        SysConfig hiveConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("hive-info"))).orElse(null);
//        checkState(Objects.nonNull(hiveConfig), "数据源连接信息不正确");
//        String jdbcUrl = hiveConfig.getValueOne();
//        for (int i = 0; i < 10; i++) {
//            try {
//                long s1 = System.currentTimeMillis();
//                Connection conn = DriverManager.getConnection(jdbcUrl);
//                long s2 = System.currentTimeMillis();
//                connections.add(conn);
//                System.out.println("init:" + (s2 - s1));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Connection getConnection() {
//        return connections.removeFirst();
//    }
//
//    public void returnConnection(Connection connection) {
//        connections.add(connection);
//    }
//
//
//}
//
