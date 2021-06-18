import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table } from 'antd';
import type { FC } from 'react';

import { Enum, EnumValue, LabelAttribute } from '@/types/tablemanage';
import Title from '../../../components/Title';

export interface ViewEnumProps {
  data?: Enum;
}

interface EnumListItem {
  parentValue: string;
  enumValue: {
    code: string;
    value: string;
  };
  [index: string]: any; // TODO
}

const { Item } = Descriptions;
const ViewInitialColumns = [
  { title: '枚举值', dataIndex: ['enumValue', 'value'], key: 'enumValue', type: 'STRING' },
  {
    title: '父级枚举值',
    dataIndex: 'parentValue',
    key: 'parentValue',
    type: 'STRING',
    render: (_: any) => _ || '-',
  },
];

const ViewEnum: FC<ViewEnumProps> = ({ data }) => {
  const [columns, setColumns] = useState(ViewInitialColumns);
  const [dataSource, setDateSource] = useState<EnumListItem[]>();

  useEffect(() => {
    const enumValues = Array.isArray(data?.enumValues) ? data?.enumValues : [];
    const attrs = Array.isArray(enumValues[0]?.enumAttributes) ? enumValues[0]?.enumAttributes : [];
    // 格式化枚举参数生成的列
    const exCols = attrs.map((_: LabelAttribute) => ({
      title: _.attributeKey,
      dataIndex: _.attributeType.endsWith('ENUM')
        ? [_.attributeKey, 'value']
        : [_.attributeKey, 'code'],
      key: _.attributeKey,
      type: _.attributeType.endsWith('ENUM') ? _.attributeType.split(':')[0] : _.attributeType,
    }));
    // 格式化dataSource
    const dt = enumValues.map((_: EnumValue) => {
      const tmp: EnumListItem = {
        parentValue: _.parentValue || '-',
        enumValue: {
          value: _.enumValue,
          code: _.valueCode,
        },
      };
      _.enumAttributes.forEach(
        (_enum: LabelAttribute) =>
          (tmp[_enum.attributeKey] = {
            value: _enum.enumValue || '-',
            code: _enum.attributeValue,
          }),
      );
      return tmp;
    });
    setColumns(ViewInitialColumns.concat(exCols));
    setDateSource(dt);
  }, [data]);

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
        <Item
          label="枚举值"
          labelStyle={{ fontWeight: 'bold', color: '#15172f', fontSize: 16, marginTop: 16 }}
          contentStyle={{ display: 'block' }}
        >
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
