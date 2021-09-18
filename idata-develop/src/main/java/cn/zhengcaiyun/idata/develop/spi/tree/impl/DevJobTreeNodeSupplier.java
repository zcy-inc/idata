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

package cn.zhengcaiyun.idata.develop.spi.tree.impl;

import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:29
 **/
@Component
public class DevJobTreeNodeSupplier implements BizTreeNodeSupplier {

    @PostConstruct
    public void register() {
        BizTreeNodeSupplierFactory.register(FunctionModuleEnum.DEV_JOB, this);
    }

    @Override
    public List<DevTreeNodeDto> supply(FunctionModuleEnum moduleEnum) {
        return null;
    }
}
