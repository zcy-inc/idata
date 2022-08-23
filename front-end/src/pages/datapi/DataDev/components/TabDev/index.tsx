import React, { useRef, useState } from 'react';
import { Form, Input, message, Modal, Select, Tooltip, Button, Divider } from 'antd';
import { ModalForm } from '@ant-design/pro-form';
import { useModel } from 'umi';
import { get } from 'lodash';
import type { FC } from 'react';
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
  getTyrRunLog,
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
import { useJob, VersionOption } from '../../../hooks/useJob';
import { useEditorPanel } from '../../../hooks/useEditorPanel';

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
  const [isDebuging, setIsDebuging] = useState(false);
  // 提交
  const [visibleSubmit, setVisibleSubmit] = useState(false);
  const [loadingSubmit, setLoadingSubmit] = useState(false);

  const pollingFrom = useRef<number>(0);

  const { getTreeWrapped, onRemovePane } = useModel('datadev', (_) => ({
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));

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
  const [form] = Form.useForm();
  const {
    panelProps,
    editorRef,
    getDebugCode,
    setResults,
    setResultHeader,
    setLog,
    handleExpandChange,
  } = useEditorPanel();

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
    form.validateFields().then(() => {
      const values = form.getFieldsValue();
      switch (task?.jobType) {
        case TaskTypes.SQL_SPARK:
        case TaskTypes.SQL_FLINK:
          const dataSql = {
            ...values,
            jobId: pane.id,
            jobType: task?.jobType,
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
            ...values,
            jobId: pane.id,
            jobType: TaskTypes.SPARK_PYTHON,
            resourceHdfsPath: content.resourceHdfsPath,
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
            ...values,
            jobId: pane.id,
            jobType: TaskTypes.SCRIPT_SHELL,
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
            ...values,
            jobId: pane.id,
            jobType: TaskTypes.SCRIPT_SHELL,
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
    });
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
          res.data.statementState !== StatementState.CANCELED &&
          isDebuging
        ) {
          setTimeout(() => {
            fetchPysparkQueryResult({
              sessionId,
              statementId,
            });
          }, 2000);
        } else {
          setIsDebuging(false);
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
          res.data.statementState !== StatementState.CANCELED &&
          isDebuging
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
          setIsDebuging(false);
          pollingFrom.current = 0;
          const result = res.data.resultSet;
          const resultHeader = res.data.resultHeader || [];
          setResults((pre) => [...pre, result]);
          setResultHeader((pre) => [...pre, resultHeader]);
        }
      })
      .catch((err) => {});
  };

  /**
   * 调试选中代码段
   */
  const onDebug = async () => {
    if(isDebuging) {
      setIsDebuging(false);
      setTimeout(() => {
        message.success('已停止调试！')˜
      }, 2000)
    } else {
      setIsDebuging(true);
      const value = getDebugCode();
      if (typeof value !== 'string') {
        return message.error('请选择代码');
      }
      handleExpandChange(true);
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
    }
  
  };

  const tryRun = () => {
    handleExpandChange(true);
    tyrRun({
      jobId: pane.id,
      jobVersion: version,
    }).then((res: any) => {
      genTryRunLog(res.data.sessionId);
    });
  };

  const genTryRunLog = (sessionId: number, from = 0, pageSize = 10) => {
    getTyrRunLog({
      sessionId,
      from,
      pageSize
    }).then(res => {
      setLog(res.data?.log || []);
      const endSign = ['shutting_down', 'error', 'dead', 'killed', 'success'];
      if(!endSign.includes(res.data.state)) {
        setTimeout(() => {
          genTryRunLog(sessionId, res.data.from);
        }, 1000)
      }
    });
  }

  const btnMap = new Map([
    [
      Btns.SAVE,
      <Tooltip title="保存" key="9">
        <Button className={styles.btn} icon={<IconFont type="icon-baocun" />} onClick={onSave} />
      </Tooltip>,
    ],
    [
      Btns.DEBUG,
      <Tooltip title={`${isDebuging ? '停止': '开始'}调试`} key="8">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-tiaoshi" />}
          onClick={onDebug}
        />
      </Tooltip>,
    ],
    [
      Btns.TRY_RUN,
      <Tooltip title="试运行" key="10">
        <Button
          className={styles.btn}
          icon={<IconFont type="icon-shiyunhang" />}
          onClick={tryRun}
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
  ]);

  const getBtnNames = () => {
    switch (task?.jobType) {
      case TaskTypes.SQL_SPARK:
        return [
          Btns.SAVE,
          Btns.DEBUG,
          Btns.TRY_RUN,
          Btns.RUN_ONCE,
          Btns.SUBMIT,
          Btns.ONLINE,
          Btns.OFFLINE,
          Btns.DELETE,
          Btns.DIVIDER,
          Btns.JOB_CONFIG,
        ];
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
          Btns.JOB_CONFIG
        ];
      case TaskTypes.SQL_FLINK:
        return [Btns.SAVE, Btns.SUBMIT, Btns.TRY_RUN, Btns.RUN_ONCE, Btns.DELETE, Btns.DIVIDER, Btns.JOB_CONFIG];
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
            editorRef={editorRef}
            panelProps={panelProps}
            form={form}
            data={{ task, content }}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.SPARK_JAR:
        return (
          <SparkJava form={form} data={content} jobId={content?.jobId || task?.id} />
        );
      case TaskTypes.SPARK_PYTHON:
        return (
          <SparkPython
            editorRef={editorRef}
            panelProps={panelProps}
            form={form}
            data={{ content }}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.SCRIPT_SHELL:
        return <ScriptShell form={form} editorRef={editorRef} data={{ content }} />;
      case TaskTypes.SCRIPT_PYTHON:
        return (
          <ScriptPython
            editorRef={editorRef}
            panelProps={panelProps}
            form={form}
            data={{ content }}
            visible={visibleJobConfig}
            onCancel={() => setVisibleJobConfig(false)}
          />
        );
      case TaskTypes.KYLIN:
        return <Kylin form={form} data={content} />;
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
                refreshTask();
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
