package cn.zhengcaiyun.idata.develop.dto.job;

public class CycleJobDependencyDto {

    /**
     * 是否循环依赖
     */
    private boolean isCycle = false;

    /**
     * 涉及到的循环依赖id
     */
    private Long cycleJobId;

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }

    public Long getCycleJobId() {
        return cycleJobId;
    }

    public void setCycleJobId(Long cycleJobId) {
        this.cycleJobId = cycleJobId;
    }
}
