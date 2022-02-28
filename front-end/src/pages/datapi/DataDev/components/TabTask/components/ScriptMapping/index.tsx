import React from 'react';
import type { FC } from 'react';
import { Form, Input } from 'antd';
import styles from './index.less';

interface ScriptMappingProps {}

const { Item } = Form;
const { TextArea } = Input;

const ScriptMapping: FC<ScriptMappingProps> = ({}) => {
  const [form] = Form.useForm();

  return (
    <div className={styles.form}>
      <Form form={form} layout="vertical">
        <Item label="字段" name="1">
          <TextArea placeholder="请输入" autoSize={{ minRows: 5 }} />
        </Item>
        <div className={styles.tip}>
          <div>
            <span className={styles['tip-title']}>示例</span>
            announcement_id,
          </div>
          <div className={styles['tip-second']}>
            convert(meta_data USING utf8) as meta_data 字段命名须与源表名称保持一致
          </div>
        </div>
        <Item label="去向表关键键" name="2">
          <TextArea placeholder="若获取关键键失败，则默认显示id。" autoSize={{ minRows: 5 }} />
        </Item>
        <Item label="抽取天数" name="3">
          <Input placeholder="单位为天，可输入数值范围为1-7" />
        </Item>
        <Item label="mergeSQL" name="4">
          <TextArea placeholder="请输入" autoSize={{ minRows: 5 }} />
        </Item>
      </Form>
    </div>
  );
};

export default ScriptMapping;
