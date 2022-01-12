package cn.zhengcaiyun.idata.merge.data.service.impl;


import cn.hutool.core.util.NumberUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.service.FunctionMigrationService;
import cn.zhengcaiyun.idata.merge.data.util.IdPadTool;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FunctionMigrationServiceImpl implements FunctionMigrationService {

    @Autowired
    private OldIDataDao oldIDataDao;

    @Autowired
    private DevJobUdfDao devJobUdfDao;

    @Autowired
    private CompositeFolderMyDao compositeFolderMyDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MigrateResultDto> migrate() {
        String sql =
                "select t1.id, " +
                "       t1.del, " +
                "       t1.creator, " +
                "       t1.editor, " +
                "       t1.create_time as createTime, " +
                "       t1.edit_time as editTime, " +
                "       t1.udf_name as udfName, " +
                "       t1.udf_type as udfType, " +
                "       t2.resource_name as fileName, " +
                "       t2.hdfs_path as hdfsPath, " +
                "       t1.return_type as returnType, " +
                "       t1.folder_id as folderId, " +
                "       t1.description as description " +
                "from metadata.spark_udf t1 left join metadata.file_resource t2 on t1.resource_id = t2.id;";
        List<Map<String, Object>> list = oldIDataDao.selectList(sql);
        List<Long> disMappingIds = new ArrayList<>();
        List<DevJobUdf> jobUdfList = list.stream()
                .map(e -> {
                    DevJobUdf devJobUdf = JSON.parseObject(JSON.toJSONString(e), DevJobUdf.class);
                    Long newId = getMappingFolderId(devJobUdf.getFolderId());
                    devJobUdf.setFolderId(newId);
                    if (newId == null) {
                        disMappingIds.add(devJobUdf.getFolderId());
                    }
                    return devJobUdf;
                })
                .collect(Collectors.toList());

        // 插入新数据库
        jobUdfList.forEach(e -> devJobUdfDao.insert(e));

        if (CollectionUtils.isNotEmpty(disMappingIds)) {
            List<MigrateResultDto> resultDtoList = new ArrayList<>();
            disMappingIds.forEach(e -> {
                MigrateResultDto dto = new MigrateResultDto("FunctionMigrationType", "can't match folderId", e.toString());
                resultDtoList.add(dto);
            });
            return resultDtoList;
        }
        return null;
    }

    private Long getMappingFolderId(Long folderId) {
        CompositeFolder compositeFolder = compositeFolderMyDao.selectByName(IdPadTool.padId(folderId + "") + "#_");
        if (compositeFolder != null) {
            return compositeFolder.getId();
        }
        return null;
    }

}
