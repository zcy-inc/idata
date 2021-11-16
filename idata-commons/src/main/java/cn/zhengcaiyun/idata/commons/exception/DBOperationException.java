package cn.zhengcaiyun.idata.commons.exception;

/**
 * @description: sql执行异常
 * @author: yangjianhua
 * @create: 2021-07-01 09:57
 **/
public class DBOperationException extends RuntimeException {

    public DBOperationException(String message) {
        super(message);
    }

    public DBOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
