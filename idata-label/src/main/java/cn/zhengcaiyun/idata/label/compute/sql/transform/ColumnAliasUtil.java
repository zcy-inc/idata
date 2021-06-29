package cn.zhengcaiyun.idata.label.compute.sql.transform;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-29 14:54
 **/
public class ColumnAliasUtil {
    public static final String aliasWithQuotes(String alias) {
        return "\"" + alias + "\"";
    }
}
