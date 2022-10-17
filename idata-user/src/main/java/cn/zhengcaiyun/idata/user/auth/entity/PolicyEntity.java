package cn.zhengcaiyun.idata.user.auth.entity;

import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthEffectEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;

import java.util.List;
import java.util.Set;

public class PolicyEntity {
    /**
     * 授权作用：allow：允许，deny：拒绝
     */
    private AuthEffectEnum effect;

    /**
     * 授权操作：read：读，write：写
     */
    private Set<AuthActionEnum> actions;

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
    private List<ResourceEntity> resources;

    public AuthEffectEnum getEffect() {
        return effect;
    }

    public void setEffect(AuthEffectEnum effect) {
        this.effect = effect;
    }

    public Set<AuthActionEnum> getActions() {
        return actions;
    }

    public void setActions(Set<AuthActionEnum> actions) {
        this.actions = actions;
    }

    public AuthResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(AuthResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public List<ResourceEntity> getResources() {
        return resources;
    }

    public void setResources(List<ResourceEntity> resources) {
        this.resources = resources;
    }
}
