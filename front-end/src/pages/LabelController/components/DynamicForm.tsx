import type { FC } from 'react';
import React, { useState } from 'react';
import { BetaSchemaForm } from '@ant-design/pro-form';
import type { ProFormColumnsType } from '@ant-design/pro-form';
import { findDefines } from '@/services/labelController'
import { Spin } from 'antd';
import type { TSubjectType } from '@/types/labelController'
import { useRequest } from 'umi';
import { dataToColumns } from '../utils'
import './index.less'

type DataItem = {
  name: string;
  state: string;
};
interface IDynamicFormProps {
  subjectType: TSubjectType
}
const DynamicForm: FC<IDynamicFormProps> = (props) => {
  const { subjectType } = props;
  const [columns, setColumns] = useState<ProFormColumnsType[]>([])
  const { loading: fetchLoading } = useRequest(() => findDefines(subjectType), {
    onSuccess: (data) => {
      const newColumns = dataToColumns(data)
      setColumns(newColumns)
    }
  });

  return (
    <Spin spinning={fetchLoading}>
      <BetaSchemaForm<DataItem>
        layout="horizontal"
        colon={false}
        submitter={false}
        columns={columns}
      />
    </Spin>
  );
};

export default DynamicForm;
