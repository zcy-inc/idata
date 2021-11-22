import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs, Tooltip } from 'antd';
import type { FC, CSSProperties } from 'react';

import { getDWOwner, getTableLabels } from '@/services/datadev';
import { ColumnLabel, TableLable, User } from '@/types/datapi';
import { LabelTag } from '@/constants/datapi';
import { TransformBoolean } from './constants';

import Title from '@/components/Title';
import ER from './components/ER';
import { IconFont } from '@/components';

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
const ViewInitialColumns = [
  {
    title: '字段英文名',
    dataIndex: 'columnName',
    key: 'columnName',
    render: (_: string, record: any) => (
      <div>
        {record.enableCompare && record.hiveDiff && <IconFont type="icon-baocuo" />}
        <span>{_}</span>
      </div>
    ),
  },
];

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
      getTableLabels({ subjectType: 'COLUMN' }).then((res) => {
        const columnInfos = Array.isArray(data.columnInfos) ? data.columnInfos : [];
        const columnLabels = Array.isArray(res.data) ? res.data : [];
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
      });
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
      <Descriptions title={<Title>基本信息</Title>} colon={false}>
        <Item label="表名称" contentStyle={ellipsis}>
          <Tooltip placement="topLeft" title={data?.tableName}>
            {data?.tableName}
          </Tooltip>
        </Item>
        {data?.tableLabels?.map((_: TableLable) => (
          <Item key={_.id} label={_.labelName} contentStyle={ellipsis}>
            {_.labelCode === 'metabaseUrl:LABEL' ? (
              <a target="_blank" href={transformLabelValue(_)}>
                查看
              </a>
            ) : (
              <Tooltip placement="topLeft" title={transformLabelValue(_)}>
                {transformLabelValue(_)}
              </Tooltip>
            )}
          </Item>
        ))}
      </Descriptions>
      <Tabs className="reset-tabs">
        <TabPane key="struct" tab="表结构设计" style={{ paddingTop: 24 }}>
          <Table
            rowKey="columnName"
            columns={columns}
            dataSource={dataSource}
            pagination={false}
            scroll={{ x: 'max-content' }}
          />
        </TabPane>
        <TabPane key="fk" tab="关系" style={{ paddingTop: 24 }}>
          <ER id={data?.id} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewTable;
