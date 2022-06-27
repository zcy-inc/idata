package cn.zhengcaiyun.idata.dqc.service;

import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsFileInfo;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:zheng
 * Date:2022/6/22
 */
@Service
public class HiveInfoService {
    @Autowired
    private HdfsService hdfsService;

    public void getPartition(String partition) {
        String[] arr = new String[]{"yyyy", "yyyy-MM", "yyyy/MM", "yyyyMM",
                "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd"};

    }

    public HdfsFileInfo getTableInfo(String databaseName, String tableName, String partition) {
        String hdfsPath = "/hive/"+databaseName+".db/"+tableName+"/"+partition;
        return hdfsService.getFileInfo(hdfsPath);
    }
}
