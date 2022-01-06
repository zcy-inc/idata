package cn.zhengcaiyun.idata.merge.data.service.impl;


import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.service.FunctionMigrationService;
import com.alibaba.fastjson.JSON;
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
        List<DevJobUdf> jobUdfList = list.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), DevJobUdf.class))
                .collect(Collectors.toList());

        // 插入新数据库
        jobUdfList.forEach(e -> devJobUdfDao.insert(e));
        return null;
    }

}
