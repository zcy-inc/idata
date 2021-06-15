import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Form, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../index.less';

import ViewTable from './ViewTable';
import EditTable from './EditTable';
import { createTable, delTable, getTable } from '@/services/tablemanage';

export interface TabTableProps {
  initialMode: 'view' | 'edit';
  fileCode: string;
}
export interface EditTableExportProps {
  structData: any;
  fkData: any;
}

const TabTable: FC<TabTableProps> = ({ initialMode = 'view', fileCode }) => {
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [loading, setLoading] = useState<boolean>(false);
  const [data, setData] = useState<any>();

  const [label] = Form.useForm();
  const refs = { label };

  const refTable = useRef<EditTableExportProps>();

  const { getTree, onRemovePane } = useModel('tabalmanage', (ret) => ({
    getTree: ret.getTree,
    onRemovePane: ret.onRemovePane,
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
        const labels = label.getFieldsValue();
        let folderId = null;
        let tableName = '';
        let tableLabels = [];
        let columnInfos = refTable?.current?.structData || [];
        let foreignKeys = refTable?.current?.fkData || [];
        for (let _ of foreignKeys) {
          if (_.columnNames.length !== _.referColumnNames.length) {
            message.error('字段与参考字段的长度不一致');
            return;
          }
        }
        console.log({ labels, columnInfos, foreignKeys });
        for (let key of Object.keys(labels)) {
          if (labels[key]) {
            if (key === 'tableName') {
              tableName = labels[key];
            } else if (key === 'folderId') {
              folderId = labels[key];
            } else {
              if (Array.isArray(labels[key])) {
                tableLabels.push({ labelCode: key });
              } else {
                tableLabels.push({ labelCode: key, labelParamValue: labels[key] });
              }
            }
          }
        }
        const params = {
          folderId,
          tableName,
          tableLabels,
          columnInfos: columnInfos.map((_: any, i: number) => {
            const columnLabels = [];
            for (let key of Object.keys(_)) {
              if (key !== 'id' && key !== 'columnName' && key !== 'folderId' && _[key] !== null) {
                columnLabels.push({
                  columnName: _.columnName,
                  labelCode: key,
                  labelParamValue: _[key],
                });
              }
            }
            return { id: _.id, columnIndex: i, columnName: _.columnName, columnLabels };
          }),
          foreignKeys: foreignKeys.map((_: any) => {
            return {
              id: _.id,
              columnNames: _.columnNames.join(','),
              referDbName: _.referDbName,
              referTableId: _.referTableId,
              referColumnNames: _.referColumnNames.join(','),
              erType: _.erType,
            };
          }),
        };
        if (data) {
          Object.assign(params, { id: data.id });
        }
        createTable(params)
          .then((res) => {
            if (res.success) {
              message.success(fileCode === 'newTable' ? '创建表成功' : '修改表成功');
              setMode('view');
              getTableInfo(res.data.id);
              getTree('TABLE');
            }
          })
          .catch((err) => {})
          .finally(() => setLoading(false));
      })
      .finally(() => setLoading(false));
  };

  const onDelete = () => {
    setLoading(true);
    delTable({ tableId: fileCode })
      .then((res) => {
        if (res.success) message.success('删除成功');
        onRemovePane(`T_${fileCode}`);
        getTree('TABLE');
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  return (
    <Fragment>
      {mode === 'view' && <ViewTable data={data} />}
      {mode === 'edit' && (
        <EditTable
          ref={refTable}
          refs={refs}
          initial={fileCode === 'newTable' ? undefined : data}
        />
      )}
      <div className={styles.submit}>
        {mode === 'view' && [
          <Button key="edit" type="primary" onClick={() => setMode('edit')}>
            编辑
          </Button>,
          <Popconfirm
            key="del"
            title="您确认要删除该枚举吗？"
            onConfirm={() => onDelete()}
            okButtonProps={{ loading }}
            okText="确认"
            cancelText="取消"
          >
            <Button>删除</Button>
          </Popconfirm>,
        ]}
        {mode === 'edit' && [
          <Button key="save" type="primary" onClick={onSubmit} loading={loading}>
            保存
          </Button>,
          <Button
            key="cancel"
            onClick={() => {
              if (fileCode === 'newTable') {
                onRemovePane('newTable');
              } else {
                setMode('view');
                getTableInfo(fileCode);
              }
            }}
          >
            取消
          </Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabTable;
