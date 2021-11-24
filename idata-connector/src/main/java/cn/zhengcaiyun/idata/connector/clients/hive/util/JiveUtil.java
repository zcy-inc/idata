package cn.zhengcaiyun.idata.connector.clients.hive.util;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JiveUtil {

    /**
     * 美化大小展示
     * @param bytes
     * @return
     */
    public static String beautifySize(long bytes) {
        if (bytes < 0) {
            return null;
        } else if (bytes < 1024 * 1024) {
            return (bytes / 1024) + "KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return (bytes / 1024 / 1024) + "MB";
        }
        return (bytes / 1024 / 1024 / 1024) + "GB";
    }
}
