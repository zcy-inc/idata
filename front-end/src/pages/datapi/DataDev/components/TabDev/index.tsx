import React, { useEffect, useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Button, Form, FormInstance, Input, message, Modal, Select, Space } from 'antd';
import { ModalForm } from '@ant-design/pro-form';
import { useModel } from 'umi';
import { get } from 'lodash';
import type { FC } from 'react';
import type { IRange } from 'monaco-editor';
import styles from './index.less';

import { IPane } from '@/models/datadev';
import { Task, TaskVersion } from '@/types/datadev';
import {
  EnvRunningState,
  StatementState,
  TaskTypes,
  VersionStatus,
  VersionStatusDisplayMap,
} from '@/constants/datadev';
import { Environments } from '@/constants/datasource';
import {
  deleteTask,
  getKylin,
  getScript,
  getSpark,
  getSqlSpark,
  getTask,
  getTaskVersions,
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
} from '@/services/datadev';

import DrawerBasic from './components/DrawerBasic';
import DrawerConfig from './components/DrawerConfig';
import DrawerVersion from './components/DrawerVersion';
import DrawerHistory from './components/DrawerHistory';
import IconRun from './components/IconRun';
import IconPause from './components/IconPause';
import SparkSql from './components/Content/SparkSql';
import SparkJava from './components/Content/SparkJava';
import SparkPython from './components/Content/SparkPython';
import ScriptShell from './components/Content/ScriptShell';
import ScriptPython from './components/Content/ScriptPython';
import Kylin from './components/Content/Kylin';

export interface TabTaskProps {
  pane: IPane;
}

const { Item } = Form;
const { confirm } = Modal;
const { TextArea } = Input;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];

