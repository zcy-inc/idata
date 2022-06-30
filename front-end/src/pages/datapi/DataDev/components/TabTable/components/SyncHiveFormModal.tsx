import { Button, Modal, Table as AntdTable, Tag } from 'antd';
import type { ColumnsType } from 'antd/lib/table/Table';
import { useEffect, useState } from 'react';

import type { FC } from 'react';

import { hiveTableChange, compareHiveChange } from '@/services/datadev';
import type { HivechangeContentInfo, Table as HiveTable } from '@/types/datapi';

interface SyncHiveFormModalProps {
  visible: boolean;
  onCancel: () => void;
  refresh: () => void;
  createTableParams: () => HiveTable;
  data: HiveTable;
}

const HivechangeTypeString = {
  1: { text: '字段新增', color: 'green' },
  2: { text: '字段删除', color: 'magenta' },
  3: { text: '字段修改', color: 'orange' },
};

let params: HiveTable;

const columns: ColumnsType<HivechangeContentInfo> = [
  {
    title: '更新前字段名称',
    dataIndex: 'columnNameBefore',
    key: 'columnNameBefore',
  },
  {
    title: '更新内容',
    dataIndex: 'changeDescription',
    key: 'changeDescription',
    render: (_, record) => (
      <>
        <Tag color={HivechangeTypeString?.[record?.changeType]?.color ?? ''}>
          {HivechangeTypeString?.[record?.changeType]?.text ?? '-'}
        </Tag>
        <pre  dangerouslySetInnerHTML={{ __html: record.changeDescription }}/>
      </>
    ),
  },
  {
    title: '更新后字段名称',
    dataIndex: 'columnNameAfter',
    key: 'columnNameAfter',
  },
];

const SyncHiveFormModal: FC<SyncHiveFormModalProps> = ({
  visible,
  onCancel,
  createTableParams,
  data,
  refresh,
}) => {
  const [dataSource, setDataSource] = useState<HivechangeContentInfo[]>([]);
  const [tableLoading, setTableLoading] = useState<boolean>(false);
  const [btnLoading, setBtnLoading] = useState<boolean>(false);

  useEffect(() => {
    if (data) {
      setTableLoading(true);
      params = createTableParams()
      compareHiveChange({...params, id: data.id })
        .then((res) => {
          setDataSource(res?.data?.changeContentInfoList ?? []);
        })
        .catch((e) => { })
        .finally(() => setTableLoading(false));
    }
  }, [visible]);

  return (
    <Modal
      bodyStyle={{height: '75VH', overflowY: 'auto', padding: 16}}
      title="同步Hive表结构"
      visible={visible}
      onCancel={onCancel}
      width={800}
      footer={[
        <Button key="hive_cancelBtn" size="large" onClick={onCancel}>
          取消
        </Button>,
        <Button
          key="hive_createBtb"
          size="large"
          type="primary"
          loading={btnLoading}
          onClick={() =>{
            setBtnLoading(true);
            hiveTableChange({...params, id: data.id }).then((res) => {
              if (res.success) {
                onCancel();
                refresh();
              }
            }).finally(() => setBtnLoading(false));
          }
        }
        >
          确认覆盖表结构
        </Button>,
      ]}
    >
      <p>hive数据源与平台内的表结构经对比存在以下差异：</p>
      <AntdTable<HivechangeContentInfo>
        loading={tableLoading}
        dataSource={dataSource}
        columns={columns}
        pagination={false}
      />
    </Modal>
  );
};

export default SyncHiveFormModal;
