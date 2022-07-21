import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { useParams, Prompt } from 'umi';
import { Button, Form, Table, Input, message, Modal, Popconfirm } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';

import type { MonitorItem,MonitorRuleItem } from '@/types/quality';
import {
  getMonitorInfo,
  editMonitorInfo,
  getMonitorRules,
  addMonitorRule,
  updateMonitorRule,
  removeMonitorRule,
  toggleMonitorRule
} from '@/services/quality';
import AddMonitorRules from '../AddMonitorRules';
import LogsContent from '../LogsContent';
import styles from './index.less';
import showDialog from '@/utils/showDialog';
import { alarmLevelList, ruleTypeList, monitorObjList } from '@/constants/quality';

const { Item } = Form;
const EditMonitor: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string; tableName: string}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [baseInfo, setBaseInfo] = useState<MonitorItem>({id: +params.id, tableName: params.tableName})
  const [originBaseInfo, setOriginBaseInfo] = useState<MonitorItem>({id: +params.id, tableName: params.tableName})
  const [isEdit, setIsEdit] = useState(false);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [form] = Form.useForm();
  useEffect(() => {
    getTasksWrapped(1);
    getBaseInfo();
  }, []);

  useEffect(() => {
    getTasksWrapped();
  }, [currentPage])

  const getTasksWrapped = (pageNum: number = currentPage) => {
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
    getMonitorInfo({id: params.id}).then(res => {
      setBaseInfo(res.data);
      setOriginBaseInfo(res.data);
      form.setFieldsValue({
        partitionExpr: res.data.partitionExpr
      });
    })
  }

  const handleAddMonitorRule = (row?: MonitorRuleItem) => {
    const isEdit = !!row?.id;
    showDrawer(`${isEdit ? '编辑' : '新增'}监控规则`, {
      drawerProps: {
        width: 800
      },
      formProps: {
        id: row?.id,
        tableName: baseInfo.tableName
      },
      beforeConfirm: (dialog, form, done) => {
        form.handleSubmit().then((res: any) => {
          const handler = isEdit ? updateMonitorRule : addMonitorRule;
          const params = isEdit ? {...res, id: row.id} : res;
          dialog.showLoading();
          handler(params).then(() => {
            message.success(`${isEdit ? '编辑' : '新增'}成功`);
            done();
            getTasksWrapped();
          })
        }).finally(() => {
          dialog.hideLoading();
        })
      }
    }, AddMonitorRules)
  }

  const updateMonitorInfo = () => {
    return form.validateFields().then(res => {
      return editMonitorInfo({
        id: params.id,
        baselineId: -1,
        partitionExpr: res.partitionExpr
      }).then(() => {
        message.success('修改成功！');
        setIsEdit(false);
        const newBaseInfo = {...baseInfo, partitionExpr: res.partitionExpr}
        setBaseInfo(newBaseInfo);
        setOriginBaseInfo(newBaseInfo);
      })
    })
  }

  const onCancal = () => {
    setBaseInfo({...originBaseInfo});
    setIsEdit(false);
  }

  const viewLogs = (row: MonitorRuleItem, type: 1 | 2) => {
    showDialog(type === 1 ? '监控日志' : '试跑结果', {
      formProps: {
        params: {
          ruleId: row.id
        },
        type
      }
    }, LogsContent)
  }

  const handleDelete = (row: MonitorRuleItem) => {
    removeMonitorRule({id: row.id}).then(res => {
      message.success('删除成功');
      getTasksWrapped();
    })
  }

  const handleToggle = (row: MonitorRuleItem) => {
    const isStop = row.status === 1;
    Modal.confirm({
      title: `确认要${isStop ? '停用' : '启用'}规则【${row.name}】吗？`,
      onOk() {
        toggleMonitorRule({id: row.id, status: isStop ? 0 : 1}).then(() => {
          message.success(`${isStop ? '停用' : '启用'}成功`);
          getTasksWrapped();
        })
      }
    })
  }

  const columns: ColumnsType<MonitorRuleItem> = [
    { title: '规则名称', key: 'name', dataIndex: 'name' },
    {
      title: '规则类型',
      key: 'ruleType',
      dataIndex: 'ruleType',
      render: (text) => ruleTypeList.find(item => item.value === text)?.label || '-'
    },
    {
      title: '监控对象',
      key: 'monitorObj',
      dataIndex: 'monitorObj',
      render: (text) => monitorObjList.find(item => item.value === text)?.label || '-'
    },
    {
      title: '告警等级',
      key: 'alarmLevel',
      dataIndex: 'alarmLevel',
      render: (text) => alarmLevelList.find(item => item.value === text)?.label || '-'
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
          <Button type="link" onClick={() => handleToggle(row)}>
            {row.status === 0 ? '启用' : '禁用'}
          </Button>
          <Button type="link" onClick={() => handleAddMonitorRule(row)}>
            编辑
          </Button>
          <Button type="link" onClick={() => viewLogs(row, 1)}>
            日志
          </Button>
          <Button type="link" onClick={() => viewLogs(row, 2)}>
            试跑
          </Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(row)}  disabled={row.status === 0}>
            <Button type="link" disabled={row.status === 0}>
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
              path: '/quality/monitor/list',
              breadcrumbName: '监控管理',
            },
            {
              path: '',
              breadcrumbName: '监控规则',
            },
          ],
        },
      }}
      extra={[
        <Button onClick={() => history.go(-1)} key="goBack">
          返回
        </Button>,
      ]}
    >
      <Prompt
        when={baseInfo.partitionExpr !== originBaseInfo.partitionExpr}
        message={(location) => {
          Modal.confirm({
            title: '暂未保存您所做的更改，是否保存？',
            content: '点击“保存”将保存信息并返回，点击“取消”停留在该页面',
            okText: '保存',
            cancelText: '取消',
            onOk() {
              updateMonitorInfo().then(() => {
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
              <Button type="primary" onClick={updateMonitorInfo}>保存</Button>
            </> :
              <Button onClick={() => setIsEdit(true)} type="primary">编辑</Button>
            }
          </div>
        </div>
        <Form form={form} className={styles.form} layout="inline">
          <Item label="表名称">
            {baseInfo.tableName}
          </Item>
          <Item label="时间分区表达式" name={isEdit ? 'partitionExpr' : undefined} rules={[{ required: isEdit, message: '请输入时间分区表达式' }]}>
            {isEdit ? <Input placeholder='请输入' onChange={e => setBaseInfo({...baseInfo, partitionExpr: e.target.value})} /> : baseInfo.partitionExpr }
          </Item>
        </Form>
      </div>
    
      <div className={styles.container} style={{marginTop: 16}}>
        <p>监控信息</p>
        <Button onClick={() => handleAddMonitorRule()}>新增监控规则</Button>
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
            onChange: (page) => setCurrentPage(page),
          }}
        />
      </div>
    </PageContainer>
  );
};

export default EditMonitor;
