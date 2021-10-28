import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, message, Modal, Space, Spin } from 'antd';
import { useModel } from 'umi';
import MonacoEditor from 'react-monaco-editor';
import type { FC } from 'react';
import type { FormInstance } from 'antd';

import ViewMetric from './ViewMetric';
import EditMetric from './EditMetric';
import { Metric } from '@/types/datapi';
import {
  createMetric,
  deleteMetric,
  generateSQL,
  getMetric,
  switchMetric,
} from '@/services/measure';
import { LabelTag, TreeNodeType } from '@/constants/datapi';

export interface TabMetricProps {
  initialMode: 'view' | 'edit';
  tabKey: string;
  fileCode: string;
}
interface MetricExportProps {
  form: FormInstance;
  data: () => any;
}
const { confirm } = Modal;

const TabMetric: FC<TabMetricProps> = ({ initialMode = 'view', tabKey, fileCode }) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [getLoading, setGetLoading] = useState<boolean>(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Metric>();
  const [visible, setVisible] = useState(false);
  const [SQL, setSQL] = useState('');
  const [SQLLoading, setSQLLoading] = useState(false);
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
    setGetLoading(true);
    getMetric({ metricCode })
      .then((res) => {
        setData(res.data);
      })
      .finally(() => {
        setGetLoading(false);
      });
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
      folderId: form.folderId || 0,
      subjectType: 'COLUMN',
      labelTag: 'DIMENSION_LABEL',
      labelAttributes,
    };
    switch (typeData.type) {
      case 'ATOMIC':
        if (typeData.data.length === 0) {
          message.error('事实表为必填项');
          setLoading(false);
          return;
        }
        const measureLabels = typeData?.data?.map((_: { tableId: number; columnName: string }) => ({
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
        const modifiers = typeData?.data?.modifiers?.map(
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
            replaceTab(
              tabKey,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.METRIC_LABEL,
            );
          } else {
            message.success('更新指标成功');
            replaceTab(
              `L_${res.data.labelCode}`,
              `L_${res.data.labelCode}`,
              res.data.labelName,
              TreeNodeType.METRIC_LABEL,
            );
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
    confirm({
      title: '删除指标',
      content: '您确认要删除该指标吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteMetric({ metricCode: fileCode })
          .then((res) => {
            if (res.success) {
              message.success('删除成功');
              removeTab(`L_${data!.labelCode}`);
              getTree(TreeNodeType.METRIC_LABEL);
            }
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newMetric') {
      removeTab(tabKey);
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
          getMetricInfo(fileCode);
        }
      })
      .catch((err) => {});
  };

  const generateSQLWrapped = () => {
    setSQLLoading(true);
    generateSQL({ metricCode: data?.labelCode as string })
      .then((res) => {
        if (res.success) {
          message.success('生成SQL成功');
          setSQL(res.data);
          setVisible(true);
        } else {
          message.error(`生成SQL失败：${res.msg}`);
        }
      })
      .catch((err) => {})
      .finally(() => setSQLLoading(false));
  };

  return (
    <Fragment>
      <Spin spinning={getLoading}>
        {mode === 'view' ? <ViewMetric data={data as Metric} /> : null}
        {mode === 'edit' ? <EditMetric ref={refM} initial={data} /> : null}
      </Spin>
      <div className="workbench-submit">
        {mode === 'view' && (
          <Space>
            <Button key="edit" size="large" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="sql" size="large" onClick={generateSQLWrapped} loading={SQLLoading}>
              生成SQL
            </Button>
            <Button key="labelTag" size="large" onClick={switchStatus}>
              {transformLabelTagText(data?.labelTag as LabelTag)}
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
      <Modal
        title="生成SQL"
        width={800}
        bodyStyle={{ padding: 16 }}
        visible={visible}
        onCancel={() => setVisible(false)}
        footer={null}
      >
        <MonacoEditor
          height="400"
          language="sql"
          theme="vs-dark"
          value={SQL}
          options={{ readOnly: true }}
        />
      </Modal>
    </Fragment>
  );
};

export default TabMetric;
