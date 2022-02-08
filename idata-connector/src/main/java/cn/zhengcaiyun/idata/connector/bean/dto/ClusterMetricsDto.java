/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.connector.bean.dto;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 17:36
 **/
public class ClusterMetricsDto {
    /**
     * 已提交作业
     */
    private Integer submittedJobs;

    /**
     * 已完成作业
     */
    private Integer completedJobs;

    /**
     * 正等待作业
     */
    private Integer pendingJobs;

    /**
     * 正运行作业
     */
    private Integer runningJobs;

    /**
     * 已失败作业
     */
    private Integer failedJobs;

    /**
     * 已kill作业
     */
    private Integer killedJobs;

    /**
     * 预留内存，单位：MB
     */
    private Long reservedMem;

    /**
     * 有效内存，单位：MB
     */
    private Long availableMem;

    /**
     * 已分配内存，单位：MB
     */
    private Long allocatedMem;

    /**
     * 总内存，单位：MB
     */
    private Long totalMem;

    /**
     * The number of reserved virtual cores
     */
    private Long reservedVCores;

    /**
     * The number of available virtual cores
     */
    private Long availableVCores;

    /**
     * The number of allocated virtual cores
     */
    private Long allocatedVCores;

    /**
     * The total number of virtual cores
     */
    private Long totalVCores;

    public Integer getSubmittedJobs() {
        return submittedJobs;
    }

    public void setSubmittedJobs(Integer submittedJobs) {
        this.submittedJobs = submittedJobs;
    }

    public Integer getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(Integer completedJobs) {
        this.completedJobs = completedJobs;
    }

    public Integer getPendingJobs() {
        return pendingJobs;
    }

    public void setPendingJobs(Integer pendingJobs) {
        this.pendingJobs = pendingJobs;
    }

    public Integer getRunningJobs() {
        return runningJobs;
    }

    public void setRunningJobs(Integer runningJobs) {
        this.runningJobs = runningJobs;
    }

    public Integer getFailedJobs() {
        return failedJobs;
    }

    public void setFailedJobs(Integer failedJobs) {
        this.failedJobs = failedJobs;
    }

    public Integer getKilledJobs() {
        return killedJobs;
    }

    public void setKilledJobs(Integer killedJobs) {
        this.killedJobs = killedJobs;
    }

    public Long getReservedMem() {
        return reservedMem;
    }

    public void setReservedMem(Long reservedMem) {
        this.reservedMem = reservedMem;
    }

    public Long getAvailableMem() {
        return availableMem;
    }

    public void setAvailableMem(Long availableMem) {
        this.availableMem = availableMem;
    }

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

    public Long getReservedVCores() {
        return reservedVCores;
    }

    public void setReservedVCores(Long reservedVCores) {
        this.reservedVCores = reservedVCores;
    }

    public Long getAvailableVCores() {
        return availableVCores;
    }

    public void setAvailableVCores(Long availableVCores) {
        this.availableVCores = availableVCores;
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
}
