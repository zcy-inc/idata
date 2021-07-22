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
package cn.zhengcaiyun.idata.sql.model.function;

import cn.zhengcaiyun.idata.sql.model.BaseColumn;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:27
 **/
public abstract class BaseFunction implements BaseColumn {
    protected final BaseColumn column;
    protected final String alias;

    protected BaseFunction(BaseColumn column, String alias) {
        checkNotNull(column, "函数列不能为空.");
        this.column = column;
        this.alias = alias;
    }
}
