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
package cn.zhengcaiyun.idata.label.service;

import cn.zhengcaiyun.idata.label.dto.LabObjectLabelDto;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;

/**
 * @description: 对象标签service
 * @author: yangjianhua
 * @create: 2021-06-23 09:34
 **/
public interface LabObjectLabelService {
    /**
     * 创建标签
     *
     * @param labelDto
     * @param operator
     * @return
     */
    Long createLabel(LabObjectLabelDto labelDto, String operator);

    /**
     * 编辑标签
     *
     * @param labelDto
     * @param operator
     * @return
     */
    Long editLabel(LabObjectLabelDto labelDto, String operator);

    /**
     * 获取标签
     *
     * @param originId
     * @return
     */
    LabObjectLabelDto getLabel(Long originId);

    /**
     * 删除标签
     *
     * @param id
     * @param operator
     * @return
     */
    Boolean deleteLabel(Long id, String operator);

    /**
     * 查询标签计算数据
     *
     * @param id
     * @param layerId
     * @return
     */
    LabelQueryDataDto queryLabelResultData(Long id, Long layerId, Long limit, Long offset);
}
