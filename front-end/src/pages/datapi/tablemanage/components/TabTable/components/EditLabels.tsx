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
import { Checkbox, Popover, Tooltip, Typography } from 'antd';
import { useModel } from 'umi';
import type { FormInstance } from 'antd';
import type { FC, ForwardRefRenderFunction } from 'react';
import styles from '../../../index.less';

import { getTableLabels, getDWOwner, getFolders } from '@/services/tablemanage';
import { EnumValue, FlatTreeNode, Table, TableLable, User } from '@/types/tablemanage';
import { rules } from '@/constants/tablemanage';
import { InitialLabel, RadioOps } from '../constants';

import IconFont from '@/components/IconFont';
import Title from '../../../../components/Title';

export interface EditLabelsProps {
  form: FormInstance;
  initial?: Table;
}

const CheckboxGroup = Checkbox.Group;
const { Text } = Typography;
const { require } = rules;

const FormLabel: FC = ({ children }) => {
  return (
    <Tooltip title={children}>
      <Text ellipsis>{children}</Text>
    </Tooltip>
  );
};

const EditLabels: ForwardRefRenderFunction<unknown, EditLabelsProps> = ({ form, initial }, ref) => {
  // 平铺的目录树, 用于表单的位置
  const [folders, setFolders] = useState<any[]>([]);
  // checklist
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');
  const [checkedList, setCheckedList] = useState<string[]>([]); // 齿轮那儿选中的项
  const [allChecked, setAllChecked] = useState(true); // 是否全选
  const [indeterminate, setIndeterminate] = useState(false); // 全选的一个样式开关
  // labels
  const [labels, setLabels] = useState<any[]>([]); // 用以渲染checklist的完整obj
  const labelsMap = useRef(new Map()); // 方便检索做的map
  const labelValues = useRef(new Map()); // 表单的数据（应要求checklist只做显隐, 数据还是全量）

  const { curFolder } = useModel('tabalmanage', (ret) => ({
    curFolder: ret.curFolder,
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
          const ops = [InitialLabel, ...res.data].map((_: TableLable) => {
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
                enums = owners.data.content.map((_: User) => ({
                  label: _.nickname,
                  value: _.id,
                }));
              }
              // 处理枚举类型的ops
              if (_.labelParamType?.endsWith('ENUM')) {
                enums = _.enumValues.map((item: EnumValue) => ({
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
            const initialValue = { tableName: initial.tableName };
            tableLabels.forEach((_: TableLable) => {
              const v = _.labelTag === 'ATTRIBUTE_LABEL' ? [_.labelCode] : _.labelParamValue;
              labelValues.current.set(_.labelCode, v);
              initialValue[_.labelCode] = v;
            });
            form.setFieldsValue(initialValue);
          }
          setCheckedList(list);
          setLabels(ops);
        })
        .catch((err) => []);
    });
    // 获取平铺的目录树
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: FlatTreeNode) => ({ label: _.folderName, value: `${_.id}` }));
        setFolders(fd);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    let folderId = null;
    if (initial) {
      folderId = initial.folderId?.toString();
    } else if (curFolder) {
      folderId = curFolder.type === 'FOLDER' ? curFolder.folderId : curFolder.parentId;
    }
    form.setFieldsValue({ folderId });
  }, [initial, curFolder]);

  // 单选
  const onCheck = (list) => {
    setCheckedList(list);
    setAllChecked(list.length === labels.length);
    setIndeterminate(!!list.length && list.length < labels.length);
  };

  // 全选
  const onAllCheck = (checked: boolean) => {
    const list = checked
      ? labels.map((_) => _.value)
      : labels.filter((_) => _.labelRequired).map((_) => _.value);
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
              label={<FormLabel>{_.labelName}</FormLabel>}
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
              label={<FormLabel>{_.labelName}</FormLabel>}
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
              label={<FormLabel>{_.labelName}</FormLabel>}
              width="sm"
              rules={!!_.labelRequired ? require : []}
              options={_.enums}
              fieldProps={{ onChange: (v) => labelValues.current.set(_.labelCode, v) }}
            />
          );
        case 'ATTRIBUTE_LABEL':
        default:
          return (
            <ProFormCheckbox.Group
              key={_.value}
              name={_.labelCode}
              label={<FormLabel>{_.labelName}</FormLabel>}
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
          overlayClassName={styles['reset-popover']}
          className={styles.popover}
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
              onChange={onCheck}
              style={{ display: 'flex', flexDirection: 'column' }}
            />
          }
        >
          <IconFont type={iconType} style={{ cursor: 'pointer', marginLeft: 8 }} />
        </Popover>
      </Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="inline"
        colon={false}
        form={form}
        submitter={false}
      >
        {renderFormList()}
        <ProFormSelect
          name="folderId"
          label="位置"
          width="md"
          placeholder="根目录"
          options={folders}
        />
      </ProForm>
    </Fragment>
  );
};

export default forwardRef(EditLabels);
