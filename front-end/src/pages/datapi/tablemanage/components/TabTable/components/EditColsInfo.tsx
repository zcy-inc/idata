import React, { Fragment, useEffect, useState, useImperativeHandle, forwardRef } from 'react';
import { Typography, Select, Input, Switch, Divider } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../../index.less';

import IconFont from '@/components/IconFont';

export type StructAction = 'del' | 'up' | 'down';
export interface EditColsInfoProps {
  initial?: any;
  _props: {
    checkedList: string[];
    columns: any[];
    columnsMap: Map<string, any>;
  };
}

const { Link } = Typography;

const EditColsInfo: ForwardRefRenderFunction<unknown, EditColsInfoProps> = (
  { initial, _props: { checkedList, columns, columnsMap } },
  ref,
) => {
  // 表结构的列与数据
  const [data, setData] = useState<any[]>([]);
  const [keys, setKeys] = useState<React.Key[]>([]);

  // 暴露表结构的数据给父组件
  // 这儿还有一种做法是用表单的formRef来获取, 但是这样拿不到index, 所以用useImperativeHandle
  useImperativeHandle(ref, () => ({ data }));

  useEffect(() => {
    if (initial) {
      const columnsInfo = initial.columnInfos;
      const _keys: React.Key[] = [];
      const _data = columnsInfo.map((_: any) => {
        const t = { key: _.id, id: _.id, columnName: _.columnName };
        _.columnLabels.forEach((l: any) => {
          let v = l.labelParamValue;
          l.labelTag === 'BOOLEAN_LABEL' && (v = v === 'true');
          t[l.labelCode] = v;
        });
        _keys.push(_.id);
        return t;
      });
      setData(_data);
      setKeys(_keys);
    }
  }, [initial]);

  // 添加一行数据
  const addData = () => {
    const key = Date.now();
    const item = { key };
    columns.forEach(
      (_: any) => (item[_.labelCode] = _.labelTag === 'BOOLEAN_LABEL' ? false : null),
    );
    setData([...data, item]);
    setKeys([...keys, key]);
  };

  // 因为是自己维护的data, 所以手动录入数据
  const setValue = (schema: any, value: any) => {
    data[schema.index][schema.dataIndex] = value;
    setData([...data]);
  };

  // 渲染表结构的列
  const renderColumns = () =>
    checkedList
      .map((labelCode) => {
        const _ = columnsMap?.get(labelCode);
        const column: ProColumns = {
          title: _.labelName,
          dataIndex: _.labelCode,
          key: _.labelCode,
          width: 200,
          // labelRequired,
        };
        switch (_.labelTag) {
          case 'STRING_LABEL':
            Object.assign(column, {
              renderFormItem: (schema: any) => (
                <Input
                  placeholder="请输入"
                  onChange={({ target: { value } }) => setValue(schema, value)}
                  required
                />
              ),
            });
            break;
          case 'BOOLEAN_LABEL':
            Object.assign(column, {
              width: 'auto',
              renderFormItem: (schema: any) => (
                <Switch
                  checked={data[schema.index][schema.dataIndex]}
                  onChange={(checked) => setValue(schema, checked)}
                />
              ),
            });
            break;
          case 'ENUM_VALUE_LABEL':
          default:
            Object.assign(column, {
              renderFormItem: (schema: any) => (
                <Select
                  placeholder="请选择"
                  options={_.enums}
                  onChange={(value) => setValue(schema, value)}
                />
              ),
            });
            break;
        }
        return column;
      })
      .concat({ title: '操作', valueType: 'option', fixed: 'right', width: 140 });

  // 表结构的操作栏事件处理
  const onStructAction = (row: any, _: any, type: StructAction) => {
    const i = data.findIndex((_) => _.key === row.key);
    if (type === 'del') {
      data.splice(i, 1);
      keys.splice(i, 1);
    }
    if (type === 'up') {
      [data[i - 1], data[i]] = [data[i], data[i - 1]];
      [keys[i - 1], keys[i]] = [keys[i], keys[i - 1]];
    }
    if (type === 'down') {
      [data[i + 1], data[i]] = [data[i], data[i + 1]];
      [keys[i + 1], keys[i]] = [keys[i], keys[i + 1]];
    }
    setData([...data]);
    setKeys([...keys]);
  };

  return (
    <Fragment>
      <EditableProTable
        className={styles.struct}
        rowKey="key"
        columns={renderColumns()}
        value={data}
        bordered
        scroll={{ x: 'max-content' }}
        style={{ paddingTop: 24 }}
        recordCreatorProps={false}
        editable={{
          type: 'multiple',
          editableKeys: keys,
          onChange: setKeys,
          actionRender: (row, _) => {
            return [
              <Link key="del" onClick={() => onStructAction(row, _, 'del')}>
                删除
              </Link>,
              <Divider key="div1" type="vertical" />,
              <Link
                key="up"
                disabled={row.index === 0}
                onClick={() => onStructAction(row, _, 'up')}
              >
                上移
              </Link>,
              <Divider key="div2" type="vertical" />,
              <Link
                key="down"
                disabled={row.index === (_.editableKeys?.length || 1) - 1}
                onClick={() => onStructAction(row, _, 'down')}
              >
                下移
              </Link>,
            ];
          },
        }}
      />
      <Link className={styles['add-string']} onClick={addData}>
        <IconFont type="icon-tianjia" />
        添加字段
      </Link>
    </Fragment>
  );
};

export default forwardRef(EditColsInfo);
