package cn.zhengcaiyun.idata.develop.dal.dao;


import cn.zhengcaiyun.idata.develop.dal.model.TableSibshipVO;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.query.TableSibshipQuery;
import cn.zhengcaiyun.idata.develop.dto.table.TableSibship;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 表血缘关系(TableSibship)表数据库访问层
 *
 * @author zheng
 * @since 2022-09-29 10:36:24
 */
public interface TableSibshipDao {

    int insertBatch(@Param("entities") List<TableSibship> entities);

    int update(TableSibship tableSibship);

    int delByJobId(Long jobId);

    List<JobPublishRecord> getPublishedJobs(@Param("startIndex")Integer startIndex);

    List<TableSibship> getChildren(@Param("db")String db,@Param("tableName")String tableName);

    List<TableSibship> getParents(@Param("db")String db,@Param("tableName")String tableName);

    List<TableSibshipVO> getSqlJobs(@Param("db")String db,@Param("tableName")String tableName);

    List<TableSibshipVO> getDIJobs(@Param("tableName")String tableName);

    List<TableSibshipVO> getJobs(@Param("tableName")String tableName);
}

