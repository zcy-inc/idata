import { FolderBelong, FolderTypes } from '@/constants/datadev';
import { IPane } from './datadev';

export const newTable: IPane = {
  title: '新建表',
  name: '新建表',
  key: 'newTable',
  id: -1,
  cid: 'newTable',
  type: FolderTypes.RECORD,
  belong: FolderBelong.DESIGNTABLE,
  mode: 'edit',
};

export const newEnum: IPane = {
  title: '新建枚举类型',
  name: '新建枚举类型',
  key: 'newEnum',
  id: -1,
  cid: 'newEnum',
  type: FolderTypes.RECORD,
  belong: FolderBelong.DESIGNENUM,
  mode: 'edit',
};

export const newDAG: IPane = {
  title: '新建DAG',
  name: '新建DAG',
  key: 'newDAG',
  id: -1,
  cid: 'newDAG',
  type: FolderTypes.RECORD,
  belong: FolderBelong.DAG,
  mode: 'edit',
};

export const newFun: IPane = {
  title: '新建函数',
  name: '新建函数',
  key: 'newFun',
  id: -1,
  cid: 'newFun',
  type: FolderTypes.RECORD,
  belong: FolderBelong.DEVFUN,
  mode: 'edit',
};

export const mockBelongTree = [
  {
    cid: '1',
    id: 1,
    name: '数仓设计',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DESIGN,
    children: [
      {
        cid: '11',
        id: 11,
        name: '表',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNTABLE,
        parentCid: '11',
        parentId: 11,
      },
      {
        cid: '12',
        id: 12,
        name: '标签',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNLABEL,
        parentCid: '1',
        parentId: 1,
      },
      {
        cid: '13',
        id: 13,
        name: '枚举',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNENUM,
        parentCid: '1',
        parentId: 1,
      },
    ],
  },
  {
    cid: '2',
    id: 2,
    name: 'DAG',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DAG,
  },
  {
    cid: '3',
    id: 3,
    name: '数据集成',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DI,
  },
  {
    cid: '4',
    id: 4,
    name: '数据开发',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DEV,
    children: [
      {
        cid: '41',
        id: 41,
        name: '作业',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DEVJOB,
        parentCid: '4',
        parentId: 4,
      },
    ],
  },
];

export const mockTree = [
  {
    cid: '1',
    id: 1,
    name: '数仓设计',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DESIGN,
    children: [
      {
        cid: '11',
        id: 11,
        name: '表',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNTABLE,
        parentCid: '11',
        parentId: 11,
        children: [
          {
            cid: '111',
            id: 111,
            name: '一个文件夹',
            type: FolderTypes.FOLDER,
            belong: FolderBelong.DESIGNTABLE,
            parentCid: '11',
            parentId: 11,
          },
          {
            cid: '112',
            id: 112,
            name: '一张表',
            type: FolderTypes.RECORD,
            belong: FolderBelong.DESIGNTABLE,
            parentCid: '11',
            parentId: 11,
          },
        ],
      },
      {
        cid: '12',
        id: 12,
        name: '标签',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNLABEL,
        parentCid: '1',
        parentId: 1,
      },
      {
        cid: '13',
        id: 13,
        name: '枚举',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DESIGNENUM,
        parentCid: '1',
        parentId: 1,
      },
    ],
  },
  {
    cid: '2',
    id: 2,
    name: 'DAG',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DAG,
    children: [
      {
        cid: '21',
        id: 21,
        name: '一个DAG',
        type: FolderTypes.RECORD,
        belong: FolderBelong.DAG,
        parentCid: '2',
        parentId: 2,
      },
    ],
  },
  {
    cid: '3',
    id: 3,
    name: '数据集成',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DI,
  },
  {
    cid: '4',
    id: 4,
    name: '数据开发',
    type: FolderTypes.FUNCTION,
    belong: FolderBelong.DEV,
    children: [
      {
        cid: '41',
        id: 41,
        name: '作业',
        type: FolderTypes.FUNCTION,
        belong: FolderBelong.DEVJOB,
        parentCid: '4',
        parentId: 4,
        children: [
          {
            cid: '411',
            id: 411,
            name: '一个作业',
            type: FolderTypes.RECORD,
            belong: FolderBelong.DEVJOB,
            parentCid: '41',
            parentId: 41,
          },
        ],
      },
    ],
  },
];
