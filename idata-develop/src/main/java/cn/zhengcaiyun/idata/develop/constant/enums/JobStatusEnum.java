package cn.zhengcaiyun.idata.develop.constant.enums;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

public enum JobStatusEnum {

    READY(1, new String[]{"submit success"}, "等待运行"),

    RUNNING(2, new String[]{"running"}, "运行中"),

    FAIL(6, new String[]{"failure"}, "失败"),

    SUCCESS(7, new String[]{"success"}, "成功"),

    OTHER(-1, new String[]{"ready pause", "pause", "ready stop", "stop", "need fault tolerance", "kill", "waiting thread",
            "waiting depend node complete", "delay execution", "forced success"}, "其他"),
    ;

    public Integer value;

    /**
     * dolphinSchedule 描述状态
     */
    public String[] dsCodes;

    public String description;

    JobStatusEnum(Integer value, String[] dsCodes, String description) {
        this.value = value;
        this.dsCodes = dsCodes;
        this.description = description;
    }

    public static Integer getValueByDsCode(String code) {
        for (JobStatusEnum jobStatusEnum : JobStatusEnum.values()) {
            for (String dsCode : jobStatusEnum.dsCodes) {
                if (StringUtils.equalsIgnoreCase(dsCode, code)) {
                    return jobStatusEnum.value;
                }
            }
        }
        return null;
    }
}
