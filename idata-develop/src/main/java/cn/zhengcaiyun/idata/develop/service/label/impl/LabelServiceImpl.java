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
package cn.zhengcaiyun.idata.develop.service.label.impl;

import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDefineDto;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shiyin
 * @date 2021-05-30 20:28
 */
@Service
public class LabelServiceImpl implements LabelService {


    @Override
    public LabelDefineDto defineLabel(LabelDefineDto labelDefineDto, String operator) {
        return null;
    }

    @Override
    public LabelDefineDto findDefine(String labelCode) {
        return null;
    }

    @Override
    public List<LabelDefineDto> findDefines(String subjectType, String labelTag) {
        return null;
    }

    @Override
    public boolean deleteDefine(String labelCode, String operator) {
        return false;
    }

    @Override
    public LabelDto label(LabelDto labelDto, String operator) {
        return null;
    }

    @Override
    public List<LabelDto> findLabels(Long tableId, String columnName) {
        return null;
    }

    @Override
    public Map<String, List<LabelDto>> findColumnLabelMap(Long tableId, List<String> columnNames) {
        return null;
    }

    @Override
    public boolean removeLabel(LabelDto labelDto, String operator) {
        return false;
    }
}
