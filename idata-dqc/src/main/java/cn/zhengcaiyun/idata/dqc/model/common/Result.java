package cn.zhengcaiyun.idata.dqc.model.common;

/**
 * Created by zheng on 16/6/12.
 */
public class Result<R> {
    private String msg;
    private boolean success;
    private R data;

    public Result(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public Result(String msg, boolean success, R data) {
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String getmsg() {
        return msg;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static <R> Result<R> successResult() {
        return new Result<R>(null, true, null);
    }

    public static <R> Result<R> successResult(R data) {
        return new Result<R>(null, true, data);
    }

    public static <R> Result<R> failureResult(String msg) {
        return new Result<R>(msg, false, null);
    }

}
