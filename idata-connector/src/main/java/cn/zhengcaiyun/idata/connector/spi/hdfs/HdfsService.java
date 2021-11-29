package cn.zhengcaiyun.idata.connector.spi.hdfs;

import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Service
public class HdfsService implements InitializingBean, DisposableBean {

    @Value("${hdfs.nameservices}")
    private String HDFS_NAMESERVICES;
    @Value("${hdfs.nn1}")
    private String HDFS_NN1;
    @Value("${hdfs.nn2}")
    private String HDFS_NN2;
    @Value("${hdfs.basePath}")
    private String HDFS_BASE_PATH;
    @Value("${hdfs.user}")
    private String HDFS_USER;

    private FileSystem fs;
    private String HDFS_CSV_PATH;
    private String HDFS_RESOURCE_PATH;
    private String HDFS_UDF_PATH;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration cfg = new Configuration();

        cfg.set("fs.defaultFS", "hdfs://" + HDFS_NAMESERVICES);
        cfg.set("dfs.nameservices", HDFS_NAMESERVICES);
        cfg.set("dfs.ha.namenodes." + HDFS_NAMESERVICES, "nn1,nn2");
        cfg.set("dfs.namenode.rpc-address." + HDFS_NAMESERVICES + ".nn1", HDFS_NN1);
        cfg.set("dfs.namenode.rpc-address." + HDFS_NAMESERVICES + ".nn2", HDFS_NN2);
        cfg.set("dfs.client.failover.proxy.provider." + HDFS_NAMESERVICES,
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        try {
            fs = FileSystem.get(URI.create(HDFS_BASE_PATH), cfg, HDFS_USER);
            HDFS_CSV_PATH = HDFS_BASE_PATH + "csv/";
            if (!fs.exists(new Path(HDFS_CSV_PATH))) {
                fs.mkdirs(new Path(HDFS_CSV_PATH));
            }
            HDFS_RESOURCE_PATH = HDFS_BASE_PATH + "resource/";
            if (!fs.exists(new Path(HDFS_RESOURCE_PATH))) {
                fs.mkdirs(new Path(HDFS_RESOURCE_PATH));
            }
            HDFS_UDF_PATH = HDFS_BASE_PATH + "udf/";
            if (!fs.exists(new Path(HDFS_UDF_PATH))) {
                fs.mkdirs(new Path(HDFS_UDF_PATH));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (fs != null) {
            fs.close();
            fs = null;
        }
    }

    public String getHdfsPrefix() {
        return "hdfs://" + HDFS_NAMESERVICES;
    }

    public void readFile(String pathString, OutputStream out) throws IOException {
        InputStream in = fs.open(new Path(pathString));
        IOUtils.copyBytes(in, out, 4096);
        in.close();
    }

    public void modifyFile(String pathString, String source) throws IOException {
        fs.delete(new Path(pathString), true);
        writeStringToFile(pathString, source);
    }

    public String uploadFileToCsv(InputStream inputStream, String originalName) {
        return uploadFile(inputStream, HDFS_CSV_PATH, originalName);
    }

    public String uploadFileToResource(InputStream inputStream, String originalName) {
        return uploadFile(inputStream, HDFS_RESOURCE_PATH, originalName);
    }

    public String uploadFileToUDF(InputStream inputStream, String originalName) {
        return uploadFile(inputStream, HDFS_UDF_PATH, originalName);
    }

    public String uploadTempFileToResource(String pathString, String source) throws IOException {
        String tempPathString = pathString.split("\\.")[0] + "_temp." + pathString.split("\\.")[1];
        Path hdfsTempFilePath = new Path(tempPathString);
        if (fs.exists(hdfsTempFilePath)) {
            fs.delete(hdfsTempFilePath, true);
        }
        writeStringToFile(tempPathString, source);
        return tempPathString;
    }

    private String uploadFile(InputStream inputStream, String filePath, String originalName) {
        String hdfsFilePath = filePath + System.currentTimeMillis() + "_" + originalName;
        try {
            IOUtils.copyBytes(inputStream, fs.create(new Path(hdfsFilePath)), 4096, true);
            return hdfsFilePath;
        } catch (IOException e) {
            throw new BizProcessException("", e);
        }
    }

    private void writeStringToFile(String pathString, String source) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes());
            IOUtils.copyBytes(in, fs.create(new Path(pathString)), 4096, true);
            in.close();
        } catch (IOException e) {
            throw new BizProcessException("", e);
        }
    }

    /**
     * 校验hdfs的udf路径是否正确（用于鉴权，防止下载其他路径敏感信息，此处留作权限扩展）
     * @param path
     * @return
     */
    public boolean checkUdfPath(String path) {
        return StringUtils.contains(path, HDFS_UDF_PATH);
    }

    /**
     * 下载资源
     * @param path
     * @return
     * @throws IOException
     */
    public FSDataInputStream open(String path) throws IOException {
        return fs.open(new Path(path));
    }
}
