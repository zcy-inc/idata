import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import type { ForwardRefRenderFunction } from 'react';
import { Form, FormInstance, Input, Modal, Button, message, Select } from 'antd';
import DataSourceSelect from '@/components/DSSelect';
import { JoinSelect, Title, TableNameInput } from '@/components';
import { getUDFList, genFlinkTemplate } from '@/services/datadev';
import { getDataSourceList, getDataSourceTypesNew } from '@/services/datasource';
import type { UDF, Task } from '@/types/datadev';
import { TaskTypes } from '@/constants/datadev';
import SqlEditor from '@/components/SqlEditor';
import { EditorPanel, EditorPanelProps } from '../../../../../../components/EditorPanel';

const { confirm } = Modal;
const { Item } = Form;
const maxWidth = 400;
const minWidth = 200;

interface SparkSqlProps {
  panelProps: EditorPanelProps;
  form: FormInstance<any>;
  editorRef: any;
  data: {
    task: Task;
    content: any;
  };
  visible: boolean;
  onCancel: () => void;
}

const SqlContent: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { data: { content, task }, visible, onCancel, panelProps, form, editorRef },
  ref,
) => {
  const [UDFList, setUDFList] = useState<UDF[]>([]);

  const [loading, setLoading] = useState(false);

  const [srcDSOptions, setSrcDSOption] = useState([]);
  const [tableType, setTableType] = useState('');
  const [externalList, setExternalList] = useState([]);

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    getUDFListWrapped();
  }, []);

  useEffect(() => {
    if (content) {
      form.setFieldsValue(content);
      if (task.jobType === TaskTypes.SQL_SPARK) {
        getDataSourceTypesNew(task.jobType).then((res) => {
          setExternalList(res.data && res.data.externalList as never[]);
        });
        const { extTables = [{}] } =  content;
        const { dataSourceType, dataSourceId = {}, tables = [] } = extTables[0];
        form.setFieldsValue({
          srcDataSourceType: dataSourceType,
          srcDataSourceId: dataSourceId.key, // TODO:luzhu 新增外部表 回显tableName
          srcTableNamse: tables.map((table: { tableName: any;tableAlias:any }) => ({
            name: table.tableName,
            alias: table.tableAlias
          })),
        });
        setTableType(dataSourceType);
        if (dataSourceType) {
          getSrcDSOptions(dataSourceType);
        }
      }
    }
  }, [content]);

  const getUDFListWrapped = () => {
    setLoading(true);
    getUDFList()
      .then((res) => setUDFList(res.data))
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  // sql-spark
  const getSrcDSOptions = (type: string) => {
    getDataSourceList({
      type,
      limit: 10000,
      offset: 0,
    }).then((res) => {
      const option = res?.data?.content.map((p) => ({
        value: p.name,
        key: p.id,
      }));
      setSrcDSOption(option as []);
    });
  }
  const onDataSourceChange = (type:string) => {
    getSrcDSOptions(type);
    setTableType(type);
    form.setFieldsValue({
      srcDataSourceId: '',
    });
  }
  const saveDevSetting = () => {
    const values = form.getFieldsValue();
    const {srcDataSourceId, srcDataSourceType, srcTableNamse} = values;
    if (srcDataSourceType) {
      if (!srcDataSourceId) {
        message.error('请选择数据源名称');
        return;
      } else {
        if (!srcTableNamse) {
          message.error('请输入表名');
          return;
        }
      }
    }
    onCancel();
  };

  // sql-flink
  const fetchSrcTemplate = (value: unknown[]) => genFlinkTemplate({ flinkSourceConfigs: value });
  const fetchDestTemplate = (value: unknown[]) => genFlinkTemplate({ flinkSinkConfigs: value });
  const genEditorTemplate = () => {
    const flinkExtConfig = form.getFieldValue(['extConfig', 'flinkExtConfig']);
    if (flinkExtConfig) {
      confirm({
        title: '提示',
        content: '生成模板会覆盖原有编辑内容',
        onOk: async () => {
          const temp = await genFlinkTemplate(flinkExtConfig);
          form.setFieldsValue({ sourceSql: temp });
        },
      });
    } else {
      message.warning('请先选择数据源');
    }
  };
  const footer =
    task.jobType === TaskTypes.SQL_FLINK ? (
      <Button type="primary" onClick={genEditorTemplate}>
        生成模板
      </Button>
    ) : <Button type="primary" onClick={saveDevSetting}>
      确认
    </Button>;

  return (
    <Form
      layout="horizontal"
      form={form}
      colon={false}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 16 }}
    >
      <EditorPanel {...panelProps}>
        <Item name="sourceSql" noStyle>
          <SqlEditor
            formRef={editorRef}
            height="100%"
            width="100%"
            language="sql"
            options={{ automaticLayout: true, quickSuggestions: false, fontSize: 28 }}
          />
        </Item>
      </EditorPanel>
      {/* SQL_SPARK */}
      {
        task.jobType === TaskTypes.SQL_SPARK && (
          <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={footer} forceRender>
            <Title>外部表</Title>
            <Item name="srcDataSourceType" label="数据源类型" key="1">
              <Select
                size="large"
                style={{ maxWidth, minWidth }}
                placeholder="请选择数据源类型"
                options={externalList.map(value => ({value, label: value}))}
                showSearch
                filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
                onChange={onDataSourceChange}
              />
            </Item>
            <Item name="srcDataSourceId" label="数据源名称" key="2">
              <Select
                size="large"
                style={{ maxWidth, minWidth }}
                placeholder="请选择"
                options={srcDSOptions}
                showSearch
                labelInValue
                filterOption={(v: string, option: any) => option.label.indexOf(v) >= 0}
              />
            </Item>
            {
              tableType && (
                <Item name="srcTableNamse" label="表名" tooltip="在SQL中引用外部表时，需要使用表别名。">
                  <TableNameInput tableType={tableType} />
                </Item>
              )
            }
            <Title>自定义函数</Title>
            <Item name="udfIds" label="自定义函数">
              <JoinSelect
                loading={loading}
                placeholder="请选择"
                options={UDFList.map(({ udfName, id }) => ({
                  label: udfName,
                  value: `${id}`,
                }))}
              />
            </Item>
        </Modal>
        )
      }
      {/* SQL_FLINK */}
      {
        task.jobType === TaskTypes.SQL_FLINK && (
          <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={footer} forceRender>
            <Item name="externalTables" label="外部表">
              <Input placeholder="请输入" />
            </Item>
            <Item name="udfIds" label="自定义函数">
              <JoinSelect
                loading={loading}
                placeholder="请选择"
                options={UDFList.map(({ udfName, id }) => ({
                  label: udfName,
                  value: `${id}`,
                }))}
              />
            </Item>
            <Item label="来源数据源" name={['extConfig', 'flinkExtConfig', 'flinkSourceConfigs']}>
              <DataSourceSelect quantityCustom fetchTemplate={fetchSrcTemplate} jobType={task.jobType} isDest={false} />
            </Item>
            <Item label="目标数据源" name={['extConfig', 'flinkExtConfig', 'flinkSinkConfigs']}>
              <DataSourceSelect fetchTemplate={fetchDestTemplate} jobType={task.jobType} isDest={true} />
            </Item>
          </Modal>
        )
      }
    </Form>
  );
};

export default forwardRef(SqlContent);
