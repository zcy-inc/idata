package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 映射业务的yarn state枚举
 */
public enum YarnJobStatusEnum {

    // 目前同DS READY/
    PENDING(1,  new String[]{"SUBMITTED", "ACCEPTED"}, "等待运行"),

    RUNNING(2, new String[]{"RUNNING"}, "运行中"),

    FAIL(6,  new String[]{"FAILURE", "KILLED"}, "失败"),

    SUCCESS(7, new String[]{"FINISHED"}, "成功"),

    OTHER(-1, new String[]{"NEW", "NEW_SAVING"}, "其他")

    ;

    public Integer value;

    /**
     * 别名描述状态
     */
    public String[] yarnEnumCodes;

    public String description;

    YarnJobStatusEnum(Integer value, String[] dsEnumCodes, String description) {
        this.value = value;
        this.yarnEnumCodes = dsEnumCodes;
        this.description = description;
    }

    public static Integer getValueByYarnEnumCode(String code) {
        for (YarnJobStatusEnum dsJobStatusEnum : YarnJobStatusEnum.values()) {
            for (String dsCode : dsJobStatusEnum.yarnEnumCodes) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return dsJobStatusEnum.value;
                }
            }
        }
        return null;
    }

    public static YarnJobStatusEnum getByYarnEnumCode(String code) {
        for (YarnJobStatusEnum dsJobStatusEnum : YarnJobStatusEnum.values()) {
            for (String dsCode : dsJobStatusEnum.yarnEnumCodes) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return dsJobStatusEnum;
                }
            }
        }
        return null;
    }

    public static List<String> getCodesByValue(Integer value) {
        for (YarnJobStatusEnum yarnEnumCodes : YarnJobStatusEnum.values()) {
            if (yarnEnumCodes.value == value) {
                return Arrays.asList(yarnEnumCodes.yarnEnumCodes);
            }
        }
        return new ArrayList<>();
    }


}
