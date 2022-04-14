import React, {
  Fragment,
  useEffect,
  useRef,
  useState,
} from 'react';
import { useParams, history } from 'umi';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormGroup,
  ProFormDatePicker,
  ProFormTextArea,
  ProFormCascader
} from '@ant-design/pro-form';
import { PageContainer } from '@ant-design/pro-layout';
import { Form, Spin, message, Button } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import moment from 'moment';
import Title from '@/components/Title';
import EditDerive from './components/EditDerive';
import { getTableReferStr, getTableReferTbs } from '@/services/datadev';
import { getMetric, createMetric, getFolderTree } from '@/services/measure';
import { rules } from '@/constants/datapi';
import { getEnumValues } from '@/services/datadev';
import { AggregatorCodeOptions, degradeDimOptions } from '@/constants/datapi';
import { TreeNodeType } from '@/constants/datapi';
import style from './view.less';
interface ViewModifierProps {
  initialMode: 'view' | 'edit';
  location: {
    pathname: string;
    state: Record<string, any>
  };
}

interface TableOptions {
  label: string;
  value: number | string;
}

const { require } = rules;
const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
];

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ location }) => {
  const params = useParams<{ id: string; }>();
  const isEdit = !!params.id;
  const [form] = Form.useForm();
  const [folderOps, setFolderOps] = useState<TableOptions []>([]);
  const [bizProcessEnum, setBizProcessEnum] = useState<TableOptions []>([]);
  const [DWDTables, setDWDTables] = useState<TableOptions []>([]);
  const [keyList, setKeyList] = useState<TableOptions []>([]);
  const [dataSetOptions, setDataSetOptions] = useState<TableOptions []>([]);
  const [data, setData] = useState<Record<string, any>>({});
  const [metricType, setMetricType] = useState('');
  const [loading, setLoading] = useState<boolean>(false);
  const refDerive = useRef<{onValuesChage: (T: Record<string, any>, P: boolean) => void}>(null);

  useEffect(() => {
    let p1 = getFolderTree({ devTreeType: TreeNodeType.METRIC_LABEL });  // 获取文件夹
    let p2 = getEnumValues({ enumCode: 'bizProcessEnum:ENUM' }); // 获取业务过程
    let p3 = getEnumValues({ enumCode: 'domainIdEnum:ENUM' }); // 获取数据域
    let p4 = getTableReferTbs({ labelValue: 'dws' }); // 获取数据表list

    Promise.all([p1, p2, p3, p4]).then((res) => {
      const [res1, res2, res3, res4] = res;
      const folderOps = res1.data || [];

      const bizProcessEnum = res2.data?.map((enumValue: {enumValue: string; valueCode: string}) => ({
        label: enumValue.enumValue,
        value: enumValue.valueCode,
      }));

      const dataSetOptions = res3.data?.map((enumValue: {enumValue: string; valueCode: string}) => ({
        label: enumValue.enumValue,
        value: enumValue.valueCode,
      }));

      const DWDTables = res4?.data?.map((table: { tableName: any; id: any; }) => ({
        label: table.tableName,
        value: table.id,
      }));

      setFolderOps(folderOps);
      setBizProcessEnum(bizProcessEnum);
      setDWDTables(DWDTables);
      setDataSetOptions(dataSetOptions);

      if(isEdit) {
        getMetricInfo(params.id, folderOps);
      }
    })

    if(!isEdit && !location.state?.labelTag) {
      history.push('/measure/list');
    }
    if(location.state) {
      form.setFieldsValue(location.state);
      setMetricType(location.state.labelTag);
    }
   
  }, []);

  const getMetricInfo = (metricCode: string, folderOps: any[]) => {
    setLoading(true);
    getMetric({ metricCode })
      .then((res) => {
        const transformedData = transformOriginData(res.data, folderOps);
        form.setFieldsValue(transformedData);
        setMetricType(transformedData.labelTag);
        setData(transformedData);
        Object.keys(transformedData).forEach(k => {
          handleValueChange({[k]: transformedData[k]}, true);
        });
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const transformOriginData = (data: {folderId: string; labelCode:string; metricId: string; labelName: string; labelTag: string; labelAttributes: any; measureLabels: any; specialAttribute: any; }, folderOps: any[]) => {
    const { metricId, folderId, labelName, labelCode, labelTag,labelAttributes, measureLabels,  specialAttribute} = data;
    const labelParams: {
      metricId?: string;
      enName?: string;
      bizProcessCode?: string;
      metricDefine?: string;
      comment?: string;
      aggregatorCode?: string;
      tableId?: string;
      columnId?: string;
      timeDim?: string;
      dimTableIds?: string [];
      modifiers?: string [];
      columnName?: string;
    } = {};
    labelAttributes.forEach((attribute: { attributeKey: string; attributeValue: string | number; }) => {
      labelParams[attribute.attributeKey] = attribute.attributeValue;
    });
    Object.keys(specialAttribute).forEach(key => {
      if(key === 'modifiers') {
        labelParams[key] = specialAttribute[key]?.map((item: { modifierCode: any; }) => item.modifierCode);
      } else if(key === 'timeAttribute') {
        labelParams.columnId = specialAttribute[key].columnName;
        labelParams.timeDim = specialAttribute[key].timeDim;
      }else if(key === 'dimTables') {
        labelParams.dimTableIds = specialAttribute[key]?.map((item: { tableId: any; }) => item.tableId);
      } else {
        labelParams[key] = specialAttribute[key];
      }
    });
    labelParams.tableId = measureLabels[0] && measureLabels[0]?.tableId;
    labelParams.columnName = measureLabels[0] && measureLabels[0]?.columnName;
    return {
      metricId,
      labelName,
      labelTag: labelTag.indexOf('ATOMIC_METRIC_LABEL') > -1 ? 'ATOMIC_METRIC_LABEL' : 'DERIVE_METRIC_LABEL',
      labelCode,
      folderId: getFolderRelation(folderOps, folderId),
      ...labelParams,
      measureLabels,
      specialAttribute
    }
  }

  const getFolderRelation = (list: any[], folderId: string) => {
    let relationList: any[] = [];
    for(let i = 0; i < list.length; i++) {
      const item = list[i];
      if(item.folderId === folderId) {
        relationList = [item.folderId];
        break;
      } else if(item.children?.length) {
        const arr = getFolderRelation(item.children, folderId);
        if(arr) {
          relationList = [item.folderId, ...arr];
        }
      } 
    }
    return relationList;
  }

  const handleValueChange = (values: Record<string, any>, init = false) => {
    refDerive.current?.onValuesChage(values, init);
    if(values.labelTag) {
      setMetricType(values.labelTag);
    }
    // 获取字段列表
    if(values.tableId) {
      !init && form.setFieldsValue({columnName: undefined});
      getTableReferStr({ tableId: values.tableId }).then(res => {
        const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setKeyList(strs)
      })
    }
  }

  const onSubmit = () => {
    return form.validateFields().then(values => {
      const {
        aggregatorCode,
        tableId,
        calculableType,
        columnName,
        atomicMetricCode,
        columnId,
        timeDim,
        dimTableIds,
        modifiers,
        degradeDim,
        ...form
      } = values;
      form.metricDeadline = moment(form.metricDeadline).format('YYYY-MM-DD');
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
        folderId: form.folderId?.length ? form.folderId[form.folderId.length -1 ] : 0,
        labelTag: form.labelTag,
        subjectType: 'COLUMN',
        labelAttributes,
      };
      const measureLabels = [{
        tableId: tableId,
        columnName: columnName,
        labelParamValue: 'false',
      }];
      let specialAttribute: any =  { }
     
      // 针对派生指标的额外处理
      if(form.labelTag === 'DERIVE_METRIC_LABEL') {
        specialAttribute = {
          aggregatorCode,
          calculableType,
          atomicMetricCode,
          timeAttribute: {
            columnName: columnId,
            timeDim,
          },
          dimTables: dimTableIds.map((item: any) => ({tableId: item})),
          modifiers: modifiers.map((item: any) => ({modifierCode: item}))
        }
      } else {
        specialAttribute =  { aggregatorCode, calculableType }
      }
      Object.assign(params, {
        measureLabels,
        specialAttribute,
      });
      if (data.labelCode) {
        Object.assign(params, { labelCode: data.labelCode });
      }
      setLoading(true);
      createMetric(params)
        .then((res) => {
          if (res.success) {
            message.success(`${isEdit ? '更新': '新建'}指标成功`);
            history.push(`/measure/view/${res.data.labelCode}`)
          }
        })
        .catch((err) => {})
        .finally(() => setLoading(false));
    })
   
  };

  const renderEditComponent = (metricType: string) => {
    if(metricType) {
      return (
        <Fragment>
          <Title style={{marginTop: 8}}>技术口径</Title>
          <ProFormGroup>
            <ProFormSelect
              name="tableId"
              label="指标来源"
              width="md"
              placeholder="请选择"
              rules={require}
              options={DWDTables}
              fieldProps={{
                showSearch: true,
                filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
              }}
            />
            <ProFormSelect
              name="columnName"
              label=""
              width="md"
              placeholder="请选择"
              rules={require}
              options={keyList}
              fieldProps={{
                showSearch: true,
                filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
              }}
            />
          </ProFormGroup>
          {
            metricType === 'DERIVE_METRIC_LABEL' ?
            <EditDerive ref={refDerive} form={form} /> :
            <ProFormGroup>
              <ProFormSelect
                name="aggregatorCode"
                label="可计算方式"
                width="md"
                placeholder="请选择"
                rules={require}
                options={AggregatorCodeOptions}
                fieldProps={{
                  showSearch: true,
                  filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
                }}
              />
            </ProFormGroup>
          }
          <ProFormGroup>
            <ProFormSelect
              name="calculableType"
              label="是否可累加"
              width="md"
              placeholder="请选择"
              rules={require}
              options={degradeDimOptions}
              fieldProps={{
                showSearch: true,
                filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
              }}
            />
          </ProFormGroup>
        </Fragment>
      )
    }
    return null;
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
                breadcrumbName: '编辑指标信息',
              },
            ],
          },
        }}
      > 
      <Spin spinning={loading} wrapperClassName={style['edit-wrap']}>
        <ProForm
          layout="horizontal"
          colon={false}
          form={form}
          onFinish={onSubmit}
          style={{ marginTop: 16 }}
          onValuesChange={handleValueChange}
          submitter={{
            render: props => {
              return <div className={style['btn-list']}>
                 <a
                  className={style['plain-default-btn']}
                  onClick={() => history.push('/measure/list')}
                >返 回</a>
                <Button key="submit" type="primary" onClick={() => props.form?.submit?.()}>
                  提交
                </Button>
              </div>;
            }
          }}
        >
          <Title>基本信息</Title>
          <ProFormGroup>
            <ProFormText
              name="metricId"
              label="指标ID"
              width="sm"
              placeholder="请输入"
              rules={require}
            />
            <ProFormText
              name="labelName"
              label="指标名称"
              width="sm"
              placeholder="请输入"
              rules={require}
            />
              <ProFormText
              name="enName"
              label="英文别名"
              width="sm"
              placeholder="请输入"
              rules={require}
            />
            <ProFormSelect
              name="domainCode"
              label="数据域"
              width="sm"
              placeholder="请选择"
              rules={require}
              options={dataSetOptions}
              fieldProps={{
                showSearch: true,
                filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
              }}
            />
            <ProFormSelect
              name="bizProcessCode"
              label="业务过程"
              width="sm"
              placeholder="请选择"
              rules={require}
              options={bizProcessEnum}
              fieldProps={{
                showSearch: true,
                filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
              }}
            />
            <ProFormSelect
              name="labelTag"
              label="指标类型"
              disabled
              width="sm"
              placeholder="请选择"
              rules={require}
              options={MetricTypeOps}
            />
            <ProFormDatePicker
              name="metricDeadline"
              label="截止生效日期"
              rules={require}
              width="sm"
            />
            <ProFormCascader
              name="folderId"
              width="sm"
              label="所属文件夹"
              rules={require}
              fieldProps={{
                options: folderOps,
                changeOnSelect: true,
                fieldNames: { label: 'name', value: 'folderId', children: 'children' }
              }}
            />
          </ProFormGroup>
          <Title style={{marginTop: 8}}>指标定义</Title>
          <ProFormGroup>
            <ProFormTextArea
              name="metricDefine"
              label="定义"
              width="xl"
              placeholder="请输入"
              rules={require}
              fieldProps={
                {maxLength:200}
              }
            />
          </ProFormGroup>
          {renderEditComponent(metricType)}
        </ProForm>
      </Spin>
    </PageContainer>
  );
};

export default ViewModifier;
