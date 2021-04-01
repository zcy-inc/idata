import React, { useEffect } from 'react';
import { useParams, history } from 'umi';
import { Form } from 'antd';
import { editRole } from '@/services/role';
import { saveFn } from '@/utils/utils';
import useAuthSetting from '../../hooks/useAuthSetting';
import RoleConf from '../../components/RoleConf';

const Edit = () => {
  const params = useParams<{ id: string; name: string }>();
  const [form] = Form.useForm();
  const { fetchData, authSettingProps } = useAuthSetting();
  const roleId = Number(params.id);
  useEffect(() => {
    fetchData({ roleId })
  }, [roleId])
  useEffect(() => {
    form.setFieldsValue({ roleName: params.name });
  }, [params.name, form]);
  
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
    history.push('/authority/role/list');
  };
  const roleConfProps = {
    authSettingProps,
    form,
    onSave,
  };
  return <RoleConf {...roleConfProps} />;
};

export default Edit;
