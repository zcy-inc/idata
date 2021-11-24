package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.sql.SQLException;

public class Test {

    //需要解决的问题：
    //1.空闲连接怎么保证正确？
    //  - testOnBorrow、testOnReturn、testWhileIdle
    //      - testOnBorrow可以实现，但是HiveFactory#validateObject()还是依靠HiveConnection本身的方法实现，此处可以待验证
    //      - testOnBorrow和testOnReturn在生产环境一般是不开启的，主要是性能考虑。失效连接主要通过testWhileIdle保证，如果获取到了不可用的数据库连接，一般由应用处理异常。
    //2.怎么归还连接
    //  - Jive#close()
    //3.连接池连接主动关闭？不关闭会连接泄露吗？
    //  - 方法1：将pool注入到spring，然后依靠销毁时机进行手动主动关闭
    //  - 方法2：依靠hiveService的bean的生命周期
    //      - 缺点：其他bean也要这么做，很麻烦，并且jivePool不共用

    public static void main(String[] args) throws SQLException {
//        testRename();
        testExist();
    }

    public static void testRename() {
        Jive jive = null;
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc("jdbc:hive2://172.29.108.184:10000/default");
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            HivePool hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            System.out.println("testRename：" + jive.rename("dws", "tmp_sync_hive222", "tmp_sync_hive"));
        } finally {
            jive.close(); // ！重要，使用完后归还
        }
    }


    public static void testExist() {
        Jive jive = null;
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc("jdbc:hive2://172.29.108.184:10000/default");
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            HivePool hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            System.out.println("======" + jive.exist("dws", "tmp_sync_hive"));
        } finally {
            jive.close(); // ！重要，使用完后归还
        }
    }

    /**
     * 测试可靠性连接
     */
    public static void testReliableConnection() {
        Jive jive = null;
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc("jdbc:hive2://172.29.108.184:10000/default");
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            HivePool hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            jive.test();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jive.close(); // ！重要，使用完后归还
        }
    }

    /**
     * 测试连接池大小和功能
     */
    public void testPoolSize() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setJdbc("jdbc:hive2://172.29.108.184:10000/default");
        HivePool hivePool = new HivePool(connectInfo);
        for (int i = 0; i < 100; i++) {
            final int j = i;
            new Thread(() -> {
                Jive jive = hivePool.getResource();
                System.out.println("序号" + j + "， jive：" + jive + "， NumActive：" + hivePool.getNumActive());
                try {
                    Thread.sleep(30000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    jive.test();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    jive.close(); // ！重要，使用完后归还
                }
            }).start();
        }
    }
}
