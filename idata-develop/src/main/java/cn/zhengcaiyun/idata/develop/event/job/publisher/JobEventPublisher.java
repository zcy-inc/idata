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

package cn.zhengcaiyun.idata.develop.event.job.publisher;

import cn.zhengcaiyun.idata.develop.event.job.*;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:45
 **/
@Component
public class JobEventPublisher {

    public boolean onCreated(JobCreatedEvent event) {

    }

    public boolean onUpdated(JobUpdatedEvent event) {

    }

    public boolean onDeleted(JobDeletedEvent event) {

    }

    public boolean onEnabled(JobEnabledEvent event) {

    }

    public boolean onDisabled(JobDisabledEvent event) {

    }

    public boolean onPublished(JobPublishedEvent event) {

    }

    public boolean onDagChanged(JobDagChangedEvent event) {

    }

    public boolean onRun(JobRunEvent event) {

    }
}
