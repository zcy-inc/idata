package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.connector.clients.hive.exception.JiveException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.annotation.PreDestroy;

public class HivePool extends HivePoolAbstract {

    public static final int DEFAULT_TIMEOUT = 2000;

    public HivePool(ConnectInfo connectInfo) {
        this(new GenericObjectPoolConfig(), connectInfo);
    }

    public HivePool(final GenericObjectPoolConfig poolConfig, ConnectInfo connectInfo) {
        super(poolConfig, new HiveFactory(connectInfo));
    }

    @PreDestroy
    public void cleanAllConnection() {
        System.out.println("clean all valid hive connection");
    }

    @Override
    public Jive getResource() {
        Jive jedis = super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }

    @Override
    protected void returnBrokenResource(final Jive resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override
    protected void returnResource(final Jive resource) {
        if (resource != null) {
            try {
                returnResourceObject(resource);
            } catch (Exception e) {
                returnBrokenResource(resource);
                throw new JiveException("Resource is returned to the pool as broken", e);
            }
        }
    }

}
