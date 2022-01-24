package cn.zhengcaiyun.idata.connector.spi.resouce.manage.model.response;

/**
 * sample:
 *              {
 *                 "id":"application_1636461038777_71071",
 *                 "user":"root",
 *                 "name":"livy-session-40",
 *                 "queue":"root.users.root",
 *                 "state":"FINISHED",
 *                 "finalStatus":"SUCCEEDED",
 *                 "progress":100,
 *                 "trackingUI":"History",
 *                 "trackingUrl":"http://bigdata-master3.cai-inc.com:8088/proxy/application_1636461038777_71071/",
 *                 "diagnostics":"",
 *                 "clusterId":1636461038777,
 *                 "applicationType":"SPARK",
 *                 "applicationTags":"livy-session-40-dcshxfzf",
 *                 "priority":0,
 *                 "startedTime":1638251589798,
 *                 "launchTime":1638251590237,
 *                 "finishedTime":1638252540598,
 *                 "elapsedTime":950800,
 *                 "amContainerLogs":"http://bigdata-worker02.cai-inc.com:8042/node/containerlogs/container_1636461038777_71071_01_000001/root",
 *                 "amHostHttpAddress":"bigdata-worker02.cai-inc.com:8042",
 *                 "allocatedMB":-1,
 *                 "allocatedVCores":-1,
 *                 "reservedMB":-1,
 *                 "reservedVCores":-1,
 *                 "runningContainers":-1,
 *                 "memorySeconds":2938475,
 *                 "vcoreSeconds":1335,
 *                 "queueUsagePercentage":0,
 *                 "clusterUsagePercentage":0,
 *                 "resourceSecondsMap":{
 *                     "entry":{
 *                         "key":"vcores",
 *                         "value":"1335"
 *                     }
 *                 },
 *                 "preemptedResourceMB":0,
 *                 "preemptedResourceVCores":0,
 *                 "numNonAMContainerPreempted":0,
 *                 "numAMContainerPreempted":0,
 *                 "preemptedMemorySeconds":0,
 *                 "preemptedVcoreSeconds":0,
 *                 "preemptedResourceSecondsMap":{
 *
 *                 },
 *                 "logAggregationStatus":"SUCCEEDED",
 *                 "unmanagedApplication":false,
 *                 "amNodeLabelExpression":"",
 *                 "timeouts":{
 *                     "timeout":[
 *                         {
 *                             "type":"LIFETIME",
 *                             "expiryTime":"UNLIMITED",
 *                             "remainingTimeInSeconds":-1
 *                         }
 *                     ]
 *                 }
 *             }
 */
public class YarnAppResourceResponse {
    private String id;
    private String user;
    private String name;
    private String queue;
    private String state;
    private String finalStatus;
    private Integer progress;
    private String trackingUrl;
    private String applicationType;
    private Integer priority;
    private Long startedTime;
    private Long launchTime;
    private Long finishedTime;
    private Long elapsedTime;
    private Long allocatedMB;
    private Integer allocatedVCores;
    private Long vcoreSeconds;
    private Long memorySeconds;

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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Long startedTime) {
        this.startedTime = startedTime;
    }

    public Long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Long launchTime) {
        this.launchTime = launchTime;
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

    public Long getAllocatedMB() {
        return allocatedMB;
    }

    public void setAllocatedMB(Long allocatedMB) {
        this.allocatedMB = allocatedMB;
    }

    public Integer getAllocatedVCores() {
        return allocatedVCores;
    }

    public void setAllocatedVCores(Integer allocatedVCores) {
        this.allocatedVCores = allocatedVCores;
    }

    public Long getVcoreSeconds() {
        return vcoreSeconds;
    }

    public void setVcoreSeconds(Long vcoreSeconds) {
        this.vcoreSeconds = vcoreSeconds;
    }

    public Long getMemorySeconds() {
        return memorySeconds;
    }

    public void setMemorySeconds(Long memorySeconds) {
        this.memorySeconds = memorySeconds;
    }
}
