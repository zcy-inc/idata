package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.dqc.model.entity.MonitorTable;
import cn.zhengcaiyun.idata.dqc.model.query.MonitorTableQuery;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorBaselineVO;
import cn.zhengcaiyun.idata.dqc.model.vo.MonitorTableVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 数据质量被监控的表(DqcMonitorTable)表数据库访问层
 *
 * @author zheng
 * @since 2022-06-28 11:01:50
 */
public interface MonitorTableDao {

    MonitorTable getByTableName(@Param("tableName")String tableName,@Param("baselineId") Long baselineId);
    MonitorTable getById(Long id);

    List<MonitorTableVO> getByBaselineId(@Param("baselineId") Long baselineId);

    List<MonitorTable> getByPage(MonitorTableQuery query);

    int count(MonitorTableQuery query);

    int insert(MonitorTable monitorTable);

    int batchInsert(@Param("entities") List<MonitorTable> entities);

    int update(MonitorTable monitorTable);
    int updateByTableName(MonitorTable monitorTable);

    int delByBaselineId(@Param("baselineId") Long baselineId, @Param("editor") String editor);

    @MapKey("baselineId")
    HashMap<Long, MonitorBaselineVO> getTableCountByBaselineId(@Param("baselineIdList") List<Long> baselineIdList);

}

