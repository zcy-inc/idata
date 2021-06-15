import React, { Fragment, useEffect, useState, useImperativeHandle, forwardRef } from 'react';
import { Checkbox, Popover, Tabs, Typography, Select, Input, Switch, Divider } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { FormInstance } from 'antd';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';
import TableLabels from './TableLabels';
import TableRelation from './TableRelation';
import Title from '../Title';
import { InitialColumn, EROps } from './constants';

import {
  getTableLabels,
  getTableReferDbs,
  getTableReferTbs,
  getTableReferStr,
} from '@/services/tablemanage';

export type StructAction = 'del' | 'up' | 'down';
export type EnumValueType = { enumValue: string; valueCode: string };
export interface EditTableProps {
  refs: { [key: string]: FormInstance };
  initial?: any;
}

const CheckboxGroup = Checkbox.Group;
const { TabPane } = Tabs;
const { Link } = Typography;

const EditTable: FC<EditTableProps> = ({ refs, initial }, ref) => {
  // 表结构可选列设置
  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [allChecked, setAllChecked] = useState(true);
  const [indeterminate, setIndeterminate] = useState(false);
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');
  // 表结构的列与数据
  const [columns, setColumns] = useState<any[]>([]);
  const [columnsMap, setColumnsMap] = useState<Map<string, any>>(new Map());
  const [structData, setStructData] = useState<any[]>([]);
  const [structKeys, setStructKeys] = useState<React.Key[]>([]);
  // 外键
  const [strOps, setStrOps] = useState<any[]>([]);
  const [fkData, setFkData] = useState<any[]>([]);
  const [fkKeys, setFkKeys] = useState<React.Key[]>([]);
  const [dbs, setDbs] = useState<any[]>([]);
  const [tbs, setTbs] = useState<any[][]>([]);
  const [str, setStr] = useState<any[][]>([]);

  // 暴露表结构和外键的数据给父组件
  // 这儿还有一种做法是用表单的formRef来获取, 但是这样拿不到index, 所以用useImperativeHandle
  useImperativeHandle(ref, () => ({ structData, fkData }));

  useEffect(() => {
    getTableLabels({ subjectType: 'COLUMN' }).then((res) => {
      const map = new Map(); // 检索用的map
      const list: string[] = []; // checked list
      // check.group 用的ops
      const ops = [InitialColumn, ...res.data]
        .filter((_: any) => _.labelTag !== 'ATTRIBUTE_LABEL')
        .map((_: any) => {
          const tmp = { ..._, label: _.labelName, value: _.labelCode, disabled: _.labelRequired };
          if (_.labelTag === 'ENUM_VALUE_LABEL') {
            tmp.enums = _.enumValues.map((item: any) => ({
              label: item.enumValue,
              value: item.valueCode,
            }));
          }
          map.set(_.labelCode, tmp);
          list.push(_.labelCode);
          return tmp;
        });
      setColumnsMap(map);
      setCheckedList(list);
      setColumns(ops);
    });
    getTableReferDbs()
      .then((res) => {
        const dbs = res.data.map((_: any) => ({
          label: _.labelParamValue,
          value: _.labelParamValue,
        }));
        setDbs(dbs);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    console.log(initial);
    if (initial) {
      const columnsInfo = initial.columnInfos;
      const kc: React.Key[] = [];
      const dtc = columnsInfo.map((_: any) => {
        const t = { id: _.id, columnName: _.columnName };
        _.columnLabels.forEach((l: any) => {
          let v = l.labelParamValue;
          l.labelTag === 'BOOLEAN_LABEL' && (v = v === 'true' ? true : false);
          t[l.labelCode] = v;
        });
        kc.push(_.id);
        return t;
      });
      setStructData(dtc);
      setStructKeys(kc);

      const foreignKeys = initial.foreignKeys;
      const promisesT: Promise<any>[] = [];
      const promisesC: Promise<any>[] = [];
      const kf: React.Key[] = [];
      const dtf = foreignKeys.map((_: any, i: number) => {
        kf.push(_.id);
        promisesT[i] = getTableReferTbs({ labelValue: _.referDbName });
        promisesC[i] = getTableReferStr({ tableId: _.referTableId });
        return {
          id: _.id,
          columnNames: _.columnNames.split(','),
          referDbName: _.referDbName,
          referTableId: _.referTableId,
          referColumnNames: _.referColumnNames.split(','),
          erType: _.erType,
        };
      });
      Promise.all(promisesT).then((resT) => {
        Promise.all(promisesC).then((resC) => {
          resT.forEach((_T: any, i: number) => {
            tbs[i] = _T.data.map((_: any) => ({ label: _.tableName, value: _.id }));
          });
          resC.forEach((_C: any, i: number) => {
            str[i] = _C.data.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          });
          setTbs([...tbs]);
          setStr([...str]);
        });
      });
      setFkData(dtf);
      setFkKeys(kf);
    }
  }, [initial]);

  // 单选
  const onCheck = (list: any[]) => {
    setCheckedList(list);
    setAllChecked(list.length === columns.length);
    setIndeterminate(!!list.length && list.length < columns.length);
  };
  // 全选
  const onAllCheck = (checked: boolean) => {
    const list = checked
      ? columns.map((_) => _.value)
      : columns.filter((_) => _.labelRequired).map((_) => _.value);
    setCheckedList(list);
    setAllChecked(checked);
    setIndeterminate(!checked);
  };
  // 添加一行数据
  const addData = (type: 'struct' | 'fk') => {
    const id = Date.now();
    const data = { id };
    columns.forEach(
      (_: any) => (data[_.labelCode] = _.labelTag === 'BOOLEAN_LABEL' ? false : null),
    );
    if (type === 'struct') {
      setStructData([...structData, data]);
      setStructKeys([...structKeys, id]);
    }
    if (type === 'fk') {
      setFkData([...fkData, data]);
      setFkKeys([...fkKeys, id]);
    }
  };
  // 因为是自己维护的data, 所以手动录入数据
  const setValue = (schema: any, value: any, type: 'struct' | 'fk') => {
    if (type === 'struct') {
      structData[schema.index][schema.dataIndex] = value;
      setStructData([...structData]);
    }
    if (type === 'fk') {
      if (schema.dataIndex === 'referDbName') {
        getTableReferTbs({ labelValue: value })
          .then((res) => {
            const tmp = res.data.map((_: any) => ({ label: _.tableName, value: _.id }));
            tbs[schema.index] = tmp;
            setTbs([...tbs]);
          })
          .catch((err) => {});
      }
      if (schema.dataIndex === 'referTableId') {
        getTableReferStr({ tableId: value })
          .then((res) => {
            const tmp = res.data.map((_: any) => ({ label: _.columnName, value: _.columnName }));
            str[schema.index] = tmp;
            setStr([...str]);
          })
          .catch((err) => {});
      }
      fkData[schema.index][schema.dataIndex] = value;
      setFkData([...fkData]);
    }
  };
  // 渲染表结构的列
  const renderColumns = () => {
    return checkedList
      .map((labelCode) => {
        const _ = columnsMap.get(labelCode);
        const column: ProColumns = {
          title: _.labelName,
          dataIndex: _.labelCode,
          key: _.labelCode,
          width: 200,
        };
        switch (_.labelTag) {
          case 'STRING_LABEL':
            Object.assign(column, {
              renderFormItem: (schema: any) => (
                <Input
                  placeholder="请输入"
                  onChange={({ target: { value } }) => setValue(schema, value, 'struct')}
                />
              ),
            });
            break;
          case 'BOOLEAN_LABEL':
            Object.assign(column, {
              width: 'auto',
              renderFormItem: (schema: any) => (
                <Switch
                  checked={structData[schema.index][schema.dataIndex]}
                  onChange={(checked) => setValue(schema, checked, 'struct')}
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
                  onChange={(value) => setValue(schema, value, 'struct')}
                />
              ),
            });
            break;
        }
        return column;
      })
      .concat({ title: '操作', valueType: 'option', fixed: 'right', width: 140 });
  };
  // 表结构的操作栏事件处理
  const onStructAction = (row: any, _: any, type: StructAction) => {
    const i = structData.findIndex((_) => _.id === row.id);
    if (type === 'del') {
      structData.splice(i, 1);
      structKeys.splice(i, 1);
    }
    if (type === 'up') {
      [structData[i - 1], structData[i]] = [structData[i], structData[i - 1]];
      [structKeys[i - 1], structKeys[i]] = [structKeys[i], structKeys[i - 1]];
    }
    if (type === 'down') {
      [structData[i + 1], structData[i]] = [structData[i], structData[i + 1]];
      [structKeys[i + 1], structKeys[i]] = [structKeys[i], structKeys[i + 1]];
    }
    setStructData([...structData]);
    setStructKeys([...structKeys]);
  };
  // 外键表的列
  const fkColumns: ProColumns[] = [
    {
      title: '字段',
      dataIndex: 'columnNames',
      key: 'columnNames',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          mode="multiple"
          allowClear
          placeholder="请选择"
          maxTagCount="responsive"
          options={strOps}
          onChange={(value) => setValue(schema, value, 'fk')}
        />
      ),
    },
    {
      title: '参考数据库',
      dataIndex: 'referDbName',
      key: 'referDbName',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={dbs}
          onChange={(value) => setValue(schema, value, 'fk')}
        />
      ),
    },
    {
      title: '参考表',
      dataIndex: 'referTableId',
      key: 'referTableId',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema: any) => (
        <Select
          allowClear
          placeholder="请选择"
          options={tbs[schema.index]}
          onChange={(value) => setValue(schema, value, 'fk')}
        />
      ),
    },
    {
      title: '参考字段',
      dataIndex: 'referColumnNames',
      key: 'referColumnNames',
      width: 200,
      renderFormItem: (schema: any) => (
        <Select
          mode="multiple"
          allowClear
          placeholder="请选择"
          maxTagCount="responsive"
          options={str[schema.index]}
          onChange={(value) => setValue(schema, value, 'fk')}
        />
      ),
    },
    {
      title: 'ER图关系类型',
      dataIndex: 'erType',
      key: 'erType',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={EROps}
          onChange={(value) => setValue(schema, value, 'fk')}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];
  // 外键表的操作
  const onFKAction = (row: any, _: any) => {
    const i = fkData.findIndex((_) => _.id === row.id);
    fkData.splice(i, 1);
    fkKeys.splice(i, 1);
    setFkData([...fkData]);
    setFkKeys([...fkKeys]);
  };

  return (
    <Fragment>
      <TableLabels form={refs.label} initial={initial} />
      <Tabs
        className={`${styles.reset} ${styles['reset-tabs']}`}
        defaultActiveKey="struct"
        onChange={(key) => {
          if (key === 'fk') {
            // 切换到外键的时候，保存表结构字段，生成Select的ops
            const dataIndex = columns[0].labelCode;
            const ops = structData.map((_) => ({ label: _[dataIndex], value: _[dataIndex] }));
            setStrOps(ops);
          }
        }}
      >
        <TabPane
          key="struct"
          tab={[
            '表结构设计',
            <Popover
              overlayClassName={styles['reset-popover']}
              key="setting"
              trigger="click"
              placement="bottomRight"
              onVisibleChange={(v) => setIconType(v ? 'icon-shezhijihuo' : 'icon-shezhi')}
              title={
                <Checkbox
                  indeterminate={indeterminate}
                  onChange={({ target: { checked } }) => onAllCheck(checked)}
                  checked={allChecked}
                >
                  字段信息展示
                </Checkbox>
              }
              content={
                <CheckboxGroup
                  options={columns}
                  value={checkedList}
                  onChange={(checked) => onCheck(checked)}
                  style={{ display: 'flex', flexDirection: 'column' }}
                />
              }
            >
              <IconFont type={iconType} style={{ cursor: 'pointer', marginLeft: 8 }} />
            </Popover>,
          ]}
        >
          <EditableProTable
            className={styles.struct}
            rowKey="id"
            columns={renderColumns()}
            value={structData}
            bordered
            scroll={{ x: 'max-content' }}
            style={{ paddingTop: 24 }}
            recordCreatorProps={false}
            editable={{
              type: 'multiple',
              editableKeys: structKeys,
              onChange: setStructKeys,
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
          <Link className={styles['add-string']} onClick={() => addData('struct')}>
            <IconFont type="icon-tianjia" />
            添加字段
          </Link>
        </TabPane>
        <TabPane tab="关系" key="fk">
          <EditableProTable
            className={`${styles.reset} ${styles.fk}`}
            rowKey="id"
            columns={fkColumns}
            value={fkData}
            bordered
            scroll={{ x: 'max-content' }}
            style={{ padding: '24px 0' }}
            recordCreatorProps={false}
            editable={{
              type: 'multiple',
              editableKeys: fkKeys,
              onChange: setFkKeys,
              actionRender: (row, _) => [
                <Link key="del" onClick={() => onFKAction(row, _)}>
                  删除
                </Link>,
              ],
            }}
          />
          <Link className={styles['add-fk']} onClick={() => addData('fk')}>
            <IconFont type="icon-tianjia" />
            添加外键
          </Link>
          {initial && (
            <Fragment>
              <Title>关系图预览</Title>
              <TableRelation id={initial.id} />
            </Fragment>
          )}
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditTable);
