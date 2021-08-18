import React, { Fragment, useEffect, useImperativeHandle, useState, forwardRef } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form, Tabs, Radio, Select, Typography, message } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction, Key } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../components/Title';
import { getFolders } from '@/services/measure';
import { rules, BooleanOptions } from '@/constants/datapi';
import { Dimension } from '@/types/datapi';
import { getTableReferStr, getTableReferTbs } from '@/services/tablemanage';

interface EditDimensionProps {
  initial?: Dimension;
}
interface TableOptions {
  label: string;
  value: string | number;
}
interface DIMDataList {
  id: number;
  tableId?: number;
  columnName?: string;
  degradeDim?: boolean;
}
interface DWDDataList {
  id: number;
  tableId?: number;
  columnName?: string;
}

const { TabPane } = Tabs;
const { Link } = Typography;
const { require } = rules;

const EditDimension: ForwardRefRenderFunction<unknown, EditDimensionProps> = ({ initial }, ref) => {
  const [folderOps, setFolderOps] = useState([]);
  const [form] = Form.useForm();
  // 主表
  const [DIMData, setDIMData] = useState<DIMDataList[]>([]);
  const [DIMKeys, setDIMKeys] = useState<Key[]>([]);
  const [DIMTables, setDIMTables] = useState<TableOptions[]>([]);
  const [DIMStrings, setDIMStrings] = useState([]);
  // 事实表
  const [DWDData, setDWDData] = useState<DWDDataList[]>([]);
  const [DWDKeys, setDWDKeys] = useState<Key[]>([]);
  const [DWDTables, setDWDTables] = useState<TableOptions[]>([]);
  const [DWDStrings, setDWDStrings] = useState<[][]>([]);

  useImperativeHandle(ref, () => ({
    form: form,
    DIM: DIMData,
    DWD: DWDData,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data?.map((_: any) => ({ label: _.folderName, value: _.id }));
        setFolderOps(fd);
      })
      .catch((err) => {});
    getTableReferTbs({ labelValue: 'dim' })
      .then((res) => {
        const tableOptions = res.data?.map((table) => ({ label: table.tableName, value: table.id }));
        setDIMTables(tableOptions);
      })
      .catch((err) => {});
    getTableReferTbs({ labelValue: 'dwd' })
      .then((res) => {
        const tableOptions = res.data?.map((table) => ({ label: table.tableName, value: table.id }));
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
      initial.labelAttributes?.forEach((labelAttribute) => {
        values[labelAttribute.attributeKey] = labelAttribute.attributeValue;
      });
      form.setFieldsValue(values);
      // DIM && DWD initial
      initial.measureLabels?.forEach((measureLabel) => {
        if (measureLabel.labelParamValue === 'true') {
          DIMKeys.push(measureLabel.id);
          DIMData.push({
            id: measureLabel.id,
            tableId: measureLabel.tableId,
            columnName: measureLabel.columnName,
            degradeDim: initial.specialAttribute.degradeDim,
          });
        } else {
          DWDKeys.push(measureLabel.id);
          DWDData.push({
            id: measureLabel.id,
            tableId: measureLabel.tableId,
            columnName: measureLabel.columnName,
          });
        }
      });
      setDIMData([...DIMData]);
      setDIMKeys([...DIMKeys]);
      setDWDData([...DWDData]);
      setDWDKeys([...DWDKeys]);
    }
  }, [initial]);

  // 添加一行数据
  const addData = (type: 'DIM' | 'DWD') => {
    const id = Date.now();
    const data = { id };

    if (type === 'DIM') {
      if (DIMData.length > 0) {
        message.info('主表只能有一张');
        return;
      } else {
        setDIMData([...DIMData, data]);
        setDIMKeys([...DIMKeys, id]);
      }
    }
    if (type === 'DWD') {
      setDWDData([...DWDData, data]);
      setDWDKeys([...DWDKeys, id]);
    }
  };

  const setValue = (schema: any, value: any, type: 'DIM' | 'DWD') => {
    if (type === 'DIM') {
      if (schema.dataIndex === 'tableId') {
        getTableReferStr({ tableId: value })
          .then((res) => {
            const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
            setDIMStrings(strs);
          })
          .catch((err) => {});
      }
      DIMData[schema.index][schema.dataIndex] = value;
      setDIMData([...DIMData]);
    }
    if (type === 'DWD') {
      if (schema.dataIndex === 'tableId') {
        getTableReferStr({ tableId: value })
          .then((res) => {
            const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
            DWDStrings[schema.index] = strs;
            setDWDStrings([...DWDStrings]);
          })
          .catch((err) => {});
      }
      DWDData[schema.index][schema.dataIndex] = value;
      setDWDData([...DWDData]);
    }
  };
  // 操作栏行为
  const onAction = (row: any, _: any, type: 'DIM' | 'DWD') => {
    if (type === 'DIM') {
      const i = DIMData.findIndex((_) => _.id === row.id);
      DIMData.splice(i, 1);
      DIMKeys.splice(i, 1);
      setDIMData([...DIMData]);
      setDIMKeys([...DIMKeys]);
    }
    if (type === 'DWD') {
      const i = DWDData.findIndex((_) => _.id === row.id);
      DWDData.splice(i, 1);
      DWDKeys.splice(i, 1);
      setDWDData([...DWDData]);
      setDWDKeys([...DWDKeys]);
    }
  };

  const ColsDIM: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'tableId',
      key: 'tableId',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DIMTables}
          onChange={(value) => setValue(schema, value, 'DIM')}
        />
      ),
    },
    {
      title: '字段名',
      dataIndex: 'columnName',
      key: 'columnName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DIMStrings}
          onChange={(value) => setValue(schema, value, 'DIM')}
        />
      ),
    },
    {
      title: '退化维',
      dataIndex: 'degradeDim',
      key: 'degradeDim',
      renderFormItem: (schema) => (
        <Radio.Group
          options={BooleanOptions}
          onChange={({ target: { checked } }) => setValue(schema, checked, 'DIM')}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];
  const ColsDWD: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'tableId',
      key: 'tableName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DWDTables}
          onChange={(value) => setValue(schema, value, 'DWD')}
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
          onChange={(value) => setValue(schema, value, 'DWD')}
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
            label="维度名称"
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
          <ProFormText
            name="dimensionId"
            label="维度ID"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
        </ProFormGroup>
        <ProFormText
          name="dimensionDefine"
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
        <TabPane key="DIM" tab="主表">
          <EditableProTable
            rowKey="id"
            columns={ColsDIM}
            value={DIMData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: DIMKeys,
              onChange: setDIMKeys,
              actionRender: (row, _) => [
                <Link key="del" onClick={() => onAction(row, _, 'DIM')}>
                  删除
                </Link>,
              ],
            }}
          />
          <Link onClick={() => addData('DIM')} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
            添加主表
          </Link>
        </TabPane>
        <TabPane key="DWD" tab="事实表">
          <EditableProTable
            rowKey="id"
            columns={ColsDWD}
            value={DWDData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: DWDKeys,
              onChange: setDWDKeys,
              actionRender: (row, _) => [<Link onClick={() => onAction(row, _, 'DWD')}>删除</Link>],
            }}
          />
          <Link onClick={() => addData('DWD')} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
            添加事实表
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditDimension);
