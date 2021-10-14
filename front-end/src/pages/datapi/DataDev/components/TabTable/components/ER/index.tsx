import React, { useEffect, useRef, useState } from 'react';
import G6 from '@antv/g6';
import { findDOMNode } from 'react-dom';
import type { FC } from 'react';
import { Rect, Text, Path, Group, createNodeFromReact } from '@antv/g6-react-node';
import styles from './index.less';
import { getTableRelations } from '@/services/datadev';
import { Spin } from 'antd';

export interface TableRelationProps {
  id: string;
}
const handleDataTransform = (tables: any[], tedges: any[]) => {
  const nodes = tables?.map((node) => ({
    ...node,
    id: `${node.key}`,
    key: `${node.key}`,
    attrs: node.columnInfos?.map((_: any) => ({ ..._, key: _.columnName })),
    label: `${node.key}.${node.tableComment}`,
  }));
  const edges = tedges?.map((edge) => ({
    ...edge,
    source: edge.from,
    sourceKey: edge.fromPort,
    target: edge.to,
    targetKey: edge.toPort,
    label: `${edge.text}:${edge.toText}`,
  }));
  return { nodes, edges };
};

const TableRelation: FC<TableRelationProps> = ({ id }) => {
  const [loading, setLoading] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    setLoading(true);

    const Card = ({ cfg }: { cfg: any }) => {
      const primaryKey: any[] = [];
      const columnNames: any[] = [];
      const columnComments: any[] = [];
      const columnTypes: any[] = [];
      cfg.columnInfos?.map((_: any) => {
        primaryKey.push(
          <Text key={_.id} style={{ fill: '#000', margin: [4, 8] }}>
            {_.pk ? '🔑' : ''}
          </Text>,
        );
        columnNames.push(
          <Text key={_.id} style={{ fill: '#000', margin: [4, 8] }}>
            {_.columnName || '-'}
          </Text>,
        );
        columnComments.push(
          <Text key={_.id} style={{ fill: '#000', margin: [4, 8] }}>
            {_.columnComment || '-'}
          </Text>,
        );
        columnTypes.push(
          <Text key={_.id} style={{ fill: '#000', margin: [4, 8] }}>
            {_.columnType || '-'}
          </Text>,
        );
      });

      return (
        <Group>
          <Rect style={{ radius: [8], fill: '#fff', stroke: '#1890ff' }} keyshape draggable>
            <Rect
              style={{
                fill: 'l(0) 0:#0049FF 1:#0EB7FF',
                radius: [8, 8, 0, 0],
                padding: 12,
                cursor: 'pointer',
              }}
              draggable
            >
              <Text draggable style={{ fill: '#fff' }}>
                {cfg.label}
              </Text>
            </Rect>
            <Group style={{ flexDirection: 'row', margin: [6, 12] }}>
              <Group>{primaryKey}</Group>
              <Group>{columnNames}</Group>
              <Group>{columnComments}</Group>
              <Group>{columnTypes}</Group>
            </Group>
          </Rect>
        </Group>
      );
    };
    G6.registerNode('node', createNodeFromReact(Card));

    const Line = ({ cfg }: { cfg: any }) => {
      return (
        <Group>
          <Path
            style={{
              path: [['M', 0, 0], ['L', 20, -7.5], ['L', 13.33, 0], ['L', 20, 7.5], ['Z']],
              fill: cfg.color,
              cursor: 'move',
              stroke: '#888',
            }}
            draggable
          />
        </Group>
      );
    };
    G6.registerNode('line', createNodeFromReact(Line));

    const container: any = findDOMNode(ref.current);

    const width = container.scrollWidth;
    const height = (container.scrollHeight || 800) - 20;
    const toolbar = new G6.ToolBar({ className: styles.toolbar });
    const graph = new G6.Graph({
      container,
      width,
      height,
      plugins: [toolbar],
      defaultNode: { type: 'node' },
      defaultEdge: { type: 'line', style: { stroke: '#e2e2e2', lineWidth: 2, endArrow: true } },
      modes: { default: ['drag-node', 'drag-canvas'] },
      layout: {
        type: 'dagre',
        rankdir: 'LR',
        align: 'UL',
        controlPoints: true,
        nodesepFunc: () => 0.2,
        ranksepFunc: () => 0.5,
      },
      animate: true,
      scroller: {
        enabled: true,
        pannable: true,
        pageVisible: true,
        pageBreak: false,
      },
      mousewheel: {
        enabled: true,
        modifiers: ['ctrl', 'meta'],
      },
    });
    getTableRelations({ tableId: id })
      .then((res) => {
        const tables = Array.isArray(res.data.tables) ? res.data.tables : [];
        const edges = Array.isArray(res.data.edges) ? res.data.edges : [];
        graph.data(handleDataTransform(tables, edges));
        graph.render();
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <Spin spinning={loading}>
      <div className={styles.g6} ref={ref} />
    </Spin>
  );
};

export default TableRelation;
