package cn.zhengcaiyun.idata.label.compute.query.exception;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 16:22
 **/
public class ExecuteSqlException extends RuntimeException {

    public ExecuteSqlException(String message) {
        super(message);
    }

    public ExecuteSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