const TabTask: FC<TabTaskProps> = ({ pane }) => {
  const [task, setTask] = useState<Task>();
  const [versions, setVersions] = useState<TaskVersion[]>([]);
  const [version, setVersion] = useState<number | undefined>(-1);
  const [content, setContent] = useState<any>({});

  const [visibleBasic, setVisibleBasic] = useState(false);
  const [visibleConfig, setVisibleConfig] = useState(false);
  const [visibleVersion, setVisibleVersion] = useState(false);
  const [visibleHistory, setVisibleHistory] = useState(false);
  // 启用 / 暂停
  const [actionType, setActionType] = useState('');
  const [visibleAction, setVisibleAction] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);
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

  useEffect(() => {
    if (pane) {
      getTaskWrapped();
      getTaskVersionsWrapped();
    }
  }, [pane.id]);

  useEffect(() => {
    if (version && version > 0) {
      getTaskContent(version);
    }
  }, [version]);

  /**
   * 移除结果
   */
  const removeResult = (i: number) => {
    results.splice(i, 1);
    setResults([...results]);
  };

  const componentMap = {
    [TaskTypes.SQL_SPARK]: (
      <SparkSql
        ref={formRef}
        monaco={monaco}
        data={{ content, log, res: results }}
        removeResult={removeResult}
      />
    ),
    [TaskTypes.SPARK_JAR]: (
      <SparkJava ref={formRef} data={content} jobId={content?.jobId || task?.id} />
    ),
    [TaskTypes.SPARK_PYTHON]: <SparkPython ref={formRef} monaco={monaco} data={{ content, log }} />,
    [TaskTypes.SCRIPT_SHELL]: <ScriptShell ref={formRef} monaco={monaco} data={{ content }} />,
    [TaskTypes.SCRIPT_PYTHON]: (
      <ScriptPython ref={formRef} monaco={monaco} data={{ content, log }} />
    ),
    [TaskTypes.KYLIN]: <Kylin ref={formRef} data={content} />,
  };

  /**
   * 获取作业配置
   */
  const getTaskWrapped = () =>
    getTask({ id: pane.id })
      .then((res) => setTask(res.data))
      .catch((err) => {});

  /**
   * 获取作业版本
   */
  const getTaskVersionsWrapped = () =>
    getTaskVersions({ jobId: pane.id })
      .then((res) => {
        setVersions(res.data);
        if (res.data[0]?.version) {
          setVersion(res.data[0]?.version);
        } else {
          setVersion(undefined);
        }
      })
      .catch((err) => {});

  /**
   * 获取作业内容
   */
  const getTaskContent = (version: number) => {
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
        getSqlSpark({ jobId: pane.id, version })
          .then((res) => setContent(res.data))
          .catch((err) => {});
        break;
      case TaskTypes.SPARK_JAR:
      case TaskTypes.SPARK_PYTHON:
        getSpark({ jobId: pane.id, version })
          .then((res) => setContent(res.data))
          .catch((err) => {});
        break;
      case TaskTypes.SCRIPT_SHELL:
      case TaskTypes.SCRIPT_PYTHON:
        getScript({ jobId: pane.id, version })
          .then((res) => setContent(res.data))
          .catch((err) => {});
        break;
      case TaskTypes.KYLIN:
        getKylin({ jobId: pane.id, version })
          .then((res) => setContent(res.data))
          .catch((err) => {});
        break;

      default:
        break;
    }
  };

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
            } else {
              message.error(`删除失败：${res.msg}`);
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
        const dataSql = {
          jobId: pane.id,
          jobType: TaskTypes.SQL_SPARK,
          sourceSql: monacoValue,
          externalTables: values.externalTables,
        };
        saveSqlSpark({ jobId: pane.id }, dataSql)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
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
        };
        saveSpark({ jobId: pane.id }, dataSparkJar)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
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
        };
        saveSpark({ jobId: pane.id }, dataSparkPython)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
            }
          })
          .catch((err) => {});
        break;
      case TaskTypes.SCRIPT_SHELL:
        const dataScriptShell = {
          jobId: pane.id,
          jobType: TaskTypes.SCRIPT_SHELL,
          sourceResource: monacoValue,
        };
        saveScript({ jobId: pane.id }, dataScriptShell)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
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
        };
        saveScript({ jobId: pane.id }, dataScriptPython)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
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
        };
        saveKylin({ jobId: pane.id }, dataKylin)
          .then((res) => {
            if (res.success) {
              message.success('保存成功');
              getTaskVersionsWrapped();
            } else {
              message.error(`保存失败：${res.msg}`);
            }
          })
          .catch((err) => {});
        break;
      default:
        break;
    }
  };

  /**
   * 调试选中代码段
   */
  const onDebug = () => {
    const editor = monaco.current?.editor;
    const range = editor?.getSelection() as IRange;
    let value = editor?.getModel()?.getValueInRange(range);
    if (!value) {
      value = editor?.getValue();
    }
    if (task?.jobType === TaskTypes.SQL_SPARK) {
      runQuery({ querySource: value as string, sessionKind: 'spark' })
        .then((res) => {
          if (!res.data) {
            message.error('请选择代码');
            return;
          }
          const { sessionId, statementId } = res.data;
          const runQueryResultWrapped = () => {
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
                    runQueryResultWrapped();
                  }, 2000);
                } else {
                  pollingFrom.current = 0;
                  const columns: any[] = [];
                  const dataSource = res.data.resultSet;
                  const record = dataSource[0] || {};
                  Object.keys(record).forEach((_) =>
                    columns.push({ title: _, dataIndex: _, key: _ }),
                  );
                  setResults((pre) => [...pre, { columns, dataSource }]);
                }
              })
              .catch((err) => {});
          };
          runQueryResultWrapped();
        })
        .catch((err) => {});
    }
    if (task?.jobType === TaskTypes.SPARK_PYTHON || task?.jobType === TaskTypes.SCRIPT_PYTHON) {
      runQuery({ querySource: value as string, sessionKind: 'pyspark' })
        .then((res) => {
          if (!res.data) {
            message.error('请选择代码');
            return;
          }
          const { sessionId, statementId } = res.data;
          const runQueryResultWrapped = () => {
            runQueryResult({ sessionId, sessionKind: 'pyspark', statementId })
              .then((res) => {
                if (
                  res.data.statementState !== StatementState.AVAILABLE &&
                  res.data.statementState !== StatementState.CANCELED
                ) {
                  setTimeout(() => {
                    runQueryResultWrapped();
                  }, 2000);
                } else {
                  const logs = get(res, 'data.pythonResults', '');
                  setLog([logs]);
                }
              })
              .catch((err) => {});
          };
          runQueryResultWrapped();
        })
        .catch((err) => {});
    }
  };

  /**
   * 测试任务
   */
  const onTest = () => {};

  const renderVersionLabel = (_: TaskVersion) => {
    const env = _.environment || '';
    const verState = VersionStatusDisplayMap[_.versionStatus];
    let runState = '';
    if (
      _.versionStatus === VersionStatus.PUBLISHED &&
      _.envRunningState === EnvRunningState.PAUSED
    ) {
      runState = '(暂停)';
    }
    return `${_.versionDisplay}-${env}-${verState}${runState}`;
  };

  return (
    <>
      <div className={styles.task}>
        <div className={styles.toolbar}>
          <div className={styles.version}>
            <span>当前版本：</span>
            {versions.length === 0 ? (
              '-'
            ) : (
              <Select
                size="large"
                style={{ width: 'auto', color: '#304ffe', fontSize: 16 }}
                placeholder="请选择"
                bordered={false}
                disabled={versions.length <= 0}
                options={versions.map((_) => ({
                  label: renderVersionLabel(_),
                  value: _.version,
                }))}
                value={version}
                onChange={(v) => setVersion(v as number)}
              />
            )}
          </div>
          <Space size={16}>
            <a onClick={() => onAction('run')}>单次运行</a>
            <a onClick={() => setVisibleBasic(true)}>基本信息</a>
            <a onClick={() => setVisibleConfig(true)}>配置</a>
            <a onClick={() => setVisibleConfig(true)}>血缘</a>
            <a onClick={() => setVisibleVersion(true)}>版本</a>
            <a onClick={() => setVisibleHistory(true)}>历史</a>
          </Space>
        </div>
        {componentMap[task?.jobType as TaskTypes]}
        <DrawerBasic
          visible={visibleBasic}
          onClose={() => setVisibleBasic(false)}
          data={task}
          pane={pane}
          getTaskWrapped={() => {}}
        />
        <DrawerConfig visible={visibleConfig} onClose={() => setVisibleConfig(false)} data={task} />
        <DrawerVersion
          visible={visibleVersion}
          onClose={() => setVisibleVersion(false)}
          data={task}
        />
        <DrawerHistory visible={visibleHistory} onClose={() => setVisibleHistory(false)} />
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
                    getTaskWrapped();
                    setVisibleAction(false);
                  } else {
                    message.error(`暂停失败：${res.msg}`);
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
                    getTaskWrapped();
                    setVisibleAction(false);
                  } else {
                    message.error(`恢复失败：${res.msg}`);
                  }
                })
                .catch((err) => {})
                .finally(() => setLoadingAction(false));
            }
            if (actionType === 'run') {
              runTask(params)
                .then((res) => {
                  if (res.success) {
                    message.success('单次运行成功');
                    getTaskWrapped();
                    setVisibleAction(false);
                  } else {
                    message.error(`单次运行失败：${res.msg}`);
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
              options={[
                { label: '预发', value: Environments.STAG },
                { label: '真线', value: Environments.PROD },
              ]}
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
                  message.error(`提交失败：${res.msg}`);
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
              options={[
                { label: '预发', value: Environments.STAG },
                { label: '真线', value: Environments.PROD },
              ]}
            />
          </Item>
          <Item name="remark" label="变更说明" rules={ruleText} style={{ marginBottom: 0 }}>
            <TextArea placeholder="请输入" />
          </Item>
        </ModalForm>
        <div className="workbench-submit">
          <Space>
            <IconRun onClick={() => onAction('resume')} />
            <IconPause onClick={() => onAction('pause')} />
            <Button key="delete" size="large" className="normal" onClick={onDelete}>
              删除
            </Button>
            <Button key="save" size="large" onClick={onSave}>
              保存
            </Button>
            {(task?.jobType === TaskTypes.SQL_SPARK ||
              task?.jobType === TaskTypes.SPARK_PYTHON ||
              task?.jobType === TaskTypes.SCRIPT_PYTHON ||
              task?.jobType === TaskTypes.KYLIN) && (
              <Button key="debug" size="large" type="primary" onClick={onDebug}>
                调试
              </Button>
            )}
            {task?.jobType === TaskTypes.SQL_SPARK && (
              <Button key="test" size="large" type="primary" onClick={onTest}>
                测试
              </Button>
            )}
            <Button
              key="publish"
              size="large"
              type="primary"
              onClick={() => setVisibleSubmit(true)}
            >
              申请发布
            </Button>
          </Space>
        </div>
      </div>
    </>
  );
};

export default TabTask;
