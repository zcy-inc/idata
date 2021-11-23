package cn.zhengcaiyun.idata.connector.clients.hive.exception;

public class JiveExhaustedPoolException extends JiveException {

    public JiveExhaustedPoolException(String message) {
        super(message);
    }

    public JiveExhaustedPoolException(Throwable e) {
        super(e);
    }

    public JiveExhaustedPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}