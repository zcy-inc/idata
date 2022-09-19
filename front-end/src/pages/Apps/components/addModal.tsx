import React, { FC, useEffect, useRef, useState } from 'react';
import {
  ModalForm,
  ProFormText,
  ProFormSelect,
  ProFormTextArea,
} from '@ant-design/pro-form';
import type { ProFormInstance } from '@ant-design/pro-form';
import { getFeaturesByCodes, IFeature, IApp } from '@/services/apps';

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};

interface IModalProps {
  onSubmit: any;
  onCancel: any;
  visible?: boolean;
  app: IApp | undefined;
  isPreview: boolean;
}

export const AddModal: FC<IModalProps> = ({ visible, onCancel, onSubmit, app, isPreview}) => {
  const formRef = useRef<ProFormInstance>();
  const [featuresOptions, setFeaturesOptions] = useState<Array<IFeature>>([]);

  useEffect(() => {
    getFeaturesByCodesData();
  }, []);

  const getFeaturesByCodesData = async () => {
    const { success, data } = await getFeaturesByCodes({
      featureCodes: 'F_MENU_DATAPI,F_MENU_XIAOCAI_BI',
    });
    if (success) {
      setFeaturesOptions(data);
    }
  }

  const onFinish = async (values: any) => {
    if (isPreview) {
      onCancel();
      return;
    }
    values && onSubmit(values, !!app, app && app.id);
    onCancel();
  }

  return (
    <ModalForm
      className="zcy-modal"
      {...layout}
      initialValues={ app && {
        ...app,
        appFeatures: app.appFeatures && app.appFeatures.map(f => f.featureCode),
      }}
      formRef={formRef}
      layout="horizontal"
      visible={visible}
      autoFocusFirstInput
      modalProps={{
        onCancel: onCancel,
        title: isPreview ? '查看应用' : !!app ? '编辑应用' : "新增应用",
        width:580,
        destroyOnClose: true
      }}
      onFinish={onFinish}
    >
      <ProFormText
        width="md"
        name="appName"
        label="应用名称"
        placeholder="请输入"
        rules={[{ required: true, message: '请输入应用名称' }]}
        disabled={isPreview}
      />
      <ProFormSelect
        width="md"
        mode="multiple"
        name="appFeatures"
        label="接入模块"
        placeholder="请选择"
        rules={[{ required: true, message: '请选择接入模块' }]}
        options={featuresOptions.map((p) => (
          {
            label: p.featureName,
            value: p.featureCode,
          }
        ))}
        disabled={isPreview}
      />
      <ProFormTextArea
        width="md"
        name="description"
        label="应用描述"
        placeholder="请输入"
        disabled={isPreview}
      />
      {
        !!app && (
          <>
            <ProFormText
              width="md"
              name="editor"
              label="最新编辑人"
              disabled={true}
            />
            <ProFormText
              width="md"
              name="appKey"
              label="应用编码"
              disabled={true}
            />
            <ProFormText
              width="md"
              name="appSecret"
              label="应用密钥"
              disabled={true}
            />
          </>
        )
      }
    </ModalForm>
  );
};