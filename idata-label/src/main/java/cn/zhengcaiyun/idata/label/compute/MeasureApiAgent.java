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
package cn.zhengcaiyun.idata.label.compute;

import cn.zhengcaiyun.idata.develop.api.MeasureApi;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.SpecialAttributeDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import cn.zhengcaiyun.idata.label.compute.metadata.DimensionMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.IndicatorMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.ObjectMetadata;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-29 18:40
 **/
@Component
public class MeasureApiAgent {

    private final MeasureApi measureApi;

    @Autowired
    public MeasureApiAgent(MeasureApi measureApi) {
        this.measureApi = measureApi;
    }

    public Optional<Map<String, String>> getNames(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) return Optional.empty();
        List<MeasureDto> measureDtoList = measureApi.getMeasures(codes);
        if (CollectionUtils.isEmpty(measureDtoList)) return Optional.empty();

        Map<String, String> nameMap = Maps.newHashMap();
        for (MeasureDto measureDto : measureDtoList) {
            nameMap.put(measureDto.getLabelCode(), measureDto.getLabelName());
        }
        return Optional.of(nameMap);
    }

    public Optional<IndicatorMetadata> getIndicatorMetadata(String indicatorCode) {
        if (StringUtils.isEmpty(indicatorCode)) return Optional.empty();

        List<MeasureDto> measureDtoList = measureApi.getMeasures(Lists.newArrayList(indicatorCode));
        if (CollectionUtils.isEmpty(measureDtoList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(convertToIndicatorMetadata(measureDtoList.get(0)));
    }

    private IndicatorMetadata convertToIndicatorMetadata(MeasureDto measureDto) {
        IndicatorMetadata metadata = new IndicatorMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)) {
            return null;
        }
        metadata.setTable(measureLabels.get(0).getTableName());
        metadata.setColumn(measureLabels.get(0).getColumnName());
        SpecialAttributeDto specialAttributeDto = measureDto.getSpecialAttribute();
        if (Objects.isNull(specialAttributeDto)) {
            return null;
        }
        metadata.setFunction(specialAttributeDto.getAggregatorCode());
        List<ModifierDto> modifiers = measureDto.getModifiers();
        if (!CollectionUtils.isEmpty(modifiers)) {
            List<IndicatorMetadata.DecorateWordMetadata> decorateWords = Lists.newArrayList();
            for (ModifierDto modifierDto : modifiers) {
                IndicatorMetadata.DecorateWordMetadata decorateWord = new IndicatorMetadata.DecorateWordMetadata();
                decorateWord.setColumn(modifierDto.getColumnName());
                decorateWord.setParams(modifierDto.getEnumValues());
                decorateWords.add(decorateWord);
            }
            metadata.setDecorateWords(decorateWords);
        }
        return metadata;
    }

    public Optional<Map<String, DimensionMetadata>> getDimensionMetadata(List<String> dimensionCodes,
                                                                         IndicatorMetadata indicatorMetadata) {
        if (CollectionUtils.isEmpty(dimensionCodes)) return Optional.empty();
        List<MeasureDto> measureDtoList = measureApi.getMeasures(dimensionCodes);
        if (CollectionUtils.isEmpty(measureDtoList)) {
            return Optional.empty();
        }
        Map<String, DimensionMetadata> metadataMap = Maps.newHashMap();
        for (MeasureDto measureDto : measureDtoList) {
            DimensionMetadata metadata = convertToDimensionMetadata(measureDto, indicatorMetadata);
            if (!Objects.isNull(metadata))
                metadataMap.put(metadata.getCode(), metadata);
        }
        return Optional.of(metadataMap);
    }

    private DimensionMetadata convertToDimensionMetadata(MeasureDto measureDto,
                                                         IndicatorMetadata indicatorMetadata) {
        DimensionMetadata metadata = new DimensionMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)) {
            return null;
        }
        for (LabelDto labelDto : measureLabels) {
            if (labelDto.getTableName().equals(indicatorMetadata.getTable())) {
                metadata.setTable(measureLabels.get(0).getTableName());
                metadata.setColumn(measureLabels.get(0).getColumnName());
                break;
            }
        }
        if (StringUtils.isEmpty(metadata.getColumn())) {
            return null;
        }
        return metadata;
    }

    public Optional<ObjectMetadata> getObjectMetadata(String objectCode) {
        if (StringUtils.isEmpty(objectCode)) return Optional.empty();
        List<MeasureDto> measureDtoList = measureApi.getMeasures(Lists.newArrayList(objectCode));
        if (CollectionUtils.isEmpty(measureDtoList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(convertToObjectMetadata(measureDtoList.get(0)));
    }

    private ObjectMetadata convertToObjectMetadata(MeasureDto measureDto) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCode(measureDto.getLabelCode());
        metadata.setName(measureDto.getLabelName());
        List<LabelDto> measureLabels = measureDto.getMeasureLabels();
        if (CollectionUtils.isEmpty(measureLabels)) {
            return null;
        }
        for (LabelDto labelDto : measureLabels) {
            if ("true".equals(labelDto.getLabelParamValue())) {
                metadata.setTable(labelDto.getTableName());
                metadata.setColumn(labelDto.getColumnName());
                metadata.setOriginTable(labelDto.getTableName());
                break;
            }
        }
        if (StringUtils.isEmpty(metadata.getColumn())
                || StringUtils.isEmpty(metadata.getOriginTable()))
            return null;
        return metadata;
    }

}
