package cn.zhengcaiyun.idata.mergedata.util;

import cn.zhengcaiyun.idata.mergedata.enums.MigrateItemEnum;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: 简单实现的限制用户访问次数限流器，非精确，并发情况下可能稍微超出限制次数，可根据后续需要改为更精确的实现；
 * 当前由于实际使用场景及前置的限流措施，该实现方案影响不大；
 * 访问次数限制策略：根据请求参数限制请求次数，时间段t内可以有n次不同的条件组合查询，相同的条件组合t时间段内可以多次查询。
 * 实现方案：使用redis的set集合存储查询条件组合的md5值，当set大小大于n时，限制新增查询条件组合的查询，t为一天内。
 * @author: yangjianhua
 * @create: 2021-03-12 16:27
 **/
@Component
public class MergeDataRequestLimiter {

    private final LoadingCache<MigrateItemEnum, AtomicBoolean> cache = Caffeine.newBuilder()
            .maximumSize(20)
            .expireAfterWrite(Duration.ofMinutes(3))
            .build(key -> load(key));

    private AtomicBoolean load(MigrateItemEnum key) {
        return new AtomicBoolean(false);
    }

    public synchronized Boolean exceed(MigrateItemEnum migrateItemEnum) {
        AtomicBoolean isExceed = cache.get(migrateItemEnum);
        if (isExceed.get()) {
            return Boolean.TRUE;
        } else {
            isExceed.set(Boolean.TRUE);
            return Boolean.FALSE;
        }
    }

}
