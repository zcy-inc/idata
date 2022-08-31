import React, { useEffect, useState, useImperativeHandle, forwardRef, ForwardRefRenderFunction } from 'react';
import { Form, Modal, Select } from 'antd';
import { Title } from '@/components';
import { useRequest } from 'ahooks';
import styles from './index.less';
import {
  getTaskTables,
  getStreamJobBasicInfo,
} from '@/services/datadev';
import {
  SrcReadMode,
  DestWriteMode,
  DIConfigMode,
} from '@/constants/datadev';
import { getDataSourceList, getNewDataSourceTypes } from '@/services/datasource';
import ActualTable from '../ActualTable';
import { DIJobBasicInfo } from '@/types/datadev';
export interface ContentProps {
  jobId: number;
  version: string;
  onChange?: any;
  isView?: boolean;
  basicInfo: DIJobBasicInfo | undefined;
}

enum DataSources {
  SRC = 1,
  DEST,
}

enum Mode {
  INPUT = 'E',
  SELECT = 'S',
}

const { Item } = Form;
const maxWidth = 400;
const minWidth = 200;
const ruleSlct = [{ required: true, message: '请选择' }];
const formInitialValues = {
  srcReadMode: SrcReadMode.ALL,
  destWriteMode: DestWriteMode.INIT,
  configMode: DIConfigMode.VISUALIZATION,
  srcShardingNum: 1,
  destShardingNum: 1,
};

