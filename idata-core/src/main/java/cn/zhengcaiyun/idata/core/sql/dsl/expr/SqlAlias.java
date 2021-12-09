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

package cn.zhengcaiyun.idata.core.sql.dsl.expr;

import java.util.Optional;

/**
 * @description: 别名
 * @author: yangjianhua
 * @create: 2021-07-26 16:21
 **/
public interface SqlAlias<T extends SqlAlias> {

    /**
     * 别名
     *
     * @return
     */
    Optional<String> alias();

    /**
     * 最终名称，若有别名，则为别名；否则为实际列名、表名等。
     *
     * @return
     */
    String finalName();

    /**
     * 创建别名
     *
     * @param alias
     * @return
     */
    T as(String alias);
}
