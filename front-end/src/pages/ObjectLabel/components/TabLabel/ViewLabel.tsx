import React, { Fragment, useEffect, useState } from 'react';
import ProTable from '@ant-design/pro-table';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import { Button, Card, Descriptions, Form, Typography } from 'antd';
import type { FC, Key } from 'react';
import type { ProColumns } from '@ant-design/pro-table';
import styles from '../../index.less';

import Title from '../Title';
import ViewRules from './components/ViewRules';

import { exportObjectLabel, getObjectLabelLayer } from '@/services/objectlabel';
import { ObjectLabel } from '@/types/objectlabel';

export interface ViewDIMProps {}

const { Item } = Descriptions;
const { Link } = Typography;
const columns: ProColumns[] = [
  { title: '主体ID', dataIndex: 'test1', key: 'test1', hideInSearch: true },
  { title: '主体名称', dataIndex: 'test2', key: 'test2', hideInSearch: true },
  { title: '近一月交易额（万元）', dataIndex: 'test3', key: 'test3', hideInSearch: true },
  { title: '近一月交易数（笔）', dataIndex: 'test4', key: 'test4', hideInSearch: true },
];

const ViewDIM: FC<ViewDIMProps> = ({}) => {
  const [data, setData] = useState<ObjectLabel>();
  const [key, setKey] = useState<Key>('');
  const [dataSource, setDataSource] = useState([{}]);
  const [form] = Form.useForm();

  const [visible, setVisible] = useState(false);

  const showModal = () => setVisible(true);
  const hideModal = () => setVisible(false);

  const getData = () => {
    getObjectLabelLayer({
      id: data?.id as number,
      layerId: key as number,
    });
  };

  const onExport = () => {
    exportObjectLabel({
      id: data?.id as number,
      layerId: key as number,
    });
  };

  return (
    <Fragment>
      <Title>基本信息</Title>
      <Descriptions
        column={3}
        colon={false}
        labelStyle={{ color: '#8A8FAE' }}
        style={{ margin: '16px 0' }}
      >
        <Item label="标签名称">{'-'}</Item>
        <Item label="标签英文名">{'-'}</Item>
        <Item label="标签主体">{'-'}</Item>
        <Item label="标签规则">
          <Link onClick={showModal}>查看详情</Link>
        </Item>
        <Item label="更新人">{'-'}</Item>
        <Item label="最近编辑时间">{'-'}</Item>
        <Item label="备注" span={3}>
          {'-'}
        </Item>
      </Descriptions>
      <Title>数据内容</Title>
      <Card className={`${styles.content} ${styles.reset}`}>
        <ProForm layout="horizontal" submitter={false} form={form}>
          <ProFormText
            name="param"
            label="搜索主体"
            placeholder="请输入ID或者名称"
            width="sm"
            fieldProps={{
              onBlur: () => {
                const param = form.getFieldsValue();
                console.log(param);
              },
            }}
          />
        </ProForm>
        <ProTable
          columns={columns}
          dataSource={dataSource}
          search={false}
          options={false}
          toolbar={{
            menu: {
              type: 'tab',
              items: [
                { key: 'tab1', label: '分层1' },
                { key: 'tab2', label: '分层2' },
                { key: 'tab3', label: '分层3' },
              ],
              onChange: (key) => setKey(key as Key),
            },
            actions: [
              <Button onClick={onExport}>导出</Button>,
              <Button onClick={getData}>查询</Button>,
            ],
          }}
        />
      </Card>
      <ViewRules visible={visible} onCancel={hideModal} />
    </Fragment>
  );
};

export default ViewDIM;
