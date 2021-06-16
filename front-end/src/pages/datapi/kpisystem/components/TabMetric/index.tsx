import React, { Fragment, useEffect, useState } from 'react';
import { Button, Modal, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewMetric from './ViewMetric';
import EditMetric from './EditMetric';

export interface TabMetricProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}

const { confirm } = Modal;

const TabMetric: FC<TabMetricProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [loading, setLoading] = useState<boolean>(false);

  const { getTree, removeTab } = useModel('kpisystem', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
  }));

  const getDIMInfo = (code: string) => {};

  const onSubmit = () => {};

  const onDelete = () =>
    confirm({
      title: '您确定要删除该指标吗？',
      onOk: () => {},
    });

  const onCancel = () => {
    if (fileCode === 'newTable') {
      removeTab('newTable');
    } else {
      setMode('view');
      getDIMInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewMetric />}
      {mode === 'edit' && <EditMetric />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Button onClick={onDelete}>删除</Button>,
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
