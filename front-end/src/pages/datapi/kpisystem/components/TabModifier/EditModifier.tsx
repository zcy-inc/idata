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
import { getFolders } from '@/services/kpisystem';
import { rules } from '@/constants/datapi';
import { Modifier, Table as ITable } from '@/types/datapi';
import { getTableReferStr, getTableReferTbs } from '@/services/tablemanage';

export interface ViewModifierProps {
  initial?: Modifier;
}
interface TableOptions extends ITable {
  label: string;
  value: string;
}

const { TabPane } = Tabs;
const { Link } = Typography;
const { require } = rules;

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ initial }, ref) => {
  const [folderOps, setFolderOps] = useState([]);
  const [form] = Form.useForm();
  // 事实表
  const [DWDData, setDWDData] = useState<any[]>([]);
  const [DWDKeys, setDWDKeys] = useState<Key[]>([]);
  const [DWDTables, setDWDTables] = useState<TableOptions[]>([]);
  const [DWDStrings, setDWDStrings] = useState<[][]>([]);

  useImperativeHandle(ref, () => ({
    form: form.getFieldsValue(),
    DWD: DWDData,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({
          label: _.folderName,
          value: `${_.id}`,
        }));
        setFolderOps(fd);
      })
      .catch((err) => {});
    getTableReferTbs({ labelValue: 'dwd' })
      .then((res) => {
        setDWDTables(res.data);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
    }
  }, [initial]);

  // 添加一行数据
  const addData = () => {
    const id = Date.now();
    const data = { id };
    setDWDData([...DWDData, data]);
    setDWDKeys([...DWDKeys, id]);
  };

  const setValue = (schema: any, value: any) => {
    if (schema.dataIndex === 'tableName') {
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
      dataIndex: 'tableName',
      key: 'tableName',
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
      dataIndex: 'stringName',
      key: 'stringName',
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
  const ColsPreview = [
    { title: '枚举值', dataIndex: 'enumValue', key: 'enumValue' },
    { title: '父级枚举值', dataIndex: 'parentValue', key: 'parentValue' },
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
            name="英文别名"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormSelect
            name="枚举值"
            label="枚举值"
            width="sm"
            placeholder="请输入"
            rules={require}
            tooltip="若为空，请在数仓设计-新建枚举类型处新建。"
            options={[]}
          />
          <ProForm.Item>
            <Popover
              content={
                <Table columns={ColsPreview} dataSource={[]} pagination={false} size="small" />
              }
            >
              <Link>预览</Link>
            </Popover>
          </ProForm.Item>
        </ProFormGroup>
        <ProFormText name="定义" label="定义" width="md" placeholder="请输入" rules={require} />
        <ProFormTextArea name="备注" label="备注" width="md" placeholder="请输入" />
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
        <TabPane key="main" tab="事实表">
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
            <IconFont type="icon-tianjia" />
            添加字段
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(ViewModifier);
