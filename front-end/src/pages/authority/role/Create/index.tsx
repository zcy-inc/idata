import React from 'react';
import { Form } from 'antd';
import { createRole } from '@/services/role';
import { saveFn } from '@/utils/utils';
import useAuthSetting from '../../hooks/useAuthSetting';
import RoleConf from '../../components/RoleConf';

const Create = () => {
  const [form] = Form.useForm();
  const authSettingProps = useAuthSetting();
  const onSave = async () => {
    const values = await form.validateFields();
    const { folderTree, origFeatureTree } = authSettingProps;
    await saveFn(() =>
      createRole({ folderTree, featureTree: origFeatureTree, roleName: values.roleName }),
    );
  };
  const roleConfProps = { authSettingProps, form, onSave };
  return <RoleConf {...roleConfProps} />;
};

export default Create;
