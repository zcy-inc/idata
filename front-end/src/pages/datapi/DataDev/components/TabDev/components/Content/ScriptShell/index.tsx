import React, { forwardRef, useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import type { ForwardRefRenderFunction } from 'react';
import { Form, FormInstance } from 'antd';

interface ScriptShellProps {
  form: FormInstance<any>;
  editorRef: any;
  data: {
    content: any;
  };
}

const { Item } = Form;

const ScriptShell: ForwardRefRenderFunction<unknown, ScriptShellProps> = (
  { data: { content }, form, editorRef },
  ref,
) => {
  const [monacoHeight, setMonacoHeight] = useState(500);

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setMonacoHeight(height);
  }, []);

  // TODO:
  useEffect(() => {
    form.setFieldsValue({ pythonResource: content.pythonResource });
  }, [content]);

  return (
    <div style={{ position: 'relative', height: monacoHeight }}>
      <Item name="sourceResource" noStyle>
        <MonacoEditor
          ref={editorRef}
          height="100%"
          language="shell"
          theme="vs-dark"
          options={{ automaticLayout: true }}
        />
      </Item>
    </div>
  );
};

export default forwardRef(ScriptShell);
