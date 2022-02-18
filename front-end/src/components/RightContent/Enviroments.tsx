import React, { useEffect, useState } from 'react';
import { Form, Select } from 'antd';
import type { FC } from 'react';
import { getEnvironments } from '@/services/global';
import { GlobalEnvironment } from '@/types/common';

interface EnviromentsProps {}

const Enviroments: FC<EnviromentsProps> = ({}) => {
  const [envs, setEnvs] = useState<GlobalEnvironment[]>([]);
  const [form] = Form.useForm();

  useEffect(() => {
    init();
    getEnvironmentsWrapped();
  }, []);

  const init = () => {
    const workspaceString = sessionStorage.getItem('workspace');
    try {
      if (workspaceString) {
        const workspaceJson = JSON.parse(workspaceString);
        const env = workspaceJson.urlPath || 'zcy';
        form.setFieldsValue({ env });
      } else {
        form.setFieldsValue({ env: 'zcy' });
      }
    } catch (e) {
      form.setFieldsValue({ env: 'zcy' });
    }
  };

  const getEnvironmentsWrapped = () => {
    getEnvironments()
      .then((res) => {
        setEnvs(res.data);
      })
      .catch(() => {});
  };

  const onEnvChange = (env: string) => {
    const workspaceJson: GlobalEnvironment = envs.find((item) => item.urlPath === env) || {
      id: -1,
      name: '',
      code: '',
      urlPath: '',
    };

    sessionStorage.setItem(
      'workspace',
      JSON.stringify({
        id: workspaceJson.id || -1,
        name: workspaceJson.name || '政采云',
        code: workspaceJson.code || -1,
        urlPath: workspaceJson.urlPath || 'zcy',
      }),
    );
    window.location.reload();
  };

  return (
    <Form form={form}>
      <Form.Item name="env" style={{ marginBottom: 0 }}>
        <Select
          style={{ width: 120 }}
          placeholder="请选择环境"
          options={envs.map((_) => ({ label: _.name, value: _.urlPath }))}
          onChange={(v) => onEnvChange(v as string)}
        />
      </Form.Item>
    </Form>
  );
};

export default Enviroments;
