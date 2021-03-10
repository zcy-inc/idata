import React, { Fragment } from 'react';
import { history } from 'umi';
import { H4, PageContainer } from '@/components';
import { Button, Form, Input, FormInstance } from 'antd';
import AuthSetting, { AuthSettingProps } from '../../components/AuthSetting';

const RoleConf: React.FC<{
  authSettingProps: Partial<AuthSettingProps>;
  form: FormInstance<any>;
  onSave: () => void;
}> = ({ authSettingProps, form, onSave }) => {
  return (
    <PageContainer
      extra={
        <Fragment>
          <Button onClick={history.goBack}>取消</Button>
          <Button type="primary" onClick={onSave} style={{ marginLeft: 16 }}>
            保存
          </Button>
        </Fragment>
      }
    >
      <H4>基本信息</H4>
      <Form form={form}>
        <Form.Item label="角色名称" name="roleName" rules={[{ required: true }]}>
          <Input placeholder="请输入名称" style={{ width: 300 }} />
        </Form.Item>
      </Form>
      <H4>权限信息</H4>
      <AuthSetting style={{ height: 680, marginBottom: 24 }} {...authSettingProps} />
    </PageContainer>
  );
};

export default RoleConf;
