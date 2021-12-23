import React, { useEffect, useRef, useState } from 'react';
import { Table } from 'antd';
import type { TableProps } from 'antd/es/table';

import styles from './index.less';

const bodyHeightVarName = '--tbl-body-height';
const paginationHeight = 64;
const bottomPaddingHeight = 16;
const tableHeaderHeight = 47;

interface TiledTableProps<RecordType> extends TableProps<RecordType> {
  height?: number;
  leftBtns?: React.ReactNode;
  rightBtns?: React.ReactNode;
}
/**
 * 指定高度时使用指定的高度，不指定高度时铺满屏幕剩余高度
 * 组件使用场景：自动根据当前节点位置距视框上边距铺满首屏，只在内容区域高度大于100时生效, 指定scroll时不生效
 * @param props
 */
function TiledTable<RecordType extends Record<string, any> = any>(
  props: TiledTableProps<RecordType>,
) {
  const { height, leftBtns, rightBtns, ...tableProps } = props;
  const wrapperRef = useRef<HTMLDivElement>(null);
  const topMarkRef = useRef<HTMLSpanElement>(null);
  const [info, setInfo] = useState<{ show: boolean; y?: number }>();

  useEffect(() => {
    const newInfo = { show: true, y: undefined };
    // 无数据时不显示分页，不需要减去分页高度
    // const tblRestHeight =
    //   props.dataSource?.length && props.dataSource?.length > 0
    //     ? tableHeaderHeight + paginationHeight
    //     : tableHeaderHeight;
    // if (typeof height === 'number') {
    //   const bodyHeight = height - tblRestHeight;
    //   Object.assign(newInfo, { y: bodyHeight });
    //   wrapperRef.current?.style.setProperty(bodyHeightVarName, `${bodyHeight}px`); // 设置body高度
    // } else {
    //   const rect = topMarkRef.current!.getBoundingClientRect();
    //   const { clientHeight } = document.body;
    //   const bodyHeight = clientHeight - rect.top - bottomPaddingHeight - tblRestHeight;
    //   // 表格内容区最小高度100
    //   if (bodyHeight > 100) {
    //     Object.assign(newInfo, { y: bodyHeight });
    //     wrapperRef.current?.style.setProperty(bodyHeightVarName, `${bodyHeight}px`); // 设置body高度
    //   }
    // }
    // const zcyContent = document.querySelector('.zcy-content');
    // const zcyContentW = zcyContent?.clientWidth - 48;
    // Object.assign(newInfo, { x: zcyContentW });
    setInfo(newInfo);
  }, [height, props.dataSource?.length]);

  return (
    <div className={styles.tiledTblWrapper} ref={wrapperRef}>
      <div className={styles.btnWrapper}>
        <div>{leftBtns}</div>
        <div>{rightBtns}</div>
      </div>
      <span ref={topMarkRef} />
      {/* {info?.show && <Table {...tableProps} scroll={{ y: info.y }} />} */}
      {info?.show && <Table {...tableProps} scroll={{ x: 'max-content' }} />}
    </div>
  );
}

export default TiledTable;
