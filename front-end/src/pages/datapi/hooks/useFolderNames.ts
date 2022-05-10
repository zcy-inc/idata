import { useState, useEffect } from 'react';
import { getSpecifiedFunctionTree } from '@/services/datadev';
import { getTreeParents } from '@/utils/utils';
import { FolderBelong } from '@/constants/datadev';

export const useFolderNames = (belongFunctions: FolderBelong[], folderId?: string | number) => {
  const [folderNames, setFolderNames] = useState('');

  useEffect(() => {
    (async function () {
      if (typeof folderId !== 'undefined') {
        const tree = await getSpecifiedFunctionTree({
          belongFunctions,
        });
        const folderNames = getTreeParents(tree, (node) => node.value, folderId)
          .map((node) => node.title)
          .join(' / ');
        setFolderNames(folderNames || '-');
      }
    })();
  }, [folderId]);

  return { folderNames };
};
