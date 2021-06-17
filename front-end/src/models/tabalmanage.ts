import { useCallback, useState } from 'react';
import { getFolderTree } from '@/services/tablemanage';

export interface IPane {
  key: string;
  [key: string]: any;
}

const newEnum = {
  title: '新建枚举类型',
  key: 'newEnum',
  code: 'newEnum',
  type: 'ENUM',
  mode: 'edit',
};
const newTable = {
  title: '新建表',
  key: 'newTable',
  code: 'newTable',
  type: 'TABLE',
  mode: 'edit',
};

export default () => {
  const [curTreeType, setCurTreeType] = useState<string>('TABLE'); // 当前树的类型
  const [curFolder, setCurFolder] = useState<any>(); // 存储文件夹数据, 编辑的时候赋值
  const [folderMode, setFolderMode] = useState<'create' | 'edit'>('create');
  const [tree, setTree] = useState([]); // 当前树的数据
  const [panes, setPanes] = useState<IPane[]>([]); // 右侧tabs的list
  const [activeKey, setActiveKey] = useState(''); // 右侧tabs的activeKey
  const [visibleLabel, setVisibleLabel] = useState<boolean>(false);
  const [curLabel, setCurLabel] = useState(''); // 用来判断新建和编辑

  // 获取树
  const getTree = (devTreeType: any) => {
    setCurTreeType(devTreeType);
    getFolderTree({ devTreeType }).then((res) => {
      setTree(res.data);
    });
  };

  // 切换Tab
  const onChangeTab = useCallback((key) => setActiveKey(key), [panes]);

  // 新建枚举
  const onCreateEnum = useCallback(() => {
    !panes.some((_) => _.key === 'newEnum') && setPanes([...panes, newEnum]);
    setActiveKey('newEnum');
  }, [panes]);

  // 新建表
  const onCreateTable = useCallback(() => {
    !panes.some((_) => _.key === 'newTable') && setPanes([...panes, newTable]);
    setActiveKey('newTable');
  }, [panes]);

  // 点击树节点联动工作区新增tab
  const onViewTree = useCallback(
    (node) => {
      const { cid, name, fileCode, type } = node;
      if (!panes.some((_) => _.key === cid)) {
        setPanes([...panes, { key: cid, title: name, code: fileCode, type, mode: 'view' }]);
      }
      setActiveKey(cid);
    },
    [panes],
  );

  // edit模式切换到view模式时, 检索key以替换pane的title
  const replacePaneKey = (key: string, title: string) => {};

  // 关闭Tab
  const onRemovePane = useCallback(
    (key) => {
      const curPanes = panes.filter((_) => _.key !== key);
      let curActiveKey = activeKey;
      let lastIndex = panes.findIndex((_, i) => _.key === key) - 1;

      if (curPanes.length && curActiveKey === key) {
        curActiveKey = curPanes[lastIndex > 0 ? lastIndex : 0].key;
      }
      setPanes(curPanes);
      setActiveKey(curActiveKey);
    },
    [panes],
  );

  const showLabel = (fileCode?: string) => {
    fileCode && setCurLabel(fileCode);
    setVisibleLabel(true);
  };
  
  const hideLabel = () => {
    setCurLabel('');
    setVisibleLabel(false);
  };

  return {
    folderMode,
    setFolderMode,
    curFolder,
    setCurFolder,
    curTreeType,
    tree,
    getTree,
    panes,
    activeKey,
    onChangeTab,
    onCreateEnum,
    onCreateTable,
    onViewTree,
    replacePaneKey,
    onRemovePane,
    visibleLabel,
    showLabel,
    hideLabel,
    curLabel,
    setCurLabel,
  };
};
