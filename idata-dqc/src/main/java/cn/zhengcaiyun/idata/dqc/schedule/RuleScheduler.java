package cn.zhengcaiyun.idata.dqc.schedule;

import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorTemplateEnum;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.utils.DateUtils;
import cn.zhengcaiyun.idata.dqc.utils.ParameterUtils;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
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
    private TableService tableService;

    @Autowired
    private MessageSendService messageSendService;

    @Scheduled(fixedDelay = 1000 * 5)
    public void schedule() {
        List<String> types = Lists.newArrayList(MonitorTemplateEnum.TABLE_OUTPUT_TIME.getValue());
        int startIndex = 0;

        //普通规则
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
        //基线规则
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
            Date now = new Date();
            String curDay = DateUtils.format(now, "yyyy-MM-dd");
            Date deadline = DateUtils.parse(DateUtils.format(now, "yyyy-MM-dd " + rule.getContent()), "yyyy-MM-dd HH:mm:ss");

            //当前时间跟设置时间比较
            if (now.compareTo(deadline) > 0) {
                String partitionExpr = rule.getPartitionExpr();
                String partition = "";
                if (StringUtils.isNotEmpty(partitionExpr)) {
                    partition = ParameterUtils.dateTemplateParse(partitionExpr, new Date());
                }

                String[] arr = rule.getTableName().split("\\.");
                HiveTable hiveTable = tableService.getTableInfo(arr[0], arr[1], partition);
                String tableAccessDay = DateUtils.format(new Date(hiveTable.getModifyTime()), "yyyy-MM-dd");

                //默认数据每天更新一次
                if (!curDay.equals(tableAccessDay)) {
                    String[] nicknames = rule.getAlarmReceivers().split(",");
                    String message = String.format("根据你在数据质量平台上配置的规则{%s}，监测到表%s的数据产出时间已超过设置时间%s",
                            rule.getName(), rule.getContent());
                    messageSendService.send(RuleUtils.getAlarmTypes(rule.getAlarmLevel()), nicknames, message);
                }

                monitorRuleService.updateAccessTime(rule.getId(), curDay);
            }
        }
    }

}
