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
package cn.zhengcaiyun.idata.develop.dto.table;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-07-29 10:14
 */

public class TableDetailDto extends TableInfoDto {
    private String tableComment;
    private List<String> assetCatalogues;
    private String metabaseUrl;
    private SecurityLevelEnum securityLevel;

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<String> getAssetCatalogues() {
        return assetCatalogues;
    }

    public void setAssetCatalogues(List<String> assetCatalogues) {
        this.assetCatalogues = assetCatalogues;
    }

    public String getMetabaseUrl() {
        return metabaseUrl;
    }

    public void setMetabaseUrl(String metabaseUrl) {
        this.metabaseUrl = metabaseUrl;
    }

    public SecurityLevelEnum getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(SecurityLevelEnum securityLevel) {
        this.securityLevel = securityLevel;
    }
}
