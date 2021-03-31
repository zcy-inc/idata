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
package cn.zhengcaiyun.idata.system.zcy;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.commons.rpc.HttpUtil;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.ResourceTypeEnum;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shiyin
 * @date 2021-03-26 14:31
 */
@Service
public class ZcyService {

    @Value("${zcy.idata.baseUri}")
    private String ZCY_IDATA_BASE_URI;

    public List<FolderTreeNodeDto> getFolders(ResourceTypeEnum resourceType) {
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("resourceType", resourceType.name());
        return HttpUtil.executeHttpRequest(new HttpInput().setMethod("GET").setServerName("ZCY IData")
                .setQueryParamMap(queryParamMap)
                .setUri(ZCY_IDATA_BASE_URI + "/outbound/folders"),
                new TypeReference<RestResult<List<FolderTreeNodeDto>>>(){}).getData()
                .stream().peek(folderTreeNode -> folderTreeNode.setType(resourceType.name()))
                .collect(Collectors.toList());
    }

}
