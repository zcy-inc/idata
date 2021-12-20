import React, { useEffect, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Button, Form, Table, Tabs } from 'antd';
import { IconFont, PageContainer } from '@/components';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';
import styles from './index.less';

import { TaskTypes } from '@/constants/datadev';
import { getOverhang } from '@/services/operations';
import { Overhang } from '@/types/operations';

const { TabPane } = Tabs;

const ClusterMonitor: FC = () => {
  const [data, setData] = useState<Overhang[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {}, []);

  const getTasksWrapped = (offset: number) => {
    const params = form.getFieldsValue();
    setLoading(true);
    getOverhang({ ...params, limit: 10, offset })
      .then((res) => {
        setTotal(res.data.overhangJobDtoPage.total);
        setData(res.data.overhangJobDtoPage.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const columns: ColumnsType<Overhang> = [
    { title: '作业ID', key: 'id', dataIndex: 'id' },
    { title: '作业名称', key: 'name', dataIndex: 'name' },
    { title: '作业类型', key: 'jobType', dataIndex: 'jobType' },
    { title: '作业所有者', key: 'creator', dataIndex: 'creator' },
  ];

  return (
    <PageContainer>
      <Tabs>
        <TabPane tab="悬垂作业" key="overhang">
          <div className={styles.toolbar}>
            <IconFont type="icon-info" style={{ fontSize: 16 }} />
            <span
              style={{ marginLeft: 4 }}
            >{`悬垂作业: 既没有作业依赖又没有API依赖的作业列表（计算时间: ${'2021-12-10 00:00:00'}）`}</span>
          </div>
          <ProForm
            form={form}
            className={styles.form}
            layout="inline"
            colon={false}
            submitter={false}
          >
            <ProFormText
              name="name"
              label="作业名称"
              placeholder="请输入"
              fieldProps={{ style: { width: 200 }, size: 'large' }}
            />
            <ProFormSelect
              name="jobType"
              label="作业类型"
              placeholder="请选择"
              fieldProps={{ style: { width: 200 }, size: 'large' }}
              options={Object.values(TaskTypes).map((_) => ({ label: _, value: _ }))}
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
          <Table<Overhang>
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
          />
        </TabPane>
      </Tabs>
    </PageContainer>
  );
};

export default ClusterMonitor;
