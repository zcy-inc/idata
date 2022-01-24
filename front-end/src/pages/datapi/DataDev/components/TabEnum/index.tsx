import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';

import { createEnum, deleteEnum, getEnum } from '@/services/datadev';
import { Enum } from '@/types/datapi';

import ViewEnum from './ViewEnum';
import EditEnum from './EditEnum';
import { IPane } from '@/models/datadev';

export interface TabEnumProps {
  pane: IPane;
}

const { confirm } = Modal;

const TabEnum: FC<TabEnumProps> = ({ pane }) => {
  const [mode, setMode] = useState<'view' | 'edit'>(pane.mode);
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const [data, setData] = useState<Enum>();

  const { getTreeWrapped, onRemovePane, replaceTab } = useModel('datadev', (_) => ({
    getTreeWrapped: _.getTreeWrapped,
    onRemovePane: _.onRemovePane,
    replaceTab: _.replaceTab,
  }));

  useEffect(() => {
    pane.id !== -1 && getEnumInfo(pane.id);
  }, []);

  const getEnumInfo = (id: number) => getEnum({ enumId: id }).then((res) => setData(res.data));

  const onSubmit = () => {
    form.validateFields().then(() => {
      setLoading(true);
      const values = form.getFieldsValue();
      const params = {
        enumName: values.enumName,
        folderId: values.folderId || 0,
        enumValues: values.enumValues.enums?.map((_: any) => ({
          enumValue: _.enumValue.value,
          valueCode: _.enumValue.code,
          parentCode: _.parentCode,
          enumAttributes: values.enumValues.columns?.map((col: any) => ({
            attributeKey: col.title,
            attributeType: col.type,
            attributeValue: _[col.title],
          })),
        })),
      };
      pane.id !== -1 && Object.assign(params, { enumCode: data?.enumCode });
      createEnum(params)
        .then((res) => {
          if (res.success) {
            if (pane.id === -1) {
              message.success('新建枚举成功');
              replaceTab({
                oldKey: 'newEnum',
                newKey: `${pane.type}_${pane.belong}_${res.data.id}`,
                title: res.data.enumName,
                pane: { ...pane, id: res.data.id },
              });
            } else {
              message.success('更新枚举成功');
              replaceTab({ oldKey: pane.cid, newKey: pane.cid, title: res.data.enumName, pane });
              getTreeWrapped();
              getEnumInfo(res.data.id).then(() => setMode('view'));
            }
          }
        })
        .catch((err) => {})
        .finally(() => setLoading(false));
    });
  };

  const onDelete = () =>
    confirm({
      title: '删除枚举',
      content: '您确认要删除该枚举吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteEnum({ enumCode: data?.enumCode as string }).then((res) => {
          res.success && message.success('删除成功');
          onRemovePane(pane.cid);
          getTreeWrapped();
        }),
    });

  return (
    <Fragment>
      {mode === 'view' && <ViewEnum data={data} />}
      {mode === 'edit' && <EditEnum form={form} data={pane.id === -1 ? undefined : data} />}
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
            <Button
              key="cancel"
              size="large"
              onClick={() => {
                if (pane.id === -1) {
                  onRemovePane('newEnum');
                } else {
                  setMode('view');
                  getEnumInfo(pane.id);
                }
              }}
            >
              取消
            </Button>
          </Space>
        )}
      </div>
    </Fragment>
  );
};

export default TabEnum;
