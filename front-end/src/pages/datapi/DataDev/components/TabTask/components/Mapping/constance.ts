import { TaskContent } from '@/types/datadev';

export const data: Partial<TaskContent> = {
  srcCols: [
    {
      name: 'node1',
      dataType: 'String',
      primaryKey: false,
      mappedColumn: { name: 'node7', dataType: '', primaryKey: false },
    },
    {
      name: 'node2',
      dataType: 'String',
      primaryKey: false,
      mappedColumn: { name: 'node8', dataType: '', primaryKey: false },
    },
    {
      name: 'node3',
      dataType: 'Boolean',
      primaryKey: false,
      mappedColumn: { name: 'node6', dataType: '', primaryKey: false },
    },
    {
      name: 'node4',
      dataType: 'String',
      primaryKey: false,
      mappedColumn: { name: 'node0', dataType: '', primaryKey: false },
    },
    {
      name: 'node5',
      dataType: 'Boolean',
      primaryKey: false,
      mappedColumn: { name: 'node9', dataType: '', primaryKey: false },
    },
  ],
  destCols: [
    {
      name: 'node6',
      dataType: '',
      primaryKey: false,
      mappedColumn: { name: 'node3', dataType: '', primaryKey: false },
    },
    {
      name: 'node7',
      dataType: '',
      primaryKey: false,
      mappedColumn: { name: 'node1', dataType: '', primaryKey: false },
    },
    {
      name: 'node8',
      dataType: '',
      primaryKey: false,
      mappedColumn: { name: 'node2', dataType: '', primaryKey: false },
    },
    {
      name: 'node9',
      dataType: '',
      primaryKey: false,
      mappedColumn: { name: 'node5', dataType: '', primaryKey: false },
    },
    {
      name: 'node0',
      dataType: '',
      primaryKey: false,
      mappedColumn: { name: 'node4', dataType: '', primaryKey: false },
    },
  ],
};
