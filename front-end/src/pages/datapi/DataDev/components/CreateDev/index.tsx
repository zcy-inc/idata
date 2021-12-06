import React, { useEffect, useState } from 'react';
import { ModalForm } from '@ant-design/pro-form';
import { Form, Input, message, Select } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import {
  createTask,
  getDataDevTypes,
  getEnumValues,
  getFolders,
  getTaskTypes,
} from '@/services/datadev';
import { FolderBelong, TaskCategory, TaskTypes } from '@/constants/datadev';
import { Folder, TaskType } from '@/types/datadev';

interface CreateTaskProps {}

const { Item } = Form;
const { TextArea } = Input;
const width = 300;
const rules = [{ required: true, message: '请选择' }];

const CreateTask: FC<CreateTaskProps> = ({}) => {
  const [taskTypes, setTaskTypes] = useState<TaskCategory[]>([]);
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [folders, setFolders] = useState<Folder[]>([]);
  const [taskType, setTaskType] = useState<TaskCategory>();
  const [languages, setLanguages] = useState<TaskType[]>([]);
  const [form] = Form.useForm();
  const { visibleDev, setVisibleDev, getTreeWrapped } = useModel('datadev', (_) => ({
    visibleDev: _.visibleDev,
    setVisibleDev: _.setVisibleDev,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    getDataDevTypes()
      .then((res) => setTaskTypes(res.data))
      .catch((err) => {});
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
      .catch((err) => {});
    getFolders({ belong: FolderBelong.DEVJOB })
      .then((res) => setFolders(res.data))
      .catch((err) => {});
  }, []);

  const getDataDevTypesWrapped = (v: TaskCategory) =>
    getTaskTypes({ catalog: v })
      .then((res) => setLanguages(res.data))
      .catch((err) => {});

  return (
    <ModalForm
      className={styles.form}
      title="新建作业"
      layout="horizontal"
      width={536}
      labelCol={{ span: 6 }}
      colon={false}
      form={form}
      visible={visibleDev}
      preserve={false}
      modalProps={{
        destroyOnClose: true,
        onCancel: () => setVisibleDev(false),
      }}
      submitter={{
        submitButtonProps: { size: 'large' },
        resetButtonProps: { size: 'large' },
      }}
      onFinish={async (values) => {
        const params = { ...values };
        if (values.jobType === TaskCategory.SQL) {
          params.jobType = TaskTypes.SQL_SPARK;
        }
        if (values.language) {
          params.jobType = values.language;
        }
        createTask(params)
          .then((res) => {
            if (res.success) {
              message.success('创建作业成功');
              setVisibleDev(false);
              getTreeWrapped();
            } else {
              message.success(`创建作业失败：${res.msg}`);
            }
          })
          .catch((err) => {});
      }}
    >
      <Item name="jobType" label="任务类型" rules={rules}>
        <Select<TaskCategory>
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={taskTypes.map((_) => ({ label: _, value: _ }))}
          onChange={(v) => {
            setTaskType(v);
            if (v === TaskCategory.SPARK || v === TaskCategory.SCRIPT) {
              getDataDevTypesWrapped(v);
            }
          }}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
      {(taskType === TaskCategory.SPARK || taskType === TaskCategory.SCRIPT) && (
        <Item name="language" label="运行方式" rules={rules}>
          <Select
            size="large"
            style={{ width }}
            placeholder="请选择"
            options={languages.map((_) => ({ label: _.language, value: _.code }))}
            showSearch
            filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          />
        </Item>
      )}
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
