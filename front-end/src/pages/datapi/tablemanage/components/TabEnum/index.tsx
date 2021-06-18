import React, { Fragment, useEffect, useState } from 'react';
import { Button, Form, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import { createEnum, delEnum, getEnum } from '@/services/tablemanage';
import { isEnumType } from '@/utils/tablemanage';
import { Enum } from '@/types/tablemanage';

import ViewEnum from './ViewEnum';
import EditEnum from './EditEnum';

export interface TabEnumProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}

const TabEnum: FC<TabEnumProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const [data, setData] = useState<Enum>();

  const { getTree, onRemovePane } = useModel('tabalmanage', (ret) => ({
    getTree: ret.getTree,
    onRemovePane: ret.onRemovePane,
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
        enumValues: values.enumValues.enums.map((_: any) => ({
          enumValue: _.enumValue.value,
          valueCode: _.enumValue.code,
          parentCode: _.parentCode,
          enumAttributes: values.enumValues.columns.map((col: any) => ({
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
            message.success(fileCode === 'newEnum' ? '新建枚举成功' : '更新枚举成功');
            getTree('ENUM');
            getEnumInfo(res.data.enumCode);
            setMode('view');
          }
        })
        .catch((err) => [])
        .finally(() => setLoading(false));
    });
  };

  const onDelete = () => {
    setLoading(true);
    delEnum({ enumCode: fileCode })
      .then((res) => {
        if (res.success) message.success('删除成功');
        onRemovePane(`E_${fileCode}`);
        getTree('ENUM');
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewEnum data={data} />}
      {mode === 'edit' && <EditEnum form={form} data={fileCode === 'newEnum' ? undefined : data} />}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Popconfirm
            key="del"
            title="您确认要删除该枚举吗？"
            onConfirm={() => onDelete()}
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
          <Button
            key="cancel"
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
          </Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabEnum;
