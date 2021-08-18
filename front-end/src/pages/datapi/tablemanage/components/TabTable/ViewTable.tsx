import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs, Tooltip } from 'antd';
import type { FC, CSSProperties } from 'react';
import styles from '../../index.less';

import { getDWOwner } from '@/services/tablemanage';
import { ColumnLabel, TableLable, User } from '@/types/datapi';
import { LabelTag } from '@/constants/datapi';
import { ViewInitialColumns, TransformBoolean } from './constants';

import Title from '../../../components/Title';
import TableRelation from './components/TableRelation';

export interface ViewTableProps {
  data: any;
}

const { Item } = Descriptions;
const { TabPane } = Tabs;
const ellipsis: CSSProperties = {
  display: 'inline-block',
  whiteSpace: 'nowrap',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
};

const ViewTable: FC<ViewTableProps> = ({ data }) => {
  const [columns, setColumns] = useState<any[]>([]);
  const [dataSource, setDataSource] = useState<any[]>([]);
  const [users, setUsers] = useState<User[]>();

  useEffect(() => {
    getDWOwner()
      .then((res) => {
        setUsers(res.data.content);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (data) {
      const columnInfos = Array.isArray(data.columnInfos) ? data.columnInfos : [];
      const columnLabels = Array.isArray(columnInfos[0]?.columnLabels)
        ? columnInfos[0].columnLabels
        : [];
      const exCols = columnLabels.map((_: ColumnLabel) => ({
        title: _.labelName,
        dataIndex: _.labelCode,
        key: _.labelCode,
        render: (_: any) => _ || '-',
      }));
      const dt = columnInfos.map((columnInfo: ColumnLabel) => {
        const tmp = { columnName: columnInfo.columnName };
        columnInfo.columnLabels?.forEach(
          (item: TableLable) => (tmp[item.labelCode] = transformLabelValue(item)),
        );
        return tmp;
      });

      setColumns(ViewInitialColumns.concat(exCols));
      setDataSource(dt);
    }
  }, [data]);

  const transformLabelValue = (_: any) => {
    switch (_.labelTag) {
      case LabelTag.ENUM_VALUE_LABEL:
        return _.enumNameOrValue || '-';
      case LabelTag.BOOLEAN_LABEL:
        return TransformBoolean[_.labelParamValue] || '-';
      case LabelTag.USER_LABEL:
        return users?.find((user) => `${user.id}` === _.labelParamValue)?.nickname || '-';
      default:
        return _.labelParamValue || '-';
    }
  };

  return (
    <Fragment>
      <Descriptions
        title={<Title>基本信息</Title>}
        colon={false}
        labelStyle={{ color: '#8A8FAE', textAlign: 'right' }}
      >
        <Item label="表名称" contentStyle={ellipsis}>
          <Tooltip placement="topLeft" title={data?.tableName}>
            {data?.tableName}
          </Tooltip>
        </Item>
        {data?.tableLabels?.map((_: TableLable) => (
          <Item key={_.id} label={_.labelName} contentStyle={ellipsis}>
            <Tooltip placement="topLeft" title={transformLabelValue(_)}>
              {transformLabelValue(_)}
            </Tooltip>
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
