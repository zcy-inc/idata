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
import type { FC } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import EditLabels from './components/EditLabels';
import EditColsInfo from './components/EditColsInfo';
import EditForeign from './components/EditForeign';

import { InitialColumn } from './constants';
import { getTableLabels } from '@/services/tablemanage';

export interface EditTableProps {
  refs: { [key: string]: FormInstance };
  initial?: any;
}
export interface LabelsExportProps {
  labels: any;
}
export interface ColumnsExportProps {
  data: any;
}
export interface ForeignExportProps {
  data: any;
}

const CheckboxGroup = Checkbox.Group;
const { TabPane } = Tabs;

const EditTable: FC<EditTableProps> = ({ refs, initial }, ref) => {
  // 表结构可选列设置
  const [checkedList, setCheckedList] = useState<string[]>([]);
  const [allChecked, setAllChecked] = useState(true);
  const [indeterminate, setIndeterminate] = useState(false);
  const [iconType, setIconType] = useState<'icon-shezhi' | 'icon-shezhijihuo'>('icon-shezhi');
  // 表结构的列与检索表
  const [columns, setColumns] = useState<any[]>([]);
  const columnsMap = useRef(new Map()); // 检索用的
  // 外键
  const [strOps, setStrOps] = useState<any[]>([]);

  const refLabel = useRef<LabelsExportProps>();
  const refColumns = useRef<ColumnsExportProps>();
  const refForeign = useRef<ForeignExportProps>();

  useImperativeHandle(ref, () => ({
    labels: refLabel.current?.labels,
    stData: refColumns.current?.data,
    fkData: refForeign.current?.data,
  }));

  useEffect(() => {
    getTableLabels({ subjectType: 'COLUMN' }).then((res) => {
      // const map = new Map(); // 检索用的map
      const list: string[] = []; // checked list
      // check.group 用的ops
      const ops = [InitialColumn, ...res.data]
        .filter((_: any) => _.labelTag !== 'ATTRIBUTE_LABEL')
        .map((_: any) => {
          const tmp = { ..._, label: _.labelName, value: _.labelCode, disabled: _.labelRequired };
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
        defaultActiveKey="struct"
        onChange={(key) => {
          if (key === 'fk') {
            // 切换到外键的时候，保存表结构字段，生成Select的ops
            const dataIndex = columns[0].labelCode;
            const stData = refColumns.current?.data || [];
            const ops = stData.map((_: any) => ({ label: _[dataIndex], value: _[dataIndex] }));
            setStrOps(ops);
          }
        }}
      >
        <TabPane
          key="struct"
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
            _props={{
              checkedList,
              columns,
              columnsMap: columnsMap.current,
            }}
          />
        </TabPane>
        <TabPane tab="关系" key="fk">
          <EditForeign ref={refForeign} initial={initial} _props={{ strOps }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditTable);
