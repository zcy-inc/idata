import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import { createEnum, delEnum, getEnum } from '@/services/tablemanage';
import { Enum } from '@/types/datapi';

import ViewEnum from './ViewEnum';
import EditEnum from './EditEnum';

export interface TabEnumProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}

const { confirm } = Modal;

const TabEnum: FC<TabEnumProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const [data, setData] = useState<Enum>();

  const { getTree, onRemovePane, replaceTab } = useModel('tablemanage', (ret) => ({
    getTree: ret.getTree,
    onRemovePane: ret.onRemovePane,
    replaceTab: ret.replaceTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newEnum' && getEnumInfo(fileCode);
  }, []);

  const getEnumInfo = (enumCode: string) => {
    getEnum({ enumCode })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    form.validateFields().then(() => {
      setLoading(true);
      const values = form.getFieldsValue();
      const data = {
        enumName: values.enumName,
        folderId: values.folderId,
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
      fileCode !== 'newEnum' && Object.assign(data, { enumCode: fileCode });
      createEnum(data)
        .then((res) => {
          if (res.success) {
            if (fileCode === 'newEnum') {
              message.success('新建枚举成功');
              replaceTab('newEnum', `E_${res.data.enumCode}`, res.data.enumName, 'ENUM');
            } else {
              message.success('更新枚举成功');
              replaceTab(
                `E_${res.data.enumCode}`,
                `E_${res.data.enumCode}`,
                res.data.enumName,
                'ENUM',
              );
              getTree('ENUM');
              getEnumInfo(res.data.enumCode);
              setMode('view');
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
        delEnum({ enumCode: fileCode })
          .then((res) => {
            if (res.success) message.success('删除成功');
            onRemovePane(`E_${fileCode}`);
            getTree('ENUM');
          })
          .catch((err) => {}),
    });

  return (
    <Fragment>
      {mode === 'view' && <ViewEnum data={data} />}
      {mode === 'edit' && <EditEnum form={form} data={fileCode === 'newEnum' ? undefined : data} />}
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
                if (fileCode === 'newEnum') {
                  onRemovePane('newEnum');
                } else {
                  setMode('view');
                  getEnumInfo(fileCode);
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
