import React, { useState } from 'react';
import { PageContainer } from '@/components';
import SplitPane from 'react-split-pane';
import type { FC } from 'react';
import { KeepAlive } from 'umi';
import type  { MetricFloderItem } from '@/types/measure';
import FolderTree from './components/FolderTree';
import List from './components/MeasureTable'
const Measure: FC = () => {

  const [currentNode, setCurrentNode] = useState<MetricFloderItem>({folderId: '', pos: [] })
  const handleChange = (node: React.SetStateAction<MetricFloderItem>) => {
    setCurrentNode(node)
  }

  return (
    <PageContainer contentClassName="split-panel">
      <SplitPane
        className="board"
        defaultSize={266}
        style={{ position: 'relative', overflow: 'auto' }}
        pane2Style={{ width: 0 }}
      >
        <FolderTree onChange={handleChange} />
        <List currentNode={currentNode} />
      </SplitPane>
    </PageContainer>
  );
};


export default () => <KeepAlive>
  <Measure />
</KeepAlive>;

