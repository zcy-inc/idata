import React, { useEffect, useState } from 'react';
import type { FC } from 'react';
import { getLogs, tryRunMonitorRule } from '@/services/quality';
import { Spin } from 'antd';
import { alarmLevelList } from '@/constants/quality';
import type { LogItem } from '@/types/quality';

const AddMonitor: FC<{params: any; type: 1 | 2}> = ({params, type}, ref) => {
  const [contents, setContents] = useState<LogItem []>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getContent();
  }, [])

  const getContent = () => {
    setLoading(true);
    const handler = type === 1 ? getLogs : tryRunMonitorRule;
    handler(params).then(res => {
      setContents(res.data);
    }).finally(() => {
      setLoading(false);
    })
  }

  const getAlarmLevel = (item: LogItem) => {
    if(item.alarm === 0) {
      return '监控[未告警]'
    }
    return `监控[告警], 告警等级[${alarmLevelList.find(alarm => alarm.value === item.alarmLevel)?.label || '未知'}]`
  }

  const getResult = (log: LogItem) => {
    if(log.ruleType === "system" && log.ruleName==='表产出时间') {
      return log.content;
    } else {
      return log.fixValue || `${log.rangeStart}-${log.rangeEnd}`;
    }
  }

  return (
   <Spin spinning={loading}>
      {contents.map((item, index) => <div style={{marginBottom: 10}} key={index}>
        {`[${item.createTime}] ${item.comment}[${item.tableName}]，监控规则[${item.ruleName}]，规则内容[${getResult(item)}]，监控结果[${item.dataValue}]，${getAlarmLevel(item)}。`}
      </div>)}
   </Spin>
  );
};

export default AddMonitor;
