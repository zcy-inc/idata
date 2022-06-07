import React, { useState, useEffect } from 'react';
import { Tree, Table, Input, Button, message, Upload, Modal } from 'antd';
import { IconFont } from '@/components';
import { getTree, getJobInfo, jobExport, jobCopy, jobMove } from '@/services/datadev';
import styles from './TaskSelect.less';
import type { Job } from '@/types/datadev';
import type { TreeNode } from '@/types/datadev';
import SplitPane from 'react-split-pane';
import Iconfont from '@/components/IconFont'
import showDialog from '@/utils/showDialog';
import FolderTreeItem from '../FolderTreeItem';
import { TreeTitle } from '@/components';
import type { UploadFile } from 'antd/lib/upload/interface';
import ImportResult from './ImportResult';
export default ({ belongFunctions, getTreeWrapped, setLoading }: {belongFunctions: string [], getTreeWrapped: any, setLoading: any}) => {
  const [tree, setTree] = useState<TreeNode []>([]);
  const [jobInfo, setJobInfo] = useState<Job []>([]);
  const [expandedKeys, setExpandedKeys] = useState<(string | number)[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<(string | number)[]>([]);
  const [plaingTree, setPlainTree] = useState<TreeNode []>([]);
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
  }

  const onCheck = (_: any, {checkedNodes}: {checkedNodes : any []}) => {
    let leafNodes: number [] = [];
    checkedNodes.forEach(node => {
      if(!node.children?.length) {
        leafNodes.push(node.key)
      }
    });
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
            getTreeData();
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
          }).then(() => {
            message.success('操作成功！');
            done();
            getTreeData();
            getTreeWrapped();
          }).finally(() => {
            dialogItem.hideLoading();
          })
        } else {
          message.warning('请选择要复制的文件夹')
        }
      }
    },FolderTreeItem);
  }

  const onImport = ({file}: {file: UploadFile}) => {
    if(file.status === 'uploading') {
      setLoading(true);
    }
   if(file.status === 'done') {
    setLoading(false);
    getTreeData();
    getTreeWrapped();
     const someError = file.response.data.some((item: { success: boolean; }) => !item.success);
     if(someError) {
      showDialog('导入结果', {
        modalProps: {
          width: 580
        },
        formProps: {
          data: file.response.data || []
        }
      }, ImportResult)
      return;
     }
     message.success('上传完成');
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

  return <div className={styles['task-select']}>
    <div className={styles['operation-list']}> 
      <Button icon={<Iconfont type="icon-daochu" />} onClick={onExport}>导出</Button>
      <Button icon={<Iconfont type="icon-yidong" />} onClick={onMove}>移动</Button>
      <Button icon={<Iconfont type="icon-fuzhi1" />} onClick={onCopy}>复制</Button>
      <Upload
        action={`/api/p1/dev/jobs/import?destFolderId=${selectedKeys[0]}`}
        style={{display: 'inline-block'}}
        showUploadList={false}
        onChange={onImport}
        accept="text/plain"
      >
        <Button icon={<Iconfont type="icon-daoru" />} onClick={beforeUpload}>导入</Button>
      </Upload>
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
                getTreeData()
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
        />
      </div>
      <div className={styles["right-table"]}>
        <Table dataSource={jobInfo} columns={columns} pagination={false} rowKey='id' scroll={{ y: 315 }} />
      </div>
    </SplitPane>
  </div>
}