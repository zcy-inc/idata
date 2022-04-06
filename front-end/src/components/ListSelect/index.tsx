import React, { useRef } from 'react';
import { message, Select, Table } from 'antd';
import { TableProps } from 'antd/es/table';
import { useRequest } from 'ahooks';

interface ListSelectProps<RecordType> {
  value?: RecordType[];
  fetchData: () => Promise<RecordType[]>;
  labelField?: string;
  valueField?: string;
  columns?: TableProps<RecordType>['columns'];
  onChange?: (value: RecordType[]) => void;
}

function ListSelect<RecordType extends object = any>({
  columns = [],
  fetchData,
  value,
  labelField,
  valueField,
  onChange,
}: ListSelectProps<RecordType>) {
  const selectItem = useRef<RecordType>();
  let { data = []  } = useRequest(fetchData);
  const options = data.map((item) => {
    const option = { ...item };
    if (labelField) {
      Object.assign(option, { label: item[labelField] });
    }
    if (valueField) {
      Object.assign(option, { value: item[valueField] });
    }
    return option;
  });
  const handleSelect = (_: any, item: RecordType | RecordType[]) => {
    selectItem.current = item as RecordType;
  };
  const handleAdd = () => {
    if (!selectItem.current) {
      message.info('请先选择');
      return;
    }
    if (Array.isArray(value)) {
      const newVal = [...value];
      newVal.push(selectItem.current);
      onChange?.(newVal);
    } else {
      onChange?.([selectItem.current]);
    }
  };
  const handleDelete = (index: number) => {
    if (Array.isArray(value)) {
      const newVal = [...value];
      newVal.splice(index, 1);
      onChange?.(newVal);
    }
  };
  const combinedColumns = [
    ...columns,
    {
      title: '操作',
      key: 'option',
      algin: 'center',
      width: '120px',
      render: (v: unknown, r: unknown, index: number) => (
        <a onClick={() => handleDelete(index)}>删除</a>
      ),
    },
  ];

  return (
    <div style={{ position: 'relative' }}>
      {/* <div style={{ display: 'grid', gridTemplateColumns: 'auto' }}></div> */}
      <Select
        placeholder="请选择"
        options={options}
        showSearch
        filterOption={(input: string, option: any) => option.label.indexOf(input) >= 0}
        onChange={handleSelect}
      />
      {/* TODO: 位置样式 */}
      <a style={{ position: 'absolute', width: 30, right: -34, top: 6 }} onClick={handleAdd}>
        添加
      </a>
      <Table
        rowKey={valueField}
        columns={combinedColumns}
        dataSource={value}
        pagination={false}
        size="small"
        style={{ minHeight: 150, marginTop: 8 }}
      />
    </div>
  );
}

export default ListSelect;
