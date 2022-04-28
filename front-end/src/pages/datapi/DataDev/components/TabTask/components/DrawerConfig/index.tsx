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
import type { FC } from 'react';
import { MapInput, ListSelect } from '@/components';
import { DIJobType } from '@/constants/datadev';
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
  getConfiguredTaskList,
  getDependenceTaskList,
} from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import {
  SchPriority,
  execEngineOptions,
  execCoresOptions,
  defaultExecCores,
} from '@/constants/datadev';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: DIJobBasicInfo;
}

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const envs = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);
  const [DAGList, setDAGList] = useState<DAGListItem[]>([]);
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);

  const [stagForm] = Form.useForm();
  const [prodForm] = Form.useForm();

  const jobType = data?.jobType;

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
        const config = res.data;
        if (config.executeConfig.schTimeOut) {
          config.executeConfig.schTimeOut = config.executeConfig.schTimeOut / 60;
        } else {
          config.executeConfig.schTimeOut = 0;
        }
        if (config.executeConfig.schDryRun === 1) {
          config.executeConfig.schDryRun = ['schDryRun'];
        } else {
          config.executeConfig.schDryRun = [];
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
    if (!values.executeConfig.schDryRun) {
      values.executeConfig.schDryRun = 0;
    }
    if (values.executeConfig.schDryRun && Array.isArray(values.executeConfig.schDryRun)) {
      values.executeConfig.schDryRun = values.executeConfig.schDryRun.length > 0 ? 1 : 0;
    }
    if (!Number.isNaN(values.executeConfig.schTimeOut)) {
      values.executeConfig.schTimeOut = values.executeConfig.schTimeOut * 60;
    }
    saveTaskConfig({ jobId: data?.id as number, environment }, values)
      .then((res) => {
        if (res.success) {
          message.success(`保存${environment}成功`);
          onClose();
        }
      })
      .catch((err) => {});
  };

  const columns = [
    { title: '父节点输出任务名称', dataIndex: 'prevJobName', key: 'prevJobName', width: '30%' },
    { title: '所属DAG', dataIndex: 'dagName', key: 'dagName' },
  ];

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
        {envs.map((env) => (
          <TabPane tab={env} key={env}>
            <Form
              form={env === Environments.STAG ? stagForm : prodForm}
              layout="horizontal"
              colon={false}
              initialValues={{ execCores: defaultExecCores }}
              wrapperCol={{ span: 16 }}
            >
              <Title>调度配置</Title>
              <Item name={['executeConfig', 'schDryRun']} label="空跑调度">
                <Checkbox.Group>
                  <Checkbox value="schDryRun" />
                </Checkbox.Group>
              </Item>
              <Row>
                <Col span={12}>
                  <Item name={['executeConfig', 'schDagId']} label="DAG" rules={ruleSelc}>
                    <Select
                      size="large"
                      style={{ width }}
                      placeholder="请选择"
                      showSearch
                      options={DAGList.map((_) => ({ label: _.name, value: _.id }))}
                      filterOption={(input: string, option: any) =>
                        option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
                      }
                    />
                  </Item>
                </Col>
                <Col span={12}>
                  <Item name={['executeConfig', 'execQueue']} label="队列" rules={ruleSelc}>
                    <Select
                      size="large"
                      style={{ width }}
                      placeholder="请选择"
                      options={executeQueues.map((_) => ({ label: _.name, value: _.code }))}
                    />
                  </Item>
                </Col>
              </Row>
              <Item name={['executeConfig', 'schTimeOut']} label="超时时间" rules={ruleSelc}>
                <Input size="large" style={{ width }} placeholder="请输入" suffix="分" />
              </Item>
              <Item name={['executeConfig', 'schTimeOutStrategy']} label="超时策略">
                <Radio.Group>
                  <Radio value="alarm">超时告警</Radio>
                  <Radio value="fail">超时失败</Radio>
                </Radio.Group>
              </Item>
              <Item name={['executeConfig', 'schRerunMode']} label="重跑属性" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={restartOptions}
                />
              </Item>
              <Item name={['executeConfig', 'execWarnLevel']} label="报警等级" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={security.map((_) => ({ label: _.enumValue, value: _.valueCode }))}
                />
              </Item>

              <Item name={['executeConfig', 'schPriority']} label="优先等级" rules={ruleSelc}>
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
              <Item name={['executeConfig', 'execEngine']} label="执行引擎" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execEngineOptions}
                />
              </Item>
              <Item name={['executeConfig', 'execDriverMem']} label="Driver Memory" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execDriverMemOptions}
                />
              </Item>
              <Item name={['executeConfig', 'execWorkerMem']} label="Executor Memory" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execWorkerMemOptions}
                />
              </Item>
              <Item name={['executeConfig', 'execCores']} label="Executor Cores" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execCoresOptions}
                />
              </Item>
              <Item name={['executeConfig', 'extProperties']} label="自定义参数">
                <MapInput style={{ width }} />
              </Item>
              {jobType === DIJobType.BACK_FLOW && <>
                <Title>依赖配置</Title>
                <Item name="dependencies" label="依赖的上游任务">
                  <ListSelect
                    fetchData={() => getDependenceTaskList({ environment: env })}
                    labelField="prevJobName"
                    valueField="prevJobId"
                    columns={columns}
                  />
                </Item>
              </>}
            </Form>
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerConfig;
