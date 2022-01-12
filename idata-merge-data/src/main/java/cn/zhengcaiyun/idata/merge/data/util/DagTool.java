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

package cn.zhengcaiyun.idata.merge.data.util;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-11 17:02
 **/
public class DagTool {

    public static Optional<DAGInfo> findDag(final Integer oldDagId, final List<DAGInfo> dagInfoList) {
        return dagInfoList.stream()
                .filter(dagInfo -> dagInfo.getName().startsWith(IdPadTool.padId(oldDagId.toString()) + "#_"))
                .findFirst();
    }

    public static Optional<DAGInfo> findDagByNewId(final Long newDagId, final List<DAGInfo> dagInfoList) {
        return dagInfoList.stream()
                .filter(dagInfo -> dagInfo.getId().equals(newDagId))
                .findFirst();
    }
}
