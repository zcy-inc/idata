import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import ViewEnum from './ViewEnum';
import EditEnum from './EditEnum';
import { createEnum, delEnum, getEnum } from '@/services/tablemanage';
import { initialColumns } from './constants';

export interface TabEnumProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}

const TabEnum: FC<TabEnumProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const [data, setData] = useState<any>({});
  const [columns, setColumns] = useState<any[]>(initialColumns);
  const [dataSource, setDateSource] = useState<any[]>([]);

  const { getTree, onRemovePane, replacePaneKey } = useModel('tabalmanage', (ret) => ({
    getTree: ret.getTree,
    onRemovePane: ret.onRemovePane,
    replacePaneKey: ret.replacePaneKey,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newEnum' && getEnumInfo(fileCode);
  }, []);

  const getEnumInfo = (enumCode: string) => {
    getEnum({ enumCode })
      .then((res) => {
        const enumValues = Array.isArray(res.data.enumValues) ? res.data.enumValues : [];
        const attrs = Array.isArray(enumValues[0]?.enumAttributes)
          ? enumValues[0]?.enumAttributes
          : [];
        // 格式化枚举参数生成的列
        const exCols = attrs.map((_: any) => ({
          title: _.attributeKey,
          dataIndex: _.attributeType.endsWith('ENUM')
            ? [_.attributeKey, 'value']
            : [_.attributeKey, 'code'],
          key: _.attributeKey,
          type: _.attributeType.endsWith('ENUM') ? _.attributeType.split(':')[0] : _.attributeType,
        }));
        // 格式化dataSource
        const dt = enumValues.map((_: any) => {
          const tmp = {
            enumValue: { value: _.enumValue, code: _.valueCode },
            parentValue: _.parentValue,
          };
          _.enumAttributes.forEach(
            (_enum: any) =>
              (tmp[_enum.attributeKey] = { value: _enum.enumValue, code: _enum.attributeValue }),
          );
          return tmp;
        });
        setData(res.data);
        setColumns(initialColumns.concat(exCols));
        setDateSource(dt);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    form.validateFields().then(() => {
      setLoading(true);
      const values = form.getFieldsValue();
      const data: any = {
        enumName: values.enumName,
        folderId: values.folderId,
        enumValues: values.enumValues.enums.map((_: any) => ({
          enumValue: _.enumValue.value,
          valueCode: _.enumValue.code,
          parentCode: _.parentCode,
          enumAttributes: values.enumValues.columns.map((col: any) => ({
            attributeKey: col.title,
            attributeType: col.type.length === 10 ? `${col.type}:ENUM` : col.type,
            attributeValue: _[col.title],
          })),
        })),
      };
      fileCode !== 'newEnum' && (data.enumCode = fileCode);
      createEnum(data)
        .then((res) => {
          if (res.success) {
            message.success(fileCode === 'newEnum' ? '新建枚举成功' : '更新枚举成功');
            getTree('ENUM');
            getEnumInfo(res.data.enumCode);
            setMode('view');
            // replacePaneKey(`E_${res.data.id}`);
          }
        })
        .catch((err) => [])
        .finally(() => setLoading(false));
    });
  };

  const onDelete = () => {
    setLoading(true);
    delEnum({ enumCode: fileCode })
      .then((res) => {
        if (res.success) message.success('删除成功');
        onRemovePane(`E_${fileCode}`);
        getTree('ENUM');
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewEnum info={{ data, columns, dataSource }} />}
      {mode === 'edit' && (
        <EditEnum
          form={form}
          initialValue={fileCode === 'newEnum' ? undefined : { data, columns, dataSource }}
        />
      )}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Popconfirm
            key="del"
            title="您确认要删除该枚举吗？"
            onConfirm={() => onDelete()}
            okButtonProps={{ loading }}
            okText="确认"
            cancelText="取消"
          >
            <Button>删除</Button>
          </Popconfirm>,
        ]}
        {mode === 'edit' && [
          <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
            保存
          </Button>,
          <Button
            key="cancel"
            onClick={() => {
              setMode('view');
              getEnumInfo(fileCode);
            }}
          >
            取消
          </Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabEnum;
