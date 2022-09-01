import React from 'react';
import { ProFormSelect, ProFormText, QueryFilter } from '@ant-design/pro-form';
import { Form, Table, Button, message, Popconfirm } from 'antd';
import type { FC } from 'react';
import { useAntdTable } from 'ahooks';
import type { ColumnsType } from 'antd/lib/table/Table';
import styles from './index.less';
import { getStreamJobs, startJob, stopJob, destoryJob } from '@/services/operations';
import { statusList } from '@/constants/operations';
import { PageContainer } from '@/components';
import type { StreamListItem } from '@/types/operations';
import StartJob from './components/StartJob';
import Detail from './components/Detail';
import showDialog from '@/utils/showDialog';
import showDrawer from '@/utils/showDrawer';

const Stream: FC<{}> = ({}) => {
  const [form] = Form.useForm();
  const { tableProps, search, refresh }= useAntdTable((params, formParams) => {
    return getStreamJobs({
      ...formParams,
      limit: params.pageSize,
      offset: (params.current - 1) * params.pageSize
    }).then(res => ({
      list: res.data.content,
      total: res.data.total
    }))
  }, {
    form
  }); 

  // 启动/停止
  const toggleState = (row: StreamListItem, isStop: boolean) => {
    if(isStop) {
      stopJob({id: row.id}).then(() => {
        message.success("操作成功！");
        refresh();
      })
    } else {
      showDialog('启动任务',{
        formProps: {
          id: row.id
        },
        modalProps: {
          width: 500
        },
        beforeConfirm: (dialog, formInstance, done) => {
          dialog.showLoading();
          formInstance.handleSubmit().then((res: {forceInitTables: string []}) => {
            return startJob({
              id: row.id,
              initDITables: res.forceInitTables
            })
          }).then(() => {
            message.success("操作成功！");
            done();
            refresh();
          }).finally(() => {
            dialog.hideLoading();
          })
        }
      }, StartJob)
    }
  }

  // 下线
  const offLine = (row: StreamListItem) => {
    destoryJob({id: row.id}).then(() => {
      message.success("操作成功！");
      refresh();
    })
  }

  // 查看详情
  const showDetail = (row: StreamListItem) => {
    showDrawer('任务详情', {
      drawerProps: {
        width: 1000
      },
      btns: {
        positive: false,
        negetive: false
      }
    }, Detail)
  }

  const columns: ColumnsType<StreamListItem> = [
    { title: '任务ID', key: 'jobId', dataIndex: 'jobId' },
    {
      title: '任务名称',
      key: 'jobName',
      dataIndex: 'jobName',
      width: 120,
      render: (text, row) => {
        return <Button onClick={() => showDetail(row)} type="link">{text}</Button>
      }
    },
    { 
      title: '任务状态',
      key: 'status',
      dataIndex: 'status',
      render: (text, row) => {
        return statusList.find(item => item.value === text)?.label
      }
    },
    { title: '版本', key: 'jobContentVersionDisplay', dataIndex: 'jobContentVersionDisplay' },
    { title: '环境', key: 'environment', dataIndex: 'environment' },
    { title: '责任人', key: 'owner', dataIndex: 'owner', width: 100 },
    { title: '运行开始时间', key: 'runStartTime', dataIndex: 'runStartTime', width:180 },
    { title: '最近操作时间', key: 'editTime', dataIndex: 'editTime',  width:180 },
    { title: '最近操作人', key: 'editor', dataIndex: 'editor',  width:110 },
    {
      title: '操作',
      key: 'status',
      dataIndex: 'status',
      fixed: 'right',
      render: (text, row) => {
        if(text !== 9) {
          return <>
            {[2, 7].includes(text) ?
              <Popconfirm
                title="你确定要停止该任务吗？"
                onConfirm={() => offLine(row)}
                disabled={text === 1}
              >
                <Button type="link" disabled={text === 1} onClick={() => toggleState(row, true)}>
                  停止
                </Button>
              </Popconfirm>
               :
              <Button type="link" disabled={text === 1} onClick={() => toggleState(row, false)}>
                启动
              </Button>}
            <Popconfirm
              title={<div>如果任务为运行中，<span style={{color:'red'}}>下线将自动停止任务</span>。你确定要下线该任务吗？</div>}
              onConfirm={() => offLine(row)}
              disabled={[1, 2, 7].includes(text)}
            >
              <Button type="link" disabled={[1, 2, 7].includes(text)}>
                下线
              </Button>
            </Popconfirm>
            {row.externalUrl && <Button
              type="link"
              onClick={() => window.open(row.externalUrl)}
              disabled={[0, 1].includes(text)}
            >日志</Button>}
          </>
        } else { // 已下线
          return null;
        }
      }
    },
  ];

  return (
    <PageContainer className={styles.container}>
      <QueryFilter
        form={form}
        layout="horizontal"
        onFinish={async ()=> search.submit()}
        onReset={search.reset}
        labelCol={{span: 6}}
        labelWidth={96}
      >
        <ProFormText
          name="jobNamePattern"
          label="任务名称"
          placeholder="请输入"
        />
        <ProFormSelect
          name="statusList"
          label="任务状态"
          placeholder="请选择"
          options={statusList}
          fieldProps={{
            mode: "multiple",
            maxTagCount: 2
          }}
        />
        <ProFormText
          name="ownerPattern"
          label="负责人"
          placeholder="请输入"
        />
      </QueryFilter>
      <Table<StreamListItem>
        rowKey="id"
        columns={columns}
        {...tableProps}
      />
    </PageContainer>
  );
};

export default Stream;
