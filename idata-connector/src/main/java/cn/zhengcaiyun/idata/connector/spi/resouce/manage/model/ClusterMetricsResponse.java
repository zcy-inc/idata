package cn.zhengcaiyun.idata.connector.spi.resouce.manage.model;

public class ClusterMetricsResponse {

    private Integer appsPending;
    private Integer appsRunning;
    /**
     * 集群启动后累加
     */
    private Long appsCompleted;
    private Integer appsFailed;
    private Integer appsKilled;
    /**
     * 等于其他apps指标加和
     */
    private Long appsSubmitted;
    /**
     *集群资源
     */
    private Long allocatedMB;
    private Long availableMB;
    private Long reservedMB;
    /**
     *等于其他内存指标加和
     */
    private Long totalMB;
    private Integer allocatedVirtualCores;
    private Integer availableVirtualCores;
    private Integer reservedVirtualCores;
    /**
     *等于其他cpu核数加和
     */
    private Integer totalVirtualCores;

    public Integer getAppsPending() {
        return appsPending;
    }

    public void setAppsPending(Integer appsPending) {
        this.appsPending = appsPending;
    }

    public Integer getAppsRunning() {
        return appsRunning;
    }

    public void setAppsRunning(Integer appsRunning) {
        this.appsRunning = appsRunning;
    }

    public Long getAppsCompleted() {
        return appsCompleted;
    }

    public void setAppsCompleted(Long appsCompleted) {
        this.appsCompleted = appsCompleted;
    }

    public Integer getAppsFailed() {
        return appsFailed;
    }

    public void setAppsFailed(Integer appsFailed) {
        this.appsFailed = appsFailed;
    }

    public Integer getAppsKilled() {
        return appsKilled;
    }

    public void setAppsKilled(Integer appsKilled) {
        this.appsKilled = appsKilled;
    }

    public Long getAppsSubmitted() {
        return appsSubmitted;
    }

    public void setAppsSubmitted(Long appsSubmitted) {
        this.appsSubmitted = appsSubmitted;
    }

    public Long getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(Long allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public Long getAvailableMB() {
        return availableMB;
    }

    public void setAvailableMB(Long availableMB) {
        this.availableMB = availableMB;
    }

    public Long getReservedMB() {
        return reservedMB;
    }

    public void setReservedMB(Long reservedMB) {
        this.reservedMB = reservedMB;
    }

    public Long getTotalMB() {
        return totalMB;
    }

    public void setTotalMB(Long totalMB) {
        this.totalMB = totalMB;
    }

    public Integer getAllocatedVirtualCores() {
        return allocatedVirtualCores;
    }

    public void setAllocatedVirtualCores(Integer allocatedVirtualCores) {
        this.allocatedVirtualCores = allocatedVirtualCores;
    }

    public Integer getAvailableVirtualCores() {
        return availableVirtualCores;
    }

    public void setAvailableVirtualCores(Integer availableVirtualCores) {
        this.availableVirtualCores = availableVirtualCores;
    }

    public Integer getReservedVirtualCores() {
        return reservedVirtualCores;
    }

    public void setReservedVirtualCores(Integer reservedVirtualCores) {
        this.reservedVirtualCores = reservedVirtualCores;
    }

    public Integer getTotalVirtualCores() {
        return totalVirtualCores;
    }

    public void setTotalVirtualCores(Integer totalVirtualCores) {
        this.totalVirtualCores = totalVirtualCores;
    }
}
