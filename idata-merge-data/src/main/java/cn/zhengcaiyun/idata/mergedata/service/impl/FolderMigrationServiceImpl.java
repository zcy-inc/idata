package cn.zhengcaiyun.idata.mergedata.service.impl;

import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDao;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.mergedata.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.service.FolderMigrationService;
import cn.zhengcaiyun.idata.mergedata.util.IdPadTool;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FolderMigrationServiceImpl implements FolderMigrationService {

    @Autowired
    private OldIDataDao oldIDataDao;

    @Autowired
    private CompositeFolderDao compositeFolderDao;

    @Autowired
    private DevTreeNodeLocalCache devTreeNodeLocalCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MigrateResultDto> migrate() {

        // 1. 迁移函数文件夹
        String sql =
                "select t1.id, " +
                "       t1.del, " +
                "       t1.creator, " +
                "       t1.editor, " +
                "       t1.create_time as createTime, " +
                "       t1.edit_time as editTime, " +
                "       t1.folder_name as name, " +
                "       'FOLDER' as type, " +
                "       'DEV.FUN' as belong, " +
                "       t1.parent_id as parentId " +
                "from metadata.spark_udf_folder t1";
        List<Map<String, Object>> udfFolderList = oldIDataDao.selectList(sql);
        List<CompositeFolder> udfList = udfFolderList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        // 初始化顶层id，因为顶层文件夹id不插入
        Map<Long, Long> udfIdMapping = new HashMap<>();
        udfIdMapping.put(-1L, 10037L);
        doMigrate(udfList, udfIdMapping);

        // 2. 迁移数据表文件夹
//        sql =
//            "select t1.id, " +
//            "       t1.del, " +
//            "       t1.creator, " +
//            "       t1.editor, " +
//            "       t1.create_time as createTime, " +
//            "       t1.edit_time as editTime, " +
//            "       t1.folder_name as name, " +
//            "       'FOLDER' as type, " +
//            "       'DESIGN.TABLE' as belong, " +
//            "       t1.parent_id as parentId " +
//            "from idata.table_folder t1";
//        List<Map<String, Object>> tableFolderList = oldIDataDao.selectList(sql);
//        List<CompositeFolder> tableList = tableFolderList.stream()
//                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
//                .collect(Collectors.toList());
//        // 初始化顶层id，因为顶层文件夹id不插入
//        Map<Long, Long> tableIdMapping = new HashMap<>();
//        tableIdMapping.put(-1L, 10005L);
//        doMigrate(tableList, tableIdMapping);

        // 3. 迁移集成
        sql =
            " select id,  " +
            "       del, " +
            "       creator, " +
            "       editor, " +
            "       create_time as createTime, " +
            "       edit_time as editTime, " +
            "       folder_name as name, " +
            "       'FOLDER' as type, " +
            "       parent_id as parentId " +
            " from metadata.job_folder ";
        List<Map<String, Object>> folderMapList = oldIDataDao.selectList(sql);
        List<CompositeFolder> folderList = folderMapList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        Map<Long, Long> gMapping = folderList.stream()
                .filter(e -> e.getParentId() != null)
                .collect(Collectors.toMap(e -> e.getId(), e -> e.getParentId()));

        sql =
            " select t1.id " +
            " from metadata.job_folder t1 left join metadata.job_info t2 on t1.id = t2.folder_id " +
            " where t2.job_type = 'DI' and t1.del = false " +
            " group by t1.id; ";
        List<Long> diIdList = oldIDataDao.selectList(sql).stream()
                .map(e -> Long.parseLong(e.get("id").toString()))
                .collect(Collectors.toList());
        List<CompositeFolder> diList = getFolderList(diIdList, gMapping, folderList, "DI");
        // 初始化顶层id，因为顶层文件夹id不插入
        Map<Long, Long> diIdMapping = new HashMap<>();
        diIdMapping.put(-1L, 10003L);
        doMigrate(diList, diIdMapping, "DI");

        // 4. 迁移作业
         sql =
            " select t1.id " +
            " from metadata.job_folder t1 left join metadata.job_info t2 on t1.id = t2.folder_id " +
            " where t2.job_type != 'DI' " +
            " group by t1.id;";
        List<Long> jobIdList = oldIDataDao.selectList(sql).stream()
                .map(e -> Long.parseLong(e.get("id").toString()))
                .collect(Collectors.toList());

        // doMigrate方法会修改元素信息，所以此处需要重新初始化所有数据
        folderList = folderMapList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        List<CompositeFolder> jobList = getFolderList(jobIdList, gMapping, folderList, "DEV.JOB");
        // 初始化顶层id，因为顶层文件夹id不插入
          Map<Long, Long> jobIdMapping = new HashMap<>();
        jobIdMapping.put(-1L, 10008L);
        doMigrate(jobList, jobIdMapping);
        // clear cache
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DEV_FUN);
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_TABLE);
        return new ArrayList<>();
    }

    private List<CompositeFolder> getFolderList(List<Long> idList, Map<Long, Long> gMapping, List<CompositeFolder> folderList, String belong) {
        Set<Long> set = new HashSet<>();
        for (Long id : idList) {
            set.add(id);
            while (gMapping.containsKey(id)) {
                id = gMapping.get(id);
                set.add(id);
            }
        }
        List<CompositeFolder> list = folderList.stream().filter(e -> set.contains(e.getId())).collect(Collectors.toList());
        list.forEach(e -> e.setBelong(belong));
        return list;
    }

    private void doMigrate(List<CompositeFolder> list, Map<Long, Long> idMapping) {
        doMigrate(list, idMapping, null);
    }

    private void doMigrate(List<CompositeFolder> list, Map<Long, Long> idMapping, String extraAlias) {
        list.forEach(e -> {
            if (StringUtils.isEmpty(extraAlias)) {
                e.setName(IdPadTool.padId(e.getId() + "") + "#_" + e.getName());
            } else {
                e.setName(IdPadTool.padId(e.getId() + "") + "#_" + extraAlias + "#_" + e.getName());
            }
            Long oldId = e.getId();
            compositeFolderDao.insert(e);
            // 插入后，e的id被覆盖成自增id
            idMapping.put(oldId, e.getId());
        });

        //prentId替换成新主键id
        list.forEach(e -> {
            if (idMapping.get(e.getParentId()) == null) {
                System.out.println(e.getParentId());
            }
            e.setParentId(idMapping.get(e.getParentId()));
            compositeFolderDao.updateByPrimaryKey(e);
        });
    }

}
