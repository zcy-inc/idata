import React, { FC, useEffect, useState } from 'react';
import SplitPane from 'react-split-pane';
import { Tabs, Table } from 'antd';
import MonacoEditor from 'react-monaco-editor';
import { UpSquareOutlined, DownSquareOutlined } from '@ant-design/icons';

import styles from './index.less';

const { TabPane } = Tabs;

export interface EditorPanelProps {
  expand: boolean;
  handleExpandChange: () => void;
  log: string[];
  size: number | string;
  setSize: (size: number) => void;
  results: Record<string, unknown>[][];
  removeResult?: (index: number) => void;
  maxSize?: number | string;
}

export const EditorPanel: FC<EditorPanelProps> = ({
  expand,
  handleExpandChange,
  log,
  size,
  setSize,
  results,
  removeResult,
  maxSize,
  children,
}) => {
  const [height, setHeight] = useState(500);

  const calcColumns = (result: Record<string, unknown>[]) => {
    const keys = Object.keys(result[0] || {});
    return keys.map((key) => ({ title: key, dataIndex: key, key }));
  };

  useEffect(() => {
    const container = document.querySelector('.ant-tabs-content-holder');
    let height = container?.clientHeight || 500;
    height = height - 40 - 48;
    setHeight(height);
  }, []);

  return (
    <div style={{ position: 'relative', height: height }}>
      <SplitPane
        split="horizontal"
        pane2Style={{ overflow: 'hidden' }}
        maxSize={maxSize}
        size={size}
        onChange={setSize}
      >
        {children}
        <Tabs
          className={styles.tabs}
          type="editable-card"
          hideAdd
          onEdit={(key, action) => {
            if (action === 'remove') {
              const index = Number(key);
              if (Number.isInteger(index)) {
                removeResult?.(index);
              }
            }
          }}
          tabBarExtraContent={
            <div className={styles.collapseBtn}>
              {expand ? (
                <DownSquareOutlined onClick={handleExpandChange} />
              ) : (
                <UpSquareOutlined onClick={handleExpandChange} />
              )}
            </div>
          }
          style={{ height: '100%' }}
        >
          <TabPane tab="运行日志" key="log" style={{ height: '100%' }} closable={false}>
            <MonacoEditor
              height="100%"
              language="json"
              theme="vs-dark"
              value={log.join('\n')}
              options={{ readOnly: true, automaticLayout: true }}
            />
          </TabPane>
          {results &&
            results.map((result, i) => (
              <TabPane
                tab={`结果${i + 1}`}
                key={i}
                style={{
                  height: '100%',
                  backgroundColor: '#2d3956',
                  padding: 16,
                  overflowY: 'auto',
                }}
              >
                <Table
                  className={styles.table}
                  columns={calcColumns(result)}
                  dataSource={result}
                  size="small"
                  pagination={{
                    hideOnSinglePage: true,
                    pageSize: 9999,
                  }}
                  scroll={{
                    x: 'max-content',
                  }}
                />
              </TabPane>
            ))}
        </Tabs>
      </SplitPane>
    </div>
  );
};
