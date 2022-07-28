import React, { useEffect, useState } from 'react';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import { Button, Form, Table, Popconfirm, message, Modal } from 'antd';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import { PageContainer } from '@/components';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDialog from '@/utils/showDialog';
import type { BaselineItem } from '@/types/quality';
import { getBaselineList, addBaseline, deleteBaseline, toggleBaseline } from '@/services/quality';
import { statusList } from '@/constants/quality'
import AddBaseline from './components/AddBaseline';
import styles from './index.less';
import LogsContent from '../Monitor/components/LogsContent'

const Baseline: FC<{history: any}> = ({ history }) => {
  const [data, setData] = useState<BaselineItem[]>([]);
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
      name: params.name
    };
    setLoading(true);
    getBaselineList({ pageSize: 10, curPage: pageNum, ...condition })
      .then((res) => {
        setTotal(res.data.totalElements);
        setData(res.data.data);
      })
      .finally(() => setLoading(false));
  };

  const handleAddBaseline = (row: any = {}) => {
    showDialog('新建基线', {
      modalProps: {
        width: 500
      },
      formProps: {
        id: row.id
      },
      beforeConfirm: (dialog, form, done) => {
        form.handleSubmit().then((values: any) => {
          dialog.showLoading();
          addBaseline(values).then(() => {
            message.success('新增成功！');
            done();
            getTasksWrapped();
          }).finally(() => {
            dialog.hideLoading();
          })
        })
      }
    }, AddBaseline);
  }

  const handleDelete = (row: BaselineItem) => {
    deleteBaseline({id: row.id}).then(() => {
      message.success('删除成功');
      getTasksWrapped();
    })
  }

  const handleToggle = (row: BaselineItem) => {
    const isStop = row.status === 1;
    Modal.confirm({
      title: `确认要${isStop ? '停用' : '启用'}基线【${row.name}】吗？`,
      onOk() {
        toggleBaseline({id: row.id, status: isStop ? 0 : 1}).then(() => {
          message.success(`${isStop ? '停用' : '启用'}成功`);
          getTasksWrapped();
        })
      }
    })
  }

  const viewLogs = (row: BaselineItem) => {
    showDialog('监控日志' , {
      formProps: {
        params: {
          baselineId: row.id
        },
      }
    }, LogsContent)
  }

  const columns: ColumnsType<BaselineItem> = [
    { title: '规则名称', key: 'name', dataIndex: 'name' },
    { 
      title: '包含规则数量',
      key: 'ruleCount',
      dataIndex: 'ruleCount'
    },
    {
      title: '包含表数',
      key: 'tableCount',
      dataIndex: 'tableCount'
    },
    {
      title: '状态',
      key: 'status',
      dataIndex: 'status',
      render: (_) => statusList.find(item => item.value === _)?.label || '-'
    },
    {
      title: '操作',
      key: 'amContainerLogsUrl',
      dataIndex: 'amContainerLogsUrl',
      width: 180,
      fixed: 'right',
      render: (_, row) => {
        return (
          <>
          <Button type="link" onClick={() => handleToggle(row)}>
            {row.status === 0 ? '启用' : '停用'}
          </Button>
          <Button type="link" onClick={() => viewLogs(row)}>
            日志
          </Button>
          <Button type="link" onClick={() => history.push(`/quality/baseline/edit/${row.id}`)} disabled={row.status === 1}>
            编辑
          </Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(row)} disabled={row.status === 1}>
            <Button type="link" disabled={row.status === 1}>
              删除
            </Button>
          </Popconfirm>
        </>
        )
      },
    },
  ];

  return (
    <PageContainer>
      <ProForm form={form} className={styles.form} layout="inline" colon={false} submitter={false}>
        <ProFormText
          name="name"
          label="基线名称"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
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
        <Button onClick={handleAddBaseline}>新建基线</Button>
      </div>
      <Table<BaselineItem>
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

export default Baseline;
