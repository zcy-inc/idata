package cn.zhengcaiyun.idata.connector.jdbc.model;

public class IdataException extends RuntimeException {
    public IdataException(String message) {
        super(message);
    }

    public IdataException(String message, Throwable cause) {
        super(message, cause);
    }
}
