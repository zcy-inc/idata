import React, { useEffect, useState } from 'react';
import { ModalForm } from '@ant-design/pro-form';
import { Button, Checkbox, Form, Input, message, Select, Table, Tabs, Upload } from 'antd';
import { get } from 'lodash';
import type { FC, ReactNode } from 'react';

import styles from './index.less';
import { CheckboxValueType } from 'antd/lib/checkbox/Group';
import { UploadOutlined } from '@ant-design/icons';
import { DataSourceItem, DBConfigList } from '@/types/datasource';
import {
  createDataSource,
  createDataSourceCSV,
  getCSVPreview,
  getDataSourceTypes,
  getEnvironments,
  postCSV,
  testConnection,
  updateDataSource,
} from '@/services/datasource';
import { DataSourceTypes, Environments } from '@/constants/datasource';
import { IconFont } from '@/components';

const { Item } = Form;
const { TextArea } = Input;
const { TabPane } = Tabs;

const width = 300;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSelc = [{ required: true, message: '请选择' }];
const ConnectionSuccessMsg = [
  <IconFont type="icon-liantong" />,
  <span style={{ color: '#05cc87' }}>已连通</span>,
];
const ConnectionFailedMsg = [
  <IconFont type="icon-lianjieshibai" />,
  <span style={{ color: '#fa8c15' }}>连接失败，可重试连接</span>,
];

interface CreateModalProps {
  visible: boolean;
  onCancel: () => void;
  initial?: DataSourceItem;
  refresh: () => void;
}

