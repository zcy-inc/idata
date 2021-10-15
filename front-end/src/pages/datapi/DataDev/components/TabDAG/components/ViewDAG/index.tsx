import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { Descriptions } from 'antd';
import { get } from 'lodash';
import type { FC } from 'react';

import { DAG, Folder } from '@/types/datadev';
import { FolderBelong, PeriodRange, TriggerMode } from '@/constants/datadev';
import Title from '@/components/Title';
import { getEnumValues, getFolders } from '@/services/datadev';

interface ViewDAGProps {
  data?: DAG;
}

const { Item } = Descriptions;
const format = 'YYYY-MM-DD HH:mm:ss';
const MapPeriodRangeTranslated = {
  [PeriodRange.YEAR]: '年',
  [PeriodRange.MONTH]: '月',
  [PeriodRange.WEEK]: '周',
  [PeriodRange.DAY]: '日',
  [PeriodRange.HOUR]: '时',
  [PeriodRange.MINUTE]: '分',
};

const ViewDAG: FC<ViewDAGProps> = ({ data }) => {
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [folders, setFolders] = useState<Folder[]>([]);

  useEffect(() => {
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' }).then((res) => setLayers(res.data));
    getFolders({ belong: FolderBelong.DAG }).then((res) => setFolders(res.data));
  }, []);

  const renderPeriod = () => {
    const cron = get(data, 'dagScheduleDto.cronExpression', '').split(' ');
    // 0秒 / 1分 / 2时 / 3日(月) / 4月 / 5日(星期) / 6年
    switch (data?.dagScheduleDto.periodRange) {
      case PeriodRange.YEAR:
        return (
          <>
            <Item label="指定月份">{cron[4] || '-'}</Item>
            <Item label="指定日期">{cron[3] || '-'}</Item>
            <Item label="定时调度">{`${cron[2]}:${cron[1]}` || '-'}</Item>
          </>
        );
      case PeriodRange.MONTH:
        return (
          <>
            <Item label="指定日期">{cron[3] || '-'}</Item>
            <Item label="定时调度">{`${cron[2]}:${cron[1]}` || '-'}</Item>
          </>
        );
      case PeriodRange.WEEK:
        return (
          <>
            <Item label="指定星期">{cron[5] || '-'}</Item>
            <Item label="定时调度">{`${cron[2]}:${cron[1]}` || '-'}</Item>
          </>
        );
      case PeriodRange.DAY:
        return <Item label="定时调度">{`${cron[2]}:${cron[1]}` || '-'}</Item>;
      case PeriodRange.HOUR:
        if (data.dagScheduleDto.triggerMode === TriggerMode.INTERVAL) {
          const cronStartHour = cron[2].split('-')[0];
          const cronEndHour = cron[2].split('-')[1].split('/')[0];
          const cronInterval = cron[2].split('-')[1].split('/')[1];
          return (
            <>
              <Item label="方式选择">{data.dagScheduleDto.triggerMode || '-'}</Item>
              <Item label="开始时间">
                {moment(`${cron[1]}:${cronStartHour}`).format(format) || '-'}
              </Item>
              <Item label="时间间隔">{cronInterval || '-'}</Item>
              <Item label="结束时间">{cronEndHour || '-'}</Item>
            </>
          );
        }
        if (data.dagScheduleDto.triggerMode === TriggerMode.POINT) {
          return (
            <>
              <Item label="方式选择">{data.dagScheduleDto.triggerMode || '-'}</Item>
              <Item label="指定小时">{cron[2] || '-'}</Item>
            </>
          );
        }
      case PeriodRange.MINUTE:
        const cronInterval = cron[1].split('/')[1];
        const cronHours = cron[2].split('-');
        return (
          <>
            <Item label="开始时间">{cronHours[0] || '-'}</Item>
            <Item label="时间间隔">{cronInterval || '-'}</Item>
            <Item label="结束时间">{cronHours[1] || '-'}</Item>
          </>
        );

      default:
        return null;
    }
  };

  return (
    <div>
      <Title>DAG信息</Title>
      <Descriptions colon={false} column={1}>
        <Item label="DAG名称">{data?.dagInfoDto.name || '-'}</Item>
        <Item label="数仓分层">
          {layers.find((_) => _.valueCode === data?.dagInfoDto.dwLayerCode)?.enumValue || '-'}
        </Item>
        <Item label="目标文件夹">
          {folders.find((_) => _.id === data?.dagInfoDto.folderId)?.name || '-'}
        </Item>
        <Item label="始止时间">
          {moment(data?.dagScheduleDto.beginTime).format(format)}
          {' ~ '}
          {moment(data?.dagScheduleDto.endTime).format(format)}
        </Item>
        <Item label="调度周期">
          {MapPeriodRangeTranslated[data?.dagScheduleDto.periodRange as PeriodRange] || '-'}
        </Item>
        {renderPeriod()}
        <Item label="CRON表达式">{data?.dagScheduleDto.cronExpression}</Item>
      </Descriptions>
    </div>
  );
};

export default ViewDAG;
