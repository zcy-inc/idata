import React, { useEffect, FC } from 'react';
import { Button, Form, Table, Alert, Switch, Space, Input } from 'antd';
import type { ColumnsType } from 'antd/lib/table/Table';
import type { ActualTableItem } from '@/types/datasource';
import { TableSelectInput } from '@/components';
import { getDestTableName } from '@/services/datadev';
import styles from './index.less';
import { PlusCircleOutlined, MinusCircleOutlined } from '@ant-design/icons';
const { Item } = Form;
interface ActualTableProps {
  tableOptions: {label: string; value: string} [];
  onChange: any;
  jobContent: any;
  isView: boolean;
}



const ActualTable: FC<ActualTableProps> = ({ tableOptions, jobContent, onChange, isView }) => {
  const {tableDtoList: data = [], enableSharding = false} = jobContent;
  useEffect(() => {
  }, []);

  const addNew = () => {
    const newRow: ActualTableItem = {
      srcTable: {
        inputMode: 'S',
        rawTable: ''
      },
      destTable: undefined,
      tableCdcPropList: [],
    }
    onChange([...data, newRow]);
  }

  const remove = (index: number) => {
    onChange([
      ...data.slice(0, index),
      ...data.slice(index + 1)
    ])
  }

  const addParams = (rowInex: number) => {
    data[rowInex].tableCdcPropList = data[rowInex].tableCdcPropList || []; 
    onChange([
      ...data.slice(0, rowInex),
      {
        ...data[rowInex],
        tableCdcPropList: [
          ...data[rowInex].tableCdcPropList,
          {
            key: undefined,
            value: undefined
          }
        ]
      },
      ...data.slice(rowInex + 1)
    ])
  }

  const removeParams = (rowInex: number, index: number) => {
    onChange([
      ...data.slice(0, rowInex),
      {
        ...data[rowInex],
        tableCdcPropList: [
          ...data[rowInex].tableCdcPropList.slice(0, index),
          ...data[rowInex].tableCdcPropList.slice(index + 1),
        ]
      },
      ...data.slice(rowInex + 1)
    ])
  }

  const srcItemRules = (value: any, rowIndex: number) => {
    if(!value) {
      return Promise.reject();
    } else if(value.inputMode === 'E' && (!value.rawTable || !value.tableIdxBegin || !value.tableIdxEnd)) {
      return Promise.reject();
    } else if(value.inputMode === 'S' && !value.rawTable) {
      return Promise.reject(' ');
    } else  {
      const exist = data.find((item: any, index: number) => {
        return item.srcTable.rawTable === value.rawTable && rowIndex !== index
      });
      return exist ? Promise.reject(' ') : Promise.resolve();
    }
  }

  const handleSrcTableBlur = async (srcTable: any, rowIndex: number) => {
    const {inputMode, rawTable, tableIdxBegin, tableIdxEnd} = srcTable;
    if((inputMode === 'E' && rawTable && tableIdxBegin && tableIdxEnd) || (inputMode === 'S' && rawTable)) {
      const { srcDataSourceType, srcDataSourceId,  destDataSourceType, enableSharding } = jobContent;
      const target = {...data[rowIndex], srcTable};
      target.destTable = await getDestTableName({
        srcDataSourceType,
        srcDataSourceId,
        destDataSourceType,
        enableSharding: enableSharding ? 1 : 0,
        srcTables: inputMode === "E" ? `${rawTable}[${tableIdxBegin}-${tableIdxEnd}]` : rawTable
      }).then(data => data[0]);
      onChange([
        ...data.slice(0, rowIndex),
        target,
        ...data.slice(rowIndex + 1)
      ])
    }
  }

  const tableColumns: ColumnsType<ActualTableItem> = [
    {
      title: '来源表名',
      key: 'srcTable',
      dataIndex: 'srcTable',
      width:240,
      render: (_, row, index) =>
        <Item rules={[{validator: (_, value) => srcItemRules(value, index)}]} name={['tableDtoList', index, 'srcTable']}>
           <TableSelectInput
            options={tableOptions}
            showChange={false}
            disabled={isView}
            selectMode="single"
            onBlur={val => handleSrcTableBlur(val ,index)}
          />
        </Item>
    },
    {
      title: '去向表名',
      key: 'destTable',
      dataIndex: 'destTable',
      width:240
    },
    {
      title: '自定义参数',
      key: 'tableCdcPropList',
      dataIndex: 'tableCdcPropList',
      width:240,
      render: (fields, row, rowIndex) => {
        return fields?.length ? fields.map(({key}: {key: string}, index: number) => (
          <Space key={index} align="baseline">
            <Item
              name={['tableDtoList', rowIndex, 'tableCdcPropList', index, 'key']}
              style={{ width: 100 }}
              rules={[{ required: true, message: '' }]}
            >
              <Input placeholder="请输入参数名" disabled={isView} />
            </Item>
            <Item
              name={['tableDtoList', rowIndex, 'tableCdcPropList', index, 'value']}
              style={{ width: 100 }}
              rules={[{ required: true, message: '' }]}
            >
              <Input placeholder="请输入参数值" disabled={isView} />
            </Item>
            {!isView &&  <>
              <MinusCircleOutlined onClick={() => removeParams(rowIndex, index)} style={{color: '#ff5753'}} />
              {index === fields.length - 1 && <PlusCircleOutlined onClick={() => addParams(rowIndex)} style={{color: '#1890ff'}} />}
            </> }
          </Space>
        )) : isView ? null : <PlusCircleOutlined
          style={{color: '#1890ff'}}
          onClick={() => addParams(rowIndex)}
        />
      }
    
    },
    {
      title: '操作',
      width:200,
      render: (text, row, index) => <Button danger type="text" onClick={() => remove(index)} disabled={enableSharding}>
      删除
    </Button>,
    },
  ];

  const tableData = data.map((item: any, index: number) => ({...item, id: index}))
  return (
    <>
     {!isView && <Item
        valuePropName="checked"
        name="enableSharding"
        label="开启分表同步"
        tooltip="分表同步是将拆分在多张表的数据，同步至一张表。来源表名必须是规则的，去向表名只有一张"
      >
        <Switch size="small" />
      </Item>}
      <Alert showIcon message="实时同步作业不支持筛选同步字段，来源表与去向表的字段必须保持一致。" type="info" />
      <Table<ActualTableItem>
        rowKey="id"
        columns={isView ? tableColumns.slice(0, 2) : tableColumns}
        dataSource={tableData}
        scroll={{ x: 'max-content' }}
        className={styles.table}
        pagination={false}
      />
      {!enableSharding && !isView && <Button
        type="dashed"
        disabled={data.length > 49}
        style={{display: 'block', margin: '10px 0', width: '100%'}}
        onClick={addNew}
      >
        添加一行数据
      </Button>}
     
    </>
  );
};

export default ActualTable;
