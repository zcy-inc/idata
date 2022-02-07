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

import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.enums.MigrateItemEnum;
import cn.zhengcaiyun.idata.mergedata.service.*;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-12 16:18
 **/
@RestController
@RequestMapping(path = "/p1/merge")
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
    @Autowired
    private ModifyDIJobNameService modifyDIJobNameService;

    @GetMapping("/data")
    public void mergeData(@RequestParam String mergeModules, HttpServletResponse response) {
        if (StringUtils.isBlank(mergeModules))
            throw new BizProcessException("需要指定迁移模块：" + Joiner.on(",").join(MigrateItemEnum.values()));
        List<String> modules = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(mergeModules);

        boolean mergeAll = modules.contains(MigrateItemEnum.all.name());
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        if (mergeAll || modules.contains(MigrateItemEnum.datasource.name())) {
            resultDtoList.addAll(datasourceMigrationService.migrateDatasource());
        }
        if (mergeAll || modules.contains(MigrateItemEnum.folder.name())) {
            resultDtoList.addAll(folderMigrationService.migrate());
        }
        if (mergeAll || modules.contains(MigrateItemEnum.model.name())) {
            modelMigrationService.syncModelMigration();
        }
        if (mergeAll || modules.contains(MigrateItemEnum.dag.name())) {
            resultDtoList.addAll(dagMigrationService.migrateFolder());
            resultDtoList.addAll(dagMigrationService.migrateDAG());
        }
        if (mergeAll || modules.contains(MigrateItemEnum.function.name())) {
            resultDtoList.addAll(functionMigrationService.migrate());
        }
        if (mergeAll || modules.contains(MigrateItemEnum.job.name())) {
            resultDtoList.addAll(jobMigrationService.migrate());
        }
        if (modules.contains(MigrateItemEnum.modify_di_job_name.name())) {
            resultDtoList.addAll(modifyDIJobNameService.modify());
        }

        if (CollectionUtils.isEmpty(resultDtoList))
            return;

        SXSSFWorkbook workbook = downloadAsExcel(resultDtoList);
        String fileName = "merge_failed_data_" + System.currentTimeMillis();
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

    private SXSSFWorkbook downloadAsExcel(List<MigrateResultDto> resultDtoList) {
        List<String> columns = Lists.newArrayList("migrateType", "reason", "data");

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("merge failed data"));
        for (int columnIdx = 0; columnIdx < columns.size(); columnIdx++) {
            Row row = sheet.getRow(0);
            if (row == null) {
                row = sheet.createRow(0);
            }
            Cell cell = row.createCell(columnIdx);
            cell.setCellValue(columns.get(columnIdx));
            cell.setCellStyle(cellStyle);
        }

        for (int rowIdx = 0; rowIdx < resultDtoList.size(); rowIdx++) {
            Row row = sheet.getRow(rowIdx + 1);
            if (row == null) {
                row = sheet.createRow(rowIdx + 1);
            }

            MigrateResultDto rowData = resultDtoList.get(rowIdx);
            Cell cell_0 = row.createCell(0);
            cell_0.setCellValue(rowData.getMigrateType());
            cell_0.setCellStyle(cellStyle);

            Cell cell_1 = row.createCell(1);
            cell_1.setCellValue(rowData.getReason());
            cell_1.setCellStyle(cellStyle);

            Cell cell_2 = row.createCell(2);
            cell_2.setCellValue(rowData.getData());
            cell_2.setCellStyle(cellStyle);
        }
        return workbook;
    }

}
