import React, { FC, useEffect, useState } from 'react';
import { Select, message } from 'antd';
import { cloneDeep, get } from 'lodash';
import useDataSource, { DSOption } from '@/hooks/useDataSource';
import { DefaultOptionType } from 'antd/lib/select';
import { PlusCircleOutlined, DeleteOutlined, CopyOutlined } from '@ant-design/icons';
import { copy } from '@/utils/utils';
import styles from './index.less';

export interface ItemValue {
  dataSourceType?: string;
  dataSourceId?: number;
  dataSourceUDCode?: string;
}

const defaultItemValue: ItemValue = {};

const DataSourceSelectItem: FC<{
  value: ItemValue;
  typeOptions: DefaultOptionType[];
  fetchSourceList: (type?: string) => unknown;
  sourceOptions: DSOption[];
  onChange: (value: ItemValue) => void;
  onAdd?: () => void;
  onDelete?: () => void;
  quantityCustom?: boolean;
  fetchTemplate?: (value: ItemValue[]) => Promise<string>;
}> = ({
  typeOptions,
  value,
  fetchSourceList,
  sourceOptions,
  onChange,
  onAdd,
  onDelete,
  quantityCustom,
  fetchTemplate,
}) => {
  useEffect(() => {
    fetchSourceList(value.dataSourceType);
  }, [value.dataSourceType]);
  const onTypeChange = (type?: string) => {
    const newValue: ItemValue = { dataSourceType: type };
    onChange(newValue);
  };

  const onSourceChange = (sourceId: number, itemData: DSOption | DSOption[]) => {
    const dataSourceUDCode = get(itemData, 'dbConfigList[0].dbName');
    const newValue: ItemValue = {
      ...value,
      dataSourceId: sourceId,
      dataSourceUDCode,
    };
    onChange(newValue);
  };

  const handleCopy = async () => {
    if (typeof value.dataSourceType !== 'undefined' && typeof value.dataSourceId !== 'undefined') {
      const template = await fetchTemplate?.([value]);
      if (typeof template === 'string') {
        copy(template);
        message.success('已复制到剪贴板');
      }
    }
  };

  return (
    <div className={styles.DSSelectItem}>
      <Select value={value?.dataSourceType} options={typeOptions} onChange={onTypeChange} />
      <Select<number, DSOption>
        value={value?.dataSourceId}
        options={sourceOptions}
        onChange={onSourceChange}
      />

      <div className={styles.btns}>
        {quantityCustom && (
          <>
            <PlusCircleOutlined style={{ color: '#304ffe' }} onClick={onAdd} />
            <DeleteOutlined style={{ marginLeft: 4 }} onClick={onDelete} />
          </>
        )}
        <CopyOutlined style={{ marginLeft: 4 }} onClick={handleCopy} />
      </div>
    </div>
  );
};

export interface DataSourceSelectProps {
  value?: ItemValue[];
  onChange?: (value: ItemValue[]) => void;
  fetchTemplate?: (value: ItemValue[]) => Promise<string>;
  quantityCustom?: boolean;
  jobType: string;
  isDest: boolean;
}

const DataSourceSelect: FC<DataSourceSelectProps> = ({
  value,
  onChange,
  quantityCustom,
  fetchTemplate,
  jobType,
  isDest,
}) => {
  const [innerValue, setInnerValue] = useState<ItemValue[]>([]);

  useEffect(() => {
    if (!Array.isArray(value) || value.length === 0) {
      setInnerValue([cloneDeep(defaultItemValue)]);
    } else {
      setInnerValue(value);
    }
  }, [value]);
  const { destOptions, fromOptions, getSourceOptions, fetchSourceList } = useDataSource({jobType});
  const typeOptions = isDest ? destOptions : fromOptions;
  const handleTypeChange = (index: number) => (type?: string) => {
    fetchSourceList(index, type);
  };

  const handleChange = (index: number, value: ItemValue) => {
    const newValue = cloneDeep(innerValue);
    newValue.splice(index, 1, value);
    if (value) {
      onChange?.(newValue);
    } else {
      setInnerValue(newValue);
    }
  };

  const handleAdd = (index: number) => {
    const newInnerValue = cloneDeep(innerValue);
    newInnerValue.splice(index + 1, 0, { ...defaultItemValue });
    if (!value) {
      setInnerValue(newInnerValue);
    }
    onChange?.(newInnerValue);
  };

  const handleDelete = (index: number) => {
    const newInnerValue = cloneDeep(innerValue);
    newInnerValue.splice(index, 1);
    if (!value) {
      setInnerValue(newInnerValue);
    }
    onChange?.(newInnerValue);
  };

  const restItemProps = { fetchTemplate, quantityCustom };

  return (
    <div className={styles.DSSelect}>
      {innerValue.map((itemValue, index) => (
        <DataSourceSelectItem
          key={index}
          value={itemValue}
          typeOptions={typeOptions}
          fetchSourceList={handleTypeChange(index)}
          sourceOptions={getSourceOptions(index)}
          onChange={(val) => handleChange(index, val)}
          onAdd={() => handleAdd(index)}
          onDelete={() => handleDelete(index)}
          {...restItemProps}
        />
      ))}
    </div>
  );
};

export default DataSourceSelect;
