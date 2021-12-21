import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import * as m from 'monaco-editor';
import { Form, Input, Select, Table, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import type { IDisposable } from 'monaco-editor';
import styles from './index.less';

import Test from './Test';

interface SparkSqlProps {
  monaco: any;
  data: {
    content: any;
    log: any;
    res: any[];
  };
  removeResult: (i: number) => void;
}

const { TabPane } = Tabs;
const { Item } = Form;
const width = 200;

const KINDS = m.languages.CompletionItemKind;
const suggestionsSql = [
  { label: 'SELECT', insertText: 'SELECT', detail: 'description', kind: KINDS.Keyword },
  { label: 'FROM', insertText: 'FROM', detail: 'description', kind: KINDS.Keyword },
  { label: 'WHERE', insertText: 'WHERE', detail: 'description', kind: KINDS.Keyword },
  { label: 'DISTINCT', insertText: 'DISTINCT', detail: 'description', kind: KINDS.Keyword },
  { label: 'AND', insertText: 'AND', detail: 'description', kind: KINDS.Keyword },
  { label: 'OR', insertText: 'OR', detail: 'description', kind: KINDS.Keyword },
  { label: 'ORDER BY', insertText: 'ORDER BY', detail: 'description', kind: KINDS.Keyword },
  { label: 'INSERT', insertText: 'INSERT', detail: 'description', kind: KINDS.Keyword },
  { label: 'UPDATE', insertText: 'UPDATE', detail: 'description', kind: KINDS.Keyword },
  { label: 'DELETE', insertText: 'DELETE', detail: 'description', kind: KINDS.Keyword },
  { label: 'TOP', insertText: 'TOP', detail: 'description', kind: KINDS.Keyword },
  { label: 'LIKE', insertText: 'LIKE', detail: 'description', kind: KINDS.Keyword },
  { label: 'IN', insertText: 'IN', detail: 'description', kind: KINDS.Keyword },
  { label: 'BETWEEN', insertText: 'BETWEEN', detail: 'description', kind: KINDS.Keyword },
  { label: 'JOIN', insertText: 'JOIN', detail: 'description', kind: KINDS.Keyword },
];
const suggestions = [
  { label: '测试1', insertText: '测试1', detail: '提示的文字' },
  { label: '测试2', insertText: '测试22', detail: '提示的文字' },
  { label: '测试3', insertText: '测试3', detail: '提示的文字' },
];

const SparkSql: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { monaco, data: { content, log, res }, removeResult },
  ref,
) => {
  const [monacoValue, setMonacoValue] = useState('');
  const [form] = Form.useForm();
  const editorRef = useRef<any>();
  const monacoInnerRef = useRef<IDisposable>();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    return () => {
      editorRef.current?.dispose();
      monacoInnerRef.current?.dispose();
    };
  }, []);

  useEffect(() => {
    if (content) {
      setMonacoValue(content.sourceSql);
      form.setFieldsValue({ externalTables: content.externalTables });
    }
  }, [content]);

  return (
    <>
      <Tabs>
        <TabPane tab="编辑器" key="editor" style={{ padding: '16px 0', height: 440 }} forceRender>
          {/* <Test
            key="plaintext"
            height="260"
            lineNumbers="off"
            wordWrap="on"
            defaultValue=""
            mode="plaintext"
            suggestions={suggestions}
          /> */}
          <MonacoEditor
            // ref={monaco}
            height="400"
            language="sql"
            theme="vs-dark"
            value={monacoValue}
            onChange={(v) => setMonacoValue(v)}
            options={{ automaticLayout: true }}
            editorDidMount={(editor, monacoInner) => {
              editorRef.current = editor;
              monacoInnerRef.current = monacoInner.languages.registerCompletionItemProvider(
                'plaintext',
                {
                  provideCompletionItems() {
                    return {
                      suggestions: suggestionsSql,
                    };
                  },
                  triggerCharacters: [':'],
                },
              );
            }}
          />
        </TabPane>
        <TabPane tab="作业配置" key="config" style={{ padding: 16 }} forceRender>
          <Form form={form} colon={false}>
            <Item name="externalTables" label="外部表">
              <Input placeholder="请输入" style={{ width }} />
            </Item>
            <Item name="udfIds" label="自定义函数">
              <Select placeholder="请选择" options={[]} style={{ width }} />
            </Item>
          </Form>
        </TabPane>
      </Tabs>
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
      >
        <TabPane tab="日志" key="log" style={{ height: 440, padding: '16px 0' }} closable={false}>
          <MonacoEditor
            height="400"
            language="json"
            theme="vs-dark"
            value={log.join('\n')}
            options={{ readOnly: true, automaticLayout: true }}
          />
        </TabPane>
        {res.map((_, i) => (
          <TabPane tab={`结果${i + 1}`} key={i} style={{ height: 440, padding: '16px 0' }}>
            <Table
              columns={_.columns}
              dataSource={_.dataSource}
              size="small"
              pagination={{
                hideOnSinglePage: true,
                pageSize: 9999,
              }}
            />
          </TabPane>
        ))}
      </Tabs>
    </>
  );
};

export default forwardRef(SparkSql);
