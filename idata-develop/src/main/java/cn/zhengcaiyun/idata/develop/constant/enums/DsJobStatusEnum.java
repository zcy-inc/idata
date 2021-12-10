package cn.zhengcaiyun.idata.develop.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum DsJobStatusEnum {

    READY(1, new String[]{"submit success"}, new String[]{"SUBMITTED_SUCCESS"}, "等待运行"),

    RUNNING(2, new String[]{"running"}, new String[]{"RUNNING_EXECUTION"}, "运行中"),

    FAIL(6, new String[]{"failure"}, new String[]{"FAILURE"}, "失败"),

    SUCCESS(7, new String[]{"success", "forced success"},  new String[]{"SUCCESS", "FORCED_SUCCESS"}, "成功"),

    OTHER(-1, new String[]{"ready pause", "pause", "ready stop", "stop", "need fault tolerance", "kill", "waiting thread",
            "waiting depend node complete", "delay execution"},
            new String[]{"READY_PAUSE", "PAUSE", "READY_STOP", "STOP", "NEED_FAULT_TOLERANCE", "KILL", "WAITING_THREAD",
            "WAITING_DEPEND", "DELAY_EXECUTION"},"其他"),
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
}
