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
package cn.zhengcaiyun.idata.label.enums;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 标签主体枚举
 * @author: yangjianhua
 * @create: 2021-06-25 10:26
 **/
public enum ObjectTypeEnum {
    purchaser("purchaserOrg:LABEL", "purchaser_org_id", "purchaser_org_name", "采购单位"),
    supplier("supplier:LABEL", "supplier_org_id", "supplier_org_name", "供应商"),
    ;

    private String type;
    private String objectIdFiled;
    private String objectNameFiled;
    private String desc;

    ObjectTypeEnum(String type, String objectIdFiled, String objectNameFiled, String desc) {
        this.type = type;
        this.objectIdFiled = objectIdFiled;
        this.objectNameFiled = objectNameFiled;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getObjectIdFiled() {
        return objectIdFiled;
    }

    public String getObjectNameFiled() {
        return objectNameFiled;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<String, ObjectTypeEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(ObjectTypeEnum.values())
                .forEach(enumObject -> map.put(enumObject.getType(), enumObject));
    }

    public static Optional<ObjectTypeEnum> getEnum(String objectType) {
        if (StringUtils.isEmpty(objectType)) return Optional.empty();

        return Optional.ofNullable(map.get(objectType));
    }
}
