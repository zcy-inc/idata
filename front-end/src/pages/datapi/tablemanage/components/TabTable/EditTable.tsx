import React, {
  Fragment,
  useEffect,
  useRef,
  useState,
  useImperativeHandle,
  forwardRef,
} from 'react';
import { Checkbox, Popover, Tabs } from 'antd';
import type { FormInstance } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../index.less';

import { getDWOwner, getTableLabels } from '@/services/tablemanage';
import { Table, ForeignKey, TableLable } from '@/types/datapi';
import { InitialColumn } from './constants';

import IconFont from '@/components/IconFont';
import EditLabels from './components/EditLabels';
import EditColsInfo from './components/EditColsInfo';
import EditForeign from './components/EditForeign';

interface EditTableProps {
  refs: { [key: string]: FormInstance };
  initial?: Table;
}
interface LabelsExportProps {
  labels: Map<string, any>;
}
interface ColumnsExportProps {
  data: { [key: string]: any }[];
}
interface ForeignExportProps {
  data: ForeignKey[];
}
interface TableLableOption extends TableLable {
  label: string;
  value: string;
}

const CheckboxGroup = Checkbox.Group;
const { TabPane } = Tabs;

const EditTable: ForwardRefRenderFunction<unknown, EditTableProps> = ({ refs, initial }, ref) => {
  // 表结构可选列设置
  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [allChecked, setAllChecked] = useState(true);
  const [indeterminate, setIndeterminate] = useState(false);
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');
  // 表结构的列与检索表
  const [columns, setColumns] = useState<TableLableOption[]>([]);
  const columnsMap = useRef(new Map()); // 检索用的
  // 外键
  const [strOps, setStrOps] = useState<TableLableOption[]>([]);

  const refLabel = useRef<LabelsExportProps>();
  const refColumns = useRef<ColumnsExportProps>();
  const refForeign = useRef<ForeignExportProps>();

  useImperativeHandle(ref, () => ({
    labels: refLabel.current?.labels,
    stData: refColumns.current?.data,
    fkData: refForeign.current?.data,
    columnsMap: columnsMap.current,
  }));

  useEffect(() => {
    getTableLabels({ subjectType: 'COLUMN' }).then((res) => {
      // 这里和TableLabels一样调用了getDWOwner(), 要做上级数据管理吗?
      getDWOwner()
        .then((owners) => {
          // const map = new Map(); // 检索用的map
          const list: string[] = []; // checked list
          // check.group 用的ops
          const ops = [InitialColumn, ...res.data]
            .filter((_: any) => _.labelTag !== 'ATTRIBUTE_LABEL')
            .map((_: any) => {
              const tmp = {
                ..._,
                label: _.labelName,
                value: _.labelCode,
                disabled: _.labelRequired,
              };
              if (_.labelTag === 'USER_LABEL') {
                tmp.enums = owners.data.content.map((_: any) => ({
                  label: _.nickname,
                  value: _.id,
                }));
              }
              if (_.labelTag === 'ENUM_VALUE_LABEL') {
                tmp.enums = _.enumValues.map((item: any) => ({
                  label: item.enumValue,
                  value: item.valueCode,
                }));
              }
              columnsMap.current.set(_.labelCode, tmp);
              list.push(_.labelCode);
              return tmp;
            });
          setCheckedList(list);
          setColumns(ops);
        })
        .catch((err) => {});
    });
  }, []);

  // 单选
  const onCheck = (list: any[]) => {
    setCheckedList(list);
    setAllChecked(list.length === columns.length);
    setIndeterminate(!!list.length && list.length < columns.length);
  };
  // 全选
  const onAllCheck = (checked: boolean) => {
    const list = checked
      ? columns.map((_) => _.value)
      : columns.filter((_) => _.labelRequired).map((_) => _.value);
    setCheckedList(list);
    setAllChecked(checked);
    setIndeterminate(!checked);
  };

  return (
    <Fragment>
      <EditLabels ref={refLabel} form={refs.label} initial={initial} />
      <Tabs
        className={`${styles.reset} ${styles['reset-tabs']}`}
        defaultActiveKey="columnInfos"
        onChange={(key) => {
          if (key === 'foreignKey') {
            // 切换到外键的时候，保存表结构字段，生成Select的ops
            const dataIndex = columns[0].labelCode;
            const stData = refColumns.current?.data || [];
            const ops = stData.map((_: any) => ({
              ..._,
              label: _[dataIndex],
              value: _[dataIndex],
            }));
            setStrOps(ops);
          }
        }}
      >
        <TabPane
          key="columnInfos"
          tab={[
            '表结构设计',
            <Popover
              overlayClassName={styles['reset-popover']}
              key="setting"
              trigger="click"
              placement="bottomRight"
              onVisibleChange={(v) => setIconType(v ? 'icon-shezhijihuo' : 'icon-shezhi')}
              title={
                <Checkbox
                  indeterminate={indeterminate}
                  onChange={({ target: { checked } }) => onAllCheck(checked)}
                  checked={allChecked}
                >
                  字段信息展示
                </Checkbox>
              }
              content={
                <CheckboxGroup
                  options={columns}
                  value={checkedList}
                  onChange={(checked) => onCheck(checked)}
                  style={{ display: 'flex', flexDirection: 'column' }}
                />
              }
            >
              <IconFont type={iconType} style={{ cursor: 'pointer', marginLeft: 8 }} />
            </Popover>,
          ]}
        >
          <EditColsInfo
            ref={refColumns}
            initial={initial}
            colProps={{
              checkedList,
              columns,
              columnsMap: columnsMap.current,
            }}
          />
        </TabPane>
        <TabPane tab="关系" key="foreignKey">
          <EditForeign ref={refForeign} initial={initial} _props={{ strOps }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditTable);
