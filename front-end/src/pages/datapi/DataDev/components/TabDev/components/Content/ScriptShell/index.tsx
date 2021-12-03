import React, { forwardRef } from 'react';
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
  return (
    <div style={{ marginTop: 16 }}>
      <MonacoEditor
        ref={monaco}
        height="400"
        language="shell"
        theme="vs-dark"
        value={content.sourceResource}
        options={{ automaticLayout: true }}
      />
    </div>
  );
};

export default forwardRef(ScriptShell);
