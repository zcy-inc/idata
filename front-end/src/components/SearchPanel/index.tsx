import React from 'react';
import { Form, Button } from 'antd';
import { SearchOutlined, RedoOutlined } from '@ant-design/icons';

import styles from './index.less';

const getSearchItems = (
  widths: string[],
  options?: TsearchOption[],
  children?: React.ReactNode,
) => {
  if (options) {
    return options.map(({ label, name, component }, index) => (
      <div key={name} style={{ width: widths[index] }}>
        <Form.Item label={label} name={name}>
          {component}
        </Form.Item>
      </div>
    ));
  }
  return React.Children.map(children, (child, index) => (
    <div key={index} style={{ width: widths[index] }}>
      {React.cloneElement(child as React.ReactElement)}
    </div>
  ));
};

const getSearchItemCount = (options?: TsearchOption[], children?: React.ReactNode) => {
  if (options) {
    return options.length;
  }
  return React.Children.count(children);
};

export interface TsearchOption {
  label: string;
  name: string;
  component: React.ReactNode;
}
interface SearchPanelProps {
  templateColumns?: string;
  onSearch?: (values: Record<string, any>) => void;
  options?: TsearchOption[];
  onReset?: () => void;
}

const SearchPanel = ({
  options,
  children,
  templateColumns, // 搜索项宽度
  onSearch,
  onReset,
}: React.PropsWithChildren<SearchPanelProps>) => {
  const [form] = Form.useForm();
  const handleReset = () => {
    form.resetFields();
    onReset?.();
  };
  const handleSearch = () => {
    const values = form.getFieldsValue() || {};
    onSearch?.(values);
  };

  const widthArr = templateColumns?.split(' ') || [];
  const showBtns = getSearchItemCount(options, children) > 1; // 搜索项大于一个时显示搜索按钮
  const searchItemNode = getSearchItems(widthArr, options, children);

  return (
    <Form form={form} className={styles.wrapper}>
      {searchItemNode}
      {showBtns && (
        <Form.Item>
          <Button
            type="primary"
            style={{ marginRight: 16 }}
            onClick={handleSearch}
            icon={<SearchOutlined />}
          >
            查询
          </Button>
          <Button onClick={handleReset} icon={<RedoOutlined />}>
            重置
          </Button>
        </Form.Item>
      )}
    </Form>
  );
};

SearchPanel.SearchItem = Form.Item;

export default SearchPanel;
