import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { useParams, Prompt } from 'umi';
import { Button, Form, Table, Input, message, Modal, Popconfirm } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';

import type { MonitorRuleItem } from '@/types/quality';
import { getBaseline, updateBaseline, getMonitorRules, addMonitorRule } from '@/services/quality';
import AddMonitorRules from '../../../Monitor/components/AddMonitorRules';
import styles from './index.less';

const format = 'YYYY-MM-DD HH:mm:ss';
const { Item } = Form;
const EditBaseline: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string; tableName: string}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [baseInfo, setBaseInfo] = useState<{id: string; name: string; creator?: string;}>({id: params.id, name: ''})
  const [originBaseInfo, setOriginBaseInfo] = useState<{id: string; name: string; creator?: string;}>({id: params.id, name: ''})
  const [isEdit, setIsEdit] = useState(false);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  useEffect(() => {
    getTasksWrapped(1);
    getBaseInfo();
  }, []);

  const getTasksWrapped = (pageNum: number) => {
    const params = form.getFieldsValue();
    const condition: any = {
      tableName: params.tableName,
      alarmLevel: params.alarmLevel,
      baselineId: -1
    };
    setLoading(true);
    getMonitorRules({ pageSize: 10, curPage: pageNum, ...condition })
      .then((res) => {
        setTotal(res.data.totalElements);
        setData(res.data.data);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const getBaseInfo = () => {
    getBaseline({id: params.id}).then(res => {
      const newBaseInfo = {
        ...baseInfo,
        name: res.data.name,
        creator: res.data.creator
      }
      setBaseInfo(newBaseInfo);
      setOriginBaseInfo(newBaseInfo);
      form.setFieldsValue({
        name: newBaseInfo.name
      });
    })
  }

  const addMonitorRules = (row={}) => {
    showDrawer('编辑监控规则', {
      drawerProps: {
        width: 800
      },
      beforeConfirm: () => {}
    }, AddMonitorRules)
  }

  const updateBaseInfo = () => {
    return form.validateFields().then(res => {
      return updateBaseline({
        id: +params.id,
        name: res.name
      }).then(() => {
        message.success('修改成功！');
        setIsEdit(false);
        const newBaseInfo = {...baseInfo, name: res.name}
        setBaseInfo(newBaseInfo);
        setOriginBaseInfo(newBaseInfo);
      })
    })
  }

  const onCancal = () => {
    setBaseInfo({...originBaseInfo});
    setIsEdit(false);
  }

  const handleDelete = (row: MonitorRuleItem) => {
    console.log(row);
  }

  const columns: ColumnsType<MonitorRuleItem> = [
    { title: '规则名称', key: 'name', dataIndex: 'name' },
    { title: '规则类型', key: 'ruleType', dataIndex: 'ruleType' },
    { title: '监控对象', key: 'monitorObj', dataIndex: 'monitorObj' },
    {
      title: '告警等级',
      key: 'alarmLevel',
      dataIndex: 'alarmLevel',
      render: (_) => moment(_).format(format)
    },
    {
      title: '告知人',
      key: 'alarmReceivers',
      dataIndex: 'alarmReceivers'
    },
    {
      title: '操作',
      key: 'amContainerLogsUrl',
      dataIndex: 'amContainerLogsUrl',
      fixed: 'right',
      render: (_, row) => (
        <>
         <Button type="link" onClick={() => history.push(`/quality/baseline/edit/${row.id}`)}>
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
    <PageContainer
      header={{
        breadcrumb: {
          routes: [
            {
              path: '/quality/baseline/list',
              breadcrumbName: '基线管理',
            },
            {
              path: '',
              breadcrumbName: '基线编辑',
            },
          ],
        },
      }}
    >
      <Prompt
        when={baseInfo.name !== originBaseInfo.name}
        message={(location) => {
          Modal.confirm({
            title: '暂未保存您所做的更改，是否保存？',
            content: '点击“保存”将保存信息并返回，点击“取消”停留在该页面',
            okText: '保存',
            cancelText: '取消',
            onOk() {
              updateBaseInfo().then(() => {
                history.push(location.pathname)
              })
            },
            onCancel() {
              // history.push(location.pathname);
            }
          });
          return false;
        }}
      />

      <div className={styles.container}>
        <div>基本信息
          <div className={styles.operations}>
            {isEdit ? <>
              <Button onClick={onCancal} style={{marginRight: 8}}>取消</Button>
              <Button type="primary" onClick={updateBaseInfo}>保存</Button>
            </> :
              <Button onClick={() => setIsEdit(true)} type="primary">编辑</Button>
            }
          </div>
        </div>
        <Form form={form} className={styles.form} layout="inline">
          <Item label="基线名称" name={isEdit ? 'name' : undefined} rules={[{ required: isEdit, message: '请输入基线名称' }]}>
            {isEdit ? <Input placeholder='请输入' onChange={e => setBaseInfo({...baseInfo, name: e.target.value})} /> : baseInfo.name }
          </Item>
          <Item label="基线创建人">
            {baseInfo.creator}
          </Item>
        </Form>
      </div>

      <div className={styles.container} style={{marginTop: 16}}>
        <p>表信息</p>
        <Table<MonitorRuleItem>
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
      </div>
    
      <div className={styles.container} style={{marginTop: 16}}>
        <p>监控信息</p>
        <Button onClick={() => addMonitorRules()}>新增监控规则</Button>
        <Table<MonitorRuleItem>
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
      </div>
    </PageContainer>
  );
};

export default EditBaseline;
