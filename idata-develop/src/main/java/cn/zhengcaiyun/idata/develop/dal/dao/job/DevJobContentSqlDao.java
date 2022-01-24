package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobContentSql;
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
public interface DevJobContentSqlDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, editable, version, udfIds, externalTables, sourceSql);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DevJobContentSql> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DevJobContentSqlResult")
    Optional<DevJobContentSql> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DevJobContentSqlResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="editable", property="editable", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="udf_ids", property="udfIds", jdbcType=JdbcType.VARCHAR),
        @Result(column="external_tables", property="externalTables", jdbcType=JdbcType.VARCHAR),
        @Result(column="source_sql", property="sourceSql", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<DevJobContentSql> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int insert(DevJobContentSql record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentSql, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(editable).toProperty("editable")
            .map(version).toProperty("version")
            .map(udfIds).toProperty("udfIds")
            .map(externalTables).toProperty("externalTables")
            .map(sourceSql).toProperty("sourceSql")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int insertSelective(DevJobContentSql record) {
        return MyBatis3Utils.insert(this::insert, record, devJobContentSql, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(editable).toPropertyWhenPresent("editable", record::getEditable)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(udfIds).toPropertyWhenPresent("udfIds", record::getUdfIds)
            .map(externalTables).toPropertyWhenPresent("externalTables", record::getExternalTables)
            .map(sourceSql).toPropertyWhenPresent("sourceSql", record::getSourceSql)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default Optional<DevJobContentSql> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default List<DevJobContentSql> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default List<DevJobContentSql> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default Optional<DevJobContentSql> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, devJobContentSql, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    static UpdateDSL<UpdateModel> updateAllColumns(DevJobContentSql record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(editable).equalTo(record::getEditable)
                .set(version).equalTo(record::getVersion)
                .set(udfIds).equalTo(record::getUdfIds)
                .set(externalTables).equalTo(record::getExternalTables)
                .set(sourceSql).equalTo(record::getSourceSql);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DevJobContentSql record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(editable).equalToWhenPresent(record::getEditable)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(udfIds).equalToWhenPresent(record::getUdfIds)
                .set(externalTables).equalToWhenPresent(record::getExternalTables)
                .set(sourceSql).equalToWhenPresent(record::getSourceSql);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int updateByPrimaryKey(DevJobContentSql record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(editable).equalTo(record::getEditable)
            .set(version).equalTo(record::getVersion)
            .set(udfIds).equalTo(record::getUdfIds)
            .set(externalTables).equalTo(record::getExternalTables)
            .set(sourceSql).equalTo(record::getSourceSql)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_sql")
    default int updateByPrimaryKeySelective(DevJobContentSql record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(editable).equalToWhenPresent(record::getEditable)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(udfIds).equalToWhenPresent(record::getUdfIds)
            .set(externalTables).equalToWhenPresent(record::getExternalTables)
            .set(sourceSql).equalToWhenPresent(record::getSourceSql)
            .where(id, isEqualTo(record::getId))
        );
    }
}