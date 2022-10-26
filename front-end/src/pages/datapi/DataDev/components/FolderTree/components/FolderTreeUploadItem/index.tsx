import React, { useState, useImperativeHandle } from 'react';
import { Tree, Button, Upload, message } from 'antd';
import type { TreeNode } from '@/types/datadev';
import _ from 'lodash';
import { TreeTitle } from '@/components';
import Iconfont from '@/components/IconFont'
import { getRequestUrl } from '@/utils/utils';
import showDialog from '@/utils/showDialog';
import type { UploadFile } from 'antd/es/upload/interface';
import ImportResult from '../BatchOpetate/ImportResult';
import './index.less';

export default ({ data, setLoading, getTreeWrapped, dialog }: { data: TreeNode[], setLoading: Function, getTreeWrapped: Function, dialog: any }, ref: React.Ref<unknown> | undefined) => {
  const [selectedFolder, setSelectedFolder] = useState<string | number>('');
  const [expandedKeys, setExpandedKeys] = useState<(string | number)[]>([data[0].id]);

  useImperativeHandle(ref, () => ({
    selectedFolder,
  }))

  const getTreeNode = (data: TreeNode[]): Record<string, any>[] => {
    return data.map((item) => {
      if (item.type !== 'RECORD') {
        let children = item.children || [];
        children = children.filter(child => child.type !== 'RECORD');
        return {
          title: <TreeTitle icon="icon-wenjianjia" text={item.name} />,
          key: item.id,
          children: getTreeNode(children)
        };
      }
      return {}
    })
  };

  const beforeUpload = (e: React.MouseEvent) => {
    if(!selectedFolder) {
      message.warning('请选择要导入的目标文件夹')
      e.stopPropagation();
      return;
    }
  }

  const onImport = ({file}: {file: UploadFile}) => {
    if(file.status === 'uploading') {
      setLoading(true);
    } else if(file.status === 'error') {
      setLoading(false);
      message.success('文件导入失败');
    } else if(file.status === 'done') {
      setLoading(false);
      if(file.response.success) {
        getTreeWrapped();
        const someError = file.response.data.some((item: { success: boolean; }) => !item.success);
        if(someError) {
          showDialog('导入结果', {
            modalProps: {
              width: 580
            },
            btns: {
              positive:'确定',
              negetive: false
            },
            formProps: {
              data: file.response.data || []
            },
          }, ImportResult).then(() => {
            dialog.handleCancel();
          })
          return;
        }
        message.success('上传完成');
        dialog.handleCancel();
      } else {
        message.error(file.response.msg);
      }
    }
  }

  return (
    <>
      <Tree
        className='folder-tree-item'
        treeData={getTreeNode(data)}
        expandedKeys={expandedKeys}
        onExpand={key => setExpandedKeys(key)}
        onSelect={(selectedKeys,) => {
          setSelectedFolder(selectedKeys[0])
        }}
      />
      <Upload
        action={getRequestUrl(`/api/p1/dev/jobs/import?destFolderId=${selectedFolder}`)}
        className="uploadBtn"
        showUploadList={false}
        onChange={onImport}
        accept="text/plain"
        beforeUpload={file => {
          if(file.size > 30 * 1024 * 1024) {
            message.warning('请选择小于30M的文件');
            return false;
          }
          return true;
        }}
      >
        <Button icon={<Iconfont type="icon-daoru" />}size="middle" onClick={beforeUpload}>请选择导入文件</Button>
      </Upload>
    </>
  )
}