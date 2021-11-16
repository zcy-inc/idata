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

package cn.zhengcaiyun.idata.develop.event.job.subscriber.impl;

import cn.zhengcaiyun.idata.develop.event.job.*;
import cn.zhengcaiyun.idata.develop.event.job.subscriber.IJobEventSubscriber;
import cn.zhengcaiyun.idata.develop.integration.schedule.IJobIntegrator;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-03 17:57
 **/
@Component
public class JobIntegrationSubscriber implements IJobEventSubscriber {

    private final IJobIntegrator jobIntegrator;

    @Autowired
    public JobIntegrationSubscriber(IJobIntegrator jobIntegrator) {
        this.jobIntegrator = jobIntegrator;
    }

    @Override
    @Subscribe
    public void onCreated(JobCreatedEvent event) {

    }

    @Override
    @Subscribe
    public void onUpdated(JobUpdatedEvent event) {

    }

    @Override
    @Subscribe
    public void onDeleted(JobDeletedEvent event) {

    }

    @Override
    @Subscribe
    public void onEnabled(JobEnabledEvent event) {

    }

    @Override
    @Subscribe
    public void onDisabled(JobDisabledEvent event) {

    }

    @Override
    @Subscribe
    public void onPublished(JobPublishedEvent event) {

    }

    @Override
    @Subscribe
    public void onUnbindDag(JobUnBindDagEvent event) {

    }

    @Override
    @Subscribe
    public void onBindDag(JobBindDagEvent event) {

    }

    @Override
    @Subscribe
    public void onRun(JobRunEvent event) {

    }
}
