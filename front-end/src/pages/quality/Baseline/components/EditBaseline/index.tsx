import React, { useEffect, useState } from 'react';
import { useParams, Prompt } from 'umi';
import { Button, Form, Table, Input, message, Modal, Popconfirm } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';
import showDialog from '@/utils/showDialog';

import type { MonitorRuleItem, TableItem } from '@/types/quality';
import {
  getBaseline,
  updateBaseline,
  getMonitorRules,
  addMonitorRule,
  updateMonitorRule,
  removeMonitorRule,
  getBaselineTables,
  addMonitor,
  editMonitorInfo,
  deleteMonitor
} from '@/services/quality';
import { alarmLevelList, ruleTypeList, monitorObjList } from '@/constants/quality';
import AddMonitorRules from '../../../Monitor/components/AddMonitorRules';
import styles from './index.less';
import LogsContent from '../../../Monitor/components/LogsContent'
import TableSelect from '@/pages/quality/Monitor/components/TableSelect';

const { Item } = Form;
const EditBaseline: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string;}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [tableData, setTableData] = useState<TableItem[]>([]);
  const [tableModified, setTableModified] = useState(false);
  const [originTableData, setOriginTableData] = useState<TableItem[]>([]);
  const [baseInfo, setBaseInfo] = useState<{id: string; name: string; creator?: string;tableName: string;}>({id: params.id, name: '', tableName: ''})
  const [originBaseInfo, setOriginBaseInfo] = useState<{id: string; name: string; creator?: string;tableName: string;}>({id: params.id, name: '', tableName: ''})
  const [isEdit, setIsEdit] = useState(false);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [form] = Form.useForm();
  useEffect(() => {
    getBaseInfo();
    getTablesData();
  }, []);

  useEffect(() => {
    getRulesWrapped();
  }, [currentPage])

  const getRulesWrapped = (pageNum: number = currentPage) => {
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

  const getTablesData = () => {
    getBaselineTables({id: params.id}).then(res => {
      const tableData = res.data.map(item => ({...item, showEdit: false}));
      setTableData(tableData);
      setOriginTableData(tableData);
      setTableModified(false);
    });
  }

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
            getRulesWrapped();
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
      getRulesWrapped();
    })
  }

  const handleDeleteTables = (row: TableItem) => {
    deleteMonitor({id: row.id, isBaseline: true}).then(res => {
      message.success('删除成功！');
      getTablesData();
    })
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

  const handleTableSelectChange = (tableName: string, partitioned: boolean, index: any) => {
    setTableModified(true);
    setTableData([...tableData.slice(0, index), {...tableData[index], partitioned, tableName}, ...tableData.slice(index + 1)]);
  }

  const handlePartitionChange = (val: string, index: number) => {
    setTableModified(true);
    setTableData([...tableData.slice(0, index), {...tableData[index], partitionExpr: val}, ...tableData.slice(index + 1)]);
  }

  const showEdit = (rowIndex: number, edit: boolean) => {
    const exist = tableData.some(item => item.showEdit);
    if(exist && edit) {
      message.warning('请先保存正在编辑的行');
      return;
    }
    setTableData([...tableData.slice(0, rowIndex), {...tableData[rowIndex], showEdit: edit}, ...tableData.slice(rowIndex + 1)])
  }

  const onSave = async (row: TableItem) => {
    if(row.id === -9999) {
      await addMonitor({tableName: row.tableName, partitionExpr:row.partitionExpr, baselineId: +params.id})
    } else {
      await editMonitorInfo({ partitionExpr:row.partitionExpr, baselineId: +params.id, id: row.id })
    }
    message.success('操作成功');
    getTablesData();
  }

  const onCancel = (row: TableItem, rowIndex: number) => {
    if(row.id === -9999) {
      setTableData([...tableData.slice(0, rowIndex), ...tableData.slice(rowIndex + 1)])
    } else {
      const originRow = originTableData[rowIndex];
      setTableData([...tableData.slice(0, rowIndex), {...originRow}, ...tableData.slice(rowIndex + 1)])
    }
  } 

  const addNewLine = () => {
    const exist = tableData.some(item => item.showEdit);
    if(exist) {
      message.warning('请先保存正在编辑的行');
      return;
    }
    const newRow = {
      id: -9999,
      showEdit: true,
      partitioned: false,
      partitionExpr: '',
      tableName: undefined
    }
    setTableData([...tableData, newRow])
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

  const tableColumns: ColumnsType<TableItem> = [
    {
      title: '表英文名',
      key: 'tableName',
      dataIndex: 'tableName',
      render: (_, row, index) =>
        row.showEdit ? 
        <TableSelect
          value={_}
          onChange={(val: string, partitioned: boolean) => handleTableSelectChange(val, partitioned, index)}
        /> : _
    },
    {
      title: '时间分区表达式',
      key: 'partitionExpr',
      dataIndex: 'partitionExpr',
      render: (_, row, index) => row.showEdit && row.partitioned ?
        <Input
          value={_}
          placeholder="请输入"
          onChange={e => handlePartitionChange(e.target.value, index)}
        /> : _
    },
    {
      title: '操作',
      render: (text, row, index) => (
        <>
          {row.showEdit ? <>
            <Button type="link" onClick={() => onSave(row)}>
              保存
            </Button>
            <Button type="link" onClick={() => onCancel(row, index)}>
              取消
            </Button>
          </> :  <Button type="link" onClick={() => showEdit(index, true)}>
            编辑
          </Button>}
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
        when={baseInfo.name !== originBaseInfo.name || tableModified}
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
        <Table<TableItem>
          rowKey="id"
          columns={tableColumns}
          dataSource={tableData}
          scroll={{ x: 'max-content' }}
          style={{ marginTop: 16 }}
          loading={loading}
          pagination={false}
        />
        <Button
          type="dashed"
          style={{display: 'block', margin: '10px 0', width: '100%'}}
          onClick={addNewLine}
        >
          添加一行数据
        </Button>
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
