package cn.zhengcaiyun.idata.client.hive;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.thrift.TException;

import java.util.Arrays;
import java.util.List;


public class TestMetastore {

    public static void main(String[] args) throws TException {
        HiveMetaStoreClient hiveMetaStoreClient = setHiveMetaStoreConf();
        String dbName = "dws";
        // 由数据库的名称获取数据库的对象(一些基本信息)
        Database database= hiveMetaStoreClient.getDatabase(dbName);
        // 根据数据库名称获取所有的表名
        List<String> tablesList = hiveMetaStoreClient.getAllTables(dbName);
        // 由表名和数据库名称获取table对象(能获取列、表信息)
        Table table= hiveMetaStoreClient.getTable(dbName, "tmp_sync_hive");

        createTable(hiveMetaStoreClient);
        // 获取所有的列对象
        List<FieldSchema> fieldSchemaList= table.getSd().getCols();
        // 关闭当前连接
        hiveMetaStoreClient.close();
    }

    /**
     * 功能描述:
     *          设置HiveJDBC的数据源
     */
    public static HiveMetaStoreClient setHiveMetaStoreConf() {
        //HiveMetaStore的客户端
        HiveMetaStoreClient hiveMetaStoreClient = null;
        HiveConf hiveConf = new HiveConf();
        hiveConf.set("hive.metastore.uris", "thrift://172.29.108.183:9083");
        hiveConf.set("javax.jdo.option.ConnectionUserName", "hdfs");
        hiveConf.set("hive.metastore.warehouse.dir", "/tmp");
        try {
            //设置hiveMetaStore服务的地址
            new HiveMetaStoreClient(hiveConf);;
            hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
            //当前版本2.3.4与集群3.0版本不兼容，加入此设置
//            hiveMetaStoreClient.setMetaConf("hive.metastore.client.capability.check","false");
        } catch (MetaException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return hiveMetaStoreClient;
    }

    private static void createTable(HiveMetaStoreClient hiveMetaStoreClient) throws TException {
        Table table = new Table();
        table.setDbName("dws");
        table.setTableName("tmp_test_metastore");
        table.setPartitionKeys(Arrays.asList(new FieldSchema("partcol", "int", null)));
        table.setSd(new StorageDescriptor());
        table.getSd().setCols(Arrays.asList(new FieldSchema("id", "int", null), new FieldSchema("name", "string", null)));
        table.getSd().setInputFormat("org.apache.hadoop.mapred.TextInputFormat");
        table.getSd().setOutputFormat("org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat");
        table.getSd().setSerdeInfo(new SerDeInfo());
        table.getSd().getSerdeInfo().setSerializationLib("org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe");
        hiveMetaStoreClient.createTable(table);
    }
}
//hdfs:
//        nameservices: nameservice1
//        nn1: 172.29.108.184:8020
//        nn2: 172.29.108.185:8020
//        basePath: /tmp/shiyin/
//        user: hdfs
