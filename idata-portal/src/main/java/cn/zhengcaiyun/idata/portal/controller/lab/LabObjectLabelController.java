package cn.zhengcaiyun.idata.portal.controller.lab;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;
import cn.zhengcaiyun.idata.label.dto.LabObjectLabelDto;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;
import cn.zhengcaiyun.idata.label.service.LabObjectLabelService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description: 标签controller
 * @author: yangjianhua
 * @create: 2021-06-22 14:34
 **/
@RestController
@RequestMapping(path = "/p1/lab")
public class LabObjectLabelController {
    private static final Logger logger = LoggerFactory.getLogger(LabObjectLabelController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private LabObjectLabelService objectLabelService;

    @PostMapping("/objectLabel")
    public RestResult<Long> createLabel(@RequestBody LabObjectLabelDto labelDto, HttpServletRequest request) {
        return RestResult.success(objectLabelService.createLabel(labelDto, tokenService.getNickname(request)));
    }

    @PutMapping("/objectLabel")
    public RestResult<Long> editLabel(@RequestBody LabObjectLabelDto labelDto, HttpServletRequest request) {
        return RestResult.success(objectLabelService.editLabel(labelDto, tokenService.getNickname(request)));
    }

    @GetMapping("/objectLabel/{id}")
    public RestResult<LabObjectLabelDto> getLabel(@PathVariable Long id) {
        return RestResult.success(objectLabelService.getLabel(id));
    }

    @DeleteMapping("/objectLabel/{id}")
    public RestResult<Boolean> deleteLabel(@PathVariable("id") Long id, HttpServletRequest request) {
        return RestResult.success(objectLabelService.deleteLabel(id, tokenService.getNickname(request)));
    }

    @GetMapping("/objectLabel/{id}/layer/{layerId}/queryData")
    public RestResult<LabelQueryDataDto> queryLabelResultData(@PathVariable("id") Long id,
                                                              @PathVariable("layerId") Long layerId) {
        return RestResult.success(objectLabelService.queryLabelResultData(id, layerId, 1L, null));
    }

    @GetMapping("/objectLabel/{id}/layer/{layerId}/exportData")
    public void exportLabelResultData(@PathVariable("id") Long id,
                                      @PathVariable("layerId") Long layerId,
                                      HttpServletResponse response) {
        LabelQueryDataDto queryDataDto = objectLabelService.queryLabelResultData(id, layerId, 50000L, null);
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
            logger.warn("exportLabelResultData failed. ex: {}.", ex);
        }
    }

    private SXSSFWorkbook downloadAsExcel(LabelQueryDataDto queryDataDto) {
        List<ColumnDto> columns = null;
        List<List<String>> data = null;
        if (queryDataDto != null) {
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
