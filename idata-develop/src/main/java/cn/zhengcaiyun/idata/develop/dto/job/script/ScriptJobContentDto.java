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
package cn.zhengcaiyun.idata.develop.dto.job.script;

import cn.zhengcaiyun.idata.develop.dto.job.JobArgumentDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;

import java.util.List;
import java.util.Objects;

/**
 * @author caizhedong
 * @date 2021-11-19 下午3:19
 */

public class ScriptJobContentDto extends JobContentBaseDto {
    private String sourceResource;
    private List<JobArgumentDto> scriptArguments;
    // JobTypeEnum.language
    private String scriptLanguage;

    // GaS
    public String getSourceResource() {
        return sourceResource;
    }

    public void setSourceResource(String sourceResource) {
        this.sourceResource = sourceResource;
    }

    public List<JobArgumentDto> getScriptArguments() {
        return scriptArguments;
    }

    public void setScriptArguments(List<JobArgumentDto> scriptArguments) {
        this.scriptArguments = scriptArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScriptJobContentDto that = (ScriptJobContentDto) o;
        if (scriptArguments.size() != that.scriptArguments.size()) return false;
        return Objects.equals(sourceResource, that.sourceResource) && Objects.equals(scriptArguments, that.scriptArguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceResource, scriptArguments);
    }
}
