import React, { useEffect, useState } from 'react';
import ProForm, { ModalForm, ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Space, Table } from 'antd';
import { PageContainer } from '@/components';

import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';

import styles from './index.less';
import { TaskListItem } from '@/types/tasks';
import { getTasks, publishTask, rejectTask } from '@/services/task';
import { getEnumValues, getTaskTypes } from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import { TaskCategory, VersionStatus } from '@/constants/datadev';
import { TaskType } from '@/types/datadev';
import moment from 'moment';

const { Item } = Form;
const { TextArea } = Input;
const ruleText = [{ required: true, message: '请输入' }];
type ActionType = 'approve' | 'reject';

const DataSource: FC = () => {
  const [data, setData] = useState<TaskListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [taskTypes, setTaskTypes] = useState<TaskType[]>([]);
  const [actionRecords, setActionRecords] = useState<TaskListItem[]>([]);
  const [actionType, setActionType] = useState<ActionType>('approve');
  const [visibleSubmit, setVisibleSubmit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    getTasksWrapped(0);
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
      .catch((err) => {});
    getTaskTypes({ catalog: TaskCategory.DI })
      .then((res) => setTaskTypes(res.data))
      .catch((err) => {});
  }, []);

  const getTasksWrapped = (offset: number) => {
    const params = form.getFieldsValue();
    setLoading(true);
    getTasks({ ...params, limit: 10, offset, publishStatus: VersionStatus.TO_BE_PUBLISHED })
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const columns: ColumnsType<TaskListItem> = [
    { title: '数仓分层', key: 'dwLayerValue', dataIndex: 'dwLayerValue' },
    { title: '任务名称', key: 'jobName', dataIndex: 'jobName' },
    { title: '任务版本', key: 'jobContentVersionDisplay', dataIndex: 'jobContentVersionDisplay' },
    { title: '任务环境', key: 'environment', dataIndex: 'environment' },
    { title: '作业类型', key: 'jobTypeCode', dataIndex: 'jobTypeCode' },
    { title: '提交人', key: 'creator', dataIndex: 'creator' },
    {
      title: '提交备注',
      key: 'approveRemark',
      dataIndex: 'approveRemark',
      render: (_) => _ || '-',
    },
    {
      title: '提交时间',
      key: 'approveTime',
      dataIndex: 'approveTime',
      render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
    },
    {
      title: '操作',
      key: 'ops',
      dataIndex: 'ops',
      fixed: 'right',
      render: (value, record) => (
        <Space size={16}>
          <a onClick={() => onAction([record], 'approve')}>发布</a>
          <a onClick={() => onAction([record], 'reject')}>驳回</a>
        </Space>
      ),
    },
  ];

  const onAction = (records: TaskListItem[], actionType: ActionType) => {
    setActionType(actionType);
    setVisibleSubmit(true);
    setActionRecords(records);
  };

  const onPushlish = async (remark: string) => {
    const recordIds = actionRecords.map((_) => _.id);
    publishTask({ recordIds, remark })
      .then((res) => {
        if (res.success) {
          message.success('发布成功');
          setVisibleSubmit(false);
          getTasksWrapped(0);
        } else {
          message.error(`发布失败：${res.msg}`);
        }
      })
      .catch((err) => {});
  };

  const onReject = async (remark: string) => {
    const recordIds = actionRecords.map((_) => _.id);
    rejectTask({ recordIds, remark })
      .then((res) => {
        if (res.success) {
          message.success('驳回成功');
          setVisibleSubmit(false);
          getTasksWrapped(0);
        } else {
          message.error(`驳回失败：${res.msg}`);
        }
      })
      .catch((err) => {});
  };

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormSelect
          name="dwLayerCode"
          label="数仓分层"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={layers.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
        />
        <ProFormSelect
          name="environment"
          label="任务环境"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={[
            { label: 'stag', value: Environments.STAG },
            { label: 'prod', value: Environments.PROD },
          ]}
        />
        <ProFormSelect
          name="jobTypeCode"
          label="作业类型"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={taskTypes.map((_) => ({ label: _.name, value: _.code }))}
        />
        <ProFormText
          name="approveOperator"
          label="提交人"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
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
      <div>
        <Button
          onClick={() => {
            if (actionRecords.length) {
              setActionType('approve');
              setVisibleSubmit(true);
            } else {
              message.info('请选择任务');
            }
          }}
        >
          批量发布
        </Button>
      </div>
      <Table<TaskListItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => getTasksWrapped(10 * (page - 1)),
        }}
        rowSelection={{
          onChange: (selectedRowKeys: React.Key[], selectedRows: TaskListItem[]) => {
            console.log(selectedRowKeys, selectedRows);

            setActionRecords(selectedRows);
          },
        }}
      />
      <ModalForm
        title="提交新版本"
        layout="horizontal"
        width={536}
        labelCol={{ span: 6 }}
        colon={false}
        preserve={false}
        modalProps={{ destroyOnClose: true, onCancel: () => setVisibleSubmit(false) }}
        visible={visibleSubmit}
        submitter={{
          submitButtonProps: { size: 'large' },
          resetButtonProps: { size: 'large' },
        }}
        onFinish={async (values) => {
          if (actionType === 'approve') {
            await onPushlish(values.remark);
          }
          if (actionType === 'reject') {
            await onReject(values.remark);
          }
          return true;
        }}
      >
        <Item name="remark" label="变更说明" rules={ruleText} style={{ marginBottom: 0 }}>
          <TextArea placeholder="请输入" />
        </Item>
      </ModalForm>
    </PageContainer>
  );
};

export default DataSource;
