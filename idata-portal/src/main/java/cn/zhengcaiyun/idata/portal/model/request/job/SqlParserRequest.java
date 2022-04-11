package cn.zhengcaiyun.idata.portal.model.request.job;

public class SqlParserRequest {

    private String sql;
    private String env;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
