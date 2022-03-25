package cn.zhengcaiyun.idata.connector.clients.hive.exception;

public class JiveException extends RuntimeException {
    private static final long serialVersionUID = -2946266495682282677L;

    public JiveException(String message) {
        super(message);
    }

    public JiveException(Throwable e) {
        super(e);
    }

    public JiveException(String message, Throwable cause) {
        super(message, cause);
    }
}