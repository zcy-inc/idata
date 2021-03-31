import React from 'react';
import { Select, SelectProps } from 'antd';
import { useRequest } from 'umi';
import { getRoleList } from '@/services/role';

const { Option } = Select;

const RoleSelect: React.FC<SelectProps<string[]>> = (props) => {
  const { data } = useRequest(getRoleList);
  return (
    <Select mode="multiple" {...props}>
      {data?.content.map((item) => (
        <Option key={item.id} value={item.roleCode}>{item.roleName}</Option>
      ))}
    </Select>
  );
};

export default RoleSelect;
