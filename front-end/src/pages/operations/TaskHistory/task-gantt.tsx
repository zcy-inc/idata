import type { FC } from 'react';
import React, { useState, useEffect } from 'react';
import ProForm, { ProFormDatePicker, ProFormSelect } from '@ant-design/pro-form';
import { SearchOutlined } from '@ant-design/icons';
import Gantt from '../../../components/Gantt';
import { Button, Form, Spin, Pagination } from 'antd';
import moment from 'moment';
import { getJobGantt, getDagList, getEnumgList } from '@/services/operations';
import styles from './task-list.less';
import  './task-gantt.less';

const timeFormat = (time: number) => {
  const oneSecond = 1000;
  const oneMinute = oneSecond * 60;
  const oneHour = oneMinute * 60;
  const hour = Math.floor(time / oneHour);
  const minute = Math.floor((time % oneHour) / oneMinute)
  const second = Math.floor((time % oneMinute) / oneSecond);
  return `${hour < 10 ? '0'+ hour : hour}:${minute < 10 ? '0'+ minute : minute}:${second < 10 ? '0'+ second : second}`
}

const initConfig = {
  readonly: true,
  duration_unit: 'minute',
  min_column_width: 160,
  columns: [
    {
      name: 'id',
      label: 'ID',
      width: 60,
      template: (obj: any) => `<span class="gantt-tag">${obj.id}</span>`,
    },
    {
      name: 'text',
      label: '作业名称',
      width: 200,
      template: (obj: any) => `<span class="task-gantt-name">${obj.text || ''}</span>`,
    },
  ]
}

const initTemplate = {
  task_class: function (start: Date, end: Date, task: any) {
    return task.businessStatus ? `milestone-${task.businessStatus}` : '';
  },
   // 自定义tooltip内容
  tooltip_text: function (start: Date, end: Date, task: any, mouse_on_grid: boolean) {
    const output = `<ul class="task-gantt-tooltip">
      <li class="task-gantt-tooltip-item name">${task.text}</li>
      <li class="task-gantt-tooltip-item">开始时间：${moment(task.startTime).format('YYYY-MM-DD HH:mm:ss')}</li>
      <li class="task-gantt-tooltip-item">结束时间：${moment(task.finishTime).format('YYYY-MM-DD HH:mm:ss')}</li>
      <li class="task-gantt-tooltip-item">运行时长：${timeFormat(task._duration)}</li>
      <li class="task-gantt-tooltip-item">
        <a href="${task.businessLogsUrl}" class="gantt-log-url" target="_blank">查看日志</a>
      </li>
    </ul>`
    return mouse_on_grid ? output : '';
  }
}

const GanttChart: FC = ({ }) => {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [data, setData] = useState<Record<string, any> []>([]);
  const [total, setTotal] = useState(0);
  const [current, setCurrent] = useState(1);
  const [pageSize, setPageSize] = useState(100);
  const [dagList, setDagList] = useState<{label: string, value: number | string} []>([])
  const [enumList, setEnumList] = useState<{label: string, value: number | string} []>([])

  const getTasksWrapped = () => {
    const params = form.getFieldsValue();
    const condition: any = {
      ...params,
      startDate: params.startDate.format('YYYY-MM-DD'), //'2021-12-21',
    };
    setLoading(true);
    getJobGantt({ pageSize, pageNum: current, condition })
      .then((res) => {
        setTotal(res.data.total);
        let tasks: Record<string, any> [] = [];
        res.data.content.forEach(item => {
          tasks.push({
            id: item.jobId,
            text: item.jobName,
            start_date: moment(item.children[0].start_date).format('DD-MM-YYYY HH:mm:ss'),
            render:"split"
          })
          if(item.children && item.children.length) {
            const childTasks = item.children.map(item2 => ({
              id: `${item.jobId}-${item2.id}`,
              text: item.jobName,
              startTime: item2.startTime,
              finishTime: item2.finishTime,
              start_date: moment(item2.startTime).format('DD-MM-YYYY HH:mm:ss'),
              end_date: moment(item2.finishTime).format('DD-MM-YYYY HH:mm:ss'),
              duration: item2.duration / 1000 / 60,
              _duration: item2.duration,
              businessStatus: item2.businessStatus,
              businessLogsUrl: item2.businessLogsUrl,
              parent: item.jobId,
              progress: 1,
            }))
            tasks = tasks.concat(childTasks);
          }
        })
        setData(tasks);
      })
      .finally(() => setLoading(false));
  };

  // 获取下拉依赖项
  const getDependices = () => {
    const p1 = getDagList();
    const p2 = getEnumgList();
    Promise.all([p1, p2]).then(([{data: dagRes}, {data: enumRes}]) => {
      setDagList(dagRes.map(item => ({label: item.name,value: item.id})))
      setEnumList(enumRes.map(item => ({label: item.enumValue,value: item.id})))
    })
  }

  const pageChange = (c: React.SetStateAction<number>, p: React.SetStateAction<number>) => {
    setCurrent(c);
    setPageSize(p);
  }

  useEffect(() => {
    getDependices();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    getTasksWrapped();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [current, pageSize]);

  return (
    <div className='job-history-gantt'>
      <ProForm
        form={form}
        className={styles.form}
        layout="inline"
        colon={false}
        submitter={false}
        initialValues={{
          startDate: moment('2021-12-21'),
      } }
      >
        <ProFormDatePicker name="startDate" label="开始时间" fieldProps={{style: { width: 200 }, size: 'large' }} />
        <ProFormSelect
          name="layerCode"
          label="数仓分层"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={enumList}
        />
        <ProFormSelect
          name="dagId"
          label="DAG"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={dagList}
        />
        <Button
          type="primary"
          size="large"
          icon={<SearchOutlined />}
          style={{ margin: '0 0 24px 16px' }}
          onClick={() => getTasksWrapped()}
        >
          查询
        </Button>
      </ProForm>
      <Spin spinning={loading} >
        <Gantt tasks={{data}} config={initConfig} templates={initTemplate} zoom='day' />
        <Pagination
          current={current}
          pageSize={pageSize}
          total={total}
          pageSizeOptions={['100', '200', '300']}
          showQuickJumper
          showSizeChanger
          className='ant-table-pagination-right ant-table-pagination'
          onChange={pageChange}
        />
      </Spin>
    </div>
  );
};

export default GanttChart;
