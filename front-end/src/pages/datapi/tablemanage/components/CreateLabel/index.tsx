import React, { useEffect, useState } from 'react';
import ProForm, { ModalForm, ProFormText, ProFormSelect, ProFormRadio } from '@ant-design/pro-form';
import { Input, Table, Radio, Select, Row, Col, message, Form } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import { useModel } from 'umi';
import type { ProColumns } from '@ant-design/pro-table';
import type { TableColumnType } from 'antd';
import type { FC, Key } from 'react';
import styles from '../../index.less';

import SelectEnum from '../SelectEnum';
import Title from '../../../components/Title';
import {
  rules,
  SubTypeOps,
  IsReqOps,
  LabelTagOps,
  TagToParamMap,
  EditorBoolOps,
  initialEnumTypeCols,
} from './constants';
import {
  createTag,
  getEnumNames,
  getEnumValues,
  getFolders,
  getLabel,
} from '@/services/tablemanage';
import { EnumName, EnumValue, FlatTreeNode, LabelAttribute } from '@/types/datapi';
import { isEnumType } from '@/utils/tablemanage';
import { LabelTag } from '@/constants/datapi';

export interface CreateTagProps {}

const CreateTag: FC<CreateTagProps> = ({}) => {
  // 标签类型
  const [tagType, setTagType] = useState<LabelTag>();
  const [folders, setFolders] = useState([]);
  // 标签类型为枚举标签时
  const [enumTypeOps, setEnumTypeOps] = useState([]);
  const [enumTypeCols, setEnumTypeCols] = useState<TableColumnType<{}>[]>(initialEnumTypeCols);
  const [enumTypeData, setEnumTypeData] = useState([]);
  // 标签类型为属性标签时
  const [propEnumOps, setPropEnumOps] = useState([]);
  const [editableKeys, setEditableRowKeys] = useState<Key[]>();
  // submit loading
  const [loading, setLoading] = useState(false);
  // edit mode
  const [form] = Form.useForm();

  const { curFolder, getTree, hideLabel, curLabel, visible } = useModel('tablemanage', (ret) => ({
    curFolder: ret.curFolder,
    getTree: ret.getTree,
    hideLabel: ret.hideLabel,
    curLabel: ret.curLabel,
    visible: ret.visibleLabel,
  }));

  const cloumns: ProColumns[] = [
    { title: '名称', dataIndex: 'attributeKey', key: 'attributeKey', formItemProps: { rules } },
    {
      title: '类型',
      dataIndex: 'attributeType',
      key: 'attributeType',
      formItemProps: { rules },
      renderFormItem: (schema) => (
        <SelectEnum
          onChange={(value) => {
            if (isEnumType(value)) {
              getEnumValues({ enumCode: value })
                .then((res) => {
                  const data = Array.isArray(res.data) ? res.data : [];
                  const ops = data?.map((_: EnumValue) => ({
                    label: _.enumValue,
                    value: _.valueCode,
                  }));
                  propEnumOps[schema.index] = ops;
                  setPropEnumOps([...propEnumOps]);
                })
                .catch((err) => {});
            }
          }}
        />
      ),
    },
    {
      title: '内容',
      dataIndex: 'attributeValue',
      key: 'attributeValue',
      formItemProps: { rules },
      renderFormItem: (schema) => {
        const type = schema.entry.attributeType;
        if (!type) return '-';
        if (type === 'STRING') return <Input placeholder="请输入" />;
        if (type === 'BOOLEAN') return <Radio.Group options={EditorBoolOps} />;
        return <Select placeholder="请选择" options={propEnumOps[schema.index]} />;
      },
    },
    { title: '操作', valueType: 'option', width: 80 },
  ];

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data?.map((_: FlatTreeNode) => ({ label: _.folderName, value: _.id }));
        setFolders(fd);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (curLabel) {
      // 编辑
      getLabelInfo(curLabel);
    } else {
      // 新建, 要给位置赋值, 参阅 EditEnum 的注释
      const folderId = curFolder
        ? curFolder.type === 'FOLDER'
          ? Number(curFolder.folderId)
          : curFolder.parentId
        : null;

      form.setFieldsValue({ folderId });
    }
  }, [curLabel]);

  const getLabelInfo = (labelCode: string) => {
    getLabel({ labelCode })
      .then((res) => {
        const _ = res.data;
        let values = {
          labelName: _.labelName,
          labelTag: _.labelTag,
          subjectType: _.subjectType,
          labelRequired: _.labelRequired,
          labelIndex: _.labelIndex,
          folderId: _.folderId || null,
        };
        if (_.labelTag === 'ENUM_VALUE_LABEL') {
          const enumCode = _.labelParamType.split(':')[0];
          Object.assign(values, { enumCode });
          onChangeLableTag('ENUM_VALUE_LABEL');
          onChangeEnumTypeOps(enumCode);
        }
        if (_.labelTag === 'ATTRIBUTE_LABEL') {
          const labelAttributes = _.labelAttributes?.map((item: LabelAttribute, i: number) => ({
            ...item,
            id: i,
            attributeType: isEnumType(item.attributeType)
              ? item.attributeType.split(':')[0]
              : item.attributeType,
          }));

          Object.assign(values, { labelAttributes });
          setEditableRowKeys(
            labelAttributes?.map((item: LabelAttribute & { id: number }) => item.id),
          );
          onChangeLableTag('ATTRIBUTE_LABEL');
        }
        form.setFieldsValue(values);
      })
      .catch((err) => {});
  };

  // 改变标签类型的时候获取枚举值ops
  const onChangeLableTag = (value: string) => {
    setTagType(value);
    if (value === 'ENUM_VALUE_LABEL') {
      getEnumNames()
        .then((res) => {
          let ops = Array.isArray(res.data) ? res.data : [];
          ops = ops.map((_: EnumName) => ({ label: _.enumName, value: _.enumCode }));
          setEnumTypeOps(ops);
        })
        .catch((err) => {});
    }
  };
  // 改变枚举值的时候修改预览表格
  const onChangeEnumTypeOps = (value: string) => {
    getEnumValues({ enumCode: value })
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        // 处理列
        const cols: TableColumnType<{}>[] = [
          { title: '枚举值', dataIndex: 'enumValue', key: 'enumValue' },
          {
            title: '父级枚举值',
            dataIndex: 'parentValue',
            key: 'parentValue',
            render: (_) => _ || '-',
          },
        ];
        const exCols = Array.isArray(data[0]?.enumAttributes) ? data[0]?.enumAttributes : [];
        exCols.forEach((_: LabelAttribute) => {
          cols.push({ title: _.attributeKey, dataIndex: _.attributeKey, key: _.attributeKey });
        });
        setEnumTypeCols(cols);
        // 处理数据
        const dt = data?.map((_: EnumValue) => {
          const d = { key: _.id, enumValue: _.enumValue, parentValue: _.parentValue };
          const _exCols = Array.isArray(_.enumAttributes) ? _.enumAttributes : [];
          _exCols.forEach((col: LabelAttribute) => (d[col.attributeKey] = col.attributeValue));
          return d;
        });

        setEnumTypeData(dt);
      })
      .catch((err) => {});
  };

  const renderWidth = () => {
    return tagType !== LabelTag.ENUM_VALUE_LABEL && tagType !== LabelTag.ATTRIBUTE_LABEL;
  };

  return (
    <ModalForm
      className={styles.reset}
      title={curLabel ? '编辑标签' : '新建标签'}
      layout="horizontal"
      colon={false}
      labelAlign="left"
      form={form}
      width={renderWidth() ? 560 : 1200}
      visible={visible}
      modalProps={{ onCancel: hideLabel, maskClosable: false, destroyOnClose: true }}
      submitter={{ submitButtonProps: { loading } }}
      onFinish={async (values) => {
        setLoading(true);
        const params = {
          labelParamType: TagToParamMap[values.labelTag],
        };
        for (let [key, value] of Object.entries(values)) {
          if (`${value}`) {
            params[key] = value;
          }
        }
        // 当类型为枚举的时候, labelParamType为选择的enumCode
        if (values.enumCode) {
          params.labelParamType = values.enumCode;
        }
        if (curLabel) {
          Object.assign(params, { labelCode: curLabel });
        }
        createTag(params)
          .then((res) => {
            if (res.success) {
              message.success(curLabel ? '编辑标签成功' : '新建标签成功');
              hideLabel();
              getTree('LABEL');
              getLabelInfo(res.data.labelCode);
            }
          })
          .catch((err) => message.error(curLabel ? '编辑标签失败' : '新建标签失败'))
          .finally(() => setLoading(false));
      }}
    >
      <Row gutter={24}>
        <Col span={renderWidth() ? 24 : 8}>
          <ProFormText name="labelName" label="标签名称" rules={rules} placeholder="请输入名称" />
        </Col>
        <Col span={renderWidth() ? 24 : 8}>
          <ProFormSelect
            name="labelTag"
            label="标签类型"
            rules={rules}
            disabled={!!curLabel}
            fieldProps={{ onChange: onChangeLableTag }}
            valueEnum={LabelTagOps}
          />
        </Col>
        <Col span={renderWidth() ? 24 : 8}>
          <ProFormRadio.Group
            name="subjectType"
            label="标签主体"
            rules={rules}
            disabled={!!curLabel}
            options={SubTypeOps}
            initialValue="TABLE"
          />
        </Col>
      </Row>
      {tagType === 'ENUM_VALUE_LABEL' && [
        <ProFormSelect
          key="enumCode"
          name="enumCode"
          label="枚举值"
          tooltip="如未找到枚举值请先新建枚举类型"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          rules={rules}
          options={enumTypeOps}
          fieldProps={{ onChange: onChangeEnumTypeOps }}
        />,
        <Table
          key="table"
          columns={enumTypeCols}
          dataSource={enumTypeData}
          pagination={false}
          size="small"
          scroll={{ x: 'max-content' }}
          style={{ marginBottom: 24 }}
        />,
      ]}
      {tagType === 'ATTRIBUTE_LABEL' && (
        <ProForm.Item
          className={styles['row-table']}
          label={<Title>属性值</Title>}
          name="labelAttributes"
          trigger="onValuesChange"
        >
          <EditableProTable
            rowKey="id"
            size="small"
            toolBarRender={false}
            columns={cloumns}
            recordCreatorProps={{
              position: 'bottom',
              newRecordType: 'dataSource',
              record: {
                id: Date.now(),
                attributeKey: null,
                attributeType: 'STRING',
                attributeValue: null,
              },
              creatorButtonText: '添加属性',
            }}
            editable={{
              type: 'multiple',
              editableKeys,
              onChange: setEditableRowKeys,
              actionRender: (record, props, dom) => [dom.delete],
            }}
          />
        </ProForm.Item>
      )}
      <ProFormRadio.Group
        name="labelRequired"
        label="是否必填"
        rules={rules}
        initialValue={1}
        options={IsReqOps}
      />
      <Row gutter={24}>
        <Col span={renderWidth() ? 24 : 8}>
          <ProFormText
            name="labelIndex"
            label="排序编号"
            placeholder="请输入"
            tooltip="编号数值小则优先级高"
          />
        </Col>
        <Col span={renderWidth() ? 24 : 8}>
          <ProFormSelect name="folderId" label="位置" placeholder="根目录" options={folders} />
        </Col>
      </Row>
    </ModalForm>
  );
};

export default CreateTag;
