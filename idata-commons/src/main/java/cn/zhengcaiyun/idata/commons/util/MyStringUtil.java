package cn.zhengcaiyun.idata.commons.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanqian
 * @date 2022/8/24 5:39 PM
 * @description
 */
public class MyStringUtil {

    public static String getFileName(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        int i = path.lastIndexOf("/");
        if (i < 0) {
            return null;
        }
        return path.substring(i + 1);
    }

    public static String removeFileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        int i = fileName.lastIndexOf(".");
        if (i < 0) {
            return fileName;
        }
        return fileName.substring(0, i);
    }

    public static void main(String[] args) {
        System.out.println(removeFileSuffix("1661334747963_udf-1.0-jar-with-dependencies"));
    }
}
