import React, { useEffect, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Button, Form, Table, Popconfirm, message } from 'antd';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import { PageContainer } from '@/components';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDialog from '@/utils/showDialog';

import type { TemplateItem } from '@/types/quality';
import { getMonitorList, addMonitor, deleteMonitor } from '@/services/quality';
import { alarmLevelList } from '@/constants/quality'

import styles from './index.less';


const Monitor: FC<{history: any}> = ({ history }) => {
  const [data, setData] = useState<TemplateItem[]>([]);
  const [curPage, setCurPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    getTasksWrapped(1);
  }, []);

  const getTasksWrapped = (pageNum: number = curPage) => {
    const params = form.getFieldsValue();
    const condition: any = {
      tableName: params.tableName,
      alarmLevel: params.alarmLevel,
      baselineId: -1
    };
    setLoading(true);
    getMonitorList({ pageSize: 10, curPage: pageNum, ...condition })
      .then((res) => {
        console.log(res);
        setTotal(res.data.totalElements);
        setData(res.data.data);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const handleAddMonitor = () => {
   
  }

  const handleDelete = (row: TemplateItem) => {
    deleteMonitor({id: row.id, isBaseline: false}).then(() => {
      message.success('删除成功');
      getTasksWrapped();
    })
  }

  const columns: ColumnsType<TemplateItem> = [
    { title: '表英文名', key: 'tableName', dataIndex: 'tableName' },
    { title: '表中文名', key: 'comment', dataIndex: 'comment'},
    { title: '最新执行时间', key: 'accessTime', dataIndex: 'accessTime' },
    {
      title: '实时告警情况',
      key: 'latestAlarmLevel',
      dataIndex: 'latestAlarmLevel',
      render: (_) => alarmLevelList.find(item => item.value === _)?.label || '-'
    },
    { title: '启用规则数量', key: 'ruleCount', dataIndex: 'ruleCount' },
    {
      title: '操作',
      key: 'amContainerLogsUrl',
      dataIndex: 'amContainerLogsUrl',
      width: 120,
      fixed: 'right',
      render: (_, row) => (
        <>
          <Button type="link" onClick={() => history.push(`/quality/monitor/edit/${row.id}/${row.tableName}`)}>
            编辑
          </Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(row)}>
            <Button danger type="text">
              删除
            </Button>
          </Popconfirm>
        </>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormText
          name="tableName"
          label="表名称"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
        />
        <ProFormSelect
          name="alarmLevel"
          label="告警等级"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={alarmLevelList}
        />
        <Button
          size="large"
          icon={<ReloadOutlined />}
          style={{ margin: '0 0 24px 14px' }}
          onClick={() => {
            form.resetFields();
            getTasksWrapped(1);
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
        <Button onClick={handleAddMonitor}>新增监控</Button>
        <Button style={{float: 'right'}}>监控日志</Button>
      </div>
      <Table<TemplateItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          showSizeChanger: false,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => setCurPage(page),
        }}
      />
    </PageContainer>
  );
};

export default Monitor;
