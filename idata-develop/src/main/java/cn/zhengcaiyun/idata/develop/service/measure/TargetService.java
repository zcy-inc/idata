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
package cn.zhengcaiyun.idata.develop.service.measure;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-23 18:02
 */

@Service
public class TargetService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private LabelService labelService;

    public void checkTargetInfo(List<LabelDto> targetLabels, String labelTag) {
        LabelTagEnum.valueOf(labelTag);
        Map<Long, String> dimensionLabelMap = targetLabels.stream().collect(Collectors.toMap(LabelDto::getTableId,
                LabelDto::getColumnName));
        dimensionLabelMap.forEach((key, value) -> {
            List<String> columnNameList = columnInfoService.getColumns(key).stream()
                    .map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            checkArgument(columnNameList.contains(value), "表" + key + "不含该字段");
        });
        if (LabelTagEnum.DIMENSION_LABEL.name().equals(labelTag)) {
            List<LabelDto> dimTableIdList = targetLabels.stream().filter(dimensionLabel ->
                    "true".equals(dimensionLabel.getLabelParamValue()))
                    .collect(Collectors.toList());
            checkArgument(dimTableIdList.size() == 1, "主表数量有误");
        }
    }

    // TODO 拆分到各自service
    public void addAndDeleteTargetLabels(String targetCode, List<LabelDto> targetLabels, String labelTag, String operator) {
//        checkTargetInfo(targetLabels, labelTag);
        List<LabelDto> existTargetLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode,
                                isEqualTo(targetCode))).build().render(RenderingStrategies.MYBATIS3)),
                LabelDto.class);
        Set<String> existTargetStr = existTargetLabelList.stream().map(existDimensionLabel ->
                existDimensionLabel.getTableId() + "_" + existDimensionLabel.getColumnName())
                .collect(Collectors.toSet());
        Set<String> targetStr = targetLabels.stream().map(targetLabel ->
                targetLabel.getTableId() + "_" + targetLabel.getColumnName())
                .collect(Collectors.toSet());
        Set<String> addTargetStr = new HashSet<>(targetStr);
        addTargetStr.removeAll(existTargetStr);
        List<LabelDto> addTargetLabelList = targetLabels.stream().filter(targetLabel ->
                addTargetStr.contains(targetLabel.getTableId() + "_" + targetLabel.getColumnName())
        ).collect(Collectors.toList());
        Set<String> deleteTargetStr = new HashSet<>(existTargetStr);
        deleteTargetStr.removeAll(targetStr);
        List<LabelDto> deleteTargetLabelList = existTargetLabelList.stream().filter(targetLabel ->
                deleteTargetStr.contains(targetLabel.getTableId() + "_" + targetLabel.getColumnName())
        ).collect(Collectors.toList());
        List<LabelDto> addEchoDimensionLabelList = addTargetLabelList.stream().map(targetLabel ->
                labelService.label(targetLabel, operator))
                .collect(Collectors.toList());
        boolean isDelete = deleteTargetLabelList.stream().allMatch(deleteTargetLabel ->
                labelService.removeLabel(deleteTargetLabel, operator));
    }
}
