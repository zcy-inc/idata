package cn.zhengcaiyun.idata.user.auth;

import com.google.common.base.Joiner;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AuthRet {
    private List<String> denyResources;

    private AuthRet(List<String> denyResources) {
        this.denyResources = denyResources;
    }

    public static AuthRet of(List<String> denyResources) {
        return new AuthRet(denyResources);
    }

    public boolean deny() {
        return !CollectionUtils.isEmpty(denyResources);
    }

    public boolean allow() {
        return !deny();
    }

    public String getDenyMsg() {
        if (deny()) {
            return String.format("没有权限使用【%s】,请申请权限！", Joiner.on(", ").join(denyResources));
        }
        return "";
    }
}
