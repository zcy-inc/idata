import React, { Fragment, useEffect, useState } from 'react';
import { Checkbox } from 'antd';
import { H5 } from '@/components';
import { boolToInt } from '@/utils/utils';
import type { FolderNode } from '@/interfaces/global';
import styles from './index.less';

type TauthListItem = { title: string; value: boolean };
type TauthTuple = [TauthListItem, TauthListItem, TauthListItem];

const decodeAuth = (filePermission: string) => {
  const splitArr = filePermission.split('');
  let deletable = false,
    writable = false,
    readable = false;
  if (typeof splitArr[0] !== undefined) {
    deletable = Boolean(Number(splitArr[0]));
  }
  if (typeof splitArr[1] !== undefined) {
    writable = Boolean(Number(splitArr[1]));
  }
  if (typeof splitArr[2] !== undefined) {
    readable = Boolean(Number(splitArr[2]));
  }
  const authTuple: TauthTuple = [
    { title: '查看', value: readable },
    { title: '编辑', value: writable },
    { title: '删除', value: deletable },
  ];
  return authTuple;
};

const encodeAuth = (authTuple: TauthTuple) => {
  const [{ value: readable }, { value: writable }, { value: deletable }] = authTuple;
  const filePermission = [boolToInt(deletable), boolToInt(writable), boolToInt(readable)].join('');
  return filePermission;
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
        <div key={node.folderId} className={styles.listItemWrap}>
          <H5>{node.name}</H5>
          {typeof node.filePermission === 'string' && (
            <FolderListItem nodeData={node} readonly={readonly} onChange={onChange} />
          )}
        </div>
      ))}
    </Fragment>
  );
};

export default FolderList;
