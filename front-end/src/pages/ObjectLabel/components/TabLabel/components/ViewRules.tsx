import React, { useState } from 'react';
import { Modal, Collapse, Space } from 'antd';
import type { FC } from 'react';

interface ViewRulesProps {
  visible: boolean;
  onCancel: () => void;
}

const { Panel } = Collapse;

const ViewRules: FC<ViewRulesProps> = ({ visible, onCancel }) => {
  const [data, setData] = useState();

  return (
    <Modal title="查看规则" visible={visible} onCancel={onCancel} bodyStyle={{ padding: 24 }}>
      <Collapse style={{ background: '#fff' }}>
        <Panel header="维度信息" key="1">
          <Space direction="vertical">
            {[{}].map((DIM) => (
              <Space>
                <span>市级</span>
                <span>杭州市</span>
              </Space>
            ))}
          </Space>
        </Panel>
        <Panel header="指标信息" key="2">
          <Space direction="vertical">
            {[{}].map((DIM) => (
              <Space>
                <span>GMV</span>
                <span>大于等于</span>
                <span>100,000</span>
              </Space>
            ))}
          </Space>
        </Panel>
      </Collapse>
    </Modal>
  );
};

export default ViewRules;
