import type {FC} from 'react';
import React from 'react';
import { Button, message } from 'antd';
import ProForm, {
  ModalForm,
  ProFormText,
  ProFormSelect,
  ProFormRadio,
  ProFormList,
  ProFormDependency
} from '@ant-design/pro-form';
import { PlusOutlined } from '@ant-design/icons';
import { defineLabel } from '@/services/labelController'
import type { ILabelDefines,TSubjectType } from '@/types/labelController'
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
interface IModalProps {
  callback: () => void;
  subjectType: TSubjectType
}
const Modal: FC<IModalProps>=  (props) => {
  const {callback,subjectType} = props;
  return (
    <ModalForm<ILabelDefines>
      {...layout}
      initialValues={{
        labelRequired: 0,
      }}
      trigger={
        <Button type="primary">
          <PlusOutlined />
          新建属性
        </Button>
      }
      autoFocusFirstInput
      onFinish={async (values) => {
        const { success } = await defineLabel({
          subjectType,
          ...values
        });
        if (success) {
          message.success('提交成功');
          callback()
        }
        return success;
      }}
    >
      <ProFormText
        width="md"
        name="labelName"
        label="属性名称"
        placeholder="请输入名称"
      />
      <ProFormSelect
        options={[
          {
            value: 'ENUM_VALUE_LABEL',
            label: '选择器',
          },
          {
            value: 'STRING_LABEL',
            label: '输入框',
          },
          {
            value: 'BOOLEAN_LABEL',
            label: '布尔',
          },
        ]}
        width="md"
        name="labelTag"
        label="合同约定生效方式"
      />
      <ProFormRadio.Group
        name="labelRequired"
        label="是否必填"
        options={[
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          }
        ]}
      />
      <ProFormDependency name={['labelTag']}>
        {({ labelTag }) => {
          if (labelTag === "ENUM_VALUE_LABEL") {
            return (<ProFormList
              name="labelEnumValues"
              label="选型列表"
              creatorButtonProps={{
                position: 'bottom',
              }}
            >
              <ProForm.Group size={8}>
                <ProFormText name="enumValue" label="编码" />
                <ProFormText name="valueCode" label="名称" />
              </ProForm.Group>
            </ProFormList>)
          }
          return null;
        }
        }
      </ProFormDependency>
    </ModalForm>
  );
};
export default Modal
