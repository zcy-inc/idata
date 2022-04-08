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
package cn.zhengcaiyun.idata.develop.dal.dao;

import cn.zhengcaiyun.idata.develop.dal.JsonColumnHandler;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.SpecialAttributeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.thrift.Option;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author shiyin
 * @date 2021-06-17 21:11
 */

@Mapper
public interface DevLabelDefineMyDao {

    @Result(column = "label_attributes", property = "labelAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "special_attribute", property = "specialAttribute", javaType = SpecialAttributeDto.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "enum_attributes", property = "enumAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Select("<script>" +
            "select * " +
            "from dev_label_define " +
            "where dev_label_define.del != 1 " +
            "and (dev_label_define.label_attributes like concat('%', #{enumCode}, '%') " +
            "or dev_label_define.label_param_type = #{enumCode})" +
            "</script>")
    List<DevLabelDefine> selectLabelDefineByEnumCode(String enumCode);

    @Result(column = "label_attributes", property = "labelAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "special_attribute", property = "specialAttribute", javaType = SpecialAttributeDto.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "enum_attributes", property = "enumAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Select("<script>" +
            "SELECT * " +
            "FROM dev_label_define " +
            "WHERE dev_label_define.del != 1 AND dev_label_define.label_tag NOT LIKE '%_DISABLE' " +
                "AND FIND_IN_SET(dev_label_define.label_code, #{labelCodes}) " +
            "ORDER BY dev_label_define.label_index" +
            "</script>")
    List<DevLabelDefine> selectLabelDefinesByLabelCodes(String labelCodes);

    @Result(column = "label_attributes", property = "labelAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "special_attribute", property = "specialAttribute", javaType = SpecialAttributeDto.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "enum_attributes", property = "enumAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Select("<script>" +
            "SELECT dev_label_define.label_code " +
            "FROM dev_label_define " +
            "<if test = 'belongTblName != null'>" +
                "LEFT JOIN dev_label ON dev_label_define.label_code = dev_label.label_code " +
                "LEFT JOIN dev_table_info ON dev_label.table_id = dev_table_info.id " +
            "</if>" +
            "WHERE dev_label_define.del != 1 AND dev_label_define.label_tag LIKE concat('%', #{measureType}, '%') " +
                // 暂过滤复合指标
                "AND dev_label_define.label_tag NOT LIKE 'COMPLEX_METRIC_LABEL%' " +
                "<if test = 'folderIds != null'>" +
                    "AND dev_label_define.folder_id in (${folderIds}) " +
                "</if>" +
                "<if test = 'metricType != null'>" +
                    "AND dev_label_define.label_tag = #{metricType} " +
                "</if>" +
                "<if test = 'measureId != null'>" +
                    "AND dev_label_define.label_attributes LIKE concat('%', #{measureId}, '%') " +
                "</if>" +
                "<if test = 'measureName != null'>" +
                    "AND dev_label_define.label_name LIKE concat('%', #{measureName}, '%') " +
                "</if>" +
                "<if test = 'bizProcess != null'>" +
                    "AND dev_label_define.label_attributes LIKE concat('%', #{bizProcess}, '%') " +
                "</if>" +
                "<if test = 'enable != null'>" +
                    "AND dev_label_define.label_tag LIKE concat('%', #{enable}) " +
                "</if>" +
                "<if test = 'creator != null'>" +
                    "AND dev_label_define.creator LIKE concat('%', #{creator}, '%') " +
                "</if>" +
                "<if test = 'measureDeadline != null'>" +
                    "AND dev_label_define.label_attributes LIKE concat('%', #{measureDeadline}, '%') " +
                "</if>" +
                "<if test = 'domain != null'>" +
                    "AND dev_label_define.label_attributes LIKE concat('%', #{domain}, '%') " +
                "</if>" +
                "<if test = 'belongTblName != null'>" +
                    "AND dev_table_info.del = 0 AND dev_table_info.table_name LIKE concat('%', #{belongTblName}, '%') " +
                "</if>" +
            "ORDER BY edit_time DESC LIMIT #{limit} OFFSET #{offset} " +
            "</script>")
    List<DevLabelDefine> selectLabelDefineCodesByCondition(String folderIds,
                                                           String measureType,
                                                           String metricType,
                                                           String measureId,
                                                           String measureName,
                                                           String bizProcess,
                                                           String enable,
                                                           String creator,
                                                           String measureDeadline,
                                                           String domain,
                                                           String belongTblName,
                                                           Long limit,
                                                           Integer offset);

    @Select("<script>" +
            "SELECT count(1) " +
            "FROM dev_label_define " +
            "<if test = 'belongTblName != null'>" +
                "LEFT JOIN dev_label ON dev_label_define.label_code = dev_label.label_code " +
                "LEFT JOIN dev_table_info ON dev_label.table_id = dev_table_info.id " +
            "</if>" +
            "WHERE dev_label_define.del != 1 AND dev_label_define.label_tag LIKE concat('%', #{measureType}, '%') " +
            // 暂过滤复合指标
            "AND dev_label_define.label_tag NOT LIKE 'COMPLEX_METRIC_LABEL%' " +
            "<if test = 'folderIds != null'>" +
                "AND dev_label_define.folder_id in (${folderIds}) " +
            "</if>" +
            "<if test = 'metricType != null'>" +
                "AND dev_label_define.label_tag = #{metricType} " +
            "</if>" +
            "<if test = 'measureId != null'>" +
                "AND dev_label_define.label_attributes LIKE concat('%', #{measureId}, '%') " +
            "</if>" +
            "<if test = 'measureName != null'>" +
                "AND dev_label_define.label_name LIKE concat('%', #{measureName}, '%') " +
            "</if>" +
            "<if test = 'bizProcess != null'>" +
                "AND dev_label_define.label_attributes LIKE concat('%', #{bizProcess}, '%') " +
            "</if>" +
            "<if test = 'enable != null'>" +
                "AND dev_label_define.label_tag LIKE concat('%', #{measureType}) " +
            "</if>" +
            "<if test = 'creator != null'>" +
                "AND dev_label_define.creator LIKE concat('%', #{creator}, '%') " +
            "</if>" +
            "<if test = 'measureDeadline != null'>" +
                "AND dev_label_define.label_attributes LIKE concat('%', #{measureDeadline}, '%') " +
            "</if>" +
            "<if test = 'domain != null'>" +
                "AND dev_label_define.label_attributes LIKE concat('%', #{domain}, '%') " +
            "</if>" +
            "<if test = 'belongTblName != null'>" +
                "AND dev_table_info.del = 0 AND dev_table_info.table_name LIKE concat('%', #{belongTblName}, '%') " +
            "</if>" +
            "</script>")
    Optional<Long> countLabelDefinesByCondition(String folderIds,
                                                String measureType,
                                                String metricType,
                                                String measureId,
                                                String measureName,
                                                String bizProcess,
                                                String enable,
                                                String creator,
                                                String measureDeadline,
                                                String domain,
                                                String belongTblName);
}
