package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGScheduleDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGSchedule;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface DAGScheduleDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, dagId, beginTime, endTime, periodRange, triggerMode, cronExpression);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DAGSchedule> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DAGScheduleResult")
    Optional<DAGSchedule> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DAGScheduleResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="dag_id", property="dagId", jdbcType=JdbcType.BIGINT),
        @Result(column="begin_time", property="beginTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="period_range", property="periodRange", jdbcType=JdbcType.VARCHAR),
        @Result(column="trigger_mode", property="triggerMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="cron_expression", property="cronExpression", jdbcType=JdbcType.VARCHAR)
    })
    List<DAGSchedule> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int insert(DAGSchedule record) {
        return MyBatis3Utils.insert(this::insert, record, DAGSchedule, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(dagId).toProperty("dagId")
            .map(beginTime).toProperty("beginTime")
            .map(endTime).toProperty("endTime")
            .map(periodRange).toProperty("periodRange")
            .map(triggerMode).toProperty("triggerMode")
            .map(cronExpression).toProperty("cronExpression")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int insertSelective(DAGSchedule record) {
        return MyBatis3Utils.insert(this::insert, record, DAGSchedule, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(dagId).toPropertyWhenPresent("dagId", record::getDagId)
            .map(beginTime).toPropertyWhenPresent("beginTime", record::getBeginTime)
            .map(endTime).toPropertyWhenPresent("endTime", record::getEndTime)
            .map(periodRange).toPropertyWhenPresent("periodRange", record::getPeriodRange)
            .map(triggerMode).toPropertyWhenPresent("triggerMode", record::getTriggerMode)
            .map(cronExpression).toPropertyWhenPresent("cronExpression", record::getCronExpression)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default Optional<DAGSchedule> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default List<DAGSchedule> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default List<DAGSchedule> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default Optional<DAGSchedule> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DAGSchedule, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    static UpdateDSL<UpdateModel> updateAllColumns(DAGSchedule record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(dagId).equalTo(record::getDagId)
                .set(beginTime).equalTo(record::getBeginTime)
                .set(endTime).equalTo(record::getEndTime)
                .set(periodRange).equalTo(record::getPeriodRange)
                .set(triggerMode).equalTo(record::getTriggerMode)
                .set(cronExpression).equalTo(record::getCronExpression);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DAGSchedule record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(dagId).equalToWhenPresent(record::getDagId)
                .set(beginTime).equalToWhenPresent(record::getBeginTime)
                .set(endTime).equalToWhenPresent(record::getEndTime)
                .set(periodRange).equalToWhenPresent(record::getPeriodRange)
                .set(triggerMode).equalToWhenPresent(record::getTriggerMode)
                .set(cronExpression).equalToWhenPresent(record::getCronExpression);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int updateByPrimaryKey(DAGSchedule record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(dagId).equalTo(record::getDagId)
            .set(beginTime).equalTo(record::getBeginTime)
            .set(endTime).equalTo(record::getEndTime)
            .set(periodRange).equalTo(record::getPeriodRange)
            .set(triggerMode).equalTo(record::getTriggerMode)
            .set(cronExpression).equalTo(record::getCronExpression)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_dag_schedule")
    default int updateByPrimaryKeySelective(DAGSchedule record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(dagId).equalToWhenPresent(record::getDagId)
            .set(beginTime).equalToWhenPresent(record::getBeginTime)
            .set(endTime).equalToWhenPresent(record::getEndTime)
            .set(periodRange).equalToWhenPresent(record::getPeriodRange)
            .set(triggerMode).equalToWhenPresent(record::getTriggerMode)
            .set(cronExpression).equalToWhenPresent(record::getCronExpression)
            .where(id, isEqualTo(record::getId))
        );
    }
}