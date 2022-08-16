package cn.zhengcaiyun.idata.dqc.model.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户表(User)实体类
 *
 * @author makejava
 * @since 2022-07-15 17:35:18
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -94670300998239679L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 是否删除(1:是,0:否)
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
    /**
     * 用户名
     */
    private String username;
    /**
     * 是否系统管理员(0:否,1:是,2:其他系统管理员)
     */
    private Integer sysAdmin;
    /**
     * 认证方式
     */
    private String authType;
    /**
     * 注册密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 工号
     */
    private String employeeId;
    /**
     * 部门
     */
    private String department;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 头像信息
     */
    private String avatar;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;

}

