import React, { useEffect, useRef, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import { Button, Form, message, Modal, Space, Table, Tabs } from 'antd';
import { PageContainer } from '@/components';

import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';

import styles from './index.less';
import { ApprovalListItem } from '@/types/measure';
import { getIndexList, publishIndex, rejectIndex } from '@/services/measure';
import { publishListStatusMode } from '@/constants/datadev';
import { defaultWaitColumns, defaultColumns } from './utils';

const { confirm } = Modal;
const { TabPane } = Tabs;
type ActionType = 'approve' | 'reject';

const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
];

const publishIndexStatus = {
  [publishListStatusMode.FINISHTASK]:[2,3,4], // 已处理
  [publishListStatusMode.WAITINGTASK]:[1], // 待处理
}

const DataSource: FC = () => {
  const [data, setData] = useState<ApprovalListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [actionRecords, setActionRecords] = useState<ApprovalListItem[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<ApprovalListItem[]>([]);
  const [columns, setColumns] = useState<ColumnsType<ApprovalListItem>>(defaultWaitColumns);
  const [activeTabKey, setActiveTabKey] = useState<publishListStatusMode>(publishListStatusMode.WAITINGTASK);
  const [loading, setLoading] = useState(false);
  const [rowSelection, setRowSelection] = useState({});
  const [form] = Form.useForm();

  const limit = useRef(10);

  const getIndexWrapped = (offset: number) => {
    setActionRecords([]); // 清空选中
    setSelectedRowKeys([]);
    const params = form.getFieldsValue();
    setLoading(true);
    getIndexList({ ...params, limit: limit.current, offset, statusList: publishIndexStatus[activeTabKey] })
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .catch((e) => {
        console.error(e);
        setTotal(0);
        setData([]);
      })
      .finally(() => setLoading(false));
  };

  // 发布
  const onPublish = async (records?: ApprovalListItem[]) => {
    const list = records || actionRecords;
    const ids = list.map((_) => _.id);
    publishIndex({ ids })
      .then((res) => {
        if (res.success) {
          message.success('发布成功');
          getIndexWrapped(0);
        }
      })
  };

  // 驳回
  const onReject = async (records?: ApprovalListItem[]) => {
    const list = records || actionRecords;
    const recordIds = list.map((_) => _.id);
    rejectIndex({ recordIds })
      .then((res) => {
        if (res.success) {
          message.success('驳回成功');
          getIndexWrapped(0);
        }
      })
  };

  const onPublishOrReject = (records: ApprovalListItem[], type: ActionType) => {
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

  const operateColumn = {
    title: '操作',
    key: 'ops',
    dataIndex: 'ops',
    fixed: 'right',
    render: (value: any, record: ApprovalListItem) => (
      <Space size={16}>
        <a onClick={() => onPublishOrReject([record], 'approve')}>发布</a>
        <a onClick={() => onPublishOrReject([record], 'reject')}>驳回</a>
      </Space>
    ),
  }

  // 是否存在操作按钮
  const columnHasOperate = () => {
    return columns[columns.length - 1].key === 'ops'
  }

  // 动态处理表格Columns数据
  useEffect(() => {
    getIndexWrapped(0);
    // 待处理
    if (activeTabKey === publishListStatusMode.WAITINGTASK && !columnHasOperate()) {
      setColumns([...defaultWaitColumns, operateColumn])
    }
    // 已处理 已处理tab下不需要操作栏
    if (activeTabKey === publishListStatusMode.FINISHTASK && columnHasOperate()) {
      setColumns(defaultColumns)
    }
  }, [activeTabKey]);


  useEffect(() => {
    if (activeTabKey === publishListStatusMode.WAITINGTASK) {
      setRowSelection({
        rowSelection: {
          onChange: (selectedRowKeys: React.Key[], selectedRows: ApprovalListItem[]) => {
            setActionRecords(selectedRows);
            setSelectedRowKeys(selectedRowKeys);
          },
          selectedRowKeys: selectedRowKeys,
        }
      });
    } else {
      setRowSelection({});
    }

  }, [activeTabKey, actionRecords]);

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormText
          name="metricNamePattern"
          label="指标名称"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
        />
        <ProFormSelect
          name="metricTag"
          label="指标类型"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={MetricTypeOps}
        />
        <ProFormText
          name="submitOperatorName"
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
            getIndexWrapped(0);
          }}
        >
          重置
        </Button>
        <Button
          type="primary"
          size="large"
          icon={<SearchOutlined />}
          style={{ margin: '0 0 24px 16px' }}
          onClick={() => getIndexWrapped(0)}
        >
          查询
        </Button>
      </ProForm>
      <Tabs
        className="reset-tabs"
        activeKey={activeTabKey}
        style={{ marginTop: 8 }}
        onChange={(status) => setActiveTabKey(status)}
      >
        <TabPane tab="待处理" key={publishListStatusMode.WAITINGTASK} />
        <TabPane tab="已处理" key={publishListStatusMode.FINISHTASK} />
      </Tabs>
      {
        activeTabKey === publishListStatusMode.WAITINGTASK && <div>
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
      }
      <Table<ApprovalListItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page, pageSize) => {
            limit.current = pageSize;
            getIndexWrapped(10 * (page - 1))
          },
        }}
        {
        ...rowSelection
        }
      />
    </PageContainer>
  );
};

export default DataSource;
