import React, { forwardRef, useEffect, useRef, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import G6, { Graph, Item as G6Item } from '@antv/g6';
import { findDOMNode } from 'react-dom';
import { Button, Form, InputNumber, message, Modal, Row, Select, Spin } from 'antd';
import { get } from 'lodash';
import type { ForwardRefRenderFunction } from 'react';
import styles from './index.less';

import { data as mockData } from './mock';
import { DependenceTreeNode, Task } from '@/types/datadev';
import { Environments } from '@/constants/datasource';
import {
  getDependenceTree,
  getDependenceTreeNodeList,
  getTaskRunningLog,
  rerunTask,
} from '@/services/datadev';
import { JobStatus } from '@/constants/datadev';

interface MapProps {
  env: Environments;
  data?: Task;
}

const { Item } = Form;
// 0 primary, 1 light
const JobStatusColorMap = {
  [JobStatus.READY]: ['#FF9324', '#FFF8F2'],
  [JobStatus.RUNNING]: ['#304FFE', '#E8F1FF'],
  [JobStatus.FAIL]: ['#F1331F', '#FFF6F5'],
  [JobStatus.SUCCESS]: ['#05CC87', '#EFFFF5'],
  [JobStatus.OTHER]: ['#2D3956', '#EBEDF3'],
};
const DefaultColor = ['#2D3956', '#EBEDF3'];

const Mapping: ForwardRefRenderFunction<unknown, MapProps> = ({ data, env }, ref) => {
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState<DependenceTreeNode>();
  const [visibleLog, setVisibleLog] = useState(false);
  const [log, setLog] = useState('');
  const [loading, setLoading] = useState(false);
  const [options, setOptions] = useState<any[]>([]);

  const containerRef = useRef(null);
  const graphRef = useRef<Graph>();
  const curNodeRef = useRef<G6Item | string>('');
  const [form] = Form.useForm();

  useEffect(() => {
    if (data) {
      G6.registerNode(
        'tree-node',
        {
          drawShape(cfg: any, group: any) {
            const rect = group.addShape('rect', {
              attrs: {
                fill: JobStatusColorMap[cfg.jobStatus]?.[1] || DefaultColor[1],
                stroke: cfg.highLight
                  ? JobStatusColorMap[cfg.jobStatus]?.[0] || DefaultColor[0]
                  : 'rgba(0, 0, 0, 0.1)',
                x: 0,
                y: 0,
                width: 1,
                height: 1,
                radius: [4],
              },
              name: 'rect-shape',
            });
            const text = group.addShape('text', {
              attrs: {
                text: cfg.jobName,
                x: 0,
                y: 0,
                textBaseline: 'middle',
                fill: '#141736',
              },
              name: 'text-shape',
            });
            const bbox = text.getBBox();
            rect.attr({
              width: bbox.width + 38,
              height: bbox.height + 20,
            });
            text.attr({
              x: 24,
              y: bbox.height / 2 + 10,
            });
            group.addShape('circle', {
              attrs: {
                x: 16,
                y: bbox.height / 2 + 10,
                r: 3,
                fill: JobStatusColorMap[cfg.jobStatus]?.[0] || DefaultColor[0],
              },
              name: 'status-dot',
            });
            return rect;
          },
        },
        'single-node',
      );

      const container: any = findDOMNode(containerRef.current);
      const width = container.scrollWidth;
      const height = container.scrollHeight || 800;
      const toolbar = new G6.ToolBar();
      const graph = new G6.TreeGraph({
        container,
        width,
        height,
        plugins: [toolbar],
        modes: {
          default: ['drag-canvas', 'zoom-canvas'],
        },
        defaultNode: {
          type: 'tree-node',
          anchorPoints: [
            [0, 0.5],
            [1, 0.5],
          ],
        },
        defaultEdge: {
          type: 'cubic-horizontal',
          style: {
            stroke: '#BFC4D5',
            endArrow: { path: G6.Arrow.triangle(3, 3), fill: '#BFC4D5' },
          },
        },
        layout: {
          type: 'compactBox',
          direction: 'H',
          getId(d: any) {
            return d.id;
          },
          getHeight() {
            return 16;
          },
          getWidth() {
            return 16;
          },
          getVGap() {
            return 20;
          },
          getHGap() {
            return 80;
          },
          getSide: (d: any) => {
            if (d.data.relation === 'prev') {
              return 'left';
            }
            return 'right';
          },
        },
      });
      graphRef.current = graph;
      G6.Util.traverseTree(mockData, (item: any) => {
        item.id = item.name;
      });
      graph.on('node:mouseenter', (e: any) => {
        const node = e.item;
        const jobStatus = get(node, '_cfg.model.jobStatus', JobStatus.OTHER);
        graph.updateItem(node, {
          style: { stroke: JobStatusColorMap[jobStatus]?.[0] || DefaultColor[0] },
        });
      });
      graph.on('node:mouseleave', (e: any) => {
        const node = e.item;
        const highLight = get(node, '_cfg.model.highLight', false);
        if (!highLight) {
          graph.updateItem(node, { style: { stroke: 'rgba(0, 0, 0, 0.1)' } });
        }
      });
      graph.on('node:click', (e: any) => {
        const node = e.item;
        const jobStatus = get(node, '_cfg.model.jobStatus', JobStatus.OTHER);
        graph.updateItem(node, {
          style: { stroke: JobStatusColorMap[jobStatus]?.[0] || DefaultColor[0] },
        });
        const model = get(node, '_cfg.model', {});
        setCurrent(model);
        setVisible(true);
        curNodeRef.current = node;
      });
      getDependenceTreeWrapped();
    }
  }, []);

  useEffect(() => {
    getDependenceTreeWrapped();
  }, [data, env]);

  /**
   * 获取树
   */
  const getDependenceTreeWrapped = () => {
    setLoading(true);
    const values = form.getFieldsValue();
    getDependenceTree({
      id: data?.id as number,
      env,
      ...values,
    })
      .then((res) => {
        const values = {
          preLevel: res.data.prevLevel,
          nextLevel: res.data.nextLevel,
        };
        form.setFieldsValue(values);
        graphRef.current?.data(res.data);
        graphRef.current?.render();
        graphRef.current?.fitView();
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  /**
   * 获取树的检索平铺列表
   */
  const getDependenceTreeNodeListWrapped = (searchName: string) => {
    getDependenceTreeNodeList({ searchName, env, jobId: data?.id as number })
      .then((res) => {
        setOptions(res.data);
      })
      .catch((err) => {});
  };

  /**
   * 重跑 / 重跑下游
   */
  const rerunTaskWrapped = (runPost: boolean) => {
    rerunTask({ id: data?.id as number, env, runPost })
      .then((res) => {
        if (res.data) {
          message.success('重跑成功');
        } else {
          message.success('重跑失败');
        }
      })
      .catch((err) => {});
  };

  return (
    <>
      <Form
        form={form}
        colon={false}
        className={styles.form}
        initialValues={{
          preLevel: 1,
          nextLevel: 1,
        }}
      >
        <Row style={{ justifyContent: 'space-between' }}>
          <Item name="searchJobId">
            <Select
              showSearch
              placeholder="搜索任务名称"
              style={{ width: 300 }}
              filterOption={false}
              onSearch={(v) => getDependenceTreeNodeListWrapped(v)}
              options={options.map((_) => ({ label: _.name, value: _.id }))}
              onChange={getDependenceTreeWrapped}
              allowClear
            />
          </Item>
          <Row>
            <Item name="preLevel" label="上游层数">
              <InputNumber
                onStep={getDependenceTreeWrapped}
                onPressEnter={getDependenceTreeWrapped}
              />
            </Item>
            <Item name="nextLevel" label="下游层数" style={{ marginLeft: 20 }}>
              <InputNumber
                onStep={getDependenceTreeWrapped}
                onPressEnter={getDependenceTreeWrapped}
              />
            </Item>
          </Row>
        </Row>
      </Form>
      <Spin spinning={loading}>
        <div className={styles.mapping} ref={containerRef} />
      </Spin>
      <Modal
        title="信息"
        visible={visible}
        onCancel={() => {
          setVisible(false);
          const highLight = get(curNodeRef.current, '_cfg.model.highLight', false);
          if (!highLight) {
            graphRef.current?.updateItem(curNodeRef.current, {
              style: { stroke: 'rgba(0, 0, 0, 0.1)' },
            });
          }
        }}
        footer={[
          <Button
            onClick={() => {
              if (current?.taskId) {
                getTaskRunningLog({
                  id: current?.jobId as number,
                  env,
                  taskId: current?.taskId as number,
                })
                  .then((res) => {
                    setLog(res.data);
                    setVisibleLog(true);
                  })
                  .catch((err) => {});
              } else {
                message.info('该任务暂无日志');
              }
            }}
          >
            查看日志
          </Button>,
          <Button onClick={() => rerunTaskWrapped(false)}>重跑</Button>,
          <Button onClick={() => rerunTaskWrapped(true)}>重跑下游</Button>,
        ]}
      >
        <p style={{ color: '#141736' }}>{current?.jobName || '-'}</p>
        <p style={{ color: '#737B96' }}>最近更新时间：{current?.lastRunTime || '-'}</p>
      </Modal>
      <Modal
        title="日志"
        visible={visibleLog}
        onCancel={() => setVisibleLog(false)}
        footer={null}
        bodyStyle={{ padding: 16 }}
      >
        <MonacoEditor
          height="400"
          language="sql"
          theme="vs-dark"
          value={log}
          options={{ readOnly: true }}
        />
      </Modal>
    </>
  );
};

export default forwardRef(Mapping);
