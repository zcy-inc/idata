package cn.zhengcaiyun.idata.connector.spi.hdfs;

import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import cn.zhengcaiyun.idata.system.common.constant.SystemConfigConstant;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Service
public class HdfsService {
    private static final Logger log = LoggerFactory.getLogger(HdfsService.class);

    @Value("${hdfs.basePath}")
    private String HDFS_BASE_PATH;

    @Autowired
    private SystemConfigService systemConfigService;

    public String getHdfsPrefix() {
        String nameService = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_OTHER_CONFIG, SystemConfigConstant.OTHER_CONFIG_HDFS_NAMESERVICES);
        return "hdfs://" + nameService;
    }

    public void readFile(String pathString, OutputStream out) throws IOException {
        try (FileSystem fs = createFileSystem(); InputStream in = fs.open(new Path(pathString))) {
            IOUtils.copyBytes(in, out, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyFile(String pathString, String source) throws IOException {
        try (FileSystem fs = createFileSystem()) {
            fs.delete(new Path(pathString), true);
        }
        writeStringToFile(pathString, source);
    }

    public String uploadFileToCsv(InputStream inputStream, String originalName) {
        String csvPath = HDFS_BASE_PATH + "csv/";
        return uploadFile(inputStream, csvPath, originalName);
    }

    public String uploadFileToResource(InputStream inputStream, String originalName) {
        String resourcePath = HDFS_BASE_PATH + "resource/";
        return uploadFile(inputStream, resourcePath, originalName);
    }

    public String uploadFileToUDF(InputStream inputStream, String originalName) {
        String udfPath = HDFS_BASE_PATH + "udf/";
        return uploadFile(inputStream, udfPath, originalName);
    }

    /**
     * 校验hdfs的udf路径是否正确（用于鉴权，防止下载其他路径敏感信息，此处留作权限扩展）
     * @param path
     * @return
     */
    public boolean checkUdfPath(String path) {
        String udfPath = HDFS_BASE_PATH + "udf/";
        // 为了兼容历史迁移数据，包括进resource目录
        return StringUtils.contains(path, udfPath) || StringUtils.contains(path, "resource");
    }

    private String uploadFile(InputStream inputStream, String directory, String originalName) {
        String hdfsFilePath = directory + System.currentTimeMillis() + "_" + originalName;
        try (FileSystem fs = createFileSystem()) {
            if (!fs.exists(new Path(directory))) {
                fs.mkdirs(new Path(directory));
            }
            IOUtils.copyBytes(inputStream, fs.create(new Path(hdfsFilePath)), 4096, true);
            return hdfsFilePath;
        } catch (IOException e) {
            throw new BizProcessException(e.getMessage(), e);
        }
    }

    private void writeStringToFile(String pathString, String source) {
        try (FileSystem fs = createFileSystem(); ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes())) {
            IOUtils.copyBytes(in, fs.create(new Path(pathString)), 4096, true);
        } catch (IOException e) {
            throw new BizProcessException(e.getMessage(), e);
        }
    }

    /**
     * 创建FS连接
     * @return
     */
    private FileSystem createFileSystem() {
        Configuration cfg = new Configuration();

        String nameService = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_OTHER_CONFIG, SystemConfigConstant.OTHER_CONFIG_HDFS_NAMESERVICES);
        String nn1 = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_OTHER_CONFIG, SystemConfigConstant.OTHER_CONFIG_HDFS_NN1);
        String nn2 = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_OTHER_CONFIG, SystemConfigConstant.OTHER_CONFIG_HDFS_NN2);
        String user = systemConfigService.getValueWithCommon(SystemConfigConstant.KEY_OTHER_CONFIG, SystemConfigConstant.OTHER_CONFIG_HDFS_USER);

        cfg.set("fs.defaultFS", "hdfs://" + nameService); //HDFS_NAMESERVICES
        cfg.set("dfs.nameservices", nameService);
        cfg.set("dfs.ha.namenodes." + nameService, "nn1,nn2");
        cfg.set("dfs.namenode.rpc-address." + nameService + ".nn1", nn1);
        cfg.set("dfs.namenode.rpc-address." + nameService + ".nn2", nn2);
        cfg.set("dfs.client.failover.proxy.provider." + nameService,
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create(HDFS_BASE_PATH), cfg, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fs;
    }

    public HiveTable getHiveTableInfo(String tableName, String hdfsPath){
        Path path = new Path(hdfsPath);
        try (FileSystem fs = createFileSystem()) {
            if (!fs.exists(path)) {
                return null;
            }
            FileStatus status = fs.getFileStatus(path);

            return new HiveTable(tableName, status.getAccessTime(),status.getModificationTime(),status.getBlockSize());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }


}
