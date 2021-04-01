import { useEffect, useState, useMemo } from 'react';
import { getRoleFeatureTree, getRoleFolderTree } from '@/services/role';
import { getUserFeatureTree, getUserFolderTree } from '@/services/user';
import { getSystemFeatureTree, getSystemFolderTree } from '@/services/global';
import { formatTreeData, getTreeNode, updateTreeNode, getFlattenChildren } from '@/utils/utils';
import type { DataNode, TreeProps } from 'antd/es/tree';
import type {
  FeatureTreeNode,
  FolderNode,
  FolderTreeNode,
  FolderDataNode,
} from '@/interfaces/global';
import { folderTreeNodeType, folderTypes } from '@/constants/common';

export default (params?: { roleId?: number; userId?: number }) => {
  const [origFeatureTree, setOrigFeatureTree] = useState<FeatureTreeNode[]>([]);
  const [featureTree, setFeatureTree] = useState<DataNode[]>([]); // 功能树
  const [folderTree, setFolderTree] = useState<FolderDataNode[]>([]); // 资源树
  const [selectedFeatureKey, setSelectedFeatureKey] = useState<React.Key>();
  const [selectedFolderKey, setSelectedFolderKey] = useState<React.Key>();

  useEffect(() => {
    (async function () {
      let tree1: FeatureTreeNode[] = [];
      let tree2: FolderTreeNode[] = [];
      if (typeof params?.roleId === 'number') {
        const [{ data: data1 }, { data: data2 }] = await Promise.all([
          getRoleFeatureTree(params.roleId),
          getRoleFolderTree(params.roleId),
        ]);
        if (Array.isArray(data1) && Array.isArray(data2)) {
          tree1 = data1;
          tree2 = data2;
        }
      } else if (typeof params?.userId === 'number') {
        const [{ data: data1 }, { data: data2 }] = await Promise.all([
          getUserFeatureTree(params.userId),
          getUserFolderTree(params.userId),
        ]);
        if (Array.isArray(data1) && Array.isArray(data2)) {
          tree1 = data1;
          tree2 = data2;
        }
      } else {
        const [{ data: data1 }, { data: data2 }] = await Promise.all([
          getSystemFeatureTree(),
          getSystemFolderTree(),
        ]);
        if (Array.isArray(data1) && Array.isArray(data2)) {
          tree1 = data1;
          tree2 = data2;
        }
      }
      // 功能树格式转换
      const featureTreeHolder = (formatTreeData(tree1, (node: FeatureTreeNode) => {
        const extObj: Partial<DataNode> = {};
        extObj.title = node.name;
        extObj.key = node.featureCode;
        return extObj;
      }) as unknown) as DataNode[];
      // featureTreeHolder = cutTreeData(featureTreeHolder, 3) as DataNode[]; // 截取3层

      // 资源树格式转换
      const folderTreeHolder = (formatTreeData(tree2, (node: FolderTreeNode) => {
        const extObj: Partial<DataNode> = {};
        extObj.title = node.name;
        if (node.type === folderTreeNodeType.F_MENU) {
          extObj.key = node.featureCode;
          extObj.className = 'menu';
        } else {
          extObj.key = `${node.type}-${node.folderId}`;
          extObj.className = 'folder';
        }
        return extObj;
      }) as unknown) as FolderDataNode[];
      setOrigFeatureTree(tree1);
      setFeatureTree(featureTreeHolder);
      setFolderTree(folderTreeHolder);
    })();
  }, [params?.roleId, params?.userId]);

  const onFeatureTreeSelect: TreeProps['onSelect'] = (selectedKeys) => {
    if (selectedKeys.length !== 1) {
      return;
    }
    const selectedKey = selectedKeys[0];
    setSelectedFeatureKey(selectedKey);
  };
  const onFolderTreeSelect: TreeProps['onSelect'] = (selectedKeys) => {
    if (selectedKeys.length !== 1) {
      return;
    }
    const selectedKey = selectedKeys[0];
    setSelectedFolderKey(selectedKey);
  };

  const onFeatureEnableChange = (curNode: FeatureTreeNode) => {
    const newOrigFeatureTree = updateTreeNode(
      origFeatureTree,
      (node) => node.featureCode === curNode.featureCode,
      () => curNode,
    );
    setOrigFeatureTree(newOrigFeatureTree);
  };
  const onFolderAuthChange = (curNode: FolderNode) => {
    const newFolderTree = updateTreeNode(
      folderTree,
      (node) => node.key === curNode.key,
      () => curNode,
    );
    setFolderTree(newFolderTree);
  };

  // 选中功能节点的子节点
  const featureList = useMemo(() => {
    const selectFeatureNode = getTreeNode(
      origFeatureTree,
      (node: FeatureTreeNode) => node.featureCode === selectedFeatureKey,
    );
    if (selectFeatureNode) {
      if (Array.isArray(selectFeatureNode.children) && selectFeatureNode.children.length > 0) {
        return selectFeatureNode.children;
      } else {
        return [selectFeatureNode];
      }
    }
    return [];
  }, [selectedFeatureKey, origFeatureTree]);
  const folderList = useMemo(() => {
    const selectFolderNode = getTreeNode(
      folderTree,
      (node: FolderDataNode) => node.key === selectedFolderKey,
    );
    // 选中菜单节点时筛选出所有子节点中的文件夹节点；选中文件夹节点时展示自身
    if (selectFolderNode?.type === folderTreeNodeType.F_MENU) {
      const a = getFlattenChildren(selectFolderNode as FolderTreeNode, (node) =>
        folderTypes.includes(node.type),
      ) as FolderNode[];
      return a;
    } else if (selectFolderNode && folderTypes.includes(selectFolderNode.type)) {
      return [selectFolderNode];
    }
    return [];
  }, [selectedFolderKey, folderTree]);

  return {
    origFeatureTree,
    featureTree,
    folderTree,
    featureList,
    folderList,
    onFeatureTreeSelect,
    onFolderTreeSelect,
    onFeatureEnableChange,
    onFolderAuthChange,
  };
};
