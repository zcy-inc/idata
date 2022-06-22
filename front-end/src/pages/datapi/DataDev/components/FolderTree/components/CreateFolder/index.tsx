import React, { useEffect, useState } from 'react';
import { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Form, message } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';

import { createFolder,  updateFolder } from '@/services/datadev';
import { FolderTypes } from '@/constants/datadev';
import { LOCATIONFolderFormItem } from '../../../../../components/FolderFormItem';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}

const rules = [{ required: true, message: '必填' }];

const CreateFolder: FC<CreateFolderProps> = ({ visible, onCancel}) => {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { curNode, folderMode, getTreeWrapped } = useModel('datadev', (_) => ({
    curNode: _.curNode,
    folderMode: _.folderMode,
    getTreeWrapped: _.getTreeWrapped,
  }));

  useEffect(() => {
    const name = folderMode === 'edit' ? curNode?.name : '';
    const parentId = folderMode === 'edit' ? curNode?.parentId : curNode?.id;
    form.setFieldsValue({ name, parentId });
  }, [folderMode, curNode]);

  return (
    <ModalForm
      title={folderMode === 'create' ? '新建文件夹' : '编辑文件夹'}
      layout="horizontal"
      form={form}
      visible={visible}
      colon={false}
      labelCol={{ span: 6 }}
      wrapperCol={{ offset: 1 }}
      width={540}
      modalProps={{ onCancel, maskClosable: false, destroyOnClose: true }}
      submitter={{
        submitButtonProps: { loading, size: 'large' },
        resetButtonProps: { size: 'large' },
      }}
      onFinish={async (values) => {
        setLoading(true);
        if (folderMode === 'create') {
          createFolder({
            name: values.name,
            parentId: values.parentId,
            belong: curNode?.belong,
            type: FolderTypes.FOLDER,
          })
            .then((res) => {
              if (res.success) {
                message.success('创建文件夹成功');
                onCancel();
                getTreeWrapped();
              }
            })
            .catch((err) => {})
            .finally(() => setLoading(false));
        }
        if (folderMode === 'edit') {
          if (curNode) {
            updateFolder({
              id: curNode.id,
              name: values.name,
              parentId: values.parentId,
              type: FolderTypes.FOLDER,
              belong: curNode.belong,
            })
              .then((res) => {
                if (res.success) {
                  message.success('编辑文件夹成功');
                  onCancel();
                  getTreeWrapped();
                }
              })
              .catch((err) => {})
              .finally(() => setLoading(false));
          }
        }
      }}
    >
      <ProFormText name="name" label="文件夹名称" placeholder="请输入名称" rules={rules} />
      <LOCATIONFolderFormItem belongFunctions={[curNode?.belong]}/>
    </ModalForm>
  );
};

export default CreateFolder;
