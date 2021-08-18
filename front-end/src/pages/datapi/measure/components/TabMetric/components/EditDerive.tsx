import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect } from '@ant-design/pro-form';
import { Form, Select, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction, Key } from 'react';
import styles from '../../../../measure/index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../../components/Title';
import { LabelTag, rules } from '@/constants/datapi';
import { EnumValue, Label, Metric, Modifier } from '@/types/datapi';
import { getEnumValues, getTableLabels } from '@/services/tablemanage';
import { getAtomicMetrics } from '@/services/measure';

interface EditDeriveProps {
  initial?: Metric;
}
interface AtomicOption {
  label: string;
  value: string;
}
interface ModifierOption {
  label: string;
  value: string;
  enumCode: string;
  disabled?: boolean;
}
interface DWD {
  id: Key;
  modifierCode?: string;
  enumValueCodes?: string[];
}

const { Link } = Typography;
const { require } = rules;

const EditDerive: ForwardRefRenderFunction<unknown, EditDeriveProps> = ({ initial }, ref) => {
  const [DWDData, setDWDData] = useState<DWD[]>([]);
  const [DWDKeys, setDWDKeys] = useState<Key[]>([]);
  const [atomicOptions, setAtomicOptions] = useState<AtomicOption[]>([]);
  const [modifierOptions, setModifierOptions] = useState<ModifierOption[]>([]);
  const [enumOptions, setEnumOptions] = useState<[][]>([]);
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    atomicMetricCode: form.getFieldsValue().atomicMetricCode,
    modifiers: DWDData,
  }));

  useEffect(() => {
    getTableLabels({ labelTag: LabelTag.ATOMIC_METRIC_LABEL, subjectType: 'COLUMN' })
      .then((res) => {
        const tmp = res.data?.map((atomic: Label) => ({
          label: atomic.labelName,
          value: atomic.labelCode,
        }));
        setAtomicOptions(tmp);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      const atomicMetricCode = initial.specialAttribute.atomicMetricCode;
      form.setFieldsValue({ atomicMetricCode });
      const tmp: DWD[] = [];
      const tmpK: Key[] = [];
      const promises: Promise<any>[] = [];
      initial.modifiers?.forEach((modifier, i) => {
        tmpK.push(i);
        tmp.push({
          id: i,
          modifierCode: modifier.modifierCode,
          enumValueCodes: modifier.enumValueCodes,
        });
        promises.push(getEnumValues({ enumCode: modifier.modifierAttribute.attributeValue }));
      });
      Promise.all(promises)
        .then((results) => {
          results?.forEach((res, i) => {
            const ops = res.data?.map((enumValue: EnumValue) => ({
              label: enumValue.enumValue,
              value: enumValue.valueCode,
            }));
            enumOptions[i] = ops;
            setEnumOptions([...enumOptions]);
          });
        })
        .catch((err) => {});
      getModifiers(atomicMetricCode);
      setDWDData(tmp);
      setDWDKeys(tmpK);
    }
  }, [initial]);

  // 选择原子指标时获取相应的修饰词
  const getModifiers = (atomicMetricCode: string) => {
    getAtomicMetrics({ atomicMetricCode })
      .then((res) => {
        const ops = res.data?.map((modifier: Modifier) => ({
          label: modifier.labelName,
          value: modifier.labelCode,
          enumCode: modifier.labelAttributes.find(
            (labelAttribute) => labelAttribute.attributeKey === 'modifierEnum',
          )?.attributeValue,
        }));
        setModifierOptions(ops);
      })
      .catch((err) => {});
  };

  const getEnums = (labelCode: string, index: number) => {
    const enumCode = modifierOptions.find((modifier) => (modifier.value = labelCode))
      ?.enumCode as string;
    getEnumValues({ enumCode })
      .then((res) => {
        const ops = res.data?.map((enumValue: EnumValue) => ({
          label: enumValue.enumValue,
          value: enumValue.valueCode,
        }));
        enumOptions[index] = ops;
        setEnumOptions([...enumOptions]);
      })
      .catch((err) => {});
  };

  // 添加一行数据
  const addData = () => {
    const id = Date.now();
    const data = { id };
    setDWDData([...DWDData, data]);
    setDWDKeys([...DWDKeys, id]);
  };

  const setValue = (schema: any, value: string) => {
    if (schema.dataIndex === 'modifierCode') {
      if (value) {
        getEnums(value, schema.index);
        // const i = modifierOptions.findIndex((item) => item.value === value);
        // modifierOptions[i].disabled = true;
      } else {
        // const i = modifierOptions.findIndex(
        //   (item) => item.value === DWDData[schema.index].modifierCode,
        // );
        // modifierOptions[i].disabled = false;
      }
    }
    DWDData[schema.index][schema.dataIndex] = value;
    setDWDData([...DWDData]);
    // setModifierOptions([...modifierOptions]);
  };

  // 操作栏行为
  const onAction = (row: any, _: any) => {
    const i = DWDData.findIndex((_) => _.id === row.id);
    DWDData.splice(i, 1);
    DWDKeys.splice(i, 1);
    setDWDData([...DWDData]);
    setDWDKeys([...DWDKeys]);
  };

  const Cols: ProColumns[] = [
    {
      title: '修饰词',
      dataIndex: 'modifierCode',
      key: 'modifierCode',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={modifierOptions}
          onChange={(value) => setValue(schema, value as string)}
        />
      ),
    },
    {
      title: '枚举值',
      dataIndex: 'enumValueCodes',
      key: 'enumValueCodes',
      renderFormItem: (schema) => (
        <Select
          mode="multiple"
          allowClear
          placeholder="请选择"
          options={enumOptions[schema.index as number]}
          onChange={(value) => setValue(schema, value as string)}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];

  return (
    <Fragment>
      <Title>配置派生指标</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        form={form}
        layout="horizontal"
        colon={false}
        submitter={false}
      >
        <ProFormSelect
          name="atomicMetricCode"
          label="原子指标"
          width="md"
          placeholder="请选择"
          rules={require}
          options={atomicOptions}
          fieldProps={{ onChange: getModifiers }}
        />
      </ProForm>
      <EditableProTable
        className={styles.reset}
        rowKey="id"
        columns={Cols}
        value={DWDData}
        pagination={false}
        recordCreatorProps={false}
        style={{ marginTop: 24 }}
        cardProps={{ bodyStyle: { padding: 0 } }}
        editable={{
          type: 'multiple',
          editableKeys: DWDKeys,
          onChange: setDWDKeys,
          actionRender: (row, _) => [<Link onClick={() => onAction(row, _)}>删除</Link>],
        }}
      />
      <Link onClick={addData} style={{ display: 'inline-block', marginTop: 16 }}>
        <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
        添加修饰词
      </Link>
    </Fragment>
  );
};

export default forwardRef(EditDerive);
