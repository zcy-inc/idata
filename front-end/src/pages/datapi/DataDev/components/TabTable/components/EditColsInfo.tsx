import React, { useEffect, useState, useImperativeHandle, forwardRef } from 'react';
import { Typography, Select, Input, Switch } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction, Key } from 'react';

import IconFont from '@/components/IconFont';
import { ColumnLabel, Table, TableLable } from '@/types/datapi';

export type StructAction = 'del' | 'up' | 'down';
export interface EditColsInfoProps {
  initial?: Table;
  colProps: {
    checkedList: string[];
    columns: any[];
    columnsMap: Map<string, any>;
  };
}

const { Link } = Typography;

const EditColsInfo: ForwardRefRenderFunction<unknown, EditColsInfoProps> = (
  { initial, colProps: { checkedList, columns, columnsMap } },
  ref,
) => {
  // 表结构的列与数据
  const [data, setData] = useState<any[]>([]);
  const [keys, setKeys] = useState<Key[]>([]);

  useImperativeHandle(ref, () => ({ data }));

  useEffect(() => {
    if (initial) {
      const tmpKeys: Key[] = [];
      const tmpData = initial.columnInfos?.map((column: ColumnLabel) => {
        const tmpKey = new Date().getTime();
        const tmp = {
          key: column.columnName || tmpKey,
          columnName: column.columnName,
        };
        column.columnLabels?.forEach((label: TableLable) => {
          let v: string | boolean = label.labelParamValue;
          label.labelTag === 'BOOLEAN_LABEL' && (v = v === 'true');
          tmp[label.labelCode] = v;
        });
        tmpKeys.push(tmp.key);
        return tmp;
      });

      setData(tmpData);
      setKeys(tmpKeys);
    }
  }, [initial]);

  // 添加一行数据
  const addData = () => {
    const key = Date.now();
    const item = { key };
    columns?.forEach(
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

  const renderInputPrefix = (_: any, i: number) => {
    if (i === 0 && _.enableCompare && _.hiveDiff) {
      return <IconFont type="icon-baocuo" />;
    } else {
      return null;
    }
  };

  // 渲染表结构的列
  const renderColumns = () =>
    checkedList
      .map((labelCode, i) => {
        const _ = columnsMap?.get(labelCode);
        const column: ProColumns = {
          title: [
            _.labelRequired === 1 && <span style={{ color: '#f50', marginRight: 2 }}>*</span>,
            _.labelName,
          ],
          key: _.labelCode,
          dataIndex: _.labelCode,
          width: 200,
        };
        switch (_.labelTag) {
          case 'STRING_LABEL':
            Object.assign(column, {
              renderFormItem: (schema: any) => (
                <Input
                  placeholder="请输入"
                  onChange={({ target: { value } }) => setValue(schema, value)}
                  required
                  prefix={renderInputPrefix(_, i)}
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
                  showSearch
                  filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
                />
              ),
            });
            break;
        }
        return column;
      })
      .concat({ title: '操作', key: 'option', valueType: 'option', fixed: 'right', width: 140 });

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
    <>
      <EditableProTable
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
              <Link
                key="up"
                disabled={row.index === 0}
                onClick={() => onStructAction(row, _, 'up')}
              >
                上移
              </Link>,
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
      <Link onClick={addData} style={{ display: 'inline-block', marginTop: 14 }}>
        <IconFont type="icon-tianjia" style={{ marginRight: 8 }} />
        添加字段
      </Link>
    </>
  );
};

export default forwardRef(EditColsInfo);
