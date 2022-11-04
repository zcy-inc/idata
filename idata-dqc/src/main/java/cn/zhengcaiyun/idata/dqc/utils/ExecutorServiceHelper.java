package cn.zhengcaiyun.idata.dqc.utils;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class ExecutorServiceHelper {
    private static final ExecutorService ASYNC_CACHE_EXECUTOR = new ThreadPoolExecutor(4, 32, 10L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), new ThreadPoolExecutor.CallerRunsPolicy());

    public static <T> CompletableFuture<T> execute(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, ASYNC_CACHE_EXECUTOR);
    }

    public static void submit(Runnable commond) {
        ASYNC_CACHE_EXECUTOR.submit(commond);
    }

}
