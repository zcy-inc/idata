import React, { useEffect, useState } from 'react';
import ProForm, {
  ProFormDateTimeRangePicker,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTimePicker,
} from '@ant-design/pro-form';
import { Form } from 'antd';
import moment from 'moment';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import { Tip } from '@/components';
import { getDAGStatus } from '@/utils/datadev';
import styles from './index.less';

import { dayOptions, hourOptions, minuteOptions, monthOptions, weekOptions } from './constants';
import { IconFont } from '@/components';
import { DAG, Folder } from '@/types/datadev';
import { FolderBelong, PeriodRange, TriggerMode } from '@/constants/datadev';
import { getEnumValues, getFolders } from '@/services/datadev';
import { getEnvironments } from '@/services/datasource';
import { Environments } from '@/constants/datasource';

interface EditDAGProps {
  data?: DAG;
  form: FormInstance;
  renderCronExpression: (values: any) => string;
}

const width = 400;
const format = 'HH:mm';
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];
const { Item } = Form;

const EditDAG: FC<EditDAGProps> = ({ data, form, renderCronExpression }) => {
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [folders, setFolders] = useState<Folder[]>([]);
  const [envs, setEnvs] = useState<Environments[]>([]);
  const [periodRange, setPeriodRange] = useState<PeriodRange>(PeriodRange.YEAR);
  const [triggerMode, setTriggerMode] = useState(TriggerMode.INTERVAL);
  const [cronExpression, setCronExpression] = useState('');

  useEffect(() => {
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
      .catch((err) => {});
    getFolders({ belong: FolderBelong.DAG })
      .then((res) => setFolders(res.data))
      .catch((err) => {});
    getEnvironments()
      .then((res) => setEnvs(res.data))
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (data) {
      const initial = {
        name: data.dagInfoDto.name,
        environment: data.dagInfoDto.environment,
        dwLayerCode: data.dagInfoDto.dwLayerCode,
        folderId: data.dagInfoDto.folderId,
        range: [data.dagScheduleDto.beginTime, data.dagScheduleDto.endTime],
        periodRange: data.dagScheduleDto.periodRange,
      };
      const cron = data.dagScheduleDto.cronExpression.split(' ');
      setCronExpression(data.dagScheduleDto.cronExpression);
      setPeriodRange(data.dagScheduleDto.periodRange);
      // 0秒 / 1分 / 2时 / 3日(月) / 4月 / 5日(星期) / 6年
      switch (data.dagScheduleDto.periodRange) {
        case PeriodRange.YEAR:
          Object.assign(initial, {
            month: cron[4].split(','),
            dayofmonth: cron[3].split(','),
            time: moment(`${cron[2]}:${cron[1]}`, format),
          });
          break;
        case PeriodRange.MONTH:
          Object.assign(initial, {
            dayofmonth: cron[3].split(','),
            time: moment(`${cron[2]}:${cron[1]}`, format),
          });
          break;
        case PeriodRange.WEEK:
          Object.assign(initial, {
            dayofweek: cron[5].split(','),
            time: moment(`${cron[2]}:${cron[1]}`, format),
          });
          break;
        case PeriodRange.DAY:
          Object.assign(initial, {
            time: moment(`${cron[2]}:${cron[1]}`, format),
          });
          break;
        case PeriodRange.HOUR:
          if (data.dagScheduleDto.triggerMode === TriggerMode.INTERVAL) {
            const cronStartHour = cron[2].split('-')[0];
            const cronEndHour = cron[2].split('-')[1].split('/')[0];
            const cronInterval = cron[2].split('-')[1].split('/')[1];
            Object.assign(initial, {
              triggerMode: data.dagScheduleDto.triggerMode,
              start: moment(`${cron[1]}:${cronStartHour}`, format),
              interval: cronInterval,
              end: moment(cronEndHour, 'HH'),
            });
          }
          if (data.dagScheduleDto.triggerMode === TriggerMode.POINT) {
            Object.assign(initial, {
              triggerMode: data.dagScheduleDto.triggerMode,
              hours: cron[2].split(','),
            });
          }
          break;
        case PeriodRange.MINUTE:
          const cronInterval = cron[1].split('/')[1];
          const cronHours = cron[2].split('-');
          Object.assign(initial, {
            start: moment(cronHours[0], 'HH'),
            interval: cronInterval,
            end: moment(cronHours[1], 'HH'),
          });
          break;

        default:
          break;
      }
      form.setFieldsValue(initial);
    }
  }, [data]);

  const renderPeriod = () => {
    switch (periodRange) {
      case PeriodRange.YEAR:
        return (
          <>
            <ProFormSelect
              name="month"
              label="指定月份"
              placeholder="请选择"
              options={monthOptions}
              fieldProps={{ size: 'large', mode: 'multiple', style: { width } }}
              rules={ruleSlct}
            />
            <ProFormSelect
              name="dayofmonth"
              label="指定日期"
              placeholder="请选择"
              options={dayOptions}
              fieldProps={{ size: 'large', mode: 'multiple', style: { width } }}
              rules={ruleSlct}
            />
            <ProFormTimePicker
              name="time"
              label="定时调度"
              fieldProps={{ size: 'large', format, style: { width } }}
              rules={ruleSlct}
            />
          </>
        );
      case PeriodRange.MONTH:
        return (
          <>
            <ProFormSelect
              name="dayofmonth"
              label="指定日期"
              placeholder="请选择"
              options={dayOptions}
              fieldProps={{ size: 'large', mode: 'multiple', style: { width } }}
              rules={ruleSlct}
            />
            <ProFormTimePicker
              name="time"
              label="定时调度"
              fieldProps={{ size: 'large', format, style: { width } }}
              rules={ruleSlct}
            />
          </>
        );
      case PeriodRange.WEEK:
        return (
          <>
            <ProFormSelect
              name="dayofweek"
              label="指定星期"
              placeholder="请选择"
              options={weekOptions}
              fieldProps={{ size: 'large', mode: 'multiple', style: { width } }}
              rules={ruleSlct}
            />
            <ProFormTimePicker
              name="time"
              label="定时调度"
              fieldProps={{ size: 'large', format, style: { width } }}
              rules={ruleSlct}
            />
          </>
        );
      case PeriodRange.DAY:
        return (
          <ProFormTimePicker
            name="time"
            label="定时调度"
            fieldProps={{ size: 'large', format, style: { width } }}
            rules={ruleSlct}
          />
        );
      case PeriodRange.HOUR:
        return (
          <>
            <ProFormRadio.Group
              name="triggerMode"
              label="方式选择"
              initialValue={TriggerMode.INTERVAL}
              options={[
                { label: '时间间隔', value: TriggerMode.INTERVAL },
                { label: '指定时间', value: TriggerMode.POINT },
              ]}
              rules={ruleSlct}
              fieldProps={{ onChange: ({ target: { value } }) => setTriggerMode(value) }}
            />
            {triggerMode === TriggerMode.INTERVAL && (
              <>
                <ProFormTimePicker
                  name="start"
                  label="开始时间"
                  fieldProps={{ size: 'large', format, style: { width } }}
                  rules={ruleSlct}
                />
                <ProFormText
                  name="interval"
                  label="时间间隔"
                  placeholder="请输入"
                  fieldProps={{ size: 'large', style: { width }, suffix: '时' }}
                  rules={ruleText}
                />
                <ProFormTimePicker
                  name="end"
                  label="结束时间"
                  fieldProps={{ size: 'large', format: 'HH', style: { width } }}
                  rules={ruleSlct}
                />
              </>
            )}
            {triggerMode === TriggerMode.POINT && (
              <ProFormSelect
                name="hours"
                label="指定小时"
                placeholder="请选择"
                options={hourOptions}
                fieldProps={{ size: 'large', mode: 'multiple', style: { width } }}
                rules={ruleSlct}
              />
            )}
          </>
        );
      case PeriodRange.MINUTE:
        return (
          <>
            <ProFormTimePicker
              name="start"
              label="开始时间"
              fieldProps={{ size: 'large', format: 'HH', style: { width } }}
              rules={ruleSlct}
            />
            <ProFormSelect
              name="interval"
              label="时间间隔"
              placeholder="分"
              fieldProps={{ size: 'large', style: { width } }}
              options={minuteOptions}
              rules={ruleSlct}
            />
            <ProFormTimePicker
              name="end"
              label="结束时间"
              fieldProps={{ size: 'large', format: 'HH', style: { width } }}
              rules={ruleSlct}
            />
          </>
        );

      default:
        return null;
    }
  };

  const renderCronExpressionWrapped = () => {
    const values = form.getFieldsValue();
    const cron = renderCronExpression(values);
    setCronExpression(cron);
  };

  return (
    <div className={styles['edit-dag']}>
      <Tip label="当前状态" content={getDAGStatus(data?.dagInfoDto.status)} style={{ marginBottom: 20 }} />
      <ProForm
        form={form}
        layout="horizontal"
        colon={false}
        submitter={false}
        onValuesChange={renderCronExpressionWrapped}
      >
        <ProFormText
          name="name"
          label="DAG名称"
          placeholder="请输入"
          fieldProps={{ size: 'large', style: { width } }}
          rules={ruleText}
        />
        <ProFormSelect
          name="environment"
          label="环境"
          placeholder="请选择"
          options={envs.map((_) => ({ label: _, value: _ }))}
          fieldProps={{ size: 'large', style: { width } }}
          rules={ruleSlct}
        />
        <ProFormSelect
          name="dwLayerCode"
          label="数仓分层"
          placeholder="请选择"
          options={layers.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
          fieldProps={{ size: 'large', style: { width } }}
          rules={ruleSlct}
        />
        <ProFormSelect
          name="folderId"
          label="目标文件夹"
          placeholder="请选择"
          options={folders.map((_) => ({ label: _.name, value: _.id }))}
          fieldProps={{
            size: 'large',
            style: { width },
            showSearch: true,
            filterOption: (input: string, option: any) => option.label.indexOf(input) >= 0,
          }}
          rules={ruleSlct}
        />
        <ProFormDateTimeRangePicker
          name="range"
          label="始止时间"
          fieldProps={{
            size: 'large',
            style: { width },
            suffixIcon: <IconFont type="icon-rili" />,
            format: 'YYYY-MM-DD HH:mm',
          }}
          rules={ruleSlct}
        />
        <ProFormRadio.Group
          name="periodRange"
          label="调度周期"
          initialValue="year"
          options={[
            { label: '年', value: PeriodRange.YEAR },
            { label: '月', value: PeriodRange.MONTH },
            { label: '周', value: PeriodRange.WEEK },
            { label: '日', value: PeriodRange.DAY },
            { label: '时', value: PeriodRange.HOUR },
            { label: '分', value: PeriodRange.MINUTE },
          ]}
          radioType="button"
          rules={ruleSlct}
          fieldProps={{ onChange: ({ target: { value } }) => setPeriodRange(value) }}
        />
        {renderPeriod()}
        <Item label="CRON表达式">
          <span style={{ color: '#737b96' }}>{cronExpression || '-'}</span>
        </Item>
      </ProForm>
    </div>
  );
};

export default EditDAG;
