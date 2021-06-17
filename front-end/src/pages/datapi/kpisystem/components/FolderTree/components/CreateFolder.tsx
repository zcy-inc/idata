import React, { useEffect, useState } from 'react';
import { ModalForm, ProFormText, ProFormSelect } from '@ant-design/pro-form';
import { message } from 'antd';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../../index.less';

import { createFolder, getFolders, updateFolder } from '@/services/tablemanage';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}

const rules = [{ required: true, message: '必填' }];

const CreateFolder: FC<CreateFolderProps> = ({ visible, onCancel }) => {
  const [folders, setFolders] = useState<any[]>([]);

  const { folderMode, curNode, treeType, getTree } = useModel('kpisystem', (ret) => ({
    folderMode: ret.folderMode,
    curNode: ret.curNode,
    treeType: ret.treeType,
    getTree: ret.getTree,
  }));

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: `${_.id}` }));
        setFolders(fd);
      })
      .catch((err) => {});
  }, []);

  return (
    <ModalForm
      className={styles.reset}
      title="新建文件夹"
      layout="horizontal"
      colon={false}
      labelCol={{ span: 6 }}
      wrapperCol={{ offset: 1 }}
      width={540}
      modalProps={{ onCancel, maskClosable: false, destroyOnClose: true }}
      visible={visible}
      onFinish={async (values) => {
        const params: any = { folderName: values.folderName, parentId: values.parentId };
        if (curNode) {
          Object.assign(params, { id: curNode.folderId });
          updateFolder(params)
            .then((res) => {
              if (res.success) {
                message.success('编辑文件夹成功');
                onCancel();
                getTree(treeType);
              }
            })
            .catch((err) => {});
        } else {
          createFolder(params)
            .then((res) => {
              if (res.success) {
                message.success('创建文件夹成功');
                onCancel();
                getTree(treeType);
              }
            })
            .catch((err) => {});
        }
      }}
    >
      <ProFormText
        name="folderName"
        label="文件夹名称"
        rules={rules}
        placeholder="请输入名称"
        initialValue={folderMode === 'create' ? '' : curNode?.name}
      />
      <ProFormSelect
        name="parentId"
        label="位置"
        placeholder="根目录"
        options={folders}
        initialValue={
          folderMode === 'create' && curNode?.type === 'FOLDER'
            ? curNode?.folderId
            : curNode?.parentId
        }
      />
    </ModalForm>
  );
};

export default CreateFolder;
