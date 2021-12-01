import React from 'react';
import { Input, Form, Space } from 'antd';
import type { FC } from 'react';
import { IconFont } from '@/components';
import styles from './index.less';

interface ParamListProps {
  formName: string[];
}

const { Item, List } = Form;
const ruleText = [{ required: true, message: '请输入' }];

const ParamList: FC<ParamListProps> = ({ formName }) => {
  return (
    <List name={formName[0]}>
      {(fields, { add, remove }) => (
        <>
          {fields.map(({ key, name, fieldKey, ...restField }) => (
            <Space key={key} style={{ display: 'flex', marginBottom: 8 }} align="baseline">
              <Item
                {...restField}
                name={[name, formName[1]]}
                fieldKey={[fieldKey, formName[1]]}
                rules={ruleText}
                style={{ marginBottom: 0 }}
              >
                <Input size="large" placeholder="参数值" />
              </Item>
              <Item
                {...restField}
                name={[name, formName[2]]}
                fieldKey={[fieldKey, formName[2]]}
                rules={ruleText}
                style={{ marginBottom: 0 }}
              >
                <Input size="large" placeholder="备注" />
              </Item>
              <IconFont type="icon-shanchuchanggui" onClick={() => remove(name)} />
            </Space>
          ))}
          <span className={styles.addparam} onClick={add}>
            + 新增参数
          </span>
        </>
      )}
    </List>
  );
};

export default ParamList;
