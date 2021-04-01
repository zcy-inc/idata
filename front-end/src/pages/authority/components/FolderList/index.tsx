import React, { Fragment, useEffect, useState } from 'react';
import { Checkbox } from 'antd';
import { H5 } from '@/components';
import { boolToInt } from '@/utils/utils';
import type { FolderNode } from '@/interfaces/global';
import styles from './index.less';

type TauthListItem = { title: string; type: string; value: boolean };
type TauthTuple = [TauthListItem, TauthListItem, TauthListItem];

const decodeAuth = (filePermission: number = 0) => {
  let str = parseInt(String(filePermission)).toString(2);
  while (str.length < 3) {
    str = '0' + str;
  }
  const deletable = Boolean(Number(str[0]));
  const writable = Boolean(Number(str[1]));
  const readable = Boolean(Number(str[2]));
  const authTuple: TauthTuple = [
    { title: '查看', type: 'readable', value: readable },
    { title: '编辑', type: 'writable', value: writable },
    { title: '删除', type: 'deletable', value: deletable },
  ];
  return authTuple;
};

const encodeAuth = (authTuple: TauthTuple) => {
  const [{ value: readable }, { value: writable }, { value: deletable }] = authTuple;
  const filePermission = [boolToInt(deletable), boolToInt(writable), boolToInt(readable)].join('');
  return Number(parseInt(filePermission, 2).toString(10));
};

const FolderListItem: React.FC<{
  nodeData: FolderNode;
  readonly?: boolean;
  onChange?: (node: FolderNode) => void;
}> = ({ nodeData, readonly, onChange }) => {
  const { filePermission } = nodeData;

  const [authTuple, setAuthTuple] = useState<TauthTuple>();
  useEffect(() => {
    setAuthTuple(decodeAuth(filePermission));
  }, [filePermission]);

  const handleChange = (index: number) => {
    if (authTuple) {
      const holder: TauthTuple = [...authTuple];
      holder.splice(index, 1, { ...authTuple[index], value: !authTuple[index].value });
      const newNode = { ...nodeData, filePermission: encodeAuth(holder) };
      onChange?.(newNode);
    }
  };

  if (!authTuple) {
    return null;
  }
  return (
    <div className={styles.contentWrap}>
      {authTuple?.map((item, index) => (
        <Checkbox
          disabled={readonly}
          key={item.title}
          checked={item.value}
          onChange={() => handleChange?.(index)}
        >
          {item.title}
        </Checkbox>
      ))}
    </div>
  );
};

const FolderList: React.FC<{
  data?: FolderNode[];
  onChange?: (node: FolderNode) => void;
  readonly?: boolean;
}> = ({ data, onChange, readonly }) => {
  return (
    <Fragment>
      {data?.map((node) => (
        <div key={`${node.type}-${node.folderId}`} className={styles.listItemWrap}>
          <H5>{node.name}</H5>
          {typeof node.filePermission === 'number' && (
            <FolderListItem nodeData={node} readonly={readonly} onChange={onChange} />
          )}
        </div>
      ))}
    </Fragment>
  );
};

export default FolderList;
