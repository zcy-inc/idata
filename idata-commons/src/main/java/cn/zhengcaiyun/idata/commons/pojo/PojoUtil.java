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
package cn.zhengcaiyun.idata.commons.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 如果fieldNames没传，则拷贝源对象类（不包括父类）中定义的所有属性值。
 * 如果fieldNames传了，则拷贝源对象类（包括父类）中fieldsNames的属性值。
 * @author shiyin
 * @date 2021-03-10 10:35
 */
public class PojoUtil {

    public static <T> T copyOne(T sPojo, String... fieldNames) {
        if (fieldNames.length == 0) {
            fieldNames = Arrays.stream(sPojo.getClass().getDeclaredFields())
                    .map(Field::getName).toArray(String[]::new);
        }
        T tPojo;
        try {
            tPojo = (T) sPojo.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(sPojo.getClass() + "没有无参构造器");
        }
        Arrays.stream(fieldNames).forEach(fieldName -> {
            try {
                Field pojoField = getFieldByName(sPojo.getClass(), fieldName);
                pojoField.setAccessible(true);
                pojoField.set(tPojo, pojoField.get(sPojo));
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException(fieldName + "属性不存在或无法访问");
            }
        });
        return tPojo;
    }

    public static <S, T> T copyOne(S sPojo, Class<T> tClazz, String... fieldNames) {
        if (fieldNames.length == 0) {
            fieldNames = Arrays.stream(sPojo.getClass().getDeclaredFields())
                    .map(Field::getName).toArray(String[]::new);
        }
        T tPojo;
        List<String> targetFieldNames = new ArrayList<>();
        try {
            targetFieldNames = Arrays.stream(tClazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
            tPojo = tClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(tClazz + "没有无参构造器");
        }
        for (String fieldName : fieldNames) {
            if (targetFieldNames.contains(fieldName)) {
                try {
                    Field sField = getFieldByName(sPojo.getClass(), fieldName);
                    Field tField = getFieldByName(tClazz, fieldName);
                    sField.setAccessible(true);
                    tField.setAccessible(true);
                    tField.set(tPojo, sField.get(sPojo));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new IllegalArgumentException(fieldName + "属性不存在或无法访问");
                }
            }
        }
        return tPojo;
    }

    public static <S, T> T copyTo(S sPojo, T tPojo, String... fieldNames) {
        if (fieldNames.length == 0) {
            fieldNames = Arrays.stream(sPojo.getClass().getDeclaredFields())
                    .map(Field::getName).toArray(String[]::new);
        }
        Arrays.stream(fieldNames).forEach(fieldName -> {
            try {
                Field sField = getFieldByName(sPojo.getClass(), fieldName);
                Field tField = getFieldByName(tPojo.getClass(), fieldName);
                sField.setAccessible(true);
                tField.setAccessible(true);
                tField.set(tPojo, sField.get(sPojo));
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException(fieldName + "属性不存在或无法访问");
            }
        });
        return tPojo;
    }

    public static <S, T> List<T> copyList(List<S> sPojoList, Class<T> tClazz, String... fieldNames) {
        return sPojoList.stream().map(pojo -> copyOne(pojo, tClazz, fieldNames)).collect(Collectors.toList());
    }

    private static Field getFieldByName(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (Object.class.equals(clazz.getSuperclass())
                    || clazz.getSuperclass() == null) {
                throw e;
            }
            return getFieldByName(clazz.getSuperclass(), fieldName);
        }
    }

    public static <T> T castType(Object sObject, TypeReference<T> typeReference) {
        if (sObject == null) return null;
        return JSON.parseObject(JSON.toJSONString(sObject), typeReference);
    }

}
