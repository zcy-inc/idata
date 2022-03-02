import React, { useEffect, useRef, useState, Fragment } from 'react';
import { Button, Form, Input, message, Modal, Radio, Select, Space } from 'antd';
import RadioGroup, { Option } from '@/components/RadioGroup';
import { useRequest } from 'ahooks';
import { useModel } from 'umi';
import { get, cloneDeep } from 'lodash';
import type { FC } from 'react';
import styles from './index.less';
import { IPane } from '@/models/datadev';
import {
  getTaskTables,
  getTaskVersions,
  pauseTask,
  deleteTask,
  submitTask,
  getTaskTableColumns,
  runTask,
  resumeTask,
  getDIJobBasicInfo,
  saveDIJobContent,
  getDIJobContent,
  genMergeSQL,
} from '@/services/datadev';
import { MappedColumn, TaskTable, TaskVersion } from '@/types/datadev';
import Title from '@/components/Title';
import DrawerBasic from './components/DrawerBasic';
import DrawerConfig from './components/DrawerConfig';
import DrawerVersion from './components/DrawerVersion';
import DrawerHistory from './components/DrawerHistory';
import Mapping from './components/Mapping';
import {
  SrcReadMode,
  DestWriteMode,
  VersionStatusDisplayMap,
  VersionStatus,
  EnvRunningState,
  DIJobType,
  diConfigOptions,
  DIConfigMode,
} from '@/constants/datadev';
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

const getDataSourceOptions = async (type: DataSourceTypes) => {
  const res = await getDataSourceList({ type, limit: 10000, offset: 0 });
  return (get(res, 'data.content', []) as DataSourceItem[]).map(({ name, id }) => ({
    label: name,
    value: id,
  }));
};

// 表选择组件
const TableSelect: FC<{
  options: TaskTable[];
  onRefresh: () => void;
  value?: string;
  onChange?: (value?: string) => void;
  showRefresh?: boolean;
}> = ({ options, onRefresh, value, onChange, showRefresh }) => {
  const innerVal = value?.split(',');
  const handleChange = (val?: string[]) => {
    const parsedVal = Array.isArray(val) && val.length > 0 ? val?.join(',') : undefined;
    onChange?.(parsedVal);
  };
  return (
    <Fragment>
      <Select
        size="large"
        mode="multiple"
        style={{ maxWidth, minWidth }}
        placeholder="请选择"
        options={options.map((_) => ({ label: _.tableName, value: _.tableName }))}
        value={innerVal}
        onChange={handleChange}
        optionFilterProp="label"
        showSearch
        filterOption={(input, option) => {
          const label = get(option, 'label', '') as string;
          return label.indexOf(input) >= 0;
        }}
      />
      {showRefresh && <a className={styles.refresh} onClick={onRefresh}></a>}
    </Fragment>
  );
};

// 表输入组件
const TableInput: FC<{
  value?: string;
  onChange?: React.ChangeEventHandler<HTMLInputElement>;
  showRefresh?: boolean;
  onRefresh: () => void;
}> = ({ showRefresh, value, onChange, onRefresh }) => {
  return (
    <Fragment>
      <Input size="large" style={{ maxWidth, minWidth }} value={value} onChange={onChange} />
      {showRefresh && <a className={styles.refresh} onClick={onRefresh}></a>}
    </Fragment>
  );
};

