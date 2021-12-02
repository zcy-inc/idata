import React, { forwardRef, useEffect, useImperativeHandle } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Form, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';
import ParamList from '../../ParamList';

interface ScriptPythonProps {
  monaco: any;
  data: {
    content: any;
    log: any;
  };
}

const { TabPane } = Tabs;
const { Item } = Form;

const ScriptPython: ForwardRefRenderFunction<unknown, ScriptPythonProps> = (
  { monaco, data: { content, log } },
  ref,
) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    if (content) {
      form.setFieldsValue({ scriptArguments: content.scriptArguments });
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
            value={content.sourceResource}
            options={{ automaticLayout: true }}
          />
        </TabPane>
        <TabPane tab="作业配置" key="config" style={{ padding: 16 }} forceRender>
          <Form form={form} colon={false}>
            <Item label="参数">
              <ParamList formName={['scriptArguments', 'argumentValue', 'argumentRemark']} />
            </Item>
          </Form>
        </TabPane>
      </Tabs>
      <Tabs className={styles.tabs} style={{ marginTop: 16 }}>
        <TabPane tab="日志" key="log" style={{ height: 440, padding: '16px 0' }}>
          <MonacoEditor
            height="400"
            language="sql"
            theme="vs-dark"
            value={log.join('\n')}
            options={{ readOnly: true, automaticLayout: true }}
          />
        </TabPane>
      </Tabs>
    </>
  );
};

export default forwardRef(ScriptPython);
