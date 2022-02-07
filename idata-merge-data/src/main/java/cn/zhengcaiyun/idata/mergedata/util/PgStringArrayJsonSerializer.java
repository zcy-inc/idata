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

package cn.zhengcaiyun.idata.mergedata.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.postgresql.jdbc.PgArray;

import java.lang.reflect.Type;
import java.sql.SQLException;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-17 18:00
 **/
public class PgStringArrayJsonSerializer implements JsonSerializer<PgArray> {

    @Override
    public JsonElement serialize(PgArray pgArray, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        try {
            Object obj = pgArray.getArray();
            if (obj == null) return jsonArray;

            Object[] objArray = (Object[]) obj;
            if (objArray.length == 0) return jsonArray;

            for (Object o : objArray) {
                if (o == null) continue;
                if (o.toString().equals("null")) continue;
                jsonArray.add(o.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
