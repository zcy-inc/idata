import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { useParams, Prompt } from 'umi';
import { Button, Form, Table, Input, message, Modal } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDialog from '@/utils/showDialog';

import type { MonitorItem,MonitorRuleItem } from '@/types/quality';
import { getMonitorInfo, editMonitorInfo, getMonitorRules } from '@/services/quality';
import AddMonitorRules from '../AddMonitorRules';
import styles from './index.less';

const format = 'YYYY-MM-DD HH:mm:ss';
const { Item } = Form;
const TaskHistory: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string; tableName: string}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [baseInfo, setBaseInfo] = useState<MonitorItem>({id: +params.id, tableName: params.tableName})
  const [originBaseInfo, setOriginBaseInfo] = useState<MonitorItem>({id: +params.id, tableName: params.tableName})
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
    getMonitorInfo({id: params.id}).then(res => {
      setBaseInfo(res.data);
      setOriginBaseInfo(res.data);
      form.setFieldsValue({
        partitionExpr: res.data.partitionExpr
      });
    })
  }

  const addMonitorRules = () => {
    showDialog('编辑监控规则', {
      modalProps: {
        width: 800
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
      key: 'alarm_receivers',
      dataIndex: 'alarm_receivers'
    },
    {
      title: '操作',
      key: 'amContainerLogsUrl',
      dataIndex: 'amContainerLogsUrl',
      fixed: 'right',
      render: (_) => (
        <>
          <a href={_} target="_blank">
            编辑
          </a>
          <a href={_} target="_blank">
            删除
          </a>
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
        <Button onClick={addMonitorRules}>新增监控规则</Button>
        <Table<MonitorItem>
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

export default TaskHistory;
