package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 映射业务的yarn state枚举
 */
public enum YarnJobStatusEnum {

    // 目前同DS READY/
    PENDING(1,  new String[]{"SUBMITTED", "ACCEPTED"}, "等待运行",  new String[]{"SUBMITTED", "ACCEPTED"},  new String[]{"UNDEFINED"}),

    RUNNING(2, new String[]{"RUNNING"}, "运行中",  new String[]{"RUNNING"},  new String[]{"UNDEFINED"}),

    FAIL(6,  new String[]{"FAILED", "KILLED"}, "失败",  new String[]{"FINISHED", "FAILED", "KILLED"},  new String[]{"FAILED", "KILLED"}),

    SUCCESS(7, new String[]{"FINISHED", "SUCCEEDED"}, "成功",  new String[]{"FINISHED", "SUCCEEDED"},  new String[]{"SUCCEEDED"}),

    OTHER(-1, new String[]{"NEW", "NEW_SAVING"}, "其他", new String[]{"NEW", "NEW_SAVING"}, new String[]{})

    ;

    public Integer value;

    /**
     * 别名描述状态
     */
    public String[] yarnEnumCodes;

    public String description;

    /**
     * 别名描述状态
     */
    public String[] states;

    /**
     * 别名描述状态
     */
    public String[] finalStatus;

    YarnJobStatusEnum(Integer value, String[] dsEnumCodes, String description, String[] states, String[] finalStatus) {
        this.value = value;
        this.yarnEnumCodes = dsEnumCodes;
        this.description = description;
        this.states = states;
        this.finalStatus = finalStatus;
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

    public static YarnJobStatusEnum getByValue(Integer value) {
        for (YarnJobStatusEnum yarnEnumCodes : YarnJobStatusEnum.values()) {
            if (yarnEnumCodes.value == value) {
                return yarnEnumCodes;
            }
        }
        return null;
    }

    public static YarnJobStatusEnum getByStateAndFinalStatus(String state, String finalStatus) {
        for (YarnJobStatusEnum yarnJobStatusEnum : YarnJobStatusEnum.values()) {
            List<String> statesList = Arrays.asList(yarnJobStatusEnum.states);
            List<String> finalStatusList = Arrays.asList(yarnJobStatusEnum.finalStatus);
            if (statesList.contains(state) && finalStatusList.contains(finalStatus)) {
                return yarnJobStatusEnum;
            }
        }
        return null;
    }

    public static Integer getValueByStateAndFinalStatus(String state, String finalStatus) {
        YarnJobStatusEnum yarnJobStatusEnum = getByStateAndFinalStatus(state, finalStatus);
        if (yarnJobStatusEnum != null) {
            return yarnJobStatusEnum.value;
        }
        return null;
    }


}
