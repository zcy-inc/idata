import React, { Fragment, useEffect, useRef, useState } from 'react';
import {
  Button,
  Card,
  Collapse,
  Divider,
  Dropdown,
  Input,
  Menu,
  Select,
  Space,
  Typography,
} from 'antd';
import { CaretRightOutlined } from '@ant-design/icons';
import { cloneDeep } from 'lodash';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../../index.less';

import { IconFont } from '@/components';
import { ObjectLabel, RuleLayer } from '@/types/objectlabel';
import { getDimensionList, getMetricList } from '@/services/objectlabel';
import { Label } from '@/types/datapi';

export interface EditRulesProps {
  initial?: ObjectLabel;
  objectType: string;
}
type ActionKey = 'copy' | 'delete';
const initialLayer: RuleLayer = {
  layerId: Date.now(),
  layerName: '分层1',
  ruleDef: {
    rules: [
      {
        ruleId: Date.now(),
        ruleName: '规则1',
        indicatorDefs: [{ indicatorCode: null, condition: null, params: [] }],
        dimensionDefs: [{ dimensionCode: null, params: [] }],
      },
    ],
  },
};
const ConditionOptions = [
  { label: '等于', value: 'equal' },
  { label: '大于', value: 'greater' },
  { label: '小于', value: 'less' },
  { label: '大于等于', value: 'greaterOrEqual' },
  { label: '小于等于', value: 'lessOrEqual' },
  { label: '介于两个值之间', value: 'between' },
];
const { Panel } = Collapse;
const { Link, Text } = Typography;

