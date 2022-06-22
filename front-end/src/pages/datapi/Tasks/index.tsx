import React, { useEffect, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import { Button, Form, message, Modal, Space, Table,Tabs } from 'antd';
import { PageContainer } from '@/components';

import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';

import styles from './index.less';
import { TaskListItem } from '@/types/tasks';
import { getTasks, publishTask, rejectTask } from '@/services/task';
import { getEnumValues, getTaskTypes } from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import { publishListStatusMode, publishListStatus } from '@/constants/datadev';
import { TaskType } from '@/types/datadev';
import moment from 'moment';

const { confirm } = Modal;
const { TabPane } = Tabs;
type ActionType = 'approve' | 'reject';

const defaultColumns: ColumnsType<TaskListItem> = [
  { title: '数仓分层', key: 'dwLayerValue', dataIndex: 'dwLayerValue' },
  { title: '任务名称', key: 'jobName', dataIndex: 'jobName' },
  { title: '任务版本', key: 'jobContentVersionDisplay', dataIndex: 'jobContentVersionDisplay' },
  { title: '任务环境', key: 'environment', dataIndex: 'environment' },
  { title: '任务类型', key: 'jobTypeCode', dataIndex: 'jobTypeCode' },
  { title: '提交人', key: 'creator', dataIndex: 'creator' },
  {
    title: '提交备注',
    key: 'submitRemark',
    dataIndex: 'submitRemark',
    render: (_) => _ || '-',
  },
  {
    title: '提交时间',
    key: 'createTime',
    dataIndex: 'createTime',
    render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
  },
];

const DataSource: FC = () => {
  const [data, setData] = useState<TaskListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [taskTypes, setTaskTypes] = useState<TaskType[]>([]);
  const [actionRecords, setActionRecords] = useState<TaskListItem[]>([]);
  const [columns, setColumns] = useState<ColumnsType<TaskListItem>>(defaultColumns);
  const [activeTabKey, setActiveTabKey] = useState<publishListStatusMode>(publishListStatusMode.WAITINGTASK);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const getTasksWrapped = (offset: number) => {
    const params = form.getFieldsValue();
    setLoading(true);
    getTasks({ ...params, limit: 10, offset, publishStatusList: publishListStatus[activeTabKey]})
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
    getTaskTypes().then((res) => setTaskTypes(res.data))
  }, []);



  const onPublish = async (records?: TaskListItem[]) => {
    const list = records || actionRecords;
    const recordIds = list.map((_) => _.id);
    publishTask({ recordIds })
      .then((res) => {
        if (res.success) {
          message.success('发布成功');
          getTasksWrapped(0);
        }
      })
  };

  const onReject = async (records?: TaskListItem[]) => {
    const list = records || actionRecords;
    const recordIds = list.map((_) => _.id);
    rejectTask({ recordIds })
      .then((res) => {
        if (res.success) {
          message.success('驳回成功');
          getTasksWrapped(0);
        }
      })
  };

  const onPublishOrReject = (records: TaskListItem[], type: ActionType) => {
    confirm({
      title: '确认',
      content: `您确认要${type === 'approve' ? '发布' : '驳回'}吗？`,
      onOk: () => {
        if (type === 'approve') {
          onPublish(records);
        }
        if (type === 'reject') {
          onReject(records);
        }
      },
    });
  };

  const operateColumn =  {
    title: '操作',
    key: 'ops',
    dataIndex: 'ops',
    fixed: 'right',
    render: (value, record: TaskListItem) => (
      <Space size={16}>
        <a onClick={() => onPublishOrReject([record], 'approve')}>发布</a>
        <a onClick={() => onPublishOrReject([record], 'reject')}>驳回</a>
      </Space>
    ),
  }

  const columnHasOperate = ()=> {
    return columns[columns.length-1].key === 'ops'
  }

  useEffect(() => {
    getTasksWrapped(0);
    // 判断activeTabKey,拼接操作栏目,已处理tab下不需要操作栏
    if(activeTabKey===publishListStatusMode.WAITINGTASK&&!columnHasOperate()) setColumns(publishList =>[...publishList, operateColumn])
    if(activeTabKey===publishListStatusMode.FINISHTASK&&columnHasOperate()) setColumns(publishList => [...publishList.slice(0,publishList.length-1)])
  }, [activeTabKey]);

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
          label="任务类型"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={taskTypes.map((_) => ({ label: _.code, value: _.code }))}
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
              onPublish();
            } else {
              message.info('请选择任务');
            }
          }}
        >
          批量发布
        </Button>
      </div>
      <Tabs
        className="reset-tabs"
        activeKey={activeTabKey}
        style={{ marginTop: 8 }}
        onChange={(status) => setActiveTabKey(status)}
      >
        <TabPane tab="待处理" key={publishListStatusMode.WAITINGTASK}/>
        <TabPane tab="已处理" key={publishListStatusMode.FINISHTASK}/>
      </Tabs>
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
            setActionRecords(selectedRows);
          },
        }}
      />
    </PageContainer>
  );
};

export default DataSource;
