import React, { useEffect, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Button, Form, Table, Popconfirm, message, Modal } from 'antd';
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import { KeepAlive } from 'umi';
import { PageContainer } from '@/components';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';
import AddTemplate from './components/AddTemplate';
import type { TemplateItem } from '@/types/quality';
import { getTemplateList, addTemplate, updateTemplate, removeTemplate, toggleTemplate } from '@/services/quality';
import { ruleTypeList, categoryList, monitorObjList, statusList } from '@/constants/quality'

import styles from './index.less';
import moment from 'moment';


const Template: FC<{history: any}> = ({ history }) => {
  const [data, setData] = useState<TemplateItem[]>([]);
  const [curPage, setCurPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  // const [useInfo, setUserInfo] = useState({nickname: ''})

  useEffect(() => {
    // getUseInfo().then(res => {
    //   setUserInfo(res.data);
    // });
  }, []);

  useEffect(() => {
    getTasksWrapped();
  }, [curPage])

  const getTasksWrapped = (pageNum: number = curPage) => {
    const params = form.getFieldsValue();
    setLoading(true);
    getTemplateList({ pageSize: 10, curPage: pageNum, ...params })
      .then((res) => {
        setTotal(res.data.totalElements);
        setData(res.data.data);
      })
      .finally(() => setLoading(false));
  };

  const onSearch = () => {
    if(curPage !== 1) {
      setCurPage(1);
    } else {
      getTasksWrapped();
    }
  }

  const handleAddTemplate = (row: any = {}) => {
    const isEdit = !!row.id;
    let operator = '';
    if(row?.status === 1) {
      operator = '查看'
    } else if(isEdit) {
      operator = '编辑'
    } else {
      operator = '新增';
    }
    showDrawer(`${operator}规则`, {
      formProps: {
        id: row.id,
        disabled: row.status ===1
      },
      beforeConfirm: (dialog, form, done) => {
        form.handleSubmit().then((values: any) => {
          const handler = isEdit ? updateTemplate : addTemplate;
          const params = isEdit ? {...values, id: row.id} : values;
          dialog.showLoading();
          handler({...params, type: 'template'}).then(() => {
            message.success(`${isEdit ? '修改': '新增'}成功！`);
            done();
            getTasksWrapped();
          }).finally(() => {
            dialog.hideLoading();
          })
        })
      }
    }, AddTemplate);
  }

  const handleDelete = (row: TemplateItem) => {
    removeTemplate({id: row.id}).then(() => {
      message.success('删除成功');
      getTasksWrapped();
    })
  }

  const toggleTemplte = (row: TemplateItem) => {
    const isStop = row.status === 1;
    Modal.confirm({
      title: `确认要${isStop ? '停用' : '启用'}规则【${row.name}】吗？`,
      onOk() {
        toggleTemplate({id: row.id, status: isStop ? 0 : 1}).then(() => {
          message.success(`${isStop ? '停用' : '启用'}成功`);
          getTasksWrapped();
        })
      }
    })
   
  }

  const columns: ColumnsType<TemplateItem> = [
    { title: '规则名称', key: 'name', dataIndex: 'name', width: 400 },
    { 
      title: '规则类型',
      key: 'type',
      dataIndex: 'type',
      render: (_) => ruleTypeList.find(item => item.value === _)?.label || '-'
    },
    {
      title: '维度',
      key: 'category',
      dataIndex: 'category',
      render: (_) => categoryList.find(item => item.value === _)?.label || '-'
    },
    {
      title: '监控对象',
      key: 'monitorObj',
      dataIndex: 'monitorObj',
      render: (_) => monitorObjList.find(item => item.value === _)?.label || '-'
    },
    { title: '创建人', key: 'creator', dataIndex: 'creator' },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss')
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
      width: 160,
      fixed: 'right',
      render: (_, row) => {
        return (
          <>
          <Button type="link" onClick={() => toggleTemplte(row)}>
            {row.status === 0 ? '启用' : '停用'}
          </Button>
          <Button type="link" onClick={() => handleAddTemplate(row)}>
            {row.status === 1 ? '查看' : '编辑'}
          </Button>
          {row.type !== 'system' &&  <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(row)} disabled={row.status === 1}>
            <Button type="link" disabled={row.status === 1}>
              删除
            </Button>
          </Popconfirm>}
         
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
          label="规则名称"
          placeholder="请输入"
          fieldProps={{ style: { width: 200 }, size: 'large' }}
        />
        <ProFormSelect
          name="category"
          label="维度"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large', allowClear: true }}
          options={categoryList}
          
        />
        <ProFormSelect
          name="type"
          label="规则类型"
          placeholder="请选择"
          fieldProps={{ style: { width: 200 }, size: 'large', allowClear: true }}
          options={ruleTypeList}
        />
        <Button
          size="large"
          icon={<ReloadOutlined />}
          style={{ margin: '0 0 24px 14px' }}
          onClick={() => {
            form.resetFields();
            getTasksWrapped();
          }}
        >
          重置
        </Button>
        <Button
          type="primary"
          size="large"
          icon={<SearchOutlined />}
          style={{ margin: '0 0 24px 16px' }}
          onClick={onSearch}
        >
          查询
        </Button>
      </ProForm>
      <div>
        <Button onClick={handleAddTemplate}>新建模版规则</Button>
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
          current: curPage,
          showSizeChanger: false,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => setCurPage(page),
        }}
      />
    </PageContainer>
  );
};

export default ({history}: {history: any}) => <KeepAlive>
  <Template history={history} />
</KeepAlive>;
