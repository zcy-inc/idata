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

package cn.zhengcaiyun.idata.develop.event.dag.publisher;

import cn.zhengcaiyun.idata.develop.event.dag.*;
import cn.zhengcaiyun.idata.develop.event.dag.bus.DagEventBus;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 15:42
 **/
@Component
public class DagEventPublisher {

    public boolean publishCreatedEvent() {
        DagCreatedEvent event = new DagCreatedEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishUpdatedEvent() {
        DagUpdatedEvent event = new DagUpdatedEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishDeletedEvent() {
        DagDeletedEvent event = new DagDeletedEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishOfflineEvent() {
        DagOfflineEvent event = new DagOfflineEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishOnlineEvent() {
        DagOnlineEvent event = new DagOnlineEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishRunEvent() {
        DagRunEvent event = new DagRunEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishScheduleUpdatedEvent() {
        DagScheduleUpdatedEvent event = new DagScheduleUpdatedEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishAddDependenceEvent() {
        DagAddDependenceEvent event = new DagAddDependenceEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    public boolean publishRemoveDependenceEvent() {
        DagRemoveDependenceEvent event = new DagRemoveDependenceEvent();
        // 发布事件
        DagEventBus.getInstance().post(event);

        // 检查事件处理结果
        processResult(event);
        return true;
    }

    private void processResult(DagBaseEvent event) {
        if (event.hasFailed()) {
            // 处理失败，则不处理，待重试
        } else {
            // 处理成功，则标记事件已处理
        }
    }
}
