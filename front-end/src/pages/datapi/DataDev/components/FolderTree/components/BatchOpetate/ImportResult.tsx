import React from 'react';
import { Table } from 'antd';

export default ({ data} : {data: any []}, _: any) => {
  const columns = [
    {
      title: '文件名',
      dataIndex: 'jobName',
      key: 'jobName',
    },
    {
      title: '失败原因',
      dataIndex: 'msg',
      key: 'msg',
      render: (text: string) => <span style={{color: '#FF5753'}}>{text}</span>
    },
  ];
  const errorData = data.filter((item: { success: boolean; }) => !item.success)
  return <div>
    <p>总共 {data.length} 条，失败 <span style={{color: '#FF5753'}}>{errorData.length}</span>条</p>
    <Table
      dataSource={errorData}
      columns={columns}
      pagination={false}
      rowKey='id'
      scroll={{ y: 315 }}
    />
  </div>
}