package cn.zhengcaiyun.idata.develop.dal.dao.job;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
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
public interface DIJobContentDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, jobId, editable, version, direct, srcDataSourceType, srcDataSourceId, srcTopic, srcReadMode, srcReadFilter, srcReadShardKey, srcShardingNum, destDataSourceType, destDataSourceId, destTopic, destTable, destWriteMode, destBeforeWrite, destAfterWrite, destProperties, destShardingNum, destBulkNum, contentHash, mergeSql, scriptSelectColumns, scriptKeyColumns, scriptMergeSqlParam, configMode, srcTables, srcColumns, srcQuery, destColumns, scriptMergeSql, scriptQuery);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<DIJobContent> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DIJobContentResult")
    Optional<DIJobContent> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DIJobContentResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="job_id", property="jobId", jdbcType=JdbcType.BIGINT),
        @Result(column="editable", property="editable", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="direct", property="direct", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_data_source_type", property="srcDataSourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_data_source_id", property="srcDataSourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="src_topic", property="srcTopic", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_read_mode", property="srcReadMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_read_filter", property="srcReadFilter", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_read_shard_key", property="srcReadShardKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="src_sharding_num", property="srcShardingNum", jdbcType=JdbcType.INTEGER),
        @Result(column="dest_data_source_type", property="destDataSourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_data_source_id", property="destDataSourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="dest_topic", property="destTopic", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_table", property="destTable", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_write_mode", property="destWriteMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_before_write", property="destBeforeWrite", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_after_write", property="destAfterWrite", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_properties", property="destProperties", jdbcType=JdbcType.VARCHAR),
        @Result(column="dest_sharding_num", property="destShardingNum", jdbcType=JdbcType.INTEGER),
        @Result(column="dest_bulk_num", property="destBulkNum", jdbcType=JdbcType.BIGINT),
        @Result(column="content_hash", property="contentHash", jdbcType=JdbcType.VARCHAR),
        @Result(column="merge_sql", property="mergeSql", jdbcType=JdbcType.VARCHAR),
        @Result(column="script_select_columns", property="scriptSelectColumns", jdbcType=JdbcType.VARCHAR),
        @Result(column="script_key_columns", property="scriptKeyColumns", jdbcType=JdbcType.VARCHAR),
        @Result(column="script_merge_sql_param", property="scriptMergeSqlParam", jdbcType=JdbcType.VARCHAR),
        @Result(column="config_mode", property="configMode", jdbcType=JdbcType.TINYINT),
        @Result(column="src_tables", property="srcTables", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="src_columns", property="srcColumns", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="src_query", property="srcQuery", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="dest_columns", property="destColumns", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="script_merge_sql", property="scriptMergeSql", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="script_query", property="scriptQuery", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<DIJobContent> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int insert(DIJobContent record) {
        return MyBatis3Utils.insert(this::insert, record, DIJobContent, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(jobId).toProperty("jobId")
            .map(editable).toProperty("editable")
            .map(version).toProperty("version")
            .map(direct).toProperty("direct")
            .map(srcDataSourceType).toProperty("srcDataSourceType")
            .map(srcDataSourceId).toProperty("srcDataSourceId")
            .map(srcTopic).toProperty("srcTopic")
            .map(srcReadMode).toProperty("srcReadMode")
            .map(srcReadFilter).toProperty("srcReadFilter")
            .map(srcReadShardKey).toProperty("srcReadShardKey")
            .map(srcShardingNum).toProperty("srcShardingNum")
            .map(destDataSourceType).toProperty("destDataSourceType")
            .map(destDataSourceId).toProperty("destDataSourceId")
            .map(destTopic).toProperty("destTopic")
            .map(destTable).toProperty("destTable")
            .map(destWriteMode).toProperty("destWriteMode")
            .map(destBeforeWrite).toProperty("destBeforeWrite")
            .map(destAfterWrite).toProperty("destAfterWrite")
            .map(destProperties).toProperty("destProperties")
            .map(destShardingNum).toProperty("destShardingNum")
            .map(destBulkNum).toProperty("destBulkNum")
            .map(contentHash).toProperty("contentHash")
            .map(mergeSql).toProperty("mergeSql")
            .map(scriptSelectColumns).toProperty("scriptSelectColumns")
            .map(scriptKeyColumns).toProperty("scriptKeyColumns")
            .map(scriptMergeSqlParam).toProperty("scriptMergeSqlParam")
            .map(configMode).toProperty("configMode")
            .map(srcTables).toProperty("srcTables")
            .map(srcColumns).toProperty("srcColumns")
            .map(srcQuery).toProperty("srcQuery")
            .map(destColumns).toProperty("destColumns")
            .map(scriptMergeSql).toProperty("scriptMergeSql")
            .map(scriptQuery).toProperty("scriptQuery")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int insertSelective(DIJobContent record) {
        return MyBatis3Utils.insert(this::insert, record, DIJobContent, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(jobId).toPropertyWhenPresent("jobId", record::getJobId)
            .map(editable).toPropertyWhenPresent("editable", record::getEditable)
            .map(version).toPropertyWhenPresent("version", record::getVersion)
            .map(direct).toPropertyWhenPresent("direct", record::getDirect)
            .map(srcDataSourceType).toPropertyWhenPresent("srcDataSourceType", record::getSrcDataSourceType)
            .map(srcDataSourceId).toPropertyWhenPresent("srcDataSourceId", record::getSrcDataSourceId)
            .map(srcTopic).toPropertyWhenPresent("srcTopic", record::getSrcTopic)
            .map(srcReadMode).toPropertyWhenPresent("srcReadMode", record::getSrcReadMode)
            .map(srcReadFilter).toPropertyWhenPresent("srcReadFilter", record::getSrcReadFilter)
            .map(srcReadShardKey).toPropertyWhenPresent("srcReadShardKey", record::getSrcReadShardKey)
            .map(srcShardingNum).toPropertyWhenPresent("srcShardingNum", record::getSrcShardingNum)
            .map(destDataSourceType).toPropertyWhenPresent("destDataSourceType", record::getDestDataSourceType)
            .map(destDataSourceId).toPropertyWhenPresent("destDataSourceId", record::getDestDataSourceId)
            .map(destTopic).toPropertyWhenPresent("destTopic", record::getDestTopic)
            .map(destTable).toPropertyWhenPresent("destTable", record::getDestTable)
            .map(destWriteMode).toPropertyWhenPresent("destWriteMode", record::getDestWriteMode)
            .map(destBeforeWrite).toPropertyWhenPresent("destBeforeWrite", record::getDestBeforeWrite)
            .map(destAfterWrite).toPropertyWhenPresent("destAfterWrite", record::getDestAfterWrite)
            .map(destProperties).toPropertyWhenPresent("destProperties", record::getDestProperties)
            .map(destShardingNum).toPropertyWhenPresent("destShardingNum", record::getDestShardingNum)
            .map(destBulkNum).toPropertyWhenPresent("destBulkNum", record::getDestBulkNum)
            .map(contentHash).toPropertyWhenPresent("contentHash", record::getContentHash)
            .map(mergeSql).toPropertyWhenPresent("mergeSql", record::getMergeSql)
            .map(scriptSelectColumns).toPropertyWhenPresent("scriptSelectColumns", record::getScriptSelectColumns)
            .map(scriptKeyColumns).toPropertyWhenPresent("scriptKeyColumns", record::getScriptKeyColumns)
            .map(scriptMergeSqlParam).toPropertyWhenPresent("scriptMergeSqlParam", record::getScriptMergeSqlParam)
            .map(configMode).toPropertyWhenPresent("configMode", record::getConfigMode)
            .map(srcTables).toPropertyWhenPresent("srcTables", record::getSrcTables)
            .map(srcColumns).toPropertyWhenPresent("srcColumns", record::getSrcColumns)
            .map(srcQuery).toPropertyWhenPresent("srcQuery", record::getSrcQuery)
            .map(destColumns).toPropertyWhenPresent("destColumns", record::getDestColumns)
            .map(scriptMergeSql).toPropertyWhenPresent("scriptMergeSql", record::getScriptMergeSql)
            .map(scriptQuery).toPropertyWhenPresent("scriptQuery", record::getScriptQuery)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default Optional<DIJobContent> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default List<DIJobContent> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default List<DIJobContent> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default Optional<DIJobContent> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, DIJobContent, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    static UpdateDSL<UpdateModel> updateAllColumns(DIJobContent record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(jobId).equalTo(record::getJobId)
                .set(editable).equalTo(record::getEditable)
                .set(version).equalTo(record::getVersion)
                .set(direct).equalTo(record::getDirect)
                .set(srcDataSourceType).equalTo(record::getSrcDataSourceType)
                .set(srcDataSourceId).equalTo(record::getSrcDataSourceId)
                .set(srcTopic).equalTo(record::getSrcTopic)
                .set(srcReadMode).equalTo(record::getSrcReadMode)
                .set(srcReadFilter).equalTo(record::getSrcReadFilter)
                .set(srcReadShardKey).equalTo(record::getSrcReadShardKey)
                .set(srcShardingNum).equalTo(record::getSrcShardingNum)
                .set(destDataSourceType).equalTo(record::getDestDataSourceType)
                .set(destDataSourceId).equalTo(record::getDestDataSourceId)
                .set(destTopic).equalTo(record::getDestTopic)
                .set(destTable).equalTo(record::getDestTable)
                .set(destWriteMode).equalTo(record::getDestWriteMode)
                .set(destBeforeWrite).equalTo(record::getDestBeforeWrite)
                .set(destAfterWrite).equalTo(record::getDestAfterWrite)
                .set(destProperties).equalTo(record::getDestProperties)
                .set(destShardingNum).equalTo(record::getDestShardingNum)
                .set(destBulkNum).equalTo(record::getDestBulkNum)
                .set(contentHash).equalTo(record::getContentHash)
                .set(mergeSql).equalTo(record::getMergeSql)
                .set(scriptSelectColumns).equalTo(record::getScriptSelectColumns)
                .set(scriptKeyColumns).equalTo(record::getScriptKeyColumns)
                .set(scriptMergeSqlParam).equalTo(record::getScriptMergeSqlParam)
                .set(configMode).equalTo(record::getConfigMode)
                .set(srcTables).equalTo(record::getSrcTables)
                .set(srcColumns).equalTo(record::getSrcColumns)
                .set(srcQuery).equalTo(record::getSrcQuery)
                .set(destColumns).equalTo(record::getDestColumns)
                .set(scriptMergeSql).equalTo(record::getScriptMergeSql)
                .set(scriptQuery).equalTo(record::getScriptQuery);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DIJobContent record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(jobId).equalToWhenPresent(record::getJobId)
                .set(editable).equalToWhenPresent(record::getEditable)
                .set(version).equalToWhenPresent(record::getVersion)
                .set(direct).equalToWhenPresent(record::getDirect)
                .set(srcDataSourceType).equalToWhenPresent(record::getSrcDataSourceType)
                .set(srcDataSourceId).equalToWhenPresent(record::getSrcDataSourceId)
                .set(srcTopic).equalToWhenPresent(record::getSrcTopic)
                .set(srcReadMode).equalToWhenPresent(record::getSrcReadMode)
                .set(srcReadFilter).equalToWhenPresent(record::getSrcReadFilter)
                .set(srcReadShardKey).equalToWhenPresent(record::getSrcReadShardKey)
                .set(srcShardingNum).equalToWhenPresent(record::getSrcShardingNum)
                .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
                .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
                .set(destTopic).equalToWhenPresent(record::getDestTopic)
                .set(destTable).equalToWhenPresent(record::getDestTable)
                .set(destWriteMode).equalToWhenPresent(record::getDestWriteMode)
                .set(destBeforeWrite).equalToWhenPresent(record::getDestBeforeWrite)
                .set(destAfterWrite).equalToWhenPresent(record::getDestAfterWrite)
                .set(destProperties).equalToWhenPresent(record::getDestProperties)
                .set(destShardingNum).equalToWhenPresent(record::getDestShardingNum)
                .set(destBulkNum).equalToWhenPresent(record::getDestBulkNum)
                .set(contentHash).equalToWhenPresent(record::getContentHash)
                .set(mergeSql).equalToWhenPresent(record::getMergeSql)
                .set(scriptSelectColumns).equalToWhenPresent(record::getScriptSelectColumns)
                .set(scriptKeyColumns).equalToWhenPresent(record::getScriptKeyColumns)
                .set(scriptMergeSqlParam).equalToWhenPresent(record::getScriptMergeSqlParam)
                .set(configMode).equalToWhenPresent(record::getConfigMode)
                .set(srcTables).equalToWhenPresent(record::getSrcTables)
                .set(srcColumns).equalToWhenPresent(record::getSrcColumns)
                .set(srcQuery).equalToWhenPresent(record::getSrcQuery)
                .set(destColumns).equalToWhenPresent(record::getDestColumns)
                .set(scriptMergeSql).equalToWhenPresent(record::getScriptMergeSql)
                .set(scriptQuery).equalToWhenPresent(record::getScriptQuery);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int updateByPrimaryKey(DIJobContent record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(jobId).equalTo(record::getJobId)
            .set(editable).equalTo(record::getEditable)
            .set(version).equalTo(record::getVersion)
            .set(direct).equalTo(record::getDirect)
            .set(srcDataSourceType).equalTo(record::getSrcDataSourceType)
            .set(srcDataSourceId).equalTo(record::getSrcDataSourceId)
            .set(srcTopic).equalTo(record::getSrcTopic)
            .set(srcReadMode).equalTo(record::getSrcReadMode)
            .set(srcReadFilter).equalTo(record::getSrcReadFilter)
            .set(srcReadShardKey).equalTo(record::getSrcReadShardKey)
            .set(srcShardingNum).equalTo(record::getSrcShardingNum)
            .set(destDataSourceType).equalTo(record::getDestDataSourceType)
            .set(destDataSourceId).equalTo(record::getDestDataSourceId)
            .set(destTopic).equalTo(record::getDestTopic)
            .set(destTable).equalTo(record::getDestTable)
            .set(destWriteMode).equalTo(record::getDestWriteMode)
            .set(destBeforeWrite).equalTo(record::getDestBeforeWrite)
            .set(destAfterWrite).equalTo(record::getDestAfterWrite)
            .set(destProperties).equalTo(record::getDestProperties)
            .set(destShardingNum).equalTo(record::getDestShardingNum)
            .set(destBulkNum).equalTo(record::getDestBulkNum)
            .set(contentHash).equalTo(record::getContentHash)
            .set(mergeSql).equalTo(record::getMergeSql)
            .set(scriptSelectColumns).equalTo(record::getScriptSelectColumns)
            .set(scriptKeyColumns).equalTo(record::getScriptKeyColumns)
            .set(scriptMergeSqlParam).equalTo(record::getScriptMergeSqlParam)
            .set(configMode).equalTo(record::getConfigMode)
            .set(srcTables).equalTo(record::getSrcTables)
            .set(srcColumns).equalTo(record::getSrcColumns)
            .set(srcQuery).equalTo(record::getSrcQuery)
            .set(destColumns).equalTo(record::getDestColumns)
            .set(scriptMergeSql).equalTo(record::getScriptMergeSql)
            .set(scriptQuery).equalTo(record::getScriptQuery)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: dev_job_content_di")
    default int updateByPrimaryKeySelective(DIJobContent record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(jobId).equalToWhenPresent(record::getJobId)
            .set(editable).equalToWhenPresent(record::getEditable)
            .set(version).equalToWhenPresent(record::getVersion)
            .set(direct).equalToWhenPresent(record::getDirect)
            .set(srcDataSourceType).equalToWhenPresent(record::getSrcDataSourceType)
            .set(srcDataSourceId).equalToWhenPresent(record::getSrcDataSourceId)
            .set(srcTopic).equalToWhenPresent(record::getSrcTopic)
            .set(srcReadMode).equalToWhenPresent(record::getSrcReadMode)
            .set(srcReadFilter).equalToWhenPresent(record::getSrcReadFilter)
            .set(srcReadShardKey).equalToWhenPresent(record::getSrcReadShardKey)
            .set(srcShardingNum).equalToWhenPresent(record::getSrcShardingNum)
            .set(destDataSourceType).equalToWhenPresent(record::getDestDataSourceType)
            .set(destDataSourceId).equalToWhenPresent(record::getDestDataSourceId)
            .set(destTopic).equalToWhenPresent(record::getDestTopic)
            .set(destTable).equalToWhenPresent(record::getDestTable)
            .set(destWriteMode).equalToWhenPresent(record::getDestWriteMode)
            .set(destBeforeWrite).equalToWhenPresent(record::getDestBeforeWrite)
            .set(destAfterWrite).equalToWhenPresent(record::getDestAfterWrite)
            .set(destProperties).equalToWhenPresent(record::getDestProperties)
            .set(destShardingNum).equalToWhenPresent(record::getDestShardingNum)
            .set(destBulkNum).equalToWhenPresent(record::getDestBulkNum)
            .set(contentHash).equalToWhenPresent(record::getContentHash)
            .set(mergeSql).equalToWhenPresent(record::getMergeSql)
            .set(scriptSelectColumns).equalToWhenPresent(record::getScriptSelectColumns)
            .set(scriptKeyColumns).equalToWhenPresent(record::getScriptKeyColumns)
            .set(scriptMergeSqlParam).equalToWhenPresent(record::getScriptMergeSqlParam)
            .set(configMode).equalToWhenPresent(record::getConfigMode)
            .set(srcTables).equalToWhenPresent(record::getSrcTables)
            .set(srcColumns).equalToWhenPresent(record::getSrcColumns)
            .set(srcQuery).equalToWhenPresent(record::getSrcQuery)
            .set(destColumns).equalToWhenPresent(record::getDestColumns)
            .set(scriptMergeSql).equalToWhenPresent(record::getScriptMergeSql)
            .set(scriptQuery).equalToWhenPresent(record::getScriptQuery)
            .where(id, isEqualTo(record::getId))
        );
    }
}