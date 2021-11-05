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

package cn.zhengcaiyun.idata.develop.event.dag.subscriber.impl;

import cn.zhengcaiyun.idata.develop.event.core.Subscriber;
import cn.zhengcaiyun.idata.develop.event.dag.*;
import cn.zhengcaiyun.idata.develop.event.dag.bus.DagEventBus;
import cn.zhengcaiyun.idata.develop.event.dag.subscriber.IDagEventSubscriber;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:56
 **/
@Component
@Subscriber(DagEventBus.EVENT_BUS_EXPRESSION)
public class DagIntegrationSubscriber implements IDagEventSubscriber {

    @Override
    @Subscribe
    public void onCreated(DagCreatedEvent event) {

    }

    @Override
    @Subscribe
    public void onUpdated(DagCreatedEvent event) {

    }

    @Override
    @Subscribe
    public void onDeleted(DagDeletedEvent event) {

    }

    @Override
    @Subscribe
    public void onOnline(DagOnlineEvent event) {

    }

    @Override
    @Subscribe
    public void onOffline(DagOfflineEvent event) {

    }

    @Override
    @Subscribe
    public void onScheduleUpdated(DagScheduleUpdatedEvent event) {

    }

    @Override
    @Subscribe
    public void onRun(DagRunEvent event) {

    }

    @Override
    @Subscribe
    public void addDependence(DagAddDependenceEvent event) {

    }

    @Override
    @Subscribe
    public void removeDependence(DagRemoveDependenceEvent event) {

    }
}
