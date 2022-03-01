import React, { useEffect, useState } from 'react';
import { ModalForm } from '@ant-design/pro-form';
import { Form, Input, message, Select } from 'antd';
import { useModel } from 'umi';
import { useRequest } from 'ahooks';
import type { FC } from 'react';
import { CreateDIJobDto } from '@/types/datadev';
import styles from './index.less';

import {
  getEnumValues,
  getFolders,
  createDIJob,
  getDIJobTypes,
  getDISyncMode,
} from '@/services/datadev';
import { FolderBelong } from '@/constants/datadev';
import { Folder } from '@/types/datadev';

interface CreateTaskProps {}

const { Item } = Form;
const { TextArea } = Input;
const width = 300;
const rules = [{ required: true, message: '请选择' }];

const CreateTask: FC<CreateTaskProps> = ({}) => {
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [folders, setFolders] = useState<Folder[]>([]);
  const [form] = Form.useForm();
  const { visibleTask, setVisibleTask, getTreeWrapped } = useModel('datadev', (_) => ({
    visibleTask: _.visibleTask,
    setVisibleTask: _.setVisibleTask,
    getTreeWrapped: _.getTreeWrapped,
  }));
  const { data: jobTypeOptions } = useRequest(getDIJobTypes);
  const { data: syncModeOptions = [], run: getSyncModeOptions } = useRequest(getDISyncMode, {
    manual: true,
  });

  const handleCreateDI = async (values: CreateDIJobDto) => {
    const { success, msg } = await createDIJob(values);
    if (success) {
      message.success('创建任务成功');
      setVisibleTask(false);
      getTreeWrapped();
    } else {
      message.success(`创建任务失败: ${msg}`);
    }
  };

  const hanldeChangeDIType = (val: string) => {
    form.resetFields(['syncMode']);
    getSyncModeOptions({ jobType: val });
  };

  useEffect(() => {
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
      .catch((err) => {});
    getFolders({ belong: FolderBelong.DI })
      .then((res) => setFolders(res.data))
      .catch((err) => {});
  }, []);

  return (
    <ModalForm
      className={styles.form}
      title="新建任务"
      layout="horizontal"
      width={536}
      labelCol={{ span: 6 }}
      colon={false}
      form={form}
      visible={visibleTask}
      preserve={false}
      modalProps={{
        destroyOnClose: true,
        onCancel: () => setVisibleTask(false),
      }}
      submitter={{
        submitButtonProps: { size: 'large' },
        resetButtonProps: { size: 'large' },
      }}
      onFinish={handleCreateDI}
    >
      <Item name="jobType" label="任务类型" rules={rules}>
        <Select
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={jobTypeOptions}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          onChange={hanldeChangeDIType}
        />
      </Item>
      <Item name="syncMode" label="同步类型" rules={rules}>
        <Select
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={syncModeOptions}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
      <Item name="name" label="任务名称" rules={rules}>
        <Input size="large" style={{ width }} placeholder="请输入" />
      </Item>
      <Item name="dwLayerCode" label="数仓分层" rules={rules}>
        <Select
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={layers.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
      <Item name="folderId" label="目标文件夹" rules={rules}>
        <Select
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={folders.map((_) => ({ label: _.name, value: _.id }))}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
      <Item name="remark" label="备注说明">
        <TextArea placeholder="请输入" style={{ width }} />
      </Item>
    </ModalForm>
  );
};

export default CreateTask;
