package cn.zhengcaiyun.idata.dqc.schedule;

import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorTemplateEnum;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.HiveTableService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.utils.DateUtils;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RuleScheduler {
    @Autowired
    private MonitorRuleService monitorRuleService;

    @Autowired
    private HiveTableService hiveTableService;

    @Scheduled(fixedDelay = 1000 * 5)
    public void schedule() {
        //todo 根据table_output_time 获取templateId，从rule表捞规则
        List<String> types = Lists.newArrayList(MonitorTemplateEnum.TABLE_OUTPUT_TIME.getValue());
        int startIndex = 0;
        while (true) {
            List<MonitorRuleVO> ruleList = monitorRuleService.getScheduleRuleList(types, startIndex, false);
            if (CollectionUtils.isEmpty(ruleList)) {
                break;
            }
            for (MonitorRuleVO rule : ruleList) {
                this.check(rule);
            }
        }

        startIndex = 0;
        while (true) {
            List<MonitorRuleVO> ruleList = monitorRuleService.getScheduleRuleList(types, startIndex, false);
            if (CollectionUtils.isEmpty(ruleList)) {
                break;
            }
            for (MonitorRuleVO rule : ruleList) {
                this.check(rule);
            }
        }
    }

    private void check(MonitorRuleVO rule) {
        if (MonitorTemplateEnum.TABLE_OUTPUT_TIME.getValue().equals(rule.getContent())) {
            String partitionExpr = rule.getPartitionExpr();
            String partition = "";
            if (StringUtils.isNotEmpty(partitionExpr)) {
                //todo
            }

            String[] arr = rule.getTableName().split("\\.");
            HiveTable hiveTable = hiveTableService.getTableInfo(arr[0], arr[1], partition);
            Date deadline = DateUtils.convertDate(DateUtils.formateDate(new Date(), "yyyy-MM-dd " + rule.getContent()), "yyyy-MM-dd HH:mm:ss");
            Date tableAccessTime = new Date(hiveTable.getModifyTime());
            if (deadline.compareTo(tableAccessTime) > 0) {
                //告警
            }

        }
    }

    private void updateAccessTime(Long id) {
//        monitorRuleService.update()
    }
}
