import React, { Fragment, useEffect, useState } from 'react';
import { Button, message, Modal } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewLabel from './ViewLabel';
import EditLabel from './EditLabel';
import {
  createObjectLabel,
  deleteObjectLabel,
  getObjectLabel,
  updateObjectLabel,
} from '@/services/objectlabel';
import { ObjectLabel } from '@/types/objectlabel';

export interface TabEnumProps {
  initialMode: 'view' | 'edit';
  fileCode: string | number;
}

const { confirm } = Modal;

const TabEnum: FC<TabEnumProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('edit');
  const [data, setData] = useState<ObjectLabel>();
  const [loading, setLoading] = useState<boolean>(false);
  const { getTree, removeTab, editLayers } = useModel('objectlabel', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
    editLayers: _.editLayers,
  }));

  useEffect(() => {
    setMode(initialMode);
  }, [initialMode]);

  const getLabel = (id: number) => {
    getObjectLabel({ id })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    // setLoading(true);
    const layers = [...editLayers];
    const params = { layers }; // TODO

    fileCode
      ? updateObjectLabel(params)
          .then((res) => {
            if (res.success) {
              message.success('更新数据标签成功');
              setMode('view');
              getTree();
            }
          })
          .catch((err) => {})
      : createObjectLabel(params)
          .then((res) => {
            if (res.success) {
              message.success('创建数据标签成功');
              setMode('view');
              getTree();
            }
          })
          .catch((err) => {});
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该数据标签吗？',
      onOk: () =>
        deleteObjectLabel({ id: data?.id as number })
          .then((res) => {
            if (res.success) {
              message.success('删除数据标签成功');
              removeTab(''); // TODO
              getTree();
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newTable') {
      removeTab('newTable');
    } else {
      setMode('view');
      getLabel(fileCode as number);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewLabel data={data as ObjectLabel} />}
      {mode === 'edit' && <EditLabel initial={data} />}
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

export default TabEnum;
