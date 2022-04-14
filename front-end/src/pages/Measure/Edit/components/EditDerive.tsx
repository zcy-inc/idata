import React, {
  forwardRef,
  Fragment,
  useEffect,
  useState,
  useImperativeHandle,
} from 'react';
import {
  ProFormSelect,
  ProFormGroup,
} from '@ant-design/pro-form';
import type { ForwardRefRenderFunction } from 'react';
import { getTableInfo, getMeasures, getForeignKeyTables, getModifiers } from '@/services/measure';
import { rules, timeDimOptions } from '@/constants/datapi';
export interface ViewModifierProps {
  form: any;
}

interface TableOptions {
  label: string;
  value: number | string;
}

const { require } = rules;

interface TempFormTypes {
  dimTableIds: string [],
  atomicMetricCode: string,
  tableId: string
}

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ form }, ref) => {
  const [metricList, setMetricList] = useState<TableOptions []>([]);
  const [labelList, setLabelList] = useState<TableOptions []>([]);
  const [dimensionList, setDimensionList] = useState<TableOptions []>([]);
  const [modifierList, setModifierList] = useState<TableOptions []>([]);
  const [tempForm, setTempForm] = useState<TempFormTypes>({
    dimTableIds: [],
    atomicMetricCode: '',
    tableId: ''
  });

  useImperativeHandle(ref, () => ({
    onValuesChage: handleValueChange
  }));

  useEffect(() => {
    getMeasures({
      measureType: 'METRIC_LABEL',
      enable: true,
      metricType: 'ATOMIC_METRIC_LABEL'
    }).then(res => {  // 获取原子指标列表
      const metricList = res.data?.content.map((_: any) => ({
        label: _.labelName,
        value: _.labelCode,
      }));
      setMetricList(metricList);
    }); 
  }, []);

  const handleValueChange = (values:TempFormTypes, init = false) => {
    setTempForm({
      ...tempForm,
      ...values
    });
    // 根据原子指标获取时间周期
    if(values.atomicMetricCode) {
      !init && form.setFieldsValue({columnId: undefined});
      getTableInfo({metricCode: values.atomicMetricCode}).then(res => {
        const labelList = res.data?.columnInfos?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setLabelList(labelList);
      });
     
    }
    // 根据指标来源获取维度和修饰词
    if(values.tableId) {
      let p1 = getForeignKeyTables({ tableId: values.tableId });
      let p2 = getModifiers({modifierTableIds: [...tempForm.dimTableIds, values.tableId].join()});
      !init && form.setFieldsValue({dimTableIds: [], modifiers: []});
      Promise.all([p1, p2]).then(([res1, res2]) => {
        const dimensionList = res1.data?.map((_: any) => ({ label: _.tableName, value: _.id }));
        const modifierList = res2.data?.map((_: any) => ({ label: _.labelName, value: _.labelCode }));
        setDimensionList(dimensionList);
        setModifierList(modifierList);
      });
    }

    // 根据维度获取修饰词
    if(values.dimTableIds) {
      !init && form.setFieldsValue({ modifiers: []});
      getModifiers({modifierTableIds: [...values.dimTableIds, tempForm.tableId].join()}).then((res) => {
        const modifierList = res.data?.map((_: any) => ({ label: _.labelName, value: _.labelCode }));
        setModifierList(modifierList);
      })
    }
   
  }

  return (
    <Fragment>
      <ProFormGroup>
        <ProFormSelect
          name="atomicMetricCode"
          label="原子指标"
          width="md"
          placeholder="请选择"
          rules={require}
          options={metricList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormSelect
          name="columnId"
          label="时间周期"
          width="md"
          placeholder="请选择"
          rules={require}
          options={labelList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormSelect
          name="timeDim"
          label=""
          width="md"
          placeholder="请选择"
          rules={require}
          options={timeDimOptions}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormSelect
          name="dimTableIds"
          label="维度"
          width="md"
          placeholder="请选择"
          mode="multiple"
          options={dimensionList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormSelect
          name="modifiers"
          label="修饰词"
          mode="multiple"
          width="md"
          placeholder="请选择"
          options={modifierList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
      </ProFormGroup>
    </Fragment>
  );
};

export default forwardRef(ViewModifier);