const TabTaskActualContent: ForwardRefRenderFunction<any, ContentProps> = ({ basicInfo, jobId, version, onChange, isView=false }, ref: any) => {
  useImperativeHandle(ref, () => ({
    getValues: () => form.validateFields().then(() => jobContent)
  }))

  // 获取数据源类型下拉列表
  const {data: dataSourceType} = useRequest(
    () => getNewDataSourceTypes({jobType: (basicInfo as DIJobBasicInfo).jobTypeEnum}).then(({ data }) => {
      const fromList = data.fromList.map(type => ({label: type, value: type}));
      const destList = data.destList.map(type => ({label: type, value: type}));
      return {
        fromList,
        destList
      }
    })
  )
  // 数据来源-表 下拉列表
  const { data: { data: srcTableOptions = [] } = {}, run: getSrcTableOptions } = useRequest(
    (params) => getTaskTables(params).then(res => {
      return {
        data: res.data.map((table) => ({
          label: table.tableName,
          value: table.tableName,
        }))
      }
    }),
    {
      manual: true,
    },
  );
  const [sourceListMap, setSourceListMap] = useState<Record<string | number, {label: string; value: string| number}[]>>({});
  const [jobContent, setJobContent] = useState<Record<string, any>>({});
  const [form] = Form.useForm();
  const {
    srcDataSourceType,
    destDataSourceType,
    srcDataSourceId,
  } = jobContent;

  useEffect(() => {
    onChange && onChange(jobContent)
  }, [jobContent]);

  // 获取作业内容
  useEffect(() => {
    (async () => {
      if (jobId && version) {
        const data = await getStreamJobBasicInfo({ jobId, version: +version }).then(res => ({
          ...res,
          tableDtoList: res.tableDtoList.map((item: any) => ({
            ...item,
            srcTable: transformTable(item, res.enableSharding)
          }))
        }));
        setJobContent(data);
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
    if (srcDataSourceType && srcDataSourceId && destDataSourceType) {
      getSrcTableOptions({
        dataSourceId: srcDataSourceId,
        dataSourceType: srcDataSourceType,
      });
    }
  }, [srcDataSourceId, srcDataSourceType, destDataSourceType]);

  const getSrcDSOptions = (type: string) => fetchSourceList(DataSources.SRC, type);
  const getDestDSOptions = (type: string) => fetchSourceList(DataSources.DEST, type);

  // 数据源名称列表
  const fetchSourceList = async (typeKey: string | number, type?: string) => {
    if (type) {
      const sourceList = await getDataSourceList({ type, limit: 10000, offset: 0 }).then(
        ({ data }) => data.content,
      );
      setSourceListMap((prev) => ({ ...prev, [typeKey]: sourceList.map(item => ({
        label: item.name,
        value: item.id,
      })) }));
      return sourceList;
    }
    return [];
  };

  const transformTable = (item: any,enableSharding: boolean) => {
    const { srcTable } = item;
    if(enableSharding) {
      const reg = /\[.*?\]/g;
      const matchs = srcTable.match(reg);
      if(matchs) {
        const matchString = matchs[0].slice(1, matchs[0].length - 1);
        return {
          inputMode: Mode.INPUT,
          rawTable: srcTable.replace(`_${matchs[0]}`, ''),
          tableIdxBegin: matchString.split('-')[0],
          tableIdxEnd: matchString.split('-')[1],
        }
      }
      return {
        inputMode: Mode.INPUT,
        rawTable: srcTable
      }
    } else {
      return {
        inputMode: Mode.SELECT,
        rawTable: srcTable,
      }
    }
  }

  const resetTableDtoListValue = (enableSharding: boolean) => {
    if(enableSharding) {  // 分表同步打开
      return [{
        id: -1,
        srcTable: {
          inputMode: Mode.INPUT,
          rawTable: '',
          tableIdxBegin: undefined,
          tableIdxEnd: undefined
        },
        tableCdcPropList: []
      }]
    }
    return []
  }

  const handleFormValuesChange = async (values: any, allValues: Record<string, any>) => {
    if(values.enableSharding === false || values.enableSharding || values.srcDataSourceId || values.srcDataSourceType) {
      // 针对table重新赋值
      if(values.enableSharding) { // 开关提示
        await Modal.confirm({
          title: '切换后将丢失现在的表配置信息，请确认切换？',
          onOk(){
            allValues.tableDtoList = resetTableDtoListValue(allValues.enableSharding);
            const newJobContent = Object.assign({}, jobContent, allValues)
            setJobContent(newJobContent);
            form.setFieldsValue(newJobContent);
          },
          onCancel(){
            allValues.enableSharding = false;
            const newJobContent = Object.assign({}, jobContent, allValues)
            setJobContent(newJobContent);
            form.setFieldsValue(newJobContent);
          }
        })
        return;
      }
      allValues.tableDtoList = resetTableDtoListValue(allValues.enableSharding);
    }

    if(values.srcDataSourceType) {
      allValues.srcDataSourceId = undefined;
    }
    if(values.tableDtoList) {
      for(let index = 0; index<values.tableDtoList.length; index++) {
        allValues.tableDtoList[index].destTable = jobContent.tableDtoList[index].destTable;
      }
    }
    const newJobContent = Object.assign({}, jobContent, allValues)
    setJobContent(newJobContent);
    form.setFieldsValue(newJobContent);
  };

  return (
    <Form
      form={form}
      layout="horizontal"
      colon={false}
      className={styles.form}
      initialValues={formInitialValues}
      onValuesChange={handleFormValuesChange}
    >
      <div className={styles['form-box']}>
        <div className={styles['form-box-item']}>
          <Title>数据来源</Title>
          <Item name="srcDataSourceType" label="数据源类型" rules={ruleSlct} key="1">
            <Select
              size="large"
              disabled={isView}
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={dataSourceType?.fromList || []}
              showSearch
              filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
            />
          </Item>
          <Item name="srcDataSourceId" label="数据源名称" rules={ruleSlct} key="2">
            <Select
              disabled={isView}
              size="large"
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={sourceListMap[DataSources.SRC]}
              showSearch
              filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
            />
          </Item>
        </div>
        <div className={styles['form-box-item']}>
          <Title>数据去向</Title>
          <Item name="destDataSourceType" label="数据源类型" rules={ruleSlct} key="1">
            <Select
              size="large"
              disabled={isView}
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={dataSourceType?.destList || []}
              showSearch
              filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
            />
          </Item>
          <Item name="destDataSourceId" label="数据源名称" rules={ruleSlct} key="2">
            <Select
              size="large"
              disabled={isView}
              style={{ maxWidth, minWidth }}
              placeholder="请选择"
              options={sourceListMap[DataSources.DEST]}
              showSearch
              filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
            />
          </Item>
        </div>
      </div>
      <div>
        <Title>同步表配置</Title>
        <ActualTable
          tableOptions={srcTableOptions}
          jobContent={jobContent}
          isView={isView}
          onChange={(data: any) => {
            setJobContent({...jobContent, tableDtoList: data});
            form.setFieldsValue({...jobContent, tableDtoList: data});
          }}
        />
      </div>
    </Form>
  );
};

export default forwardRef(TabTaskActualContent);
