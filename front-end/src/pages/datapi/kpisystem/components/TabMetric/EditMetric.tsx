import React, {
  forwardRef,
  Fragment,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';
import EditAtomic from './components/EditAtomic';
import EditDerive from './components/EditDerive';
import EditComplex from './components/EditComplex';
import { getFolders } from '@/services/kpisystem';
import { rules } from '@/constants/datapi';
import { Metric } from '@/types/datapi';

export interface ViewModifierProps {
  initial?: Metric;
}
interface AtomicExportProps {
  data: [];
}
interface DeriveExportProps {
  data: {
    atomic: string;
    test: [];
  };
}
interface ComplexExportProps {
  data: string;
}

const { require } = rules;
const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
  { label: '复合指标', value: 'COMPLEX_METRIC_LABEL' },
];

const ViewModifier: ForwardRefRenderFunction<unknown, ViewModifierProps> = ({ initial }, ref) => {
  const [folderOps, setFolderOps] = useState([]);
  const [form] = Form.useForm();
  const [metricType, setMetricType] = useState('ATOMIC_METRIC_LABEL');
  const refAtomic = useRef<AtomicExportProps>();
  const refDerive = useRef<DeriveExportProps>();
  const refComplex = useRef<ComplexExportProps>();
  const EditMap = {
    ATOMIC_METRIC_LABEL: [<EditAtomic ref={refAtomic} />, refAtomic.current?.data],
    DERIVE_METRIC_LABEL: [<EditDerive ref={refDerive} />, refDerive.current?.data],
    COMPLEX_METRIC_LABEL: [<EditComplex ref={refComplex} />, refComplex.current?.data],
  };

  useImperativeHandle(ref, () => ({
    form: form.getFieldsValue(),
    data: EditMap[metricType][1],
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({
          label: _.folderName,
          value: `${_.id}`,
        }));
        setFolderOps(fd);
      })
      .catch((err) => {});
  }, []);

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        form={form}
        submitter={false}
      >
        <ProFormGroup>
          <ProFormSelect
            name="labelTag"
            label="指标类型"
            width="sm"
            placeholder="请选择"
            rules={require}
            options={MetricTypeOps}
            initialValue="ATOMIC_METRIC_LABEL"
            fieldProps={{ onChange: setMetricType }}
          />
          <ProFormText
            name="labelName"
            label="指标名称"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormText
            name="metricId"
            label="Code"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
        </ProFormGroup>
        <ProFormGroup>
          <ProFormText
            name="enName"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={require}
          />
          <ProFormSelect
            name="bizTypeCode"
            label="业务过程"
            width="sm"
            placeholder="请选择"
            rules={require}
            options={folderOps}
          />
          <ProFormSelect
            name="folderId"
            label="位置"
            width="sm"
            placeholder="根目录"
            options={folderOps}
          />
        </ProFormGroup>
        <ProFormText
          name="metricDefine"
          label="定义"
          width="md"
          placeholder="请输入"
          rules={require}
        />
        <ProFormTextArea name="comment" label="备注" width="md" placeholder="请输入" />
      </ProForm>
      {EditMap[metricType][0]}
    </Fragment>
  );
};

export default forwardRef(ViewModifier);
