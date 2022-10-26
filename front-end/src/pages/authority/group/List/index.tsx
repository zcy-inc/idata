import React, { useState, useEffect } from 'react';
import { Button, Input, Modal, Form, Select, Tooltip } from 'antd';
import { pick } from 'lodash';
import { TiledTable, Operation, SearchPanel } from '@/components';
import { usePaginated, useSave } from '@/hooks';
import { getGroupPaging, queryUserList, addGroup, editGroup, deleteGroup } from '@/services/group';
import { getDeleteFn } from '@/utils/utils';
import type { Tuser } from '@/interfaces/group';
import type { ColumnsType } from 'antd/es/table';
import AuthorizationModal from '../../components/AuthorizationModal'

const { Option } = Select;

const List: React.FC = () => {
  const [namePattern, setNamePattern] = useState(); // 用户组名称搜索
  const [userList, setUserList] = useState<any>([]); // 用户列表
  const [addVisible, setAddVisible] = useState(false); // 新建用户组弹窗
  const [currentRecord, setCurrentRecord] = useState<Tuser>();
  const [editVisible, setEditVisible] = useState(false); // 编辑用户组弹窗
  const [authorizeVisible, setAuthorizeVisible] = useState(false); // 授权弹窗
  const [currentAuthorize, setCurrentAuthorize] = useState({}); // 当前授权对象
  const [addForm] = Form.useForm();
  const [editForm] = Form.useForm();
  // 分页查询用户组
  const { tableProps, refresh } = usePaginated((params) => getGroupPaging({ ...params, namePattern }), {
    refreshDeps: [namePattern],
  });

  useEffect(() => {
    queryUserList().then(res => {
      const { data = [] } = res
      setUserList(data)
    })
  }, [])

  // 确认新建用户组
  const { run: onAdd } = useSave(
    async () => {
      const values = await addForm.validateFields();
      const { name, ownerId, remark, relatedUsers } = values
      const params = {
        name,
        ownerId,
        remark,
        relatedUsers: relatedUsers?.map((i: string) => { return { id: i } })
      }
      return addGroup(params);
    },
    () => {
      setAddVisible(false);
      addForm.resetFields();
      refresh();
    },
  );

  // 关闭新建用户组弹窗
  const onCloseAddModal = () => {
    addForm.resetFields();
    setAddVisible(false);
  };

  // 打开编辑弹窗 数据回填
  const onEditClick = (row: any) => {
    const partial = pick(row, ['name', 'ownerId', 'relatedUsers', 'remark']);
    setCurrentRecord(row);
    const editData = {
      name: partial.name,
      ownerId: JSON.stringify(partial.ownerId),
      relatedUsers: partial.relatedUsers?.map((i: string) => JSON.stringify(i.id)),
      remark: partial.remark,
    }
    editForm.setFieldsValue(editData);
    setEditVisible(true);
  };
  // 关闭编辑弹窗
  const onCloseEditModal = () => {
    setEditVisible(false);
  };

  // 确认编辑用户组
  const { run: onEdit } = useSave(
    async () => {
      const values = await editForm.validateFields();
      const params = {
        name: values.name,
        ownerId: values.ownerId,
        remark: values.remark,
        relatedUsers: values.relatedUsers?.map((i: string) => { return { id: i } })
      }
      return editGroup({ ...params, id: currentRecord?.id });
    },
    () => {
      setEditVisible(false);
      refresh();
    },
  );

  // 用户组名称搜索
  const onSearch = (e: React.KeyboardEvent<HTMLInputElement>) => setNamePattern((e.target as any).value);

  // 打开授权弹窗
  const onAuthorize = (row: Tuser) => {
    addForm.resetFields();
    setCurrentAuthorize(row);
    setAuthorizeVisible(true);
  };

  // 删除用户组
  const onDelete = getDeleteFn(deleteGroup, refresh);

  const columns: ColumnsType<Tuser> = [
    { title: '用户组名称', dataIndex: 'name', render: (_) => _ || '-' },
    { title: '负责人', dataIndex: 'ownerName', render: (_) => _ || '-' },
    {
      title: '用户',
      dataIndex: 'relatedUsers',
      ellipsis: {
        showTitle: false,
      },
      render: (relatedUsers) => {
        const arr = relatedUsers.map((i: any) => i.nickname)
        return Array.isArray(arr) && arr.length ?
          <Tooltip placement="topLeft" title={arr.join('、')}>
            <div style={{ textOverflow: 'ellipsis', overflow: 'hidden', 'width': 200 }}>
              {arr.join('、')}
            </div>
          </Tooltip> : '-'
      },
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: {
        showTitle: false,
      },
      render: _ => (
        <Tooltip placement="topLeft" title={_}>
          <div style={{ textOverflow: 'ellipsis', overflow: 'hidden', 'width': 200 }}>
            {_ || '-'}
          </div>
        </Tooltip>
      ),
    },
    { title: '修改时间', dataIndex: 'editTime', render: (_) => _ || '-' },
    {
      title: '操作',
      width: 200,
      fixed: 'right',
      render: (_, row) => (
        <Operation.Group>
          <Operation label="编辑" onClick={() => onEditClick(row)} />
          <Operation label="删除" onClick={() => onDelete(row.id)} />
          <Operation label="授权" onClick={() => onAuthorize(row)} />
        </Operation.Group>
      ),
    },
  ];
  return (
    <>
      <SearchPanel
        templateColumns="280px"
        options={[
          {
            label: '用户组名称',
            name: 'namePattern',
            component: <Input onPressEnter={onSearch} placeholder="请输入" />,
          },
        ]}
      />
      <TiledTable
        rowKey="id"
        leftBtns={<Button onClick={() => setAddVisible(true)}>新建用户组</Button>}
        columns={columns}
        {...tableProps}
      />

      {/* 新建用户组 */}
      <Modal
        visible={addVisible}
        title="新建用户组"
        width={530}
        onCancel={onCloseAddModal}
        onOk={onAdd}
      >
        <Form form={addForm} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
          <Form.Item label="用户组名称" name="name" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="负责人" name="ownerId" rules={[{ required: true }]}>
            <Select style={{ width: 300 }} showSearch filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }>
              {
                userList?.map(item => {
                  return <Option value={item.id} key={item.id}>{item.name}</Option>
                })
              }
            </Select>
          </Form.Item>
          <Form.Item label="成员" name="relatedUsers" rules={[{ required: false }]}>
            <Select mode="multiple" style={{ width: 300 }}>
              {
                userList?.map(item => {
                  return <Option value={item.id} key={item.id}>{item.name}</Option>
                })
              }
            </Select>
          </Form.Item>
          <Form.Item label="备注" name="remark" rules={[{ required: false }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 编辑用户 */}
      <Modal
        visible={editVisible}
        title="编辑用户"
        width={530}
        onCancel={onCloseEditModal}
        onOk={onEdit}
      >
        <Form form={editForm} labelCol={{ span: 6 }} wrapperCol={{ span: 18 }}>
          <Form.Item label="用户组名称" name="name" rules={[{ required: true }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
          <Form.Item label="负责人" name="ownerId" rules={[{ required: true }]}>
            <Select style={{ width: 300 }} showSearch filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }>
              {
                userList?.map(item => {
                  return <Option value={item.id} key={item.id}>{item.name}</Option>
                })
              }
            </Select>
          </Form.Item>
          <Form.Item label="成员" name="relatedUsers" rules={[{ required: false }]}>
            <Select mode="multiple" style={{ width: 300 }}>
              {
                userList?.map(item => {
                  return <Option value={item.id} key={item.id}>{item.name}</Option>
                })
              }
            </Select>
          </Form.Item>
          <Form.Item label="备注" name="remark" rules={[{ required: false }]}>
            <Input style={{ width: 300 }} />
          </Form.Item>
        </Form>
      </Modal>

      {/* 授权 */}
      <AuthorizationModal
        subjectType="groups"
        subjectId={currentAuthorize?.id}
        authorizeVisible={authorizeVisible}
        setAuthorizeVisible={setAuthorizeVisible}
      />
    </>
  );
};

export default List;
