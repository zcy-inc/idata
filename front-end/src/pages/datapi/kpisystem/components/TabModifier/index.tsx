import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Modal, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewModifier from './ViewModifier';
import EditModifier from './EditModifier';
import { Modifier } from '@/types/datapi';
import { createModifier, deleteModifier, getModifier } from '@/services/kpisystem';
import { TreeNodeType } from '@/constants/datapi';

export interface TabEnumProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
interface ModifierExportProps {
  form: any;
  DWD: [];
}

const { confirm } = Modal;

const TabEnum: FC<TabEnumProps> = ({ initialMode = 'view', fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Modifier>();
  const refM = useRef<ModifierExportProps>();
  const { getTree, removeTab } = useModel('kpisystem', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newModifier' && getModifierInfo(fileCode);
  }, []);

  const getModifierInfo = (modifierId: string) => {
    getModifier({ modifierId })
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
    createModifier({})
      .then((res) => {
        if (res.success) {
          message.success(fileCode === 'newDimensiob' ? '新建维度成功' : '更新维度成功');
          getTree(TreeNodeType.DIMENSION_LABEL);
          getModifierInfo(''); // TODO
          setMode('view');
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '您确定要删除该修饰词吗？',
      onOk: () =>
        deleteModifier({ modifierId: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(''); // TODO
              getTree(TreeNodeType.MODIFIER_LABEL);
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newModifier') {
      removeTab('newModifier');
    } else {
      setMode('view');
      getModifierInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewModifier data={data as Modifier} />}
      {mode === 'edit' && <EditModifier ref={refM} initial={data} />}
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

export default TabEnum;
