import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import SplitPane from 'react-split-pane';
import { Form, Modal, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';
import ParamList from '../../ParamList';

interface ScriptPythonProps {
  monaco: any;
  data: {
    content: any;
    log: any;
  };
  visible: boolean;
  onCancel: () => void;
}

const { TabPane } = Tabs;
const { Item } = Form;

const ScriptPython: ForwardRefRenderFunction<unknown, ScriptPythonProps> = (
  { monaco, data: { content, log }, visible, onCancel },
  ref,
) => {
  const [monacoValue, setMonacoValue] = useState('');
  const [monacoHeight, setMonacoHeight] = useState(500);
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setMonacoHeight(height);
  }, []);

  useEffect(() => {
    if (content) {
      setMonacoValue(content.sourceResource);
      form.setFieldsValue({ scriptArguments: content.scriptArguments });
    }
  }, [content]);

  return (
    <>
      <div style={{ position: 'relative', height: monacoHeight }}>
        <SplitPane split="horizontal" defaultSize="60%">
          <MonacoEditor
            ref={monaco}
            height="100%"
            language="python"
            theme="vs-dark"
            value={monacoValue}
            onChange={(v) => setMonacoValue(v)}
            options={{ automaticLayout: true }}
          />
          <Tabs className={styles.tabs} type="editable-card" hideAdd style={{ height: '100%' }}>
            <TabPane tab="运行日志" key="log" style={{ height: '100%' }} closable={false}>
              <MonacoEditor
                height="100%"
                language="json"
                theme="vs-dark"
                value={log.join('\n')}
                options={{ readOnly: true, automaticLayout: true }}
              />
            </TabPane>
          </Tabs>
        </SplitPane>
      </div>
      <Modal title="作业配置" visible={visible} onCancel={onCancel} forceRender>
        <Form form={form} colon={false}>
          <Item label="参数">
            <ParamList formName={['scriptArguments', 'argumentValue', 'argumentRemark']} />
          </Item>
        </Form>
      </Modal>
    </>
  );
};

export default forwardRef(ScriptPython);
