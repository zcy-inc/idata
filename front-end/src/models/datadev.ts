import { useCallback, useEffect, useRef, useState } from 'react';
import { getTree } from '@/services/datadev';
import { TreeNode, Task } from '@/types/datadev';
import { newEnum, newTable, newDAG, mockTree, newFun } from './constants';
import { FolderBelong,FolderTypes } from '@/constants/datadev';

export interface IPane extends TreeNode {
  mode: 'view' | 'edit';
}
interface FolderListItem {
  key: string;
  title: string;
}

export default () => {
  const [tree, setTree] = useState<TreeNode[]>([]); // 当前树的数据
  const [belongFunctions, setBelongFunctions] = useState<string[]>(Object.values(FolderBelong));
  const [keyWord, setKeyWord] = useState('');
  const [curNode, setCurNode] = useState<TreeNode | null>(); // 存储文件夹数据, 编辑的时候赋值
  const [folderMode, setFolderMode] = useState<'create' | 'edit'>('create');
  const [panes, setPanes] = useState<IPane[]>([]); // 右侧tabs的list
  const [activeKey, setActiveKey] = useState(''); // 右侧tabs的activeKey
  const [visibleLabel, setVisibleLabel] = useState<boolean>(false);
  const [visibleTask, setVisibleTask] = useState<boolean>(false);
  const [visibleDev, setVisibleDev] = useState<boolean>(false);
  const [curLabel, setCurLabel] = useState(-1); // 存储label的id，用来判断新建和编辑
  const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);// 树的选中节点 cid且唯一[xxx]
  const folderList = useRef<FolderListItem[]>([]);

  useEffect(() => {
    getTreeWrapped();
  }, [belongFunctions]);

  /**
   * 获取树，封装了入参 belongFunctions 和 keyWord
   */
  const getTreeWrapped = () =>
    getTree({ belongFunctions, keyWord })
      .then((res) => {
        setTree(res.data);
        if (!folderList.current.length) {
          generateFolderList(res.data);
        }
        return res;
      })
      .catch(() => setTree(mockTree));

  const generateFolderList = (data: TreeNode[]) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i] as TreeNode;
      folderList.current.push({ key: node.cid, title: node.name });
      if (node.children) {
        generateFolderList(node.children);
      }
    }
  };

  /**
   * 切换当前 active 的tab
   */
  const onChangeTab = useCallback((key) => setActiveKey(key), [panes]);

   /**
   * V1.8 新增DI/作业直接打开编辑器和树 一帧
   * cid = record+belong+id
   */
    const onSelectNewTab =  (belong:FolderBelong,concreteBelong: string, data: Task) => {
      const cid:string = `${FolderTypes.RECORD}_${belong}_${data.id}`
      onViewTree({...data, belong, cid, concreteBelong} as unknown as TreeNode);
      setSelectedKeys([cid]);
    }

  /**
   * 增加一个 view mode 的tab
   */
  const onViewTree = useCallback(
    (node: TreeNode) => {
      const { cid, name } = node;
      if (!panes.some((_) => _.key === cid)) {
        setPanes([...panes, { ...node, key: cid, title: name, mode: 'view' }]);
      }
      setActiveKey(cid);
    },
    [panes],
  );

  /**
   * 增加一个新建枚举的Tab
   */
  const onCreateEnum = useCallback(() => {
    !panes.some((_) => _.key === 'newEnum') && setPanes([...panes, newEnum]);
    setActiveKey('newEnum');
  }, [panes]);

  /**
   * 增加一个新建表的Tab
   */
  const onCreateTable = useCallback(() => {
    !panes.some((_) => _.key === 'newTable') && setPanes([...panes, newTable]);
    setActiveKey('newTable');
  }, [panes]);

  /**
   * 增加一个新建DAG的Tab
   */
  const onCreateDAG = useCallback(() => {
    !panes.some((_) => _.key === 'newDAG') && setPanes([...panes, newDAG]);
    setActiveKey('newDAG');
  }, [panes]);

  /**
   * 增加一个新建函数的Tab
   */
  const onCreateFun = useCallback(() => {
    !panes.some((_) => _.key === 'newFun') && setPanes([...panes, newFun]);
    setActiveKey('newFun');
  }, [panes]);

  /**
   * 关闭Tab
   */
  const onRemovePane = useCallback(
    (key) => {
      const curPanes = panes.filter((_) => _.key !== key);
      let curActiveKey = activeKey;
      let lastIndex = panes.findIndex((_) => _.key === key) - 1;

      if (curPanes.length && curActiveKey === key) {
        curActiveKey = curPanes[lastIndex > 0 ? lastIndex : 0].key || '';
      }
      setPanes(curPanes);
      setActiveKey(curActiveKey);
    },
    [panes],
  );

  /**
   * 显示编辑标签的Modal
   * @param id
   */
  const showLabel = (id?: number) => {
    id && setCurLabel(id);
    setVisibleLabel(true);
  };

  /**
   * 关闭编辑标签的Modal
   */
  const hideLabel = () => {
    setCurLabel(-1);
    setVisibleLabel(false);
  };

  /**
   * 新建完成后替换key和title，编辑完成后可能修改了title也需要替换
   * @param oldKey 用以检索原tab的位置
   * @param newKey 自己拼装的cid，type + belong + id
   * @param data 返回的数据对象
   */
  const replaceTab = ({
    oldKey,
    newKey,
    title,
    pane,
  }: {
    oldKey: string;
    newKey: string;
    title: string;
    pane: IPane;
  }) => {
    let targetI = panes.findIndex((_) => _.key === oldKey);
    let newTab: IPane = {
      ...pane,
      key: newKey,
      title,
      cid: newKey,
      mode: 'view',
    };
    panes[targetI] = newTab;
    getTree({ belongFunctions, keyWord }).then((res) => setTree(res.data));
    setActiveKey(newKey);
    setPanes([...panes]);
  };

  return {
    tree, // 树
    getTreeWrapped, // 获取树的接口
    folderMode, // 文件夹弹窗的模式，edit / create
    setFolderMode, // 设置文件夹弹窗的模式
    curNode, // 当前操作的节点
    setCurNode, // 设置当前操作的节点
    belongFunctions, // 筛选树的belongs
    setBelongFunctions, // 设置树的belongs
    keyWord, // 筛选树的keyword
    setKeyWord, // 设置筛选树的keyWord
    folderList,
    panes,
    activeKey,
    onChangeTab,
    onCreateEnum,
    onCreateTable,
    onViewTree,
    onRemovePane,
    visibleLabel,
    showLabel,
    hideLabel,
    curLabel,
    setCurLabel,
    replaceTab,
    onCreateDAG,
    onCreateFun,
    visibleTask,
    setVisibleTask,
    visibleDev,
    setVisibleDev,
    selectedKeys, 
    setSelectedKeys,
    onSelectNewTab
  };
};