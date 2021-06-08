import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Form, message, Popconfirm } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../tablemanage/index.less';

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
  const [column] = Form.useForm();
  const [FK] = Form.useForm();

  const refs = { label, column, FK };
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
    label.validateFields().then(() => {
      column.validateFields().then(() => {
        FK.validateFields().then(() => {
          const labels = label.getFieldsValue();
          let tableName = '';
          let tableLabels = [];
          let columnInfos = refTable?.current?.structData || [];
          let foreignKeys = refTable?.current?.fkData || [];
          for (let key of Object.keys(labels)) {
            if (labels[key]) {
              if (key === 'tableName') {
                tableName = labels[key];
              } else {
                if (Array.isArray(labels[key])) {
                  tableLabels.push({ labelCode: key });
                } else {
                  tableLabels.push({ labelCode: key, labelParamValue: labels[key] });
                }
              }
            }
          }
          const data = {
            tableName,
            tableLabels,
            columnInfos: columnInfos.map((_: any, i: number) => {
              const columnLabels = [];
              for (let key of Object.keys(_)) {
                if (key !== 'id' && key !== 'stringName' && _[key] !== null) {
                  columnLabels.push({
                    columnName: _.stringName,
                    labelCode: key,
                    labelParamValue: _[key],
                  });
                }
              }
              return { columnIndex: i, columnName: _.stringName, columnLabels };
            }),
            foreignKeys: foreignKeys.map((_: any) => {
              return {
                ..._,
                columnNames: _.columnNames.join(','),
                referColumnNames: _.referColumnNames.join(','),
              };
            }),
          };
          createTable(data)
            .then((res) => {
              if (res.success) {
                message.success(fileCode === 'newTable' ? '创建表成功' : '修改表成功');
                setMode('view');
                getTableInfo(res.data.id);
              }
            })
            .catch((err) => {})
            .finally(() => setLoading(false));
        });
      });
    });
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
      {mode === 'view' && <ViewTable info={{ data }} />}
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
          <Button key="cancel">取消</Button>,
        ]}
      </div>
    </Fragment>
  );
};

export default TabTable;
