package cn.zhengcaiyun.idata.label.service.impl;

import cn.zhengcaiyun.idata.label.dal.dao.LabFolderDao;
import cn.zhengcaiyun.idata.label.dal.model.LabFolder;
import cn.zhengcaiyun.idata.label.dto.LabFolderDto;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;
import cn.zhengcaiyun.idata.label.enums.FolderTreeNodeTypeEnum;
import cn.zhengcaiyun.idata.label.service.LabFolderService;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSupplier;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSuppliers;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.label.dal.dao.LabFolderDynamicSqlSupport.labFolder;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 文件夹接口实现
 * @author: yangjianhua
 * @create: 2021-06-21 16:11
 **/
@Service
public class LabFolderServiceImpl implements LabFolderService {

    @Autowired
    private LabFolderDao labFolderDao;

    @Override
    public Long createFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder existFolder = getFolder(labFolderDto.getName());
        checkState(existFolder == null, "文件夹名称已存在");
        Optional.ofNullable(labFolderDto.getParentId())
                .ifPresent(parentId -> getFolder(parentId, "父文件夹不存在"));

        LabFolder labFolder = newCreatedFolder(labFolderDto, operator);
        labFolderDao.insertSelective(labFolder);
        return labFolder.getId();
    }

    @Override
    public Long editFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder folder = getFolder(labFolderDto.getId(), "文件夹不存在");
        LabFolder checkNameFolder = getFolder(labFolderDto.getName());
        if (!Objects.isNull(checkNameFolder)) {
            checkState(Objects.equals(folder.getId(), checkNameFolder.getId()), "文件夹名称已存在");
        }
        Optional.ofNullable(labFolderDto.getParentId())
                .ifPresent(parentId -> getFolder(parentId, "父文件夹不存在"));

        LabFolder updateFolder = newEditedFolder(labFolderDto, operator);
        labFolderDao.updateByPrimaryKeySelective(updateFolder);
        return updateFolder.getId();
    }

    @Override
    public LabFolderDto getFolder(Long id) {
        LabFolder folder = getFolder(id, "文件夹不存在");
        LabFolderDto dto = new LabFolderDto();
        BeanUtils.copyProperties(folder, dto);
        return dto;
    }

    @Override
    public Boolean deleteFolder(Long id, String operator) {
        getFolder(id, "文件夹不存在");
        // 软删除，修改del状态
        labFolderDao.update(dsl -> dsl.set(labFolder.del).equalTo(DEL_YES.val)
                .set(labFolder.editor).equalTo(operator).where(labFolder.id, isEqualTo(id)));
        return true;
    }

    @Override
    public List<LabFolderDto> getFolders(String belong) {
        if (isEmpty(belong)) return Lists.newArrayList();

        List<LabFolder> folders = queryFolders(belong);
        BeanCopier copier = BeanCopier.create(LabFolder.class, LabFolderDto.class, false);
        // todo test
        List<LabFolderDto> folderDtoList = Lists.newArrayList();
        List<LabFolderDto> folderDtoList1 = Lists.newArrayList();
        copier.copy(folders, folderDtoList, null);
        BeanUtils.copyProperties(folders, folderDtoList1);
        return folderDtoList;
    }

    @Override
    public List<LabFolderTreeNodeDto> getFolderTree(String belong) {
        if (isEmpty(belong)) return Lists.newArrayList();
        List<LabFolder> folders = queryFolders(belong);
        if (CollectionUtils.isEmpty(folders)) return Lists.newArrayList();

        List<LabFolderTreeNodeDto> treeNodeDtoList = Lists.newArrayListWithCapacity(256);
        treeNodeDtoList.addAll(convertTreeNodes(folders));
        treeNodeDtoList.addAll(getBizEntityTreeNodes(belong));
        return makeTree(treeNodeDtoList);
    }

    private List<LabFolderTreeNodeDto> makeTree(List<LabFolderTreeNodeDto> nodeDtoList) {
        if (CollectionUtils.isEmpty(nodeDtoList)) return Lists.newArrayList();

        ListMultimap<Long, LabFolderTreeNodeDto> treeListMultimap = MultimapBuilder.treeKeys().arrayListValues().build();
        nodeDtoList.stream().forEach(nodeDto -> treeListMultimap.put(nodeDto.getParentId(), nodeDto));

        // 最上层文件夹parent id 为0
        List<LabFolderTreeNodeDto> treeList = makeTree(0L, treeListMultimap);
        return treeList == null ? Lists.newArrayList() : treeList;
    }

    private List<LabFolderTreeNodeDto> makeTree(Long parentId, ListMultimap<Long, LabFolderTreeNodeDto> listMultimap) {
        List<LabFolderTreeNodeDto> nodeDtoList = listMultimap.get(parentId);
        if (CollectionUtils.isEmpty(nodeDtoList)) return null;

        for (LabFolderTreeNodeDto nodeDto : nodeDtoList) {
            nodeDto.setChildren(makeTree(nodeDto.getId(), listMultimap));
        }
        return nodeDtoList;
    }

    private List<LabFolderTreeNodeDto> getBizEntityTreeNodes(String belong) {
        LabFolderTreeNodeSupplier supplier = LabFolderTreeNodeSuppliers.getSupplier(belong);
        return supplier == null ? Lists.newArrayList() : supplier.get();
    }

    private List<LabFolderTreeNodeDto> convertTreeNodes(List<LabFolder> folders) {
        if (CollectionUtils.isEmpty(folders)) return Lists.newArrayList();
        return folders.stream()
                .map(folder -> convertTreeNode(folder))
                .collect(Collectors.toList());
    }

    private LabFolderTreeNodeDto convertTreeNode(LabFolder folder) {
        LabFolderTreeNodeDto nodeDto = new LabFolderTreeNodeDto();
        nodeDto.setId(folder.getId());
        nodeDto.setName(folder.getName());
        nodeDto.setType(FolderTreeNodeTypeEnum.FOLDER.getCode());
        nodeDto.setBelong(folder.getBelong());
        nodeDto.setParentId(folder.getParentId());
        nodeDto.setCid(nodeDto.getType() + "_" + nodeDto.getId());
        return nodeDto;
    }

    private List<LabFolder> queryFolders(String belong) {
        return labFolderDao.select(
                dsl -> dsl.where(labFolder.belong, isEqualTo(belong),
                        and(labFolder.del, isEqualTo(DEL_NO.val))));
    }

    private LabFolder getFolder(String folderName) {
        checkArgument(isNotEmpty(folderName), "文件夹名称不能为空");
        LabFolder folder = labFolderDao.selectOne(
                dsl -> dsl.where(labFolder.name, isEqualTo(folderName),
                        and(labFolder.del, isEqualTo(DEL_NO.val))))
                .orElse(null);
        return folder;
    }

    private LabFolder getFolder(Long folderId, String errorMsg) {
        checkArgument(folderId != null, "文件夹编号不能为空");
        Optional<LabFolder> folderOptional = labFolderDao.selectByPrimaryKey(folderId);
        checkState(folderOptional.isPresent(), errorMsg);
        return folderOptional.get();
    }

    private LabFolder newCreatedFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder labFolder = new LabFolder();
        labFolder.setName(labFolderDto.getName());
        labFolder.setParentId(MoreObjects.firstNonNull(labFolderDto.getParentId(), 0L));
        labFolder.setBelong(labFolderDto.getBelong());
        labFolder.setCreator(operator);
        labFolder.setEditor(operator);
        labFolder.setDel(DEL_NO.val);
        return labFolder;
    }

    private LabFolder newEditedFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder labFolder = new LabFolder();
        labFolder.setId(labFolderDto.getId());
        labFolder.setName(labFolderDto.getName());
        labFolder.setParentId(MoreObjects.firstNonNull(labFolderDto.getParentId(), 0L));
        labFolder.setEditor(operator);
        return labFolder;
    }
}
