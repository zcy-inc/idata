import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import SplitPane from 'react-split-pane';
import { Form, Input, Modal, Select, Table, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import type { IDisposable } from 'monaco-editor';
import styles from './index.less';
import { getUDFList } from '@/services/datadev';
import type { UDF } from '@/types/datadev';

import SqlEditor from '@/components/SqlEditor';

interface SparkSqlProps {
  monaco: any;
  data: {
    content: any;
    log: any;
    res: any[];
  };
  removeResult: (i: number) => void;
  visible: boolean;
  onCancel: () => void;
}

const { TabPane } = Tabs;
const { Item } = Form;
const width = 200;

const SparkSql: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { monaco, data: { content, log, res }, removeResult, visible, onCancel },
  ref,
) => {
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

  return (
    <>
      <div style={{ position: 'relative', height: monacoHeight }}>
        <SplitPane split="horizontal" defaultSize="60%" pane2Style={{ overflow: 'hidden' }}>
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
                style={{ height: '100%', backgroundColor: '#2d3956', padding: 16, overflowY: 'auto' }}
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
      <Modal title="作业配置" visible={visible} onCancel={onCancel} footer={null} forceRender>
        <Form form={form} colon={false}>
          <Item name="externalTables" label="外部表">
            <Input placeholder="请输入" style={{ width }} />
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
              style={{ width }}
            />
          </Item>
        </Form>
      </Modal>
    </>
  );
};

export default forwardRef(SparkSql);
