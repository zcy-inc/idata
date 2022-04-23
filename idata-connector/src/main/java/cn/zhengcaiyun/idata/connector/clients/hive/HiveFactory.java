package cn.zhengcaiyun.idata.connector.clients.hive;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.List;

public class HiveFactory implements PooledObjectFactory<Jive> {

    private ConnectInfo connectInfo;

    public HiveFactory(ConnectInfo connectInfo) {
        this.connectInfo = connectInfo;
    }

    @Override
    public void activateObject(PooledObject<Jive> pooledJedis) throws Exception {
        // 根据功能描述加了如下功能，但是会拖累整个接口速度。本地测试占用160ms左右
        BinaryJive jedis = pooledJedis.getObject();
        jedis.reconnect();
    }

    @Override
    public void destroyObject(PooledObject<Jive> pooledJedis) throws Exception {
        final BinaryJive jedis = pooledJedis.getObject();
        if (jedis.isClosed()) {
            jedis.shutdown();
        }
    }

    @Override
    public PooledObject<Jive> makeObject() throws Exception {

        final Jive jedis = new Jive(connectInfo.getJdbc(), connectInfo.getUsername(), connectInfo.getPassword());
        jedis.connect();
        return new DefaultPooledObject<>(jedis);
    }

    @Override
    public boolean validateObject(PooledObject<Jive> pooledJedis) {
        Jive jive = pooledJedis.getObject();
        try {
            jive.testConnection();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    public void passivateObject(PooledObject<Jive> p) throws Exception {

    }
}
