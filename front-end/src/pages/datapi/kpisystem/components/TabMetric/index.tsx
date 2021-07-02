import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Modal, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewMetric from './ViewMetric';
import EditMetric from './EditMetric';
import { Metric } from '@/types/datapi';
import { createMetric, deleteMetric, getMetric } from '@/services/kpisystem';
import { TreeNodeType } from '@/constants/datapi';

export interface TabMetricProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
interface MetricExportProps {
  form: any;
  DWD: [];
}

const { confirm } = Modal;

const TabMetric: FC<TabMetricProps> = ({ initialMode = 'view', fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Metric>();
  const refM = useRef<MetricExportProps>();
  const { getTree, removeTab } = useModel('kpisystem', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newMetric' && getMetricInfo(fileCode);
  }, []);

  const getMetricInfo = (metricId: string) => {
    getMetric({ metricId })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    const form = refM.current?.form;
    const DWD = refM.current?.DWD;
    console.log({ form, DWD });
    // TODO
    createMetric({})
      .then((res) => {
        if (res.success) {
          message.success(fileCode === 'newDimensiob' ? '新建维度成功' : '更新维度成功');
          getTree(TreeNodeType.DIMENSION_LABEL);
          getMetricInfo(''); // TODO
          setMode('view');
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该指标吗？',
      onOk: () =>
        deleteMetric({ metricId: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(''); // TODO
              getTree(TreeNodeType.METRIC_LABEL);
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newMetric') {
      removeTab('newMetric');
    } else {
      setMode('view');
      getMetricInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewMetric data={data as Metric} />}
      {mode === 'edit' && <EditMetric ref={refM} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Popconfirm
            key="del"
            title="您确认要删除该修饰词吗？"
            onConfirm={onDelete}
            okButtonProps={{ loading }}
            okText="确认"
            cancelText="取消"
          >
            <Button>删除</Button>
          </Popconfirm>,
        ]}
        {mode === 'edit' && [
          <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
            保存
          </Button>,
          <Button key="cancel" onClick={onCancel}>
            取消
          </Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabMetric;
