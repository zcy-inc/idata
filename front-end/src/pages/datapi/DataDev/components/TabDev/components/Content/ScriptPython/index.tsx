import React, { forwardRef, useEffect, useImperativeHandle } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Form, FormInstance, Modal } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import ParamList from '../../ParamList';
import { EditorPanel, EditorPanelProps } from '../../../../../../components/EditorPanel';

interface ScriptPythonProps {
  panelProps: EditorPanelProps;
  form: FormInstance<any>;
  editorRef: any;
  data: {
    content: any;
  };
  visible: boolean;
  onCancel: () => void;
}

const { Item } = Form;

const ScriptPython: ForwardRefRenderFunction<unknown, ScriptPythonProps> = (
  { data: { content }, visible, onCancel, panelProps, form, editorRef },
  ref,
) => {
  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    if (content) {
      form.setFieldsValue(content);
    }
  }, [content]);

  return (
    <Form form={form} colon={false}>
      <EditorPanel {...panelProps}>
        <Item name="sourceResource" noStyle>
          <MonacoEditor
            ref={editorRef}
            height="100%"
            language="python"
            theme="vs-dark"
            options={{ automaticLayout: true }}
          />
        </Item>
      </EditorPanel>
      <Modal title="作业配置" visible={visible} footer={null} onCancel={onCancel} forceRender>
        <Item label="参数">
          <ParamList formName={['scriptArguments', 'argumentValue', 'argumentRemark']} />
        </Item>
      </Modal>
    </Form>
  );
};

export default forwardRef(ScriptPython);
