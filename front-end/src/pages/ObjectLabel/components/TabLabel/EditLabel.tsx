import React, { Fragment, useEffect, useState } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import { getFolders } from '@/services/objectlabel';
import Title from '../Title';
import EditRules from './components/EditRules';
import { rules, ObjectType } from './constants';
import { ObjectLabel } from '@/types/objectlabel';

export interface EditLableProps {
  initial?: ObjectLabel;
}

const EditLable: FC<EditLableProps> = ({ initial }) => {
  const [folderOps, setFolderOps] = useState([]);
  const [form] = Form.useForm();

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: `${_.id}` }));
        setFolderOps(fd);
      })
      .catch((err) => {});
  }, []);

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
          <ProFormText name="name" label="标签名称" width="sm" placeholder="请输入" rules={rules} />
          <ProFormText
            name="nameEn"
            label="标签英文名"
            width="sm"
            placeholder="请输入"
            rules={rules}
          />
          <ProFormSelect
            name="objectType"
            label="标签主体"
            width="sm"
            placeholder="请输入"
            rules={rules}
            options={ObjectType}
          />
        </ProFormGroup>
        <ProFormTextArea name="remark" label="备注" width="md" placeholder="请输入" />
        <ProFormSelect
          name="folderId"
          label="位置"
          width="md"
          placeholder="根目录"
          options={folderOps}
        />
      </ProForm>
      <Title>标签规则</Title>
      <EditRules initial={initial} />
    </Fragment>
  );
};

export default EditLable;
