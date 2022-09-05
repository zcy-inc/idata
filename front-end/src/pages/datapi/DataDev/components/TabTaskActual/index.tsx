import React, { useState, Fragment, useRef } from 'react';
import { Button, Form, Input, message, Modal, Select, Space, Skeleton } from 'antd';
import { Tip } from '@/components';
import { useRequest } from 'ahooks';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';
import { IPane } from '@/models/datadev';
import {
  getTaskVersions,
  pauseTask,
  deleteTask,
  submitTask,
  resumeTask,
  getDIJobBasicInfo,
  saveStreamJobContent,
  saveDIJobBasicInfo,
  saveTaskConfig
} from '@/services/datadev';
import { TaskVersion } from '@/types/datadev';
import Basic from './components/Basic';
import Config from './components/Config';
import DrawerVersion from '../TabTask/components/DrawerVersion';
import DrawerHistory from '../TabTask/components/DrawerHistory';
import {
  VersionStatusDisplayMap,
  VersionStatus,
  EnvRunningState,
} from '@/constants/datadev';
import { Environments } from '@/constants/datasource';
import IconRun from '../TabTask/components/IconRun';
import IconPause from '../TabTask/components/IconPause';
import { ModalForm } from '@ant-design/pro-form';
import showDrawer from '@/utils/showDrawer';
import ActualContent from './components/Content'

export interface TabTaskProps {
  pane: IPane;
}

const { confirm } = Modal;
const { Item } = Form;
const { TextArea } = Input;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];

//弹窗ModalForm的title枚举
enum ActionModalTitle {
  "resume"="恢复作业",
  "run"="选择环境",
  "pause"= "暂停作业"
}

type ActionType = keyof typeof ActionModalTitle;

const TabTaskActual: FC<TabTaskProps> = ({ pane }) => {
  const { id: jobId } = pane;

  const contentRef = useRef<{getValues: any}>(null);
  // 当前版本
  const [version, setVersion] = useState<string>('');
  // 获取DI基础信息
  const { data: basicInfo, refresh: refreshBasicInfo, loading: basicInfoLoading } = useRequest(() =>
    getDIJobBasicInfo(jobId)
  );
  // 版本列表
  const { data: versions = [], refresh: refreshVersions } = useRequest(
    () => getTaskVersions({ jobId: pane.id }).then(({ data }) => data),
    {
      onSuccess: (data) => {
        if (typeof data[0]?.version !== 'undefined') {
          setVersion(`${data[0]?.version}#${data[0]?.environment}`);
        }
      },
    },
  );

  // kafka topic 下拉列表
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [actionType, setActionType] = useState<ActionType>('run');
  const [visibleAction, setVisibleAction] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);
  const [visibleVersion, setVisibleVersion] = useState(false);
  const [visibleHistory, setVisibleHistory] = useState(false);

  const { getTreeWrapped, onRemovePane } = useModel('datadev', (_: { onRemovePane: any; getTreeWrapped: any; }) => ({
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));

  // 暂停、恢复、单次运行任务
  const onAction = (type: ActionType) => {
    setActionType(type);
    setVisibleAction(true);
  };

  const showBaseInfo = () => {
    showDrawer('基本信息', {
      formProps: {
        data: basicInfo
      },
      beforeConfirm: (dialog, form, done) => {
        const values = form.onSave();
        dialog.showLoading();
        saveDIJobBasicInfo(values).then(() => {
          message.success('保存成功');
          refreshBasicInfo();
          done();
        }).finally(() => {
          dialog.hideLoading();
        })
      }
    }, Basic)
  }

  const showConfig = () => {
    showDrawer('配置', {
      formProps: {
        data: basicInfo
      },
      drawerProps: {
        width: 780
      },
      beforeConfirm: (dialog, form, done) => {
        const { values, activeKey } = form.getValues();
        dialog.showLoading();
        saveTaskConfig({ jobId: pane.id, environment: activeKey }, values).then(() => {
          message.success('保存成功');
          refreshBasicInfo();
          done();
        }).finally(() => {
          dialog.hideLoading();
        })
      }
    }, Config)
  }

  // 删除任务
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

  // 保存任务内容
  const onSave = () => {
    contentRef.current?.getValues().then(async (jobContent: any) => {
      const params = {
        ...jobContent,
        tableDtoList: jobContent.tableDtoList.map((item: any) => ({
          ...item,
          srcTable: item.srcTable.inputMode === "E" ? `${item.srcTable.rawTable}[${item.tableIdxBegin}-${item.tableIdxEnd}]` : item.srcTable.rawTable
        })),
        enableSharding: jobContent.enableSharding ? 1 : 0,
        jobId: pane.id,
      };
      const { success } = await saveStreamJobContent(params);
      if (success) {
        message.success('保存成功');
        refreshVersions();
      }
    })
  };

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
    <Skeleton loading={basicInfoLoading}>
      <div className={styles['task-actual']}>
        <Tip
          label="当前版本"
          className={styles.toolbar}
          content={
            versions.length === 0 ? (
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
                  value: `${_.version}#${_.environment}`,
                }))}
                value={version}
                onChange={(v) => setVersion(v)}
                showSearch
                filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
              />
            )
          }
          extra={
            <Space size={16}>
              <a onClick={showBaseInfo}>基本信息</a>
              <a onClick={showConfig}>配置</a>
              <a onClick={() => setVisibleVersion(true)}>版本</a>
              <a onClick={() => setVisibleHistory(true)}>历史</a>
            </Space>
          }
        />
        <ActualContent ref={contentRef} jobId={pane.id} version={version?.split('#')[0]} basicInfo={basicInfo} />
      </div>
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
          <Button
            key="stag"
            size="large"
            type="primary"
            onClick={() => setVisible(true)}
            disabled={versions?.length === 0}
          >
            提交
          </Button>
        </Space>
        <ModalForm
          title="提交"
          layout="horizontal"
          width={536}
          labelCol={{ span: 6 }}
          colon={false}
          preserve={false}
          modalProps={{ destroyOnClose: true, onCancel: () => setVisible(false) }}
          visible={visible}
          submitter={{
            submitButtonProps: { size: 'large', loading },
            resetButtonProps: { size: 'large' },
          }}
          onFinish={async (values) => {
            setLoading(true);
            submitTask(
              {
                jobId: pane.id,
                version: +version?.split('#')[0],
                env: values.env,
              },
              {
                remark: values.remark,
              },
            )
              .then((res) => {
                if (res.success) {
                  message.success('提交成功');
                  refreshVersions();
                  setVisible(false);
                  return true;
                } else {
                  return false;
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
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
        <ModalForm
          title={ActionModalTitle[actionType]}
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
                    refreshBasicInfo();
                    refreshVersions();
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
                    refreshBasicInfo();
                    refreshVersions();
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
      </div>
      {!!basicInfo && (
        <Fragment>
          <DrawerVersion
            visible={visibleVersion}
            onClose={() => setVisibleVersion(false)}
            data={basicInfo}
          />
          <DrawerHistory
            visible={visibleHistory}
            onClose={() => setVisibleHistory(false)}
            data={basicInfo}
          />
        </Fragment>
      )}
    </Skeleton>
  );
};

export default TabTaskActual;
