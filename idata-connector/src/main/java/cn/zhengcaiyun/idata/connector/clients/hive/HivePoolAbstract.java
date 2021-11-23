package cn.zhengcaiyun.idata.connector.clients.hive;

import cn.zhengcaiyun.idata.connector.clients.hive.util.Pool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class HivePoolAbstract extends Pool<Jive> {

    public HivePoolAbstract() {
        super();
    }

    public HivePoolAbstract(GenericObjectPoolConfig poolConfig, PooledObjectFactory<Jive> factory) {
        super(poolConfig, factory);
    }

    @Override
    protected void returnBrokenResource(Jive resource) {
        super.returnBrokenResource(resource);
    }

    @Override
    protected void returnResource(Jive resource) {
        super.returnResource(resource);
    }

}
