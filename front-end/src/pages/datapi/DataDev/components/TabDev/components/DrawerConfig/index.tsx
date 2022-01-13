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
  Table,
  Tabs,
} from 'antd';
import { get } from 'lodash';
import type { FC } from 'react';
import { ColumnsType } from 'antd/es/table';
import styles from './index.less';

import Title from '@/components/Title';
import { restartOptions, execDriverMemOptions, execWorkerMemOptions } from './constants';
import { ConfiguredTaskListItem, DAGListItem, TaskConfig, Task } from '@/types/datadev';
import {
  getConfiguredTaskList,
  getDAGList,
  getDataDevConfig,
  getEnumValues,
  getExecuteQueues,
  saveTaskConfig,
} from '@/services/datadev';
import { DataSourceTypes, Environments } from '@/constants/datasource';
import { ExecEngine, SchPriority, TaskTypes } from '@/constants/datadev';
import { getDataSourceList } from '@/services/datasource';
import { DataSourceItem } from '@/types/datasource';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
}

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const env = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerConfig: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);
  const [DAGList, setDAGList] = useState<DAGListItem[]>([]);
  const [configuredTaskList, setConfiguredTaskList] = useState<ConfiguredTaskListItem[]>([]);
  const [dataSource, setDataSource] = useState<DataSourceItem[]>([]);
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);
  const [destWriteMode, setDestWriteMode] = useState('');

  const [depDataStag, setDepDataStag] = useState<any[]>([]);
  const [outDataStag, setOutDataStag] = useState<any[]>([]);
  const [depDataProd, setDepDataProd] = useState<any[]>([]);
  const [outDataProd, setOutDataProd] = useState<any[]>([]);
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
      getConfiguredTaskListWrapped(Environments.STAG);
      getDataSourceList({ limit: 999, offset: 0 })
        .then((res) => setDataSource(res.data.content || []))
        .catch((err) => {});
    }
  }, [visible]);

  const columnsDependence: ColumnsType<ConfiguredTaskListItem> = [
    { title: '父节点输出任务名称', dataIndex: 'jobName', key: 'jobName', width: '45%' },
    { title: '所属DAG', dataIndex: 'dagName', key: 'dagName', width: '45%' },
    {
      title: '操作',
      key: 'option',
      render: (v, r, i) => (
        <a
          onClick={() => {
            if (activeKey === Environments.STAG) {
              depDataStag.splice(i, 1);
              setDepDataStag([...depDataStag]);
            }
            if (activeKey === Environments.PROD) {
              depDataProd.splice(i, 1);
              setDepDataProd([...depDataProd]);
            }
          }}
        >
          删除
        </a>
      ),
    },
  ];

  const columnsOutput: ColumnsType<ConfiguredTaskListItem> = [
    { title: '输出表名', dataIndex: 'destTable', key: 'destTable' },
    {
      title: '操作',
      key: 'option',
      render: (v, r, i) => (
        <a
          onClick={() => {
            if (activeKey === Environments.STAG) {
              outDataStag.splice(i, 1);
              setOutDataStag([...outDataStag]);
            }
            if (activeKey === Environments.PROD) {
              outDataProd.splice(i, 1);
              setOutDataProd([...outDataProd]);
            }
          }}
        >
          删除
        </a>
      ),
    },
  ];

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
        // 处理上游依赖
        if (environment === Environments.STAG) {
          const tmp = dependencies.map((_: any) => ({
            ..._,
            jobName: _.prevJobName,
            dagName: _.prevJobDagName,
            dagId: _.prevJobDagId,
          }));
          setDepDataStag(tmp);
        }
        if (environment === Environments.PROD) {
          const tmp = dependencies.map((_: any) => ({
            ..._,
            jobName: _.prevJobName,
            dagName: _.prevJobDagName,
            dagId: _.prevJobDagId,
          }));
          setDepDataProd(tmp);
        }
        // 处理输出
        executeConfig.destWriteMode = output?.destWriteMode;
        executeConfig.jobTargetTablePk = output?.jobTargetTablePk;
        executeConfig.destDataSourceId = output?.destDataSourceId;
        executeConfig.destTable = output?.destTable;
        // 赋值调度配置
        if (environment === Environments.STAG) {
          stagForm.setFieldsValue({ ...executeConfig });
          if (output) {
            setOutDataStag([{ ...output }]);
          }
        }
        if (environment === Environments.PROD) {
          prodForm.setFieldsValue(executeConfig);
          if (output) {
            setOutDataProd([{ ...output }]);
          }
        }
      })
      .catch((err) => {});

  const getDAGListWrapped = (environment: Environments) =>
    getDAGList({ dwLayerCode: data?.dwLayerCode as string, environment })
      .then((res) => setDAGList(res.data))
      .catch((err) => {});

  const getConfiguredTaskListWrapped = (environment: Environments) =>
    getConfiguredTaskList({ environment })
      .then((res) => setConfiguredTaskList(res.data))
      .catch((err) => {});

  const addDepRecord = () => {
    const key = new Date().getTime();
    let curConfiguredTask;
    if (activeKey === Environments.STAG) {
      const curConfiguredTaskId = stagForm.getFieldValue('configuredTask');
      if (!curConfiguredTaskId) {
        message.info('请先选择依赖的上游任务');
        return;
      }
      curConfiguredTask = configuredTaskList.find((_) => _.jobId === curConfiguredTaskId);
      depDataStag.push({
        key,
        ...curConfiguredTask,
        prevJobId: curConfiguredTask?.jobId,
        prevJobDagId: curConfiguredTask?.dagId,
      });
      setDepDataStag([...depDataStag]);
    }
    if (activeKey === Environments.PROD) {
      const curConfiguredTaskId = prodForm.getFieldValue('configuredTask');
      if (!curConfiguredTaskId) {
        message.info('请先选择依赖的上游任务');
        return;
      }
      curConfiguredTask = configuredTaskList.find((_) => _.jobId === curConfiguredTaskId);
      depDataProd.push({
        key,
        ...curConfiguredTask,
        prevJobId: curConfiguredTask?.jobId,
        prevJobDagId: curConfiguredTask?.dagId,
      });
      setDepDataProd([...depDataProd]);
    }
  };

  const addOutRecord = () => {
    if (activeKey === Environments.STAG) {
      if (!stagForm.getFieldValue('destWriteMode')) {
        message.info('请选择数据写入模式');
        return;
      }
      if (!stagForm.getFieldValue('destDataSourceId')) {
        message.info('请选择本任务的输出');
        return;
      }
      if (!stagForm.getFieldValue('destTable')) {
        message.info('请填写输出的表名');
        return;
      }
      if (outDataStag.length > 0) {
        message.info('输出表只能存在一张');
        return;
      }
      const destDataSourceId = stagForm.getFieldValue('destDataSourceId');
      const destTable = stagForm.getFieldValue('destTable');
      const jobTargetTablePk = stagForm.getFieldValue('jobTargetTablePk');
      const outData = {
        jobId: data?.id,
        environment: Environments.STAG,
        destDataSourceType: DataSourceTypes.HIVE,
        destDataSourceId,
        destTable,
        destWriteMode,
        jobTargetTablePk,
      };
      outDataStag.push(outData);
      setOutDataStag([...outDataStag]);
    }
    if (activeKey === Environments.PROD) {
      if (!prodForm.getFieldValue('destWriteMode')) {
        message.info('请选择数据写入模式');
        return;
      }
      if (!prodForm.getFieldValue('destDataSourceId')) {
        message.info('请选择本任务的输出');
        return;
      }
      if (!prodForm.getFieldValue('destTable')) {
        message.info('请填写输出的表名');
        return;
      }
      if (outDataProd.length > 0) {
        message.info('输出表只能存在一张');
        return;
      }
      const destDataSourceId = prodForm.getFieldValue('destDataSourceId');
      const destTable = prodForm.getFieldValue('destTable');
      const jobTargetTablePk = stagForm.getFieldValue('jobTargetTablePk');
      const outData = {
        jobId: data?.id,
        environment: Environments.PROD,
        destDataSourceType: DataSourceTypes.HIVE,
        destDataSourceId,
        destTable,
        destWriteMode,
        jobTargetTablePk,
      };
      outDataProd.push(outData);
      setOutDataProd([...outDataProd]);
    }
  };

  const onSave = (environment: Environments) => {
    let values: any = {};
    let depData: any[] = [];
    let outData: any = {};
    if (activeKey === Environments.STAG) {
      values = stagForm.getFieldsValue();
      depData = depDataStag;
      outData = outDataStag[0] || {};
    }
    if (activeKey === Environments.PROD) {
      values = prodForm.getFieldsValue();
      depData = depDataProd;
      outData = outDataProd[0] || {};
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
    const params = {
      executeConfig: {
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
      },
      dependencies: depData.map((_) => ({
        jobId: data?.id as number,
        environment: activeKey,
        prevJobId: _.prevJobId,
        prevJobDagId: _.prevJobDagId,
      })),
      output: {
        jobId: data?.id as number,
        environment: activeKey,
        destDataSourceType: DataSourceTypes.HIVE,
        destDataSourceId: outData.destDataSourceId,
        destTable: outData.destTable,
        destWriteMode: outData.destWriteMode,
        jobTargetTablePk: outData.jobTargetTablePk,
      },
    };
    saveTaskConfig({ jobId: data?.id as number, environment }, params as TaskConfig)
      .then((res) => {
        if (res.success) {
          message.success(`保存${environment}成功`);
          onClose();
        } else {
          message.error(`保存${environment}失败: ${res.msg}`);
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
      default:
        return [];
    }
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
          getConfiguredTaskListWrapped(k as Environments);
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
                      showSearch
                      filterOption={(input: string, option: any) =>
                        option.label.indexOf(input) >= 0
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
              <Item name="execDriverMem" label="驱动器内存" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={execDriverMemOptions}
                />
              </Item>
              <Item name="execWorkerMem" label="执行器内存" rules={ruleSelc}>
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
                  options={renderExecEngineOptions()}
                />
              </Item>
              <Title>依赖配置</Title>
              <div style={{ display: 'flex' }}>
                <Item
                  name="configuredTask"
                  className={styles['reset-label']}
                  label="依赖的上游任务"
                >
                  <Select
                    style={{ width }}
                    placeholder="请选择"
                    options={configuredTaskList.map((_) => ({ label: _.jobName, value: _.jobId }))}
                    showSearch
                    filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
                  />
                </Item>
                <a style={{ marginLeft: 16, marginTop: 5 }} onClick={addDepRecord}>
                  添加
                </a>
              </div>
              <Table<ConfiguredTaskListItem>
                columns={columnsDependence}
                dataSource={activeKey === Environments.STAG ? depDataStag : depDataProd}
                pagination={false}
                size="small"
                style={{ minHeight: 150 }}
              />
              <Row>
                <Col span={12}>
                  <Item
                    name="destWriteMode"
                    className={styles['reset-label']}
                    label="数据写入模式"
                    style={{ marginTop: 16 }}
                  >
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
                </Col>
                {destWriteMode === 'UPSERT' && (
                  <Col span={12}>
                    <Item
                      name="jobTargetTablePk"
                      label="目标表主键"
                      className={styles['reset-label']}
                      style={{ marginTop: 16 }}
                    >
                      <Input placeholder="请输入" style={{ width }} />
                    </Item>
                  </Col>
                )}
              </Row>
              <Row>
                <Col span={12}>
                  <Item
                    name="destDataSourceId"
                    className={styles['reset-label']}
                    label="本任务的输出"
                  >
                    <Select
                      style={{ width }}
                      placeholder="选择数据源"
                      options={dataSource.map((_) => ({ label: _.name, value: _.id }))}
                      showSearch
                      filterOption={(input: string, option: any) =>
                        option.label.indexOf(input) >= 0
                      }
                    />
                  </Item>
                </Col>
                <Col span={10}>
                  <Item name="destTable" className={styles['reset-label']} label="输出的表名">
                    <Input placeholder="请输入" style={{ width }} />
                  </Item>
                </Col>
                <Col span={2}>
                  <a
                    style={{ marginLeft: 16, marginTop: 5, display: 'flow-root' }}
                    onClick={addOutRecord}
                  >
                    添加
                  </a>
                </Col>
              </Row>
              <Table
                columns={columnsOutput}
                dataSource={activeKey === Environments.STAG ? outDataStag : outDataProd}
                pagination={false}
                size="small"
                style={{ minHeight: 150 }}
              />
            </Form>
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerConfig;
