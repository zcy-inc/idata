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
import { cloneDeep, get, set } from 'lodash';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../../index.less';

import { IconFont } from '@/components';
import { DimensionDef, ObjectLabel, ObjectType, RuleLayer } from '@/types/objectlabel';
import { getDimensionSecondaryOptions, getMetricList } from '@/services/objectlabel';
import { Label } from '@/types/datapi';
import { InitialLayer, ConditionOptions } from './constants';

const { Panel } = Collapse;
const { Link, Text } = Typography;
export interface EditRulesProps {
  initial?: ObjectLabel;
  objectType: ObjectType;
}
type ActionKey = 'copy' | 'delete';

const EditRules: FC<EditRulesProps> = ({ initial, objectType }) => {
  const [layers, setLayers] = useState<RuleLayer[]>([InitialLayer]); // 规则分层
  const [activeKey, setActiveKey] = useState(0); // 规则分层的key，取的是数组的index而非layerId
  const [expandKeys, setExpandKeys] = useState<string[][]>([['indicator', 'dimension']]); // 折叠面板的展开key
  const [indicatorCodeOptions, setIndicatorCodeOptions] = useState([]); // 指标信息中指标的ops，切换objectType时更新
  const [dimensionCodeOptions, setDimensionCodeOptions] = useState([]); // 维度信息中维度的ops，切换指标时更新
  const [dimensionParamOptions, setDimensionParamOptions] = useState<[][]>([]); // 维度信息中维度具体值的ops，切换维度时更新
  const layerCount = useRef(1); // 用以新增规则分层时区分层数的计数器
  const dimensionsMap = useRef({}); // 维度的map，更新维度值的ops时需要用到
  const { setEditLayers } = useModel('objectlabel', (_) => ({
    setEditLayers: _.setEditLayers,
  }));

  useEffect(() => {
    setLayers([
      {
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
      },
    ]);
    layerCount.current = 1;
    getIndicators(objectType);
  }, [objectType]);

  useEffect(() => {
    if (initial && objectType) {
      // 将每一层的指标和维度都展开
      const tmpExpandKeys = initial.ruleLayers.map(() => ['indicator', 'dimension']);
      setExpandKeys(tmpExpandKeys);
      // 赋值分层
      setLayers(initial.ruleLayers);
      // 基于已有的层数，赋值计数器
      layerCount.current = initial.ruleLayers.length;
      // 获取指标信息的ops
      getIndicators(initial.objectType);
      // 获取维度信息的一级维度ops
      const indicatorCodePath = 'ruleLayers.[0].ruleDef.rules.[0].indicatorDefs.[0].indicatorCode';
      const indicatorCode = get(initial, indicatorCodePath, '');
      getMetricList({ labelTag: 'DIMENSION_LABEL', labelCodes: [objectType, `${indicatorCode}`] })
        .then((res) => {
          const tmp = res.data?.map((label: Label) => {
            dimensionsMap.current[label.labelCode] = label;
            return { label: label.labelName, value: label.labelCode };
          });
          setDimensionCodeOptions(tmp);
          // 获取维度信息的二级维度值ops
          // 可能存在复数个维度，用Promise.all来批量获取它们的二级维度值ops
          const promises: Promise<any>[] = [];
          const dimensionDefsPath = 'ruleLayers.[0].ruleDef.rules.[0].dimensionDefs';
          const dimensionDefs: DimensionDef[] = get(initial, dimensionDefsPath, []);
          dimensionDefs.forEach((dimension, i) => {
            if (dimension?.dimensionCode) {
              const measureLabelPath = `${dimension.dimensionCode}.measureLabels.[0`;
              const tmp = get(dimensionsMap.current, measureLabelPath, {});
              promises[i] = getDimensionSecondaryOptions({
                dbSchema: tmp.dbName,
                tableName: tmp.tableName,
                pageSize: 50,
                dimensions: [
                  {
                    columnName: tmp.columnName,
                    dataType: tmp.columnDataType,
                    tableName: tmp.tableName,
                  },
                ],
              });
            }
          });
          Promise.all(promises)
            .then((results) => {
              results?.forEach((res, i) => {
                const tmp = get(res, 'data.data', []);
                dimensionParamOptions[i] = tmp.map((v: string[]) => ({ label: v[0], value: v[0] }));
              });
            })
            .catch((err) => {})
            .finally(() => {
              setDimensionParamOptions([...dimensionParamOptions]);
            });
        })
        .catch((err) => {});
    }
  }, [initial]);

  useEffect(() => {
    setEditLayers(layers);
  }, [layers]);

  /**
   * 获取指标信息
   */
  const getIndicators = (objectType: ObjectType) => {
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

  /**
   * 复制分层时处理名称
   */
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

  /**
   * 创建分层
   */
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

  /**
   * 修改分层的名称
   */
  const changeLayerName = (v: string) => {
    layers[activeKey].layerName = v;
    setLayers([...layers]);
  };

  /**
   * 新建指标
   */
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

  /**
   * 删除指标
   */
  // const deleteIndicator = (iR: number, iI: number) => {
  //   layers[activeKey].ruleDef.rules[iR].indicatorDefs.splice(iI, 1);
  //   setLayers([...layers]);
  // };

  /**
   * 新建维度
   */
  const createDimension = (iR: number) => {
    layers[activeKey].ruleDef.rules[iR].dimensionDefs.push({
      dimensionCode: null,
      params: [],
    });
    setLayers([...layers]);
  };

  /**
   * 删除维度
   */
  const deleteDimension = (iR: number, iD: number) => {
    layers[activeKey].ruleDef.rules[iR].dimensionDefs.splice(iD, 1);
    dimensionParamOptions.splice(iD, 1);
    setDimensionParamOptions([...dimensionParamOptions]);
    setLayers([...layers]);
  };

  /**
   * 修改指标信息
   * @param v 修改的值
   * @param iR 所属规则的下标
   * @param iI 所属指标的下标
   * @param prop 修改的键
   * @param iP 当是区间值当时候，用来区分最小值和最大值
   */
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
          const tmp = res.data?.map((label: Label) => {
            dimensionsMap.current[label.labelCode] = label;
            return { label: label.labelName, value: label.labelCode };
          });
          setDimensionCodeOptions(tmp);
          layers[activeKey].ruleDef.rules[iR].dimensionDefs = [{ dimensionCode: null, params: [] }];
          setLayers([...layers]);
        })
        .catch((err) => {});
    }
  };

  /**
   * 修改维度信息
   * @param v 修改的值
   * @param iR 所属规则的下标
   * @param iD 所属维度的下标
   * @param prop 修改的键
   */
  const setDimension = (v: string, iR: number, iD: number, prop: string) => {
    if (prop === 'params') {
      layers[activeKey].ruleDef.rules[iR].dimensionDefs[iD].params[0] = v;
      setLayers([...layers]);
    } else {
      const tmp = get(dimensionsMap.current[v], 'measureLabels.[0]', {});
      getDimensionSecondaryOptions({
        dbSchema: tmp.dbName,
        tableName: tmp.tableName,
        pageSize: 50,
        dimensions: [
          { columnName: tmp.columnName, dataType: tmp.columnDataType, tableName: tmp.tableName },
        ],
      })
        .then((res) => {
          set(layers, `${activeKey}.ruleDef.rules.[${iR}].dimensionDefs.[${iD}].dimensionCode`, v);
          const tmpList = get(res, 'data.data', []);
          dimensionParamOptions[iD] = tmpList.map((_: string[]) => ({ label: _[0], value: _[0] }));
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
            <Panel header="指标信息" key="indicator">
              <Space direction="vertical">
                {rule?.indicatorDefs?.map((_, iI) => (
                  <Space key={iI}>
                    <Select
                      placeholder="请选择指标"
                      options={indicatorCodeOptions}
                      value={_.indicatorCode as string}
                      onChange={(v) => setIndicator(v as string, iR, iI, 'indicatorCode')}
                      style={{ width: 240 }}
                      showSearch
                      filterOption={(input, option) => `${option?.label}`.indexOf(input) > -1}
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
                          style={{ textAlign: 'center', width: 120 }}
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
                          style={{ textAlign: 'center', width: 120 }}
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
                      style={{ width: 120 }}
                      onChange={(v) => setDimension(v as string, iR, iD, 'dimensionCode')}
                    />
                    <Select
                      placeholder="请选择"
                      value={_.params[0]}
                      options={dimensionParamOptions[iD]}
                      onChange={(v) => setDimension(v as string, iR, iD, 'params')}
                      style={{ width: 120 }}
                      showSearch
                      filterOption={(input, option) => `${option?.label}`.indexOf(input) > -1}
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
