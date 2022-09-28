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
package cn.zhengcaiyun.idata.user.dto;

import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dto.FeatureDto;
import cn.zhengcaiyun.idata.user.dal.model.UacAppInfo;

import java.util.List;

/**
 * @author caizhedong
 * @date 2022-09-18 下午12:03
 */

public class AppInfoDto extends UacAppInfo {
    private String featureCodes;
    private List<SysFeature> appFeatures;

    // GaS
    public String getFeatureCodes() {
        return featureCodes;
    }

    public void setFeatureCodes(String featureCodes) {
        this.featureCodes = featureCodes;
    }

    public List<SysFeature> getAppFeatures() {
        return appFeatures;
    }

    public void setAppFeatures(List<SysFeature> appFeatures) {
        this.appFeatures = appFeatures;
    }
}
