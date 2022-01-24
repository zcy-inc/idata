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

package cn.zhengcaiyun.idata.connector.resourcemanager.yarn.agent.param;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-09 16:16
 **/
public class QueryClusterAppParam {
    /**
     * applications matching the given application states, specified as a comma-separated list.
     * enum: NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED
     */
    private String states;

    /**
     * the final status of the application - reported by the application itself
     * FinalApplicationStatus enum: UNDEFINED, SUCCEEDED, FAILED, KILLED
     */
    private String finalStatus;

    /**
     * user
     */
    private String user;

    /**
     * unfinished applications that are currently in this queue
     */
    private String queue;

    /**
     * total number of app objects to be returned
     */
    private String limit;

    /**
     * applications with start time beginning with this time, specified in ms since epoch
     */
    private String startedTimeBegin;

    /**
     * applications with start time ending with this time, specified in ms since epoch
     */
    private String startedTimeEnd;

    /**
     * applications with finish time beginning with this time, specified in ms since epoch
     */
    private String finishedTimeBegin;

    /**
     * applications with finish time ending with this time, specified in ms since epoch
     */
    private String finishedTimeEnd;

    /**
     * applications matching the given application types, specified as a comma-separated list
     */
    private String applicationTypes;

    /**
     * applications matching any of the given application tags, specified as a comma-separated list
     */
    private String applicationTags;

    /**
     * a generic fields which will be skipped in the result
     */
    private String deSelects;

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getStartedTimeBegin() {
        return startedTimeBegin;
    }

    public void setStartedTimeBegin(String startedTimeBegin) {
        this.startedTimeBegin = startedTimeBegin;
    }

    public String getStartedTimeEnd() {
        return startedTimeEnd;
    }

    public void setStartedTimeEnd(String startedTimeEnd) {
        this.startedTimeEnd = startedTimeEnd;
    }

    public String getFinishedTimeBegin() {
        return finishedTimeBegin;
    }

    public void setFinishedTimeBegin(String finishedTimeBegin) {
        this.finishedTimeBegin = finishedTimeBegin;
    }

    public String getFinishedTimeEnd() {
        return finishedTimeEnd;
    }

    public void setFinishedTimeEnd(String finishedTimeEnd) {
        this.finishedTimeEnd = finishedTimeEnd;
    }

    public String getApplicationTypes() {
        return applicationTypes;
    }

    public void setApplicationTypes(String applicationTypes) {
        this.applicationTypes = applicationTypes;
    }

    public String getApplicationTags() {
        return applicationTags;
    }

    public void setApplicationTags(String applicationTags) {
        this.applicationTags = applicationTags;
    }

    public String getDeSelects() {
        return deSelects;
    }

    public void setDeSelects(String deSelects) {
        this.deSelects = deSelects;
    }
}
