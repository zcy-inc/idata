package cn.zhengcaiyun.idata.portal.model.request.job;

import java.util.Date;

/**
 * @author zhanqian
 * @date 2022/9/27 4:35 PM
 * @description
 */
public class ExtendActivityEndRequest {

    /**
     * 作业id
     */
    private Long id;

    /**
     * 过期时间
     */
    private Date activityEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }
}
