package cn.zhengcaiyun.idata.develop.condition.job;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:20
 **/
public class JobExecuteConfigCondition {

    /**
     * 环境，为空时查询所有环境的配置，不为空时查询指定环境配置（单环境最多一条记录）
     */
    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}