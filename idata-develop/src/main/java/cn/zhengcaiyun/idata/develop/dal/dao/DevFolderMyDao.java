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

import cn.zhengcaiyun.idata.dto.develop.folder.DevelopFolderTreeNodeDto;
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
                "SELECT id AS folderId, folder_name AS folderName, (@type := 'FOLDER') AS type, " +
                    "folder_name AS fileName, id AS fileCode, parent_id AS parentId " +
                "FROM dev_folder " +
                "WHERE del = 0 " +
                "<choose>" +
                    "<when test = 'devFolderType != null and \"TABLE\" == devFolderType'>" +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'TABLE') AS type, table_name AS fileName, id AS fileCode, folder_id AS parentId " +
                        "FROM dev_table_info " +
                        "WHERE del = 0 " +
                    "</when>" +
                    "<when test = 'devFolderType != null and \"LABEL\" == devFolderType'>" +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'LABEL') AS type, label_name AS fileName, label_code AS fileCode, folder_id AS parentId " +
                        "FROM dev_label_define " +
                        "WHERE del = 0" +
                    "</when>" +
                    "<when test = 'devFolderType != null and \"ENUM\" == devFolderType'>" +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'ENUM') AS type, enum_name AS fileName, enum_code AS fileCode,  folder_id AS parentId " +
                        "FROM dev_enum " +
                        "WHERE del = 0" +
                    "</when>" +
                    "<otherwise>" +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'TABLE') AS type, table_name AS fileName, id AS fileCode, folder_id AS parentId " +
                        "FROM dev_table_info " +
                        "WHERE del = 0 " +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'LABEL') AS type, label_name AS fileName, label_code AS fileCode, folder_id AS parentId " +
                        "FROM dev_label_define " +
                        "WHERE del = 0 " +
                        "UNION ALL " +
                        "SELECT (@folderId := null) AS folderId, (@folderName := null) AS folderName, " +
                            "(@type := 'ENUM') AS type, enum_name AS fileName, enum_code AS fileCode,  folder_id AS parentId " +
                        "FROM dev_enum " +
                        "WHERE del = 0" +
                    "</otherwise>" +
                "</choose>" +
            "</script>")
    List<DevelopFolderTreeNodeDto> getDevelopFolders(String devFolderType);
}
