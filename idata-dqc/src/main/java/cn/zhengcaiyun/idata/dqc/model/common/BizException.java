package cn.zhengcaiyun.idata.dqc.model.common;

public class BizException extends RuntimeException {
    private int code;
    private String msg;

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public BizException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
