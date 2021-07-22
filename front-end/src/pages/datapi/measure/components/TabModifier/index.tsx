import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import styles from '../../index.less';

import ViewModifier from './ViewModifier';
import EditModifier from './EditModifier';
import { Modifier } from '@/types/datapi';
import { createModifier, deleteModifier, getModifier, switchModifier } from '@/services/measure';
import { LabelTag, TreeNodeType } from '@/constants/datapi';

export interface TabModifierProps {
  initialMode: 'view' | 'edit';
  tabKey: string;
  fileCode: string;
}
interface ModifierExportProps {
  form: FormInstance;
  DWD: [];
}
const { confirm } = Modal;

const TabModifier: FC<TabModifierProps> = ({ initialMode = 'view', tabKey, fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Modifier>();
  const refM = useRef<ModifierExportProps>();
  const { getTree, removeTab, replaceTab } = useModel('measure', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
    replaceTab: _.replaceTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newModifier' && getModifierInfo(fileCode);
  }, []);

  const getModifierInfo = (modifierCode: string) => {
    getModifier({ modifierCode })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    const form = refM.current!.form.getFieldsValue();
    const DWD = refM.current!.DWD.map((_: { tableId: number; columnName: string }) => ({
      tableId: _.tableId,
      columnName: _.columnName,
      labelParamValue: 'false',
    }));
    const labelAttributes = [];
    for (let [key, value] of Object.entries(form)) {
      if (key !== 'labelName' && key !== 'folderId') {
        labelAttributes.push({
          attributeKey: key,
          attributeType: 'STRING',
          attributeValue: value || '',
        });
      }
    }
    const params = {
      labelName: form.labelName,
      folderId: form.folderId,
      subjectType: 'COLUMN',
      labelTag: 'MODIFIER_LABEL',
      labelAttributes,
      measureLabels: DWD,
    };
    if (data) {
      Object.assign(params, { labelCode: data.labelCode });
    }
    createModifier(params)
      .then((res) => {
        if (res.success) {
          if (fileCode === 'newModifier') {
            message.success('新建修饰词成功');
            replaceTab(
              tabKey,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.MODIFIER_LABEL,
            );
          } else {
            message.success('更新修饰词成功');
            replaceTab(
              `L_${res.data.labelCode}`,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.MODIFIER_LABEL,
            );
            getTree(TreeNodeType.MODIFIER_LABEL);
            getModifierInfo(res.data.labelCode);
            setMode('view');
          }
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '删除修饰词',
      content: '您确认要删除该修饰词吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteModifier({ modifierCode: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(`L_${data!.labelCode}`);
              getTree(TreeNodeType.MODIFIER_LABEL);
            }
          })
          .catch((err) => {}),
    });
  const onCancel = () => {
    if (fileCode === 'newModifier') {
      removeTab(tabKey);
    } else {
      setMode('view');
      getModifierInfo(fileCode);
    }
  };

  const switchStatus = () => {
    switchModifier({
      modifierCode: fileCode,
      labelTag:
        data?.labelTag === LabelTag.MODIFIER_LABEL
          ? LabelTag.MODIFIER_LABEL_DISABLE
          : LabelTag.MODIFIER_LABEL,
    })
      .then((res) => {
        if (res.success) {
          message.success('操作成功');
          getModifierInfo(fileCode);
        }
      })
      .catch((err) => {});
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewModifier data={data as Modifier} />}
      {mode === 'edit' && <EditModifier ref={refM} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && (
          <Space>
            <Button key="edit" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="labelTag" onClick={switchStatus}>
              {data?.labelTag === LabelTag.MODIFIER_LABEL ? '停用' : '启用'}
            </Button>
            <Button key="del" onClick={onDelete}>
              删除
            </Button>
          </Space>
        )}
        {mode === 'edit' && (
          <Space>
            <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
              保存
            </Button>
            <Button key="cancel" onClick={onCancel}>
              取消
            </Button>
          </Space>
        )}
      </div>
    </Fragment>
  );
};

export default TabModifier;
