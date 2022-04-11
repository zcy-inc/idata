import React, { useEffect, useState } from 'react';
import {
  Button,
  Checkbox,
  Col,
  Drawer,
  Form,
  Input,
  message,
  Radio,
  Row,
  Select,
  Tabs,
} from 'antd';
import { get } from 'lodash';
import type { FC } from 'react';
import styles from './index.less';

import Title from '@/components/Title';
import { execDriverMemOptions, execWorkerMemOptions, restartOptions } from './constants';
import { DAGListItem, DIJobBasicInfo } from '@/types/datadev';
import {
  getDAGList,
  getDataDevConfig,
  getEnumValues,
  getExecuteQueues,
  saveTaskConfig,
} from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import { SchPriority, execEngineOptions } from '@/constants/datadev';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: DIJobBasicInfo;
}

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const env = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);
  const [DAGList, setDAGList] = useState<DAGListItem[]>([]);
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);

  const [stagForm] = Form.useForm();
  const [prodForm] = Form.useForm();

  useEffect(() => {
    if (visible) {
      getTaskConfigsWrapped(Environments.STAG);
      getDAGListWrapped(Environments.STAG);
      getEnumValues({ enumCode: 'alarmLayerEnum:ENUM' })
        .then((res) => setSecurity(res.data))
        .catch((err) => {});
      getExecuteQueues()
        .then((res) => setExecuteQueues(res.data))
        .catch((err) => {});
    }
  }, [visible]);

  const getTaskConfigsWrapped = (environment: Environments) =>
    getDataDevConfig({ jobId: data?.id as number, environment })
      .then((res) => {
        const config = get(res, 'data.executeConfig', {});
        if (config.schTimeOut) {
          config.schTimeOut = config.schTimeOut / 60;
        } else {
          config.schTimeOut = 0;
        }
        if (config.schDryRun === 1) {
          config.schDryRun = ['schDryRun'];
        } else {
          config.schDryRun = [];
        }
        if (environment === Environments.STAG) {
          stagForm.setFieldsValue(config);
        }
        if (environment === Environments.PROD) {
          prodForm.setFieldsValue(config);
        }
      })
      .catch((err) => {});

  const getDAGListWrapped = (environment: Environments) =>
    // getDAGList({ dwLayerCode: data?.dwLayerCode as string, environment })
    getDAGList({ environment })
      .then((res) => setDAGList(res.data))
      .catch((err) => {});

  const onSave = (environment: Environments) => {
    let values: any = {};
    if (activeKey === Environments.STAG) {
      values = stagForm.getFieldsValue();
    }
    if (activeKey === Environments.PROD) {
      values = prodForm.getFieldsValue();
    }
    if (!values.schDryRun) {
      values.schDryRun = 0;
    }
    if (values.schDryRun && Array.isArray(values.schDryRun)) {
      values.schDryRun = values.schDryRun.length > 0 ? 1 : 0;
    }
    if (!Number.isNaN(values.schTimeOut)) {
      values.schTimeOut = values.schTimeOut * 60;
    }
    saveTaskConfig({ jobId: data?.id as number, environment }, { executeConfig: values })
      .then((res) => {
        if (res.success) {
          message.success(`保存${environment}成功`);
          onClose();
        }
      })
      .catch((err) => {});
  };

  return (
    <Drawer
      className={styles['drawer-config']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="配置"
      footer={
        <Button size="large" onClick={() => onSave(activeKey)}>
          {`保存${activeKey}`}
        </Button>
      }
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Tabs
        className="reset-tabs"
        onChange={(k) => {
          setActiveKey(k as Environments);
          getTaskConfigsWrapped(k as Environments);
          getDAGListWrapped(k as Environments);
        }}
      >
        {env.map((_) => (
          <TabPane tab={_} key={_}>
            <Form
              form={_ === Environments.STAG ? stagForm : prodForm}
              layout="horizontal"
              colon={false}
            >
              <Title>调度配置</Title>
              <Item name="schDryRun" label="空跑调度">
                <Checkbox.Group>
                  <Checkbox value="schDryRun" />
                </Checkbox.Group>
              </Item>
              <Row>
                <Col span={12}>
                  <Item name="schDagId" label="DAG" rules={ruleSelc}>
                    <Select
                      size="large"
                      style={{ width }}
                      placeholder="请选择"
                      options={DAGList.map((_) => ({ label: _.name, value: _.id }))}
                    />
                  </Item>
                </Col>
                <Col span={12}>
                  <Item name="execQueue" label="队列" rules={ruleSelc}>
                    <Select
                      size="large"
                      style={{ width }}
                      placeholder="请选择"
                      options={executeQueues.map((_) => ({ label: _.name, value: _.code }))}
                    />
                  </Item>
                </Col>
              </Row>
              <Item name="schTimeOut" label="超时时间" rules={ruleSelc}>
                <Input size="large" style={{ width }} placeholder="请输入" suffix="分" />
              </Item>
              <Item name="schTimeOutStrategy" label="超时策略">
                <Radio.Group>
                  <Radio value="alarm">超时告警</Radio>
                  <Radio value="fail">超时失败</Radio>
                </Radio.Group>
              </Item>
              <Item name="schRerunMode" label="重跑属性" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={restartOptions}
                />
              </Item>
              <Item name="execWarnLevel" label="报警等级" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={security.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
                />
              </Item>
              <Item name="execDriverMem" label="Driver Memory" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execDriverMemOptions}
                />
              </Item>
              <Item name="execWorkerMem" label="Executor Memory" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execWorkerMemOptions}
                />
              </Item>
              <Item name="schPriority" label="优先等级" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={[
                    { label: '低', value: SchPriority.LOW },
                    { label: '中', value: SchPriority.MIDDLE },
                    { label: '高', value: SchPriority.HIGH },
                  ]}
                />
              </Item>
              <Title>运行配置</Title>
              <Item name="execEngine" label="执行引擎" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execEngineOptions}
                />
              </Item>
            </Form>
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerConfig;
