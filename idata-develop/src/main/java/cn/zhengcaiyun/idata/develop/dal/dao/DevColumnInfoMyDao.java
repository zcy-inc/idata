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
 * @date 2021-08-31 下午7:59
 */

@Mapper
public interface DevColumnInfoMyDao {

    @Select("<script>" +
            "SELECT DISTINCT dev_column_info.table_id " +
            "FROM dev_column_info " +
            "WHERE dev_column_info.del != 1 AND (" +
                "<foreach collection = 'searchTexts' item = 'searchText' index = 'index' open = '(' separator = 'AND' close = ')'>" +
                    "dev_column_info.column_name LIKE CONCAT('%', #{searchText}, '%')" +
                "</foreach>" +
            ") " +
            "</script>")
    List<Long> getSearchColumns(String searchType, List<String> searchTexts);
}
