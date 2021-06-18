import React, { useEffect, useState } from 'react';
import ProForm, { ProFormText, ProFormSelect } from '@ant-design/pro-form';
import { useModel } from 'umi';
import type { FormInstance } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import { getFolders } from '@/services/tablemanage';
import { rules } from '@/constants/tablemanage';
import { Enum, FLatTreeNode } from '@/types/tablemanage';
import EnumTable from './components/EnumTable';

export interface EditEnumProps {
  form: FormInstance;
  data?: Enum;
}

const { require } = rules;

const EditEnum: FC<EditEnumProps> = ({ form, data }) => {
  const [folders, setFolders] = useState([]);
  const { curFolder } = useModel('tabalmanage', (ret) => ({
    curFolder: ret.curFolder,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: FLatTreeNode) => ({
          label: _.folderName,
          value: `${_.id}`,
        }));
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
        className={styles.test}
        label="枚举类型名称"
        name="enumName"
        width="md"
        placeholder="请输入"
        rules={require}
        initialValue={data?.enumName}
        style={{ color: 'red' }}
      />
      <ProForm.Item
        className={`${styles['label']} ${styles['bold-label']}`}
        label="枚举值"
        name="enumValues"
        trigger="onChange"
        rules={require}
        labelCol={{ span: 24 }}
      >
        {/* 这儿使用form提供的initialValue时只会在第一次的时候传递, 之后由自定义组件自身的value接管而失效 */}
        {/* 虽然确实卸载了这个组件, 但是不知道为什么value值仍然生效 */}
        {/* initialValue和value、onChange一样是内部已存在的属性名, 受控于内部逻辑 */}
        {/* 所以这里使用额外的属性initial来传递view模式下的初始值 */}
        <EnumTable initial={data} />
      </ProForm.Item>
      <ProFormSelect
        className={styles['label']}
        name="folderId"
        label="位置"
        width="md"
        placeholder="根目录"
        options={folders}
        initialValue={
          // data? 判断新建和编辑
          // curFolder? 判断加号还是右键新建
          // curFolder.type === 'FOLDER' 判断右键点在文件夹或文件上
          data
            ? data.folderId?.toString() || null
            : curFolder
            ? curFolder.type === 'FOLDER'
              ? curFolder?.folderId
              : curFolder?.parentId
            : null
        }
      />
    </ProForm>
  );
};

export default EditEnum;