import React, { useEffect, FC, useImperativeHandle } from 'react';
import {  Form, Input, Select } from 'antd';
import { useRequest } from 'ahooks';
import _ from 'lodash';
import { DIJobBasicInfo } from '@/types/datadev';
import { getEnumValues, getDIJobTypes, getDISyncMode } from '@/services/datadev';
import { DIFolderFormItem } from '../../../../../components/FolderFormItem';

interface DrawerBasicProps {
  data?: DIJobBasicInfo;
}

const { Item } = Form;
const { TextArea } = Input;
const widthL = 400;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerBasic: FC<DrawerBasicProps> = ({data }, ref) => {
  useImperativeHandle(ref, () => ({
    onSave
  }))

  const [form] = Form.useForm();
  const { data: jobTypeOptions } = useRequest(getDIJobTypes);
  const { data: syncModeOptions = [], run: getSyncModeOptions } = useRequest(getDISyncMode, {
    manual: true,
  });
  const { data: layersRes } = useRequest(() => getEnumValues({ enumCode: 'dwLayerEnum:ENUM' }));
  const layerOption =
    layersRes?.data.map(({ enumValue, valueCode }) => ({
      label: enumValue,
      value: valueCode,
    })) || [];

  useEffect(() => {
    (async () => {
      if (data) {
        await getSyncModeOptions({ jobType: data.jobType });
        form.setFieldsValue(data);
      }
    })();
  }, [data]);

  const onSave = () => {
    const values = form.getFieldsValue();
    return {
      ...data,
      ...values,
    };
  };

  return (
    <Form form={form} layout="horizontal" colon={false}>
      <Item name="name" label="作业名称" rules={ruleSelc}>
        <Input size="large" style={{ width: widthL }} placeholder="请选择" />
      </Item>
      <Item name="id" label="ID" rules={ruleText}>
        <Input size="large" style={{ width: widthL }} placeholder="请选择" disabled />
      </Item>
      <Item name="dwLayerCode" label="数仓分层" rules={ruleSelc}>
        <Select
          size="large"
          style={{ width: widthL }}
          placeholder="请选择"
          options={layerOption}
          disabled
        />
      </Item>
      <Item name="jobType" label="作业类型" rules={ruleText}>
        <Select
          size="large"
          style={{ width: widthL }}
          placeholder="请输入"
          options={jobTypeOptions}
          disabled
        />
      </Item>
      <Item name="syncMode" label="同步类型" rules={ruleText}>
        <Select
          size="large"
          style={{ width: widthL }}
          placeholder="请选择"
          options={syncModeOptions}
          disabled
        />
      </Item>
      <Item name="creator" label="所属人">
        <Input size="large" style={{ width: widthL }} placeholder="-" disabled />
      </Item>
      <DIFolderFormItem style={{ width: widthL }} />
      <Item name="remark" label="备注">
        <TextArea style={{ width: widthL }} placeholder="请输入" />
      </Item>
    </Form>
  );
};

export default DrawerBasic;
