import React, { useEffect, useState } from 'react';
import ProForm, { ProFormText, ProFormSelect } from '@ant-design/pro-form';
import type { FormInstance } from 'antd';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import EnumTable from './EnumTable';
import { getFolders } from '@/services/tablemanage';

export interface EditEnumProps {
  form: FormInstance;
  initialValue?: { data: any; columns: any[]; dataSource: any[] };
}

const rules = [{ required: true, message: '必填' }];

const EditEnum: FC<EditEnumProps> = ({ form, initialValue }) => {
  const [folders, setFolders] = useState<any[]>([]);

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: _.id }));
        setFolders(fd);
      })
      .catch((err) => {});
  }, []);

  return (
    <ProForm
      className={styles.reset}
      layout="horizontal"
      colon={false}
      form={form}
      submitter={false}
    >
      <ProFormText
        label="枚举类型名称"
        name="enumName"
        width="md"
        placeholder="请输入"
        rules={rules}
        initialValue={initialValue?.data.enumName}
      />
      <ProForm.Item
        label="枚举值"
        name="enumValues"
        trigger="onChange"
        rules={rules}
        labelCol={{ span: 24 }}
      >
        {/* 这儿使用form提供的initialValue时只会在第一次的时候正确传递, 之后由自定义组件自身的value接管而失效 */}
        {/* 虽然确实卸载了这个组件, 但是不知道为什么value值仍然生效 */}
        {/* initialValue和value、onChange一样是内部已存在的属性名, 受控于内部逻辑 */}
        {/* 所以这里使用额外的属性initial来传递view模式下的初始值 */}
        <EnumTable initial={initialValue} />
      </ProForm.Item>
      <ProFormSelect
        name="folderId"
        label="位置"
        width="md"
        placeholder="根目录"
        options={folders}
        initialValue={initialValue?.data.folderId}
      />
    </ProForm>
  );
};

export default EditEnum;
