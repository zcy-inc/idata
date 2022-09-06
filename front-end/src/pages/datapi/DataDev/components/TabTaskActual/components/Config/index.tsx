import React, { useEffect, useState, useImperativeHandle } from 'react';
import {
  Checkbox,
  Col,
  Form,
  Input,
  Radio,
  Row,
  Select,
  Tabs,
} from 'antd';
import type { FC } from 'react';
import { MapInput } from '@/components';
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
  getEngineType
} from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import {
  SchPriority,
  execCoresOptions,
  defaultExecCores,
} from '@/constants/datadev';
import { DependenciesFormItem } from '../../../../../components/DependenciesFormItem';

interface DrawerConfigProps {
  data?: DIJobBasicInfo;
  isStream: boolean; // 是否实时作业
}

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const envs = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ data, isStream = false }, ref) => {
  useImperativeHandle(ref, () => ({
    getValues: () => {
      let values: any = {};
      if (activeKey === Environments.STAG) {
        values = stagForm.getFieldsValue();
      }
      if (activeKey === Environments.PROD) {
        values = prodForm.getFieldsValue();
      }
      if (values.executeConfig.schDryRun && Array.isArray(values.executeConfig.schDryRun)) {
        values.executeConfig.schDryRun = values.executeConfig.schDryRun.length > 0 ? 1 : 0;
      }
      if (!Number.isNaN(values.executeConfig.schTimeOut)) {
        values.executeConfig.schTimeOut = values.executeConfig.schTimeOut * 60;
      }
      return {
        values: {
          ...values,
          executeConfig: {
            ...initialValues.executeConfig,
            ...values.executeConfig
          },
        },
        activeKey
      };
    }
  }))

  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);
  const [DAGList, setDAGList] = useState<DAGListItem[]>([]);
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);
  const [execEngineOptions, setExecEngineOptions] = useState<{ label: string; value: string }[]>([]);

  const [stagForm] = Form.useForm();
  const [prodForm] = Form.useForm();

  const jobType = data?.jobType;

  useEffect(() => {
    getTaskConfigsWrapped(Environments.STAG);
    getDAGListWrapped(Environments.STAG);
    getEnumValues({ enumCode: 'alarmLayerEnum:ENUM' })
      .then((res) => setSecurity(res.data))
      .catch((err) => {});
    getExecuteQueues({ jobType: (data as DIJobBasicInfo).jobTypeEnum })
      .then((res) => setExecuteQueues(res.data))
      .catch((err) => {});
    getEngineType({ jobType: (data as DIJobBasicInfo).jobTypeEnum })
      .then((res) => setExecEngineOptions(res.data?.map((item: string) => ({label: item, value: item}))))
      .catch((err) => {});
  }, []);

  const getTaskConfigsWrapped = (environment: Environments) =>
    getDataDevConfig({ jobId: data?.id as number, environment })
      .then((res) => {
        const config = res.data;
        // TODO: 逻辑优化，不要数据转换后再次复制给该变量，同一个变量名应该只有一个含义
        if (config.executeConfig.schTimeOut) {
          config.executeConfig.schTimeOut = config.executeConfig.schTimeOut / 60;
        } else {
          config.executeConfig.schTimeOut = 0;
        }
        // TODO: 逻辑优化
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
    getDAGList({ environment, jobType: (data as DIJobBasicInfo).jobTypeEnum })
      .then((res) => setDAGList(res.data))
      .catch((err) => {});

  const initialValues = {
    executeConfig: {
      schTimeOut: 0, // TODO: 
      schTimeOutStrategy: 'alarm',
      schRerunMode: 'always',
      execWarnLevel: 'ALARM_LEVEL_MEDIUM:ENUM_VALUE',
      schPriority: 2,
      execEngine: undefined,
      execDriverMem: 3,
      execWorkerMem: 4,
      execCores: defaultExecCores,
      execQueue: undefined,
    },
  };

  return (
    <Tabs
      className={styles['drawer-config']}
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
            initialValues={initialValues}
            wrapperCol={{ span: 16 }}
          >
            <Title>调度配置</Title>
            {isStream &&  <Item name={['executeConfig', 'schDryRun']} label="空跑调度">
              <Checkbox.Group>
                <Checkbox value="schDryRun" />
              </Checkbox.Group>
            </Item>}
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
            {isStream && <>
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
            </>}
          
            <Title>运行配置</Title>
            <Item name={['executeConfig', 'execEngine']} label="执行引擎" rules={ruleSelc}>
              <Select
                size="large"
                style={{ width }}
                placeholder="请选择"
                options={execEngineOptions}
              />
            </Item>
            <Item
              name={['executeConfig', 'execDriverMem']}
              label="Driver Memory"
              rules={ruleSelc}
            >
              <Select
                size="large"
                style={{ width }}
                placeholder="请选择"
                options={execDriverMemOptions}
              />
            </Item>
            <Item
              name={['executeConfig', 'execWorkerMem']}
              label="Executor Memory"
              rules={ruleSelc}
            >
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
            {jobType === DIJobType.BACK_FLOW && (
              <>
                <Title>依赖配置</Title>
                <DependenciesFormItem
                  name="dependencies"
                  fieldProps={{ environment: env, jobId: data?.id as number }}
                />
              </>
            )}
          </Form>
        </TabPane>
      ))}
    </Tabs>
  );
};

export default DrawerConfig;
