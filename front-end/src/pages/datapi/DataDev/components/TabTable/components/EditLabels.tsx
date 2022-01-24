import React, {
  Fragment,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
  forwardRef,
} from 'react';
import ProForm, {
  ProFormText,
  ProFormRadio,
  ProFormSelect,
  ProFormCheckbox,
} from '@ant-design/pro-form';
import { Checkbox, Popover } from 'antd';
import { useModel } from 'umi';
import type { FormInstance } from 'antd';
import type { ForwardRefRenderFunction } from 'react';

import { getTableLabels, getDWOwner, getFolders } from '@/services/datadev';
import { EnumValue, Table, TableLable, User } from '@/types/datapi';
import { rules } from '@/constants/datapi';
import { InitialLabel, RadioOps } from '../constants';

import IconFont from '@/components/IconFont';
import Title from '@/components/Title';
import { FolderBelong } from '@/constants/datadev';
import { Folder } from '@/types/datadev';

interface EditLabelsProps {
  form: FormInstance;
  initial?: Table;
}

interface TableLableOptions extends TableLable {
  label: string;
  value: string;
}

const CheckboxGroup = Checkbox.Group;
const { require } = rules;

const EditLabels: ForwardRefRenderFunction<unknown, EditLabelsProps> = ({ form, initial }, ref) => {
  // 平铺的目录树, 用于表单的位置
  const [folders, setFolders] = useState<Folder[]>([]);
  // checklist
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');
  const [checkedList, setCheckedList] = useState<string[]>([]); // 齿轮那儿选中的项
  const [allChecked, setAllChecked] = useState(true); // 是否全选
  const [indeterminate, setIndeterminate] = useState(false); // 全选的一个样式开关
  // labels
  const [labels, setLabels] = useState<TableLableOptions[]>([]); // 用以渲染checklist的完整obj
  const labelsMap = useRef(new Map()); // 方便检索做的map
  const labelValues = useRef(new Map()); // 表单的数据（应要求checklist只做显隐, 数据还是全量）

  const { curNode } = useModel('datadev', (_) => ({
    curNode: _.curNode,
  }));

  useImperativeHandle(ref, () => ({ labels: labelValues.current }));

  useEffect(() => {
    // 获取基本信息的labels
    getTableLabels({ subjectType: 'TABLE' }).then((res) => {
      // 获取数仓管理人
      getDWOwner()
        .then((owners) => {
          const list: string[] = []; // 选中的checkedlist, labelCode[]
          // ops, 用以渲染checklist.group
          const ops = [InitialLabel, ...res.data]
            .sort((a, b) => b.labelRequired - a.labelRequired) // 排序, 必填项在前
            .map((_: TableLable) => {
              const tmp = {
                ..._,
                label: _.labelName,
                value: _.labelCode,
                disabled: _.labelRequired,
              };
              // 只有当 labelTag === "ATTRIBUTE_LABEL" 时不存在 labelParamType
              if (_.labelTag !== 'ATTRIBUTE_LABEL') {
                let enums = [];
                // 处理数仓管理人的ops
                if (_.labelTag === 'USER_LABEL') {
                  enums = owners.data.content?.map((_: User) => ({
                    label: _.nickname,
                    value: `${_.id}`,
                  }));
                }
                // 处理枚举类型的ops
                if (_.labelParamType?.endsWith('ENUM')) {
                  enums = _.enumValues?.map((item: EnumValue) => ({
                    label: item.enumValue,
                    value: item.valueCode,
                  }));
                }
                Object.assign(tmp, { enums });
              }
              labelsMap.current.set(_.labelCode, tmp);
              list.push(_.labelCode);
              return tmp;
            });
          // 如果有初始值, 就进行赋值操作
          if (initial) {
            const tableLabels = initial.tableLabels;
            const initialValue = { tableName: initial.tableName, folderId: initial.folderId };
            labelValues.current.set('tableName', initial.tableName);
            labelValues.current.set('folderId', initial.folderId);
            tableLabels?.forEach((_: TableLable) => {
              const v = _.labelTag === 'ATTRIBUTE_LABEL' ? [_.labelCode] : _.labelParamValue;
              labelValues.current.set(_.labelCode, v);
              initialValue[_.labelCode] = v;
            });
            form.setFieldsValue(initialValue);
          } else {
            if (curNode) {
              const folderId = curNode.id;
              labelValues.current.set('folderId', folderId);
              form.setFieldsValue({ folderId });
            }
          }
          setCheckedList(list);
          setLabels(ops);
        })
        .catch((err) => []);
    });
    // 获取平铺的目录树
    getFolders({ belong: FolderBelong.DESIGNTABLE }).then((res) => setFolders(res.data));
  }, []);

  // 单选
  const onCheck = (list: string[]) => {
    setCheckedList(list);
    setAllChecked(list.length === labels.length);
    setIndeterminate(!!list.length && list.length < labels.length);
  };

  // 全选
  const onAllCheck = (checked: boolean) => {
    const list = checked
      ? labels?.map((_) => _.value)
      : labels?.filter((_) => _.labelRequired).map((_) => _.value);
    setCheckedList(list);
    setAllChecked(checked);
    setIndeterminate(!checked);
  };

  // 渲染checklist的每一项
  const renderFormList = () => {
    return checkedList.map((labelCode) => {
      const _ = labelsMap.current.get(labelCode);
      switch (_.labelTag) {
        case 'STRING_LABEL':
          return (
            <ProFormText
              key={_.value}
              name={_.labelCode}
              label={_.labelName}
              width="sm"
              rules={!!_.labelRequired ? require : []}
              placeholder="请输入"
              fieldProps={{
                onChange: ({ target: { value } }) => labelValues.current.set(_.labelCode, value),
              }}
            />
          );
        case 'BOOLEAN_LABEL':
          return (
            <ProFormRadio.Group
              key={_.value}
              name={_.labelCode}
              label={_.labelName}
              width="sm"
              rules={!!_.labelRequired ? require : []}
              options={RadioOps}
              fieldProps={{
                onChange: ({ target: { value } }) => labelValues.current.set(_.labelCode, value),
              }}
            />
          );
        case 'ENUM_VALUE_LABEL':
        case 'USER_LABEL':
          return (
            <ProFormSelect
              key={_.value}
              name={_.labelCode}
              label={_.labelName}
              width="sm"
              rules={!!_.labelRequired ? require : []}
              options={_.enums}
              fieldProps={{
                onChange: (v) => labelValues.current.set(_.labelCode, v),
                showSearch: true,
                filterOption: (input: string, option: any) => option.label.indexOf(input) >= 0,
              }}
            />
          );
        case 'ATTRIBUTE_LABEL':
        default:
          return (
            <ProFormCheckbox.Group
              key={_.value}
              name={_.labelCode}
              label={_.labelName}
              width="sm"
              rules={!!_.labelRequired ? require : []}
              options={[{ label: null, value: _.labelCode }]}
              fieldProps={{ onChange: (v) => labelValues.current.set(_.labelCode, v) }}
            />
          );
      }
    });
  };

  return (
    <Fragment>
      <Title>
        <span>基本信息</span>
        <Popover
          trigger="click"
          placement="bottomRight"
          onVisibleChange={(v) => setIconType(v ? 'icon-shezhijihuo' : 'icon-shezhi')}
          title={
            <Checkbox
              indeterminate={indeterminate}
              onChange={({ target: { checked } }) => onAllCheck(checked)}
              checked={allChecked}
            >
              表信息展示
            </Checkbox>
          }
          content={
            <CheckboxGroup
              options={labels}
              value={checkedList}
              onChange={(v) => onCheck(v as string[])}
              style={{ display: 'flex', flexDirection: 'column' }}
            />
          }
        >
          <IconFont type={iconType} style={{ cursor: 'pointer', marginLeft: 8 }} />
        </Popover>
      </Title>
      <ProForm layout="inline" colon={false} form={form} submitter={false}>
        {renderFormList()}
        <ProFormSelect
          name="folderId"
          label="位置"
          width="md"
          placeholder="请选择"
          rules={require}
          options={folders.map((_) => ({ label: _.name, value: _.id }))}
          fieldProps={{
            onChange: (v) => labelValues.current.set('folderId', v),
            showSearch: true,
            filterOption: (input: string, option: any) => option.label.indexOf(input) >= 0,
          }}
        />
      </ProForm>
    </Fragment>
  );
};

export default forwardRef(EditLabels);
