package cn.zhengcaiyun.idata.merge.data.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDao;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.service.FolderMigrationService;
import cn.zhengcaiyun.idata.merge.data.util.IdPadTool;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        sql =
            "select t1.id, " +
            "       t1.del, " +
            "       t1.creator, " +
            "       t1.editor, " +
            "       t1.create_time as createTime, " +
            "       t1.edit_time as editTime, " +
            "       t1.folder_name as name, " +
            "       'FOLDER' as type, " +
            "       'DEV.JOB' as belong, " +
            "       t1.parent_id as parentId " +
            "from idata.table_folder t1";
        List<Map<String, Object>> tableFolderList = oldIDataDao.selectList(sql);
        List<CompositeFolder> tableList = tableFolderList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        // 初始化顶层id，因为顶层文件夹id不插入
        Map<Long, Long> tableIdMapping = new HashMap<>();
        tableIdMapping.put(-1L, 10005L);
        doMigrate(tableList, tableIdMapping);

        // 3. 迁移集成
        sql =
            "select t1.id,  " +
            "       t1.del," +
            "       t1.creator," +
            "       t1.editor," +
            "       t1.create_time as createTime," +
            "       t1.edit_time as editTime," +
            "       t1.folder_name as name," +
            "       'FOLDER' as type," +
            "       'DI' as belong," +
            "       t1.parent_id as parentId" +
            "from metadata.job_folder t1 left join metadata.job_info t2 on t1.id = t2.folder_id" +
            "where t2.job_type = 'DI'" +
            "group by t1.id;";
        List<Map<String, Object>> diFolderList = oldIDataDao.selectList(sql);
        List<CompositeFolder> diList = diFolderList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        // 初始化顶层id，因为顶层文件夹id不插入
        Map<Long, Long> diIdMapping = new HashMap<>();
        diIdMapping.put(-1L, 10003L);
        doMigrate(diList, diIdMapping);

        // 4. 迁移作业
        sql =
            "select t1.id,  " +
            "       t1.del," +
            "       t1.creator," +
            "       t1.editor," +
            "       t1.create_time as createTime," +
            "       t1.edit_time as editTime," +
            "       t1.folder_name as name," +
            "       'FOLDER' as type," +
            "       'DI' as belong," +
            "       t1.parent_id as parentId" +
            "from metadata.job_folder t1 left join metadata.job_info t2 on t1.id = t2.folder_id" +
            "where t2.job_type != 'DI'" +
            "group by t1.id;";
        List<Map<String, Object>> jobFolderList = oldIDataDao.selectList(sql);
        List<CompositeFolder> jobList = jobFolderList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), CompositeFolder.class))
                .collect(Collectors.toList());
        // 初始化顶层id，因为顶层文件夹id不插入
        Map<Long, Long> jobIdMapping = new HashMap<>();
        jobIdMapping.put(-1L, 10008L);
        doMigrate(jobList, jobIdMapping);
        // clear cache
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DEV_FUN);
        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_TABLE);
        return null;
    }

    private void doMigrate(List<CompositeFolder> list, Map<Long, Long> idMapping) {
        list.forEach(e -> {
            e.setName(IdPadTool.padId(e.getId() + "") + "#_" + e.getName());
            Long oldId = e.getId();
            compositeFolderDao.insert(e);
            // 插入后，e的id被覆盖成自增id
            idMapping.put(oldId, e.getId());
        });

        //prentId替换成新主键id
        list.forEach(e -> {
            e.setParentId(idMapping.get(e.getParentId()));
            compositeFolderDao.updateByPrimaryKey(e);
        });
    }

}
