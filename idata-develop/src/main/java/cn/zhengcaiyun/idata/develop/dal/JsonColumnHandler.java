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
package cn.zhengcaiyun.idata.develop.dal;

import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;
import cn.zhengcaiyun.idata.develop.dto.label.SpecialAttributeDto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-06-01 17:14
 */
public class JsonColumnHandler implements TypeHandler {
    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        TypeReference<?> typeReference = JsonColumnTypeEnum.getType(columnName);
        if (typeReference != null) {
            return JSON.parseObject(rs.getString(columnName), typeReference);
        }
        return rs.getString(columnName);
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

    enum JsonColumnTypeEnum {
        EnumAttributes("enum_attributes", new TypeReference<List<AttributeDto>>(){}),
        LabelAttributes("label_attributes", new TypeReference<List<AttributeDto>>(){}),
        SpecialAttribute("special_attribute", new TypeReference<SpecialAttributeDto>(){});
        private String columnName;
        private TypeReference<?> type;

        JsonColumnTypeEnum(String columnName, TypeReference<?> type) {
            this.type = type;
            this.columnName = columnName;
        }

        static TypeReference<?> getType(String columnName) {
            for (JsonColumnTypeEnum t: JsonColumnTypeEnum.values()) {
                if (t.columnName.equals(columnName)) {
                    return t.type;
                }
            }
            return null;
        }
    }
}
