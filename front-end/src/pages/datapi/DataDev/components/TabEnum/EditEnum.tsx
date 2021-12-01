import React, { useEffect, useState } from 'react';
import ProForm, { ProFormText, ProFormSelect } from '@ant-design/pro-form';
import type { FC } from 'react';
import { FormInstance } from 'antd';
import styles from './index.less';

import { getFolders } from '@/services/datadev';
import { rules } from '@/constants/datapi';
import { Enum } from '@/types/datapi';
import EnumTable from './components/EnumTable';
import { Folder } from '@/types/datadev';
import { FolderBelong } from '@/constants/datadev';

export interface EditEnumProps {
  form: FormInstance;
  data?: Enum;
}

const { require } = rules;

const EditEnum: FC<EditEnumProps> = ({ form, data }) => {
  const [folders, setFolders] = useState<Folder[]>([]);

  useEffect(() => {
    getFolders({ belong: FolderBelong.DESIGNENUM })
      .then((res) => setFolders(res.data))
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (data) {
      form.setFieldsValue({
        enumName: data.enumName,
        folderId: data.folderId,
      });
    }
  }, [data]);

  return (
    <ProForm
      className={styles.form}
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
        rules={require}
        initialValue={data?.enumName}
      />
      <ProForm.Item
        // label={<span style={{ fontWeight: 'bold', color: 'rgba(0, 0, 0, 0.85)' }}>枚举值</span>}
        label="枚举值"
        name="enumValues"
        trigger="onChange"
        rules={require}
        labelCol={{ span: 24 }}
        wrapperCol={{ span: 24 }} // 不知道为什么出现的样式问题，所以加上的
        style={{ display: 'flex', flexDirection: 'column' }} // 同上
      >
        {/* 这儿使用form提供的initialValue时只会在第一次的时候传递, 之后由自定义组件自身的value接管而失效 */}
        {/* 虽然确实卸载了这个组件, 但是不知道为什么value值仍然生效 */}
        {/* initialValue和value、onChange一样是内部已存在的属性名, 受控于内部逻辑 */}
        {/* 所以这里使用额外的属性initial来传递view模式下的初始值 */}
        <EnumTable initial={data} />
      </ProForm.Item>
      <ProFormSelect
        name="folderId"
        label="位置"
        width="md"
        rules={require}
        options={folders.map((_) => ({ label: _.name, value: _.id }))}
        fieldProps={{
          showSearch: true,
          filterOption: (input: string, option: any) => option.label.indexOf(input) >= 0,
        }}
      />
    </ProForm>
  );
};

export default EditEnum;
