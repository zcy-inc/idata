import React, { useEffect, useState } from 'react';
import { Button, Drawer, Form, Input, message, Select } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import Title from '@/components/Title';
import { Task, TaskType } from '@/types/datadev';
import { editTask, getEnumValues, getTaskTypes } from '@/services/datadev';
import { TaskCategory } from '@/constants/datadev';
import { IPane } from '@/models/datadev';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
  pane: IPane;
  getTaskWrapped: () => Promise<void>;
}

const { Item } = Form;
const { TextArea } = Input;
const widthL = 400;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ visible, onClose, data, pane, getTaskWrapped }) => {
  const [taskTypes, setTaskTypes] = useState<TaskType[]>([]);
  const [layers, setLayers] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [form] = Form.useForm();

  const { replaceTab, getTreeWrapped } = useModel('datadev', (_) => ({
    replaceTab: _.replaceTab,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    getTaskTypes({ catalog: TaskCategory.DI })
      .then((res) => setTaskTypes(res.data))
      .catch((err) => {});
    getEnumValues({ enumCode: 'dwLayerEnum:ENUM' })
      .then((res) => setLayers(res.data))
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (visible && data) {
      const values = {
        name: data.name,
        dwLayerCode: data.dwLayerCode,
        jobType: data.jobType,
        creator: data.creator,
        remark: data.remark,
      };
      form.setFieldsValue(values);
    }
  }, [visible, data]);

  const onSave = () => {
    const values = form.getFieldsValue();
    const params = {
      ...data,
      ...values,
    };
    editTask(params)
      .then((res) => {
        if (res.success) {
          message.success('保存成功');
          getTreeWrapped();
          replaceTab({
            oldKey: pane.cid,
            newKey: pane.cid,
            title: res.data.name,
            pane,
          });
          onClose();
          getTaskWrapped();
        } else {
          message.error(`保存失败：${res.msg}`);
        }
      })
      .catch((err) => {});
  };

  return (
    <Drawer
      className={styles['drawer-basic']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="配置"
      footer={
        <Button size="large" onClick={onSave}>
          保存
        </Button>
      }
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Title>基本配置</Title>
      <Form form={form} layout="horizontal" colon={false}>
        <Item name="name" label="任务名称" rules={ruleSelc}>
          <Input size="large" style={{ width: widthL }} placeholder="请选择" />
        </Item>
        <Item name="dwLayerCode" label="数仓分层" rules={ruleSelc}>
          <Select
            size="large"
            style={{ width: widthL }}
            placeholder="请选择"
            options={layers.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
            disabled
          />
        </Item>
        <Item name="jobType" label="任务类型" rules={ruleText}>
          <Select
            size="large"
            style={{ width: widthL }}
            placeholder="请输入"
            options={taskTypes.map((_) => ({ label: _.name, value: _.code }))}
            disabled
          />
        </Item>
        <Item name="creator" label="所属人">
          <Input size="large" style={{ width: widthL }} placeholder="-" disabled />
        </Item>
        <Item name="remark" label="备注">
          <TextArea style={{ width: widthL }} placeholder="请输入" />
        </Item>
      </Form>
    </Drawer>
  );
};

export default DrawerConfig;
