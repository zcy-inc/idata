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

import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-28 17:32
 */

@Mapper
public interface DevLabelMyDao {

    @Select("<script>" +
            "SELECT dev_label.id, dev_label.label_code, dev_label.table_id, dev_label.column_name, " +
                "dev_label.label_param_value, dev_label_define.label_name, dev_label_define.label_tag, " +
                "dev_label_define.label_param_type " +
            "FROM dev_label " +
            "LEFT JOIN dev_label_define " +
            "ON dev_label.label_code = dev_label_define.label_code " +
            "WHERE dev_label.del != 1 AND dev_label_define.del != 1 AND dev_label_define.label_tag not like '%_METRIC_LABEL' " +
                "AND dev_label_define.label_tag != 'MODIFIER_LABEL' AND dev_label_define.label_tag != 'DIMENSION_LABEL '" +
                "AND dev_label.table_id = #{tableId} " +
                "<choose>" +
                    "<when test = 'columnNames != null'>" +
                        "AND FIND_IN_SET(dev_label.column_name, #{columnNames}) " +
                    "</when>" +
                    "<otherwise>" +
                        "AND dev_label.column_name IS null " +
                    "</otherwise>" +
                "</choose>" +
            "ORDER BY dev_label_define.label_index, dev_label_define.id" +
            "</script>")
    List<LabelDto> selectLabelsBySubject(Long tableId, String columnNames);

    @Select("<script>" +
            "SELECT DISTINCT dev_label.table_id " +
            "FROM dev_label " +
            "WHERE dev_label.del != 1 AND dev_label.column_name IS NOT NULL AND dev_label.label_code = 'columnComment:LABEL' AND (" +
                "<foreach collection = 'searchTexts' item = 'searchText' index = 'index' open = '(' separator = 'AND' close = ')'>" +
                    "dev_label.label_param_value LIKE CONCAT('%', #{searchText}, '%')" +
                "</foreach>" +
            ") " +
            "</script>")
    List<Long> getSearchTableIds(String searchType, List<String> searchTexts);

    @Insert("<script>" +
            "insert into dev_label(del, creator, create_time, editor, edit_time, label_code, table_id, column_id, column_name, label_param_value, hidden) " +
            "values " +
            "<foreach collection='list' item='item' index='index' separator=','> " +
            "(#{item.del}, #{item.creator}, #{item.createTime}, #{item.editor}, #{item.editTime}, #{item.labelCode}, #{item.tableId}, #{item.columnId}, #{item.columnName}, #{item.labelParamValue}, #{item.hidden})" +
            "</foreach> " +
            "ON DUPLICATE KEY UPDATE del = VALUES(del), label_param_value = VALUES(label_param_value), column_name = VALUES(column_name)" +
            "</script>")
    void batchUpsert(List<DevLabel> list);

    @Update("<script>" +
            "update dev_label set del = 1 where column_id = #{columnId} and column_name = #{columnName} and label_code like 'hive%' " +
            "</script>")
    void deleteDeprecatedHiveColumn(Long columnId, String columnName);
}
