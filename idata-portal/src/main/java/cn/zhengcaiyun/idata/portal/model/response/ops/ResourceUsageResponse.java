package cn.zhengcaiyun.idata.portal.model.response.ops;

/**
 * 资源使用情况
 */
public class ResourceUsageResponse {

    /**
     * 已分配内存，单位：MB
     */
    private Long allocatedMem;

    /**
     * 总内存，单位：MB
     */
    private Long totalMem;

    /**
     * 内存使用率
     */
    private String memUsageRate;

    /**
     * The number of allocated virtual cores
     */
    private Long allocatedVCores;

    /**
     * The total number of virtual cores
     */
    private Long totalVCores;

    /**
     * cpu使用率
     */
    private String vCoreUsageRate;

    public Long getAllocatedMem() {
        return allocatedMem;
    }

    public void setAllocatedMem(Long allocatedMem) {
        this.allocatedMem = allocatedMem;
    }

    public Long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(Long totalMem) {
        this.totalMem = totalMem;
    }

    public Long getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(Long allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }

    public Long getTotalVCores() {
        return totalVCores;
    }

    public void setTotalVCores(Long totalVCores) {
        this.totalVCores = totalVCores;
    }

    public String getMemUsageRate() {
        return memUsageRate;
    }

    public void setMemUsageRate(String memUsageRate) {
        this.memUsageRate = memUsageRate;
    }

    public String getvCoreUsageRate() {
        return vCoreUsageRate;
    }

    public void setvCoreUsageRate(String vCoreUsageRate) {
        this.vCoreUsageRate = vCoreUsageRate;
    }
}
