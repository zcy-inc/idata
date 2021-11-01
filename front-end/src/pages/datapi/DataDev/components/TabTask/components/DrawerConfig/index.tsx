import React, { useEffect, useState } from 'react';
import { Button, Checkbox, Col, Drawer, Form, Input, message, Row, Select, Tabs } from 'antd';
import { get } from 'lodash';
// import { EditableProTable } from '@ant-design/pro-table';
// import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import Title from '@/components/Title';
// import { columnsParent, columnsOutput, restartOptions, concurrentOptions } from './constants';
import { concurrentOptions, restartOptions } from './constants';
import { DAGListItem, Task } from '@/types/datadev';
import {
  getDAGList,
  getEnumValues,
  getExecuteQueues,
  getTaskConfigs,
  saveTaskConfig,
} from '@/services/datadev';
import { Environments } from '@/constants/datasource';

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
  const [security, setSecurity] = useState<{ enumValue: string; valueCode: string }[]>([]);
  const [executeQueues, setExecuteQueues] = useState<{ name: string; code: string }[]>([]);

  // const [dataParent, setDataParent] = useState<any[]>([]);
  // const [keysParent, setKeysParent] = useState<React.Key[]>([]);
  // const [dataOutput, setDataOutput] = useState<any[]>([]);
  // const [keysOutput, setKeysOutput] = useState<React.Key[]>([]);
  // const { initialState } = useModel('@@initialState');
  // const currentUser = initialState?.currentUser || { nickname: '' };
  const [stagForm] = Form.useForm();
  const [prodForm] = Form.useForm();

  useEffect(() => {
    if (visible) {
      getTaskConfigsWrapped(Environments.STAG);
      getDAGList({ dwLayerCode: data?.dwLayerCode as string })
        .then((res) => setDAGList(res.data))
        .catch((err) => {});
      getEnumValues({ enumCode: 'alarmLayerEnum:ENUM' })
        .then((res) => setSecurity(res.data))
        .catch((err) => {});
      getExecuteQueues()
        .then((res) => setExecuteQueues(res.data))
        .catch((err) => {});
    }
  }, [visible]);

  const getTaskConfigsWrapped = (environment: Environments) =>
    getTaskConfigs({ jobId: data?.id as number, environment })
      .then((res) => {
        const config = get(res, 'data.[0]', {});
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

  // useEffect(() => {
  //   setDataParent([]);
  //   setKeysParent([]);
  //   setDataOutput([]);
  //   setKeysOutput([]);
  // }, [visible]);

  // const createRecordParent = () => {
  //   const key = Date.now();
  //   const item = { key, user: currentUser.nickname, from: '手动添加' };
  //   setDataParent([...dataParent, item]);
  //   setKeysParent([...keysParent, key]);
  // };

  // const createRecordOutput = () => {
  //   const key = Date.now();
  //   const item = { key, user: currentUser.nickname, from: '手动添加' };
  //   setDataOutput([...dataOutput, item]);
  //   setKeysOutput([...keysOutput, key]);
  // };

  // const onActionParent = (row: any, _: any, action: string) => {
  //   if (action === 'del') {
  //     const i = dataParent.findIndex((_) => _.key === row.key);
  //     dataParent.splice(i, 1);
  //     keysParent.splice(i, 1);
  //     setDataParent([...dataParent]);
  //     setKeysParent([...keysParent]);
  //   }
  // };

  // const onActionOutput = (row: any, _: any, action: string) => {
  //   if (action === 'del') {
  //     const i = dataOutput.findIndex((_) => _.key === row.key);
  //     dataOutput.splice(i, 1);
  //     keysOutput.splice(i, 1);
  //     setDataOutput([...dataOutput]);
  //     setKeysOutput([...keysOutput]);
  //   }
  // };

  // const getRecord = () => {};

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
    saveTaskConfig({ jobId: data?.id as number, environment, ...values })
      .then((res) => {
        if (res.success) {
          message.success(`保存${environment}成功`);
          onClose();
        } else {
          message.error(`保存${environment}失败：${res.msg}`);
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
        }}
      >
        {env.map((_) => (
          <TabPane tab={_} key={_}>
            <Title>调度配置</Title>
            <Form
              form={_ === Environments.STAG ? stagForm : prodForm}
              layout="horizontal"
              colon={false}
            >
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
              <Item name="execMaxParallelism" label="任务期望最大并发数" rules={ruleSelc}>
                <Select
                  size="large"
                  style={{ width }}
                  placeholder="请选择"
                  options={concurrentOptions}
                />
              </Item>
            </Form>
            {/* <Title>依赖配置</Title>
            <Form layout="horizontal" colon={false}>
              <Item name="9" className={styles['reset-label']} label="依赖上游节点">
                <Select size="large" style={{ width: widthL }} placeholder="请选择" options={[]} />
                <a style={{ marginLeft: 16 }} onClick={createRecordParent}>
                  添加
                </a>
                <a style={{ marginLeft: 16 }} onClick={getRecord}>
                  自动获取
                </a>
              </Item>
              <EditableProTable
                rowKey="key"
                columns={columnsParent}
                value={dataParent}
                recordCreatorProps={false}
                style={{ minHeight: 206 }}
                editable={{
                  type: 'multiple',
                  editableKeys: keysParent,
                  onChange: setKeysParent,
                  actionRender: (row, _) => [
                    <a key="del" onClick={() => onActionParent(row, _, 'del')}>
                      删除
                    </a>,
                  ],
                }}
              />
              <Item
                name="10"
                className={styles['reset-label']}
                label="本节点的输出"
                style={{ marginTop: 16 }}
              >
                <Select size="large" style={{ width: widthL }} placeholder="请选择" options={[]} />
                <a style={{ marginLeft: 16 }} onClick={createRecordOutput}>
                  添加
                </a>
                <a style={{ marginLeft: 16 }} onClick={getRecord}>
                  自动获取
                </a>
              </Item>
              <EditableProTable
                rowKey="key"
                columns={columnsOutput}
                value={dataOutput}
                recordCreatorProps={false}
                style={{ minHeight: 206 }}
                editable={{
                  type: 'multiple',
                  editableKeys: keysOutput,
                  onChange: setKeysOutput,
                  actionRender: (row, _) => [
                    <a key="del" onClick={() => onActionOutput(row, _, 'del')}>
                      删除
                    </a>,
                  ],
                }}
              />
            </Form> */}
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerConfig;
