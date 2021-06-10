import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import { ViewInitialColumns } from './constants';
import Title from '../Title';
import TableRelation from './TableRelation';

export interface ViewTableProps {
  data: any;
}

const { Item } = Descriptions;
const { TabPane } = Tabs;

const ViewTable: FC<ViewTableProps> = ({ data }) => {
  const [columns, setColumns] = useState<any[]>([]);
  const [dataSource, setDataSource] = useState<any[]>([]);

  useEffect(() => {
    if (data) {
      const dts = Array.isArray(data.columnInfos) ? data.columnInfos : [];
      const colLabels = Array.isArray(dts[0]?.columnLabels) ? dts[0].columnLabels : [];
      const exCols = colLabels.map((_: any) => ({
        title: _.labelName,
        dataIndex: _.labelCode,
        key: _.labelCode,
      }));
      const dt = dts.map((_: any) => {
        const tmp = { columnName: _.columnName };
        _.columnLabels.forEach((item: any) => (tmp[item.labelCode] = transformLabelValue(item)));
        return tmp;
      });
      setColumns(ViewInitialColumns.concat(exCols));
      setDataSource(dt);
    }
  }, [data]);

  const transformLabelValue = (_: any) => {
    switch (_.labelTag) {
      case 'ENUM_VALUE_LABEL':
        return _.enumNameOrValue;
      default:
        return _.labelParamValue;
    }
  };

  return (
    <Fragment>
      <Descriptions
        title={<Title>基本信息</Title>}
        colon={false}
        labelStyle={{ color: '#8A8FAE', textAlign: 'right' }}
      >
        <Item label="表名称">{data?.tableName}</Item>
        {data?.tableLabels?.map((_: any) => (
          <Item key={_.id} label={_.labelName}>
            {_.labelTag === 'ENUM_VALUE_LABEL' ? _.enumNameOrValue : _.labelParamValue}
          </Item>
        ))}
      </Descriptions>
      <Tabs className={`${styles.reset} ${styles['reset-tabs']}`}>
        <TabPane key="struct" tab="表结构设计" style={{ paddingTop: 24 }}>
          <Table
            rowKey="columnName"
            columns={columns}
            dataSource={dataSource}
            pagination={false}
            size="small"
          />
        </TabPane>
        <TabPane key="fk" tab="关系" style={{ paddingTop: 24 }}>
          <TableRelation id={data?.id} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewTable;
