package cn.zhengcaiyun.idata.label.service.folder;

import cn.zhengcaiyun.idata.label.dal.dao.LabFolderDao;
import cn.zhengcaiyun.idata.label.dal.model.LabFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.label.dal.dao.LabFolderDynamicSqlSupport.labFolder;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 提供事务（避免一些事务直接加在service层，造成"大事务"）方法或基础、可复用的方法，想不到好点的名称，暂时取名manager
 * @author: yangjianhua
 * @create: 2021-06-23 13:49
 **/
@Component
public class LabFolderManager {
    private final LabFolderDao folderDao;

    @Autowired
    public LabFolderManager(LabFolderDao folderDao) {
        this.folderDao = folderDao;
    }

    public List<LabFolder> queryFolders(String belong) {
        return folderDao.select(
                dsl -> dsl.where(labFolder.belong, isEqualTo(belong),
                        and(labFolder.del, isEqualTo(DEL_NO.val))));
    }

    public LabFolder getFolder(String folderName) {
        checkArgument(isNotEmpty(folderName), "文件夹名称不能为空");
        LabFolder folder = folderDao.selectOne(
                dsl -> dsl.where(labFolder.name, isEqualTo(folderName),
                        and(labFolder.del, isEqualTo(DEL_NO.val))))
                .orElse(null);
        return folder;
    }

    public LabFolder getFolder(Long folderId, String errorMsg) {
        checkArgument(folderId != null, "文件夹编号不能为空");
        Optional<LabFolder> folderOptional = folderDao.selectByPrimaryKey(folderId);
        checkState(folderOptional.isPresent(), errorMsg);
        return folderOptional.get();
    }
}
