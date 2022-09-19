import React, { useEffect, useRef, useState, Fragment } from 'react';
import { Button, Form, Input, message, Modal, Radio, Select, Space } from 'antd';
import { MapInput, RadioGroup, TableSelectInput, Tip, Title } from '@/components';
import type { Option } from '@/components/RadioGroup';
import { useRequest } from 'ahooks';
import { useModel } from 'umi';
import { get, cloneDeep } from 'lodash';
import type { FC } from 'react';
import { useDataSource } from '@/hooks';
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
  saveDIJobContent,
  getDIJobContent,
  genMergeSQL,
  getKafkaTopics,
  getDbNames,
  getTableNames,
  getColumns,
  getWriteModeEnum,
  saveDIJobBasicInfo,
  saveTaskConfig
} from '@/services/datadev';
import { MappedColumn, TaskTable, TaskVersion, DIJobBasicInfo } from '@/types/datadev';
import Basic from '../../../TabTaskActual/components/Basic';
import Config from '../../../TabTaskActual/components/Config';
import DrawerVersion from '../DrawerVersion';
import DrawerHistory from '../../../TabDev/components/DrawerHistory';
import Mapping from '../Mapping';
import {
  SrcReadMode,
  DestWriteMode,
  VersionStatusDisplayMap,
  VersionStatus,
  EnvRunningState,
  DIJobType,
  diConfigOptions,
  DIConfigMode,
  shardingNumOptions,
  DataSourceType,
} from '@/constants/datadev';
import { Environments } from '@/constants/datasource';
import IconRun from '../IconRun';
import IconPause from '../IconPause';
import { ModalForm } from '@ant-design/pro-form';
import { DefaultOptionType } from 'antd/lib/select';
import showDrawer from '@/utils/showDrawer';

export interface TaskBasicProps {
  pane: IPane;
  basicInfo: DIJobBasicInfo;
  refreshBasicInfo: () => Promise<DIJobBasicInfo>;
}

enum DataSources {
  SRC = 1,
  DEST,
}

enum Mode {
  INPUT = 'E',
  SELECT = 'S',
}

const { confirm } = Modal;
const { Item } = Form;
const { TextArea } = Input;
const maxWidth = 400;
const minWidth = 200;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSlct = [{ required: true, message: '请选择' }];

//弹窗ModalForm的title枚举
enum ActionModalTitle {
  "resume"="恢复作业",
  "run"="选择环境",
  "pause"= "暂停作业"
}

type ActionType = keyof typeof ActionModalTitle;

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

