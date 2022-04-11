import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, message, Modal, Space, Spin } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import type { FormInstance } from 'antd';

import ViewDIM from './ViewDimension';
import EditDIM from './EditDimension';
import {
  createDimension,
  deleteDimension,
  getDimension,
  switchDimension,
} from '@/services/measure';
import { Dimension } from '@/types/datapi';
import { LabelTag, TreeNodeType } from '@/constants/datapi';

export interface TabDimensionProps {
  initialMode: 'view' | 'edit';
  tabKey: string;
  fileCode: string;
}
interface DimensionExportProps {
  form: FormInstance;
  DIM: [];
  DWD: [];
}
const { confirm } = Modal;

const TabDimension: FC<TabDimensionProps> = ({ initialMode = 'view', tabKey, fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [getLoading, setGetLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Dimension>();
  const refD = useRef<DimensionExportProps>();
  const { getTree, removeTab, replaceTab } = useModel('measure', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
    replaceTab: _.replaceTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newDimension' && getDimensionInfo(fileCode);
  }, []);

  const getDimensionInfo = (dimensionCode: string) => {
    setGetLoading(true);
    getDimension({ dimensionCode })
      .then((res) => {
        setData(res.data);
      })
      .finally(() => {
        setGetLoading(false);
      });
  };

  const onSubmit = () => {
    const form = refD.current!.form.getFieldsValue();
    const DIM = refD.current!.DIM?.map((_: { tableId: number; columnName: string }) => ({
      tableId: _.tableId,
      columnName: _.columnName,
      labelParamValue: 'true',
    }));
    const DWD = refD.current!.DWD?.map((_: { tableId: number; columnName: string }) => ({
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
      folderId: form.folderId || 0,
      subjectType: 'COLUMN',
      labelTag: 'DIMENSION_LABEL',
      labelAttributes,
      specialAttribute: { degradeDim: refD.current!.DIM[0]?.degradeDim },
      measureLabels: DIM.concat(DWD),
    };

    if (data) {
      Object.assign(params, { labelCode: data.labelCode });
    }
    setLoading(true);
    createDimension(params)
      .then((res) => {
        if (res.success) {
          if (fileCode === 'newDimension') {
            message.success('新建维度成功');
            replaceTab(
              tabKey,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.DIMENSION_LABEL,
            );
          } else {
            message.success('更新维度成功');
            replaceTab(
              `L_${res.data.labelCode}`,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.DIMENSION_LABEL,
            );
            getTree(TreeNodeType.DIMENSION_LABEL);
            getDimensionInfo(res.data.labelCode);
            setMode('view');
          }
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '删除维度',
      content: '您确认要删除该维度吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteDimension({ dimensionCode: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(`L_${data!.labelCode}`);
              getTree(TreeNodeType.DIMENSION_LABEL);
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newDimension') {
      removeTab(tabKey);
    } else {
      setMode('view');
      getDimensionInfo(fileCode);
    }
  };

  const switchStatus = () => {
    switchDimension({
      dimensionCode: fileCode,
      labelTag:
        data?.labelTag === LabelTag.DIMENSION_LABEL
          ? LabelTag.DIMENSION_LABEL_DISABLE
          : LabelTag.DIMENSION_LABEL,
    })
      .then((res) => {
        if (res.success) {
          message.success('操作成功');
          getDimensionInfo(fileCode);
        }
      })
      .catch((err) => {});
  };

  return (
    <Fragment>
      <Spin spinning={getLoading}>
        {mode === 'view' && <ViewDIM data={data as Dimension} />}
        {mode === 'edit' && <EditDIM ref={refD} initial={data} />}
      </Spin>
      <div className="workbench-submit">
        {mode === 'view' && (
          <Space>
            <Button key="edit" size="large" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="labelTag" size="large" onClick={switchStatus}>
              {data?.labelTag === LabelTag.DIMENSION_LABEL ? '停用' : '启用'}
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

export default TabDimension;
