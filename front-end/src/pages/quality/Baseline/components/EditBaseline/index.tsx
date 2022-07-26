import React, { useEffect, useState } from 'react';
import { useParams, Prompt } from 'umi';
import { EditableProTable } from '@ant-design/pro-table';
import { Button, Form, Table, Input, message, Modal, Popconfirm } from 'antd';
import type { FC } from 'react';
import type { ProColumns } from '@ant-design/pro-table';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';
import showDialog from '@/utils/showDialog';

import type { MonitorRuleItem, TableItem } from '@/types/quality';
import { getBaseline, updateBaseline, getMonitorRules, addMonitorRule, updateMonitorRule, removeMonitorRule, getBaselineTables } from '@/services/quality';
import { alarmLevelList, ruleTypeList, monitorObjList } from '@/constants/quality';
import AddMonitorRules from '../../../Monitor/components/AddMonitorRules';
import styles from './index.less';
import LogsContent from '../../../Monitor/components/LogsContent'
import TableSelect from '@/pages/quality/Monitor/components/TableSelect';

const { Item } = Form;
const EditBaseline: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string;}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [tableData, setTableData] = useState<TableItem[]>([])
  const [baseInfo, setBaseInfo] = useState<{id: string; name: string; creator?: string;tableName: string;}>({id: params.id, name: '', tableName: ''})
  const [originBaseInfo, setOriginBaseInfo] = useState<{id: string; name: string; creator?: string;tableName: string;}>({id: params.id, name: '', tableName: ''})
  const [isEdit, setIsEdit] = useState(false);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [form] = Form.useForm();
  useEffect(() => {
    getBaseInfo();
  }, []);

  useEffect(() => {
    getTasksWrapped();
  }, [currentPage])

  const getTasksWrapped = (pageNum: number = currentPage) => {
    const condition: any = {
      tableName: baseInfo.tableName,
      baselineId: params.id
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
    removeMonitorRule({id: row.id}).then(_ => {
      message.success('删除成功');
      getTasksWrapped();
    })
  }

 const handleDeleteTables = (row: TableItem) => {}

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

  const handleTableSelectChange = (val, row) => {
    console.log(val ,row);
    tableData.find(item => item.id === row.rowKey);
  }

  const columns:ColumnsType<MonitorRuleItem>  = [
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
         <Button type="link" onClick={() => handleAddMonitorRule(row)}>
            编辑
          </Button>
          <Button type="link" onClick={() => viewLogs(row, 2)}>
            试跑
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
  

  const tableColumns: ProColumns<TableItem> [] = [
    {
      title: '表英文名',
      key: 'tableName',
      dataIndex: 'tableName',
      formItemProps: () => ({
        rules: [{ required: true, message: '必填' }]
      }),
      renderFormItem: (_, row) => <TableSelect labelInValue onChange={(val: string) => handleTableSelectChange(val, row)} />
    },
    {
      title: '表中文名',
      key: 'comment',
      dataIndex: 'comment',
      editable: false,
      render: _ => _ || '-'
    },
    {
      title: '时间分区表达式',
      key: 'partitioned',
      dataIndex: 'partitioned',
      formItemProps: () => ({
        rules: [{ required: true, message: '必填' }]
      }),
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, row, _, action) => (
        <>
         <Button type="link" onClick={() => action?.startEditable?.(row.id)}>
            编辑
          </Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDeleteTables(row)}>
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
        <EditableProTable<TableItem>
          rowKey="id"
          maxLength={5}
          scroll={{
            x: 960,
          }}
          recordCreatorProps={{
            position: 'bottom',
            record: () => ({
              id: +(Math.random() * 1000000).toFixed(0),
              tableName: '',
              comment: '-',
              partitioned: ''
            })
          }}
          loading={false}
          columns={tableColumns}
          request={async () => {
            const res = await getBaselineTables({id: params.id});
            return {
              data: res.data,
              success: res.success
            }
          }}
          value={tableData}
          onChange={setTableData}
          editable={{
            type: 'multiple',
            onSave: async (rowKey, data, row) => {
              // console.log(rowKey, data, row);
              // await waitTime(2000);
            },
            // onChange: setEditableRowKeys,
          }}
        />
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

export default EditBaseline;
