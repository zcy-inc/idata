import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { useModel } from 'umi';
import type { FormInstance } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../index.less';

import { getFolders } from '@/services/objectlabel';
import { ObjectLabel, ObjectType } from '@/types/objectlabel';
import Title from '../Title';
import EditRules from './components/EditRules';
import { rules, ObjectTypeOptions } from './constants';

export interface EditLableProps {
  initial?: ObjectLabel;
  form: FormInstance;
}

const EditLable: ForwardRefRenderFunction<unknown, EditLableProps> = ({ initial, form }, ref) => {
  const [folderOps, setFolderOps] = useState<{ label: string; value: any }[]>([]);
  const [objectType, setObjectType] = useState<ObjectType>();
  const { curNode } = useModel('objectlabel', (_) => ({
    curNode: _.curNode,
  }));

  useImperativeHandle(ref, () => ({ form }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const folders = res.data.map((_) => ({ label: _.name, value: _.id }));
        setFolderOps(folders);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      form.setFieldsValue({
        name: initial.name,
        nameEn: initial.nameEn,
        objectType: initial.objectType,
        remark: initial.remark,
        folderId: initial.folderId === 0 ? null : initial.folderId,
      });
      setObjectType(initial.objectType);
    }
  }, [initial]);

  useEffect(() => {
    let folderId = null;
    if (initial) {
      folderId = initial.folderId === 0 ? null : initial.folderId;
    } else if (curNode) {
      folderId = curNode.type === 'FOLDER' ? curNode.id : curNode.parentId;
    }
    form.setFieldsValue({ folderId });
  }, [initial, curNode]);

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
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
            placeholder="请选择"
            rules={rules}
            allowClear={false}
            options={ObjectTypeOptions}
            fieldProps={{ onChange: setObjectType }}
            disabled={!!initial}
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
      {objectType ? <EditRules initial={initial} objectType={objectType as ObjectType} /> : null}
    </Fragment>
  );
};

export default forwardRef(EditLable);
