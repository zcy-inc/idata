import React, { Fragment, useEffect, useState } from 'react';
import { Button, Descriptions, message, Modal, Space, Table } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';

import { getLabel, getEnumValues, delLabel } from '@/services/tablemanage';
import { isEnumType } from '@/utils/tablemanage';
import {
  SubjectTypeMap,
  LabelTagMap,
  LabelRequiredMap,
  ExLabelTag,
  ExLabelTagTitle,
  InitialColumns,
  AttributeTypeMap,
} from './constants';
import Title from '@/components/Title';

export interface ViewLabelProps {
  fileCode: string;
}

const { Item } = Descriptions;
const { confirm } = Modal;

const ViewLabel: FC<ViewLabelProps> = ({ fileCode }) => {
  const [data, setData] = useState<any>({});
  const [columns, setColumns] = useState<any[]>(InitialColumns);
  const [dataSource, setDateSource] = useState<any[]>([]);

  const { getTree, showLabel, onRemovePane } = useModel('tablemanage', (ret) => ({
    getTree: ret.getTree,
    showLabel: ret.showLabel,
    onRemovePane: ret.onRemovePane,
  }));

  useEffect(() => {
    getLabel({ labelCode: fileCode })
      .then((res) => {
        setData(res.data);
        res.data.labelTag === 'ENUM_VALUE_LABEL' && renderEnumValues(res);
        res.data.labelTag === 'ATTRIBUTE_LABEL' && renderAttribute(res);
      })
      .catch((err) => {});
  }, []);

  // labelTag === 'ENUM_VALUE_LABEL'
  const renderEnumValues = (res: any) => {
    const enumCode = res.data.labelParamType;
    getEnumValues({ enumCode })
      .then((_res) => {
        const values = Array.isArray(_res.data) ? _res.data : [];
        const attrs = Array.isArray(values[0]?.enumAttributes) ? values[0].enumAttributes : [];
        const exCols = attrs.map((_: any) => ({
          title: _.attributeKey,
          dataIndex: _.attributeKey,
          key: _.attributeKey,
        }));
        const data = values.map((_: any) => {
          const tmp = { id: _.id, enumValue: _.enumValue, parentValue: _.parentValue };
          _.enumAttributes?.forEach((_enum: any) => {
            tmp[_enum.attributeKey] = _enum.attributeValue;
          });
          return tmp;
        });
        setColumns(InitialColumns.concat(exCols));
        setDateSource(data);
      })
      .catch((err) => {});
  };

  // labelTag === 'ATTRIBUTE_LABEL'
  const renderAttribute = (res: any) => {
    const cols = [
      { title: '名称', dataIndex: 'attributeKey', key: 'attributeKey' },
      {
        title: '类型',
        key: 'attributeType',
        render: (_: any) =>
          isEnumType(_.attributeType) ? _.enumName : AttributeTypeMap[_.attributeType],
      },
      {
        title: '内容',
        key: 'enumValue',
        render: (_: any) => (isEnumType(_.attributeType) ? _.enumValue : _.attributeValue),
      },
    ];
    const attrs = Array.isArray(res.data.labelAttributes) ? res.data.labelAttributes : [];
    setColumns(cols);
    setDateSource(attrs);
  };

  const onDelete = (labelCode: string) =>
    confirm({
      title: '删除标签',
      content: '您确认要删除该标签吗？',
      autoFocusButton: null,
      onOk: () =>
        delLabel({ labelCode })
          .then((res) => {
            if (res.success) message.success('删除成功');
            onRemovePane(`L_${labelCode}`);
            getTree('LABEL');
          })
          .catch((err) => {}),
    });

  return (
    <Fragment>
      <Descriptions title={<Title>基本信息</Title>} colon={false}>
        <Item label="标签名称">{data.labelName}</Item>
        <Item label="标签主体">{SubjectTypeMap[data.subjectType]}</Item>
        <Item label="标签类型">{LabelTagMap[data.labelTag]}</Item>
        <Item label="是否必填">{LabelRequiredMap[data.labelRequired]}</Item>
        <Item label="排序编号">{data.labelIndex || '-'}</Item>
        <Item label="创建人">{data.creator}</Item>
      </Descriptions>
      {ExLabelTag.includes(data.labelTag) && (
        <Descriptions colon={false} layout="vertical">
          <Item label={ExLabelTagTitle[data.labelTag]} contentStyle={{ display: 'block' }}>
            <Table
              rowKey={(_) => _.id || _.attributeKey}
              columns={columns}
              dataSource={dataSource}
              pagination={false}
              size="middle"
            />
          </Item>
        </Descriptions>
      )}
      <div className="workbench-submit">
        <Space>
          <Button type="primary" size="large" onClick={() => showLabel(fileCode)}>
            编辑
          </Button>
          <Button key="del" size="large" onClick={() => onDelete(fileCode)}>
            删除
          </Button>
        </Space>
      </div>
    </Fragment>
  );
};

export default ViewLabel;
