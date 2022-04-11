import React, { useEffect, useState } from 'react';
import { ProFormSelect, ProFormText, QueryFilter } from '@ant-design/pro-form';
import { Form, Space, Table, Button, message, Modal } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@/components';
import type { ColumnsType } from 'antd/lib/table/Table';
import type  { MetricFloderItem } from '@/types/measure';
import showDialog from '@/utils/showDialog';
import styles from './index.less';
import { MetricListItem } from '@/types/measure';
import { getMeasures, deleteMetric, getTables } from '@/services/measure';
import CreateModifier from './components/CreateModifier';


const { confirm } = Modal;
const DataSource: FC<{currentNode: MetricFloderItem}> = ({currentNode}) => {
  const [data, setData] = useState<MetricListItem[]>([]);
  const [tables, setTables] = useState([]);
  const [total, setTotal] = useState(0);
  const [current, setCurrent] = useState(1);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
      getTables()
      .then((res) => {
        const list = res.data?.map((_: any) => ({
          label: _.tableName,
          value: `${_.id}`,
        }));
        setTables(list);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    getTableData(10 * (current - 1));
  }, [currentNode, current])

  const getTableData = (offset: number) => {
    const params = form.getFieldsValue();
    setLoading(true);
    getMeasures({
      ...params,
      limit: 10,
      offset,
      measureType: 'MODIFIER_LABEL'
    })
      .then((res) => {
        setTotal(res.data.total);
        setData(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const columns: ColumnsType<MetricListItem> = [
    { title: '修饰词名称', key: 'labelName', dataIndex: 'labelName' },
    { title: '字段名', key: 'columnName', dataIndex: 'columnName' },
    { title: '所属表', key: 'belongTblName', dataIndex: 'belongTblName' },
    { title: '业务口径', key: 'measureDefine', dataIndex: 'measureDefine' },
    { title: '创建人', key: 'creator', dataIndex: 'creator' },
    { title: '更新人', key: 'editor', dataIndex: 'editor' },
    { title: '最近更新时间', key: 'editTime', dataIndex: 'editTime' },
    {
      title: '操作',
      key: 'ops',
      dataIndex: 'ops',
      fixed: 'right',
      render: (value, record) => (
        <Space size={16}>
          <a onClick={() => addTreeItem(record)}>编辑</a>
          <a onClick={() => onDelete(record)}>删除</a>
        </Space>
      ),
    },
  ];

  const onFinish = async (values: any) => {
    setCurrent(1)
    getTableData(0);
  }

  const onReset = () => {
    onFinish({});
  }

  const onDelete = (record: MetricListItem) =>
    confirm({
      title: '删除指标',
      content: '您确认要删除该修饰词吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteMetric({ modifierCode: record.labelCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
            }
          })
          .catch((err) => {}),
  });

  const addTreeItem = (node: any = {}) => {
    const isEdit = !!node.id;
    showDialog(`${isEdit ? '编辑' : '新建'}修饰词`, {
      modalProps: {
        width: 540
      },
      formProps: {
        node
      },
      beforeConfirm: (dialog, form, done) => {
        dialog.showLoading();
        form.handleSubmit().then(() => {
          done();
          message.success(`${isEdit ? '编辑' : '新建'}修饰词成功！`);
          getTableData(10 * (current - 1));
        }).finally(() => {
          dialog.hideLoading();
        })
      },
    }, CreateModifier)
  }

  return (
    <PageContainer contentClassName={styles.container}>
      <QueryFilter
        form={form}
        className='meaure-list-wrap'
        layout="horizontal"
        onFinish={onFinish}
        onReset={onReset}
        labelCol={{span: 6}}
        labelWidth={96}
        submitter={{
          render: (props) => {
            return [
              <Button key="search" type="primary" onClick={props?.form?.submit}>
                查询
              </Button>,
              <Button key="edit" onClick={() => addTreeItem()}>
                新增
              </Button>,
            ];
          },
        }}
      >
        <ProFormText
          name="measureName"
          label="修饰词名称"
          placeholder="请输入"
        />
        <ProFormSelect
          name="belongTblName"
          label="所属表"
          placeholder="请选择"
          options={tables}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormText
          name="creator"
          label="创建人"
          placeholder="请输入"
        />
      </QueryFilter>
      <Table<MetricListItem>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={{
          total,
          current,
          showTotal: (t) => `共${t}条`,
          onChange: (page) =>setCurrent(page),
        }}
      />
    </PageContainer>
  );
};

export default DataSource;
