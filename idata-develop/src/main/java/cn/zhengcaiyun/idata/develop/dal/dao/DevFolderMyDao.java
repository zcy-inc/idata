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
package cn.zhengcaiyun.idata.develop.dal.dao;

import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-02 17:40
 */

@Mapper
public interface DevFolderMyDao {

    @Select("<script>" +
                "SELECT id AS folderId, folder_name AS name, (@type := 'FOLDER') AS type, " +
                    "(@fileCode := null) AS fileCode, parent_id AS parentId, (@cid := concat('F_', id)) AS cid, " +
                    "folder_type AS folderType " +
                "FROM dev_folder " +
                "WHERE del = 0 " +
                "<if test = 'treeNodeName != null'>" +
                    "AND dev_folder.folder_name like '%${treeNodeName}%' " +
                "</if>" +
                "<if test = 'devTreeType != null'>" +
                    "AND dev_folder.folder_type like '${devTreeType}' " +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"TABLE\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, table_name AS name, (@type := 'TABLE') AS type, " +
                            "id AS fileCode, folder_id AS parentId, (@cid := concat('T_', id)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_table_info " +
                        "WHERE del = 0 " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_table_info.table_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"LABEL\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, label_name AS name, (@type := 'LABEL') AS type, " +
                            "label_code AS fileCode, folder_id AS parentId, (@cid := concat('L_', label_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 AND label_tag NOT LIKE 'DIMENSION%' AND label_tag NOT LIKE 'MODIFIER%' " +
                            "AND label_tag NOT LIKE '%METRIC%' " +
                            "<if test = 'treeNodeName != null'>" +
                                "AND dev_label_define.label_name like '%${treeNodeName}%' " +
                            "</if>" +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"ENUM\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, enum_name AS name, (@type := 'ENUM') AS type, " +
                            "enum_code AS fileCode,  folder_id AS parentId, (@cid := concat('E_', enum_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_enum " +
                        "WHERE del = 0 " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_enum.enum_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"DIMENSION\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, label_name AS name, (@type := 'DIMENSION_LABEL') AS type, " +
                            "label_code AS fileCode, folder_id AS parentId, (@cid := concat('L_', label_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 AND label_tag LIKE 'DIMENSION_LABEL%' " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_label_define.label_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"MODIFIER\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, label_name AS name, (@type := 'MODIFIER_LABEL') AS type, " +
                            "label_code AS fileCode, folder_id AS parentId, (@cid := concat('L_', label_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 AND label_tag LIKE 'MODIFIER_LABEL%' " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_label_define.label_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "<if test = 'devTreeType != null and devTreeType.indexOf(\"METRIC\") != -1'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, label_name AS name, (@type := 'METRIC_LABEL') AS type, " +
                            "label_code AS fileCode, folder_id AS parentId, (@cid := concat('L_', label_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 AND label_tag LIKE '%_METRIC_LABEL%' " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_label_define.label_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "<if test = 'devTreeType == null or \"\" == devTreeType'>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, table_name AS name, (@type := 'TABLE') AS type, " +
                            "id AS fileCode, folder_id AS parentId, (@cid := concat('T_', id)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_table_info " +
                        "WHERE del = 0 " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_table_info.table_name like '%${treeNodeName}%' " +
                        "</if>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, label_name AS name, (@type := 'LABEL') AS type, " +
                            "label_code AS fileCode, folder_id AS parentId, (@cid := concat('L_', label_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 " +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_label_define.label_name like '%${treeNodeName}%' " +
                        "</if>" +
                    "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, enum_name AS name, (@type := 'ENUM') AS type, " +
                            "enum_code AS fileCode,  folder_id AS parentId, (@cid := concat('E_', enum_code)) AS cid, " +
                            "(@folderType := null) AS folderType " +
                        "FROM dev_enum " +
                        "WHERE del = 0" +
                        "<if test = 'treeNodeName != null'>" +
                            "AND dev_enum.enum_name like '%${treeNodeName}%' " +
                        "</if>" +
                "</if>" +
                "ORDER BY folderId desc" +
            "</script>")
    List<DevelopFolderTreeNodeDto> getDevelopFolders(String devTreeType, String treeNodeName);
}
