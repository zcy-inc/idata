package cn.zhengcaiyun.idata.portal.controller.open;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.common.constant.SystemConfigConstant;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * TODO 数据质量 目前是空实现，为了适配htool调用
 */
@RestController
@RequestMapping(path = "/p0/quality/")
public class OpenDataQualityController {

    /**
     * TODO 目前是空实现，为了适配htool调用
     */
    @PostMapping("/monitorHistory")
    public RestResult add(@RequestBody DataQualityDto dataQualityDto) {
        return RestResult.success();
    }

    public static class DataQualityDto {
        private Long alarmId;
        private Long tableId;
        private String monitorSql;
        // 表主键，联合主键,分隔
        private String pkColumnName;
        private String columnName;
        private String output;

        public Long getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(Long alarmId) {
            this.alarmId = alarmId;
        }

        public Long getTableId() {
            return tableId;
        }

        public void setTableId(Long tableId) {
            this.tableId = tableId;
        }

        public String getMonitorSql() {
            return monitorSql;
        }

        public void setMonitorSql(String monitorSql) {
            this.monitorSql = monitorSql;
        }

        public String getPkColumnName() {
            return pkColumnName;
        }

        public void setPkColumnName(String pkColumnName) {
            this.pkColumnName = pkColumnName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }
    }
}
