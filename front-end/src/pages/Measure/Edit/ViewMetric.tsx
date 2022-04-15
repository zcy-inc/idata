import React, { useEffect, useState } from 'react';
import { Descriptions, Button, message, Spin, Table } from 'antd';
import type { FC } from 'react';
import { useParams, history } from 'umi';
import MonacoEditor from 'react-monaco-editor';
import { PageContainer } from '@ant-design/pro-layout';
import { getMetric, switchMetric, generateSQL } from '@/services/measure';
import type  { MetricListItem, MetricDetail } from '@/types/measure';
import Title from '@/components/Title';
import { LabelTag, timeDimOptions, AggregatorCodeOptions, degradeDimOptions } from '@/constants/datapi';
import style from './view.less';
import showDialog from '@/utils/showDialog';
import DimensionSelect from './components/DimensionSelect';
export interface ViewModifierProps {
  data: MetricListItem;
}

const { Item } = Descriptions;
const TagMap = (labelTag: LabelTag) => {
  switch (labelTag) {
    case LabelTag.ATOMIC_METRIC_LABEL:
    case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
      return '原子指标';
    case LabelTag.DERIVE_METRIC_LABEL:
    case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
      return '派生指标';
    case LabelTag.COMPLEX_METRIC_LABEL:
    case LabelTag.COMPLEX_METRIC_LABEL_DISABLE:
    default:
      return '';
  }
};

