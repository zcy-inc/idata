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

package cn.zhengcaiyun.idata.develop.dto.opt.stream;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 17:34
 **/
public class StreamJobRunParamDto {

    /**
     * DI_STREAM 作业运行参数
     */
    private DIStreamRunParam diStreamRunParam;

    /**
     * SQL_FLINK 作业运行参数
     */
    private FlinkSqlRunParam flinkSqlRunParam;

    public static class DIStreamRunParam {
        /**
         * 强制初始化表集合
         */
        private List<String> forceInitTables;

        public List<String> getForceInitTables() {
            return forceInitTables;
        }

        public void setForceInitTables(List<String> forceInitTables) {
            this.forceInitTables = forceInitTables;
        }
    }

    public static class FlinkSqlRunParam {

    }

    public DIStreamRunParam getDiStreamRunParam() {
        return diStreamRunParam;
    }

    public void setDiStreamRunParam(DIStreamRunParam diStreamRunParam) {
        this.diStreamRunParam = diStreamRunParam;
    }

    public FlinkSqlRunParam getFlinkSqlRunParam() {
        return flinkSqlRunParam;
    }

    public void setFlinkSqlRunParam(FlinkSqlRunParam flinkSqlRunParam) {
        this.flinkSqlRunParam = flinkSqlRunParam;
    }
}
