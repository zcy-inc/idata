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
    PENDING(1,  "等待运行",  new String[]{"SUBMITTED", "ACCEPTED"},  new String[]{"UNDEFINED"}),

    RUNNING(2, "运行中",  new String[]{"RUNNING"},  new String[]{"UNDEFINED"}),

    FAIL(6,   "失败",  new String[]{"FINISHED", "FAILED", "KILLED"},  new String[]{"FAILED", "KILLED"}),

    SUCCESS(7,  "成功",  new String[]{"FINISHED", "SUCCEEDED"},  new String[]{"SUCCEEDED"}),

    OTHER(-1, "其他", new String[]{"NEW", "NEW_SAVING"}, new String[]{"UNDEFINED"})

    ;

    public Integer value;

    public String description;

    /**
     * 别名描述状态
     */
    public String[] states;

    /**
     * 别名描述状态
     */
    public String[] finalStatus;

    YarnJobStatusEnum(Integer value, String description, String[] states, String[] finalStatus) {
        this.value = value;
        this.description = description;
        this.states = states;
        this.finalStatus = finalStatus;
    }

    public static YarnJobStatusEnum getByFinalStatus(String code) {
        for (YarnJobStatusEnum dsJobStatusEnum : YarnJobStatusEnum.values()) {
            for (String dsCode : dsJobStatusEnum.finalStatus) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return dsJobStatusEnum;
                }
            }
        }
        return null;
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
