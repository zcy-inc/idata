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

import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-03-03 14:00
 **/
public class StringExtractTool {

//    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("");
//
//        List<String> words = Arrays.stream(builder.toString().split(" |,|;|--|\n")).filter(t -> t.contains("ods_db_")).distinct()
//                .collect(Collectors.toList());
//        for (String word : words) {
//            System.out.println("'" + word.replace("`", "").trim() + "'" + ",");
//        }
//    }

//    public static void main(String[] args) throws IOException {
//        String di_table_info = "di.columns=id,title,district,announcement_type,project_name,project_code,template_id,meta_data,content,status,created_at,released_at,expired_at,checked_at,check_opinion,creator_name,creator_id,checker_name,checker_id,url,backlog_id,serial_num,create_orgid,pub_type,show_duration,create_depid,out_url,sync_status,is_government_purchase_service,biz_id,org_name,creator_info,ann_big_type,is_send,app_code,system_type,is_secret,is_delete,create_time,update_time,gp_catalog_code,serial_type,hidden,objection_state,source,extend_first,extend_second,extend_third,extend_four,extend_five,agent_org_id,form_page_code,process_define_key\n" +
//                "di.query=select id,title,district,announcement_type,project_name,project_code,template_id,CONVERT(meta_data USING utf8) as meta_data,CONVERT(content USING utf8) as content,status,created_at,released_at,expired_at,checked_at,check_opinion,creator_name,creator_id,checker_name,checker_id,url,backlog_id,serial_num,create_orgid,pub_type,show_duration,create_depid,out_url,sync_status,is_government_purchase_service,biz_id,org_name,creator_info,ann_big_type,is_send,app_code,system_type,is_secret,is_delete,create_time,update_time,gp_catalog_code,serial_type,hidden,objection_state,source,extend_first,extend_second,extend_third,extend_four,extend_five,agent_org_id,form_page_code,process_define_key from announcement_article where update_time >= '${dt}' and update_time < CURRENT_DATE";
//        Properties properties = new Properties();
//        if (StringUtils.isNotBlank(di_table_info)) {
//            try (ByteArrayInputStream input = new ByteArrayInputStream(di_table_info.getBytes("UTF-8"))) {
//                properties.load(input);
//            } catch (UnsupportedEncodingException ex) {
////                LOGGER.warn("parse di_table_info error", ex);
//            }
//        }
//        String di_query = properties.getProperty("di.query");
//        di_query = di_query.trim().toLowerCase();
//        String src_columns = di_query.substring(di_query.indexOf("select ") + 6, di_query.indexOf(" from ")).trim();
//        int where_idx = di_query.indexOf(" where ");
//        String src_where = null;
//        if (where_idx > 0) {
//            src_where = di_query.substring(where_idx + 6).trim();
//        }
//        return;
//    }


//    public static void main(String[] args) throws IOException {
//        String old_introduce_condition = "max_filter_ratio=0.1,sourceTable=ads.ads_sinan_supplier_selfprovince_summary";
//        Map<String, String> destPropMap = Maps.newHashMap();
//        Splitter.on(",").trimResults().omitEmptyStrings()
//                .splitToList(old_introduce_condition)
//                .stream().forEach(temp_condition -> {
//                    List<String> cond_array = Splitter.on("=").trimResults().omitEmptyStrings().splitToList(temp_condition);
//                    destPropMap.put(cond_array.get(0), cond_array.get(1));
//                });
//
//        String old_source_table = destPropMap.get("sourceTable");
//        return;
//    }

    public static void main(String[] args) throws IOException {
        String old_source_sql = "select \n" +
                "  id \n" +
                "  ,announcement_id\n" +
                "  ,CONVERT(meta_data USING utf8) as meta_data\n" +
                "  ,create_at\n" +
                "  ,update_at\n" +
                "from announcement_ext\n" +
                "where  update_at  >= date_sub(CURRENT_DATE, interval 1 day) \n" +
                "and update_at < CURRENT_DATE";
        List<String> srcCols = Lists.newArrayList();
//        old_source_sql = old_source_sql.trim().replace("\\n", " ");
        String src_columns_str = old_source_sql.substring(old_source_sql.indexOf("select ") + 6, old_source_sql.indexOf("from "));
//        src_columns_str = src_columns_str.replace("\\n", " ").trim();
        Splitter.on(",").trimResults().omitEmptyStrings()
                .splitToList(src_columns_str)
                .stream().forEach(temp_column -> {
                    String column = temp_column;
                    if (temp_column.indexOf(".") > 0) {
                        column = column.substring(temp_column.indexOf(".") + 1);
                    }
                    if (column.indexOf("--") > 0) {
                        column = column.substring(0, column.indexOf("--"));
                        srcCols.add(column.trim());
                    }
                });
        return;
    }

}
