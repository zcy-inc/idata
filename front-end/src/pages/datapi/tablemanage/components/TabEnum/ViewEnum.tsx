import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table } from 'antd';
import type { FC } from 'react';

import { Enum, EnumValue, LabelAttribute } from '@/types/datapi';
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
const ViewInitialColumns: {
  title: string;
  dataIndex: string | string[];
  key: string;
  render?: (_: any) => any;
}[] = [
  { title: '枚举值', dataIndex: ['enumValue', 'value'], key: 'enumValue' },
  {
    title: '父级枚举值',
    dataIndex: 'parentValue',
    key: 'parentValue',
    render: (_: any) => _ || '-',
  },
];

const ViewEnum: FC<ViewEnumProps> = ({ data }) => {
  const [columns, setColumns] = useState(ViewInitialColumns);
  const [dataSource, setDateSource] = useState<EnumListItem[]>();

  useEffect(() => {
    if (data) {
      const enumValues = data.enumValues;
      const enumAttributes = enumValues?.[0]?.enumAttributes || [];
      // 格式化枚举参数生成的列
      const exCols = enumAttributes?.map((enumAttribute: LabelAttribute) => ({
        key: enumAttribute.attributeKey,
        dataIndex: enumAttribute.attributeKey,
        title: enumAttribute.attributeKey,
      }));
      // 格式化dataSource
      const dt = enumValues?.map((enumValue: EnumValue) => {
        const tmp: EnumListItem = {
          parentValue: enumValue.parentValue || '-',
          enumValue: { value: enumValue.enumValue, code: enumValue.valueCode },
        };
        enumValue.enumAttributes?.forEach(
          (enumAttribute: LabelAttribute) =>
            (tmp[enumAttribute.attributeKey] = transformValue(enumAttribute)),
        );
        return tmp;
      });

      setColumns(ViewInitialColumns.concat(exCols));
      setDateSource(dt);
    }
  }, [data]);

  const transformValue = (enumAttribute: LabelAttribute) => {
    let value = enumAttribute.enumValue || enumAttribute.attributeValue || '-';
    if (enumAttribute.attributeType === 'BOOLEAN') {
      value = value === 'true' ? '是' : '否';
    }
    return value;
  };

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
