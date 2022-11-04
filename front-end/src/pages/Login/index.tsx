import React, { Fragment, useState } from 'react';
import { Button, Form, notification, Tabs, Radio } from 'antd';
import { omit } from 'lodash';
import { useLocation, useModel } from 'umi';
import { ValidateStatus } from 'antd/es/form/FormItem';
import { InfoCircleOutlined } from '@ant-design/icons';
import { mainPublicPath } from '@/constants/common';
import { queryRegister, queryLogin } from '@/services/user';

import OutlinedInput, { OutlinedPassword } from './components/OutlinedInput';

import styles from './index.less';

const { TabPane } = Tabs;
enum errCodeEnum {
  USER = 'userName.is.wrong',
  PW = 'psw.is.wrong',
}
const getValidateMsg = (msg: string) => (
  <Fragment>
    <InfoCircleOutlined /> {msg}
  </Fragment>
);
const getValidateInfo = (code: errCodeEnum, msg: string) => {
  const validateObj: TvalidateObj = {
    status: 'error',
    help: getValidateMsg(msg),
  };
  if (code === errCodeEnum.USER) {
    return {
      username: validateObj,
    };
  }
  if (code === errCodeEnum.PW) {
    return {
      password: validateObj,
    };
  }
  return undefined;
};

interface TvalidateObj {
  status: ValidateStatus;
  help: React.ReactNode;
}
type TvalidateInfo = {
  username?: TvalidateObj;
  password?: TvalidateObj;
};

const Empty = () => {
  return (
    <div className={styles.empty}>
      <img
        alt="illustration"
        src="https://sitecdn.zcycdn.com/f2e-assets/680a5a36-da31-41f5-a601-fcecf67ba8a2.svg"
      />
      <p>管理员已关闭注册通道</p>
      <p>请联系管理员~</p>
    </div>
  );
};

