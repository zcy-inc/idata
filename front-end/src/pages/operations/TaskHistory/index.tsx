import React, { useEffect, useState } from 'react';
import moment from 'moment';
import ProForm, { ProFormDateRangePicker, ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Button, Form, Table } from 'antd';
import { PageContainer } from '@/components';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';
import styles from './index.less';

import { JobHistoryItem } from '@/types/operations';
import { getJobHistory } from '@/services/operations';

const format = 'YYYY-MM-DD HH:mm:ss';
const formatYMD = 'YYYY-MM-DD';

const TaskHistory: FC = () => {
  const [data, setData] = useState<JobHistoryItem[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    getTasksWrapped(1);
  }, []);

  const getTasksWrapped = (pageNum: number) => {
    const params = form.getFieldsValue();
    const condition: any = {
      jobName: params.jobName,
      jobStatus: params.jobStatus,
    };
    if (params.startTime) {
      const startDateBegin = `${moment(params.startTime[0]).format(formatYMD)} 00:00:00`;
      const startDateEnd = `${moment(params.startTime[1]).format(formatYMD)} 23:59:59`;
      Object.assign(condition, { startDateBegin, startDateEnd });
    }
    if (params.finishTime) {
      const finishDateBegin = `${moment(params.finishTime[0]).format(formatYMD)} 00:00:00`;
      const finishDateEnd = `${moment(params.finishTime[1]).format(formatYMD)} 23:59:59`;
      Object.assign(condition, { finishDateBegin, finishDateEnd });
    }
    setLoading(true);
    getJobHistory({ pageSize: 10, pageNum, condition })
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const renderBusinessStatus = (businessStatus: number) => {
    switch (businessStatus) {
      case 1:
        return '等待运行';
      case 2:
        return '运行中';
      case 6:
        return '失败';
      case 7:
        return '成功';
      case -1:
      default:
        return '其他';
    }
  };

  const columns: ColumnsType<JobHistoryItem> = [
    { title: '作业ID', key: 'jobId', dataIndex: 'jobId' },
    { title: '作业名称', key: 'jobName', dataIndex: 'jobName', render: (_) => _ || '-' },
    { title: '耗时（分）', key: 'duration', dataIndex: 'duration' },
    {
      title: '开始时间',
      key: 'startTime',
      dataIndex: 'startTime',
      render: (_) => moment(_).format(format),
    },
    {
      title: '完成时间',
      key: 'finishTime',
      dataIndex: 'finishTime',
      render: (_) => moment(_).format(format),
    },
    { title: '所有者', key: 'user', dataIndex: 'user', render: (_) => _ || '-' },
    { title: '集群应用ID', key: 'applicationId', dataIndex: 'applicationId' },
    { title: '平均内存（GB）', key: 'avgMemory', dataIndex: 'avgMemory' },
    { title: '平均CPU核数', key: 'avgVcores', dataIndex: 'avgVcores' },
    {
      title: '最终状态',
      key: 'businessStatus',
      dataIndex: 'businessStatus',
      render: (_) => renderBusinessStatus(_),
    },
    {
      title: '操作',
      key: 'businessLogsUrl',
      dataIndex: 'businessLogsUrl',
      fixed: 'right',
      render: (_) => (
        <a href={_} target="_blank">
          查看日志
        </a>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormDateRangePicker name="startTime" label="开始时间" fieldProps={{ size: 'large' }} />
        <ProFormDateRangePicker name="finishTime" label="完成时间" fieldProps={{ size: 'large' }} />
        <ProFormText
          name="jobName"
          label="作业"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
        />
        <ProFormSelect
          name="jobStatus"
          label="状态"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={[
            { label: '队列中', value: 1 },
            { label: '运行中', value: 2 },
            { label: '失败', value: 6 },
            { label: '成功', value: 7 },
            { label: '其他', value: -1 },
          ]}
        />
        <Button
          size="large"
          icon={<ReloadOutlined />}
          style={{ margin: '0 0 24px 14px' }}
          onClick={() => {
            form.resetFields();
            getTasksWrapped(0);
          }}
        >
          重置
        </Button>
        <Button
          type="primary"
          size="large"
          icon={<SearchOutlined />}
          style={{ margin: '0 0 24px 16px' }}
          onClick={() => getTasksWrapped(0)}
        >
          查询
        </Button>
      </ProForm>
      <Table<JobHistoryItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => getTasksWrapped(page),
        }}
      />
    </PageContainer>
  );
};

export default TaskHistory;
