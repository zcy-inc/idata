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
package cn.zhengcaiyun.idata.develop.dto.label;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-06-18 14:07
 */

public enum SysLabelCodeEnum {
    DB_NAME_LABEL("dbName:LABEL"),
    TBL_COMMENT_LABEL("tblComment:LABEL"),
    PK_LABEL("pk:LABEL"),
    COLUMN_TYPE_LABEL("columnType:LABEL"),
    COLUMN_COMMENT_LABEL("columnComment:LABEL");

    private String labelCode;

    SysLabelCodeEnum(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public static boolean checkSysLabelCode(String labelCode) {
        boolean isSysLabelCode = false;
        for (SysLabelCodeEnum sysLabelCode : SysLabelCodeEnum.values()) {
            if (sysLabelCode.getLabelCode().equals(labelCode)) {
                isSysLabelCode = true;
                break;
            }
        }
        return isSysLabelCode;
    }
}
