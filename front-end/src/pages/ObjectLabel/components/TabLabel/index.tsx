import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Modal } from 'antd';
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
import { NewObjectLabelOriginId } from './constants';

export interface TabObjectLabelProps {
  initialMode: 'view' | 'edit';
  originId: number;
}

const { confirm } = Modal;

const TabObjectLabel: FC<TabObjectLabelProps> = ({ initialMode = 'view', originId }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<ObjectLabel>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();
  const { getTree, removeTab, editLayers } = useModel('objectlabel', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
    editLayers: _.editLayers,
  }));

  useEffect(() => {
    setMode(initialMode);
    if (initialMode === 'view' && originId !== NewObjectLabelOriginId) {
      getLabel(originId);
    }
  }, [initialMode]);

  const getLabel = (originId: number) => {
    getObjectLabel({ id: originId })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    const layers = [...editLayers];
    form
      .validateFields()
      .then(() => {
        const values = form.getFieldsValue();
        const params = { ...values, ruleLayers: layers };
        originId === NewObjectLabelOriginId
          ? createObjectLabel(params)
              .then((res) => {
                if (res.success) {
                  message.success('创建数据标签成功');
                  setMode('view');
                  getTree();
                  getLabel(res.data);
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false))
          : updateObjectLabel({ ...params, id: data!.id })
              .then((res) => {
                if (res.success) {
                  message.success('更新数据标签成功');
                  setMode('view');
                  getTree();
                  getLabel(originId);
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
      })
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该数据标签吗？',
      onOk: () =>
        deleteObjectLabel({ id: data!.id })
          .then((res) => {
            if (res.success) {
              message.success('删除数据标签成功');
              getTree();
              if (originId === NewObjectLabelOriginId) {
                removeTab('newObjectLabel');
              } else {
                removeTab(`${data!.originId}`);
              }
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (originId === NewObjectLabelOriginId) {
      removeTab('newObjectLabel');
    } else {
      setMode('view');
      getLabel(originId);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewLabel data={data as ObjectLabel} />}
      {mode === 'edit' && <EditLabel initial={data} form={form} />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Button key="del" onClick={onDelete}>
            删除
          </Button>,
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

export default TabObjectLabel;
