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
import { get, pick } from 'lodash';
import type { FC } from 'react';
import styles from './index.less';
import { MapInput, Title } from '@/components';
import { restartOptions, execDriverMemOptions, execWorkerMemOptions } from './constants';
import { DAGListItem, TaskConfig, Task } from '@/types/datadev';
import {
  getDAGList,
  getDataDevConfig,
  getEnumValues,
  getExecuteQueues,
  saveTaskConfig,
} from '@/services/datadev';
import { DataSourceTypes, Environments } from '@/constants/datasource';
import {
  ExecEngine,
  SchPriority,
  TaskTypes,
  execCoresOptions,
  defaultExecCores,
  // SchRerunMode,
} from '@/constants/datadev';
import { getDataSourceList } from '@/services/datasource';
import { DataSourceItem } from '@/types/datasource';
import { DependenciesFormItem } from '../../../../../components/DependenciesFormItem';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
  // version: string;
}

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const env = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);
  const [DAGList, setDAGList] = useState<DAGListItem[]>([]);
  const [dataSource, setDataSource] = useState<DataSourceItem[]>([]);
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);
  const [destWriteMode, setDestWriteMode] = useState('');

  const [stagForm] = Form.useForm();
  const [prodForm] = Form.useForm();

  useEffect(() => {
    if (visible) {
      setActiveKey(Environments.STAG);
      getTaskConfigsWrapped(Environments.STAG);
      getDAGListWrapped(Environments.STAG);
      getEnumValues({ enumCode: 'alarmLayerEnum:ENUM' })
        .then((res) => setSecurity(res.data))
        .catch((err) => {});
      getExecuteQueues()
        .then((res) => setExecuteQueues(res.data))
        .catch((err) => {});
      getDataSourceList({ limit: 999, offset: 0 })
        .then((res) => setDataSource(res.data.content || []))
        .catch((err) => {});
    }
  }, [visible]);

  const getTaskConfigsWrapped = (environment: Environments) =>
    getDataDevConfig({ jobId: data?.id as number, environment })
      .then((res) => {
        const executeConfig = get(res, 'data.executeConfig', {});
        const dependencies = get(res, 'data.dependencies', []);
        const output = get(res, 'data.output', null);
        // 处理超时
        if (executeConfig.schTimeOut) {
          executeConfig.schTimeOut = executeConfig.schTimeOut / 60;
        } else {
          executeConfig.schTimeOut = 0;
        }
        // 处理空跑
        if (executeConfig.schDryRun === 1) {
          executeConfig.schDryRun = ['schDryRun'];
        } else {
          executeConfig.schDryRun = [];
        }

        setDestWriteMode(output.destWriteMode || 'OVERWRITE');
        // 赋值调度配置
        if (environment === Environments.STAG) {
          stagForm.setFieldsValue({ ...executeConfig, ...output, dependencies });
        }
        if (environment === Environments.PROD) {
          prodForm.setFieldsValue({ ...executeConfig, ...output, dependencies });
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
    // 处理空跑调度
    if (!values.schDryRun) {
      values.schDryRun = 0;
    }
    if (values.schDryRun && Array.isArray(values.schDryRun)) {
      values.schDryRun = values.schDryRun.length > 0 ? 1 : 0;
    }
    // 处理超时时间
    if (!Number.isNaN(values.schTimeOut)) {
      values.schTimeOut = values.schTimeOut * 60;
    }

    const destDataSourceId = values.destDataSourceId;
    const destDataSourceType =
      dataSource.find((_) => _.id === destDataSourceId)?.type || DataSourceTypes.HIVE;
    const params = {
      executeConfig: {
        ...values,
        jobId: data?.id as number,
        environment: activeKey,
        schDagId: values.schDagId,
        schRerunMode: values.schRerunMode,
        schTimeOut: values.schTimeOut,
        schDryRun: values.schDryRun,
        execQueue: values.execQueue,
        execMaxParallelism: values.execMaxParallelism,
        execWarnLevel: values.execWarnLevel,
        schTimeOutStrategy: values.schTimeOutStrategy,
        schPriority: values.schPriority,
        execDriverMem: values.execDriverMem,
        execWorkerMem: values.execWorkerMem,
        execEngine: values.execEngine,
      },
      dependencies: values.dependencies,
      output: {
        ...pick(values, ['destDataSourceId', 'destTable', 'destWriteMode', 'jobTargetTablePk']),
        jobId: data?.id as number,
        environment: activeKey,
        destDataSourceType: destDataSourceType,
      },
    };
    saveTaskConfig({ jobId: data?.id as number, environment }, params as TaskConfig)
      .then((res) => {
        if (res.success) {
          message.success(`保存${environment}成功`);
          onClose();
        }
      })
      .catch((err) => {});
  };

  const renderExecEngineOptions = () => {
    switch (data?.jobType) {
      case TaskTypes.SQL_SPARK:
      case TaskTypes.SCRIPT_PYTHON:
      case TaskTypes.SCRIPT_SHELL:
        return [
          { label: 'SPARK', value: ExecEngine.SPARK },
          { label: 'SQOOP', value: ExecEngine.SQOOP },
          { label: 'KYLIN', value: ExecEngine.KYLIN },
        ];
      case TaskTypes.SPARK_PYTHON:
      case TaskTypes.SPARK_JAR:
        return [{ label: 'SPARK', value: ExecEngine.SPARK }];
      case TaskTypes.KYLIN:
        return [{ label: 'KYLIN', value: ExecEngine.KYLIN }];
      case TaskTypes.SQL_FLINK:
        return [{ label: 'FLINK', value: ExecEngine.FLINK }];
      default:
        return [];
    }
  };

  const initialValues = {
    schTimeOut: 60, // TODO:
    schTimeOutStrategy: 'alarm',
    schRerunMode: 'always',
    execWarnLevel: 'ALARM_LEVEL_MEDIUM:ENUM_VALUE',
    schPriority: 2,
    execDriverMem: 3,
    execWorkerMem: 4,
    execCores: defaultExecCores,
    execQueue: 'root.offline',
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
        activeKey={activeKey}
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
              initialValues={initialValues}
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
                      showSearch
                      filterOption={(input: string, option: any) =>
                        option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
                      }
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
                      showSearch
                      filterOption={(input: string, option: any) =>
                        option.label.indexOf(input) >= 0
                      }
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
                  options={renderExecEngineOptions()}
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
              <Item name="execCores" label="Executor Cores" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execCoresOptions}
                />
              </Item>
              <Item name="extProperties" label="自定义参数">
                <MapInput style={{ width }} />
              </Item>
              <Title>依赖配置</Title>
              <DependenciesFormItem
                name="dependencies"
                fieldProps={{ environment: _, jobId: data?.id as number }}
              />
              <Title>输出配置</Title>
              <Item name="destWriteMode" label="数据写入模式" style={{ marginTop: 16 }}>
                <Select
                  style={{ width }}
                  placeholder="请选择"
                  options={[
                    { label: 'OVERWRITE', value: 'OVERWRITE' },
                    { label: 'UPSERT', value: 'UPSERT' },
                  ]}
                  onChange={(_) => setDestWriteMode(_ as string)}
                />
              </Item>
              {destWriteMode === 'UPSERT' && (
                <Item name="jobTargetTablePk" label="目标表主键" style={{ marginTop: 16 }}>
                  <Input placeholder="请输入" style={{ width }} />
                </Item>
              )}
              <Item name="destDataSourceId" label="本作业的输出">
                <Select
                  style={{ width }}
                  placeholder="选择数据源"
                  options={dataSource.map((_) => ({ label: _.name, value: _.id }))}
                  showSearch
                  filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
                />
              </Item>
              <Item name="destTable" label=" ">
                <Input placeholder="请输入表名" style={{ width }} />
              </Item>
            </Form>
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerConfig;
