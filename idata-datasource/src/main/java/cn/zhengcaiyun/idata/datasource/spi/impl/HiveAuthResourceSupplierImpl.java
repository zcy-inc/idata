package cn.zhengcaiyun.idata.datasource.spi.impl;

import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.user.spi.AuthResourceSupplier;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HiveAuthResourceSupplierImpl implements AuthResourceSupplier {
    private final SystemConfigApi systemConfigApi;

    @Autowired
    public HiveAuthResourceSupplierImpl(SystemConfigApi systemConfigApi) {
        this.systemConfigApi = systemConfigApi;
    }

    @Override
    public List<String> supplyResources(JSONObject paramJson) {
        String dbName = paramJson.getString("dbName");
        if (StringUtils.isBlank(dbName))
            return Lists.newArrayList();

        ConfigDto systemConfigByKey = systemConfigApi.getSystemConfigByKey("hive-info");
        String jdbcUrl = systemConfigByKey.getValueOne().get("hive-info").getConfigValue();

        List<String> tableList = new ArrayList<>();
        try (Connection client = DriverManager.getConnection(jdbcUrl, null, null);
             Statement statement = client.createStatement()) {
            statement.execute("use " + dbName);
            try (ResultSet rs = statement.executeQuery("show tables")) {
                while (rs.next()) {
                    String tableName = dbName + "." + rs.getString("tab_name");
                    tableList.add(tableName.toLowerCase());
                }
            }
        } catch (SQLException ex) {
            throw new GeneralException("getTableNameList fail!", ex);
        }
        return tableList;
    }
}
