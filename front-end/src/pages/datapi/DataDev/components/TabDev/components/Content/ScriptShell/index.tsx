import React, { forwardRef, useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import type { ForwardRefRenderFunction } from 'react';

interface ScriptShellProps {
  monaco: any;
  data: {
    content: any;
  };
}

const ScriptShell: ForwardRefRenderFunction<unknown, ScriptShellProps> = (
  { monaco, data: { content } },
  ref,
) => {
  const [monacoHeight, setMonacoHeight] = useState(500);

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setMonacoHeight(height);
  }, []);

  return (
    <div style={{ position: 'relative', height: monacoHeight }}>
      <MonacoEditor
        ref={monaco}
        height="100%"
        language="shell"
        theme="vs-dark"
        value={content.sourceResource}
        options={{ automaticLayout: true }}
      />
    </div>
  );
};

export default forwardRef(ScriptShell);
