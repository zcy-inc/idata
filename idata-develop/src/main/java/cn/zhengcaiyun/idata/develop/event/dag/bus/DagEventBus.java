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

package cn.zhengcaiyun.idata.develop.event.dag.bus;

import cn.zhengcaiyun.idata.develop.event.core.IEventBus;
import cn.zhengcaiyun.idata.develop.event.dag.DagBaseEvent;
import com.google.common.eventbus.EventBus;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-05 15:00
 **/
public class DagEventBus implements IEventBus<DagBaseEvent> {

    private static final DagEventBus DAG_EVENT_BUS = new DagEventBus();

    public static final String EVENT_BUS_EXPRESSION = "T(cn.zhengcaiyun.idata.develop.event.dag.bus.DagEventBus).getEventBus()";

    private final EventBus eventBus = new EventBus("dag-event-bus");

    private DagEventBus() {
    }

    public static DagEventBus getInstance() {
        return DagEventBus.DAG_EVENT_BUS;
    }

    public static EventBus getEventBus() {
        return DagEventBus.DAG_EVENT_BUS.eventBus;
    }

    @Override
    public void post(DagBaseEvent event) {
        getEventBus().post(event);
    }
}
