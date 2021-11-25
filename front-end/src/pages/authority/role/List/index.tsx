import React, { useState } from 'react';
import { history } from 'umi';
import { Button, Modal } from 'antd';
import { TiledTable, Operation } from '@/components';
import { usePaginated } from '@/hooks';
import { getRoleList, deleteRole } from '@/services/role';
import { getDeleteFn } from '@/utils/utils';
import type { ColumnsType } from 'antd/es/table';
import type { Trole } from '@/interfaces/role';

import AuthSetting from '../../components/AuthSetting';
import useAuthSetting from '../../hooks/useAuthSetting';

const List: React.FC = () => {
  const { tableProps, refresh } = usePaginated(getRoleList);
  const [visible, setVisible] = useState(false);
  const { fetchData, authSettingProps } = useAuthSetting();

  const skip2Edit = (record: Trole) =>
    history.push(`/configuration/authority/role/edit/${record.id}/${record.roleName}`);
  const onDelete = getDeleteFn(deleteRole, refresh);
  const skip2Add = () => history.push('/configuration/authority/role/create');
  const showAuth = (id: number) => {
    fetchData({ roleId: id });
    setVisible(true);
  };
  const columns: ColumnsType<Trole> = [
    { title: '角色名称', dataIndex: 'roleName' },
    { title: '最近编辑人', dataIndex: 'editor' },
    {
      title: '操作',
      width: 200,
      render: (_, row) => (
        <Operation.Group>
          <Operation label="权限查看" onClick={() => showAuth(row.id)} />
          <Operation label="编辑" onClick={() => skip2Edit(row)} />
          <Operation label="删除" onClick={() => onDelete(row.id)} />
        </Operation.Group>
      ),
    },
  ];
  return (
    <>
      <TiledTable
        rowKey="id"
        leftBtns={<Button onClick={skip2Add}>新增角色</Button>}
        columns={columns}
        {...tableProps}
      />
      <Modal
        title="权限查看"
        width={1200}
        visible={visible}
        footer={null}
        onCancel={() => setVisible(false)}
      >
        <AuthSetting readonly={true} {...authSettingProps} />
      </Modal>
    </>
  );
};

export default List;
