/* eslint-disable @typescript-eslint/no-use-before-define */
import type { FC } from 'react';
import React, { useState } from 'react';
import { BetaSchemaForm } from '@ant-design/pro-form';
import type { ProFormColumnsType } from '@ant-design/pro-form';
import { findDefines } from '@/services/labelController'
import { deleteLabel } from '@/services/datadev';
import { Spin, message, Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import type { TSubjectType } from '@/types/labelController'
import Modal from './Modal'
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
  const [columns, setColumns] = useState<ProFormColumnsType[]>([]);
  const [modelVisible, setVisible] = useState<boolean>(false);
  const [labelDefineId, setLabelDefineId] = useState<number>();
  const { loading: delLoading, run: deleteLabelRun } = useRequest(deleteLabel, {
    onSuccess: () => {
      message.success('删除成功')
      reload()
    }
  });
  const addLabel = () => {
    setLabelDefineId(undefined);
    setVisible(true)
  }
  const { loading: fetchLoading, run: reload } = useRequest(() => findDefines(subjectType), {
    onSuccess: (data) => {
      const newColumns = dataToColumns({
        data,
        del: deleteLabelRun
      })
      newColumns?.push?.({
        title: ' ',
        renderFormItem: () => {
          return (
            <Button
              type="primary"
              onClick={addLabel}
            >
              <PlusOutlined />
              新建属性
            </Button>
          )
        }
      })
      setColumns([{
        valueType: 'group',
        columns:newColumns
      }])
    }
  });


  return (
    <Spin spinning={fetchLoading || delLoading}>
      <BetaSchemaForm<DataItem>
        layout="horizontal"
        colon={false}
        submitter={false}
        columns={columns}
      />
      <Modal
        callback={reload}
        onCancel={() => setVisible(false)}
        subjectType={subjectType}
        visible={modelVisible}
        labelDefineId={labelDefineId}
      />
    </Spin>
  );
};

export default DynamicForm;
