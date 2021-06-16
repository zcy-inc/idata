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

import Title from '../../../components/Title';
import EditAtomic from './components/EditAtomic';
import EditDerive from './components/EditDerive';
import EditComplex from './components/EditComplex';
import { getFolders } from '@/services/kpisystem';
import { rulesText, rulesSelect, MetricTypeOps } from './constants';

export interface ViewModifierProps {}

const ViewModifier: FC<ViewModifierProps> = ({}) => {
  const [folderOps, setFolderOps] = useState([]);
  const [formLabel] = Form.useForm();

  const [metricType, setMetricType] = useState('atomic');

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: `${_.id}` }));
        setFolderOps(fd);
      })
      .catch((err) => {});
  }, []);

  const EditMap = {
    atomic: <EditAtomic />,
    derive: <EditDerive />,
    complex: <EditComplex />,
  };

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        form={formLabel}
        submitter={false}
      >
        <ProFormGroup>
          <ProFormSelect
            name="type"
            label="指标类型"
            width="sm"
            placeholder="请选择"
            rules={rulesSelect}
            options={MetricTypeOps}
            initialValue="atomic"
            fieldProps={{ onChange: setMetricType }}
          />
          <ProFormText
            name="name"
            label="指标名称"
            width="sm"
            placeholder="请输入"
            rules={rulesText}
          />
          <ProFormText name="code" label="Code" width="sm" placeholder="请输入" rules={rulesText} />
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText
            name="e"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={rulesText}
          />
          <ProFormSelect
            name="folderId"
            label="业务过程"
            width="sm"
            placeholder="请选择"
            rules={rulesText}
            options={folderOps}
          />
          <ProFormSelect
            name="folderId"
            label="位置"
            width="sm"
            placeholder="根目录"
            options={folderOps}
          />
        </ProFormGroup>
        <ProFormText name="define" label="定义" width="md" placeholder="请输入" rules={rulesText} />
        <ProFormTextArea name="remark" label="备注" width="md" placeholder="请输入" />
      </ProForm>
      {EditMap[metricType]}
    </Fragment>
  );
};

export default ViewModifier;
