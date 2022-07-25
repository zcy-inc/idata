package cn.zhengcaiyun.idata.dqc.dao;

import cn.zhengcaiyun.idata.connector.jdbc.model.Column;
import cn.zhengcaiyun.idata.dqc.model.vo.JobOutputVO;
import cn.zhengcaiyun.idata.dqc.model.vo.TableVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据开发-作业输出(JobOutput)表数据库访问层
 *
 * @author zheng
 * @since 2022-07-11 10:01:47
 */
public interface TableDao {
    List<TableVO> getTables(@Param("tableName") String tableName,@Param("limit") Integer limit);
    TableVO getPartitionTable(@Param("database") String database,@Param("tableName") String tableName);

    List<Column> getColumns(@Param("database") String database,@Param("tableName") String tableName);
    List<String> getOwners(@Param("database") String database,@Param("tableName") String tableName);

}