const EditRules: FC<EditRulesProps> = ({ initial, objectType }) => {
  const [layers, setLayers] = useState<RuleLayer[]>([initialLayer]);
  const [activeKey, setActiveKey] = useState(0); // 取的是数组的index而非layerId
  const [expandKeys, setExpandKeys] = useState<string[][]>([['indicator', 'dimension']]); // 折叠面板的展开key
  const [indicatorCodeOptions, setIndicatorCodeOptions] = useState([]);
  const [dimensionCodeOptions, setDimensionCodeOptions] = useState([]);
  const [dimensionParamOptions, setDimensionParamOptions] = useState<[][]>([]);
  const layerCount = useRef(1);
  const { setEditLayers } = useModel('objectlabel', (_) => ({
    setEditLayers: _.setEditLayers,
  }));

  useEffect(() => {
    getMetric(objectType);
  }, [objectType]);

  useEffect(() => {
    if (initial) {
      const tmpExpandKeys = [];
      for (let i = 0; i < initial.ruleLayers.length; i++) {
        tmpExpandKeys.push(['indicator', 'dimension']);
      }
      setExpandKeys(tmpExpandKeys);
      setLayers(initial.ruleLayers);
      layerCount.current = initial.ruleLayers.length;
      // 获取指标信息的options
      getMetric(initial.objectType);
      // 获取维度信息的一级options
      const indicatorCode = initial.ruleLayers[0].ruleDef.rules[0].indicatorDefs[0].indicatorCode;
      getMetricList({
        labelTag: 'DIMENSION_LABEL',
        labelCodes: [objectType, indicatorCode as string],
      })
        .then((res) => {
          const tmp = res.data?.map((label: Label) => ({
            label: label.labelName,
            value: label.labelCode,
          }));
          setDimensionCodeOptions(tmp);
        })
        .catch((err) => {});
      // 获取维度信息的二级options
      const promises: Promise<any>[] = [];
      initial.ruleLayers[0].ruleDef.rules[0].dimensionDefs?.forEach((dimension, i) => {
        if (dimension.dimensionCode) {
          promises[i] = getDimensionList({ dimensionCode: dimension.dimensionCode as string });
        }
      });
      Promise.all(promises)
        .then((results) => {
          results?.forEach((res, i) => {
            dimensionParamOptions[i] = res.data?.map((v: string) => ({ label: v, value: v }));
          });
        })
        .catch((err) => {});
    }
  }, [initial]);

  useEffect(() => {
    setEditLayers(layers);
  }, [layers]);

  const getMetric = (objectType: string) => {
    getMetricList({ labelTag: 'METRIC_LABEL', labelCodes: [objectType] })
      .then((res) => {
        const tmp = res.data?.map((label: Label) => ({
          label: label.labelName,
          value: label.labelCode,
        }));
        setIndicatorCodeOptions(tmp);
      })
      .catch((err) => {});
  };

  const getCopyName = (name: string, i: number): string => {
    if (layers.findIndex((layer) => layer.layerName === `${name}${i}`) > -1) {
      return getCopyName(name, i + 1);
    } else {
      return `${name}${i}`;
    }
  };

  const onMenuAction = (key: ActionKey, i: number) => {
    if (key === 'delete') {
      layers.splice(i, 1);
      setLayers([...layers]);
      setActiveKey(i >= layers.length ? layers.length - 1 : i);
    }
    if (key === 'copy') {
      const copy = cloneDeep(layers[i]);
      copy.layerId = Date.now();
      if (layers.findIndex((layer) => layer.layerName === `${copy.layerName}_cpoy`) > -1) {
        copy.layerName = getCopyName(`${copy.layerName}_cpoy`, 2);
      } else {
        copy.layerName = `${copy.layerName}_cpoy`;
      }
      layers.push(copy);
      setLayers([...layers]);
      setActiveKey(layers.length - 1);
    }
  };

  const menu = (i: number) => (
    <Menu onClick={({ key }) => onMenuAction(key as ActionKey, i)}>
      <Menu.Item key="copy">复制</Menu.Item>
      <Menu.Item key="delete">删除</Menu.Item>
    </Menu>
  );

  const createLayer = () => {
    const layerId = Date.now();
    layerCount.current++;
    const newLayer: RuleLayer = {
      layerId,
      layerName: `分层${layerCount.current}`,
      ruleDef: {
        rules: [
          {
            ruleId: Date.now(),
            ruleName: '规则1',
            indicatorDefs: [{ indicatorCode: null, condition: null, params: [] }],
            dimensionDefs: [{ dimensionCode: null, params: [] }],
          },
        ],
      },
    };
    layers.push(newLayer);
    setLayers([...layers]);
    setActiveKey(layers.length - 1);
  };

  const changeLayerName = (v: string) => {
    layers[activeKey].layerName = v;
    setLayers([...layers]);
  };

  // const createIndicator = (iR: number) => {
  //   if (layers[activeKey].ruleDef.rules[iR].indicatorDefs.length > 0) {
  //     message.info('同一规则中只能存在一条指标');
  //     return;
  //   }
  //   layers[activeKey].ruleDef.rules[iR].indicatorDefs.push({
  //     indicatorCode: null,
  //     condition: null,
  //     params: [],
  //   });
  //   setLayers([...layers]);
  // };

  // const deleteIndicator = (iR: number, iI: number) => {
  //   layers[activeKey].ruleDef.rules[iR].indicatorDefs.splice(iI, 1);
  //   setLayers([...layers]);
  // };

  const createDimension = (iR: number) => {
    layers[activeKey].ruleDef.rules[iR].dimensionDefs.push({
      dimensionCode: null,
      params: [],
    });
    setLayers([...layers]);
  };

  const deleteDimension = (iR: number, iD: number) => {
    layers[activeKey].ruleDef.rules[iR].dimensionDefs.splice(iD, 1);
    dimensionParamOptions.splice(iD, 1);
    setDimensionParamOptions([...dimensionParamOptions]);
    setLayers([...layers]);
  };

  const setIndicator = (v: string, iR: number, iI: number, prop: string, iP?: number) => {
    if (prop === 'params') {
      layers[activeKey].ruleDef.rules[iR].indicatorDefs[iI].params[iP || 0] = v;
    } else {
      layers[activeKey].ruleDef.rules[iR].indicatorDefs[iI][prop] = v;
    }
    setLayers([...layers]);

    if (prop === 'indicatorCode') {
      getMetricList({ labelTag: 'DIMENSION_LABEL', labelCodes: [objectType, v] })
        .then((res) => {
          const tmp = res.data?.map((label: Label) => ({
            label: label.labelName,
            value: label.labelCode,
          }));
          setDimensionCodeOptions(tmp);
          layers[activeKey].ruleDef.rules[iR].dimensionDefs = [{ dimensionCode: null, params: [] }];
        })
        .catch((err) => {});
    }
  };

  const setDimension = (v: string, iR: number, iD: number, prop: string) => {
    if (prop === 'params') {
      layers[activeKey].ruleDef.rules[iR].dimensionDefs[iD].params[0] = v;
      setLayers([...layers]);
    } else {
      getDimensionList({ dimensionCode: v })
        .then((res) => {
          layers[activeKey].ruleDef.rules[iR].dimensionDefs[iD].dimensionCode = v;
          dimensionParamOptions[iD] = res.data?.map((v: string) => ({ label: v, value: v }));
          setLayers([...layers]);
          setDimensionParamOptions([...dimensionParamOptions]);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <Card className={styles['edit-content']}>
      <Space wrap>
        {layers.map((layer, i) => (
          <Dropdown.Button
            key={layer.layerId}
            className={layers[activeKey]?.layerId === layer.layerId && styles['layer-active']}
            onClick={() => setActiveKey(i)}
            onVisibleChange={() => {}}
            overlay={menu(i)}
            trigger={['click']}
          >
            <Text ellipsis style={{ maxWidth: 88 }}>
              {layer.layerName}
            </Text>
          </Dropdown.Button>
        ))}
        <Button icon={<IconFont type="icon-xinjian" />} type="dashed" onClick={createLayer}>
          添加分层
        </Button>
      </Space>
      <Divider />
      {layers[activeKey]?.ruleDef?.rules?.map((rule, iR) => (
        <Fragment key={rule.ruleId}>
          <Input
            placeholder="请输入"
            value={layers[activeKey]?.layerName}
            onChange={({ target: { value } }) => changeLayerName(value)}
          />
          <div className={styles['title-bar']}>
            <span className={styles.title}>规则1</span>
          </div>
          <Collapse
            style={{ marginTop: 16, background: '#fff' }}
            activeKey={expandKeys[iR]}
            onChange={(key) => {
              expandKeys[iR] = key as string[];
              setExpandKeys([...expandKeys]);
            }}
            expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
          >
            <Panel
              header="指标信息"
              key="indicator"
              // extra={
              //   <Link
              //     onClick={(e) => {
              //       e.stopPropagation();
              //       createIndicator(iR);
              //     }}
              //   >
              //     添加
              //   </Link>
              // }
            >
              <Space direction="vertical">
                {rule?.indicatorDefs?.map((_, iI) => (
                  <Space key={iI}>
                    <Select
                      placeholder="请选择指标"
                      options={indicatorCodeOptions}
                      value={_.indicatorCode as string}
                      onChange={(v) => setIndicator(v as string, iR, iI, 'indicatorCode')}
                    />
                    <Select
                      placeholder="关系"
                      options={ConditionOptions}
                      value={_.condition as string}
                      onChange={(v) => setIndicator(v as string, iR, iI, 'condition')}
                    />
                    {rule.indicatorDefs[iI].condition === 'between' ? (
                      <Input.Group compact>
                        <Input
                          style={{ textAlign: 'center' }}
                          placeholder="最小值"
                          value={_.params[0]}
                          onChange={({ target: { value } }) =>
                            setIndicator(value, iR, iI, 'params')
                          }
                        />
                        <Input
                          className="site-input-split"
                          style={{ width: 30, pointerEvents: 'none', background: '#fff' }}
                          placeholder="~"
                          disabled
                        />
                        <Input
                          className="site-input-right"
                          style={{ textAlign: 'center' }}
                          placeholder="最大值"
                          value={rule.indicatorDefs[iI].params[1]}
                          onChange={({ target: { value } }) =>
                            setIndicator(value, iR, iI, 'params', 1)
                          }
                        />
                      </Input.Group>
                    ) : (
                      <Input
                        placeholder="请输入"
                        value={rule.indicatorDefs[iI].params[0]}
                        onChange={({ target: { value } }) => setIndicator(value, iR, iI, 'params')}
                      />
                    )}
                    {/* <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => deleteIndicator(iR, iI)}
                    /> */}
                  </Space>
                ))}
              </Space>
            </Panel>
            <Panel
              header="维度信息"
              key="dimension"
              extra={
                <Link
                  onClick={(e) => {
                    e.stopPropagation();
                    createDimension(iR);
                    if (!expandKeys[iR].includes('dimension')) {
                      expandKeys[iR].push('dimension');
                      setExpandKeys([...expandKeys]);
                    }
                  }}
                >
                  添加
                </Link>
              }
            >
              <Space direction="vertical">
                {rule?.dimensionDefs?.map((_, iD) => (
                  <Space key={iD}>
                    <Select
                      placeholder="请选择维度"
                      value={_.dimensionCode as string}
                      options={dimensionCodeOptions}
                      onChange={(v) => setDimension(v as string, iR, iD, 'dimensionCode')}
                    />
                    <Select
                      placeholder="请选择"
                      value={_.params[0]}
                      options={dimensionParamOptions[iD]}
                      onChange={(v) => setDimension(v as string, iR, iD, 'params')}
                    />
                    <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => deleteDimension(iR, iD)}
                    />
                  </Space>
                ))}
              </Space>
            </Panel>
          </Collapse>
        </Fragment>
      ))}
    </Card>
  );
};

export default EditRules;
