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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-28 17:32
 */

@Mapper
public interface DevLabelMyDao {

    @Select("<script>" +
            "SELECT * " +
            "FROM dev_label " +
            "WHERE dev_label.del != 1 " +
                "AND FIND_IN_SET(dev_label.label_code, #{labelCodes})" +
            "</script>")
    List<DevLabel> selectLabelsByLabelCodes(String labelCodes);
}
