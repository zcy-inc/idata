package cn.zhengcaiyun.idata.commons.exception;

/**
 * @description: sql执行异常
 * @author: yangjianhua
 * @create: 2021-07-01 09:57
 **/
public class BizProcessException extends GeneralException {

    public BizProcessException(String message) {
        super(message);
    }

    public BizProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
