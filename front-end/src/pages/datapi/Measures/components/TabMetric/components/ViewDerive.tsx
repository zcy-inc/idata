import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';

import Title from '@/components/Title';
import { Metric } from '@/types/datapi';

interface EditDeriveProps {
  data: Metric;
}
interface MetricModifierView {
  modifierName: string;
  enumValues: string;
}
interface DIMList {
  labelName: string;
  enName: string;
  tableName: string;
  columnName: string;
}

const { TabPane } = Tabs;
const { Item } = Descriptions;
const ColsDerive = [
  { title: '修饰词', dataIndex: 'modifierName', key: 'modifierName' },
  { title: '枚举值', dataIndex: 'enumValues', key: 'enumValues' },
];
const ColsDIM = [
  { title: '维度名称', dataIndex: 'labelName', key: 'labelName' },
  { title: '英文别名', dataIndex: 'enName', key: 'enName' },
  { title: '表名', dataIndex: 'tableName', key: 'tableName' },
  { title: '关联表字段', dataIndex: 'columnName', key: 'columnName' },
];

const EditDerive: FC<EditDeriveProps> = ({ data }) => {
  const [modifiers, setModifiers] = useState<MetricModifierView[]>([]);
  const [DIM, setDIM] = useState<DIMList[]>([]);

  useEffect(() => {
    if (data) {
      // 修饰词列表
      const tmp = data.modifiers?.map((modifier) => ({
        modifierName: modifier.modifierName,
        enumValues: modifier.enumValues.join(' / '),
      }));
      // 维度
      const dim = data.dimensions?.map((dimension) => ({
        labelName: dimension.labelName,
        enName: dimension.enName as string,
        tableName: dimension.measureLabels[0].tableName,
        columnName: dimension.measureLabels[0].columnName,
      }));
      setModifiers(tmp);
      setDIM(dim);
    }
  }, [data]);

  return (
    <Fragment>
      <Title>配置派生指标</Title>
      <Descriptions colon={false} style={{ margin: '16px 0' }}>
        <Item label="原子指标" style={{ paddingBottom: 0 }}>
          {data.specialAttribute.atomicMetricName || '-'}
        </Item>
      </Descriptions>
      <Table
        columns={ColsDerive}
        dataSource={modifiers}
        pagination={false}
        scroll={{ x: 'max-content' }}
        style={{ marginBottom: 24 }}
      />
      <Title>关联信息</Title>
      <Tabs className="reset-tabs">
        <TabPane key="dim" tab="维度">
          <Table
            columns={ColsDIM}
            dataSource={DIM}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 16 }}
          />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default EditDerive;
