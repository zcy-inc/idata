import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, message, Popconfirm, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import styles from '../../index.less';

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
  fileCode: string;
}
interface DimensionExportProps {
  form: FormInstance;
  DIM: [];
  DWD: [];
}

const TabDimension: FC<TabDimensionProps> = ({ initialMode = 'view', fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
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
    getDimension({ dimensionCode })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    const form = refD.current!.form.getFieldsValue();
    const DIM = refD.current!.DIM.map((_: { tableId: number; columnName: string }) => ({
      tableId: _.tableId,
      columnName: _.columnName,
      labelParamValue: 'true',
    }));
    const DWD = refD.current!.DWD.map((_: { tableId: number; columnName: string }) => ({
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
            replaceTab('newDimension', `L_${res.data.labelCode}`, res.data.labelName);
          } else {
            message.success('更新维度成功');
            replaceTab(`L_${res.data.labelCode}`, `L_${res.data.labelCode}`, res.data.labelName);
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
    deleteDimension({ dimensionCode: fileCode })
      .then((res) => {
        if (res.success) {
          message.success('删除成功');
          removeTab(`L_${data!.labelCode}`);
          getTree(TreeNodeType.DIMENSION_LABEL);
        }
      })
      .catch((err) => {});

  const onCancel = () => {
    if (fileCode === 'newDimension') {
      removeTab('newDimension');
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
          setData(res.data);
        }
      })
      .catch((err) => {});
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewDIM data={data as Dimension} />}
      {mode === 'edit' && <EditDIM ref={refD} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && (
          <Space>
            <Button key="edit" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="labelTag" onClick={switchStatus}>
              {data?.labelTag === LabelTag.DIMENSION_LABEL ? '停用' : '启用'}
            </Button>
            <Popconfirm
              key="del"
              title="您确认要删除该维度吗？"
              onConfirm={onDelete}
              okButtonProps={{ loading }}
              okText="确认"
              cancelText="取消"
            >
              <Button>删除</Button>
            </Popconfirm>
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

export default TabDimension;
