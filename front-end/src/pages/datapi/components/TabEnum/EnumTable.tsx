import React, { Fragment, useEffect, useState } from 'react';
import { Input, Radio, Select, Typography } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';
import SelectType from '../SelectType';
import { getRandomStr } from '@/utils/utils';
import { getEnumNames, getEnumValues } from '@/services/tablemanage';
import { isEnumType } from '../../utils';

export interface EnumTableProps {
  initial?: any;
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
  const [isShadowL, setIsShadowL] = useState(false);
  const [isShadowR, setIsShadowR] = useState(false);

  useEffect(() => {
    if (initial) {
      const enumValues = Array.isArray(initial.enumValues) ? initial.enumValues : []; // 传入的datsSource
      const attrs = Array.isArray(enumValues[0]?.enumAttributes)
        ? enumValues[0]?.enumAttributes
        : []; // datsSource第一项的enumAttributes, 用以生成自定义参数的列
      const pOps: any[] = []; // 存储父级枚举值
      const promises: Promise<any>[] = []; // // 对参数类型是枚举的列, 需要使用getEnumNames获取列表
      // 生成自定义参数的列
      const exCols = attrs.map((_: any, i: number) => {
        let type = _.attributeType;
        if (isEnumType(_.attributeType)) {
          type = _.attributeType.split(':')[0];
          promises[i] = getEnumNames({ enumCode: type });
        }
        return { id: i, title: _.attributeKey, type };
      });
      // 格式化datsSource
      const dt = enumValues.map((_: any, i: number) => {
        const tmp = {
          enumValue: { code: _.valueCode, value: _.enumValue },
          parentCode: _.parentCode,
        };
        _.enumAttributes.forEach((attr: any) => (tmp[attr.attributeKey] = attr.attributeValue));
        pOps[i] = { label: _.enumValue, value: _.valueCode };

        return tmp;
      });
      Promise.all(promises).then((res) => {
        res.forEach(
          (_: any, i: number) =>
            _ &&
            (exCols[i].enumValues = _.data.map((_enum: any) => ({
              label: _enum.enumValue,
              value: _enum.valueCode,
            }))),
        );
        setColumns(exCols);
        setEnums(dt);
        setParentOps(pOps);
        onChange?.({ columns: exCols, enums: dt });
      });
    }
  }, [initial]);

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
    parentOps.map((_) => ({ ..._, disabled: _.value === enums[i].enumValue.code }));
  // 置空父级枚举值
  const onClearParentCode = (i: number) => {
    enums[i].parentCode = null;
    setEnums([...enums]);
  };
  // shadow
  const setShadow = (t: any) => {
    // 左侧的阴影
    if (t.scrollLeft > 0) {
      !isShadowL && setIsShadowL(true);
    } else {
      isShadowL && setIsShadowL(false);
    }
    // 右侧的阴影
    if (t.scrollLeft + t.clientWidth < t.scrollWidth) {
      !isShadowR && setIsShadowR(true);
    } else {
      isShadowR && setIsShadowR(false);
    }
  };

  return (
    <Fragment>
      <div className={styles['enum-table-shadowbox']}>
        <div
          className={`${styles['enum-table']} ${isShadowL && styles['enum-table-left-shadow']} ${
            isShadowR && styles['enum-table-right-shadow']
          }`}
          onScroll={({ target }) => setShadow(target)}
        >
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
                      columns[i].type = type;
                      if (isEnumType(type)) {
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
            <div key={_.id || i} className={styles.enum}>
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
