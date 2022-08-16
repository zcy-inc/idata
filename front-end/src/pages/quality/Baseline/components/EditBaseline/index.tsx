import React, { useEffect, useState } from 'react';
import { useParams, Prompt } from 'umi';
import { Button, Form, Table, Input, message, Modal, Popconfirm, Tooltip, Row } from 'antd';
import type { FC } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ColumnsType } from 'antd/lib/table/Table';
import showDrawer from '@/utils/showDrawer';
import { InfoCircleOutlined } from '@ant-design/icons';

import type { MonitorRuleItem, TableItem, Status } from '@/types/quality';
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
  deleteMonitor,
  tryRunMonitorRule,
  toggleMonitorRule
} from '@/services/quality';
import { alarmLevelList, ruleTypeList, monitorObjList } from '@/constants/quality';
import AddMonitorRules from '../../../Monitor/components/AddMonitorRules';
import styles from './index.less';
import TableSelect from '@/pages/quality/Monitor/components/TableSelect';
interface BaseInfoTypes {
  id: string;
  name: string;
  creator?: string;
  tableName: string;
  status: Status
}
const { Item } = Form;
const EditBaseline: FC<{history: any}> = ({history}) => {
  const params = useParams<{id: string;}>();
  const [data, setData] = useState<MonitorRuleItem[]>([]);
  const [tableData, setTableData] = useState<TableItem[]>([]);
  const [tableModified, setTableModified] = useState(false);
  const [originTableData, setOriginTableData] = useState<TableItem[]>([]);
  const [baseInfo, setBaseInfo] = useState<BaseInfoTypes>({id: params.id, name: '', tableName: '', status: 1})
  const [originBaseInfo, setOriginBaseInfo] = useState<BaseInfoTypes>({id: params.id, name: '', tableName: '', status: 1})
  const [isEdit, setIsEdit] = useState(false);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [form] = Form.useForm();
  const [tableForm] = Form.useForm();

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
      const tableData = res.data.map(item => ({...item, showEdit: false, partitioned: item.partitionExpr ? true : false}));
      setTableData(tableData);
      setOriginTableData(tableData);
      tableForm.setFieldsValue(tableData)
      setTableModified(false);
    });
  }

  const getBaseInfo = () => {
    getBaseline({id: params.id}).then(res => {
      const newBaseInfo = {
        ...baseInfo,
        name: res.data.name,
        creator: res.data.creator,
        status: res.data.status
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
    let operator = "";
    if(baseInfo.status === 1 || row?.status === 1) {
      operator = '查看'
    } else if(!isEdit) {
      operator = '新增'
    } else {
      operator = '编辑';
    }
    showDrawer(`${operator}基线规则`, {
      drawerProps: {
        width: 800
      },
      formProps: {
        id: row?.id,
        baselineId: +params.id,
        disabled: baseInfo.status === 1 || row?.status === 1
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
        return;
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

  const handleDeleteTables = (row: TableItem, index: number) => {
    if(row.id === -9999) {
      setTableData([...tableData.slice(0,index), ...tableData.slice(index +1)])
      return;
    }
    deleteMonitor({id: row.id, isBaseline: true}).then(res => {
      message.success('删除成功！');
      getTablesData();
    })
  }

  const handleTableSelectChange = (tableName: string, partitioned: boolean, index: any) => {
    setTableModified(true);
    setTableData([
      ...tableData.slice(0, index),
      {
        ...tableData[index],
        tableName,
        partitioned,
        partitionExpr:partitioned ? tableData[index].partitionExpr: ''
      },
      ...tableData.slice(index + 1)]);
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

  const onSave = (row: TableItem) => {
    tableForm.validateFields().then(async res => {
      console.log(res);
      if(row.id === -9999) {
        await addMonitor({tableName: row.tableName, partitionExpr:row.partitionExpr, baselineId: +params.id})
      } else {
        await editMonitorInfo({ tableName: row.tableName,partitionExpr:row.partitionExpr, baselineId: +params.id, id: row.id })
      }
      message.success('操作成功');
      getTablesData();
    })
  }

  const onCancel = (row: TableItem, rowIndex: number) => {
    if(row.id === -9999) {
      setTableData([...tableData.slice(0, rowIndex), ...tableData.slice(rowIndex + 1)])
    } else {
      const originRow = originTableData[rowIndex];
      setTableData([...tableData.slice(0, rowIndex), {...originRow}, ...tableData.slice(rowIndex + 1)])
    }
  }

  const tryRun = (row: MonitorRuleItem) => {
    tryRunMonitorRule({
      ruleId: row.id,
      baselineId: params.id
    }).then(() => {
      message.success('提交成功，试跑结束后结果将以钉钉形式发送');
    })
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

  const handleToggle = (row: MonitorRuleItem) => {
    const isStop = row.status === 1;
    Modal.confirm({
      title: `确认要${isStop ? '停用' : '启用'}规则【${row.name}】吗？`,
      onOk() {
        toggleMonitorRule({id: row.id, status: isStop ? 0 : 1}).then(() => {
          message.success(`${isStop ? '停用' : '启用'}成功`);
          getRulesWrapped();
        })
      }
    })
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
         <Button type="link" onClick={() => handleToggle(row)} disabled={baseInfo.status === 1}>
            {row.status === 0 ? '启用' : '停用'}
          </Button>
         <Button type="link" onClick={() => handleAddMonitorRule(row)}>
         {(row.status === 1 || baseInfo.status === 1) ? '查看' : '编辑'}
          </Button>
          <Button type="link" onClick={() => tryRun(row)}>
            试跑
          </Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(row)} disabled={row.status === 1 || baseInfo.status === 1}>
            <Button danger type="text" disabled={row.status === 1 || baseInfo.status === 1}>
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
      width:240,
      render: (_, row, index) =>
        row.showEdit ?
        <Item rules={[{ required: true, message: '' }]} name={[index, 'tableName']}>
          <TableSelect
            onChange={(val: string, partitioned: boolean) => handleTableSelectChange(val, partitioned, index)}
          />
        </Item>
        : _
    },
    {
      title: <span>时间分区表达式 <Tooltip title="当前只支持日期形式的分区，示例：若分区格式为yyyyMMdd，表为t+1的数据，则分区表达式填写为dt=${yyyyMMdd-1}">
       <InfoCircleOutlined />
      </Tooltip></span>,
      key: 'partitionExpr',
      width:240,
      dataIndex: 'partitionExpr',
      render: (_, row, index) => row.partitioned ? (row.showEdit ?
        <Item rules={[{ required: true, message: '' }]} name={[index, 'partitionExpr']}>
          <Input
            placeholder="请输入"
            onChange={e => handlePartitionChange(e.target.value, index)}
          />
        </Item>
         : _ ) : '-'
    },
    {
      title: '操作',
      width:200,
      render: (text, row, index) => (
        <>
          {row.showEdit ? <>
            <Button type="link" onClick={() => onSave(row)}>
              保存
            </Button>
            <Button type="link" onClick={() => onCancel(row, index)}>
              取消
            </Button>
          </> :  <Button type="link" onClick={() => showEdit(index, true)} disabled={baseInfo.status === 1}>
            编辑
          </Button>}
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDeleteTables(row, index)} disabled={baseInfo.status === 1}>
            <Button danger type="text" disabled={baseInfo.status === 1}>
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
        title: `基线${baseInfo.status === 1 ? '查看' : '编辑'}`,
        breadcrumb: {
          routes: [
            {
              path: '/quality/baseline/list',
              breadcrumbName: '基线管理',
            },
            {
              path: '',
              breadcrumbName: `基线${baseInfo.status === 1 ? '查看' : '编辑'}`,
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
            content: '点击“保存”将保存信息并返回',
            okText: '保存',
            cancelText: '不保存',
            closable: true,
            onOk() {
              updateBaseInfo().then(() => {
                history.push(location.pathname)
              })
            },
            onCancel(close) {
              if(!close?.triggerCancel) {
                setBaseInfo(originBaseInfo);
                setTableModified(false);
                close();
                setTimeout(() => {
                  history.push(location.pathname);
                }, 0)
              }
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
              <Button onClick={() => setIsEdit(true)} type="primary" disabled={baseInfo.status === 1}>编辑</Button>
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
        <Form form={tableForm} className={styles['table-form']}>
          <Table<TableItem>
            rowKey="id"
            columns={tableColumns}
            dataSource={tableData}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 16 }}
            loading={loading}
            pagination={false}
          />
        </Form>
       
        <Button
          type="dashed"
          style={{display: 'block', margin: '10px 0', width: '100%'}}
          onClick={addNewLine}
          disabled={baseInfo.status === 1}
        >
          添加一行数据
        </Button>
      </div>
    
      <div className={styles.container} style={{marginTop: 16}}>
        <p>监控信息</p>
        <Button onClick={() => handleAddMonitorRule()} disabled={baseInfo.status === 1}>新增监控规则</Button>
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
