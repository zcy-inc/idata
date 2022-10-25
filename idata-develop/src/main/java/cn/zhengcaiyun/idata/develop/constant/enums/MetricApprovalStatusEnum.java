package cn.zhengcaiyun.idata.develop.constant.enums;

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
}