const TabTask: FC<TabTaskProps> = ({ pane }) => {
  const { id: jobId } = pane;
  // 获取DI基础信息
  const { data: basicInfo, refresh: refreshBasicInfo } = useRequest(() => getDIJobBasicInfo(jobId));
  // 当前版本
  const [version, setVersion] = useState<number>();
  // 版本列表
  const { data: versions = [], refresh: refreshVersions } = useRequest(
    () => getTaskVersions({ jobId: pane.id }).then(({ data }) => data),
    {
      onSuccess: (data) => {
        if (typeof version === 'undefined' && typeof data[0]?.version !== 'undefined') {
          setVersion(data[0]?.version);
        }
      },
    },
  );
  // 获取数据源类型下拉列表
  const { data: dataSourceOptions } = useRequest(() =>
    getDataSourceTypes().then(({ data }) => data.map((item) => ({ label: item, value: item }))),
  );
  // 数据来源-数据源名称 下拉列表
  const { data: srcDSOptions, run: getSrcDSOptions } = useRequest(getDataSourceOptions, {
    manual: true,
  });
  // 数据去向-数据源名称 下拉列表
  const { data: destDSOptions, run: getDestDSOptions } = useRequest(getDataSourceOptions, {
    manual: true,
  });
  // 数据来源-表 下拉列表
  const { data: { data: srcTableOptions = [] } = {}, run: getSrcTableOptions } = useRequest(
    getTaskTables,
    {
      manual: true,
    },
  );
  const [srcColumns, setSrcColumns] = useState<MappedColumn[]>([]);
  const [destColumns, setDestColumns] = useState<MappedColumn[]>([]);
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [actionType, setActionType] = useState('');
  const [visibleAction, setVisibleAction] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);

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
  const formInitialValues = {
    srcReadMode: SrcReadMode.ALL,
    destWriteMode: DestWriteMode.INIT,
    configMode: DIConfigMode.VISUALIZATION,
  };
  const [form] = Form.useForm();
  const [jobContent, setJobContent] = useState<Record<string, any>>({});
  const {
    srcDataSourceType,
    srcDataSourceId,
    srcReadMode,
    srcTables,
    destDataSourceType,
    configMode,
  } = jobContent;

  const handleFormValuesChange = (_: unknown, allValues: Record<string, unknown>) => {
    setJobContent(Object.assign({}, jobContent, allValues));
  };

  // 获取作业内容
  useEffect(() => {
    (async () => {
      if (typeof jobId !== 'undefined' && typeof version !== 'undefined') {
        const { data } = await getDIJobContent({ jobId, version });
        setJobContent(data);
        setSrcColumns(data.srcCols);
        setDestColumns(data.destCols);
        form.setFieldsValue(data);
      }
    })();
  }, [jobId, version]);

  // 数据来源-数据源类型改变: 1. 清空`数据源名称` 和 `表`; 2. 刷新`数据源名称`下拉列表
  useEffect(() => {
    if (srcDataSourceType) {
      getSrcDSOptions(srcDataSourceType);
    }
  }, [form, srcDataSourceType]);

  // 数据去向-数据源类型改变: 1. 清空`数据源名称`; 2. 刷新`数据源名称`下拉列表
  useEffect(() => {
    if (destDataSourceType) {
      getDestDSOptions(destDataSourceType);
    }
  }, [form, destDataSourceType]);

  // 数据来源-数据源名称改变: 1. 清空`表`; 2. 刷新`表`下拉列表
  useEffect(() => {
    if (typeof srcDataSourceType !== 'undefined' && typeof srcDataSourceId !== 'undefined') {
      getSrcTableOptions({
        dataSourceId: srcDataSourceId,
        dataSourceType: srcDataSourceType,
      });
    }
  }, [form, srcDataSourceId, srcDataSourceType]);

  // 数据去向-数据源名称改变: 1. 清空`表`  2. 刷新`表`下拉列表
  // useEffect(() => {
  //   const destDataSourceType = form.getFieldValue('destDataSourceType');
  //   if (typeof destDataSourceType !== 'undefined' && typeof destDataSourceId !== 'undefined') {
  //     form.resetFields(['destTable']);
  //     getDestTableOptions({
  //       dataSourceId: srcDataSourceId,
  //       dataSourceType: destDataSourceType,
  //     });
  //   }
  // }, [form, destDataSourceId]);

  // 刷新mergeSQL
  const refreshMergeSQL = async () => {
    const {
      srcDataSourceType,
      destTable,
      srcReadMode,
      srcTables,
      scriptSelectColumns,
      scriptKeyColumns,
      scriptMergeSqlParamDto,
    } = jobContent;
    const { data, success } = await genMergeSQL({
      destTable,
      srcReadMode,
      selectColumns: scriptSelectColumns,
      keyColumns: scriptKeyColumns,
      sourceTable: srcTables,
      driverType: srcDataSourceType,
      days: scriptMergeSqlParamDto?.recentDays,
    });
    if (success) {
      form.setFieldsValue({
        scriptMergeSql: data,
      });
    }
  };

  // 刷新可视化视图
  const refreshVisualise = async () => {
    const tables = srcTables?.split(',') || [];
    if (tables.length === 0) {
      return message.info('请选择表');
    }
    const { success, data } = await getTaskTableColumns({
      tableName: tables[0],
      dataSourceType: srcDataSourceType,
      dataSourceId: srcDataSourceId,
    });
    if (success) {
      message.success('刷新成功');
      setSrcColumns(data);
      setDestColumns([]);
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
    // const srcFormValues = srcForm.getFieldsValue();
    // const destFormValues = destForm.getFieldsValue();
    const values = form.getFieldsValue();
    const params = {
      // ...srcFormValues,
      // ...destFormValues,
      ...values,
      // srcTables: values.srcTables?.join(','),
      id: pane.id, // FIXME: id和jobId 不同
      jobId: pane.id,
      version: jobContent?.version,
      srcCols,
      destCols,
      contentHash: jobContent?.contentHash,
    };
    saveDIJobContent(params)
      .then((res) => {
        if (res.success) {
          message.success('保存成功');
          refreshVersions();
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

  const onBeforeConfigModeChange = (_: any, { label }: Option) =>
    new Promise((resolve) => {
      confirm({
        title: '提示',
        content: `您确定要转化为${label}吗? 转化将不保存之前操作!`,
        onOk: () => {
          // 切换时清空脚本模式下输入内容
          form.resetFields([
            'scriptSelectColumns',
            'scriptKeyColumns',
            'scriptMergeSqlParamDto',
            'mergeSql',
          ]);
          return resolve('');
        },
      });
    });

  const srcTablesNode =
    basicInfo?.jobType === DIJobType.DI ? (
      <TableSelect showRefresh options={srcTableOptions} onRefresh={refreshVisualise} />
    ) : (
      <TableInput showRefresh onRefresh={refreshVisualise} />
    );
  const srcItemsNode = (
    <Fragment>
      <Item name="srcDataSourceType" label="数据源类型" rules={ruleSlct}>
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={dataSourceOptions}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          onChange={() => form.resetFields(['srcDataSourceId', 'srcTables'])}
        />
      </Item>
      <Item name="srcDataSourceId" label="数据源名称" rules={ruleSlct}>
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={srcDSOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
          onChange={() => form.resetFields(['srcTables'])}
        />
      </Item>
      <Item name="srcTables" label="表" rules={ruleSlct} style={{ width: '100%' }}>
        {srcTablesNode}
      </Item>
      <Item name="srcReadFilter" label="数据过滤">
        <TextArea style={{ maxWidth, minWidth }} placeholder="请参考相应SQL语法填写where过滤语句" />
      </Item>
      {basicInfo?.jobType === DIJobType.DI && (
        <Fragment>
          <Item name="srcReadShardKey" label="切分键">
            <Input
              size="large"
              style={{ maxWidth, minWidth }}
              placeholder="根据配置的字段进行数据分片，实现并发读取"
            />
          </Item>
          <Item name="srcShardingNum" label="切分键分片数">
            <Select
              size="large"
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={[1, 2, 4, 8, 16].map((_) => ({ label: _, value: _ }))}
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
        </Fragment>
      )}
    </Fragment>
  );
  const destWriteModeItem = (
    <Item name="destWriteMode" label="写入模式" rules={ruleSlct}>
      <Radio.Group
        options={[
          { label: '新建表', value: DestWriteMode.INIT },
          { label: '覆盖表', value: DestWriteMode.OVERWRITE },
          { label: '追加表', value: DestWriteMode.APPEND },
        ]}
      />
    </Item>
  );
  const showDestTableRefresh = basicInfo?.jobType !== DIJobType.DI;
  const destItemsNode = (
    <Fragment>
      <Item name="destDataSourceType" label="数据源类型" rules={ruleSlct}>
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={dataSourceOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
          onChange={() => form.resetFields(['destDataSourceId'])}
        />
      </Item>
      <Item name="destDataSourceId" label="数据源名称" rules={ruleSlct}>
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={destDSOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
        />
      </Item>
      <Item name="destTable" label="表" rules={ruleText}>
        {/* TODO: */}
        <TableInput showRefresh={showDestTableRefresh} onRefresh={() => {}} />
      </Item>
      {basicInfo?.jobType === DIJobType.DI && (
        <Fragment>
          <Item name="destBeforeWrite" label="导入前准备语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据前执行的SQL脚本" />
          </Item>
          <Item name="destAfterWrite" label="导入后完成语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据后执行的SQL脚本" />
          </Item>
          {destWriteModeItem}
        </Fragment>
      )}
    </Fragment>
  );
  const scriptItemsNode = (
    <div className={styles.form}>
      <Item label="字段" name="scriptSelectColumns">
        <TextArea placeholder="请输入" autoSize={{ minRows: 5 }} />
      </Item>
      <div className={styles.tip}>
        <div>
          <span className={styles['tip-title']}>示例</span>
          announcement_id,
        </div>
        <div className={styles['tip-second']}>
          convert(meta_data USING utf8) as meta_data 字段命名须与源表名称保持一致
        </div>
      </div>
      <Item label="去向表关键键" name="scriptKeyColumns">
        <TextArea placeholder="若获取关键键失败，则默认显示id。" autoSize={{ minRows: 5 }} />
      </Item>
      {srcReadMode === SrcReadMode.INCREMENTAL && (
        <Fragment>
          <Item label="抽取天数" name={['scriptMergeSqlParamDto', 'recentDays']}>
            <Input placeholder="单位为天，可输入数值范围为1-7" />
          </Item>
          <Item
            label={
              <Fragment>
                <span style={{ marginRight: 8 }}>mergeSQL</span>
                <a onClick={refreshMergeSQL} style={{ fontWeight: 'normal' }}>
                  刷新
                </a>
              </Fragment>
            }
            name="scriptMergeSql"
          >
            <TextArea autoSize={{ minRows: 5 }} disabled />
          </Item>
        </Fragment>
      )}
    </div>
  );

  return (
    <Fragment>
      <div className={styles.task}>
        <div className={styles.toolbar}>
          <div className={styles.version}>
            <span>当前版本: </span>
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
                onChange={(v) => setVersion(v)}
                showSearch
                filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
              />
            )}
          </div>
          <Space size={16}>
            <a onClick={() => onAction('run')}>单次运行</a>
            <a onClick={() => setVisibleBasic(true)}>基本信息</a>
            <a onClick={() => setVisibleConfig(true)}>配置</a>
            <a onClick={() => setVisibleVersion(true)}>版本</a>
            <a onClick={() => setVisibleHistory(true)}>历史</a>
          </Space>
        </div>
        <Form
          form={form}
          layout="horizontal"
          colon={false}
          initialValues={formInitialValues}
          onValuesChange={handleFormValuesChange}
        >
          <div className={styles['form-box']}>
            <div className={styles['form-box-item']}>
              <Title>数据来源</Title>
              {srcItemsNode}
            </div>
            <div className={styles['form-box-item']}>
              <Title>数据去向</Title>
              {destItemsNode}
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
                <Item name="configMode" rules={ruleSlct} noStyle>
                  <RadioGroup options={diConfigOptions} onBeforeChange={onBeforeConfigModeChange} />
                </Item>
              </Space>
            </div>
          </Title>
          {configMode === DIConfigMode.VISUALIZATION ? (
            <div style={{ position: 'relative' }}>
              <Space style={{ position: 'absolute', top: 16, right: 16, zIndex: 1 }}>
                <Button className="normal" onClick={onCancelMapping}>
                  取消映射
                </Button>
                <Button className="normal" onClick={onEponymousMapping}>
                  同名映射
                </Button>
              </Space>
              <Mapping ref={mapRef} data={{ srcColumns, destColumns }} />
            </div>
          ) : (
            scriptItemsNode
          )}
        </Form>
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
                version: jobContent?.version as number,
                env: values.env,
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
                    refreshBasicInfo();
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
                    message.success('单次运行成功');
                    refreshBasicInfo();
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
          <DrawerBasic
            visible={visibleBasic}
            onClose={() => setVisibleBasic(false)}
            data={basicInfo}
            pane={pane}
            refreshBasicInfo={refreshBasicInfo}
          />
          <DrawerConfig
            visible={visibleConfig}
            onClose={() => setVisibleConfig(false)}
            data={basicInfo}
          />
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
    </Fragment>
  );
};

export default TabTask;
