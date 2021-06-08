import React, { Fragment } from 'react';
import { Descriptions, Table } from 'antd';
import type { FC } from 'react';

import Title from '../Title';

export interface ViewEnumProps {
  info: { data: any; columns: any[]; dataSource: any[] };
}

const { Item } = Descriptions;

const ViewEnum: FC<ViewEnumProps> = ({ info: { data, columns, dataSource } }) => {
  return (
    <Fragment>
      <Descriptions
        title={<Title>基本信息</Title>}
        colon={false}
        column={1}
        labelStyle={{ color: '#8A8FAE' }}
      >
        <Item label="枚举类型名称">{data?.enumName}</Item>
        <Item label="创建人">{data?.creator}</Item>
      </Descriptions>
      <Descriptions colon={false} labelStyle={{ color: '#8A8FAE' }} layout="vertical">
        <Item label="枚举值" contentStyle={{ display: 'block' }}>
          <Table
            rowKey={(row) => row.enumValue.code}
            columns={columns}
            dataSource={dataSource}
            pagination={false}
            size="middle"
          />
        </Item>
      </Descriptions>
    </Fragment>
  );
};

export default ViewEnum;
