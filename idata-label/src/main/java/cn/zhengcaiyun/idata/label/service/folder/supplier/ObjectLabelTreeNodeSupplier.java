package cn.zhengcaiyun.idata.label.service.folder.supplier;

import cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDao;
import cn.zhengcaiyun.idata.label.dal.model.LabObjectLabel;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;
import cn.zhengcaiyun.idata.label.enums.FolderTreeNodeTypeEnum;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSupplier;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSupplierFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.label.dal.dao.LabObjectLabelDynamicSqlSupport.labObjectLabel;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-22 11:50
 **/
@Component
public class ObjectLabelTreeNodeSupplier implements LabFolderTreeNodeSupplier {

    private static final String BELONG = "lab";

    private final LabObjectLabelDao objectLabelDao;

    @Autowired
    public ObjectLabelTreeNodeSupplier(LabObjectLabelDao objectLabelDao) {
        checkNotNull(objectLabelDao, "objectLabelDao must not be null.");
        this.objectLabelDao = objectLabelDao;
    }

    @PostConstruct
    public void register() {
        LabFolderTreeNodeSupplierFactory.register(BELONG, this);
    }

    /**
     * 返回业务实体集合
     *
     * @return
     */
    @Override
    public List<LabFolderTreeNodeDto> get() {
        List<LabObjectLabel> labelList = objectLabelDao.select(dsl -> dsl.where(labObjectLabel.del, isEqualTo(DEL_NO.val)));
        if (CollectionUtils.isEmpty(labelList)) return Lists.newArrayList();
        return labelList.stream()
                .map(label -> convertTreeNode(label))
                .collect(Collectors.toList());
    }

    private LabFolderTreeNodeDto convertTreeNode(LabObjectLabel label) {
        LabFolderTreeNodeDto nodeDto = new LabFolderTreeNodeDto();
        nodeDto.setId(label.getId());
        nodeDto.setName(label.getName());
        nodeDto.setType(FolderTreeNodeTypeEnum.LABEL.getCode());
        nodeDto.setBelong(BELONG);
        nodeDto.setParentId(label.getFolderId());
        nodeDto.setCid(nodeDto.getType() + "_" + nodeDto.getId());
        return nodeDto;
    }

    @Override
    public Boolean hasSubTreeNode(Long folderId) {
        long count = objectLabelDao.count(dsl -> dsl.where(labObjectLabel.folderId, isEqualTo(folderId),
                and(labObjectLabel.del, isEqualTo(DEL_NO.val))));
        return count > 0L;
    }
}
