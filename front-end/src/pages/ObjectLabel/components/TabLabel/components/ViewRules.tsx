import React, { Fragment } from 'react';
import { Modal, Collapse, Space, Tabs, Button } from 'antd';
import { CaretRightOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import styles from '../../../index.less';

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
    <Modal
      className={styles['reset-modal']}
      title="查看规则"
      visible={visible}
      onCancel={onCancel}
      bodyStyle={{ padding: 16 }}
      footer={[
        <Button type="primary" onClick={onCancel}>
          确定
        </Button>,
      ]}
    >
      <Tabs>
        {layers?.map((layer) => (
          <TabPane key={layer.layerId} tab={layer.layerName}>
            {/* 规则 */}
            {layer.ruleDef?.rules?.map((rule) => (
              <Fragment key={rule.ruleId}>
                <p style={{ marginBottom: 8, fontSize: 16, fontWeight: 'bold' }}>{rule.ruleName}</p>
                <Collapse
                  style={{ background: '#fff', marginBottom: 16 }}
                  defaultActiveKey={['indicator', 'dimension']}
                  expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                >
                  <Panel header="指标信息" key="indicator">
                    <Space direction="vertical">
                      {rule.indicatorDefs?.map((indicator) => (
                        <Space key={indicator.indicatorCode}>
                          <span>{indicator.indicatorName}</span>
                          <span>{ConditionMap[indicator.condition]}</span>
                          <span>{indicator.params}</span>
                        </Space>
                      ))}
                    </Space>
                  </Panel>
                  <Panel header="维度信息" key="dimension">
                    <Space direction="vertical">
                      {rule.dimensionDefs?.map((dimension) => (
                        <Space key={dimension.dimensionCode}>
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
