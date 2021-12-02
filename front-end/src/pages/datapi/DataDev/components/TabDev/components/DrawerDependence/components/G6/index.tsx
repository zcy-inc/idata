import React, { forwardRef, useEffect, useRef } from 'react';
import G6, { Graph } from '@antv/g6';
import { findDOMNode } from 'react-dom';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';

import { data } from './mock';

interface MapProps {}

const Mapping: ForwardRefRenderFunction<unknown, MapProps> = ({}, ref) => {
  const containerRef = useRef(null);
  const graphRef = useRef<Graph>();

  useEffect(() => {
    const container: any = findDOMNode(containerRef.current);
    const width = container.scrollWidth;
    const height = container.scrollHeight || 1000;
    const toolbar = new G6.ToolBar();
    const graph = new G6.TreeGraph({
      container,
      width,
      height,
      plugins: [toolbar],
      modes: {
        default: [
          {
            type: 'collapse-expand',
            onChange(item: any, collapsed) {
              const data = item.get('model');
              data.collapsed = collapsed;
              return true;
            },
          },
          'drag-canvas',
          'zoom-canvas',
        ],
      },
      defaultNode: {
        size: 26,
        anchorPoints: [
          [0, 0.5],
          [1, 0.5],
        ],
      },
      defaultEdge: {
        type: 'cubic-horizontal',
      },
      layout: {
        type: 'mindmap',
        direction: 'H',
        getHeight: () => {
          return 16;
        },
        getWidth: () => {
          return 16;
        },
        getVGap: () => {
          return 10;
        },
        getHGap: () => {
          return 50;
        },
        getSide: (d: any) => {
          if (d.id === 'Classification') {
            return 'left';
          }
          return 'right';
        },
      },
    });
    graphRef.current = graph;
    graph.data(data);
    graph.render();
  }, []);

  return <div className={styles.mapping} ref={containerRef} />;
};

export default forwardRef(Mapping);
