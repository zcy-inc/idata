import { useRef, useState } from 'react';
import type { FC } from 'react';
import type { ProFormInstance } from '@ant-design/pro-form';
import React, { useEffect } from 'react';
import { message } from 'antd';
import ProForm, {
  ModalForm,
  ProFormText,
  ProFormSelect,
  ProFormRadio,
  ProFormList,
  ProFormDependency
} from '@ant-design/pro-form';
import { defineLabel, findDefine } from '@/services/labelController'
import type { ILabelDefines, TSubjectType } from '@/types/labelController'
const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
interface IModalProps {
  callback: () => void;
  onCancel?: () => void;
  subjectType: TSubjectType
  labelCode?: string
  visible?: boolean;
}

const Modal: FC<IModalProps> = (props) => {
  const formRef = useRef<ProFormInstance>();
  const { callback, onCancel, subjectType, visible, labelCode } = props;
  // TODO 后端硬要加 labelParamType 参数
  const [labelParamType, setLabelParamType] = useState<string | undefined>()
  useEffect(() => {
    if (labelCode) {
      findDefine(labelCode).then(({ data }) => {
        formRef.current?.setFieldsValue(data);
        setLabelParamType(data?.labelParamType)
      })
    } else {
      formRef.current?.resetFields()
      setLabelParamType(undefined)
    }
  }, [subjectType, labelCode,visible])
  return (
    <ModalForm<ILabelDefines>
      className="zcy-modal"
      {...layout}
      initialValues={{
        labelRequired: 0
      }}
      layout="horizontal"
      formRef={formRef}
      visible={visible}
      autoFocusFirstInput
      modalProps={{
        onCancel: onCancel,
        title:"新增属性",
        width:580,
        destroyOnClose: true
      }}
      onFinish={async (values) => {
        const { success } = await defineLabel({
          subjectType,
          labelCode,
          labelParamType,
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
        width="md"
        name="labelTag"
        label="属性类别"
        disabled={!!labelCode}
        options={[
          {
            value: 'STRING_LABEL',
            label: '输入框',
          },
          {
            value: 'ENUM_VALUE_LABEL',
            label: '选择器',
          },
          {
            value: 'BOOLEAN_LABEL',
            label: '布尔',
          },
        ]}
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
            return (
              <ProFormList
                name="enumValues"
                label="选型列表"
                creatorButtonProps={{
                  position: 'bottom',
                }}
              >
                <ProForm.Group size={8}>
                  <ProFormText hidden name="valueCode" label="编码" placeholder="" />
                  <ProFormText name="enumValue" label="" />
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
