package cn.zhengcaiyun.idata.develop.condition.dag;

/**
 * @description: dag信息
 * @author: yangjianhua
 * @create: 2021-09-15 16:07
 **/
public class DAGInfoCondition {

    /**
     * 数仓分层
     */
    private String dwLayerCode;
    /**
     * 环境
     */
    private String environment;

    /**
     * 状态，1启用，0停用
     */
    private Integer status;

    public String getDwLayerCode() {
        return dwLayerCode;
    }

    public void setDwLayerCode(String dwLayerCode) {
        this.dwLayerCode = dwLayerCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}