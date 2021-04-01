import React, { Fragment } from 'react';
import { Switch, Checkbox } from 'antd';
import { H5 } from '@/components';
import type { FeatureTreeNode } from '@/interfaces/global';
import styles from './index.less';

function FeatureList(props: {
  data: FeatureTreeNode[];
  onChange?: (node: FeatureTreeNode) => void;
  readonly?: boolean;
}) {
  const { data, readonly, onChange } = props;
  const handleChange = (node: FeatureTreeNode) => {
    const newNode = { ...node, enable: !node.enable };
    onChange?.(newNode);
  };
  const showData = readonly ? data.filter((n) => n.enable) : data;
  return (
    <Fragment>
      {showData.map((node) => (
        <div key={node.featureCode} className={styles.listItemWrap}>
          <H5>
            {node.name}
            {!readonly && (
              <Switch
                size="small"
                checked={!!node.enable}
                onChange={() => handleChange(node)}
                style={{ marginLeft: 8 }}
              />
            )}
          </H5>
          {Array.isArray(node.children) && node.enable && node.children.length > 0 && (
            <div className={styles.contentWrap}>
              {Array.isArray(node.children) &&
                node.children.map((child) => (
                  <Checkbox
                    disabled={readonly}
                    key={child.featureCode}
                    checked={!!child.enable}
                    onChange={() => handleChange(child)}
                  >
                    {child.name}
                  </Checkbox>
                ))}
            </div>
          )}
        </div>
      ))}
    </Fragment>
  );
}

export default FeatureList;
