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
package cn.zhengcaiyun.idata.label.service.label;

import cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDao;
import cn.zhengcaiyun.idata.label.dal.model.LabObjectLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDynamicSqlSupport.labObjectLabel;
import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 提供事务（避免一些事务直接加在service层，造成"大事务"）方法或基础、可复用的方法，想不到好点的名称，暂时取名manager
 * @author: yangjianhua
 * @create: 2021-06-23 15:05
 **/
@Component
public class LabObjectLabelManager {

    private final LabObjectLabelDao objectLabelDao;

    @Autowired
    public LabObjectLabelManager(LabObjectLabelDao objectLabelDao) {
        checkNotNull(objectLabelDao, "objectLabelDao must not be null.");
        this.objectLabelDao = objectLabelDao;
    }

    public LabObjectLabel getObjectLabel(String labelName) {
        checkArgument(isNotEmpty(labelName), "标签名称不能为空");
        return objectLabelDao.selectOne(
                dsl -> dsl.where(labObjectLabel.name, isEqualTo(labelName),
                        and(labObjectLabel.del, isEqualTo(DEL_NO.val))))
                .orElse(null);
    }

    public LabObjectLabel getObjectLabel(Long labelId, String errorMsg) {
        checkArgument(Objects.nonNull(labelId), "标签编号不能为空");
        Optional<LabObjectLabel> optional = objectLabelDao.selectByPrimaryKey(labelId);
        checkState(optional.isPresent(), errorMsg);
        return optional.get();
    }

    public LabObjectLabel getObjectLabelByOriginId(Long labelOriginId, String errorMsg) {
        checkArgument(Objects.nonNull(labelOriginId), "标签编号不能为空");

        Optional<LabObjectLabel> optional = objectLabelDao.selectOne(
                dsl -> dsl.where(labObjectLabel.originId, isEqualTo(labelOriginId),
                        and(labObjectLabel.del, isEqualTo(DEL_NO.val))));
        checkState(optional.isPresent(), errorMsg);
        return optional.get();
    }

    @Transactional
    public Long saveLabel(LabObjectLabel label) {
        objectLabelDao.insertSelective(label);
        objectLabelDao.update(dsl -> dsl.set(labObjectLabel.originId).equalTo(label.getId())
                .where(labObjectLabel.id, isEqualTo(label.getId())));
        return label.getId();
    }

    @Transactional
    public Long renewLabel(LabObjectLabel newLabel, Long oldLabelId) {
        // 软删除，修改del状态
        objectLabelDao.update(dsl -> dsl.set(labObjectLabel.del).equalTo(DEL_YES.val)
                .set(labObjectLabel.editor).equalTo(newLabel.getCreator()).where(labObjectLabel.id, isEqualTo(oldLabelId)));
        objectLabelDao.insertSelective(newLabel);
        return newLabel.getId();
    }

}
