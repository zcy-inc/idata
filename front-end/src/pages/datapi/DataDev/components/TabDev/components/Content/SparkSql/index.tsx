import React, { forwardRef, useEffect, useImperativeHandle } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Form, Input, Select, Table, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';

interface SparkSqlProps {
  monaco: any;
  data: {
    content: any;
    log: any;
    res: any[];
  };
}

const { TabPane } = Tabs;
const { Item } = Form;
const width = 200;

const SparkSql: ForwardRefRenderFunction<unknown, SparkSqlProps> = (
  { monaco, data: { content, log, res } },
  ref,
) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    if (content) {
      form.setFieldsValue({ externalTables: content.externalTables });
    }
  }, [content]);

  return (
    <>
      <Tabs>
        <TabPane tab="编辑器" key="editor" style={{ padding: '16px 0', height: 440 }} forceRender>
          <MonacoEditor
            ref={monaco}
            height="400"
            language="sql"
            theme="vs-dark"
            value={content.sourceSql}
            options={{ automaticLayout: true }}
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
            console.log(key);
          }
        }}
      >
        <TabPane tab="日志" key="log" style={{ height: 440, padding: '16px 0' }} closable={false}>
          <MonacoEditor
            height="400"
            language="sql"
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
