import React, { useEffect, useRef, useState } from 'react';
import { Button, Form, Input, message, Modal, Radio, Select, Space } from 'antd';
import { useModel } from 'umi';
import { get, cloneDeep } from 'lodash';
import type { FC } from 'react';
import styles from './index.less';

import { IPane } from '@/models/datadev';
import {
  getTaskContent,
  getTaskTables,
  getTaskVersions,
  disableTask,
  enableTask,
  getTask,
  deleteTask,
  saveTask,
  submitTask,
  getTaskTableColumns,
} from '@/services/datadev';
import { MappedColumn, Task, TaskContent, TaskTable, TaskVersion } from '@/types/datadev';

import Title from '@/components/Title';
import DrawerBasic from './components/DrawerBasic';
import DrawerConfig from './components/DrawerConfig';
import DrawerVersion from './components/DrawerVersion';
import DrawerHistory from './components/DrawerHistory';
import Mapping from './components/Mapping';
import { SrcReadMode, DestWriteMode, VersionStatusDisplayMap } from '@/constants/datadev';
import { getDataSourceList, getDataSourceTypes } from '@/services/datasource';
import { DataSourceTypes, Environments } from '@/constants/datasource';
import { DataSourceItem } from '@/types/datasource';
import IconRun from './components/IconRun';
import IconPause from './components/IconPause';
import { ModalForm } from '@ant-design/pro-form';

export interface TabTaskProps {
  pane: IPane;
}

const { confirm } = Modal;
const { Item } = Form;
const { TextArea } = Input;
const maxWidth = 400;
const minWidth = 200;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];

const RefreshSelect: FC<{
  srcTables: TaskTable[];
  getTaskTableColumnsWrapped: () => void;
  value?: string[];
  onChange?: (value: string[]) => void;
}> = ({ srcTables, getTaskTableColumnsWrapped, value, onChange }) => {
  return (
    <>
      <Select
        size="large"
        mode="multiple"
        style={{ maxWidth, minWidth }}
        placeholder="请选择"
        optionFilterProp="label"
        filterOption={(input, option) => {
          const label = get(option, 'label', '') as string;
          return label.indexOf(input) >= 0;
        }}
        options={srcTables.map((_) => ({ label: _.tableName, value: _.tableName }))}
        value={value}
        onChange={onChange}
      />
      <a className={styles.refresh} onClick={getTaskTableColumnsWrapped}></a>
    </>
  );
};

