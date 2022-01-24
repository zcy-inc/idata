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
 * @description: yarn 集群应用
 * 作为查询列表时，暂未加 resourceRequests 属性
 * @author: yangjianhua
 * @create: 2021-12-09 16:26
 **/
public class ClusterApp {

    /**
     * The application id
     */
    private String id;

    /**
     * The user who started the application
     */
    private String user;

    /**
     * The application name
     */
    private String name;

    /**
     * The queue the application was submitted to
     */
    private String queue;

    /**
     * The application state according to the ResourceManager - valid values are members of the YarnApplicationState enum: NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED
     */
    private String state;

    /**
     * The final status of the application if finished - reported by the application itself - valid values are the members of the FinalApplicationStatus enum: UNDEFINED, SUCCEEDED, FAILED, KILLED
     */
    private String finalStatus;

    /**
     * The progress of the application as a percent
     */
    private Float progress;

    /**
     * Where the tracking url is currently pointing - History (for history server) or ApplicationMaster
     */
    private String trackingUI;

    /**
     * The web URL that can be used to track the application
     */
    private String trackingUrl;

    /**
     * Detailed diagnostics information
     */
    private String diagnostics;

    /**
     * The cluster id
     */
    private Long clusterId;

    /**
     * The application type
     */
    private String applicationType;

    /**
     * Comma separated tags of an application
     */
    private String applicationTags;

    /**
     * Priority of the submitted application
     */
    private String priority;

    /**
     * The time in which application started (in ms since epoch)
     */
    private Long startedTime;

    /**
     * The time in which the application finished (in ms since epoch)
     */
    private Long finishedTime;

    /**
     * The elapsed time since the application started (in ms)
     */
    private Long elapsedTime;

    /**
     * The URL of the application master container logs
     */
    private String amContainerLogs;

    /**
     * The nodes http address of the application master
     */
    private String amHostHttpAddress;

    /**
     * The RPC address of the application master
     */
    private String amRPCAddress;

    /**
     * The sum of memory in MB allocated to the application’s running containers
     */
    private Integer allocatedMB;

    /**
     * The sum of virtual cores allocated to the application’s running containers
     */
    private Integer allocatedVCores;

    /**
     * The number of containers currently running for the application
     */
    private Integer runningContainers;

    /**
     * The amount of memory the application has allocated (megabyte-seconds)
     */
    private Long memorySeconds;

    /**
     * The amount of CPU resources the application has allocated (virtual core-seconds)
     */
    private Long vcoreSeconds;

    /**
     * The percentage of resources of the queue that the app is using
     */
    private Float queueUsagePercentage;

    /**
     * The percentage of resources of the cluster that the app is using
     */
    private Float clusterUsagePercentage;

    /**
     * Memory used by preempted container
     */
    private Long preemptedResourceMB;

    /**
     * Number of virtual cores used by preempted container
     */
    private Long preemptedResourceVCores;

    /**
     * Number of standard containers preempted
     */
    private Integer numNonAMContainerPreempted;

    /**
     * Number of application master containers preempted
     */
    private Integer numAMContainerPreempted;

    /**
     * Status of log aggregation - valid values are the members of the LogAggregationStatus enum: DISABLED, NOT_START, RUNNING, RUNNING_WITH_FAILURE, SUCCEEDED, FAILED, TIME_OUT
     */
    private String logAggregationStatus;

    /**
     * Is the application unmanaged
     */
    private Boolean unmanagedApplication;

    /**
     * Node Label expression which is used to identify the nodes on which application’s containers are expected to run by default
     */
    private String appNodeLabelExpression;

    /**
     * Node Label expression which is used to identify the node on which application’s AM container is expected to run
     */
    private String amNodeLabelExpression;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public String getTrackingUI() {
        return trackingUI;
    }

    public void setTrackingUI(String trackingUI) {
        this.trackingUI = trackingUI;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationTags() {
        return applicationTags;
    }

    public void setApplicationTags(String applicationTags) {
        this.applicationTags = applicationTags;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Long startedTime) {
        this.startedTime = startedTime;
    }

    public Long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Long finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getAmContainerLogs() {
        return amContainerLogs;
    }

    public void setAmContainerLogs(String amContainerLogs) {
        this.amContainerLogs = amContainerLogs;
    }

    public String getAmHostHttpAddress() {
        return amHostHttpAddress;
    }

    public void setAmHostHttpAddress(String amHostHttpAddress) {
        this.amHostHttpAddress = amHostHttpAddress;
    }

    public String getAmRPCAddress() {
        return amRPCAddress;
    }

    public void setAmRPCAddress(String amRPCAddress) {
        this.amRPCAddress = amRPCAddress;
    }

    public Integer getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(Integer allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public Integer getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(Integer allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }

    public Integer getRunningContainers() {
        return runningContainers;
    }

    public void setRunningContainers(Integer runningContainers) {
        this.runningContainers = runningContainers;
    }

    public Long getMemorySeconds() {
        return memorySeconds;
    }

    public void setMemorySeconds(Long memorySeconds) {
        this.memorySeconds = memorySeconds;
    }

    public Long getVcoreSeconds() {
        return vcoreSeconds;
    }

    public void setVcoreSeconds(Long vcoreSeconds) {
        this.vcoreSeconds = vcoreSeconds;
    }

    public Float getQueueUsagePercentage() {
        return queueUsagePercentage;
    }

    public void setQueueUsagePercentage(Float queueUsagePercentage) {
        this.queueUsagePercentage = queueUsagePercentage;
    }

    public Float getClusterUsagePercentage() {
        return clusterUsagePercentage;
    }

    public void setClusterUsagePercentage(Float clusterUsagePercentage) {
        this.clusterUsagePercentage = clusterUsagePercentage;
    }

    public Long getPreemptedResourceMB() {
        return preemptedResourceMB;
    }

    public void setPreemptedResourceMB(Long preemptedResourceMB) {
        this.preemptedResourceMB = preemptedResourceMB;
    }

    public Long getPreemptedResourceVCores() {
        return preemptedResourceVCores;
    }

    public void setPreemptedResourceVCores(Long preemptedResourceVCores) {
        this.preemptedResourceVCores = preemptedResourceVCores;
    }

    public Integer getNumNonAMContainerPreempted() {
        return numNonAMContainerPreempted;
    }

    public void setNumNonAMContainerPreempted(Integer numNonAMContainerPreempted) {
        this.numNonAMContainerPreempted = numNonAMContainerPreempted;
    }

    public Integer getNumAMContainerPreempted() {
        return numAMContainerPreempted;
    }

    public void setNumAMContainerPreempted(Integer numAMContainerPreempted) {
        this.numAMContainerPreempted = numAMContainerPreempted;
    }

    public String getLogAggregationStatus() {
        return logAggregationStatus;
    }

    public void setLogAggregationStatus(String logAggregationStatus) {
        this.logAggregationStatus = logAggregationStatus;
    }

    public Boolean getUnmanagedApplication() {
        return unmanagedApplication;
    }

    public void setUnmanagedApplication(Boolean unmanagedApplication) {
        this.unmanagedApplication = unmanagedApplication;
    }

    public String getAppNodeLabelExpression() {
        return appNodeLabelExpression;
    }

    public void setAppNodeLabelExpression(String appNodeLabelExpression) {
        this.appNodeLabelExpression = appNodeLabelExpression;
    }

    public String getAmNodeLabelExpression() {
        return amNodeLabelExpression;
    }

    public void setAmNodeLabelExpression(String amNodeLabelExpression) {
        this.amNodeLabelExpression = amNodeLabelExpression;
    }
}
