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
package cn.zhengcaiyun.idata.develop.api.Impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.api.MeasureApi;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-22 11:37
 */

@Service
public class MeasureApiImpl implements MeasureApi {

    @Autowired
    private DevLabelDefineMyDao devLabelDefineMyDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private MetricService metricService;

    @Override
    public List<MeasureDto> getMeasures(List<String> labelCodes) {
        List<DevLabelDefine> measureList = devLabelDefineMyDao.selectLabelDefinesByLabelCodes(String.join(",", labelCodes));
        List<MeasureDto> echoMeasureList = measureList.stream().map(measure -> {
            MeasureDto echoMeasure = new MeasureDto();
            if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(measure.getLabelTag())) {
                echoMeasure = metricService.findMetric(measure.getLabelCode());
            }
            else {
                List<LabelDto> measureLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(measure.getLabelCode())))
                        .build().render(RenderingStrategies.MYBATIS3)), LabelDto.class);
                measureLabelList.forEach(measureLabel -> {
                    measureLabel.setTableName(devTableInfoDao.selectOne(c -> c.where(devTableInfo.del, isNotEqualTo(1),
                            and(devTableInfo.id, isEqualTo(measureLabel.getTableId())))).get().getTableName());
                });
                echoMeasure = PojoUtil.copyOne(measure, MeasureDto.class);
                echoMeasure.setMeasureLabels(measureLabelList);
            }
            return echoMeasure;
        }).collect(Collectors.toList());
        return echoMeasureList;
    }
}
