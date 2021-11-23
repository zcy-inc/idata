package cn.zhengcaiyun.idata.connector.clients.hive.commands;

import java.sql.SQLException;

public interface BasicCommands {

    /**
     * Stop all the client. Perform a SAVE (if one save point is configured).
     * Flush the append only file if AOF is enabled
     * quit the server
     * @return only in case of error.
     */
    void shutdown() throws SQLException;


}
