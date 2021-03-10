import React, { useEffect } from 'react';
import { useParams } from 'umi';
import { Form } from 'antd';
import { editRole } from '@/services/role';
import { saveFn } from '@/utils/utils';
import useAuthSetting from '../../hooks/useAuthSetting';
import RoleConf from '../../components/RoleConf';

const Edit = () => {
  const params = useParams<{ id: string; name: string }>();
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldsValue({ roleName: params.name });
  }, [params.name, form]);
  const roleId = Number(params.id);
  const authSettingProps = useAuthSetting({ roleId });

  const onSave = async () => {
    const values = await form.validateFields();
    const { folderTree, origFeatureTree } = authSettingProps;
    await saveFn(() =>
      editRole({
        folderTree,
        featureTree: origFeatureTree,
        roleName: values.roleName,
        id: Number(params.id),
      }),
    );
  };
  const roleConfProps = {
    authSettingProps,
    form,
    onSave,
  };
  return <RoleConf {...roleConfProps} />;
};

export default Edit;
