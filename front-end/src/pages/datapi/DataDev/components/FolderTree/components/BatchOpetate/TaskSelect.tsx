import React, { useState, useEffect } from 'react';
import { Tree, Table, Input, Button, message, Upload, Modal } from 'antd';
import { IconFont } from '@/components';
import { getTree, getJobInfo, jobExport, jobCopy, jobMove } from '@/services/datadev';
import styles from './TaskSelect.less';
import type { Job } from '@/types/datadev';
import type { TreeNode } from '@/types/datadev';
import SplitPane from 'react-split-pane';
import Iconfont from '@/components/IconFont'
import { getRequestUrl } from '@/utils/utils';
import showDialog from '@/utils/showDialog';
import FolderTreeItem from '../FolderTreeItem';
import FolderTreeUploadItem from '../FolderTreeUploadItem';
import { TreeTitle } from '@/components';
import type { RcFile, UploadFile } from 'antd/lib/upload/interface';
import ImportResult from './ImportResult';

export default ({ belongFunctions, getTreeWrapped, setLoading, dialog }: {belongFunctions: string [], getTreeWrapped: any, setLoading: any, dialog:any}) => {
  const [tree, setTree] = useState<TreeNode []>([]);
  const [jobInfo, setJobInfo] = useState<Job []>([]);
  const [expandedKeys, setExpandedKeys] = useState<(string | number)[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<(string | number)[]>([]);
  const [plaingTree, setPlainTree] = useState<TreeNode []>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(false);
  const [keyWord, setKeyWord] = useState('');

  useEffect(() => {
    getTreeData();
  }, []);

  const getPlainTree = (tree: TreeNode [], list: TreeNode []) => {
    tree.forEach(item => {
      list.push(item);
      if(item.children) {
        getPlainTree(item.children, list)
      }
    })
  }

  const getTreeData = () => {
    setAutoExpandParent(false);
    setExpandedKeys([]);
    getTree({ belongFunctions, keyWord }).then((res) => {
      res.data = res.data || [];
      setTree(res.data);
      const plainTree : TreeNode [] = [];
      getPlainTree(res.data, plainTree);
      setPlainTree(plainTree);
      // 如果是通过搜索查询的
      if(keyWord) {
        const keys = plainTree
          .map(item => {
            if (item.name.indexOf(keyWord) > -1) {
              return getParentKey(item.id, tree);
            }
            return null;
          })
          .filter((item, i, self) => item && self.indexOf(item) === i);
        setExpandedKeys(keys as number []);
        setAutoExpandParent(true);
      }
    });
  }

  const getTreeNode =( data: TreeNode []): Record<string, any> [] =>
    data.map((item) => {
      const index = item.name.indexOf(keyWord);
      const beforeStr = item.name.substring(0, index);
      const afterStr = item.name.slice(index + keyWord.length);
      const title =
        index > -1 ? (
          <span>
            {beforeStr}
            <span style={{color:'red'}}>{keyWord}</span>
            {afterStr}
          </span>
        ) : (
          <span>{item.name}</span>
        );
      if (item.type !== 'RECORD') {
        return {
          title: <TreeTitle icon="icon-wenjianjia" text={title} />,
          key: item.id,
          children: item.children?.length ? getTreeNode(item.children) : []
        };
      }

      return {
        title,
        key: item.id,
      };
    });

  const getParentKey = (key: number | undefined, tree: TreeNode []): number => {
    let parentKey: number = 0;
    for (let i = 0; i < tree.length; i++) {
      const node = tree[i];
      if (node.children) {
        if (node.children.some((item) => item.id === key)) {
          parentKey = node.id;
        } else if (getParentKey(key, node.children)) {
          parentKey = getParentKey(key, node.children);
        }
      }
    }
    return parentKey;
  };

  const onExpand = (expandedKeys: React.SetStateAction<(string | number)[]>) => {
    setExpandedKeys(expandedKeys);
    setAutoExpandParent(false);
  }

  const onCheck = (checked: any) => {
    let leafNodes: number [] = [];
    checked.forEach((key: number) => {
      const node = plaingTree.find(item => item.id === key);
      if(node?.type === 'RECORD') {
        leafNodes.push(node.id)
      }
    });
    if(leafNodes.length > 200) {
      leafNodes = leafNodes.slice(0, 200);
      message.warning('最多只能处理200条作业，超过200条的作业将被忽略');
    }
    getJobInfo({jobIds: leafNodes}).then(res => {
      setJobInfo(res.data);
    })
  }

  const onExport = () => {
    if(!jobInfo.length) {
      message.warning('请选择要操作的作业！');
      return;
    }
    const params = {
      jobIds: jobInfo.map(item => item.id).join()
    }
    jobExport(params);
  }

  const onMove = () => {
    if(!jobInfo.length) {
      message.warning('请选择要操作的作业！');
      return;
    }
    showDialog('移动到', {
      modalProps: {
        width: 560
      },
      formProps: {
        data: tree
      },
      beforeConfirm: (dialogItem, form, done) => {
        if(form.selectedFolder) {
          dialogItem.showLoading();
          jobMove({
            jobIds: jobInfo.map(item => item.id),
            destFolderId: form.selectedFolder
          }).then(() => {
            message.success('操作成功！');
            done();
            dialog.handleCancel();
            getTreeWrapped();
          }).finally(() => {
            dialogItem.hideLoading();
          })
        } else {
          message.warning('请选择要移动的文件夹')
        }
      }
    },FolderTreeItem);
  }

  const onCopy = () => {
    if(!jobInfo.length) {
      message.warning('请选择要操作的作业！');
      return;
    }

    showDialog('复制到', {
      modalProps: {
        width: 560
      },
      formProps: {
        data: tree
      },
      beforeConfirm: (dialogItem, form, done) => {
        if(form.selectedFolder) {
          dialogItem.showLoading();
          jobCopy({
            jobIds: jobInfo.map(item => item.id),
            destFolderId: form.selectedFolder
          }).then((res) => {
            console.log(res);
            const someError = res.data.some((item: { success: boolean; }) => !item.success);
            getTreeWrapped();
            if(someError) {
              showDialog('复制结果', {
                modalProps: {
                  width: 580
                },
                btns: {
                  positive:'确定',
                  negetive: false
                },
                formProps: {
                  data: res.data || []
                },
              }, ImportResult).then(() => {
                done();
                dialog.handleCancel();
              })
              return;
            }
            message.success('操作成功！');
            done();
            dialog.handleCancel();
          }).finally(() => {
            dialogItem.hideLoading();
          })
        } else {
          message.warning('请选择要复制的文件夹')
        }
      }
    },FolderTreeItem);
  }

  const beforeUpload = (e: React.MouseEvent) => {
    if(!selectedKeys.length) {
      message.warning('请在左侧选择要导入的目标文件夹')
      e.stopPropagation();
      return;
    }
    const exist = plaingTree.find(item => item.id === selectedKeys[0]);
    if(exist?.type === 'RECORD') {
      message.warning('请选择文件夹类型')
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

  const columns = [
    {
      title: '作业名',
      dataIndex: 'name',
      key: 'name',
      width: 240
    },
    {
      title: '作业类型',
      dataIndex: 'jobType',
      key: 'jobType',
      width: 200
    },
    {
      title: '版本',
      dataIndex: 'versionDisplay',
      key: 'versionDisplay',
    },
  ];

  // 导入
  const handleUpload = () => {
    showDialog('导入', {
      modalProps: {
        width: 560,
      },
      formProps: {
        data: tree,
        setLoading,
        getTreeWrapped,
        dialog,
      },
      btns: {
        positive:'关闭',
        negetive: false
      }
    },FolderTreeUploadItem);
  }

  return <div className={styles['task-select']}>
    <div className={styles['operation-list']}> 
      <Button icon={<Iconfont type="icon-daochu" />} onClick={onExport}>导出</Button>
      <Button icon={<Iconfont type="icon-yidong" />} onClick={onMove}>移动</Button>
      <Button icon={<Iconfont type="icon-fuzhi1" />} onClick={onCopy}>复制</Button>
      {/* <Upload
        action={getRequestUrl(`/api/p1/dev/jobs/import?destFolderId=${selectedKeys[0]}`)}
        style={{display: 'inline-block'}}
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
        <Button icon={<Iconfont type="icon-daoru" />} onClick={beforeUpload}>导入</Button>
      </Upload> */}
      <Button icon={<Iconfont type="icon-daoru" />} onClick={handleUpload}>导入</Button>
      已选中{jobInfo.length}个文件
    </div>
    <SplitPane defaultSize={240} style={{height: 370}}>
      <div className={styles['left-tree']}>
        <div className="search">
          <Input
            className="search-input"
            style={{ marginBottom: 8 }}
            placeholder="请输入关键字进行搜索"
            prefix={<IconFont type="icon-sousuo" />}
            onChange={e => setKeyWord(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                getTreeData();
              }
            }}
          />
        </div>
        <Tree
          onExpand={onExpand}
          checkable
          expandedKeys={expandedKeys}
          treeData={getTreeNode(tree)}
          onCheck={onCheck}
          selectedKeys={selectedKeys}
          onSelect={keys => setSelectedKeys(keys)}
          autoExpandParent={autoExpandParent}
        />
      </div>
      <div className={styles["right-table"]}>
        <Table dataSource={jobInfo} columns={columns} pagination={false} rowKey='id' scroll={{ y: 315 }} />
      </div>
    </SplitPane>
  </div>
}