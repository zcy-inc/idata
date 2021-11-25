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
package cn.zhengcaiyun.idata.develop.service.label;

import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;

import java.util.List;
import java.util.Map;

/**
 * @author caizhedong
 * @date 2021-05-25 15:31
 */

public interface EnumService {
    EnumDto createOrEdit(EnumDto enumDto, String operator);
    EnumDto findEnum(String enumCode);
    String getEnumCode(Long enumId);
    List<EnumDto> getEnums();
    List<EnumValueDto> getEnumValues(String enumCode);
    String getEnumName(String enumCode);
    String getEnumValue(String valueCode);
    List<DevEnumValue> getEnumValues(List<String> valueCodes);
    boolean delete(String enumCode, String operator);

    /**
     * 根据enumCode获取valuecode和enumvalue映射
     * @param enumCode
     * @return
     */
    Map<String, String> getEnumValueMapByCode(String enumCode);

}
