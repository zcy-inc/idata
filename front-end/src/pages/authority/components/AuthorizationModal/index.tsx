/**
 * 授权弹窗
 * **/
import React, { useEffect, useState } from 'react';
import { Button, Modal, Form, Select, Space, Radio, Checkbox, message } from 'antd';
import { CloseOutlined, PlusOutlined } from '@ant-design/icons';
import {
  queryGroupList,
  queryUserList,
  queryTablesList,
  getAuthorizationsInfo,
  addAuthorizations,
  editAuthorizations
} from '@/services/group';
import type { AuthorizationsInfo } from '@/interfaces/group.d';
import styles from './index.less';

const { Option } = Select;
interface AuthorizationModalProps {
  subjectType: string,
  subjectId: string | number | undefined,
  authorizeVisible: boolean,
  setAuthorizeVisible: Function
}
const resourceTypeMap = [
  { label: "表", value: "tables" }
]
const dbMap = [
  { label: "ADS", value: "ads" },
  { label: "DWS", value: "dws" },
  { label: "DWD", value: "dwd" },
  { label: "DIM", value: "dim" },
  { label: "ODS", value: "ods" },
  { label: "TAG", value: "tag" },
]

const AuthorizationModal: React.FC<AuthorizationModalProps> = ({
  subjectType,
  subjectId,
  authorizeVisible,
  setAuthorizeVisible
}) => {

  const [form] = Form.useForm();
  const [subjectName, setSubjectName] = useState('-') // 授权主体(用户/用户组)
  const [dynamicTableList, setDynamicTableList] = useState([]) // 表下拉数据(依赖据库)
  const [isEdit, setIsEdit] = useState(false) // 是否是编辑授权
  const [editData, setEditData] = useState({}) // 编辑授权回填数据

  useEffect(() => {
    if (subjectType === "users" && authorizeVisible) getUserList();
    if (subjectType === "groups" && authorizeVisible) getGroupList();

    // 根据主体查询授权详情数据 数据回填
    if (authorizeVisible && subjectType && subjectId) {
      getAuthorizationsInfo(subjectType, subjectId).then(res => {
        if (res?.data) { // 存在data 则当前主体已授权
          setIsEdit(true)
          setEditData(res?.data)
          // 动态表单回填
          const params = res?.data?.authPolicyList.map((i: any) => ({
            actionList: i.actionList,
            resourceType: i.resourceType,
            effect: i.effect,
            db: JSON.parse(i.authResourceList[0].resources)?.db,
            tables: JSON.parse(i.authResourceList[0].resources)?.tables,
          }))
          form.setFieldValue("authPolicyList", params)
        }
      }).catch(err => { })
    }
  }, [authorizeVisible])

  // 获取用户下拉列表
  const getUserList = () => {
    queryUserList().then((res: any) => {
      res?.data?.forEach((i: any) => {
        if (+i.id === subjectId) setSubjectName(i.name)
      })
    }).catch(err => { })
  }

  // 获取用户组下拉列表
  const getGroupList = () => {
    queryGroupList().then((res: any) => {
      res?.data?.forEach((i: any) => {
        if (+i.id === subjectId) setSubjectName(i.name)
      })
    }).catch(err => { })
  }

  // 取消授权 关闭弹窗
  const handleCancel = () => {
    setAuthorizeVisible(false)
    form.resetFields()
  }

  // 切换库 清空对应表数据
  const handleChangeDB = async (name: number) => {
    const formRes = await form.getFieldsValue();
    form.setFields([{
      name: ['authPolicyList', name, 'tables'],
      value: []
    }])
  }

  // 聚焦表 通过库获取表数据
  const handleFocusTales = async (name: number) => {
    const formRes = await form.getFieldsValue();
    const { authPolicyList } = formRes
    const params = {
      resourceType: authPolicyList[name]?.resourceType,
      db: authPolicyList[name]?.db
    }
    if (!params.resourceType || !params.db) {
      setDynamicTableList([])
      return
    }
    queryTablesList(params).then(res => {
      const list = res.data?.map((i: any) => ({ label: i, value: i }))
      setDynamicTableList(list)
    }).catch(err => { })
  }

  // 确认授权
  const handleAuthorize = async () => {
    try {
      const formRes = await form.validateFields();
      const confirmParams = {
        id: editData?.id,
        subjectId: subjectId,
        subjectType: subjectType,
        authPolicyList: formRes?.authPolicyList?.map((item: any) => ({
          effect: item.effect,
          actionList: item.actionList,
          resourceType: item.resourceType,
          authResourceList: [{
            resourceType: item.resourceType,
            resources: JSON.stringify({
              db: item.db,
              tables: item.tables
            })
          }]
        }))
      }
      // console.log('confirmParams>>>', confirmParams, formRes);

      if (!confirmParams.authPolicyList || !confirmParams.authPolicyList.length) {
        message.error("请先设置授权规则");
        return;
      }

      if (isEdit) {
        editAuthorizations(confirmParams as AuthorizationsInfo).then(res => {
          setAuthorizeVisible(false)
          message.success('修改授权成功');
        }).catch(err => { })
      } else {
        addAuthorizations(confirmParams as AuthorizationsInfo).then(res => {
          setAuthorizeVisible(false)
          message.success('授权成功');
        }).catch(err => { })
      }

    } catch (errorInfo) {
      console.log("Failed:", errorInfo);
    }
  };

  return (
    <Modal
      key="AuthorizationModal"
      title="授权"
      width={600}
      forceRender={true}
      getContainer={false}
      visible={authorizeVisible}
      bodyStyle={{ height: "500px", overflow: "auto" }}
      footer={[
        <Button onClick={handleCancel}>取消</Button>,
        <Button type="primary" onClick={handleAuthorize}>确定</Button>
      ]}
      onCancel={handleCancel}
    >
      <Form
        form={form}
        name="dynamic_form_content"
        autoComplete="off"
      >
        <Form.Item name="subjectType" label="被授权主体类型">
          <span>{subjectType === "users" ? "用户" : "用户组"}</span>
        </Form.Item>
        <Form.Item name="subjectId" label="被授权主体">
          <span>{subjectName}</span>
        </Form.Item>
        <span>授权规则：</span>
        <Form.List name="authPolicyList">
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name, ...restField }) => {

                return (
                  <div className={styles.dynamic}>
                    <Space key={`${key}_${name}`} direction="vertical" align="baseline" size={0}>
                      <Form.Item
                        {...restField}
                        label="授权作用"
                        name={[name, "effect"]}
                        rules={[{ required: true, message: "请选择授权作用" }]}
                      >
                        {/* <Radio.Group defaultValue="allow"> */}
                        <Radio.Group>
                          <Radio value="allow">允许</Radio>
                          <Radio value="deny">拒绝</Radio>
                        </Radio.Group>
                      </Form.Item>
                      <Form.Item
                        {...restField}
                        label="授权操作"
                        name={[name, "actionList"]}
                        rules={[{ required: true, message: "请选择授权操作" }]}
                      >
                        {/* <Checkbox.Group defaultValue={['read']}> */}
                        <Checkbox.Group>
                          <Checkbox value="read">读取</Checkbox>
                          <Checkbox value="write">写入</Checkbox>
                        </Checkbox.Group>
                      </Form.Item>
                      <Form.Item
                        {...restField}
                        label="授权资源"
                        name={[name]}
                      >
                        <Form.Item
                          name={[name, "resourceType"]}
                          noStyle
                          rules={[{ required: true, message: "请选择资源" }]}
                        >
                          <Select style={{ width: 130 }} placeholder="请选择资源">
                            {
                              resourceTypeMap?.map((i, idx) => (
                                <Option key={`${i.value}_${idx}`} value={i.value}>{i.label}</Option>
                              ))
                            }
                          </Select>
                        </Form.Item>
                        <div>
                          <Form.Item
                            name={[name, "db"]}
                            noStyle
                            rules={[{ required: true, message: "请选择库" }]}
                          >
                            <Select style={{ width: 130 }} placeholder="请选择库" onChange={() => handleChangeDB(name)}>
                              {
                                dbMap?.map((i, idx) => (
                                  <Option key={`${i.value}_${idx}`} value={i.value}>{i.label}</Option>
                                ))
                              }
                            </Select>
                          </Form.Item>
                          <Form.Item
                            name={[name, "tables"]}
                            noStyle
                            rules={[{ required: true, message: "请选择表" }]}
                          >
                            <Select style={{ width: 300, marginTop: 3 }} placeholder="请选择表" mode="multiple" onFocus={() => handleFocusTales(name)}>
                              <Option key="*" value="*">所有表</Option>
                              {
                                dynamicTableList?.map((i: any, idx) => (
                                  <Option key={`${i.value}_${idx}`} value={i.value}>{i.label}</Option>
                                ))
                              }
                            </Select>
                          </Form.Item>
                        </div>
                      </Form.Item>

                      <CloseOutlined onClick={() => remove(name)} className={styles.iconDelete} />
                    </Space>
                  </div>
                )
              })}

              <Form.Item>
                <Button
                  block
                  type="dashed"
                  icon={<PlusOutlined />}
                  onClick={() => {
                    add({
                      effect: "allow",
                      actionList: ['read']
                    });
                  }}
                >
                  增加授权规则
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
      </Form>
    </Modal>
  );
};

export default AuthorizationModal;
