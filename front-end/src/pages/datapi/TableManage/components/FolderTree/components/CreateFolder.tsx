import React, { useEffect, useState } from 'react';
import { ModalForm, ProFormText, ProFormSelect } from '@ant-design/pro-form';
import { Form, message } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';

import { createFolder, getFolders, updateFolder } from '@/services/tablemanage';
import { FlatTreeNode } from '@/types/datapi';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}

const rules = [{ required: true, message: '必填' }];

const CreateFolder: FC<CreateFolderProps> = ({ visible, onCancel }) => {
  const [folders, setFolders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { folderMode, curFolder, curTreeType, getTree } = useModel('tablemanage', (ret) => ({
    folderMode: ret.folderMode,
    curFolder: ret.curFolder,
    curTreeType: ret.curTreeType,
    getTree: ret.getTree,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const folderOps = res.data?.map((_: FlatTreeNode) => ({
          label: _.folderName,
          value: `${_.id}`,
        }));
        setFolders(folderOps);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    let folderName = '';
    let parentId = null;
    if (folderMode === 'create') {
      parentId = curFolder?.type === 'FOLDER' ? curFolder?.folderId : curFolder?.parentId;
    } else {
      folderName = curFolder?.name;
      parentId = curFolder?.parentId;
    }
    form.setFieldsValue({ folderName, parentId });
  }, [folderMode, curFolder]);

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
        if (folderMode === 'edit') {
          updateFolder({
            id: curFolder.folderId,
            folderName: values.folderName,
            parentId: values.parentId,
          })
            .then((res) => {
              if (res.success) {
                message.success('编辑文件夹成功');
                onCancel();
                getTree(curTreeType);
              }
            })
            .finally(() => {
              setLoading(false);
            });
        } else {
          createFolder({
            folderName: values.folderName,
            parentId: values.parentId,
            folderType: curTreeType,
          })
            .then((res) => {
              if (res.success) {
                message.success('创建文件夹成功');
                onCancel();
                getTree(curTreeType);
              }
            })
            .finally(() => {
              setLoading(false);
            });
        }
      }}
    >
      <ProFormText name="folderName" label="文件夹名称" placeholder="请输入名称" rules={rules} />
      <ProFormSelect name="parentId" label="位置" placeholder="根目录" options={folders} />
    </ModalForm>
  );
};

export default CreateFolder;