import React, { useEffect, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import { Button, Form, message, Modal, Space, Table, Tabs } from 'antd';
import { get } from 'lodash';
import { PageContainer } from '@/components';
import { CSVItem, DataSourceItem, DBConfigList } from '@/types/datasource';
import { DataSourceTypes, Environments } from '@/constants/datasource';
import {
  deleteDataSource,
  getCSVDataSourceList,
  getDataSourceList,
  getDataSourceTypes,
  getEnvironments,
} from '@/services/datasource';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';

import styles from './index.less';
import EditModal from './components/EditModal';

const { TabPane } = Tabs;

const DataSource: FC = () => {
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<DataSourceItem>();
  const [dsTypes, setDSTypes] = useState<DataSourceTypes[]>([]);
  const [envs, setEnvs] = useState<Environments[]>([]);
  const [list, setList] = useState<DataSourceItem[]>([]);
  const [total, setTotal] = useState(0);
  const [listCSV, setListCSV] = useState<CSVItem[]>([]);
  const [totalCSV, setTotalCSV] = useState(0);
  const [activeKey, setActiveKey] = useState('db');
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const renderConfig = (value: DBConfigList[], record: DataSourceItem) => {
    return (
      <div className={styles['table-double']}>
        {value.map((dbConfig) => (
          <div className={styles['table-double-cell']}>
            <div key="db">{`数据库：${dbConfig.dbName || '-'}`}</div>
            <div key="path">{`路径：${dbConfig.host || '-'}:${dbConfig.port || '-'}`}</div>
          </div>
        ))}
      </div>
    );
  };
  const renderEnv = (value: Environments[], record: DataSourceItem) => {
    const stag = record.dbConfigList.find((_) => _.env === Environments.STAG);
    const prod = record.dbConfigList.find((_) => _.env === Environments.PROD);
    if (stag?.dbName === prod?.dbName && stag?.schema === prod?.schema) {
      return value.join(' / ');
    } else {
      return (
        <div className={styles['table-double']}>
          {value.map((env) => (
            <div className={styles['table-double-cell']}>{env}</div>
          ))}
        </div>
      );
    }
  };

  const columns: ColumnsType<DataSourceItem> = [
    { title: '数据源名称', key: 'name', dataIndex: 'name' },
    { title: '数据源类型', key: 'type', dataIndex: 'type' },
    {
      title: '连接信息',
      key: 'dbConfigList',
      dataIndex: 'dbConfigList',
      ellipsis: true,
      width: 530,
      render: renderConfig,
    },
    { title: '最近更新时间', key: 'editTime', dataIndex: 'editTime' },
    { title: '最近更新人', key: 'editor', dataIndex: 'editor' },
    { title: '环境', key: 'envList', dataIndex: 'envList', render: renderEnv },
    {
      title: '操作',
      key: 'ops',
      dataIndex: 'ops',
      fixed: 'right',
      render: (value, record) => (
        <Space size={16}>
          <a onClick={() => onEdit(record)}>编辑</a>
          <a onClick={() => onDelete(record)}>删除</a>
        </Space>
      ),
    },
  ];
  const columnsCSV: ColumnsType<CSVItem> = [
    { title: '数据源名称', key: 'fileName', dataIndex: 'fileName' },
    { title: '数据源类型', key: 'type', render: () => 'csv' },
    { title: '连接信息', key: 'name', dataIndex: 'name' },
    { title: '最近更新时间', key: 'editTime', dataIndex: 'editTime' },
    { title: '最近更新人', key: 'editor', dataIndex: 'editor' },
    { title: '环境', key: 'envList', dataIndex: 'envList', render: (_) => _.join(' / ') },
  ];

  useEffect(() => {
    getDataSourceTypes().then((res) => {
      const types: DataSourceTypes[] = get(res, 'data', []);
      setDSTypes(types);
    });
    getEnvironments().then((res) => {
      const envs: Environments[] = get(res, 'data', []);
      setEnvs(envs);
    });
  }, []);

  useEffect(() => {
    activeKey === 'db' ? getList() : getCSVList();
  }, [activeKey]);

  const getList = () => {
    setLoading(true);
    const searchParams = form.getFieldsValue();
    getDataSourceList({ limit: 10, offset: 0, ...searchParams })
      .then((res) => {
        setList(res.data.content);
        setTotal(res.data.total);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const getCSVList = () => {
    setLoading(true);
    const searchParams = form.getFieldsValue();
    getCSVDataSourceList({ limit: 10, offset: 0, ...searchParams })
      .then((res) => {
        // const content: CSVItem[] = get(res, 'data.content', []);
        // const tmp: CSVItemProcessed[] = [];
        // for (let item of content) {
        //   // 处理合并行，新增env字段显示环境，新增dbConfig字段渲染连接信息
        //   if (item.envList.length === 2) {
        //     tmp.concat({ ...item, env: 'stag / prod' });
        //   } else {
        //     tmp.concat({ ...item, env: item.envList[0] });
        //   }
        // }
        setListCSV(get(res, 'data.content', []));
        setTotalCSV(get(res, 'data.total', 0));
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onSearch = () => {
    activeKey === 'db' ? getList() : getCSVList();
  };

  const onEdit = (record: DataSourceItem) => {
    setCurrent(record);
    setVisible(true);
  };

  const onDelete = (record: DataSourceItem) => {
    Modal.confirm({
      title: '确认操作',
      content: '确定要删除此数据源吗？',
      onOk: () =>
        deleteDataSource({ id: record.id }).then((res) => {
          if (res.data) {
            message.success('删除成功');
            getList();
          } else {
            message.error(`删除失败：${res.msg}`);
          }
        }),
    });
  };

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormText
          name="name"
          label="数据源名称"
          placeholder="请输入"
          fieldProps={{ style: { width: 300 }, size: 'large' }}
        />
        <ProFormSelect
          name="type"
          label="数据源类型"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={dsTypes.map((_) => ({ label: _, value: _ }))}
        />
        <ProFormSelect
          name="env"
          label="环境"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
          options={envs.map((_) => ({ label: _, value: _ }))}
        />
        <Button
          size="large"
          icon={<ReloadOutlined />}
          style={{ marginLeft: 14 }}
          onClick={() => {
            form.resetFields();
            onSearch();
          }}
        >
          重置
        </Button>
        <Button
          type="primary"
          size="large"
          icon={<SearchOutlined />}
          style={{ marginLeft: 16 }}
          onClick={onSearch}
        >
          查询
        </Button>
      </ProForm>
      <div style={{ marginTop: 8, textAlign: 'right' }}>
        <Button onClick={() => setVisible(true)}>新增数据源</Button>
      </div>
      <Tabs className="reset-tabs" style={{ marginTop: 8 }} onChange={(k) => setActiveKey(k)}>
        <TabPane tab="数据库数据源" key="db">
          <Table<DataSourceItem>
            rowKey="id"
            columns={columns}
            dataSource={list}
            scroll={{ x: 'max-content' }}
            pagination={{ total, showTotal: (t) => `共${t}条` }}
            loading={loading}
          />
        </TabPane>
        <TabPane tab="文件型数据源" key="file">
          <Table<CSVItem>
            rowKey="id"
            columns={columnsCSV}
            dataSource={listCSV}
            scroll={{ x: 'max-content' }}
            pagination={{ total: totalCSV, showTotal: (t) => `共${t}条` }}
            loading={loading}
          />
        </TabPane>
      </Tabs>
      <EditModal
        visible={visible}
        onCancel={() => {
          setVisible(false);
          setCurrent(undefined);
        }}
        initial={current}
        refresh={onSearch}
      />
    </PageContainer>
  );
};

export default DataSource;
