package cn.zhengcaiyun.idata.user.dto.auth;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import org.springframework.beans.BeanUtils;

public class AuthResourceDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 授权记录id
     */
    private Long authRecordId;

    /**
     * 授权策略记录id
     */
    private Long policyRecordId;

    /**
     * 资源类型：tables：表
     */
    private AuthResourceTypeEnum resourceType;

    /**
     * <p>资源</p>
     * 资源类型为表时，数据结构：
     * <pre><code>
     *     {
     *          "db": "", //库
     *          "tables": [ //表
     *              ""
     *          ]
     *     }
     * </code></pre>
     */
    private String resources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthRecordId() {
        return authRecordId;
    }

    public void setAuthRecordId(Long authRecordId) {
        this.authRecordId = authRecordId;
    }

    public Long getPolicyRecordId() {
        return policyRecordId;
    }

    public void setPolicyRecordId(Long policyRecordId) {
        this.policyRecordId = policyRecordId;
    }

    public AuthResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(AuthResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public static AuthResourceDto from(AuthResource authResource) {
        AuthResourceDto dto = new AuthResourceDto();
        BeanUtils.copyProperties(authResource, dto);
        return dto;
    }

    public AuthResource toModel() {
        AuthResource authResource = new AuthResource();
        BeanUtils.copyProperties(this, authResource);
        return authResource;
    }
}
