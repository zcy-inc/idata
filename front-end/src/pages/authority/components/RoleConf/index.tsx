import React from 'react';
import { history } from 'umi';
import { H4 } from '@/components';
import type { FormInstance, ButtonProps } from 'antd';
import { Button, Form, Input } from 'antd';
import type { AuthSettingProps } from '../../components/AuthSetting';
import AuthSetting from '../../components/AuthSetting';

const RoleConf: React.FC<{
  authSettingProps: Partial<AuthSettingProps>;
  form: FormInstance<any>;
  saveBtnProps: Partial<ButtonProps>;
}> = ({ authSettingProps, form, saveBtnProps }) => {
  return (
    <>
      <div style={{ textAlign: "right" }}>
        <Button onClick={history.goBack}>取消</Button>
        <Button type="primary" style={{ marginLeft: 16 }} {...saveBtnProps}>
          保存
        </Button>
      </div>
      <H4>基本信息</H4>
      <Form form={form}>
        <Form.Item label="角色名称" name="roleName" rules={[{ required: true }]}>
          <Input placeholder="请输入名称" style={{ width: 300 }} />
        </Form.Item>
      </Form>
      <H4>权限信息</H4>
      <AuthSetting style={{ height: 680, marginBottom: 24 }} {...authSettingProps} />
    </>
  );
};

export default RoleConf;
