package cn.zhengcaiyun.idata.connector.clients.hive.pool;

import cn.zhengcaiyun.idata.connector.clients.hive.exception.JiveConnectionException;
import cn.zhengcaiyun.idata.connector.clients.hive.exception.JiveException;
import cn.zhengcaiyun.idata.connector.clients.hive.exception.JiveExhaustedPoolException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Closeable;
import java.util.NoSuchElementException;

public abstract class Pool<T> implements Closeable {

    protected GenericObjectPool<T> internalPool;

    public Pool() {
    }

    public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
        initPool(poolConfig, factory);
    }

    @Override
    public void close() {
        destroy();
    }

    public boolean isClosed() {
        return this.internalPool.isClosed();
    }

    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<>(factory, poolConfig);
    }

    public T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (NoSuchElementException nse) {
            if (null == nse.getCause()) { // The exception was caused by an exhausted pool
                throw new JiveExhaustedPoolException(
                        "Could not get a resource since the pool is exhausted", nse);
            }
            // Otherwise, the exception was caused by the implemented activateObject() or ValidateObject()
            throw new JiveException("Could not get a resource from the pool", nse);
        } catch (Exception e) {
            throw new JiveConnectionException("Could not get a resource from the pool", e);
        }
    }

    protected void returnResourceObject(final T resource) {
        if (resource == null) {
            return;
        }
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new JiveException("Could not return the resource to the pool", e);
        }
    }

    protected void returnBrokenResource(final T resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    protected void returnResource(final T resource) {
        if (resource != null) {
            returnResourceObject(resource);
        }
    }

    public void destroy() {
        closeInternalPool();
    }

    protected void returnBrokenResourceObject(final T resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new JiveException("Could not return the broken resource to the pool", e);
        }
    }

    protected void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new JiveException("Could not destroy the pool", e);
        }
    }

    /**
     * Returns the number of instances currently borrowed from this pool.
     *
     * @return The number of instances currently borrowed from this pool, -1 if
     * the pool is inactive.
     */
    public int getNumActive() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }

    /**
     * Returns the number of instances currently idle in this pool.
     *
     * @return The number of instances currently idle in this pool, -1 if the
     * pool is inactive.
     */
    public int getNumIdle() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }

    /**
     * Returns an estimate of the number of threads currently blocked waiting for
     * a resource from this pool.
     *
     * @return The number of threads waiting, -1 if the pool is inactive.
     */
    public int getNumWaiters() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }

    /**
     * Returns the mean waiting time spent by threads to obtain a resource from
     * this pool.
     *
     * @return The mean waiting time, in milliseconds, -1 if the pool is
     * inactive.
     */
    public long getMeanBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }

    /**
     * Returns the maximum waiting time spent by threads to obtain a resource
     * from this pool.
     *
     * @return The maximum waiting time, in milliseconds, -1 if the pool is
     * inactive.
     */
    public long getMaxBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }

    private boolean poolInactive() {
        return this.internalPool == null || this.internalPool.isClosed();
    }

    public void addObjects(int count) {
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new JiveException("Error trying to add idle objects", e);
        }
    }
}
