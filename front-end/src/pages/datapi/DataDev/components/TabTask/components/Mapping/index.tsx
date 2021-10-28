import React, { forwardRef, useEffect, useImperativeHandle, useRef } from 'react';
import G6, { Graph } from '@antv/g6';
import { findDOMNode } from 'react-dom';
import { get, set, cloneDeep } from 'lodash';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';
import { MappedColumn } from '@/types/datadev';

interface MapProps {
  data: {
    srcColumns: MappedColumn[];
    destColumns: MappedColumn[];
  };
}
// const SrcNodesTitle = [{ id: 'srcNodesTitle', x: 180, y: 60, tableType: 'title' }];
// const DestNodesTitle = [{ id: 'destNodesTitle', x: 480, y: 60, tableType: 'title' }];

const Mapping: ForwardRefRenderFunction<unknown, MapProps> = (
  { data: { srcColumns, destColumns } },
  ref,
) => {
  const containerRef = useRef(null);
  const graphRef = useRef<Graph>();
  const srcMap = useRef({}); // name对MappedColumn的映射表
  const destMap = useRef({}); // 同上
  const startNode = useRef<any>(null); // 起始节点的数据对象
  useImperativeHandle(ref, () => ({
    srcMap: srcMap.current,
    destMap: destMap.current,
  }));

  const renderData = () => {
    // 画来源表的节点
    const srcNodes = srcColumns.map((column, i) => {
      const _ = cloneDeep(column);
      srcMap.current[`${_.name}-src`] = _; // 赋srcMap的值
      return {
        id: `${_.name}-src`,
        x: 160,
        y: 100 + 40 * i,
        label: `${_.name} ${_.dataType || '-'}`,
        tableType: 'src',
        data: { name: _.name, dataType: _.dataType, primaryKey: _.primaryKey },
      };
    });

    // 画去向表的节点
    const destNodes = destColumns.map((column, i) => {
      const _ = cloneDeep(column);
      destMap.current[`${_.name}-dest`] = _; // 赋destMap的值
      return {
        id: `${_.name}-dest`,
        x: 540,
        y: 100 + 40 * i,
        label: `${_.name} ${_.dataType || '-'}`,
        tableType: 'dest',
        data: { name: _.name, dataType: _.dataType, primaryKey: _.primaryKey },
      };
    });

    // 画线
    const edges = srcColumns
      .filter((_) => _.mappedColumn)
      .map((_) => ({
        source: `${_.name}-src`,
        sourceKey: `${_.name}-src`,
        target: `${_.mappedColumn?.name}-dest`,
        targetKey: `${_.mappedColumn?.name}-dest`,
      }));

    return {
      nodes: srcNodes.concat(destNodes),
      edges,
    };
  };

  useEffect(() => {
    const renderedData = renderData();
    G6.registerNode(
      'rect-node',
      {
        // draw(cfg, group) {
        //   // const isTitle = cfg.dataType === 'title';
        //   const {
        //     x,
        //     y,
        //     data: { name, dataType },
        //   } = cfg;

        //   const keyShape = group?.addShape('rect', {
        //     attrs: {
        //       x,
        //       y,
        //       height: 32,
        //       width: 240,
        //       radius: [4],
        //       fill: '#fff',
        //       stroke: '#ebedf3',
        //     },
        //   }) as IShape;
        //   group?.addShape('text', {
        //     attrs: {
        //       x: x + 20,
        //       y: y + 22,
        //       text: name,
        //       fill: '#2d3956',
        //     },
        //   });
        //   group?.addShape('text', {
        //     attrs: {
        //       x: x + 140,
        //       y: y + 22,
        //       text: dataType,
        //       fill: '#2d3956',
        //     },
        //   });

        //   return keyShape;
        // },
        /**
         * 绘制后的附加操作，默认没有任何操作
         * @param  {Object} cfg 节点的配置项
         * @param  {G.Group} group 图形分组，节点中的图形对象的容器
         */
        afterDraw(cfg, group: any) {
          // draw anchor-point circles according to the anchorPoints in afterDraw
          const bbox = group.getBBox();
          const anchorPoints = this.getAnchorPoints(cfg);
          anchorPoints.forEach((anchorPos: any, i: number) => {
            group.addShape('circle', {
              attrs: {
                r: 4,
                x: bbox.x + bbox.width * anchorPos[0], // bbox是节点，下文定义的数组代表的是百分比
                y: bbox.y + bbox.height * anchorPos[1],
                fill: '#fff',
                stroke: '#6f7b9b',
              },
              name: `anchor-point`, // the name, for searching by group.find(ele => ele.get('name') === 'anchor-point')
              anchorPointIdx: i, // flag the idx of the anchor-point circle
              links: 0, // cache the number of edges connected to this shape
              visible: true, // invisible by default, shows up when links > 1 or the node is in showAnchors state
            });
          });
        },
        /**
         * 获取锚点（相关边的连入点）
         * @param  {Object} cfg 节点的配置项
         * @return {Array|null} 锚点（相关边的连入点）的数组,如果为 null，则没有锚点
         */
        getAnchorPoints(cfg: any) {
          // 判断圆点在左边还是右边
          if (cfg.tableType === 'src') {
            return cfg.anchorPoints || [[1, 0.5]];
          } else {
            return cfg.anchorPoints || [[0, 0.5]];
          }
        },
        /**
         * 设置节点的状态，主要是交互状态，业务状态请在 draw 方法中实现
         * 单图形的节点仅考虑 selected、active 状态，有其他状态需求的用户自己复写这个方法
         * @param  {String} name 状态名称
         * @param  {Object} value 状态值
         * @param  {Node} node 节点
         */
        setState(name, value: any, item) {
          if (name === 'circle-active') {
            if (value.isHover) {
              value.target.attr({ fill: '#3445fd' });
            }
            if (!value.isHover) {
              value.target.attr({ fill: '#fff' });
            }
          }
        },
      },
      'rect',
    );

    let sourceAnchorIdx: any = null;
    let targetAnchorIdx: any = null;

    const container: any = findDOMNode(containerRef.current);
    const width = container.scrollWidth;
    const height = container.scrollHeight || 1000;
    const toolbar = new G6.ToolBar();
    const graph = new G6.Graph({
      container,
      width,
      height,
      plugins: [toolbar],
      modes: {
        default: [
          'drag-canvas',
          'scroll-canvas',
          'node',
          {
            type: 'create-edge',
            shouldBegin: (e) => {
              /* ===== check start ===== */
              const nodeModel: any = get(e, 'item._cfg.model', {});
              if (e.target && e.target.get('name') !== 'anchor-point') return false; // 锚点
              if (
                srcMap.current[nodeModel.id]?.mappedColumn ||
                destMap.current[nodeModel.id]?.mappedColumn
              ) {
                return false;
              } // 已经连线的节点
              /* ===== check end ===== */
              sourceAnchorIdx = e.target.get('anchorPointIdx');
              e.target.set('links', e.target.get('links') + 1); // cache the number of edge connected to this anchor-point circle
              startNode.current = nodeModel;
              return true;
            },
            shouldEnd: (e) => {
              /* ===== check start ===== */
              const nodeModel: any = get(e, 'item._cfg.model', {});
              if (e.target && e.target.get('name') !== 'anchor-point') return false; // avoid ending at other shapes on the node
              if (
                srcMap.current[nodeModel.id]?.mappedColumn ||
                destMap.current[nodeModel.id]?.mappedColumn
              ) {
                return false;
              } // 已经连线的节点
              if (startNode.current.id === nodeModel.id) return false; // 自环
              if (startNode.current.tableType === nodeModel.tableType) return false; // 同一张表
              /* ===== check end ===== */
              if (e.target) {
                targetAnchorIdx = e.target.get('anchorPointIdx');
                e.target.set('links', e.target.get('links') + 1); // cache the number of edge connected to this anchor-point circle
                /* ===== start 记录数据 ===== */
                if (startNode.current.tableType === 'src') {
                  const nodeModelData = cloneDeep(nodeModel.data);
                  const startNodeData = cloneDeep(startNode.current.data);
                  set(srcMap.current, `${startNode.current.id}.mappedColumn`, nodeModelData);
                  set(destMap.current, `${nodeModel.id}.mappedColumn`, startNodeData);
                }
                if (startNode.current.tableType === 'dest') {
                  set(srcMap.current, `${nodeModel.id}.mappedColumn`, startNode.current.data);
                  set(destMap.current, `${startNode.current.id}.mappedColumn`, nodeModel.data);
                }
                /* ===== end 记录数据 ===== */
                return true;
              }
              targetAnchorIdx = undefined;
              return true;
            },
          },
        ],
      },
      defaultNode: {
        type: 'rect-node',
        style: { fill: '#fff', stroke: '#ebedf3', width: 240, radius: 4 },
      },
      defaultEdge: { type: 'line', style: { stroke: '#304ffe', lineWidth: 2, lineDash: [3] } },
    });
    graphRef.current = graph;
    graph.data(renderedData);
    graph.render();
    // 当边创建完成时触发该时机事件
    graph.on('aftercreateedge', (e: any) => {
      // update the sourceAnchor and targetAnchor for the newly added edge
      graph.updateItem(e.edge, { sourceAnchor: sourceAnchorIdx, targetAnchor: targetAnchorIdx });
    });
    // if create-edge is canceled before ending, update the 'links' on the anchor-point circles
    // 调用 graph.remove / graph.removeItem 方法之后触发
    graph.on('afterremoveitem', (e: any) => {
      if (e.item && e.item.source && e.item.target) {
        const sourceNode = graph.findById(e.item.source);
        const targetNode = graph.findById(e.item.target);
        const { sourceAnchor, targetAnchor } = e.item;
        if (sourceNode && !isNaN(sourceAnchor)) {
          const sourceAnchorShape = sourceNode
            .getContainer()
            .find(
              (ele) =>
                ele.get('name') === 'anchor-point' && ele.get('anchorPointIdx') === sourceAnchor,
            );
          sourceAnchorShape && sourceAnchorShape.set('links', sourceAnchorShape.get('links') - 1);
        }
        if (targetNode && !isNaN(targetAnchor)) {
          const targetAnchorShape = targetNode
            .getContainer()
            .find(
              (ele) =>
                ele.get('name') === 'anchor-point' && ele.get('anchorPointIdx') === targetAnchor,
            );
          targetAnchorShape && targetAnchorShape.set('links', targetAnchorShape.get('links') - 1);
        }
        /* ===== start 删除数据 ===== */
        // 怎么分辨这儿的source和target分别来自哪张表？
        // 一时规避，通过两边做检查来删除
        if (srcMap.current[e.item.source]) {
          delete srcMap.current[e.item.source].mappedColumn;
        }
        if (srcMap.current[e.item.target]) {
          delete srcMap.current[e.item.target].mappedColumn;
        }
        if (destMap.current[e.item.source]) {
          delete destMap.current[e.item.source].mappedColumn;
        }
        if (destMap.current[e.item.target]) {
          delete destMap.current[e.item.target].mappedColumn;
        }
        /* ===== end 删除数据 ===== */
      }
    });
    graph.on('afteradditem', (e) => {
      if (e.item && e.item.getType() === 'edge') {
        graph.updateItem(e.item, {
          sourceAnchor: sourceAnchorIdx,
        });
      }
    });
    // 小圆点hover效果
    graph.on('anchor-point:mouseenter', (e: any) => {
      graph.setItemState(e.item, 'circle-active', { target: e.target, isHover: true });
    });
    graph.on('anchor-point:mouseleave', (e: any) => {
      graph.setItemState(e.item, 'circle-active', { target: e.target, isHover: false });
    });
    // 连线的hover效果
    graph.on('edge:mouseenter', function (e) {
      const edge: any = e.item;
      const model = edge.getModel();
      model.oriLabel = model.label;
      graph.updateItem(edge, {
        style: { lineDash: [0], shadowColor: '#304ffe', shadowBlur: 2, cursor: 'pointer' },
      });
    });
    graph.on('edge:mouseleave', function (e) {
      const edge: any = e.item;
      graph.setItemState(edge, 'hover', false);
      graph.updateItem(edge, { style: { lineDash: [3], shadowColor: undefined, shadowBlur: 0 } });
    });
    // 连线的点击删除功能
    graph.on('edge:click', function (e: any) {
      graph.removeItem(e.item);
    });
  }, []);

  useEffect(() => {
    const renderedData = renderData();
    graphRef.current?.changeData(renderedData);
  }, [srcColumns, destColumns]);

  return <div className={styles.mapping} ref={containerRef} />;
};

export default forwardRef(Mapping);
