import React, { useState } from 'react';
import { Button, Input, Modal, Form, message } from 'antd';
import { pick } from 'lodash';
import { TiledTable, Operation, PageContainer, SearchPanel } from '@/components';
import { usePaginated, useSave } from '@/hooks';
import type { ColumnsType } from 'antd/es/table';
import { getUserList, deleteUser, addUser, editUser, resetUserPassword } from '@/services/user';
import type { Tuser } from '@/interfaces/user';
import { authTypeEnum } from '@/constants/common';
import { getDeleteFn } from '@/utils/utils';
import RoleSelect from '../../components/RoleSelect';
import AuthSetting from '../../components/AuthSetting';
import useAuthSetting from '../../hooks/useAuthSetting';

const List: React.FC = () => {
  const [name, setName] = useState();
  const [addVisible, setAddVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<Tuser>();
  const { fetchData, authSettingProps } = useAuthSetting();
  const [editVisible, setEditVisible] = useState(false);
  const [authVisible, setAuthVisible] = useState(false);
  const [addForm] = Form.useForm();
  const [editForm] = Form.useForm();
  const { tableProps, refresh } = usePaginated((params) => getUserList({ ...params, name }), {
    refreshDeps: [name],
  });
  const { run: onAdd } = useSave(
    async () => {
      const values = await addForm.validateFields();
      return addUser(values);
    },
    () => {
      setAddVisible(false);
      refresh();
    },
  );
  const { run: onEdit } = useSave(
    async () => {
      const values = await editForm.validateFields();
      return editUser({ ...values, id: currentRecord?.id });
    },
    () => {
      setEditVisible(false);
      refresh();
    },
  );

  const onCloseAddModal = () => {
    addForm.resetFields();
    setAddVisible(false);
  };
  const onCloseEditModal = () => {
    setEditVisible(false);
  };
  const onEditClick = (row: Tuser) => {
    const partial = pick(row, ['nickname', 'mobile', 'roleCodes', 'department']);
    setCurrentRecord(row);
    editForm.setFieldsValue(partial);
    setEditVisible(true);
  };

  const onSearch = (e: React.KeyboardEvent<HTMLInputElement>) => setName((e.target as any).value);

  const onDelete = getDeleteFn(deleteUser, refresh);

  const onShowAuth = (row: Tuser) => {
    fetchData({ userId: row.id });
    setAuthVisible(true);
  };

  const onResetPsd = async () => {
    if (!currentRecord) {
      return;
    }
    const { success } = await resetUserPassword(currentRecord.id);
    success && message.success('重置成功');
  };

  const columns: ColumnsType<Tuser> = [
    {
      title: '账号名称',
      dataIndex: 'username',
    },
    {
      title: '员工名称',
      dataIndex: 'nickname',
    },
    {
      title: '系统管理员',
      dataIndex: 'sysAdmin',
      render: (sysAdmin) => (typeof sysAdmin === 'number' && sysAdmin > 0 ? '是' : '否'),
    },
    {
      title: '所属角色',
      dataIndex: 'roleNames',
      render: (roleNames) => (Array.isArray(roleNames) ? roleNames.join('、') : ''),
    },
    {
      title: '所属部门',
      dataIndex: 'department',
    },
    {
      title: '联系方式',
      dataIndex: 'mobile',
    },
    {
      title: '信息来源',
      dataIndex: 'authType',
    },
    {
      title: '最近编辑人',
      dataIndex: 'editor',
    },
    {
      title: '操作',
      width: 200,
      render: (_, row) => (
        <Operation.Group>
          <Operation label="权限查看" onClick={() => onShowAuth(row)} />
          <Operation label="编辑" onClick={() => onEditClick(row)} />
          <Operation label="删除" onClick={() => onDelete(row.id)} />
        </Operation.Group>
      ),
    },
  ];
  return (
    <PageContainer>
      <SearchPanel
        templateColumns="280px"
        options={[
          {
            label: '用户名称',
            name: 'name',
            component: <Input onPressEnter={onSearch} placeholder="请输入" />,
          },
        ]}
      />
      <TiledTable
        rowKey="id"
        leftBtns={<Button onClick={() => setAddVisible(true)}>新增用户</Button>}
        columns={columns}
        {...tableProps}
      />

      <Modal
        title="权限查看"
        width={1200}
        visible={authVisible}
        footer={null}
        onCancel={() => setAuthVisible(false)}
      >
        <AuthSetting readonly={true} {...authSettingProps} />
      </Modal>

      <Modal
        visible={addVisible}
        title="新增用户"
        width={530}
        onCancel={onCloseAddModal}
        onOk={onAdd}
      >
        <Form form={addForm} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
          <Form.Item label="账号名称" name="username" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="账号密码" name="password" rules={[{ required: true }]}>
            <Input.Password style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="所属角色" name="roleCodes" rules={[{ required: true }]}>
            <RoleSelect style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="员工昵称" name="nickname" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="所属部门" name="department" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="联系方式" name="mobile" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        visible={editVisible}
        title="编辑用户"
        width={530}
        onCancel={onCloseEditModal}
        onOk={onEdit}
      >
        <Form form={editForm} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
          <Form.Item label="账号名称">{currentRecord?.username}</Form.Item>
          <Form.Item label="账号密码">
            <Button
              disabled={currentRecord?.authType !== authTypeEnum.REGISTER}
              onClick={onResetPsd}
              style={{ paddingTop: 4, paddingBottom: 4, height: 32 }}
            >
              重置密码
            </Button>
          </Form.Item>
          <Form.Item label="所属角色" name="roleCodes" rules={[{ required: true }]}>
            <RoleSelect style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="员工昵称" name="nickname" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="所属部门" name="department" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="联系方式" name="mobile" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
        </Form>
      </Modal>
    </PageContainer>
  );
};

export default List;
