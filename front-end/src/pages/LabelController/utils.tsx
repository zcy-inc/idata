import React from 'react'
import { Space, Input, Select } from 'antd';
import type { ILabelDefines } from 'src/types/labelController'
import type { ProFormColumnsType } from '@ant-design/pro-form';
import BooleanComponent from './components/BooleanComponent';
import { MinusCircleOutlined, FormOutlined } from '@ant-design/icons';
import Modal  from './components/Modal';
import type {TSubjectType} from '@/types/labelController'
const componentMap = {
  "STRING_LABEL": Input,
  "ENUM_VALUE_LABEL": Select,
  "BOOLEAN_LABEL": BooleanComponent
}
interface IDataToColumnsParams{
  data: ILabelDefines[]
  callback: () => void
  subjectType: TSubjectType
}
export  function dataToColumns({data,callback,subjectType}: IDataToColumnsParams): ProFormColumnsType[]{
  const columns: ProFormColumnsType[] = data?.map?.((item) => {
    const FormComponent = componentMap[item.labelTag]||Input;
    const column: ProFormColumnsType = {
      title: item.labelName,
      dataIndex: item.labelCode,
      renderFormItem: () => {
        return (
          <Space>
            <FormComponent style={{width:"120px"}} />
            <FormOutlined
              className="dynamic-delete-button"
            />
            <MinusCircleOutlined
              className="dynamic-delete-button"
            />
          </Space>
        )
      }
    }
    if (item.labelRequired) {
      column.formItemProps = {
        rules: [
          {
            required: true,
            message: '此项为必填项',
          },
        ],
      }
    }
    return column
  })
  columns.push({
      title: ' ',
      renderFormItem: () => {
        return (
                <Modal callback={callback} subjectType={subjectType}/>
        )
      }
  })
return [{ valueType: 'group', columns }]
}
