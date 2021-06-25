import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Modal, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewDIM from './ViewDimension';
import EditDIM from './EditDimension';
import { createDimension, deleteDimension, getDimension } from '@/services/kpisystem';
import { Dimension } from '@/types/datapi';
import { TreeNodeType } from '@/constants/datapi';

export interface TabDimensionProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
interface DimensionExportProps {
  form: any;
  DIM: [];
  DWD: [];
}

const { confirm } = Modal;

const TabDimension: FC<TabDimensionProps> = ({ initialMode = 'view', fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Dimension>();
  const refD = useRef<DimensionExportProps>();
  const { getTree, removeTab } = useModel('kpisystem', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newDimension' && getDimensionInfo(fileCode);
  }, []);

  const getDimensionInfo = (dimensionId: string) => {
    getDimension({ dimensionId })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    const form = refD.current?.form;
    const DIM = refD.current?.DIM;
    const DWD = refD.current?.DWD;
    console.log({ form, DIM, DWD });
    // TODO
    createDimension({})
      .then((res) => {
        if (res.success) {
          message.success(fileCode === 'newDimensiob' ? '新建维度成功' : '更新维度成功');
          getTree(TreeNodeType.DIMENSION_LABEL);
          getDimensionInfo(''); // TODO
          setMode('view');
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该维度吗？',
      onOk: () =>
        deleteDimension({ dimensionId: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(''); // TODO
              getTree(TreeNodeType.DIMENSION_LABEL);
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newDimension') {
      removeTab('newDimension');
    } else {
      setMode('view');
      getDimensionInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewDIM data={data as Dimension} />}
      {mode === 'edit' && <EditDIM ref={refD} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Popconfirm
            key="del"
            title="您确认要删除该维度吗？"
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

export default TabDimension;
