import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react';
import type { ForwardRefRenderFunction } from 'react';
import MonacoEditor from 'react-monaco-editor';
import SplitPane from 'react-split-pane';
import { Form, Input, Modal, Select, Table, Tabs, Button, message } from 'antd';
import { UpSquareOutlined, DownSquareOutlined } from '@ant-design/icons';
import DataSourceSelect from '@/components/DSSelect';
import type { IDisposable } from 'monaco-editor';
import styles from './index.less';
import { getUDFList, genFlinkTemplate } from '@/services/datadev';
import type { UDF, Task } from '@/types/datadev';
import { TaskTypes } from '@/constants/datadev';
import SqlEditor from '@/components/SqlEditor';

const { confirm } = Modal;
const { TabPane } = Tabs;
const { Item } = Form;

interface SparkSqlProps {
  monaco: any;
  data: {
    task: Task;
    content: any;
    log: any;
    res: any[];
  };
  removeResult: (i: number) => void;
  visible: boolean;
  onCancel: () => void;
}

const SqlContent: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { monaco, data: { content, log, res, task }, removeResult, visible, onCancel },
  ref,
) => {
  const [splitPaneSize, setSplitPaneSize] = useState<number | string>('calc(100% - 40px)');
  const [collapsed, setCollapsed] = useState(true);
  const splitIns = useRef<any>(null);
  const [monacoValue, setMonacoValue] = useState('');
  const [monacoHeight, setMonacoHeight] = useState(500);
  const [UDFList, setUDFList] = useState<UDF[]>([]);
  const [form] = Form.useForm();
  const editorRef = useRef<any>();
  const monacoInnerRef = useRef<IDisposable>();

  const [loading, setLoading] = useState(false);

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setMonacoHeight(height);

    getUDFListWrapped();

    return () => {
      editorRef.current?.dispose();
      monacoInnerRef.current?.dispose();
    };
  }, []);

  useEffect(() => {
    if (content) {
      setMonacoValue(content.sourceSql);
      const udfIds = content.udfIds?.split(',') || [];
      form.setFieldsValue({
        ...content,
        externalTables: content.externalTables,
        udfIds,
      });
    }
  }, [content]);

  const getUDFListWrapped = () => {
    setLoading(true);
    getUDFList()
      .then((res) => setUDFList(res.data))
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

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
          setMonacoValue(temp);
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
    ) : null;
  const handleCollapse = () => {
    setSplitPaneSize('calc(100% - 40px)');
    setCollapsed(true);
  };
  const handleExpand = () => {
    setSplitPaneSize('calc(100% - 200px)');
    setCollapsed(false);
  };

  return (
    <>
      <div style={{ position: 'relative', height: monacoHeight }}>
        <SplitPane
          ref={splitIns}
          split="horizontal"
          defaultSize="calc(100% - 40px)"
          maxSize="calc(100% - 40px)"
          pane2Style={{ overflow: 'hidden' }}
          size={splitPaneSize}
          onChange={setSplitPaneSize}
        >
          <SqlEditor
            formRef={monaco}
            height="100%"
            width="100%"
            language="sql"
            value={monacoValue}
            onChange={(v) => setMonacoValue(v)}
            options={{ automaticLayout: true, quickSuggestions: false, fontSize: 28 }}
          />
          <Tabs
            className={styles.tabs}
            type="editable-card"
            hideAdd
            onEdit={(key, action) => {
              if (action === 'remove') {
                const index = Number(key);
                if (Number.isInteger(index)) {
                  removeResult(index);
                }
              }
            }}
            tabBarExtraContent={
              <div className={styles.collapseBtn}>
                {collapsed ? (
                  <UpSquareOutlined onClick={handleExpand} />
                ) : (
                  <DownSquareOutlined onClick={handleCollapse} />
                )}
              </div>
            }
            style={{ height: '100%' }}
          >
            <TabPane tab="运行日志" key="log" style={{ height: '100%' }} closable={false}>
              <MonacoEditor
                height="100%"
                language="json"
                theme="vs-dark"
                value={log.join('\n')}
                options={{ readOnly: true, automaticLayout: true }}
              />
            </TabPane>
            {res.map((_, i) => (
              <TabPane
                tab={`结果${i + 1}`}
                key={i}
                style={{
                  height: '100%',
                  backgroundColor: '#2d3956',
                  padding: 16,
                  overflowY: 'auto',
                }}
              >
                <Table
                  className={styles.table}
                  columns={_.columns}
                  dataSource={_.dataSource}
                  size="small"
                  pagination={{
                    hideOnSinglePage: true,
                    pageSize: 9999,
                  }}
                  scroll={{
                    x: 'max-content',
                  }}
                />
              </TabPane>
            ))}
          </Tabs>
        </SplitPane>
      </div>
      <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={footer} forceRender>
        <Form
          layout="horizontal"
          form={form}
          colon={false}
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 16 }}
        >
          <Item name="externalTables" label="外部表">
            <Input placeholder="请输入" />
          </Item>
          <Item name="udfIds" label="自定义函数">
            <Select
              mode="multiple"
              loading={loading}
              placeholder="请选择"
              options={UDFList.map((_) => ({
                label: _.udfName,
                value: `${_.id}`,
              }))}
            />
          </Item>
          {task.jobType === TaskTypes.SQL_FLINK && (
            <>
              <Item label="来源数据源" name={['extConfig', 'flinkExtConfig', 'flinkSourceConfigs']}>
                <DataSourceSelect quantityCustom fetchTemplate={fetchSrcTemplate} />
              </Item>
              <Item label="目标数据源" name={['extConfig', 'flinkExtConfig', 'flinkSinkConfigs']}>
                <DataSourceSelect fetchTemplate={fetchDestTemplate} />
              </Item>
            </>
          )}
        </Form>
      </Modal>
    </>
  );
};

export default forwardRef(SqlContent);
