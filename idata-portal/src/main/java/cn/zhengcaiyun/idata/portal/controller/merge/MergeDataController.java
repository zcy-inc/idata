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

package cn.zhengcaiyun.idata.portal.controller.merge;

import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;
import cn.zhengcaiyun.idata.merge.data.service.*;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 16:18
 **/
@RestController
@RequestMapping(path = "/p0/merge")
public class MergeDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeDataController.class);

    @Autowired
    private DatasourceMigrationService datasourceMigrationService;
    @Autowired
    private FolderMigrationService folderMigrationService;
    @Autowired
    private FunctionMigrationService functionMigrationService;
    @Autowired
    private ModelMigrationService modelMigrationService;
    @Autowired
    private DAGMigrationService dagMigrationService;
    @Autowired
    private JobMigrationService jobMigrationService;

    @GetMapping("/data")
    public void mergeData(@RequestParam String mergeModules,
                          HttpServletResponse response) {
        LabelQueryDataDto queryDataDto = null;
        SXSSFWorkbook workbook = downloadAsExcel(queryDataDto);
        String fileName = queryDataDto.getLabelName() + "_" + queryDataDto.getLayerName() + "_" + System.currentTimeMillis();
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", String.format("attachment;filename=%s.xlsx",
                    new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)));
            ServletOutputStream sos = response.getOutputStream();
            workbook.write(sos);
            sos.flush();
            sos.close();
            response.flushBuffer();
            workbook.dispose();
        } catch (IOException ex) {
            LOGGER.warn("exportLabelResultData failed. ex: {}.", ex);
        }
    }

    private SXSSFWorkbook downloadAsExcel(LabelQueryDataDto queryDataDto) {
        List<ColumnDto> columns = null;
        List<List<String>> data = null;
        if (!Objects.isNull(queryDataDto)) {
            columns = queryDataDto.getColumns();
            data = queryDataDto.getData();
        }
        columns = MoreObjects.firstNonNull(columns, Lists.newArrayList());
        data = MoreObjects.firstNonNull(data, Lists.newArrayList());

        SXSSFWorkbook workbook = new SXSSFWorkbook(200);
        workbook.setCompressTempFiles(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("label data"));
        for (int columnIdx = 0; columnIdx < columns.size(); columnIdx++) {
            ColumnDto columnDto = columns.get(columnIdx);
            Row row = sheet.getRow(0);
            if (row == null) {
                row = sheet.createRow(0);
            }
            Cell cell = row.createCell(columnIdx);
            cell.setCellValue(columnDto.getColumnName());
            cell.setCellStyle(cellStyle);
        }

        for (int rowIdx = 0; rowIdx < data.size(); rowIdx++) {
            List<String> rowDataList = data.get(rowIdx);
            for (int columnIdx = 0; columnIdx < rowDataList.size(); columnIdx++) {
                Object value = rowDataList.get(columnIdx);
                Row row = sheet.getRow(rowIdx + 1);
                if (row == null) {
                    row = sheet.createRow(rowIdx + 1);
                }
                Cell cell = row.createCell(columnIdx);
                cell.setCellValue(value == null ? null : value.toString());
                cell.setCellStyle(cellStyle);
            }
        }
        return workbook;
    }

}
