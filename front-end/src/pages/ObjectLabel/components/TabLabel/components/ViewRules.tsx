import React, { Fragment } from 'react';
import { Modal, Collapse, Space, Tabs } from 'antd';
import type { FC } from 'react';

import { RuleLayer } from '@/types/objectlabel';
import { ConditionMap } from '../constants';

interface ViewRulesProps {
  layers: RuleLayer[];
  visible: boolean;
  onCancel: () => void;
}

const { TabPane } = Tabs;
const { Panel } = Collapse;

const ViewRules: FC<ViewRulesProps> = ({ layers, visible, onCancel }) => {
  return (
    <Modal title="查看规则" visible={visible} onCancel={onCancel} bodyStyle={{ padding: 16 }}>
      <Tabs>
        {layers.map((layer) => (
          <TabPane key={layer.layerId} tab={layer.layerName}>
            {/* 规则 */}
            {layer.ruleDef.rules.map((rule) => (
              <Fragment>
                <p style={{ marginBottom: 8, fontSize: 16, fontWeight: 'bold' }}>{rule.ruleName}</p>
                <Collapse
                  style={{ background: '#fff', marginBottom: 16 }}
                  defaultActiveKey={['indicator', 'dimension']}
                >
                  <Panel header="指标信息" key="indicator">
                    <Space direction="vertical">
                      {rule.indicatorDefs.map((indicator) => (
                        <Space>
                          <span>{indicator.indicatorName}</span>
                          <span>{ConditionMap[indicator.condition]}</span>
                          <span>{indicator.params}</span>
                        </Space>
                      ))}
                    </Space>
                  </Panel>
                  <Panel header="维度信息" key="dimension">
                    <Space direction="vertical">
                      {rule.dimensionDefs.map((dimension) => (
                        <Space>
                          <span>{dimension.dimensionName}</span>
                          <span>{dimension.params}</span>
                        </Space>
                      ))}
                    </Space>
                  </Panel>
                </Collapse>
              </Fragment>
            ))}
          </TabPane>
        ))}
      </Tabs>
    </Modal>
  );
};

export default ViewRules;
