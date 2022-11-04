package cn.zhengcaiyun.idata.dqc.model.common;

import lombok.Data;

import java.util.Date;

/**
 * Created by user on 2016/4/5.
 */
@Data
public class BaseEntity {
    public Long id;
    /**
     * 是否删除，0否，1是
     */
    private Integer del;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改者
     */
    private String editor;
    /**
     * 修改时间
     */
    private Date editTime;
}