const TabTask: FC<TabTaskProps> = ({ pane }) => {
  const [task, setTask] = useState<Task>();
  const [versions, setVersions] = useState<TaskVersion[]>([]);
  const [version, setVersion] = useState<number | undefined>(-1);
  const [DSTypes, setDSTypes] = useState<string[]>([]);
  const [DSSrcList, setDSSrcList] = useState<DataSourceItem[]>([]);
  const [DSDestList, setDSDestList] = useState<DataSourceItem[]>([]);
  const [srcTables, setSrcTables] = useState<TaskTable[]>([]);
  const [data, setData] = useState<TaskContent>();
  const [srcColumns, setSrcColumns] = useState<MappedColumn[]>([]);
  const [destColumns, setDestColumns] = useState<MappedColumn[]>([]);
  const [visible, setVisible] = useState(false);
  const [publishEnv, setPublishEnv] = useState<Environments>(Environments.STAG);
  const [loading, setLoading] = useState(false);

  const [srcForm] = Form.useForm();
  const [destForm] = Form.useForm();
  const mapRef = useRef<{
    srcMap: { [key: string]: any };
    destMap: { [key: string]: any };
  }>(null);

  const [visibleBasic, setVisibleBasic] = useState(false);
  const [visibleConfig, setVisibleConfig] = useState(false);
  const [visibleVersion, setVisibleVersion] = useState(false);
  const [visibleHistory, setVisibleHistory] = useState(false);

  const { getTreeWrapped, onRemovePane } = useModel('datadev', (_) => ({
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    if (pane) {
      getTaskWrapped();
      getTaskVersionsWrapped();
      getDataSourceTypesWrapped();
    }
  }, [pane.id]);

  useEffect(() => {
    if (version && version > 0) {
      getTaskContentWrapped();
    }
  }, [version]);

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
  const getTaskContentWrapped = () =>
    getTaskContent({ jobId: pane.id, version: version as number })
      .then((r) => {
        const srcData = {
          srcDataSourceType: r.data.srcDataSourceType,
          srcDataSourceId: r.data.srcDataSourceId,
          srcTables: r.data.srcTables.split(','),
          srcReadFilter: r.data.srcReadFilter,
          srcReadShardKey: r.data.srcReadShardKey,
          srcReadMode: r.data.srcReadMode,
        };
        const destData = {
          destDataSourceType: r.data.destDataSourceType,
          destDataSourceId: r.data.destDataSourceId,
          destTable: r.data.destTable,
          destBeforeWrite: r.data.destBeforeWrite,
          destAfterWrite: r.data.destAfterWrite,
          destWriteMode: r.data.destWriteMode,
        };
        getDataSourceListWrapped(srcData.srcDataSourceType, 'src');
        getDataSourceListWrapped(destData.destDataSourceType, 'dest');
        srcForm.setFieldsValue(srcData);
        destForm.setFieldsValue(destData);
        setData(r.data);
        setSrcColumns(r.data.srcCols);
        setDestColumns(r.data.destCols);
      })
      .catch((err) => {});

  /**
   * 获取数据源类型
   */
  const getDataSourceTypesWrapped = () =>
    getDataSourceTypes()
      .then((res) => setDSTypes(res.data))
      .catch((err) => {});

  /**
   * 获取该数据源类型下的数据源列表
   */
  const getDataSourceListWrapped = (type: DataSourceTypes, dir: 'src' | 'dest') => {
    if (dir === 'src') {
      // 修改来源类型时置空来源数据源和表
      srcForm.setFieldsValue({ srcDataSourceId: null, srcTables: [] });
    }
    if (dir === 'dest') {
      // 修改目标类型时置空目标数据源
      destForm.setFieldsValue({ destDataSourceId: null });
    }
    getDataSourceList({ type, limit: 10000, offset: 0 })
      .then((res) => {
        const content = get(res, 'data.content', []);
        dir === 'src' && setDSSrcList(content);
        dir === 'dest' && setDSDestList(content);
      })
      .catch((err) => {});
  };

  /**
   * 获取该数据源下的表
   */
  const getTaskTablesWrapped = (dataSourceId: number) => {
    // 置空来源表的表单项
    srcForm.setFieldsValue({ srcTables: [] });
    const dataSourceType = srcForm.getFieldValue('srcDataSourceType');
    getTaskTables({ dataSourceType, dataSourceId })
      .then((res) => setSrcTables(res.data))
      .catch((err) => {});
  };

  /**
   * 获取该表下的字段
   */
  const getTaskTableColumnsWrapped = () => {
    let tables = srcForm.getFieldValue('srcTables');
    if (!Array.isArray(tables)) {
      tables = [];
    }
    if (tables.length) {
      getTaskTableColumns({
        tableName: tables[0],
        dataSourceType: srcForm.getFieldValue('srcDataSourceType'),
        dataSourceId: srcForm.getFieldValue('srcDataSourceId'),
      })
        .then((res) => {
          if (res.success) {
            message.success('刷新成功');
            setSrcColumns(res.data);
            setDestColumns([]);
          } else {
            message.error(`刷新失败：${res.msg}`);
          }
        })
        .catch((err) => {});
    } else {
      message.info('请选择表');
    }
  };

  /**
   * 暂停任务
   */
  const onPause = () => {
    disableTask({ id: pane.id })
      .then((res) => {
        if (res.success) {
          message.success('暂停成功');
          getTaskWrapped();
        } else {
          message.error(`暂停失败：${res.msg}`);
        }
      })
      .catch((err) => {});
  };

  /**
   * 启用任务
   */
  const onRun = () => {
    enableTask({ id: pane.id })
      .then((res) => {
        if (res.success) {
          message.success('启用成功');
          getTaskWrapped();
        } else {
          message.error(`启用失败：${res.msg}`);
        }
      })
      .catch((err) => {});
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
   * 保存任务内容
   */
  const onSave = () => {
    const srcMap = mapRef.current?.srcMap || {};
    const destMap = mapRef.current?.destMap || {};
    const srcCols: MappedColumn[] = Object.values(srcMap);
    const destCols: MappedColumn[] = Object.values(destMap);
    const srcFormValues = srcForm.getFieldsValue();
    const destFormValues = destForm.getFieldsValue();
    const params = {
      ...srcFormValues,
      ...destFormValues,
      srcTables: srcFormValues.srcTables.join(','),
      id: pane.id,
      jobId: pane.id,
      version: data?.version,
      srcCols,
      destCols,
      contentHash: data?.contentHash,
    };
    saveTask(params)
      .then((res) => {
        if (res.success) {
          message.success('保存成功');
          getTaskVersionsWrapped();
        } else {
          message.error(`保存失败：${res.msg}`);
        }
      })
      .catch((err) => {});
  };

  /**
   * 取消映射
   */
  const onCancelMapping = () => {
    srcColumns.forEach((_) => delete _.mappedColumn);
    destColumns.forEach((_) => delete _.mappedColumn);
    setSrcColumns([...srcColumns]);
    setDestColumns([...destColumns]);
  };

  /**
   * 同行映射
   */
  const onEponymousMapping = () => {
    const cloneSrcColumns = cloneDeep(srcColumns);
    const cloneDestColumns = cloneDeep(destColumns);
    srcColumns.forEach((_, i) => (_.mappedColumn = cloneDeep(cloneDestColumns[i])));
    destColumns.forEach((_, i) => (_.mappedColumn = cloneDeep(cloneSrcColumns[i])));
    setSrcColumns([...srcColumns]);
    setSrcColumns([...destColumns]);
  };

  return (
    <>
      <div className={styles.task}>
        <div className={styles.toolbar}>
          <div className={styles.version}>{`当前版本：${data?.versionDisplay || '-'}`}</div>
          <Space size={16}>
            <a onClick={() => setVisibleBasic(true)}>基本信息</a>
            <a onClick={() => setVisibleConfig(true)}>配置</a>
            <a onClick={() => setVisibleVersion(true)}>版本</a>
            <a onClick={() => setVisibleHistory(true)}>历史</a>
          </Space>
        </div>
        <Title style={{ marginTop: 16 }}>版本切换</Title>
        <Select
          size="large"
          style={{ width: maxWidth }}
          placeholder="请选择"
          disabled={versions.length <= 0}
          options={versions.map((_) => ({
            label: `${_.versionDisplay}${_.environment ? '-' + _.environment : ''}-${
              VersionStatusDisplayMap[_.versionStatus]
            }`,
            value: _.version,
          }))}
          value={version}
          onChange={(v) => setVersion(v as number)}
        />
        <div className={styles['form-box']}>
          <div className={styles['form-box-item']}>
            <Title>数据来源</Title>
            <Form
              form={srcForm}
              layout="horizontal"
              colon={false}
              initialValues={{ srcReadMode: SrcReadMode.ALL }}
            >
              <Item name="srcDataSourceType" label="数据源类型" rules={ruleSlct}>
                <Select
                  size="large"
                  style={{ maxWidth, minWidth }}
                  placeholder="请选择"
                  options={DSTypes.map((_) => ({ label: _, value: _ }))}
                  onChange={(v) => getDataSourceListWrapped(v as DataSourceTypes, 'src')}
                />
              </Item>
              <Item name="srcDataSourceId" label="数据源名称" rules={ruleSlct}>
                <Select
                  size="large"
                  style={{ maxWidth, minWidth }}
                  placeholder="请选择"
                  options={DSSrcList.map((_) => ({ label: _.name, value: _.id }))}
                  onChange={(v) => getTaskTablesWrapped(v as number)}
                />
              </Item>
              <Item name="srcTables" label="表" rules={ruleSlct} style={{ width: '100%' }}>
                <RefreshSelect
                  srcTables={srcTables}
                  getTaskTableColumnsWrapped={getTaskTableColumnsWrapped}
                />
              </Item>
              {/* 这个位置放动态增删的表单，后续版本有需要可以从 ./FormList 里复制 */}
              <Item name="srcReadFilter" label="数据过滤">
                <TextArea
                  style={{ maxWidth, minWidth }}
                  placeholder="请参考相应SQL语法填写where过滤语句"
                />
              </Item>
              <Item name="srcReadShardKey" label="切分键">
                <Input
                  size="large"
                  style={{ maxWidth, minWidth }}
                  placeholder="根据配置的字段进行数据分片，实现并发读取"
                />
              </Item>
              <Item name="srcReadMode" label="读取模式" rules={ruleSlct}>
                <Radio.Group
                  options={[
                    { label: '全量', value: SrcReadMode.ALL },
                    { label: '增量', value: SrcReadMode.INCREMENTAL },
                  ]}
                />
              </Item>
            </Form>
          </div>
          <div className={styles['form-box-item']}>
            <Title>数据去向</Title>
            <Form
              form={destForm}
              layout="horizontal"
              colon={false}
              initialValues={{ destWriteMode: DestWriteMode.INIT }}
            >
              <Item name="destDataSourceType" label="数据源类型" rules={ruleSlct}>
                <Select
                  size="large"
                  style={{ maxWidth, minWidth }}
                  placeholder="请选择"
                  options={DSTypes.map((_) => ({ label: _, value: _ }))}
                  onChange={(v) => getDataSourceListWrapped(v as DataSourceTypes, 'dest')}
                />
              </Item>
              <Item name="destDataSourceId" label="数据源名称" rules={ruleSlct}>
                <Select
                  size="large"
                  style={{ maxWidth, minWidth }}
                  placeholder="请选择"
                  options={DSDestList.map((_) => ({ label: _.name, value: _.id }))}
                />
              </Item>
              <Item name="destTable" label="表" rules={ruleText}>
                <Input size="large" style={{ maxWidth, minWidth }} placeholder="请选择" />
              </Item>
              <Item name="destBeforeWrite" label="导入前准备语句">
                <TextArea
                  style={{ maxWidth, minWidth }}
                  placeholder="请输入导入数据前执行的SQL脚本"
                />
              </Item>
              <Item name="destAfterWrite" label="导入后完成语句">
                <TextArea
                  style={{ maxWidth, minWidth }}
                  placeholder="请输入导入数据后执行的SQL脚本"
                />
              </Item>
              <Item name="destWriteMode" label="写入模式" rules={ruleSlct}>
                <Radio.Group
                  options={[
                    { label: '新建表', value: DestWriteMode.INIT },
                    { label: '覆盖表', value: DestWriteMode.OVERRIDE },
                  ]}
                />
              </Item>
            </Form>
          </div>
        </div>
        <Title>
          <div className={styles['string-map-title']}>
            <span>字段映射</span>
            <Space>
              <Button
                className="normal"
                onClick={() => {
                  const newColumns = cloneDeep(srcColumns);
                  setDestColumns(newColumns);
                }}
              >
                复制来源表
              </Button>
              <Button className="normal" onClick={onCancelMapping}>
                取消映射
              </Button>
              <Button onClick={onEponymousMapping}>同名映射</Button>
            </Space>
          </div>
        </Title>
        <Mapping ref={mapRef} data={{ srcColumns, destColumns }} />
        <DrawerBasic
          visible={visibleBasic}
          onClose={() => setVisibleBasic(false)}
          data={task}
          pane={pane}
          getTaskWrapped={getTaskWrapped}
        />
        <DrawerConfig visible={visibleConfig} onClose={() => setVisibleConfig(false)} data={task} />
        <DrawerVersion
          visible={visibleVersion}
          onClose={() => setVisibleVersion(false)}
          data={task}
        />
        <DrawerHistory visible={visibleHistory} onClose={() => setVisibleHistory(false)} />
      </div>
      <div className="workbench-submit">
        <Space>
          {task?.status === 0 && <IconRun onClick={onRun} />}
          {task?.status === 1 && <IconPause onClick={onPause} />}
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
            onClick={() => {
              setVisible(true);
              setPublishEnv(Environments.STAG);
            }}
          >
            提交预发
          </Button>
          <Button
            key="prod"
            size="large"
            type="primary"
            onClick={() => {
              setVisible(true);
              setPublishEnv(Environments.PROD);
            }}
          >
            提交真线
          </Button>
        </Space>
        <ModalForm
          title={`提交${publishEnv === Environments.STAG ? '预发' : '真线'}`}
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
                version: data?.version as number,
                env: publishEnv,
              },
              {
                remark: values.remark,
              },
            )
              .then((res) => {
                if (res.success) {
                  message.success('提交成功');
                  setVisible(false);
                  return true;
                } else {
                  message.error(`提交失败：${res.msg}`);
                  return false;
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
          }}
        >
          <Item name="remark" label="变更说明" rules={ruleText} style={{ marginBottom: 0 }}>
            <TextArea placeholder="请输入" />
          </Item>
        </ModalForm>
      </div>
    </>
  );
};

export default TabTask;
