package cn.zhengcaiyun.idata.commons.exception;

public class NameDuplicateException extends RuntimeException {

    public NameDuplicateException(String message) {
        super(message);
    }

    public NameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
