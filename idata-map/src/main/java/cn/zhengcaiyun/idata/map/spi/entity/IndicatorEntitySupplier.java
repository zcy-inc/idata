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

package cn.zhengcaiyun.idata.map.spi.entity;

import cn.zhengcaiyun.idata.develop.api.MeasureApi;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.constant.enums.EntitySourceEnum;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 获取表实体数据
 * @author: yangjianhua
 * @create: 2021-07-14 15:56
 **/
@Component
public class IndicatorEntitySupplier implements DataEntitySupplier<DataSearchCond, DataEntityDto> {

    private final MeasureApi measureApi;

    @Autowired
    public IndicatorEntitySupplier(MeasureApi measureApi) {
        this.measureApi = measureApi;
    }


    @PostConstruct
    public void register() {
        DataEntitySupplierFactory.register(EntitySourceEnum.INDICATOR.getCode(), this);
    }

    /**
     * 根据条件查询指标数据
     *
     * @param condition
     * @return
     */
    @Override
    public List<DataEntityDto> queryDataEntity(DataSearchCond condition) {
        // 从指标库查询指标数据
        List<String> metricCodes = measureApi.getMetricCodes(condition.getKeyWords(), Strings.emptyToNull(condition.getCategoryId()));
        if (ObjectUtils.isEmpty(metricCodes)) return Lists.newArrayList();

        return metricCodes.stream().map(code -> new DataEntityDto(code)).collect(Collectors.toList());
    }

    /**
     * 根据指标唯一标识获取指标数据
     *
     * @param entityCodes
     * @return
     */
    @Override
    public List<DataEntityDto> getDataEntity(List<String> entityCodes) {
        // 从指标库查询指标数据
        List<MeasureDto> measureDtoList = measureApi.getMetrics(entityCodes);
        if (ObjectUtils.isEmpty(measureDtoList)) return Lists.newArrayList();

        return measureDtoList.stream().map(this::toDataEntity).collect(Collectors.toList());
    }

    private DataEntityDto toDataEntity(MeasureDto measureDto) {
        DataEntityDto entityDto = new DataEntityDto(measureDto.getLabelCode());
        entityDto.setEntityName(measureDto.getLabelName());
        entityDto.setEntityNameEn(measureDto.getEnName());
        entityDto.setCategoryName(measureDto.getBizProcessValue());
        if (StringUtils.isNotEmpty(measureDto.getLabelTag())) {
            entityDto.putMoreAttr(DataEntityDto.more_indicator_type, measureDto.getLabelTag());
        }
        if (StringUtils.isNotEmpty(measureDto.getMeasureId())) {
            entityDto.putMoreAttr(DataEntityDto.more_indicator_id, measureDto.getMeasureId());
        }
        if (StringUtils.isNotEmpty(measureDto.getMeasureDefine())) {
            entityDto.putMoreAttr(DataEntityDto.more_indicator_comment, measureDto.getMeasureDefine());
        }
        return entityDto;
    }

}
