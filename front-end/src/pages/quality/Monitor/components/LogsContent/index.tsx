import React, { useEffect, useState } from 'react';
import type { FC } from 'react';
import { getLogs } from '@/services/quality';
import { Spin, Empty } from 'antd';
import { alarmLevelList } from '@/constants/quality';
import type { LogItem } from '@/types/quality';
import moment from 'moment';

const AddMonitor: FC<{params: any;}> = ({params = {}}, ref) => {
  const [contents, setContents] = useState<LogItem []>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getContent();
  }, [])

  const getContent = () => {
    setLoading(true);
    getLogs(params).then(res => {
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

  // 拼接规则内容模板
  const getRuleContent = (log: LogItem) => {
    if(!log.compareType && log.rangeStart) {
      return `规则内容[${log.rangeStart}-${log.rangeEnd}]`;
    } else if(log.compareType) {
      const compareType = log.compareType === 'up' ? '上浮' : log.compareType === 'down' ? '下降' : log.compareType;
      log.fixValue = log.fixValue || '';
      if(!log.rangeStart) {
        return `规则内容[${compareType + log.fixValue}]`;
      } else {
        return `规则内容[${log.fixValue}${compareType}${log.rangeStart ? `${log.rangeStart}%-${log.rangeEnd}%` : ''}]`;
      }
    } else {
      return '';
    }
  }

  // 拼接运行结果
  const getRuleResult = (log: LogItem) => {
    log.dataValue = (log.dataValue || log.dataValue === 0) ? log.dataValue : '';
    if(log.compareType === 'up' ||log.compareType === 'down') {
      return log.ruleValue ? log.ruleValue + '%' : log.dataValue
    }
    return log.dataValue;

  }

  return (
   <Spin spinning={loading}>
    <div  style={{maxHeight: 400, overflow: 'auto'}}>
      {contents?.length ? contents.map((item, index) => <div style={{marginBottom: 10}} key={index}>
        {`[${moment(item.createTime).format('YYYY-MM-DD HH:mm:ss')}] [${item.tableName}]，监控规则[${item.ruleName}]，${getRuleContent(item)}，监控结果[${getRuleResult(item)}]，${getAlarmLevel(item)}。`}
      </div>) : <Empty description="暂无相关日志" />}
    </div>
   </Spin>
  );
};

export default AddMonitor;
