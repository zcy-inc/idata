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
  getTaskTypes,
} from '@/services/datadev';
import { TaskCategory, TaskTypes } from '@/constants/datadev';
import { TaskType } from '@/types/datadev';
import { DEVJOBFolderFormItem } from '../../../components/FolderFormItem';

interface CreateTaskProps {}

const { Item } = Form;
const { TextArea } = Input;
const width = 300;
const rules = [{ required: true, message: '请选择' }];

const CreateTask: FC<CreateTaskProps> = ({}) => {
  const [taskTypes, setTaskTypes] = useState<TaskCategory[]>([]);
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [taskType, setTaskType] = useState<TaskCategory>();
  const [languages, setLanguages] = useState<TaskType[]>([]);
  const [form] = Form.useForm();
  const { visibleDev, setVisibleDev, getTreeWrapped, curNode } = useModel('datadev');

  useEffect(() => {
    const folderId = curNode?.id;
    form.setFieldsValue({ folderId });
  }, [curNode])

  useEffect(() => {
    getDataDevTypes()
      .then((res) => setTaskTypes(res.data))
      .catch((err) => {});
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
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
              message.success(`创建作业失败: ${res.msg}`);
            }
          })
          .catch((err) => {});
      }}
    >
      <Item name="jobType" label="作业类型" rules={rules}>
        <Select<TaskCategory>
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={taskTypes.map((_) => ({ label: _, value: _ }))}
          onChange={(v) => {
            setTaskType(v);
            if ([TaskCategory.SCRIPT, TaskCategory.SPARK, TaskCategory.SQL].includes(v)) {
              getDataDevTypesWrapped(v);
            }
          }}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
      <Item name="name" label="作业名称" rules={rules}>
        <Input size="large" style={{ width }} placeholder="请输入" />
      </Item>
      {taskType && [TaskCategory.SPARK, TaskCategory.SCRIPT, TaskCategory.SQL].includes(taskType) && (
        <Item name="language" label="运行方式" rules={rules}>
          <Select
            size="large"
            style={{ width }}
            placeholder="请选择"
            options={languages.map((_) => ({ label: _.code, value: _.code }))}
            showSearch
            filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          />
        </Item>
      )}
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
      <DEVJOBFolderFormItem style={{ width }} />
      <Item name="remark" label="备注说明">
        <TextArea placeholder="请输入" style={{ width }} />
      </Item>
    </ModalForm>
  );
};

export default CreateTask;
