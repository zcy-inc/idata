import React from 'react'
import { Space, Input, Select, Popconfirm } from 'antd';
import type { ILabelDefines } from 'src/types/labelController'
import type { ProFormColumnsType } from '@ant-design/pro-form';
import BooleanComponent from './components/BooleanComponent';
import { MinusCircleOutlined ,FormOutlined} from '@ant-design/icons';
import Modal from './components/Modal';
import type { TSubjectType } from '@/types/labelController'
const componentMap = {
  "STRING_LABEL": Input,
  "ENUM_VALUE_LABEL": Select,
  "BOOLEAN_LABEL": BooleanComponent
}
interface IDataToColumnsParams {
  data: ILabelDefines[]
  del: (params: { labelCode: string }) => void
  edit: (id: number|undefined) => void
}
export function dataToColumns({ data, del,edit }: IDataToColumnsParams): ProFormColumnsType[] {
  const columns: ProFormColumnsType[] = data?.map?.((item) => {
    const FormComponent = componentMap[item.labelTag||"STRING_LABEL"] || Input;
    const column: ProFormColumnsType = {
      title: item.labelName,
      dataIndex: item.labelCode,
      renderFormItem: () => {
        return (
          <Space>
            <FormComponent style={{ width: "120px" }} />
            <FormOutlined  className="dynamic-delete-button" onClick={() =>edit(item.id)}/>
            <Popconfirm
              title="是否确认删除该项?"
              onConfirm={() => {
                del({ labelCode: item.labelCode })
              }}
              okText="确认"
              cancelText="取消"
            >
              <MinusCircleOutlined
                className="dynamic-delete-button"

              />
            </Popconfirm>
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
  return columns
}
