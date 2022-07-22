import { Button, Form, message, Modal, Space } from 'antd';
import { cloneDeep, get, set } from 'lodash';
import type { FC } from 'react';
import { Fragment, useEffect, useRef, useState } from 'react';
import { useModel } from 'umi';

import {
  createTable,
  delTable,
  getTable,
  getTableConstruct,
  postSyncMetabase,
  syncHive,
} from '@/services/datadev';
import { ForeignKey, Table } from '@/types/datapi';

import { IPane } from '@/models/datadev';
import DDLModal from './components/DDLModal';
import SyncHiveFormModal from './components/SyncHiveFormModal';
import EditTable from './EditTable';
import ViewTable from './ViewTable';

export interface TabTableProps {
  pane: IPane;
}
interface EditTableExportProps {
  labels: Map<string, any>;
  stData: any[];
  fkData: ForeignKey[];
  columnsMap: Map<string, any>;
}
const { confirm } = Modal;

const TabTable: FC<TabTableProps> = ({ pane }) => {
  const [mode, setMode] = useState<'view' | 'edit'>(pane.mode);
  const [data, setData] = useState<Table>();
  const [loading, setLoading] = useState<boolean>(false);
  const [metabaseLoading, setMetabaseLoading] = useState<boolean>(false);
  const [ddlModalVisible, setDdlModalVisible] = useState(false);
  const [syncHiveFormModalVisible, setSyncHiveFormModalVisible] = useState(false);

  const [label] = Form.useForm();
  const refs = { label };

  const refTable = useRef<EditTableExportProps>();

  const { getTreeWrapped, onRemovePane, replaceTab } = useModel('datadev', (_) => ({
    replaceTab: _.replaceTab,
    onRemovePane: _.onRemovePane,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    pane.id !== -1 && getTableInfo(pane.id);
  }, [pane]);

  const getTableInfo = (tableId: number) => getTable({ tableId }).then((res) => setData(res.data));

  const createTableParams = (): Table => {
    let folderId: number = -1;
    let tableName: string = '';
    const tableLabels: [] = [];
    const labels = refTable.current?.labels || new Map();
    const stData = refTable.current?.stData || [];
    const colsInfoMap = refTable.current?.columnsMap || new Map();
    const columnInfos = [];
    let foreignKeys = refTable.current?.fkData || [];

    // 检查外键字段与参考字段的长度是否一致
    for (const _ of foreignKeys) {
      if (_.columnNames.length !== _.referColumnNames.length) {
        message.error('字段与参考字段的长度不一致');
        return;
      }
    }
    // 处理tableLabels的入参格式
    for (const [key, value] of labels.entries()) {
      if (!value) {
        continue;
      }
      if (key === 'tableName') {
        tableName = value;
        continue;
      }
      if (key === 'folderId') {
        folderId = value;
        continue;
      }
      const item = { labelCode: key, labelParamValue: value };
      if (Array.isArray(value)) {
        Reflect.deleteProperty(item, 'labelParamValue');
      }
      tableLabels.push(item);
    }
    // 处理columnInfos的入参格式
    for (const [i, _] of stData.entries()) {
      const item = { columnIndex: i, columnName: _.columnName, id: _.id };
      const columnLabels = [];
      for (const [key, value] of Object.entries(_)) {
        if (key === 'key' || key === 'id' || key === 'enableCompare' || key === 'hiveDiff') {
          continue;
        }
        // 检查表结构的必填项
        const tmp = colsInfoMap.get(key);
        if (value === null && tmp.labelRequired === 1) {
          message.error(`表结构-${tmp.labelName}为必填项`);
          return;
        }
        if (key !== 'key' && key !== 'columnName' && key !== 'folderId' && value !== null) {
          columnLabels.push({
            columnName: _.columnName,
            labelCode: key,
            labelParamValue: value,
          });
        }
      }
      Object.assign(item, { columnLabels });
      _.id && Object.assign(item, { id: _.id }); // 当id存在时带上其id表示更新该条数据
      columnInfos[i] = item;
    }
    // 处理foreignKeys的入参格式
    foreignKeys = foreignKeys?.map((_: any) => {
      return {
        ..._,
        columnNames: _.columnNames.join(','),
        referColumnNames: _.referColumnNames.join(','),
      };
    });
    //组装入参
    return {
      folderId,
      tableName,
      tableLabels,
      columnInfos,
      foreignKeys,
    };
  };

  const onSubmit = async () => {
    try {
      await label.validateFields();
      setLoading(true); // 必须在这里set,createTableParams内refTable才能获取最新更新值
      const params = createTableParams();
      // 如果data有值, 本次提交为更新, 增加id字段
      data && Object.assign(params, { id: data.id });
      createTable(params)
        .then((res) => {
          if (res.success) {
            if (pane.id === -1) {
              message.success('创建表成功');
              replaceTab({
                oldKey: 'newTable',
                newKey: `${pane.type}_${pane.belong}_${res.data.id}`,
                title: res.data.tableName,
                pane: { ...pane, id: res.data.id },
              });
            } else {
              message.success('修改表成功');
              replaceTab({ oldKey: pane.cid, newKey: pane.cid, title: res.data.tableName, pane });
              getTableInfo(res.data.id).then(() => setMode('view'));
              getTreeWrapped();
            }
          }
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => {
          // 手动异步解决视图切换延迟触发多次
          setTimeout(() => setLoading(false), 1000);
        });
    } catch(e) {
      setLoading(false);
    }
  };

  const onDelete = () =>
    confirm({
      title: '删除表',
      content: '您确认要删除该表吗？',
      autoFocusButton: null,
      onOk: () =>
        delTable({ tableId: pane.id }).then((res) => {
          if (res.success) message.success('删除成功');
          onRemovePane(pane.cid);
          getTreeWrapped();
        }),
    });

  const onCancel = () => {
    if (pane.id === -1) {
      onRemovePane('newTable');
    } else {
      getTableInfo(pane.id).then(() => setMode('view'));
    }
  };

  const syncMetabase = () => {
    setMetabaseLoading(true);
    postSyncMetabase({ tableId: pane.id })
      .then((res) => {
        if (res.success) {
          message.success('同步成功');
        }
      })
      .finally(() => {
        setMetabaseLoading(false);
      });
  };

  const generateTableConstruct = (value: string) =>
    getTableConstruct({
      tableDdl: value,
      tableId: data?.id as number,
    })
      .then((res) => {
        if (res.success) {
          const tmp = cloneDeep(data as Table);
          const columnInfos = get(res, 'data.columnInfos');
          set(tmp, 'columnInfos', columnInfos);
          setData(tmp);
          setMode('edit');
        }
        return res;
      })

  const onSyncHive = () =>
    syncHive({ tableId: data?.id as number })
      .then((res) => {
        if (res.success) {
          message.success('同步Hive成功');
        } else {
          message.error(`同步Hive失败：${res.msg}`);
        }
      })
      .catch((e) => {
        console.log(e);
      })

    const showSyncHiveFormModal = async () => {
      await label.validateFields();
      setSyncHiveFormModalVisible(true)
    }

  return (
    <Fragment>
      {mode === 'view' && <ViewTable data={data} />}
      {mode === 'edit' && <EditTable ref={refTable} refs={refs} initial={data} />}
      <div className="workbench-submit">
        {mode === 'view' && (
          <Space>
            <Button key="del" size="large" onClick={onDelete}>
              删除
            </Button>
            {/* <Button key="hive" size="large" onClick={onSyncHive}>
              同步Hive
            </Button> */}
            <Button key="edit" size="large" onClick={() => setDdlModalVisible(true)}>
              DDL模式
            </Button>
            <Button key="metabase" size="large" onClick={syncMetabase} loading={metabaseLoading}>
              同步Metabase
            </Button>
            <Button key="edit" size="large" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
          </Space>
        )}
        {mode === 'edit' && (
          <Space>
            <Button key="hive" size="large" onClick={showSyncHiveFormModal}>
              同步Hive表结构
            </Button>
            <Button key="save" size="large" type="primary" onClick={onSubmit} loading={loading}>
              保存
            </Button>
            <Button key="cancel" size="large" onClick={onCancel}>
              取消
            </Button>
          </Space>
        )}
      </div>
      {ddlModalVisible && (
        <DDLModal
          visible={ddlModalVisible}
          onCancel={() => setDdlModalVisible(false)}
          data={data}
          generateTableConstruct={generateTableConstruct}
        />
      )}
      {syncHiveFormModalVisible && (
        <SyncHiveFormModal
          visible={syncHiveFormModalVisible}
          onCancel={() => setSyncHiveFormModalVisible(false)}
          refresh={(newColumnInfos) => setData({...data, columnInfos: newColumnInfos})}
          createTableParams={createTableParams}
          data={data}
        />
      )}
    </Fragment>
  );
};

export default TabTable;
