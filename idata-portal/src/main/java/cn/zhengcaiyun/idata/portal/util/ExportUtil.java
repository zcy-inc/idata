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

package cn.zhengcaiyun.idata.portal.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-06 14:50
 **/
public class ExportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtil.class);

    /**
     * 导出文本文件
     *
     * @param response
     * @param txtContent
     * @param fileName
     */
    public static void writeToTxt(HttpServletResponse response, String txtContent, String fileName) {
        checkArgument(StringUtils.isNotBlank(txtContent), "导出数据为空");
        checkArgument(StringUtils.isNotBlank(fileName), "导出文件名为空");

        //设置响应的字符集
        response.setCharacterEncoding("utf-8");
        //设置响应内容的类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader(
                "Content-Disposition",
                "attachment; filename=" + genFileName(fileName) + ".txt");
        BufferedOutputStream bufferedOS = null;
        ServletOutputStream servletOS = null;
        try {
            servletOS = response.getOutputStream();
            bufferedOS = new BufferedOutputStream(servletOS);
            bufferedOS.write(txtContent.getBytes("UTF-8"));
            bufferedOS.flush();
        } catch (Exception ex) {
            LOGGER.warn("导出文件错误，ex:{}", ex);
        } finally {
            try {
                bufferedOS.close();
                servletOS.close();
            } catch (Exception ex2) {
                LOGGER.warn("关闭流对象错误 ex:{}", ex2);
            }
        }
    }

    private static String genFileName(String fileName) {
        return fileName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
