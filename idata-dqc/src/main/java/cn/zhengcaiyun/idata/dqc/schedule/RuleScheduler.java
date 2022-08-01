package cn.zhengcaiyun.idata.dqc.schedule;

import cn.zhengcaiyun.idata.connector.spi.hdfs.HiveTable;
import cn.zhengcaiyun.idata.dqc.common.MessageSendService;
import cn.zhengcaiyun.idata.dqc.model.entity.MonitorHistory;
import cn.zhengcaiyun.idata.dqc.model.enums.MonitorTemplateEnum;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorRuleVO;
import cn.zhengcaiyun.idata.dqc.service.MonitorHistoryService;
import cn.zhengcaiyun.idata.dqc.service.MonitorRuleService;
import cn.zhengcaiyun.idata.dqc.service.TableService;
import cn.zhengcaiyun.idata.dqc.utils.DateUtils;
import cn.zhengcaiyun.idata.dqc.utils.ParameterUtils;
import cn.zhengcaiyun.idata.dqc.utils.RuleUtils;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MonitorHistoryService monitorHistoryService;

    @Scheduled(fixedDelay = 1000 * 5)
    public void schedule() {
        List<Long> templateIdList = Lists.newArrayList(MonitorTemplateEnum.TABLE_OUTPUT_TIME.getId());
        int startIndex = 0;

        //普通规则
        while (true) {
            List<MonitorRuleVO> ruleList = monitorRuleService.getScheduleRuleList(templateIdList, startIndex);
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
            List<MonitorRuleVO> ruleList = monitorRuleService.getBaselineScheduleRuleList(templateIdList, startIndex);
            if (CollectionUtils.isEmpty(ruleList)) {
                break;
            }
            for (MonitorRuleVO rule : ruleList) {
                this.check(rule);
            }
        }
    }

    //    @Transactional(rollbackFor = Exception.class)
    public void check(MonitorRuleVO rule) {
        if (MonitorTemplateEnum.TABLE_OUTPUT_TIME.getId().equals(rule.getTemplateId())) {
            Date now = new Date();
            String curDay = DateUtils.format(now, "yyyy-MM-dd");

            //上锁
            if (!monitorRuleService.updateAccessTime(rule.getId(), curDay)) {
                return;
            }

            Date deadline = DateUtils.parse(DateUtils.format(now, "yyyy-MM-dd " + rule.getContent() + ":00"), "yyyy-MM-dd HH:mm:ss");

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
                Integer alarm = 0;
                if (!curDay.equals(tableAccessDay)) {
                    alarm = 1;

                    String[] nicknames = rule.getAlarmReceivers().split(",");
                    String message = String.format("根据你在数据质量平台上配置的规则[%s]，监测到表[%s]的数据产出时间已超过设置时间%s",
                            rule.getName(), rule.getContent());

                    messageSendService.send(RuleUtils.getAlarmTypes(rule.getAlarmLevel()), nicknames, message);
                }


                MonitorHistory monitorHistory = new MonitorHistory();
                monitorHistory.setTableName(rule.getTableName());
                monitorHistory.setPartition(partition);
                monitorHistory.setRuleId(rule.getId());
                monitorHistory.setRuleName(rule.getName());
                monitorHistory.setRuleType(rule.getRuleType());
                monitorHistory.setMonitorObj(rule.getMonitorObj());
//                monitorHistory.setDataValue();
//                monitorHistory.setSql();
//                monitorHistory.setRuleValue();
                monitorHistory.setVersion(rule.getVersion());
                monitorHistory.setCreator("系统管理员");
                monitorHistory.setEditor("系统管理员");
                monitorHistory.setAlarmLevel(rule.getAlarmLevel());
                monitorHistory.setAlarmReceivers(rule.getAlarmReceivers());
                monitorHistory.setContent(rule.getContent());
                monitorHistory.setAlarm(alarm);

                monitorHistoryService.insert(monitorHistory);
            }
        }
    }

}
