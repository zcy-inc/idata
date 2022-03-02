import React, { useEffect, FC } from 'react';
import { Button, Drawer, Form, Input, message, Select } from 'antd';
import { useModel } from 'umi';
import { useRequest } from 'ahooks';
import _ from 'lodash';
import Title from '@/components/Title';
import { DIJobBasicInfo } from '@/types/datadev';
import { editTask, getEnumValues, getDIJobTypes, getDISyncMode, saveDIJobBasicInfo } from '@/services/datadev';
import { IPane } from '@/models/datadev';

import styles from './index.less';

interface DrawerBasicProps {
  visible: boolean;
  onClose: () => void;
  data?: DIJobBasicInfo;
  pane: IPane;
  refreshBasicInfo: () => Promise<unknown>;
}

const { Item } = Form;
const { TextArea } = Input;
const widthL = 400;
const ruleText = [{ required: true, message: '请输入' }];
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerBasic: FC<DrawerBasicProps> = ({ visible, onClose, data, pane, refreshBasicInfo }) => {
  const [form] = Form.useForm();
  const { data: jobTypeOptions } = useRequest(getDIJobTypes);
  const { data: syncModeOptions = [], run: getSyncModeOptions } = useRequest(getDISyncMode, {
    manual: true,
  });
  const { data: layersRes } = useRequest(() => getEnumValues({ enumCode: 'dwLayerEnum:ENUM' }));
  const layerOption =
    layersRes?.data.map(({ enumValue, valueCode }) => ({
      label: enumValue,
      value: valueCode,
    })) || [];
  const { replaceTab, getTreeWrapped } = useModel('datadev', (_) => ({
    replaceTab: _.replaceTab,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    (async () => {
      if (visible && data) {
        await getSyncModeOptions({ jobType: data.jobType });
        form.setFieldsValue(data);
      }
    })();
  }, [visible, data]);

  const onSave = () => {
    const values = form.getFieldsValue();
    const params = {
      ...data,
      ...values,
    };
    saveDIJobBasicInfo(params)
      .then((res) => {
        if (res.success) {
          message.success('保存成功');
          getTreeWrapped();
          replaceTab({
            oldKey: pane.cid,
            newKey: pane.cid,
            title: res.data.name,
            pane,
          });
          onClose();
          refreshBasicInfo();
        }
      })
      .catch((err) => {});
  };

  return (
    <Drawer
      className={styles['drawer-basic']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="配置"
      footer={
        <Button size="large" onClick={onSave}>
          保存
        </Button>
      }
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Title>基本配置</Title>
      <Form form={form} layout="horizontal" colon={false}>
        <Item name="name" label="任务名称" rules={ruleSelc}>
          <Input size="large" style={{ width: widthL }} placeholder="请选择" />
        </Item>
        <Item name="dwLayerCode" label="数仓分层" rules={ruleSelc}>
          <Select
            size="large"
            style={{ width: widthL }}
            placeholder="请选择"
            options={layerOption}
            disabled
          />
        </Item>
        <Item name="jobType" label="任务类型" rules={ruleText}>
          <Select
            size="large"
            style={{ width: widthL }}
            placeholder="请输入"
            options={jobTypeOptions}
            disabled
          />
        </Item>
        <Item name="syncMode" label="同步类型" rules={ruleText}>
          <Select
            size="large"
            style={{ width: widthL }}
            placeholder="请选择"
            options={syncModeOptions}
            disabled
          />
        </Item>
        <Item name="creator" label="所属人">
          <Input size="large" style={{ width: widthL }} placeholder="-" disabled />
        </Item>
        <Item name="remark" label="备注">
          <TextArea style={{ width: widthL }} placeholder="请输入" />
        </Item>
      </Form>
    </Drawer>
  );
};

export default DrawerBasic;
