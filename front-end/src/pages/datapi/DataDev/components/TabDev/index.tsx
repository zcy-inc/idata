import React, { useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Form, FormInstance, Input, message, Modal, Select, Tooltip, Button, Divider } from 'antd';
import { ModalForm } from '@ant-design/pro-form';
import { useModel } from 'umi';
import { get } from 'lodash';
import type { FC } from 'react';
import type { IRange } from 'monaco-editor';
import styles from './index.less';
import { IPane } from '@/models/datadev';
import { Task } from '@/types/datadev';
import { StatementState, TaskTypes } from '@/constants/datadev';
import { Environments } from '@/constants/datasource';
import {
  deleteTask,
  getKylin,
  getScript,
  getSpark,
  getSqlSpark,
  pauseTask,
  resumeTask,
  runQueryResult,
  runQuery,
  runTask,
  saveKylin,
  saveScript,
  saveSpark,
  saveSqlSpark,
  submitTask,
  tyrRun,
} from '@/services/datadev';
import { IconFont } from '@/components';

import DrawerBasic from './components/DrawerBasic';
import DrawerConfig from './components/DrawerConfig';
import DrawerVersion from './components/DrawerVersion';
import DrawerHistory from './components/DrawerHistory';
import DrawerDependence from './components/DrawerDependence';
import SqlContent from './components/Content/SqlContent';
import SparkJava from './components/Content/SparkJava';
import SparkPython from './components/Content/SparkPython';
import ScriptShell from './components/Content/ScriptShell';
import ScriptPython from './components/Content/ScriptPython';
import Kylin from './components/Content/Kylin';
import TyeRunSetting from './components/TryRunSetting';
import showDialog from '@/utils/showDialog';
import { useJob, VersionOption } from '../../../hooks/useJob';

export interface TabTaskProps {
  pane: IPane;
}

enum Btns {
  SAVE,
  DEBUG,
  RUN_ONCE,
  SUBMIT,
  ONLINE, // 上线
  OFFLINE, // 下线
  DELETE, // 删除
  DIVIDER,
  JOB_CONFIG,
  TRY_RUN,
}

const { Item } = Form;
const { confirm } = Modal;
const { TextArea } = Input;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];