const ViewModifier: FC<ViewModifierProps> = ({ }) => {
  const params = useParams<{ id: string; }>();
  const [data, setData] = useState<MetricDetail>();
  const [getLoading, setGetLoading] = useState<boolean>(false);
  const [sql , setSql] = useState('');
  useEffect(() => {
    getMetricInfo();
  }, [])

  const getMetricInfo = () => {
    setGetLoading(true);
    getMetric({ metricCode: params.id })
      .then((res) => {
        const transformedData = transformOriginData(res.data);
        setSql(res.data.metricSql);
        setData(transformedData);
      })
      .finally(() => {
        setGetLoading(false);
      });
  };

  const transformOriginData = (data: { atomicMetric: [], creator: string; metricId: string; labelName: string; labelTag: string; labelAttributes: any; measureLabels: any; specialAttribute: any; }) => {
    const { metricId, labelName, atomicMetric, creator, labelTag,labelAttributes, measureLabels,  specialAttribute} = data;
    const labelParams: {
      metricId?: string;
      enName?: string;
      bizProcessCode?: string;
      metricDefine?: string;
      domainCodeName?: string;
      bizProcessCodeName?: string;
      comment?: string;
      metricDeadline?: string;
    } = {};
    labelAttributes.forEach((attribute: {enumValue: string; attributeKey: string; attributeValue: string | number; }) => {
      labelParams[attribute.attributeKey] = attribute.attributeValue;
      if(attribute.enumValue) {
        labelParams[`${attribute.attributeKey}Name`] = attribute.enumValue;
      }
    });
    return {
      metricId,
      labelName,
      labelTag,
      ...labelParams,
      measureLabels,
      creator,
      atomicMetric: [atomicMetric],
      modifiers: specialAttribute.modifiers,
      dimTables: specialAttribute.dimTables,
      aggregatorCode: specialAttribute.aggregatorCode,
      timeAttribute:
      (!specialAttribute.timeAttribute?.columnName && !specialAttribute.timeAttribute?.timeDim) ?
      [] : [specialAttribute.timeAttribute],
      calculableType: specialAttribute.calculableType
    }
  }

  const copy = () => {
    const  copy = function (e: any) {
      e.preventDefault();
      e.clipboardData.setData('text/plain', sql);
    }
    window.addEventListener('copy', copy);
    document.execCommand('copy');
    window.removeEventListener('copy', copy);
    message.success('内容已复制！');
  }

  const showDimensionSelect = () => {
    showDialog('维度选择', {
      modalProps: {
      },
      formProps: {
        dimTables: (data?.dimTables || []).map(item => ({label: item.tableName, value: item.tableId}))
      },
      btns: {
        positive: '保存并生成',
        negetive: '取消',
        other: []
      },
      beforeConfirm: (dialog, form, done) => {
        dialog.showLoading();
        form.handleSubmit().then((values: { dimTables: any; }) => {
          generateSQL({metricCode: params.id}, values.dimTables).then(res => {
            setSql(res.data);
            done();
          }).catch(() => {
            dialog.hideLoading();
          })
        }).finally(() => {
          dialog.hideLoading();
        })
      }
    }, DimensionSelect)
  }

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

  const switchStatus = () => {
    switchMetric({ metricCode: params.id, labelTag: transformLabelTag(data?.labelTag as LabelTag) })
      .then((res) => {
        if (res.success) {
          message.success('操作成功');
          getMetricInfo();
        }
      })
      .catch((err) => {});
  };

  return (
    <PageContainer
      header={{
        breadcrumb: {
          routes: [
            {
              path: '/measure/list',
              breadcrumbName: '数据指标',
            },
            {
              path: '',
              breadcrumbName: '查看指标信息',
            },
          ],
        },
      }}
    >
      <Spin spinning={getLoading} wrapperClassName={style['view-wrap']}>
        <Title>基本信息</Title>
        <Descriptions column={3} colon={false} style={{ margin: '16px 0' }}>
          <Item label="指标ID">{data?.metricId}</Item>
          <Item label="指标名称">{data?.labelName}</Item>
          <Item label="英文名称">{data?.enName}</Item>
          <Item label="数据域">{data?.domainCodeName}</Item>
          <Item label="业务过程">{data?.bizProcessCodeName}</Item>
          <Item label="指标类型">{TagMap(data?.labelTag as LabelTag)}</Item>
          <Item label="指标状态" className={data?.labelTag.endsWith('DISABLE') ? style['status-disable'] : style['status-enable']}>
            {data?.labelTag.endsWith('DISABLE') ? '已停用' : '使用中'}
          </Item>
          <Item label="创建人">{data?.creator}</Item>
          <Item label="截止生效日期">{data?.metricDeadline}</Item>
        </Descriptions>
        <Title>指标定义</Title>
        <p>{data?.metricDefine}</p>
        <Title style={{marginTop: 32}}>技术口径</Title>
        <p className={style['part-title']}>指标来源</p>
        <Table
          bordered
          size="small"
          pagination={false}
          dataSource={data?.measureLabels || []}
          columns={[
            {
              title: '表名称',
              dataIndex: 'tableName',
              key: 'tableName'
            }, {
              title: '字段名称',
              dataIndex: 'columnComment',
              key: 'columnComment'
            }, {
              title: '字段英文名称',
              dataIndex: 'columnName',
              key: 'columnName'
            },
          ]}
        />
        {data?.labelTag === LabelTag.DERIVE_METRIC_LABEL ?
          <>
            <p className={style['part-title']}>原子指标</p>
            <Table
              bordered
              size="small"
              pagination={false}
              dataSource={data?.atomicMetric || []}
              columns={[
                {
                  title: '指标ID',
                  dataIndex: 'metricId',
                  key: 'metricId'
                }, {
                  title: '指标名称',
                  dataIndex: 'labelName',
                  key: 'labelName'
                }, {
                  title: '字段英文名称',
                  dataIndex: 'columnName',
                  key: 'columnName'
                },
              ]}
            />
            <p className={style['part-title']}>时间周期</p>
            <Table
              bordered
              size="small"
              pagination={false}
              dataSource={data?.timeAttribute || []}
              columns={[
                {
                  title: '字段',
                  dataIndex: 'columnName',
                  key: 'columnName'
                }, {
                  title: '时间周期',
                  dataIndex: 'timeDim',
                  key: 'timeDim',
                  render: (t) => timeDimOptions.find(item => item.value === t)?.label
                }
              ]}
            />
            <p className={style['part-title']}>维度</p>
            <Table
              bordered
              pagination={false}
              dataSource={data?.dimTables || []}
              size="small"
              columns={[
                {
                  title: '维度表',
                  dataIndex: 'tableName',
                  key: 'tableName'
                }
              ]}
            />
            <p className={style['part-title']}>修饰词</p>
            <Table
              bordered
              pagination={false}
              dataSource={data?.modifiers || []}
              size="small"
              columns={[
                {
                  title: '修饰词',
                  dataIndex: 'modifierName',
                  key: 'modifierName'
                }, {
                  title: '字段英文名称',
                  dataIndex: 'columnName',
                  key: 'columnName'
                }, {
                  title: '内容',
                  dataIndex: 'modifierValue',
                  key: 'modifierValue'
                }
              ]}
            />
          </> :
        null}
       
       
        <p className={style['part-title']}>其他信息</p>
        <Descriptions column={2} colon={false} style={{ margin: '16px 0' }}>
          <Item label="计算方式">
            {AggregatorCodeOptions.find(item => item.value === data?.aggregatorCode)?.label || '-'}
          </Item>
          <Item label="是否可累加">
            {degradeDimOptions.find(item => item.value === data?.calculableType)?.label || '-'}
          </Item>
        </Descriptions>
        <p className={style['part-title']} style={{lineHeight: '32px'}}>
          SQL界面
          <span className={style['btn-list']}>
          {data?.labelTag === LabelTag.DERIVE_METRIC_LABEL ?
            <a className={style['plain-default-btn']} onClick={showDimensionSelect}>维度选择</a> : null  
          }
            
            <Button type="primary" onClick={copy}>复制内容</Button>
          </span>
        </p>
        <MonacoEditor
          height="200"
          language="sql"
          theme="vs-dark"
          value={sql}
          options={{ readOnly: true }}
        />
        <div className={style['btn-list']}>
          <a
            className={style['plain-default-btn']}
            onClick={() => history.push('/measure/list')}
          >返 回</a>
          <a
            className={style['plain-default-btn']}
            onClick={() => history.push(`/measure/edit/${params.id}`)}
          >编 辑</a>
          <Button onClick={switchStatus} danger ghost={!data?.labelTag.endsWith('DISABLE')}>
            {data?.labelTag.endsWith('DISABLE') ? '启用' : '停用'}
          </Button>
        </div>
      </Spin>
    </PageContainer>
  );
};

export default ViewModifier;
