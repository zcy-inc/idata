import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, message, Popconfirm, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import type { FormInstance } from 'antd';
import styles from '../../index.less';

import ViewMetric from './ViewMetric';
import EditMetric from './EditMetric';
import { Metric } from '@/types/datapi';
import { createMetric, deleteMetric, getMetric, switchMetric } from '@/services/measure';
import { LabelTag, TreeNodeType } from '@/constants/datapi';

export interface TabMetricProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
interface MetricExportProps {
  form: FormInstance;
  data: () => any;
}

const TabMetric: FC<TabMetricProps> = ({ initialMode = 'view', fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Metric>();
  const refM = useRef<MetricExportProps>();
  const { getTree, removeTab, replaceTab } = useModel('measure', (_) => ({
    getTree: _.getTree,
    removeTab: _.removeTab,
    replaceTab: _.replaceTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newMetric' && getMetricInfo(fileCode);
  }, []);

  const getMetricInfo = (metricCode: string) => {
    getMetric({ metricCode })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    const form = refM.current?.form.getFieldsValue();
    const typeData = refM.current?.data();
    const labelAttributes = [];
    for (let [key, value] of Object.entries(form)) {
      if (key !== 'labelName' && key !== 'folderId' && key !== 'labelTag') {
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
    };
    switch (typeData.type) {
      case 'ATOMIC':
        const measureLabels = typeData.data.map((_: { tableId: number; columnName: string }) => ({
          tableId: _.tableId,
          columnName: _.columnName,
          labelParamValue: 'false',
        }));
        Object.assign(params, {
          measureLabels,
          labelTag: LabelTag.ATOMIC_METRIC_LABEL,
          specialAttribute: { aggregatorCode: typeData.data[0].aggregatorCode },
        });
        break;
      case 'DERIVE':
        const atomicMetricCode = typeData.data.atomicMetricCode;
        const modifiers = typeData.data.modifiers.map(
          (modifier: { modifierCode: string; enumValueCodes: string[] }) => ({
            modifierCode: modifier.modifierCode,
            enumValueCodes: modifier.enumValueCodes,
          }),
        );
        Object.assign(params, {
          labelTag: LabelTag.DERIVE_METRIC_LABEL,
          specialAttribute: { atomicMetricCode, modifiers },
        });
        break;
      case 'COMPLEX':
        Object.assign(params, {
          labelTag: LabelTag.COMPLEX_METRIC_LABEL,
          specialAttribute: { complexMetricFormula: typeData.data },
        });
      default:
        break;
    }
    if (data) {
      Object.assign(params, { labelCode: data.labelCode });
    }
    createMetric(params)
      .then((res) => {
        if (res.success) {
          if (fileCode === 'newMetric') {
            message.success('新建指标成功');
            replaceTab('newMetric', `L_${res.data.labelCode}`, res.data.labelName);
          } else {
            message.success('更新指标成功');
            replaceTab(`L_${res.data.labelCode}`, `L_${res.data.labelCode}`, res.data.labelName);
            getTree(TreeNodeType.METRIC_LABEL);
            getMetricInfo(res.data.labelCode);
            setMode('view');
          }
        }
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    deleteMetric({ metricCode: fileCode })
      .then((res) => {
        if (res.success) {
          message.success('删除成功');
          removeTab(`L_${data!.labelCode}`);
          getTree(TreeNodeType.METRIC_LABEL);
        }
      })
      .catch((err) => {});

  const onCancel = () => {
    if (fileCode === 'newMetric') {
      removeTab('newMetric');
    } else {
      setMode('view');
      getMetricInfo(fileCode);
    }
  };

  const transformLabelTag = (labelTag: LabelTag) => {
    switch (labelTag) {
      case LabelTag.ATOMIC_METRIC_LABEL:
        return LabelTag.ATOMIC_METRIC_LABEL_DISABLE;
      case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
        return LabelTag.ATOMIC_METRIC_LABEL;
      case LabelTag.DERIVE_METRIC_LABEL:
        return LabelTag.DERIVE_METRIC_LABEL_DISABLE;
      case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
        return LabelTag.DERIVE_METRIC_LABEL;
      case LabelTag.COMPLEX_METRIC_LABEL:
        return LabelTag.COMPLEX_METRIC_LABEL_DISABLE;
      case LabelTag.COMPLEX_METRIC_LABEL_DISABLE:
      default:
        return LabelTag.COMPLEX_METRIC_LABEL;
    }
  };

  const transformLabelTagText = (labelTag: LabelTag) => {
    switch (labelTag) {
      case LabelTag.ATOMIC_METRIC_LABEL:
      case LabelTag.DERIVE_METRIC_LABEL:
      case LabelTag.COMPLEX_METRIC_LABEL:
        return '停用';
      case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
      case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
      case LabelTag.COMPLEX_METRIC_LABEL_DISABLE:
      default:
        return '启用';
    }
  };

  const switchStatus = () => {
    switchMetric({ metricCode: fileCode, labelTag: transformLabelTag(data!.labelTag) })
      .then((res) => {
        if (res.success) {
          message.success('操作成功');
        }
      })
      .catch((err) => {});
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewMetric data={data as Metric} />}
      {mode === 'edit' && <EditMetric ref={refM} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && (
          <Space>
            <Button key="edit" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="labelTag" onClick={switchStatus}>
              {transformLabelTagText(data?.labelTag as LabelTag)}
            </Button>
            <Popconfirm
              key="del"
              title="您确认要删除该指标吗？"
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

export default TabMetric;
