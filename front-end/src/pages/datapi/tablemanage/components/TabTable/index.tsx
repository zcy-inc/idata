import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Form, message, Modal, Space } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import { createTable, delTable, getTable } from '@/services/tablemanage';
import { Table, ForeignKey } from '@/types/datapi';
import ViewTable from './ViewTable';
import EditTable from './EditTable';

export interface TabTableProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
interface EditTableExportProps {
  labels: Map<string, any>;
  stData: any[];
  fkData: ForeignKey[];
  columnsMap: Map<string, any>;
}
const { confirm } = Modal;

const TabTable: FC<TabTableProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [data, setData] = useState<Table>();
  const [loading, setLoading] = useState<boolean>(false);

  const [label] = Form.useForm();
  const refs = { label };

  const refTable = useRef<EditTableExportProps>();

  const { getTree, onRemovePane, replaceTab } = useModel('tablemanage', (ret) => ({
    getTree: ret.getTree,
    onRemovePane: ret.onRemovePane,
    replaceTab: ret.replaceTab,
  }));

  useEffect(() => {
    setMode(initialMode);
    fileCode !== 'newTable' && getTableInfo(fileCode);
  }, []);

  const getTableInfo = (tableId: string) => {
    getTable({ tableId })
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  };

  const onSubmit = () => {
    setLoading(true);
    label
      .validateFields()
      .then(() => {
        let folderId = null;
        let tableName = '';
        let tableLabels = [];
        let labels = refTable.current?.labels || new Map();
        let stData = refTable.current?.stData || [];
        let colsInfoMap = refTable.current?.columnsMap || new Map();
        let columnInfos = [];
        let foreignKeys = refTable.current?.fkData || [];
        let params = {};
        // 检查外键字段与参考字段的长度是否一致
        for (let _ of foreignKeys) {
          if (_.columnNames.length !== _.referColumnNames.length) {
            message.error('字段与参考字段的长度不一致');
            return;
          }
        }
        // 处理tableLabels的入参格式
        for (let [key, value] of labels.entries()) {
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
        for (let [i, _] of stData.entries()) {
          const item = { columnIndex: i, columnName: _.columnName };
          const columnLabels = [];
          for (let [key, value] of Object.entries(_)) {
            if (key === 'key' || key === 'id') {
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
        foreignKeys = foreignKeys.map((_: any) => {
          return {
            ..._,
            columnNames: _.columnNames.join(','),
            referColumnNames: _.referColumnNames.join(','),
          };
        });
        //组装入参
        params = {
          folderId,
          tableName,
          tableLabels,
          columnInfos,
          foreignKeys,
        };
        // 如果data有值, 本次提交为更新, 增加id字段
        data && Object.assign(params, { id: data.id });
        createTable(params)
          .then((res) => {
            if (res.success) {
              if (fileCode === 'newTable') {
                message.success('创建表成功');
                replaceTab('newTable', `T_${res.data.id}`, res.data.tableName, 'TABLE');
              } else {
                message.success('修改表成功');
                replaceTab(`T_${res.data.id}`, `T_${res.data.id}`, res.data.tableName, 'TABLE');
                getTree('TABLE');
                getTableInfo(res.data.id);
                setMode('view');
              }
            }
          })
          .catch((err) => {})
          .finally(() => setLoading(false));
      })
      .finally(() => setLoading(false));
  };

  const onDelete = () =>
    confirm({
      title: '删除枚举',
      content: '您确认要删除该枚举吗？',
      onOk: () =>
        delTable({ tableId: fileCode })
          .then((res) => {
            if (res.success) message.success('删除成功');
            onRemovePane(`T_${fileCode}`);
            getTree('TABLE');
          })
          .catch((err) => {}),
    });

  const onCancel = () => {
    if (fileCode === 'newTable') {
      onRemovePane('newTable');
    } else {
      setMode('view');
      getTableInfo(fileCode);
    }
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewTable data={data} />}
      {mode === 'edit' && <EditTable ref={refTable} refs={refs} initial={data} />}
      <div className={styles.submit}>
        {mode === 'view' && (
          <Space>
            <Button key="edit" type="primary" onClick={() => setMode('edit')}>
              编辑
            </Button>
            <Button key="del" onClick={onDelete}>
              删除
            </Button>
          </Space>
        )}
        {mode === 'edit' && (
          <Space>
            <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
              保存
            </Button>
            <Button key="cancel" onClick={onCancel}>
              取消
            </Button>
          </Space>
        )}
      </div>
    </Fragment>
  );
};

export default TabTable;
