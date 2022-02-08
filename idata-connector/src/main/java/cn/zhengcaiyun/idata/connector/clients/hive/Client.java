package cn.zhengcaiyun.idata.connector.clients.hive;


import org.apache.hive.jdbc.HiveConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Client extends HiveConnection {

    public Client(String uri, Properties info) throws SQLException {
        super(uri, info);
    }

    public void disconnect() throws SQLException {
        super.close();
    }
}
