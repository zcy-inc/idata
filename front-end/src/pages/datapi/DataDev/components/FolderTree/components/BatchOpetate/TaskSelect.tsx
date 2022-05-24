import React, { useState, useEffect } from 'react';
import { Tree, Table, Input } from 'antd';
import { IconFont } from '@/components';
import { getTree, getJobInfo } from '@/services/datadev';
import styles from './TaskSelect.less';
import type { TreeNode } from '@/types/datadev';
import SplitPane from 'react-split-pane';

export default ({ belongFunctions }: {belongFunctions: string []}) => {
  const [tree, setTree] = useState<TreeNode []>([]);
  const [jobInfo, setJobInfo] = useState([]);
  const [expandedKeys, setExpandedKeys] = useState<(string | number)[]>([]);
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
    getTree({ belongFunctions, keyWord }).then((res) => {
      res.data = res.data || [];
      setTree(res.data);
      // 如果是通过搜索查询的
      if(keyWord) {
        const plainTree : TreeNode [] = [];
        getPlainTree(res.data, plainTree);
        const keys = plainTree
          .map(item => {
            if (item.name.indexOf(keyWord) > -1) {
              return getParentKey(item.cid, tree);
            }
            return null;
          })
          .filter((item, i, self) => item && self.indexOf(item) === i);
        setExpandedKeys(keys as string []);
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
      if (item.children) {
        return { title, key: item.cid, children: getTreeNode(item.children) };
      }

      return {
        title,
        key: item.cid,
      };
    });

  const getParentKey = (key: string | undefined, tree: TreeNode []): string => {
    let parentKey = '';
    for (let i = 0; i < tree.length; i++) {
      const node = tree[i];
      if (node.children) {
        if (node.children.some((item) => item.cid === key)) {
          parentKey = node.cid;
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

  const onCheck = (checkedKeys: any, {checkedNodes}: {checkedNodes : any []}) => {
    console.log(checkedKeys, checkedNodes);
    let leafNodes: string [] = [];
    checkedNodes.forEach(node => {
      if(!node.children?.length) {
        leafNodes.push(node.key)
      }
    });
    getJobInfo({jobIds: leafNodes.join()}).then(res => {
      console.log(res);
    })
  }

  const columns = [
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '年龄',
      dataIndex: 'age',
      key: 'age',
    },
    {
      title: '住址',
      dataIndex: 'address',
      key: 'address',
    },
  ];

  return <div className={styles['task-select']}>
    <SplitPane defaultSize={240}>
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
          autoExpandParent={autoExpandParent}
          expandedKeys={expandedKeys}
          treeData={getTreeNode(tree)}
          onCheck={onCheck}
        />
      </div>
      <Table dataSource={jobInfo} columns={columns} />
    </SplitPane>
  </div>
}