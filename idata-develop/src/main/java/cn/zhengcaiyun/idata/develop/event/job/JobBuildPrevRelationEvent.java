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

package cn.zhengcaiyun.idata.develop.event.job;

import cn.zhengcaiyun.idata.develop.util.DagJobPair;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 15:24
 **/
public class JobBuildPrevRelationEvent extends JobBaseEvent {

    private String environment;
    private List<DagJobPair> addingPrevRelations;
    private List<DagJobPair> removingPrevRelations;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<DagJobPair> getAddingPrevRelations() {
        return addingPrevRelations;
    }

    public void setAddingPrevRelations(List<DagJobPair> addingPrevRelations) {
        this.addingPrevRelations = addingPrevRelations;
    }

    public List<DagJobPair> getRemovingPrevRelations() {
        return removingPrevRelations;
    }

    public void setRemovingPrevRelations(List<DagJobPair> removingPrevRelations) {
        this.removingPrevRelations = removingPrevRelations;
    }
}
