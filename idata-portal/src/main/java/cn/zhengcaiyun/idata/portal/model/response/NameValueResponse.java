package cn.zhengcaiyun.idata.portal.model.response;

public class NameValueResponse<T> {

    public NameValueResponse() {
    }

    public NameValueResponse(String name, T value) {
        this.name = name;
        this.value = value;
    }

    private String name;

    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