const Login = () => {
  const { query: { redirect } = {} } = useLocation() as { query?: { redirect?: string } };
  const { initialState } = useModel('@@initialState');
  const [btnLoading, setBtnLoading] = useState(false);
  const [validateInfo, setValidateInfo] = useState<TvalidateInfo>({});
  const [form1] = Form.useForm();
  const [form2] = Form.useForm();

  const onLogin = async () => {
    const values = await form1.validateFields();
    setBtnLoading(true);
    const { success, msg, code } = await queryLogin(values);
    if (!success) {
      setBtnLoading(false);
      const newValidateInfo = getValidateInfo(code, msg);
      if (newValidateInfo) {
        setValidateInfo(newValidateInfo);
        return;
      }
      notification.error({
        description: msg,
        message: '出错啦！',
      });
      return;
    }
    //解决跳转总是到dashboard的bug
    let map = new Map();
    let currentUrl = window.location.href;

    for (let i = 0; i < 3; i++) {//对skip2Login的encodeComponentURI编码的uri进行解码
      currentUrl = decodeURIComponent(currentUrl);
    }
    let currentUrlArray = currentUrl.split('?redirect=');//根据?redirect=进行拆分
    for (let i = 0; i < currentUrlArray.length; i++) {
      if (map.has(currentUrlArray[i]) !== true) {
        map.set(currentUrlArray[i], true);
      }
    }
    let arrayTemp = [];
    for (const [key, value] of map) {
      arrayTemp.push(key);
    }
    currentUrlArray = arrayTemp;
    if (currentUrlArray.length >= 2) {//currentUrlArray.length>=2则表明有相应跳转
      currentUrlArray.shift();
      if (currentUrlArray.length >= 1) {
        currentUrl = currentUrlArray.join('?redirect=');
        console.error(`1234 currentUrl`, currentUrl);
      }
    } else {//currentUrlArray.length===1则表明无相应跳转，则直接跳转到默认目录
      currentUrl = mainPublicPath;
    }
    window.location.href = currentUrl;

  };

  const onRegister = async () => {
    const values = await form2.validateFields();
    const { success } = await queryRegister(values);
    if (success) {
      window.location.href = mainPublicPath;
    }
  };

  const clearUserValidateState = () => {
    if (validateInfo.username) {
      setValidateInfo(omit(validateInfo, ['username']));
    }
  };

  const clearPwValidateState = () => {
    if (validateInfo.password) {
      setValidateInfo(omit(validateInfo, ['password']));
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.bgWrap}>
        <img
          alt="背景图"
          src="https://sitecdn.zcycdn.com/f2e-assets/07676d6a-d302-4079-8b7f-5dec81c5bd64.webp"
        />
      </div>
      <div className={styles.loginWrap}>
        <div className={styles.centerBox}>
          <img
            className={styles.loginTitle}
            src="https://sitecdn.zcycdn.com/f2e-assets/b609cee4-c7d7-45bf-86da-3f5263fb8fa1.svg"
          />
          <Tabs defaultActiveKey="1" size="large">
            <TabPane tab="登录" key="1">
              <Form form={form1}>
                <Form.Item>
                  <Radio.Group defaultValue={1}>
                    <Radio value={1}>LDAP</Radio>
                    <Radio value={2}>普通登录</Radio>
                  </Radio.Group>
                </Form.Item>
                <Form.Item
                  name="username"
                  rules={[{ required: true, message: getValidateMsg('请输入用户名') }]}
                  validateStatus={validateInfo?.username?.status || undefined}
                  help={validateInfo?.username?.help || undefined}
                >
                  <OutlinedInput label="请输入用户名" onChange={clearUserValidateState} />
                </Form.Item>
                <Form.Item
                  name="password"
                  rules={[{ required: true, message: getValidateMsg('请输入密码') }]}
                  validateStatus={validateInfo?.password?.status || undefined}
                  help={validateInfo?.password?.help || undefined}
                >
                  <OutlinedPassword label="请输入密码" onChange={clearPwValidateState} />
                </Form.Item>
                <div className={styles.loginBtnWrap}>
                  <Button
                    loading={btnLoading}
                    type="primary"
                    style={{ width: 360 }}
                    onClick={onLogin}
                  >
                    登录
                  </Button>
                </div>
              </Form>
            </TabPane>
            <TabPane tab="注册" key="2">
              {initialState?.systemState?.registerEnable ? (
                <Form form={form2}>
                  <Form.Item
                    name="username"
                    rules={[{ required: true, message: getValidateMsg('请输入账号') }]}
                    validateStatus={validateInfo?.username?.status || undefined}
                    help={validateInfo?.username?.help || undefined}
                  >
                    <OutlinedInput label="请输入账号" onChange={clearUserValidateState} />
                  </Form.Item>
                  <Form.Item
                    name="password"
                    rules={[{ required: true, message: getValidateMsg('请输入密码') }]}
                    validateStatus={validateInfo?.username?.status || undefined}
                    help={validateInfo?.username?.help || undefined}
                  >
                    <OutlinedPassword label="请输入密码" onChange={clearUserValidateState} />
                  </Form.Item>
                  <Form.Item
                    name="nickname"
                    rules={[{ required: true, message: getValidateMsg('请输入名称') }]}
                    validateStatus={validateInfo?.username?.status || undefined}
                    help={validateInfo?.username?.help || undefined}
                  >
                    <OutlinedInput label="请输入名称" onChange={clearUserValidateState} />
                  </Form.Item>
                  <Form.Item
                    name="mobile"
                    rules={[{ required: true, message: getValidateMsg('请输入手机号') }]}
                    validateStatus={validateInfo?.username?.status || undefined}
                    help={validateInfo?.username?.help || undefined}
                  >
                    <OutlinedInput label="请输入手机号" onChange={clearUserValidateState} />
                  </Form.Item>
                  <Form.Item
                    name="department"
                    rules={[{ required: true, message: getValidateMsg('请输入部门') }]}
                    validateStatus={validateInfo?.username?.status || undefined}
                    help={validateInfo?.username?.help || undefined}
                  >
                    <OutlinedInput label="请输入部门" onChange={clearUserValidateState} />
                  </Form.Item>
                  <div className={styles.loginBtnWrap}>
                    <Button
                      loading={btnLoading}
                      type="primary"
                      style={{ width: 360 }}
                      onClick={onRegister}
                    >
                      注册并登录
                    </Button>
                  </div>
                </Form>
              ) : (
                <Empty />
              )}
            </TabPane>
          </Tabs>
        </div>
      </div>
    </div>
  );
};

export default Login;
