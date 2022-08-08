import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';

import { getUDF, createUDF, updateUDF, deleteUDF } from '@/services/datadev';
import { IPane } from '@/models/datadev';
import { UDF } from '@/types/datadev';

import EditUDF from './components/EditUDF';
import ViewUDF from './components/ViewUDF';

export interface TabUDFProps {
  pane: IPane;
}

const { confirm } = Modal;

const TabUDF: FC<TabUDFProps> = ({ pane }) => {
  const [mode, setMode] = useState<'view' | 'edit'>(pane.mode);
  const [data, setData] = useState<UDF>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const { getTreeWrapped, onRemovePane, replaceTab, curNode } = useModel('datadev');

  useEffect(() => {
    if (pane.id !== -1) {
      getUDFWrapped();
    } else if (mode === 'edit') {
      const folderId = curNode?.id;
      form.setFieldsValue({ folderId });
    }
  }, [pane.id]);

  const getUDFWrapped = () => getUDF({ id: pane.id }).then((res) => setData(res.data));

  const onSubmit = () => {
    setLoading(true);
    form
      .validateFields()
      .then(() => {
        const values = form.getFieldsValue();
        delete values.upload;
        if (pane.id === -1) {
          createUDF(values)
            .then((res) => {
              if (res.success) {
                message.success('创建UDF成功');
                getTreeWrapped();
                replaceTab({
                  oldKey: 'newFun',
                  newKey: `${pane.type}_${pane.belong}_${res.data.id}`,
                  title: res.data.udfName,
                  pane: { ...pane, id: res.data.id as number },
                });
              }
            })
            .catch((err) => {})
            .finally(() => setLoading(false));
        } else {
          Object.assign(values, { id: data?.id });
          updateUDF(values)
            .then((res) => {
              if (res.success) {
                message.success('编辑UDF成功');
                replaceTab({
                  oldKey: pane.cid,
                  newKey: pane.cid,
                  title: res.data.udfName,
                  pane,
                });
                getUDFWrapped().then(() => setMode('view'));
                getTreeWrapped();
              }
            })
            .catch((err) => {})
            .finally(() => setLoading(false));
        }
      })
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '删除UDF',
      content: '您确认要删除该UDF吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteUDF({ id: pane.id })
          .then((res) => {
            if (res.success) {
              message.success('删除UDF成功');
              onRemovePane(pane.cid);
              getTreeWrapped();
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (pane.id === -1) {
      onRemovePane('newFun');
    } else {
      getUDFWrapped().then(() => setMode('view'));
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewUDF data={data} />}
      {mode === 'edit' && <EditUDF data={data} form={form} />}
      <div className="workbench-submit">
        {mode === 'view' && (
          <Space>
            <Button key="edit" size="large" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="del" size="large" onClick={onDelete}>
              删除
            </Button>
          </Space>
        )}
        {mode === 'edit' && (
          <Space>
            <Button key="save" size="large" type="primary" onClick={onSubmit} loading={loading}>
              保存
            </Button>
            <Button key="cancel" size="large" onClick={onCancel}>
              取消
            </Button>
          </Space>
        )}
      </div>
    </Fragment>
  );
};

export default TabUDF;
