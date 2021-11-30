import React, { forwardRef } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Table, Tabs } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';

interface ScriptShellProps {
  monaco: any;
  data: {
    content: any;
    log: any;
    res: any[];
  };
}

const { TabPane } = Tabs;

const ScriptShell: ForwardRefRenderFunction<unknown, ScriptShellProps> = (
  { monaco, data: { content, log, res } },
  ref,
) => {
  return (
    <>
      <div style={{ marginTop: 16 }}>
        <MonacoEditor
          ref={monaco}
          height="400"
          language="sql"
          theme="vs-dark"
          value={content.sourceResource}
          options={{ automaticLayout: true }}
        />
      </div>
      <Tabs
        className={styles.tabs}
        type="editable-card"
        hideAdd
        style={{ marginTop: 16 }}
        onEdit={(key, action) => {
          if (action === 'remove') {
            console.log(key);
          }
        }}
      >
        {[log, ...res].map((_) => (
          <TabPane
            tab={_.name}
            key={_.name}
            style={{ height: 440, padding: '16px 0' }}
            closable={_.type === 'res'}
          >
            {_.type === 'log' ? (
              <MonacoEditor
                height="400"
                language="sql"
                theme="vs-dark"
                value={''}
                options={{ readOnly: true, automaticLayout: true }}
              />
            ) : (
              <Table
                columns={[]}
                dataSource={[]}
                pagination={{
                  hideOnSinglePage: true,
                  pageSize: 10,
                }}
              />
            )}
          </TabPane>
        ))}
      </Tabs>
    </>
  );
};

export default forwardRef(ScriptShell);
