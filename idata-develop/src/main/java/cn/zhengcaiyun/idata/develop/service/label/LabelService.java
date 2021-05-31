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

import cn.zhengcaiyun.idata.dto.develop.label.LabelDefineDto;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDto;

import java.util.List;
import java.util.Map;

/**
 * @author caizhedong
 * @date 2021-05-25 14:44
 */

public interface LabelService {
    LabelDefineDto defineLabel(LabelDefineDto labelDefineDto, String operator);
    LabelDefineDto findDefine(String labelCode);
    List<LabelDefineDto> findDefines(String subjectType, String labelTag);
    boolean deleteDefine(String labelCode, String operator);
    LabelDto label(LabelDto labelDto, String operator);
    List<LabelDto> findLabels(Long tableId, String columnName);
    Map<String, List<LabelDto>> findColumnLabelMap(Long tableId, List<String> columnNames);
    boolean removeLabel(LabelDto labelDto, String operator);
}
