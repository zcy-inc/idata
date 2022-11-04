package cn.zhengcaiyun.idata.portal.schedule;

import cn.zhengcaiyun.idata.user.dal.dao.UacUserTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserTokenDynamicSqlSupport.uacUserToken;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author zhanqian
 * @date 2022/9/30 10:08 AM
 * @description
 */
@Component
public class TokenInvalidSchedule {

    private static final String COLUMN_SECURITY_CRON = "0 0 1 * * ?";

    @Autowired
    private UacUserTokenDao uacUserTokenDao;

    @Scheduled(cron = COLUMN_SECURITY_CRON)
    public void tokenInvalid()  {
        uacUserTokenDao.update(c -> c.set(uacUserToken.del).equalTo(1)
                .where(uacUserToken.del, isNotEqualTo(1)));
    }
}
