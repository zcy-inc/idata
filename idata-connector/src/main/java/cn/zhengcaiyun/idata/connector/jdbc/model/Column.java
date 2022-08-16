package cn.zhengcaiyun.idata.connector.jdbc.model;


import java.io.Serializable;


public class Column implements Serializable {

    private static final long serialVersionUID = -2003588313309755191L;
    private String name;
    private String type; // 数据库原始类型
    private String comments;

    public Column() {
    }

    public Column(String name, String type, String comments) {
        this.name = name;
        this.type = type;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
