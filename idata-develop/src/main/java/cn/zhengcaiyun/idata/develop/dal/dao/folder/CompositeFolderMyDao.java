package cn.zhengcaiyun.idata.develop.dal.dao.folder;

import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;

public interface CompositeFolderMyDao {

    /**
     * 使用id对name字段进行正则匹配
     * @param folderId
     * @return
     */
    CompositeFolder selectByName(String folderId);
}
