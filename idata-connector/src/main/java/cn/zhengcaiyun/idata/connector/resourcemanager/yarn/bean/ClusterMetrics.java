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

package cn.zhengcaiyun.idata.connector.resourcemanager.yarn.bean;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 15:19
 **/
public class ClusterMetrics {

    /**
     * The number of applications submitted
     */
    private Integer appsSubmitted;

    /**
     * The number of applications completed
     */
    private Integer appsCompleted;

    /**
     * The number of applications pending
     */
    private Integer appsPending;

    /**
     * The number of applications running
     */
    private Integer appsRunning;

    /**
     * The number of applications failed
     */
    private Integer appsFailed;

    /**
     * The number of applications killed
     */
    private Integer appsKilled;

    /**
     * The amount of memory reserved in MB
     */
    private Long reservedMB;

    /**
     * The amount of memory available in MB
     */
    private Long availableMB;

    /**
     * The amount of memory allocated in MB
     */
    private Long allocatedMB;

    /**
     * The amount of total memory in MB
     */
    private Long totalMB;

    /**
     * The number of reserved virtual cores
     */
    private Long reservedVirtualCores;

    /**
     * The number of available virtual cores
     */
    private Long availableVirtualCores;

    /**
     * The number of allocated virtual cores
     */
    private Long allocatedVirtualCores;

    /**
     * The total number of virtual cores
     */
    private Long totalVirtualCores;

    /**
     * The number of containers allocated
     */
    private Integer containersAllocated;

    /**
     * The number of containers reserved
     */
    private Integer containersReserved;

    /**
     * The number of containers pending
     */
    private Integer containersPending;

    /**
     * The total number of nodes
     */
    private Integer totalNodes;

    /**
     * The number of active nodes
     */
    private Integer activeNodes;

    /**
     * The number of lost nodes
     */
    private Integer lostNodes;

    /**
     * The number of unhealthy nodes
     */
    private Integer unhealthyNodes;

    /**
     * The number of nodes being decommissioned
     */
    private Integer decommissioningNodes;

    /**
     * The number of nodes decommissioned
     */
    private Integer decommissionedNodes;

    /**
     * The number of nodes rebooted
     */
    private Integer rebootedNodes;

    /**
     * The number of nodes shut down
     */
    private Integer shutdownNodes;

    public Integer getAppsSubmitted() {
        return appsSubmitted;
    }

    public void setAppsSubmitted(Integer appsSubmitted) {
        this.appsSubmitted = appsSubmitted;
    }

    public Integer getAppsCompleted() {
        return appsCompleted;
    }

    public void setAppsCompleted(Integer appsCompleted) {
        this.appsCompleted = appsCompleted;
    }

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

    public Long getReservedMB() {
        return reservedMB;
    }

    public void setReservedMB(Long reservedMB) {
        this.reservedMB = reservedMB;
    }

    public Long getAvailableMB() {
        return availableMB;
    }

    public void setAvailableMB(Long availableMB) {
        this.availableMB = availableMB;
    }

    public Long getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(Long allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public Long getTotalMB() {
        return totalMB;
    }

    public void setTotalMB(Long totalMB) {
        this.totalMB = totalMB;
    }

    public Long getReservedVirtualCores() {
        return reservedVirtualCores;
    }

    public void setReservedVirtualCores(Long reservedVirtualCores) {
        this.reservedVirtualCores = reservedVirtualCores;
    }

    public Long getAvailableVirtualCores() {
        return availableVirtualCores;
    }

    public void setAvailableVirtualCores(Long availableVirtualCores) {
        this.availableVirtualCores = availableVirtualCores;
    }

    public Long getAllocatedVirtualCores() {
        return allocatedVirtualCores;
    }

    public void setAllocatedVirtualCores(Long allocatedVirtualCores) {
        this.allocatedVirtualCores = allocatedVirtualCores;
    }

    public Long getTotalVirtualCores() {
        return totalVirtualCores;
    }

    public void setTotalVirtualCores(Long totalVirtualCores) {
        this.totalVirtualCores = totalVirtualCores;
    }

    public Integer getContainersAllocated() {
        return containersAllocated;
    }

    public void setContainersAllocated(Integer containersAllocated) {
        this.containersAllocated = containersAllocated;
    }

    public Integer getContainersReserved() {
        return containersReserved;
    }

    public void setContainersReserved(Integer containersReserved) {
        this.containersReserved = containersReserved;
    }

    public Integer getContainersPending() {
        return containersPending;
    }

    public void setContainersPending(Integer containersPending) {
        this.containersPending = containersPending;
    }

    public Integer getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(Integer totalNodes) {
        this.totalNodes = totalNodes;
    }

    public Integer getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(Integer activeNodes) {
        this.activeNodes = activeNodes;
    }

    public Integer getLostNodes() {
        return lostNodes;
    }

    public void setLostNodes(Integer lostNodes) {
        this.lostNodes = lostNodes;
    }

    public Integer getUnhealthyNodes() {
        return unhealthyNodes;
    }

    public void setUnhealthyNodes(Integer unhealthyNodes) {
        this.unhealthyNodes = unhealthyNodes;
    }

    public Integer getDecommissioningNodes() {
        return decommissioningNodes;
    }

    public void setDecommissioningNodes(Integer decommissioningNodes) {
        this.decommissioningNodes = decommissioningNodes;
    }

    public Integer getDecommissionedNodes() {
        return decommissionedNodes;
    }

    public void setDecommissionedNodes(Integer decommissionedNodes) {
        this.decommissionedNodes = decommissionedNodes;
    }

    public Integer getRebootedNodes() {
        return rebootedNodes;
    }

    public void setRebootedNodes(Integer rebootedNodes) {
        this.rebootedNodes = rebootedNodes;
    }

    public Integer getShutdownNodes() {
        return shutdownNodes;
    }

    public void setShutdownNodes(Integer shutdownNodes) {
        this.shutdownNodes = shutdownNodes;
    }
}
