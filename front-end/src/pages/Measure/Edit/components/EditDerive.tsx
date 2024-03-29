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
  ProFormCheckbox
} from '@ant-design/pro-form';
import type { ForwardRefRenderFunction } from 'react';
import { getTableInfo, getMeasures, getForeignKeyTables, getModifiers } from '@/services/measure';
import { rules, timeDimOptions } from '@/constants/datapi';
import { isBoolean } from 'lodash';
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
  tableId: string;
  allColumns: boolean;
}

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ form }, ref) => {
  const [metricList, setMetricList] = useState<TableOptions []>([]);
  const [labelList, setLabelList] = useState<TableOptions []>([]);
  const [dimensionList, setDimensionList] = useState<TableOptions []>([]);
  const [modifierList, setModifierList] = useState<TableOptions []>([]);
  const [tempForm, setTempForm] = useState<TempFormTypes>({
    dimTableIds: [],
    atomicMetricCode: '',
    tableId: '',
    allColumns: false
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
      getTableInfo({metricCode: values.atomicMetricCode, isAllColumns: tempForm.allColumns}).then(res => {
        const labelList = res.data?.columnInfos?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setLabelList(labelList);
      });
    }
    // 根据是否勾选全部获取时间周期
    if(isBoolean(values.allColumns)) {
      !init && form.setFieldsValue({columnId: undefined});
      getTableInfo({metricCode: tempForm.atomicMetricCode, isAllColumns: values.allColumns}).then(res => {
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
    if(values.dimTableIds?.length) {
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
          tooltip="来自原子指标库（单选）"
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
          tooltip="来自原子指标所对应的表上的时间格式字段（单选）"
          rules={[{validator: (_rule: any, _value: any, cb: (arg0: string) => any) => {
            const values = form.getFieldsValue(['timeDim']);
            if(!_value && values.timeDim) {
              return cb('请选择时间周期！');
            }
            return Promise.resolve();
           
          }}]}
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
          options={timeDimOptions}
          rules={[{validator: (_rule: any, _value: any, cb: (arg0: string) => any) => {
            const values = form.getFieldsValue(['columnId']);
            if(values.columnId && !_value) {
              return cb('请选择时间范围！');
            }
            return Promise.resolve();
          }}]}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
         <ProFormCheckbox
          name="allColumns"
        >展示全部</ProFormCheckbox>
      </ProFormGroup>
      <ProFormGroup>
        <ProFormSelect
          name="dimTableIds"
          label="维度"
          width="md"
          placeholder="请选择"
          tooltip="指标来源的dws表数仓模型所关联的维表（若模型未关联，则选择不出来，多选）"
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
          tooltip="选择的指标来源+维度并集修饰词字段属性值（多选）"
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
