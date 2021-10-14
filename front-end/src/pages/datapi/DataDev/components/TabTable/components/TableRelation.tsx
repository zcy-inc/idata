import React, { useEffect, useRef, useState } from 'react';
import G6 from '@antv/g6';
import { Spin } from 'antd';
import { findDOMNode } from 'react-dom';
import type { FC } from 'react';
import styles from '../../../index.less';

import { getTableRelations } from '@/services/datadev';

export interface TableRelationProps {
  id: any;
}

const { Util, registerBehavior, registerEdge, registerNode } = G6;
const itemHeight = 30;
const fontFamily =
  'Avenir,-apple-system,BlinkMacSystemFont,Segoe UI,PingFang SC,Hiragino Sans GB,Microsoft YaHei,Helvetica Neue,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol';
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
    const isInBBox = (point: any, bbox: any) => {
      const { x, y } = point;
      const { minX, minY, maxX, maxY } = bbox;

      return x < maxX && x > minX && y > minY && y < maxY;
    };
    // 注册自定义的Behavior dice-er-scroll
    registerBehavior('dice-er-scroll', {
      getDefaultCfg() {
        return { multiple: true };
      },
      getEvents() {
        return {
          itemHeight: 50,
          wheel: 'scorll',
          click: 'click',
          'node:mousemove': 'move',
        };
      },
      scorll(e: any) {
        e.preventDefault();
        const { graph } = this;
        const nodes = graph.getNodes().filter((n: any) => {
          const bbox = n.getBBox();
          return isInBBox(graph.getPointByClient(e.clientX, e.clientY), bbox);
        });
        if (nodes) {
          nodes.forEach((node: any) => {
            const model = node.getModel();
            if (model.attrs.length < 9) return;
            const idx = model.startIndex || 0;
            let startX = model.startX || 0.5;
            let startIndex = idx + e.deltaY * 0.02;
            startX -= e.deltaX;
            if (startIndex < 0) startIndex = 0;
            if (startX > 0) startX = 0;
            if (startIndex > model.attrs.length - 1) startIndex = model.attrs.length - 1;
            graph.update(node, { startIndex, startX });
          });
        }
      },
      click(e: any) {
        const { graph } = this;
        const { item, shape } = e;
        if (!item) return;
        if (shape.get('name') === 'collapse') {
          graph.updateItem(item, { collapsed: true, size: [300, 50] });
          setTimeout(() => graph.layout(), 100);
        } else if (shape.get('name') === 'expand') {
          graph.updateItem(item, { collapsed: false, size: [300, 500] });
          setTimeout(() => graph.layout(), 100);
        }
      },
      move(e: any) {
        const name = e.shape.get('name');
        const { item } = e;

        if (name && name.startsWith('item')) {
          this.graph.updateItem(item, { selectedIndex: Number(name.split('-')[1]) });
        } else {
          this.graph.updateItem(item, { selectedIndex: NaN });
        }
      },
    });
    // 注册自定义的Edge dice-er-edge
    registerEdge('dice-er-edge', {
      /**
       * 绘制边，包含文本
       * @param  {Object} cfg 边的配置项
       * @param  {G.Group} group 图形分组，边中的图形对象的容器
       * @return {G.Shape} 绘制的图形，通过 node.get('keyShape') 可以获取到
       */
      draw(cfg: any, group: any) {
        const edge = group.cfg.item;
        const sourceNode = edge.getSource().getModel();
        const targetNode = edge.getTarget().getModel();
        const sourceIndex = sourceNode.attrs.findIndex((e) => e.columnName === cfg.sourceKey);
        const sourceStartIndex = sourceNode.startIndex || 0;

        let sourceY = 15;
        if (!sourceNode.collapsed && sourceIndex > sourceStartIndex - 1) {
          sourceY = 30 + (sourceIndex - sourceStartIndex + 0.5) * 30;
          sourceY = Math.min(sourceY, 300);
        }

        const targetIndex = targetNode.attrs.findIndex((e) => e.key === cfg.targetKey);
        const targetStartIndex = targetNode.startIndex || 0;

        let targetY = 15;
        if (!targetNode.collapsed && targetIndex > targetStartIndex - 1) {
          targetY = (targetIndex - targetStartIndex + 0.5) * 30 + 30;
          targetY = Math.min(targetY, 300);
        }

        const startPoint = { ...cfg.startPoint };
        const endPoint = { ...cfg.endPoint };

        startPoint.y = startPoint.y + sourceY;
        endPoint.y = endPoint.y + targetY;

        // 绘制线
        let shape;
        if (sourceNode.id !== targetNode.id) {
          // 节点到节点的线
          shape = group.addShape('path', {
            attrs: {
              stroke: '#5B8FF9',
              path: [
                ['M', startPoint.x, startPoint.y],
                [
                  'C',
                  endPoint.x / 3 + (2 / 3) * startPoint.x,
                  startPoint.y,
                  endPoint.x / 3 + (2 / 3) * startPoint.x,
                  endPoint.y,
                  endPoint.x,
                  endPoint.y,
                ],
              ],
              endArrow: true,
            },
            name: 'path-shape',
          });
        } else if (!sourceNode.collapsed) {
          // 节点到自身的线
          let gap = Math.abs((startPoint.y - endPoint.y) / 3);
          if (startPoint.index === 1) {
            gap = -gap;
          }
          shape = group.addShape('path', {
            attrs: {
              stroke: '#5B8FF9',
              path: [
                ['M', startPoint.x, startPoint.y],
                [
                  'C',
                  startPoint.x - gap,
                  startPoint.y,
                  startPoint.x - gap,
                  endPoint.y,
                  startPoint.x,
                  endPoint.y,
                ],
              ],
              endArrow: true,
            },
            name: 'path-shape',
          });
        }

        return shape;
      },
      /**
       * 绘制后的附加操作，默认没有任何操作
       * @param  {Object} cfg 边的配置项
       * @param  {G.Group} group 图形分组，边中的图形对象的容器
       */
      afterDraw(cfg: any, group: any) {
        const edge = group.cfg.item;
        const sourceNode = edge.getSource().getModel();
        const targetNode = edge.getTarget().getModel();
        if (sourceNode.collapsed && targetNode.collapsed) {
          return;
        }
        const path = group.find((element: any) => element.get('name') === 'path-shape');
        const labelStyle = Util.getLabelPosition(path, 0.5, 0, 0, true);
        // 绘制线上的文字
        const label = group.addShape('text', {
          attrs: {
            ...labelStyle,
            text: cfg.label || '',
            fill: '#000',
            textAlign: 'left',
            lineWidth: 1,
          },
        });
        label.rotateAtStart(labelStyle.rotate);
      },
    });
    // 注册自定义的Node dice-er-box
    registerNode('dice-er-box', {
      draw(cfg: any, group: any) {
        const width = 260;
        const height = 316;
        const itemCount = 10;
        const boxStyle: any = { stroke: '#3F88FE', fill: '#fff' }; // 控制node的主色

        const { attrs = [], startIndex = 0, selectedIndex, collapsed } = cfg;
        const list = attrs; // data传入的完整list

        // 当前container中显示的list
        const afterList = list.slice(
          Math.floor(startIndex),
          Math.floor(startIndex + itemCount - 1),
        );

        const offsetY = (0.5 - (startIndex % 1)) * itemHeight + 30;
        const keyshape = group.addShape('rect', {
          attrs: { x: 0, y: 0, width, height: collapsed ? 45 : height, ...boxStyle },
          draggable: true,
        });
        // 绘制标题的行
        group.addShape('rect', {
          attrs: { fill: boxStyle.stroke, height: 30, width },
          draggable: true,
        });

        let fontLeft = 12;
        // 绘制标题的文本
        group.addShape('text', {
          attrs: { y: 22, x: fontLeft, fill: '#fff', text: cfg.label },
          draggable: true,
        });
        // 绘制收起展开的行
        group.addShape('rect', {
          attrs: {
            x: 0,
            y: collapsed ? 30 : 300,
            height: 15,
            width,
            fill: '#eee',
            radius: [0, 0, boxStyle.radius, boxStyle.radius],
            cursor: 'pointer',
          },
          draggable: true,
          name: collapsed ? 'expand' : 'collapse',
        });
        // 绘制收起展开行的文本
        group.addShape('text', {
          attrs: {
            x: width / 2 - 6,
            y: (collapsed ? 30 : 300) + 12,
            text: collapsed ? '+' : '-',
            width,
            fill: '#000',
            radius: [0, 0, boxStyle.radius, boxStyle.radius],
            cursor: 'pointer',
          },
          draggable: true,
          name: collapsed ? 'expand' : 'collapse',
        });
        // 绘制收起展开行的 边框 ？
        // 如果是收起状态的，到这里就返回，不绘制下面的 container
        if (collapsed) return keyshape;

        // 绘制container，即展开状态下节点里的内容
        const listContainer = group.addGroup({});
        listContainer.setClip({
          type: 'rect',
          attrs: { x: -8, y: 30, width: width + 16, height: 300 - 30 },
          draggable: true,
        });
        // 绘制container的背景
        listContainer.addShape({
          type: 'rect',
          attrs: { x: 1, y: 30, width: width - 2, height: 300 - 30, fill: '#fff' },
          draggable: true,
        });
        // 列表长度大于容器的最大长度，即scroll的场景
        if (list.length > itemCount) {
          const barStyle = {
            width: 4,
            padding: 0,
            boxStyle: { stroke: '#00000022' }, // scrollbar-track 轨道
            innerStyle: { fill: '#00000022' }, // scrollbar-thumb 滑块
          };
          // 绘制 scrollbar-track
          listContainer.addShape('rect', {
            attrs: {
              y: 30,
              x: width - barStyle.padding - barStyle.width,
              width: barStyle.width,
              height: height - 30,
              ...barStyle.boxStyle,
            },
            draggable: true,
          });
          // 计算 scrollbar-thumb 的 y坐标
          const indexHeight =
            afterList.length > itemCount ? (afterList.length / list.length) * height : 10;
          // 绘制 scrollbar-thumb
          listContainer.addShape('rect', {
            attrs: {
              y: 30 + barStyle.padding + (startIndex / list.length) * (height - 30),
              x: width - barStyle.padding - barStyle.width,
              width: barStyle.width,
              height: Math.min(height, indexHeight),
              ...barStyle.innerStyle,
            },
            draggable: true,
          });
        }
        if (afterList) {
          afterList.forEach((e: any, i: any) => {
            const isSelected = Math.floor(startIndex) + i === Number(selectedIndex);
            let { key = '', type, columnName = '-', columnComment = '-', columnType = '-', pk } = e;
            if (type) key = `${key} - ${type}`; // 组装key
            // 绘制行
            listContainer.addShape('rect', {
              attrs: {
                x: 1,
                y: i * itemHeight - itemHeight / 2 + offsetY,
                width: width - 4,
                height: itemHeight,
                lineWidth: 1,
                cursor: 'pointer',
                fill: '#F4F5F8',
              },
              name: `item-${Math.floor(startIndex) + i}-content`,
              draggable: true,
            });
            // 绘制两侧的小圆点（rawData的配置有规范吗？还是随自己配？）
            if (!cfg.hideDot) {
              listContainer.addShape('circle', {
                attrs: {
                  x: 0,
                  y: i * itemHeight + offsetY,
                  r: 3,
                  stroke: boxStyle.stroke,
                  fill: '#fff',
                  radius: 2,
                  lineWidth: 1,
                  cursor: 'pointer',
                },
                draggable: true,
              });
              listContainer.addShape('circle', {
                attrs: {
                  x: width,
                  y: i * itemHeight + offsetY,
                  r: 3,
                  stroke: boxStyle.stroke,
                  fill: '#fff',
                  radius: 2,
                  lineWidth: 1,
                  cursor: 'pointer',
                },
                draggable: true,
              });
            }
            const textAttrs = {
              y: i * itemHeight + offsetY + 6,
              text: '-',
              fontSize: 12,
              fill: '#15172F',
              fontFamily,
              full: e,
              fontWeight: isSelected ? 500 : 100,
              cursor: 'pointer',
            };
            // 绘制行
            // 主键的钥匙
            listContainer.addShape('text', {
              attrs: { ...textAttrs, x: 8, text: pk ? '🔑' : '' },
              draggable: true,
              name: `item-${Math.floor(startIndex) + i}`,
            });
            // 列名
            listContainer.addShape('text', {
              attrs: {
                ...textAttrs,
                x: 32,
                text: columnName.length > 7 ? columnName.substring(0, 7) + '...' : columnName,
              },
              draggable: true,
              name: `item-${Math.floor(startIndex) + i}`,
            });
            // 备注？
            listContainer.addShape('text', {
              attrs: {
                ...textAttrs,
                x: 88,
                text:
                  columnComment.length > 15
                    ? columnComment.substring(0, 12) + '...'
                    : columnComment,
              },
              draggable: true,
              name: `item-${Math.floor(startIndex) + i}`,
            });
            // 类型
            listContainer.addShape('text', {
              attrs: { ...textAttrs, x: 200, text: columnType },
              draggable: true,
              name: `item-${Math.floor(startIndex) + i}`,
            });
          });
        }

        return keyshape;
      },
      getAnchorPoints() {
        return [
          [0, 0],
          [1, 0],
        ];
      },
    });

    const container: any = findDOMNode(ref.current);

    const width = container.scrollWidth;
    const height = (container.scrollHeight || 800) - 20;
    const toolbar = new G6.ToolBar({ className: styles.toolbar });
    const graph = new G6.Graph({
      container,
      width,
      height,
      plugins: [toolbar],
      defaultNode: { size: [300, 400], type: 'dice-er-box' },
      defaultEdge: {
        type: 'dice-er-edge',
        style: { stroke: '#e2e2e2', lineWidth: 4, endArrow: true },
      },
      modes: { default: ['drag-node', 'dice-er-scroll', 'drag-canvas'] },
      layout: {
        type: 'dagre',
        rankdir: 'LR',
        align: 'UL',
        controlPoints: true,
        nodesepFunc: () => 0.2,
        ranksepFunc: () => 0.5,
      },
      animate: true,
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
