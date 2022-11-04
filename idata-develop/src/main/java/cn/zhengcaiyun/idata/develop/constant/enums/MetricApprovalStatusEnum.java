package cn.zhengcaiyun.idata.develop.constant.enums;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public enum MetricApprovalStatusEnum {
    WAIT_APPROVE(1, "待审批"),
    APPROVED(2, "已审批"),
    RETREATED(3, "已撤回"),
    REJECTED(4, "已驳回"),
    ;

    private final int val;
    private final String desc;

    MetricApprovalStatusEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<Integer, MetricApprovalStatusEnum> codeMap = Maps.newHashMap();

    static {
        Arrays.stream(MetricApprovalStatusEnum.values())
                .forEach(enumObj -> {
                    codeMap.put(enumObj.val, enumObj);
                });
    }

    public static Optional<MetricApprovalStatusEnum> getEnum(Integer val) {
        if (Objects.isNull(val)) return Optional.empty();
        return Optional.ofNullable(codeMap.get(val));
    }
}
