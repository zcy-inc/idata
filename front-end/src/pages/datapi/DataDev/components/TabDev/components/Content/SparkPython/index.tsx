import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Form, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';
import ParamList from '../../ParamList';

interface SparkPythonProps {
  monaco: any;
  data: {
    content: any;
    log: any;
  };
}

const { TabPane } = Tabs;
const { Item } = Form;

const SparkPython: ForwardRefRenderFunction<unknown, SparkPythonProps> = (
  { monaco, data: { content, log } },
  ref,
) => {
  const [monacoValue, setMonacoValue] = useState('');
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    setMonacoValue(content.pythonResource);
  }, [content]);

  return (
    <>
      <Tabs>
        <TabPane tab="编辑器" key="editor" style={{ padding: '16px 0', height: 440 }} forceRender>
          <MonacoEditor
            ref={monaco}
            height="400"
            language="python"
            theme="vs-dark"
            value={monacoValue}
            onChange={(v) => setMonacoValue(v)}
            options={{ automaticLayout: true }}
          />
        </TabPane>
        <TabPane tab="作业配置" key="config" style={{ padding: 16 }} forceRender>
          <Form form={form} colon={false}>
            <Item label="参数">
              <ParamList formName={['appArguments', 'argumentValue', 'argumentRemark']} />
            </Item>
          </Form>
        </TabPane>
      </Tabs>
      <Tabs className={styles.tabs}>
        <TabPane tab="日志" key="log" style={{ height: 440, padding: '16px 0' }}>
          <MonacoEditor
            height="400"
            language="json"
            theme="vs-dark"
            value={log.join('\n')}
            options={{ readOnly: true, automaticLayout: true }}
          />
        </TabPane>
      </Tabs>
    </>
  );
};

export default forwardRef(SparkPython);
