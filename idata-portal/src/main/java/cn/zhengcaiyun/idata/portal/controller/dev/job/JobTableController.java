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

package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobTableDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * job-basic-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:46
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobTables")
public class JobTableController {

    /**
     * 获取表
     *
     * @param param
     * @return
     */
    @GetMapping()
    public RestResult<JobTableDto> getJobTable(@RequestParam JobTableParam param) {
        return RestResult.success();
    }

    /**
     * 获取表字段
     *
     * @param param
     * @return
     */
    @GetMapping("/columns")
    public RestResult<MappingColumnDto> getJobTableColumn(@RequestParam JobTableColumnParam param) {
        return RestResult.success();
    }

    public static class JobTableParam {
        /**
         * 数据源类型
         */
        private DataSourceTypeEnum dataSourceType;
        /**
         * 数据源编号
         */
        private Long dataSourceId;

        public DataSourceTypeEnum getDataSourceType() {
            return dataSourceType;
        }

        public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
            this.dataSourceType = dataSourceType;
        }

        public Long getDataSourceId() {
            return dataSourceId;
        }

        public void setDataSourceId(Long dataSourceId) {
            this.dataSourceId = dataSourceId;
        }
    }

    public static class JobTableColumnParam extends JobTableParam {
        /**
         * 表id（从数仓设计模块查询时有id）
         */
        private Long tableId;
        /**
         * 表名（从外部数据库查询出的表只有表名）
         */
        private String name;

        public Long getTableId() {
            return tableId;
        }

        public void setTableId(Long tableId) {
            this.tableId = tableId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
