package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.connector.clients.hive.commands.BasicCommands;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BinaryJive implements BasicCommands, Closeable {
    protected Connection client = null;
    private String jdbcUrl;
    private String username;
    private String password;

    public BinaryJive(final String jdbcUrl, final String username, final String password) throws SQLException {
        client = DriverManager.getConnection(jdbcUrl, username, password);
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public Connection getClient() {
        return client;
    }

    public boolean isConnected() throws SQLException {
        return client.isValid(10);
    }

    public boolean isValid(int timeout) throws SQLException {
        return client.isValid(timeout);
    }

    public void connect() {
        //可扩展，此处连接不像jedis一样需要验证权限、选择db等操作
    }

    public void disconnect() throws SQLException {
        client.close();
    }

    @Override
    public void shutdown() throws SQLException {
        disconnect();
        // 扩展，除了关闭hive的连接外还可以操作释放其他资源
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void reconnect() throws SQLException {
        client = DriverManager.getConnection(jdbcUrl, username, password);
    }

    public boolean isClosed() throws SQLException {
        return client.isClosed();
    }
}