const TaskBasic: FC<TaskBasicProps> = ({ pane, basicInfo, refreshBasicInfo }) => {
  const { id: jobId } = pane;
  const { data: { data: diWriteModes } = {} } = useRequest(() =>
    getWriteModeEnum({ writeMode: 'DiEnum' }),
  );
  const diWriteModeOptions = diWriteModes?.map((mode) => ({ label: mode, value: mode }));
  const { data: { data: backFlowWriteModes } = {} } = useRequest(() =>
    getWriteModeEnum({ writeMode: 'BackFlowEnum' }),
  );
  const backflowWriteModeOptions = backFlowWriteModes?.map((mode) => ({
    label: mode,
    value: mode,
  }));
  // 当前版本
  const [version, setVersion] = useState<string>();
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
  
  // 数据来源-数据源
  const { destOptions, fromOptions, getSourceOptions, fetchSourceList } = useDataSource({jobTypeEnum: basicInfo?.jobTypeEnum});
  const getSrcDSOptions = (type: string) => fetchSourceList(DataSources.SRC, type);
  const getDestDSOptions = (type: string) => fetchSourceList(DataSources.DEST, type);
  const srcDSOptions = getSourceOptions(DataSources.SRC);
  const destDSOptions = getSourceOptions(DataSources.DEST);

  // 数据来源-表 下拉列表
  const { data: { data: srcTableOptions = [] } = {}, run: getSrcTableOptions } = useRequest(
    getTaskTables,
    {
      manual: true,
    },
  );
  const { data: dbNames = [], run: fetchDbNames } = useRequest(getDbNames, {
    manual: true,
  });
  const { data: tableNames = [], run: fetchTableNames } = useRequest(getTableNames, {
    manual: true,
  });
  const srcDbOptions = dbNames.map((name) => ({ label: name, value: name }));
  const hiveTables = tableNames.map((name) => ({ tableName: name }));
  // kafka topic 下拉列表
  const [topicOptions, setTopicOptions] = useState<DefaultOptionType[]>([]);
  const [srcColumns, setSrcColumns] = useState<MappedColumn[]>([]);
  const [destColumns, setDestColumns] = useState<MappedColumn[]>([]);
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [actionType, setActionType] = useState<ActionType>('run');
  const [visibleAction, setVisibleAction] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);
  const [inputMode, setInputMode] = useState<Mode | undefined>(Mode.SELECT);

  const mapRef = useRef<{
    srcMap: { [key: string]: any };
    destMap: { [key: string]: any };
  }>(null);

  const [visibleVersion, setVisibleVersion] = useState(false);
  const [visibleHistory, setVisibleHistory] = useState(false);

  const { getTreeWrapped, onRemovePane, replaceTab } = useModel('datadev', (_) => ({
    onRemovePane: _.onRemovePane,
    replaceTab: _.replaceTab,
    getTreeWrapped: _.getTreeWrapped,
  }));
  const formInitialValues = {
    srcReadMode: SrcReadMode.ALL,
    destWriteMode: DestWriteMode.INIT,
    configMode: DIConfigMode.VISUALIZATION,
    srcShardingNum: 1,
    destShardingNum: 1,
  };
  const [form] = Form.useForm();
  const [jobContent, setJobContent] = useState<Record<string, any>>({});
  const {
    srcDataSourceType,
    srcDataSourceId,
    srcReadMode,
    srcTables,
    srcDbName,
    destDataSourceType,
    destDataSourceId,
    configMode,
    destTable,
    scriptSelectColumns,
    scriptKeyColumns,
    scriptMergeSqlParamDto,
    srcTableConfig,
  } = jobContent;

  const handleFormValuesChange = (_: unknown, allValues: Record<string, unknown>) => {
    setJobContent(Object.assign({}, jobContent, allValues));
  };

  // 获取作业内容
  useEffect(() => {
    (async () => {
      if (typeof jobId !== 'undefined' && typeof version !== 'undefined') {
        const ver = +version?.split('#')[0];
        const { data } = await getDIJobContent({ jobId, version: ver });
        setJobContent(data);
        setSrcColumns(data.srcCols);
        setDestColumns(data.destCols);
        form.setFieldsValue(data);
      }
    })();
  }, [jobId, version]);

  // 数据来源-数据源类型改变: 刷新`数据源名称`下拉列表
  useEffect(() => {
    if (srcDataSourceType) {
      getSrcDSOptions(srcDataSourceType);
    }
  }, [srcDataSourceType]);

  // 数据去向-数据源类型改变: 刷新`数据源名称`下拉列表
  useEffect(() => {
    if (destDataSourceType) {
      getDestDSOptions(destDataSourceType);
    }
  }, [destDataSourceType]);

  // 数据来源-数据源名称改变: 刷新`表`下拉列表
  useEffect(() => {
    if (typeof srcDataSourceType === 'undefined' || typeof srcDataSourceId === 'undefined') {
      return;
    }
    if (srcDataSourceType === DataSourceType.HIVE) {
      fetchDbNames({ dataSourceId: srcDataSourceId });
      return;
    }
    getSrcTableOptions({
      dataSourceId: srcDataSourceId,
      dataSourceType: srcDataSourceType,
    });
  }, [srcDataSourceId, srcDataSourceType]);

  // 数据来源-DataSourceType.HIVE 刷新`表`下拉列表
  useEffect(() => {
    if (srcDataSourceType === DataSourceType.HIVE && typeof srcDataSourceId !== 'undefined') {
      fetchTableNames({ dataSourceId: srcDataSourceId, dbName: srcDbName });
    }
  }, [srcDataSourceType, srcDataSourceId, srcDbName]);

  // 刷新 topic 下拉列表
  useEffect(() => {
    const fetchData = async () => {
      const { data } = await getKafkaTopics({ dataSourceId: destDataSourceId });
      setTopicOptions(data.map((topic) => ({ label: topic, value: topic })));
    };
    if (destDataSourceType === DataSourceType.KAFKA && typeof destDataSourceId !== 'undefined') {
      fetchData();
    }
  }, [destDataSourceId, destDataSourceType]);

  // 刷新mergeSQL
  const refreshMergeSQL = async () => {
    const { data, success } = await genMergeSQL({
      destTable,
      srcReadMode,
      selectColumns: scriptSelectColumns,
      keyColumns: scriptKeyColumns,
      sourceTable: srcTables,
      dataSourceType: srcDataSourceType,
      days: scriptMergeSqlParamDto?.recentDays,
    });
    if (success) {
      form.setFieldsValue({
        scriptMergeSql: data,
      });
    }
  };

  // 刷新来源表可视化视图
  const refreshSrcVisualise = async () => {
    const tables = srcTables?.split(',') || [];
    if (tables.length === 0) {
      return message.info('请选择表');
    }
    let success = false;
    let data = [];
    if (srcDataSourceType === DataSourceType.HIVE) {
      const res1 = await getColumns({
        dataSourceId: srcDataSourceId,
        dbName: srcDbName,
        tableName: tables[0],
      });
      success = res1.success;
      data = res1.data;
    } else {
      const res2 = await getTaskTableColumns({
        tableName: tables[0],
        dataSourceType: srcDataSourceType,
        dataSourceId: srcDataSourceId,
      });
      success = res2.success;
      data = res2.data;
    }
    if (success) {
      message.success('刷新成功');
      let scriptItem=data.map((item: {name : string}) => item.name).join();//字段名以','链接的字符串
      form.setFieldsValue({scriptSelectColumns:scriptItem});//将最终字符串设置到对应文本框scriptSelectColumns
      setSrcColumns(data);
      setDestColumns([]);
    }
  };

  const refreshDiSrcVisualise = async () => {
    const { rawTable, tableIdxBegin, tableIdxEnd } = (srcTableConfig || {});

    if (inputMode === Mode.INPUT) {
      if (!rawTable) {
        return message.info('请填写表');
      }
      if (!tableIdxBegin || !tableIdxEnd) {
        return message.info('请填写范围');
      }
    }
    if (inputMode === Mode.SELECT && !rawTable) {
      return message.info('请选择表');
    }
    let success = false;
    let data = [];
    const table = inputMode === Mode.INPUT ? `${rawTable}${tableIdxBegin}` : rawTable;
    if (srcDataSourceType === DataSourceType.HIVE) {
      const res1 = await getColumns({
        dataSourceId: srcDataSourceId,
        dbName: srcDbName,
        tableName: table,
      });
      success = res1.success;
      data = res1.data;
    } else {
      const res2 = await getTaskTableColumns({
        tableName: table,
        dataSourceType: srcDataSourceType,
        dataSourceId: srcDataSourceId,
      });
      success = res2.success;
      data = res2.data;
    }
    if (success) {
      message.success('刷新成功');
      let scriptItem=data.map((item: {name : string}) => item.name).join();//字段名以','链接的字符串
      form.setFieldsValue({scriptSelectColumns:scriptItem});//将最终字符串设置到对应文本框scriptSelectColumns
      setSrcColumns(data);
      setDestColumns([]);
    }
  };

  // 刷新去向表可视化视图
  const refreshDestVisualise = async () => {
    if (!destTable) {
      return message.info('请输入表');
    }
    let success = false;
    let data = [];
    if (destDataSourceType === DataSourceType.HIVE) {
      const res1 = await getColumns({
        dataSourceId: destDataSourceId,
        tableName: destTable,
      });
      success = res1.success;
      data = res1.data;
    } else {
      const res2 = await getTaskTableColumns({
        tableName: destTable,
        dataSourceType: destDataSourceType,
        dataSourceId: destDataSourceId,
      });
      success = res2.success;
      data = res2.data;
    }
    if (success) {
      message.success('刷新成功');
      setDestColumns(data);
    }
  };

  /**
   * 暂停、恢复、单次运行任务
   */
  const onAction = (type: ActionType) => {
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
    form.validateFields().then(async () => {
      const srcMap = mapRef.current?.srcMap || {};
      const destMap = mapRef.current?.destMap || {};
      const srcCols: MappedColumn[] = Object.values(srcMap);
      const destCols: MappedColumn[] = Object.values(destMap);
      const values = form.getFieldsValue();
      const params = {
        configMode: DIConfigMode.VISUALIZATION,
        ...values,
        jobId: pane.id,
        id: jobContent?.id,
        version: jobContent?.version,
        contentHash: jobContent?.contentHash,
        srcCols,
        destCols,
      };
      const { success } = await saveDIJobContent(params);
      if (success) {
        message.success('保存成功');
        refreshVersions();
      }
    });
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
   * 同名映射
   */
  const onEponymousMapping = () => {
    const cloneSrcColumns = cloneDeep(srcColumns);
    cloneSrcColumns.forEach(_ => {
      const exist = destColumns.find(destCol => destCol.name === _.name);
      _.mappedColumn = exist ? cloneDeep(exist) : undefined
    });
    setSrcColumns([...cloneSrcColumns]);
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

  const showBaseInfo = () => {
    showDrawer('基本信息', {
      formProps: {
        data: basicInfo
      },
      beforeConfirm: (dialog, form, done) => {
        const values = form.onSave();
        dialog.showLoading();
        saveDIJobBasicInfo(values).then((res) => {
          message.success('保存成功');
          getTreeWrapped();
          replaceTab({
            oldKey: pane.cid,
            newKey: pane.cid,
            title: res.data.name,
            pane,
          });
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

  const getSrcFormItems = () => {
    const items = [];
    const options = srcTableOptions.map((table) => ({
      label: table.tableName,
      value: table.tableName,
    }));
    items.push(
      <Item name="srcDataSourceType" label="数据源类型" rules={ruleSlct} key="1">
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={fromOptions}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
          onChange={() => form.resetFields(['srcDataSourceId', 'srcTables'])}
        />
      </Item>,
    );
    items.push(
      <Item name="srcDataSourceId" label="数据源名称" rules={ruleSlct} key="2">
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={srcDSOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
          onChange={() => form.resetFields(['srcTables', 'srcDbName'])}
        />
      </Item>,
    );
    if (basicInfo?.jobType === DIJobType.DI) {
      items.push(
        <Item label="表" name="srcTableConfig" rules={ruleSlct} key="3">
          <TableSelectInput
            options={options}
            style={{ maxWidth, minWidth }}
            onChange={({inputMode}) => {setInputMode(inputMode)}}
            onRefresh={refreshDiSrcVisualise}
          />
        </Item>,
      );
    } else if (srcDataSourceType === DataSourceType.HIVE) {
      items.push(
        <Fragment key="4">
          <Item name="srcDbName" label="数据库" rules={ruleSlct} style={{ width: '100%' }}>
            <Select
              size="large"
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={srcDbOptions}
              onChange={() => form.resetFields(['srcTables'])}
              showSearch
            />
          </Item>
          <Item name="srcTables" label="表" rules={ruleSlct} style={{ width: '100%' }}>
            <TableSelect showRefresh options={hiveTables} onRefresh={refreshSrcVisualise} />
          </Item>
        </Fragment>,
      );
    } else {
      items.push(
        <Item label="表" name="srcTables" style={{ width: '100%' }} key="5">
          <TableSelect showRefresh options={srcTableOptions} onRefresh={refreshSrcVisualise} />
        </Item>,
      );
    }
    items.push(
      <Item name="srcReadFilter" label="数据过滤" key="6">
        <TextArea style={{ maxWidth, minWidth }} placeholder="请参考相应SQL语法填写where过滤语句" />
      </Item>,
    );
    if (basicInfo?.jobType === DIJobType.DI) {
      items.push(
        <Fragment key="7">
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
              options={shardingNumOptions}
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
        </Fragment>,
      );
    }
    if (basicInfo?.jobType === DIJobType.BACK_FLOW && srcDataSourceType === DataSourceType.HIVE) {
      items.push(
        <Item label="分区" name="srcTablePt" key="8">
          <Input size="large" style={{ maxWidth, minWidth }} placeholder="请输入" />
        </Item>,
      );
    }
    return items;
  };
  const getDestItems = () => {
    const items = [];
    items.push(
      <Item name="destDataSourceType" label="数据源类型" rules={ruleSlct} key="1">
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={destOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
          onChange={() => form.resetFields(['destDataSourceId'])}
        />
      </Item>,
    );
    items.push(
      <Item name="destDataSourceId" label="数据源名称" rules={ruleSlct} key="2">
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={destDSOptions}
          showSearch
          filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
          onChange={() => form.resetFields(['destTopic'])}
        />
      </Item>,
    );
    const batchWriteNode = (
      <Fragment key="3">
        <Item name="destBulkNum" label="批量写入数据量" rules={ruleText}>
          <Input size="large" style={{ maxWidth, minWidth }} placeholder="请输入" />
        </Item>
        <Item name="destShardingNum" label="批量写入并行度" rules={ruleText}>
          <Select
            size="large"
            style={{ maxWidth, minWidth }}
            placeholder="请选择"
            options={shardingNumOptions}
          />
        </Item>
      </Fragment>
    );
    const destTableNode = (
      <Item name="destTable" label="表" rules={ruleText} key="5">
        <TableInput
          showRefresh={basicInfo?.jobType !== DIJobType.DI}
          onRefresh={refreshDestVisualise}
        />
      </Item>
    );
    const backFlowDestWriteModeNode = (
      <Item name="destWriteMode" label="写入模式" rules={ruleSlct} key="6">
        <Radio.Group options={backflowWriteModeOptions} />
      </Item>
    );
    const topicNode = (
      <Item name="destTopic" label="topic" rules={ruleSlct} key="7">
        <Select
          size="large"
          style={{ maxWidth, minWidth }}
          placeholder="请选择"
          options={topicOptions}
          showSearch
          filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        />
      </Item>
    );
    const customParamsNode = (
      <Item name="destPropertyMap" label="自定义参数" key="8">
        <MapInput style={{ maxWidth, minWidth }} />
      </Item>
    );
    if (basicInfo?.jobType === DIJobType.DI) {
      items.push(
        <Fragment key="9">
          {destTableNode}
          <Item name="destBeforeWrite" label="导入前准备语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据前执行的SQL脚本" />
          </Item>
          <Item name="destAfterWrite" label="导入后完成语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据后执行的SQL脚本" />
          </Item>
          <Item name="destWriteMode" label="写入模式" rules={ruleSlct}>
            <Radio.Group options={diWriteModeOptions} />
          </Item>
        </Fragment>,
      );
    } else if (
      [DataSourceType.MYSQL, DataSourceType.POSTGRESQL, DataSourceType.PHOENIX].includes(
        destDataSourceType,
      )
    ) {
      items.push(
        <Fragment key="10">
          {destTableNode}
          {backFlowDestWriteModeNode}
          {batchWriteNode}
        </Fragment>,
      );
    } else if ([DataSourceType.KAFKA].includes(destDataSourceType)) {
      items.push(
        <Fragment key="11">
          {topicNode}
          {batchWriteNode}
          {customParamsNode}
        </Fragment>,
      );
    } else if ([DataSourceType.STARROCKS, DataSourceType.ELASTICSEARCH].includes(destDataSourceType)) {
      items.push(
        <Fragment key="12">
          {destTableNode}
          {backFlowDestWriteModeNode}
          {batchWriteNode}
          {customParamsNode}
        </Fragment>,
      );
    }
    if (basicInfo?.jobType === DIJobType.BACK_FLOW) {
      items.push(
        <Fragment key="9">
          <Item name="destBeforeWrite" label="导入前准备语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据前执行的SQL脚本" />
          </Item>
          <Item name="destAfterWrite" label="导入后完成语句">
            <TextArea style={{ maxWidth, minWidth }} placeholder="请输入导入数据后执行的SQL脚本" />
          </Item>
        </Fragment>,
      );
    }
    if (basicInfo?.jobType === DIJobType.BACK_FLOW && destDataSourceType === DataSourceType.STARROCKS) {
      items.push(
        <Fragment key="13">
          <Item label="分区名称" name="destTablePt" key="13">
            <Input size="large" style={{ maxWidth, minWidth }} placeholder="请输入" />
          </Item>
        </Fragment>,
      );
    }
    return items;
  };
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

  // 字段映射
  const fieldMappingNode =
    configMode === DIConfigMode.SCRIPT ? (
      scriptItemsNode
    ) : (
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
    );

  return (
    <div>
      <div className={styles.task}>
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
              <a onClick={() => onAction('run')}>单次运行</a>
              <a onClick={showBaseInfo}>基本信息</a>
              <a onClick={showConfig}>配置</a>
              <a onClick={() => setVisibleVersion(true)}>版本</a>
              <a onClick={() => setVisibleHistory(true)}>历史</a>
            </Space>
          }
        />
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
              {getSrcFormItems()}
            </div>
            <div className={styles['form-box-item']}>
              <Title>数据去向</Title>
              {getDestItems()}
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
                {Object.keys(DIJobType).includes(basicInfo?.jobType??'') && (
                  <Item name="configMode" rules={ruleSlct} noStyle>
                    <RadioGroup
                      options={diConfigOptions}
                      onBeforeChange={onBeforeConfigModeChange}
                    />
                  </Item>
                )}
              </Space>
            </div>
          </Title>
          {fieldMappingNode}
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
    </div>
  );
};

export default TaskBasic;