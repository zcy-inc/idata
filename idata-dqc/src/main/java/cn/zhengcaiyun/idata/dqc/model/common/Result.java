package cn.zhengcaiyun.idata.dqc.model.common;

/**
 * Created by zheng on 16/6/12.
 */
public class Result<R> {
    private String message;
    private boolean success;
    private R data;

    public Result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public Result(String message, boolean success, R data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
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

    public static <R> Result<R> failureResult(String message) {
        return new Result<R>(message, false, null);
    }

}
