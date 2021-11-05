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

package cn.zhengcaiyun.idata.develop.event.dag.subscriber;

import cn.zhengcaiyun.idata.develop.event.dag.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:28
 **/
public interface IDagEventSubscriber {

    void onCreated(DagCreatedEvent event);

    void onUpdated(DagCreatedEvent event);

    void onDeleted(DagDeletedEvent event);

    void onOnline(DagOnlineEvent event);

    void onOffline(DagOfflineEvent event);

    void onScheduleUpdated(DagScheduleUpdatedEvent event);

    void onRun(DagRunEvent event);

    void addDependence(DagAddDependenceEvent event);

    void removeDependence(DagRemoveDependenceEvent event);
}