const CreateModal: FC<CreateModalProps> = ({ visible, onCancel, initial, refresh }) => {
  const [DSTypes, setDSTypes] = useState<DataSourceTypes[]>([]);
  const [DSType, setDSType] = useState<DataSourceTypes>(DataSourceTypes.MYSQL);
  const [envs, setEnvs] = useState<CheckboxValueType[]>([]); // 获取的可选择的环境
  const [env, setEnv] = useState<CheckboxValueType[]>([]); // 已选择的环境
  const [activeKey, setActiveKey] = useState<Environments>();
  const [showUpload, setShowUpload] = useState(true);
  const [preview, setPreview] = useState<{
    columns: any[];
    dataSource: any[];
  }>({
    columns: [],
    dataSource: [],
  });
  const [loading, setLoading] = useState(false);
  const [connectionLoading, setConnectionLoading] = useState(false);
  const [connectionMsg, setConnectionMsg] = useState<ReactNode>();
  const [form] = Form.useForm();

  useEffect(() => {
    getDataSourceTypes().then((res) => setDSTypes(res.data));
    getEnvironments().then((res) => setEnvs(res.data));
  }, []);

  useEffect(() => {
    if (initial) {
      const values = {
        type: initial.type,
        name: initial.name,
        envList: initial.envList,
        remark: initial.remark,
      };
      const dbConfigList = get(initial, 'dbConfigList', []);
      const tmpEnvList: Environments[] = [];
      setActiveKey(dbConfigList[0].env);
      dbConfigList.forEach((_) => {
        const tmp = {};
        tmpEnvList.push(_.env);
        for (let [key, value] of Object.entries(_)) {
          tmp[`${_.env}_${key}`] = value;
        }
        Object.assign(values, tmp);
      });
      form.setFieldsValue(values);
      setEnv(tmpEnvList);
      setDSType(initial.type);
    }
  }, [initial, visible]);

  const connectionTest = () => {
    const values = form.getFieldsValue();
    setConnectionMsg(undefined);
    setConnectionLoading(true);
    testConnection(
      { dataSourceType: DSType },
      {
        host: values[`${activeKey}_host`],
        port: values[`${activeKey}_port`],
        env: activeKey,
        dbName: values[`${activeKey}_dbName`],
        username: values[`${activeKey}_username`],
        password: values[`${activeKey}_password`],
        schema: values[`${activeKey}_schema`],
      },
    )
      .then((res) => {
        if (res.data) {
          setConnectionMsg(ConnectionSuccessMsg);
        } else {
          setConnectionMsg(ConnectionFailedMsg);
        }
      })
      .catch((err) => {})
      .finally(() => setConnectionLoading(false));
  };

  const renderRules = () => {
    if (DSType === DataSourceTypes.MYSQL || DSType === DataSourceTypes.POSTGRESQL) {
      return ruleText;
    } else {
      return [];
    }
  };

  const reset = () => {
    setDSType(DataSourceTypes.MYSQL);
    setEnv([]);
    setShowUpload(true);
    setPreview({ columns: [], dataSource: [] });
    onCancel();
    setConnectionMsg(undefined);
    refresh();
  };

  return (
    <ModalForm
      className={styles.form}
      title={initial ? '修改数据源' : '新增数据源'}
      layout="horizontal"
      form={form}
      width={536}
      labelCol={{ span: 6 }}
      visible={visible}
      preserve={false}
      modalProps={{
        destroyOnClose: true,
        bodyStyle: { maxHeight: 510, overflowY: 'scroll' },
        onCancel: () => {
          setDSType(DataSourceTypes.MYSQL);
          setEnv([]);
          setShowUpload(true);
          onCancel();
        },
      }}
      submitter={{
        submitButtonProps: { size: 'large', loading },
        resetButtonProps: { size: 'large' },
      }}
      onFinishFailed={(err) => {
        message.info('请检查必填项');
      }}
      onFinish={async (values) => {
        setLoading(true);
        if (DSType === 'csv') {
          const values = form.getFieldsValue();
          createDataSourceCSV({ ...values, fileName: values.name })
            .then((res) => {
              if (res.success) {
                message.success('创建文件型数据源成功');
                reset();
              } else {
                message.error(`创建文件型数据源失败：${res.msg}`);
              }
            })
            .catch((err) => {})
            .finally(() => setLoading(false));
        } else {
          const params = {
            type: values.type,
            name: values.name,
            remark: values.remark,
            envList: env as string[],
            dbConfigList: [] as Partial<DBConfigList>[],
          };
          const stag = { env: Environments.STAG };
          const prod = { env: Environments.PROD };
          for (let [key, value] of Object.entries(values)) {
            if (key.startsWith('stag')) {
              stag[key.split('_')[1]] = value;
            }
            if (key.startsWith('prod')) {
              prod[key.split('_')[1]] = value;
            }
          }
          if (env.includes('stag')) {
            params.dbConfigList.push(stag);
          }
          if (env.includes('prod')) {
            params.dbConfigList.push(prod);
          }
          if (initial) {
            updateDataSource({ ...params, id: initial.id })
              .then((res) => {
                if (res.success) {
                  message.success('修改数据源成功');
                  reset();
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
          } else {
            createDataSource(params)
              .then((res) => {
                if (res.success) {
                  message.success('创建数据源成功');
                  reset();
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
          }
        }
      }}
    >
      <Item name="type" label="数据源类型" rules={ruleSelc}>
        <Select
          size="large"
          style={{ width }}
          placeholder="请选择"
          options={DSTypes.map((_) => ({ label: _, value: _ }))}
          onChange={(v) => setDSType(`${v}` as DataSourceTypes)}
        />
      </Item>
      <Item className={styles.name} name="name" label="数据源名称" rules={ruleText}>
        <Input size="large" style={{ width }} placeholder="请输入" />
      </Item>
      <Item className={styles.env} name="envList" label="环境" rules={ruleSelc}>
        <Checkbox.Group options={envs as string[]} onChange={(v) => setEnv(v)} />
      </Item>
      <Item className={styles.comment} name="remark" label="备注说明">
        <TextArea placeholder="请输入" style={{ width }} />
      </Item>
      {env.length ? (
        <Tabs className="reset-tabs" onChange={(k) => setActiveKey(k as Environments)}>
          {env.map((e) => (
            <TabPane tab={e} key={`${e}`} forceRender>
              {DSType !== 'csv' && (
                <>
                  <Item name={`${e}_dbName`} label="数据库名称" rules={renderRules()}>
                    <Input size="large" style={{ width }} placeholder="请输入" />
                  </Item>
                  <Item name={`${e}_username`} label="数据库账号" rules={renderRules()}>
                    <Input size="large" style={{ width }} placeholder="请输入" />
                  </Item>
                  <Item name={`${e}_password`} label="数据库密码" rules={renderRules()}>
                    <Input size="large" style={{ width }} placeholder="请输入" />
                  </Item>
                  <Item name="path" label="服务器地址" required>
                    <Input.Group compact>
                      <Item name={`${e}_host`} style={{ marginBottom: 0 }} rules={ruleText}>
                        <Input className={styles.host} size="large" placeholder="host" />
                      </Item>
                      <Input className={styles.colon} size="large" placeholder=":" disabled />
                      <Item name={`${e}_port`} style={{ marginBottom: 0 }} rules={ruleText}>
                        <Input className={styles.port} size="large" placeholder="port" />
                      </Item>
                    </Input.Group>
                  </Item>
                  <Item name={`${e}_schema`} label="目录">
                    <Input size="large" style={{ width }} placeholder="请输入" />
                  </Item>
                  <Item className={styles.connection} label="测试连通性">
                    <Button onClick={connectionTest} loading={connectionLoading}>
                      测试连通性
                    </Button>
                    <span style={{ marginLeft: 8 }}>{connectionMsg}</span>
                  </Item>
                </>
              )}
            </TabPane>
          ))}
        </Tabs>
      ) : null}
      {DSType === 'csv' && !!env.length && (
        <>
          <Upload
            style={{ marginTop: 16 }}
            accept="text/csv"
            maxCount={1}
            onRemove={() => {
              setShowUpload(true);
              return true;
            }}
            customRequest={({ file, onSuccess, onError }) => {
              setShowUpload(false);
              const destTableName = form.getFieldValue('name');
              const data = new FormData();
              data.append('file', file);
              postCSV({ destTableName, environments: Environments.STAG }, data)
                .then((res) => {
                  if (res.success) {
                    onSuccess?.(res, file as unknown as XMLHttpRequest);
                    getCSVPreview({
                      type: form.getFieldValue('type'),
                      name: form.getFieldValue('name'),
                      envList: [Environments.STAG],
                      fileName: form.getFieldValue('name'),
                    }).then((resP) => {
                      const keys: string[] = [];
                      const columns = resP.data.meta.map((_) => {
                        keys.push(_.columnName);
                        return {
                          title: _.columnName,
                          key: _.columnName,
                          dataIndex: _.columnName,
                        };
                      });
                      const dataSource = resP.data.data.map((_) => {
                        const record: any = {};
                        for (let i = 0; i < _.length; i++) {
                          record[keys[i]] = _[i];
                        }
                        return record;
                      });
                      setPreview({ columns, dataSource });
                    });
                  } else {
                    message.error(`上传失败：${res.msg}`);
                  }
                })
                .catch((err) => {
                  onError?.(err, file as unknown as XMLHttpRequest);
                });
            }}
          >
            {showUpload && <Button icon={<UploadOutlined />}>上传文件</Button>}
          </Upload>
          <Table
            className={styles.table}
            columns={preview.columns}
            dataSource={preview.dataSource}
            style={{ marginTop: 16 }}
            pagination={{
              total: preview.dataSource?.length,
              showTotal: (t) => `共${t}条`,
            }}
            scroll={{ x: 'max-content' }}
          />
        </>
      )}
    </ModalForm>
  );
};

export default CreateModal;
