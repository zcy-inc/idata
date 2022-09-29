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

package cn.zhengcaiyun.idata.portal.controller.map;

import cn.hutool.core.collection.CollectionUtil;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.WhetherEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.util.MyBeanUtils;
import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.dal.dao.MapUserFavouriteDao;
import cn.zhengcaiyun.idata.map.dal.model.MapUserFavourite;
import cn.zhengcaiyun.idata.map.facade.DataMapFacade;
import cn.zhengcaiyun.idata.portal.model.request.map.UserFavoriteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.map.dal.dao.MapUserFavouriteDynamicSqlSupport.mapUserFavourite;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * data-map-controller
 *
 * @description: 数据地图rest接口
 * @author: yangjianhua
 * @create: 2021-07-12 14:08
 **/
@RestController
@RequestMapping(path = "/p1/map")
public class DataMapController {

    private final DataMapFacade dataMapFacade;

    @Autowired
    private MapUserFavouriteDao mapUserFavouriteDao;

    @Autowired
    public DataMapController(DataMapFacade dataMapFacade) {
        this.dataMapFacade = dataMapFacade;
    }

    /**
     * 搜索数据
     *
     * @param condition 搜索条件
     * @param limit     分页大小
     * @param offset    查询起始记录位置
     * @return
     */
    @PostMapping("/search")
    public RestResult<Page<DataEntityDto>> search(@RequestBody DataSearchCond condition,
                                                  @RequestParam(value = "limit") Long limit,
                                                  @RequestParam(value = "offset") Long offset) {
        return RestResult.success(dataMapFacade.searchEntity(condition, PageParam.of(limit, offset)));
    }

    /**
     * 获取类别
     *
     * @param condition 查询条件
     * @return
     */
    @GetMapping("/category")
    public RestResult<List<CategoryTreeNodeDto>> getCategory(CategoryCond condition) {
        return RestResult.success(dataMapFacade.getCategory(condition));
    }

    /**
     * 收藏表/指标
     * @param request
     * @return
     */
    @PostMapping("/addFavorite")
    public RestResult<Boolean> addFavorite(@RequestBody UserFavoriteRequest request) {
        MapUserFavourite userFavourite = MyBeanUtils.copy(request, MapUserFavourite.class);
        userFavourite.setUserId(OperatorContext.getCurrentOperator().getId());
        userFavourite.setCreator(OperatorContext.getCurrentOperator().getNickname());
        userFavourite.setDel(WhetherEnum.NO.val);
        mapUserFavouriteDao.insert(userFavourite);
        return RestResult.success(true);
    }

    /**
     * 取消收藏表/指标
     * @param request
     * @return
     */
    @PostMapping("/cancelFavorite")
    public RestResult<Boolean> cancelFavorite(@RequestBody UserFavoriteRequest request) {
        mapUserFavouriteDao.delete(dsl -> dsl.where(mapUserFavourite.entityCode, isEqualTo(request.getEntityCode()),
                and(mapUserFavourite.entitySource, isEqualTo(request.getEntitySource())),
                and(mapUserFavourite.userId, isEqualTo(OperatorContext.getCurrentOperator().getId()))));
        return RestResult.success(true);
    }

    /**
     * 查询表/指标是否已收藏
     * @param request
     * @return
     */
    @PostMapping("/isFavorite")
    public RestResult<Boolean> isFavorite(@RequestBody UserFavoriteRequest request) {
        List<MapUserFavourite> list = mapUserFavouriteDao.select(c ->
                c.where(mapUserFavourite.entityCode, isEqualTo(request.getEntityCode()),
                        and(mapUserFavourite.entitySource, isEqualTo(request.getEntitySource())),
                        and(mapUserFavourite.userId, isEqualTo(OperatorContext.getCurrentOperator().getId())),
                        and(mapUserFavourite.del, isEqualTo(0))));
        return RestResult.success(CollectionUtil.isNotEmpty(list));
    }

}
