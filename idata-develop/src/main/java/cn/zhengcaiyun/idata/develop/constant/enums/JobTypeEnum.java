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

package cn.zhengcaiyun.idata.develop.constant.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 10:28
 **/
public enum JobTypeEnum {
    DI_BATCH("DI_BATCH", "DI", "数据抽取-离线", "", ""),
    DI_STREAM("DI_STREAM", "DI", "数据抽取-实时", "", ""),
    BACK_FLOW("BACK_FLOW", "DI", "数据回流-离线", "", ""),

    SQL_SPARK("SQL_SPARK", "SQL", "Sql作业", "SQL", "SPARK"),
    SQL_FLINK("SQL_FLINK", "SQL", "Sql作业", "SQL", "FLINK"),
    SQL_STARROCKS("SQL_STARROCKS", "SQL", "Sql作业", "SQL", ""),

    SPARK_PYTHON("SPARK_PYTHON", "SPARK", "Spark作业", "PYTHON", "SPARK"),
    SPARK_JAR("SPARK_JAR", "SPARK", "Spark作业", "JAR", "SPARK"),

    SCRIPT_PYTHON("SCRIPT_PYTHON", "SCRIPT", "Script作业", "PYTHON", ""),
    SCRIPT_SHELL("SCRIPT_SHELL", "SCRIPT", "Script作业", "SHELL", ""),

    KYLIN("KYLIN", "KYLIN", "Kylin作业", "", "KYLIN"),
    ;

    /**
     * JobTypeEnum的全部类型的，但是数据集成（DI）里面分为数据抽取（DI）和数据回流（BACK_FLOW）
     * 此处的枚举是为了针对数据集成单独做聚合
     */


    private final String code;
    private final String catalog;
    private final String name;
    private final String language;
    private final String engine;

    JobTypeEnum(String code, String catalog, String name, String language, String engine) {
        this.code = code;
        this.catalog = catalog;
        this.name = name;
        this.language = language;
        this.engine = engine;
    }

    public String getCode() {
        return code;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getEngine() {
        return engine;
    }

    public FunctionModuleEnum belong() {
        if (this.catalog.equals("DI")) return FunctionModuleEnum.DI;
        return FunctionModuleEnum.DEV_JOB;
    }

    private static final Map<String, JobTypeEnum> codeMap = Maps.newHashMap();
    private static final Map<String, List<JobTypeEnum>> catalogMap = Maps.newHashMap();

    static {
        Arrays.stream(JobTypeEnum.values())
                .forEach(enumObj -> {
                    codeMap.put(enumObj.code, enumObj);
                    List<JobTypeEnum> typeEnumList = catalogMap.get(enumObj.getCatalog());
                    if (typeEnumList == null) {
                        typeEnumList = Lists.newLinkedList();
                        catalogMap.put(enumObj.getCatalog(), typeEnumList);
                    }
                    typeEnumList.add(enumObj);
                });
    }

    public static Optional<JobTypeEnum> getEnum(String code) {
        if (StringUtils.isEmpty(code)) return Optional.empty();
        return Optional.ofNullable(codeMap.get(code));
    }

    public static Optional<List<JobTypeEnum>> getCatalogEnum(String catalog) {
        if (StringUtils.isEmpty(catalog)) return Optional.empty();
        return Optional.ofNullable(catalogMap.get(catalog));
    }

}
