package cn.zhengcaiyun.idata.connector.clients.hive.exception;

public class JiveConnectionException  extends JiveException {
    private static final long serialVersionUID = 3878126572474819403L;

    public JiveConnectionException(String message) {
        super(message);
    }

    public JiveConnectionException(Throwable cause) {
        super(cause);
    }

    public JiveConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}