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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-07-26 17:23
 */

@Mapper
public interface DevTableInfoMyDao {

    @Select("<script>" +
                "SELECT dev_label.table_id " +
                "FROM dev_label " +
                "LEFT JOIN dev_table_info " +
                "ON dev_label.table_id = dev_table_info.id " +
                "WHERE dev_label.del != 1 AND dev_table_info.del != 1 AND (" +
                    "<foreach collection = 'searchTexts' item = 'searchText' index = 'index' open = '(' separator = 'AND' close = ')'>" +
                        "dev_table_info.table_name LIKE CONCAT('%', #{searchText}, '%')" +
                    "</foreach>" +
                    "OR (dev_label.column_name IS NULL AND dev_label.label_code = 'tblComment:LABEL' AND " +
                    "<foreach collection = 'searchTexts' item = 'searchText' index = 'index' open = '(' separator = 'AND' close = ')'>" +
                        "dev_label.label_param_value LIKE CONCAT('%', #{searchText}, '%')" +
                    "</foreach>" +
                ")) " +
            "</script>")
    List<Long> getSearchTableIds(String searchType, List<String> searchTexts);
}
