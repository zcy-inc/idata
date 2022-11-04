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

package cn.zhengcaiyun.idata.portal.controller.dev.job.di;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.SrcReadModeEnum;
import cn.zhengcaiyun.idata.develop.helper.rule.DIRuleHelper;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import cn.zhengcaiyun.idata.portal.model.request.job.GenerateMergeSqlRequest;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * job-content-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:46
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/di")
public class DISqlGenerateController {

    @Autowired
    private DIJobContentService diJobContentService;

    /**
     * 自动生成mergeSql
     * @return
     */
    @PostMapping("/generate/merge-sql")
    public RestResult<String> defaultMergeSql(@RequestBody @Valid GenerateMergeSqlRequest request) {
        System.out.println("merge-sql: " + JSON.toJSONString(request));
        String srcReadMode = request.getSrcReadMode();
        if (!StringUtils.equalsIgnoreCase(srcReadMode, SrcReadModeEnum.INC.value)) {
            return RestResult.success("");
        }
        String destTable = request.getDestTable();
        String[] hiveTableSplit = destTable.split("\\.");
        if (hiveTableSplit.length != 2) {
            throw new IllegalArgumentException("The hive table must have db name");
        }

        // 格式化输入列，为后续处理做准备
        request.formatColumns();

        String dataSourceType = request.getDataSourceType();
        String sourceTable = request.getSourceTable();
        String keyColumns = request.getKeyColumns();
        if (StringUtils.isEmpty(keyColumns)) {
            keyColumns = "id";
        }
        List<String> columnList = Arrays.asList(request.getSelectColumns().replaceAll(" ", "").replaceAll("\n", "").split(","));
        return RestResult.success(DIRuleHelper.generateMergeSql(columnList, keyColumns, sourceTable, destTable, DataSourceTypeEnum.valueOf(dataSourceType), request.getDays()));
    }

}
