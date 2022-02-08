package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DsJobStatusEnum {

    READY(1, new String[]{"submit success", "pause", "delay execution"}, new String[]{"SUBMITTED_SUCCESS", "PAUSE", "DELAY_EXECUTION"}, "等待运行"),

    RUNNING(2, new String[]{"running", "ready pause", "ready stop"}, new String[]{"RUNNING_EXECUTION", "READY_PAUSE", "READY_STOP"}, "运行中"),

    FAIL(6, new String[]{"failure"}, new String[]{"FAILURE"}, "失败"),

    SUCCESS(7, new String[]{"success", "forced success"},  new String[]{"SUCCESS", "FORCED_SUCCESS"}, "成功"),

    OTHER(-1, new String[]{ "stop", "need fault tolerance", "kill", "waiting thread", "waiting depend node complete"},
            new String[]{ "STOP", "NEED_FAULT_TOLERANCE", "KILL", "WAITING_THREAD", "WAITING_DEPEND"},"其他"),
    ;

    public Integer value;

    /**
     * dolphinSchedule 描述状态
     */
    public String[] dsDescriptions;

    /**
     * dolphinSchedule 别名描述状态
     */
    public String[] dsEnumCodes;

    public String description;

    DsJobStatusEnum(Integer value, String[] dsDescriptions, String[] dsEnumCodes, String description) {
        this.value = value;
        this.dsDescriptions = dsDescriptions;
        this.dsEnumCodes = dsEnumCodes;
        this.description = description;
    }

    public static Integer getValueByDsDescription(String code) {
        for (DsJobStatusEnum dsJobStatusEnum : DsJobStatusEnum.values()) {
            for (String dsCode : dsJobStatusEnum.dsDescriptions) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return dsJobStatusEnum.value;
                }
            }
        }
        return null;
    }

    public static DsJobStatusEnum getByDsEnumCode(String code) {
        for (DsJobStatusEnum dsJobStatusEnum : DsJobStatusEnum.values()) {
            for (String dsCode : dsJobStatusEnum.dsEnumCodes) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return dsJobStatusEnum;
                }
            }
        }
        return null;
    }

    public static List<String> getDsDescriptionsByValue(Integer value) {
        for (DsJobStatusEnum dsJobStatusEnum : DsJobStatusEnum.values()) {
            if (dsJobStatusEnum.value == value) {
                return Arrays.asList(dsJobStatusEnum.dsDescriptions);
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getDsEnumCodeByValue(Integer value) {
        for (DsJobStatusEnum dsJobStatusEnum : DsJobStatusEnum.values()) {
            if (dsJobStatusEnum.value == value) {
                return Arrays.asList(dsJobStatusEnum.dsEnumCodes);
            }
        }
        return new ArrayList<>();
    }
}
