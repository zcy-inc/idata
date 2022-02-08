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

package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.commons.pojo.Page;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-10 14:34
 **/
public class OverhangJobWrapperDto {
    private final LocalDateTime fetchTime;
    private final Page<OverhangJobDto> overhangJobDtoPage;

    public OverhangJobWrapperDto(LocalDateTime fetchTime, Page<OverhangJobDto> overhangJobDtoPage) {
        this.fetchTime = fetchTime;
        this.overhangJobDtoPage = overhangJobDtoPage;
    }

    public LocalDateTime getFetchTime() {
        return fetchTime;
    }

    public Page<OverhangJobDto> getOverhangJobDtoPage() {
        return overhangJobDtoPage;
    }
}