const TabDev: FC<TabTaskProps> = ({ pane }) => {
  const [visibleJobConfig, setVisibleJobConfig] = useState(false);
  const [visibleBasic, setVisibleBasic] = useState(false);
  const [visibleConfig, setVisibleConfig] = useState(false);
  const [visibleVersion, setVisibleVersion] = useState(false);
  const [visibleHistory, setVisibleHistory] = useState(false);
  const [visibleDependence, setVisibleDependence] = useState(false);
  // 启用 / 暂停
  const [actionType, setActionType] = useState('');
  const [visibleAction, setVisibleAction] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);
  const [debugLoading, setDebugLoading] = useState(false);
  // 提交
  const [visibleSubmit, setVisibleSubmit] = useState(false);
  const [loadingSubmit, setLoadingSubmit] = useState(false);
  // content相关
  const [log, setLog] = useState<string[]>([]);
  const [results, setResults] = useState<{ columns: any[]; dataSource: any[] }[]>([]);
  const monaco = useRef<MonacoEditor>(null);
  const formRef = useRef<{ form: FormInstance }>(null);
  const pollingFrom = useRef<number>(0);

  const { getTreeWrapped, onRemovePane } = useModel('datadev', (_) => ({
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));
  /**
   * 移除结果
   */
  const removeResult = (i: number) => {
    results.splice(i, 1);
    setResults([...results]);
  };

  /**
   * 获取作业内容
   */
  const getTaskContent = (task: Task, jobId: number, version: number) => {
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
      case TaskTypes.SQL_FLINK:
        return getSqlSpark({ jobId, version }).then((res) => res.data);
      case TaskTypes.SPARK_JAR:
      case TaskTypes.SPARK_PYTHON:
        return getSpark({ jobId, version }).then((res) => res.data);
      case TaskTypes.SCRIPT_SHELL:
      case TaskTypes.SCRIPT_PYTHON:
        return getScript({ jobId, version }).then((res) => res.data);
      case TaskTypes.KYLIN:
        return getKylin({ jobId, version }).then((res) => res.data);
      default:
        return Promise.resolve({});
    }
  };
  const {
    versions,
    jobBasic: task,
    versionOption,
    setVersionOption,
    version,
    content,
    refreshTaskBasic,
    refreshTask,
  } = useJob({
    jobId: pane.id,
    getContent: getTaskContent,
  });

  /**
   * 暂停、恢复、单次运行任务
   */
  const onAction = (type: 'pause' | 'resume' | 'run') => {
    setActionType(type);
    setVisibleAction(true);
  };

  /**
   * 删除任务
   */
  const onDelete = () => {
    confirm({
      title: '删除任务',
      content: '您确认要删除该任务吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteTask({ id: pane.id })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              onRemovePane(pane.cid);
              getTreeWrapped();
            }
          })
          .catch((err) => {}),
    });
  };

  /**
   * 保存任务
   */
  const onSave = () => {
    const values = formRef.current?.form?.getFieldsValue() || {};
    const monacoValue = monaco.current?.editor?.getValue() || '';
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
      case TaskTypes.SQL_FLINK:
        const dataSql = {
          ...values,
          jobId: pane.id,
          jobType: task?.jobType,
          sourceSql: monacoValue,
          externalTables: values.externalTables,
          udfIds: values.udfIds.join(','),
          version,
        };
        saveSqlSpark({ jobId: pane.id }, dataSql)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.SPARK_JAR:
        const dataSparkJar = {
          jobId: pane.id,
          jobType: TaskTypes.SPARK_JAR,
          resourceHdfsPath: values.resourceHdfsPath,
          appArguments: values.appArguments?.map((_: any) => ({
            argumentValue: _.argumentValue,
            argumentRemark: _.argumentRemark,
          })),
          mainClass: values.mainClass,
          version,
        };
        saveSpark({ jobId: pane.id }, dataSparkJar)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.SPARK_PYTHON:
        const dataSparkPython = {
          jobId: pane.id,
          jobType: TaskTypes.SPARK_PYTHON,
          pythonResource: monacoValue,
          appArguments: values.appArguments?.map((_: any) => ({
            argumentValue: _.argumentValue,
            argumentRemark: _.argumentRemark,
          })),
          version,
        };
        saveSpark({ jobId: pane.id }, dataSparkPython)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.SCRIPT_SHELL:
        const dataScriptShell = {
          jobId: pane.id,
          jobType: TaskTypes.SCRIPT_SHELL,
          sourceResource: monacoValue,
          version,
        };
        saveScript({ jobId: pane.id }, dataScriptShell)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.SCRIPT_PYTHON:
        const dataScriptPython = {
          jobId: pane.id,
          jobType: TaskTypes.SCRIPT_SHELL,
          sourceResource: monacoValue,
          scriptArguments: values.scriptArguments?.map((_: any) => ({
            argumentValue: _.argumentValue,
            argumentRemark: _.argumentRemark,
          })),
          version,
        };
        saveScript({ jobId: pane.id }, dataScriptPython)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.KYLIN:
        const dataKylin = {
          jobId: pane.id,
          jobType: TaskTypes.KYLIN,
          cubeName: values.cubeName,
          buildType: values.buildType,
          startTime: new Date(values.startTime).getTime(),
          endTime: new Date(values.endTime).getTime(),
          version,
        };
        saveKylin({ jobId: pane.id }, dataKylin)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              refreshTask();
            }
          })
          .catch((err) => {});
        break;
      default:
        break;
    }
  };

  // 获取调试代码
  const getDebugCode = () => {
    const editor = monaco.current?.editor;
    const range = editor?.getSelection() as IRange;
    let value = editor?.getModel()?.getValueInRange(range);
    if (!value) {
      value = editor?.getValue();
    }
    return value;
  };

  // 轮询 pyspark 调试结果
  const fetchPysparkQueryResult = ({
    sessionId,
    statementId,
  }: {
    sessionId: number;
    statementId: number;
  }) => {
    runQueryResult({ sessionId, sessionKind: 'pyspark', statementId })
      .then((res) => {
        if (
          res.data.statementState !== StatementState.AVAILABLE &&
          res.data.statementState !== StatementState.CANCELED
        ) {
          setTimeout(() => {
            fetchPysparkQueryResult({
              sessionId,
              statementId,
            });
          }, 2000);
        } else {
          const logs = get(res, 'data.pythonResults', '');
          setLog([logs]);
        }
      })
      .catch((err) => {});
  };

  // 轮询 spark 调试结果
  const fetchSparkQueryResult = ({
    sessionId,
    statementId,
  }: {
    sessionId: number;
    statementId: number;
  }) => {
    runQueryResult({
      sessionId,
      sessionKind: 'spark',
      statementId,
      from: pollingFrom.current,
      size: 10,
    })
      .then((res) => {
        if (
          res.data.statementState !== StatementState.AVAILABLE &&
          res.data.statementState !== StatementState.CANCELED
        ) {
          pollingFrom.current = pollingFrom.current + 10;
          const logs = get(res, 'data.queryRunLog.log', []);
          setLog((pre) => [...pre, ...logs]);
          setTimeout(() => {
            fetchSparkQueryResult({
              sessionId,
              statementId,
            });
          }, 2000);
        } else {
          pollingFrom.current = 0;
          const columns: any[] = [];
          const dataSource = res.data.resultSet;
          const record = dataSource[0] || {};
          Object.keys(record).forEach((_) => columns.push({ title: _, dataIndex: _, key: _ }));
          setResults((pre) => [...pre, { columns, dataSource }]);
        }
      })
      .catch((err) => {});
  };

  /**
   * 调试选中代码段
   */
  const onDebug = async () => {
    const value = getDebugCode();
    if (typeof value !== 'string') {
      return message.error('请选择代码');
    }
    setDebugLoading(true);
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK: {
        const { data } = await runQuery({ querySource: value as string, sessionKind: 'spark' });
        fetchSparkQueryResult(data);
        break;
      }
      case TaskTypes.SPARK_PYTHON:
      case TaskTypes.SCRIPT_PYTHON: {
        const { data } = await runQuery({ querySource: value as string, sessionKind: 'pyspark' });
        fetchPysparkQueryResult(data);
        break;
      }
    }
    setDebugLoading(false);
  };

  const tryRun = () => {
    showDialog(
      '试运行配置',
      {
        beforeConfirm: (dialog, form, done) => {
          form.form.validateFields().then((values: any) => {
            tyrRun({
              ...values,
              jobId: pane.id,
              version,
            }).then(() => {
              done();
            });
          });
        },
      },
      TyeRunSetting,
    );
  };

  const btnMap = new Map([
    [
      Btns.SAVE,
      <Tooltip title="保存" key="9">
        <Button className={styles.btn} icon={<IconFont type="icon-baocun" />} onClick={onSave} />
      </Tooltip>,
    ],
    [
      Btns.DEBUG,
      <Tooltip title="调试" key="8">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-tiaoshi" />}
          onClick={onDebug}
          loading={debugLoading}
        />
      </Tooltip>,
    ],
    [
      Btns.RUN_ONCE,
      <Tooltip title="单次运行" key="7">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-yunxing" />}
          onClick={() => onAction('run')}
        />
      </Tooltip>,
    ],
    [
      Btns.SUBMIT,
      <Tooltip title="提交版本" key="6">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-tijiaofabu" />}
          onClick={() => {
            if (!versions.length) {
              message.info('没有版本无法提交，请先保存以创建版本');
              return;
            }
            setVisibleSubmit(true);
          }}
        />
      </Tooltip>,
    ],
    [
      Btns.ONLINE,
      <Tooltip title="上线" key="5">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-yunxingbaise-copy" />}
          onClick={() => onAction('resume')}
        />
      </Tooltip>,
    ],
    [
      Btns.OFFLINE,
      <Tooltip title="下线" key="4">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-zantingbaise-copy" />}
          onClick={() => onAction('pause')}
        />
      </Tooltip>,
    ],
    [
      Btns.DELETE,
      <Tooltip title="删除" key="3">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-shanchubaise-copy" />}
          onClick={onDelete}
        />
      </Tooltip>,
    ],
    [
      Btns.DIVIDER,
      <Divider key="2" type="vertical" style={{ margin: '0 16px', backgroundColor: '#545c75' }} />,
    ],
    [
      Btns.JOB_CONFIG,
      <Tooltip title="作业配置" key="1">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-peizhiguanli" />}
          onClick={() => setVisibleJobConfig(true)}
        />
      </Tooltip>,
    ],
    [
      Btns.TRY_RUN,
      <Tooltip title="试运行" key="10">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-peizhiguanli" />}
          onClick={tryRun}
        />
      </Tooltip>,
    ],
  ]);

  const getBtnNames = () => {
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
      case TaskTypes.SPARK_PYTHON:
      case TaskTypes.SCRIPT_PYTHON:
        return [
          Btns.SAVE,
          Btns.DEBUG,
          Btns.RUN_ONCE,
          Btns.SUBMIT,
          Btns.ONLINE,
          Btns.OFFLINE,
          Btns.DELETE,
          Btns.DIVIDER,
          Btns.JOB_CONFIG,
        ];
      case TaskTypes.SQL_FLINK:
        return [Btns.SAVE, Btns.SUBMIT, Btns.RUN_ONCE, Btns.DELETE, Btns.DIVIDER, Btns.JOB_CONFIG];
      case TaskTypes.KYLIN:
        return [
          Btns.SAVE,
          Btns.DEBUG,
          Btns.RUN_ONCE,
          Btns.SUBMIT,
          Btns.ONLINE,
          Btns.OFFLINE,
          Btns.DELETE,
        ];
      default:
        return [Btns.SAVE, Btns.RUN_ONCE, Btns.SUBMIT, Btns.ONLINE, Btns.OFFLINE, Btns.DELETE];
    }
  };

  const getBtns = () => getBtnNames().map((name) => btnMap.get(name));

  const getContent = () => {
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
      case TaskTypes.SQL_FLINK:
        return (
          <SqlContent
            ref={formRef}
            monaco={monaco}
            data={{ task, content, log, res: results }}
            removeResult={removeResult}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.SPARK_JAR:
        return <SparkJava ref={formRef} data={content} jobId={content?.jobId || task?.id} />;
      case TaskTypes.SPARK_PYTHON:
        return (
          <SparkPython
            ref={formRef}
            monaco={monaco}
            data={{ content, log }}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.SCRIPT_SHELL:
        return <ScriptShell ref={formRef} monaco={monaco} data={{ content }} />;
      case TaskTypes.SCRIPT_PYTHON:
        return (
          <ScriptPython
            ref={formRef}
            monaco={monaco}
            data={{ content, log }}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.KYLIN:
        return <Kylin ref={formRef} data={content} />;
      default:
        return null;
    }
  };

  return (
    <>
      <div className={styles.task}>
        <div className={styles.box}>
          <div className={styles.content}>
            <div className={styles.header}>
              <div className={styles.btns}>{getBtns()}</div>
              <div className={styles.version}>
                <span>当前版本:</span>
                {versions.length === 0 ? (
                  '-'
                ) : (
                  <Select
                    size="large"
                    style={{ width: 'auto', color: '#fff', fontSize: 14 }}
                    placeholder="请选择"
                    bordered={false}
                    disabled={versions.length <= 0}
                    options={versions}
                    value={versionOption?.value}
                    onChange={(_, option) => setVersionOption(option as VersionOption)}
                  />
                )}
              </div>
            </div>
            {getContent()}
          </div>
          <div className={styles.sideMenu}>
            <div className={styles.sideMenuItem} onClick={() => setVisibleBasic(true)}>
              基本信息
            </div>
            <div className={styles.sideMenuItem} onClick={() => setVisibleConfig(true)}>
              配置
            </div>
            <div className={styles.sideMenuItem} onClick={() => setVisibleDependence(true)}>
              依赖
            </div>
            <div className={styles.sideMenuItem} onClick={() => setVisibleVersion(true)}>
              版本
            </div>
            <div className={styles.sideMenuItem} onClick={() => setVisibleHistory(true)}>
              历史
            </div>
          </div>
        </div>
      </div>
      <DrawerBasic
        visible={visibleBasic}
        onClose={() => setVisibleBasic(false)}
        data={task}
        pane={pane}
        getTaskWrapped={refreshTaskBasic}
      />
      <DrawerConfig visible={visibleConfig} onClose={() => setVisibleConfig(false)} data={task} />
      <DrawerVersion
        visible={visibleVersion}
        onClose={() => setVisibleVersion(false)}
        data={task}
      />
      <DrawerHistory
        visible={visibleHistory}
        onClose={() => setVisibleHistory(false)}
        data={task}
      />
      <DrawerDependence
        visible={visibleDependence}
        onClose={() => setVisibleDependence(false)}
        data={task}
      />
      <ModalForm
        title="选择环境"
        layout="horizontal"
        width={536}
        labelCol={{ span: 6 }}
        colon={false}
        preserve={false}
        modalProps={{ destroyOnClose: true, onCancel: () => setVisibleAction(false) }}
        visible={visibleAction}
        submitter={{
          submitButtonProps: { size: 'large', loading: loadingAction },
          resetButtonProps: { size: 'large' },
        }}
        onFinish={async (values) => {
          setLoadingAction(true);
          const params = {
            id: pane.id,
            environment: values.env,
          };
          if (actionType === 'pause') {
            pauseTask(params)
              .then((res) => {
                if (res.success) {
                  message.success('暂停成功');
                  refreshTaskBasic();
                  setVisibleAction(false);
                }
              })
              .catch((err) => {})
              .finally(() => setLoadingAction(false));
          }
          if (actionType === 'resume') {
            resumeTask(params)
              .then((res) => {
                if (res.success) {
                  message.success('恢复成功');
                  refreshTaskBasic();
                  setVisibleAction(false);
                }
              })
              .catch((err) => {})
              .finally(() => setLoadingAction(false));
          }
          if (actionType === 'run') {
            runTask(params)
              .then((res) => {
                if (res.success) {
                  message.success('单次运行成功，运行结果在历史标签页内查看。');
                  refreshTaskBasic();
                  setVisibleAction(false);
                }
              })
              .catch((err) => {})
              .finally(() => setLoadingAction(false));
          }
        }}
      >
        <Item name="env" label="提交环境" rules={ruleSlct}>
          <Select
            placeholder="请选择"
            options={Object.values(Environments).map((_) => ({ label: _, value: _ }))}
          />
        </Item>
      </ModalForm>
      <ModalForm
        title="提交"
        layout="horizontal"
        width={536}
        labelCol={{ span: 6 }}
        colon={false}
        preserve={false}
        modalProps={{ destroyOnClose: true, onCancel: () => setVisibleSubmit(false) }}
        visible={visibleSubmit}
        submitter={{
          submitButtonProps: { size: 'large', loading: loadingSubmit },
          resetButtonProps: { size: 'large' },
        }}
        onFinish={async (values) => {
          setLoadingSubmit(true);
          submitTask(
            {
              jobId: pane.id,
              version: content?.version as number,
              env: values.env,
            },
            {
              remark: values.remark,
            },
          )
            .then((res) => {
              if (res.success) {
                message.success('提交成功');
                setVisibleSubmit(false);
                return true;
              } else {
                return false;
              }
            })
            .catch((err) => {})
            .finally(() => setLoadingSubmit(false));
        }}
      >
        <Item name="env" label="提交环境" rules={ruleSlct}>
          <Select
            placeholder="请选择"
            options={Object.values(Environments).map((_) => ({ label: _, value: _ }))}
          />
        </Item>
        <Item name="remark" label="变更说明" rules={ruleText} style={{ marginBottom: 0 }}>
          <TextArea placeholder="请输入" />
        </Item>
      </ModalForm>
    </>
  );
};

export default TabDev;
