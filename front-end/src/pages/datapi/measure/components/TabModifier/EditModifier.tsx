import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form, Popover, Table, Tabs, Select, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction, Key } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../components/Title';
import { getFolders } from '@/services/measure';
import { rules } from '@/constants/datapi';
import { FlatTreeNode, Modifier, EnumName, EnumValue, LabelAttribute } from '@/types/datapi';
import {
  getEnumNames,
  getEnumValues,
  getTableReferStr,
  getTableReferTbs,
} from '@/services/tablemanage';

export interface ViewModifierProps {
  initial?: Modifier;
}
interface TableOptions {
  label: string;
  value: string | number;
}
interface DWDDataList {
  id: number;
  tableId?: number;
  columnName?: string;
}

const { TabPane } = Tabs;
const { Link } = Typography;
const { require } = rules;

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ initial }, ref) => {
  const [folderOps, setFolderOps] = useState([]);
  const [enumOps, setEnumOps] = useState([]);
  const [enumCols, setEnumCols] = useState([]);
  const [enumVals, setEnumVals] = useState([]);
  const [form] = Form.useForm();
  // 事实表
  const [DWDData, setDWDData] = useState<DWDDataList[]>([]);
  const [DWDKeys, setDWDKeys] = useState<Key[]>([]);
  const [DWDTables, setDWDTables] = useState<TableOptions[]>([]);
  const [DWDStrings, setDWDStrings] = useState<[][]>([]);

  useImperativeHandle(ref, () => ({
    form: form,
    DWD: DWDData,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: FlatTreeNode) => ({
          label: _.folderName,
          value: _.id,
        }));
        setFolderOps(fd);
      })
      .catch((err) => {});
    getEnumNames()
      .then((res) => {
        const ops = res.data.map((e: EnumName) => ({
          label: e.enumName,
          value: e.enumCode,
        }));
        setEnumOps(ops);
      })
      .catch((err) => {});
    getTableReferTbs({ labelValue: 'dwd' })
      .then((res) => {
        const tableOptions = res.data.map((table) => ({ label: table.tableName, value: table.id }));
        setDWDTables(tableOptions);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      // form initial
      const values = {
        labelName: initial.labelName,
        folderId: initial.folderId,
      };
      initial.labelAttributes.forEach((labelAttribute) => {
        values[labelAttribute.attributeKey] = labelAttribute.attributeValue;
        if (labelAttribute.attributeKey === 'modifierEnum') {
          onEnumChange(labelAttribute.attributeValue);
        }
      });
      form.setFieldsValue(values);
      // DWD initial
      initial.measureLabels.forEach((measureLabel) => {
        DWDKeys.push(measureLabel.id);
        DWDData.push({
          id: measureLabel.id,
          tableId: measureLabel.tableId,
          columnName: measureLabel.columnName,
        });
      });
      setDWDData([...DWDData]);
      setDWDKeys([...DWDKeys]);
    }
  }, [initial]);

  const ColsPreview = [
    { title: '枚举值', dataIndex: ['enumValue', 'value'], key: 'enumValue' },
    { title: '父级枚举值', dataIndex: 'parentValue', key: 'parentValue' },
  ];

  const onEnumChange = (enumCode: string) => {
    getEnumValues({ enumCode })
      .then((res) => {
        const enumValues = res.data;
        const enumAttributes = enumValues[0].enumAttributes;
        // 格式化枚举参数生成的列
        const exCols = enumAttributes.map((enumAttribute: LabelAttribute) => ({
          key: enumAttribute.attributeKey,
          dataIndex: enumAttribute.attributeKey,
          title: enumAttribute.attributeKey,
        }));
        // 格式化dataSource
        const dt = enumValues.map((enumValue: EnumValue) => {
          const tmp = {
            id: enumValue.id,
            parentValue: enumValue.parentValue || '-',
            enumValue: { value: enumValue.enumValue, code: enumValue.valueCode },
          };
          enumValue.enumAttributes.forEach(
            (enumAttribute: LabelAttribute) =>
              (tmp[enumAttribute.attributeKey] = transformValue(enumAttribute)),
          );
          return tmp;
        });

        setEnumCols(ColsPreview.concat(exCols));
        setEnumVals(dt);
      })
      .catch((err) => {});
  };

  const transformValue = (enumAttribute: LabelAttribute) => {
    let value = enumAttribute.enumValue || enumAttribute.attributeValue || '-';
    if (enumAttribute.attributeType === 'BOOLEAN') {
      value = value === 'true' ? '是' : '否';
    }
    return value;
  };

  // 添加一行数据
  const addData = () => {
    const id = Date.now();
    const data = { id };
    setDWDData([...DWDData, data]);
    setDWDKeys([...DWDKeys, id]);
  };

  const setValue = (schema: any, value: any) => {
    if (schema.dataIndex === 'tableId') {
      getTableReferStr({ tableId: value })
        .then((res) => {
          const strs = res.data.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          DWDStrings[schema.index] = strs;
          setDWDStrings([...DWDStrings]);
        })
        .catch((err) => {});
    }
    DWDData[schema.index][schema.dataIndex] = value;
    setDWDData([...DWDData]);
  };

  // 操作栏行为
  const onAction = (row: any, _: any) => {
    const i = DWDData.findIndex((_) => _.id === row.id);
    DWDData.splice(i, 1);
    DWDKeys.splice(i, 1);
    setDWDData([...DWDData]);
    setDWDKeys([...DWDKeys]);
  };

  const Cols: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'tableId',
      key: 'tableId',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DWDTables}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: '关联表字段',
      dataIndex: 'columnName',
      key: 'columnName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DWDStrings[schema.index as number]}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        form={form}
        submitter={false}
      >
        <ProFormGroup>
          <ProFormText
            name="labelName"
            label="修饰词名称"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormText
            name="enName"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormSelect
            name="modifierEnum"
            label="枚举值"
            width="sm"
            placeholder="请选择"
            rules={require}
            tooltip="若为空，请在数仓设计-新建枚举类型处新建。"
            options={enumOps}
            fieldProps={{ onChange: onEnumChange }}
          />
          <ProForm.Item>
            <Popover
              content={() => (
                <Table
                  rowKey="id"
                  columns={enumCols}
                  dataSource={enumVals}
                  pagination={false}
                  size="small"
                  scroll={{ x: 'max-content' }}
                  style={{ minWidth: 120 }}
                />
              )}
            >
              <Link>预览</Link>
            </Popover>
          </ProForm.Item>
        </ProFormGroup>
        <ProFormText
          name="modifierDefine"
          label="定义"
          width="md"
          placeholder="请输入"
          rules={require}
        />
        <ProFormTextArea name="comment" label="备注" width="md" placeholder="请输入" />
        <ProFormSelect
          name="folderId"
          label="位置"
          width="md"
          placeholder="根目录"
          options={folderOps}
        />
      </ProForm>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="DWD" tab="事实表">
          <EditableProTable
            rowKey="id"
            columns={Cols}
            value={DWDData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: DWDKeys,
              onChange: setDWDKeys,
              actionRender: (row, _) => [<Link onClick={() => onAction(row, _)}>删除</Link>],
            }}
          />
          <Link onClick={addData} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
            添加字段
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(ViewModifier);
