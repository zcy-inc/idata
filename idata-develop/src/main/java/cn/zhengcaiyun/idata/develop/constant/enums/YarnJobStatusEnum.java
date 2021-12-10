package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 映射业务的yarn state枚举
 */
public enum YarnJobStatusEnum {

    PENDING(1,  new String[]{"NEW", "NEW_SAVING", "SUBMITTED", "ACCEPTED"}, "等待运行"),

    RUNNING(2, new String[]{"RUNNING"}, "运行中"),

    FAIL(6,  new String[]{"FAILURE", "KILLED"}, "失败"),

    SUCCESS(7, new String[]{"FINISHED"}, "成功"),

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
}
