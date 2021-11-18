package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentKylinDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentKylin;
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
public interface DevJobContentKylinDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, editable, version, cubeName, buildType, startTime, endTime);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobContentKylin> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobContentKylinResult")
    Optional<DevJobContentKylin> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobContentKylinResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="editable", property="editable", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="cube_name", property="cubeName", jdbcType=JdbcType.VARCHAR),
        @Result(column="build_type", property="buildType", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<DevJobContentKylin> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int insert(DevJobContentKylin record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentKylin, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(editable).toProperty("editable")
            .map(version).toProperty("version")
            .map(cubeName).toProperty("cubeName")
            .map(buildType).toProperty("buildType")
            .map(startTime).toProperty("startTime")
            .map(endTime).toProperty("endTime")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int insertSelective(DevJobContentKylin record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentKylin, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(editable).toPropertyWhenPresent("editable", record::getEditable)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(cubeName).toPropertyWhenPresent("cubeName", record::getCubeName)
            .map(buildType).toPropertyWhenPresent("buildType", record::getBuildType)
            .map(startTime).toPropertyWhenPresent("startTime", record::getStartTime)
            .map(endTime).toPropertyWhenPresent("endTime", record::getEndTime)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default Optional<DevJobContentKylin> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default List<DevJobContentKylin> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default List<DevJobContentKylin> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default Optional<DevJobContentKylin> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobContentKylin, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobContentKylin record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(editable).equalTo(record::getEditable)
                .set(version).equalTo(record::getVersion)
                .set(cubeName).equalTo(record::getCubeName)
                .set(buildType).equalTo(record::getBuildType)
                .set(startTime).equalTo(record::getStartTime)
                .set(endTime).equalTo(record::getEndTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobContentKylin record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(editable).equalToWhenPresent(record::getEditable)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(cubeName).equalToWhenPresent(record::getCubeName)
                .set(buildType).equalToWhenPresent(record::getBuildType)
                .set(startTime).equalToWhenPresent(record::getStartTime)
                .set(endTime).equalToWhenPresent(record::getEndTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int updateByPrimaryKey(DevJobContentKylin record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(editable).equalTo(record::getEditable)
            .set(version).equalTo(record::getVersion)
            .set(cubeName).equalTo(record::getCubeName)
            .set(buildType).equalTo(record::getBuildType)
            .set(startTime).equalTo(record::getStartTime)
            .set(endTime).equalTo(record::getEndTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_kylin")
    default int updateByPrimaryKeySelective(DevJobContentKylin record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(editable).equalToWhenPresent(record::getEditable)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(cubeName).equalToWhenPresent(record::getCubeName)
            .set(buildType).equalToWhenPresent(record::getBuildType)
            .set(startTime).equalToWhenPresent(record::getStartTime)
            .set(endTime).equalToWhenPresent(record::getEndTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}