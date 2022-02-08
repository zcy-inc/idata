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

package cn.zhengcaiyun.idata.map.facade;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;

import java.util.List;

/**
 * @description: 数据地图接口
 * @author: yangjianhua
 * @create: 2021-07-14 13:34
 **/
public interface DataMapFacade {

    /**
     * 搜索数据，返回分页记录
     *
     * @param condition 搜索条件
     * @param pageParam 分页参数
     * @return
     */
    Page<DataEntityDto> searchEntity(DataSearchCond condition, PageParam pageParam);

    /**
     * 获取数据类别，资产目录 or 业务过程
     *
     * @param condition
     * @return
     */
    List<CategoryTreeNodeDto> getCategory(CategoryCond condition);
}
