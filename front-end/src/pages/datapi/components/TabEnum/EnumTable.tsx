import React, { Fragment, useEffect, useState } from 'react';
import { Input, Radio, Select, Typography } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';
import SelectType from '../SelectType';
import { getRandomStr } from '@/utils/utils';
import { getEnumNames, getEnumValues } from '@/services/tablemanage';

export interface EnumTableProps {
  initial?: { data: any; columns: any[]; dataSource: any[] };
  onChange?: (value: any) => void;
}

const { Link } = Typography;
const RadioOps = [
  { label: 'True', value: 'true' },
  { label: 'False', value: 'false' },
];

const EnumTable: FC<EnumTableProps> = ({ initial, onChange }) => {
  const [columns, setColumns] = useState<any[]>([]); // {id, title, type}
  const [enums, setEnums] = useState<any[]>([]); // {[title]: value}
  const [parentOps, setParentOps] = useState<any[]>([]);

  useEffect(() => {
    if (initial) {
      const cols = [...initial?.columns] || [];
      cols.splice(0, 2); // 去掉前两列[枚举值, 父级枚举值]
      // 对参数类型是枚举的列, 需要获取其code对应的names
      const promises: Promise<any>[] = [];
      cols.forEach((_: any, i: number) => {
        _.type.length === 10 && (promises[i] = getEnumNames({ enumCode: _.type }));
        cols[i].dataIndex = _.dataIndex[0];
      });
      Promise.all([...promises]).then((res) => {
        res.forEach(
          (_: any, i: number) =>
            _ &&
            (cols[i].enumValues = _.data.map((_enum: any) => ({
              label: _enum.enumValue,
              value: _enum.valueCode,
            }))),
        );

        // TODO 更新时自定义列未更新
        const dt = initial?.dataSource.map((_: any) => {
          for (let [key, value] of Object.entries(_)) {
            if (key !== 'enumValue' && key !== 'parentValue') {
              _[key] = value.code;
            }
          }
          return _;
        });

        setColumns(cols);
        setEnums(dt);
        onChange?.({ columns: cols, enums: dt });
      });
    }
  }, []);

  // 触发作为表单组件onChange事件
  const triggerChange = () => {
    onChange?.({ columns, enums });
  };
  // 添加一列
  const addColumn = () => {
    setColumns([...columns, { id: Date.now(), title: '', type: 'STRING' }]);
    triggerChange();
  };
  // 删除一列
  const delColunm = (i: number) => {
    columns.splice(i, 1);
    setColumns([...columns]);
    triggerChange();
  };
  // 添加一行
  const addEnum = () => {
    setEnums([...enums, { id: Date.now(), enumValue: {} }]);
    triggerChange();
  };
  // 删除一行
  const delEnum = (i: number) => {
    enums.splice(i, 1);
    parentOps.splice(i, 1);
    setEnums([...enums]);
    setParentOps([...parentOps]);
    triggerChange();
  };
  // 修改行数据的枚举值列, 新增下需要生成10位code
  const onChangeEnumValue = (v: any, i: number) => {
    if (!enums[i].enumValue.code) {
      const unicode = getRandomStr(10);
      enums[i].enumValue = { code: unicode };
      parentOps[i] = { value: unicode };
    }
    enums[i].enumValue.value = v;
    parentOps[i].label = v;
    setEnums([...enums]);
    setParentOps([...parentOps]);
    triggerChange();
  };
  // 修改父类枚举值
  const onChangeParentCode = (v: any, i: number) => {
    enums[i].parentCode = v;
    setEnums([...enums]);
    triggerChange();
  };
  // 修改额外参数列的值
  const onChangeEnumParam = (v: any, i: number, colI: number) => {
    enums[i][columns[colI].title] = v;
    setEnums([...enums]);
    triggerChange();
  };
  // 生成父级枚举值的ops
  // 找出 parentOps 中与第 i 项枚举值的code相等的项, disable之
  const renderParentOps = (i: number) =>
    parentOps.map((_) => {
      return { ..._, disabled: _.value === enums[i].enumValue.code };
    });
  // 置空父级枚举值
  const onClearParentCode = (i: number) => {
    enums[i].parentCode = null;
    setEnums([...enums]);
  };

  return (
    <Fragment>
      <div className={styles['enum-table']}>
        {/* title */}
        <div className={styles['columns']}>
          {/* 枚举值 */}
          <div className={styles.cell}>
            <div className={styles.title}>枚举值</div>
            <div className={styles.type}>
              <SelectType style={{ width: 150 }} disabled />
            </div>
          </div>
          {/* 父级枚举值 */}
          <div className={styles.cell}>
            <div className={styles.title}>父级枚举值</div>
            <div className={styles.type}>
              <SelectType style={{ width: 150 }} disabled />
            </div>
          </div>
          {/* 额外的参数列 */}
          {columns.map((_, i) => (
            <div key={_.id || _.title} className={styles.cell}>
              <div className={styles.title}>
                <Input
                  style={{ width: 120, marginRight: 16 }}
                  placeholder="请输入"
                  value={columns[i].title}
                  onChange={({ target: { value } }) => {
                    columns[i].title = value;
                    setColumns([...columns]);
                  }}
                />
                <Link onClick={() => delColunm(i)}>
                  <IconFont type="icon-shanchuchanggui" style={{ color: 'red' }} />
                </Link>
              </div>
              <div className={styles.type}>
                <SelectType
                  style={{ width: 150 }}
                  value={_.type}
                  onChange={(type) => {
                    console.log(type);

                    columns[i].type = type;
                    if (type.length === 10) {
                      getEnumValues({ enumCode: type })
                        .then((res) => {
                          columns[i].enumValues = res.data.map((_: any) => ({
                            label: _.enumValue,
                            value: _.valueCode,
                          }));
                          setColumns([...columns]);
                        })
                        .catch((err) => {});
                    } else {
                      setColumns([...columns]);
                    }
                  }}
                />
              </div>
            </div>
          ))}
          {/* 操作：添加参数 */}
          <div className={styles['cell-ops']} onClick={addColumn}>
            <Link>
              <PlusCircleOutlined />
              添加参数
            </Link>
          </div>
        </div>
        {/* dataSource */}
        {enums.map((_, i) => (
          <div key={_.id} className={styles.enum}>
            {/* 枚举值 */}
            <div className={styles['enum-cell']}>
              <Input
                placeholder="请输入"
                value={enums[i].enumValue.value}
                onChange={({ target: { value } }) => onChangeEnumValue(value, i)}
              />
            </div>
            {/* 父级枚举值 */}
            <div className={styles['enum-cell']}>
              <Select
                allowClear
                placeholder="请选择"
                value={enums[i].parentCode}
                options={renderParentOps(i)}
                onSelect={(value) => onChangeParentCode(value, i)}
                onClear={() => onClearParentCode(i)}
              />
            </div>
            {/* 额外的参数列 */}
            {columns.map((col, colI) => (
              <div key={col.id} className={styles['enum-cell']}>
                {col.type === 'STRING' ? (
                  <Input
                    placeholder="请输入"
                    value={enums[i][columns[colI].title]}
                    onChange={({ target: { value } }) => onChangeEnumParam(value, i, colI)}
                  />
                ) : col.type === 'BOOLEAN' ? (
                  <Radio.Group
                    options={RadioOps}
                    value={enums[i][columns[colI].title]}
                    onChange={({ target: { value } }) => onChangeEnumParam(value, i, colI)}
                  />
                ) : (
                  <Select
                    placeholder="请选择"
                    options={columns[colI].enumValues}
                    value={enums[i][columns[colI].title]}
                    onChange={(value) => onChangeEnumParam(value, i, colI)}
                  />
                )}
              </div>
            ))}
            {/* 操作：删除当前行 */}
            <div className={styles['enum-cell-ops']} onClick={() => delEnum(i)}>
              <Link>
                <IconFont type="icon-shanchuchanggui" />
              </Link>
            </div>
          </div>
        ))}
      </div>
      {/* 操作：添加 dataSource */}
      <Link className={styles['plus-enum']} onClick={addEnum}>
        <PlusCircleOutlined />
        添加枚举值
      </Link>
    </Fragment>
  );
};

export default EnumTable;
