package cn.zhengcaiyun.idata.user.dal.dao;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAccessDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.user.dal.model.UacAccess;
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
public interface UacAccessDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, createTime, editTime, accessCode, accessType, accessKey);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<UacAccess> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UacAccessResult")
    Optional<UacAccess> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UacAccessResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.SMALLINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="access_code", property="accessCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="access_type", property="accessType", jdbcType=JdbcType.VARCHAR),
        @Result(column="access_key", property="accessKey", jdbcType=JdbcType.VARCHAR)
    })
    List<UacAccess> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int insert(UacAccess record) {
        return MyBatis3Utils.insert(this::insert, record, uacAccess, c ->
            c.map(del).toProperty("del")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(accessCode).toProperty("accessCode")
            .map(accessType).toProperty("accessType")
            .map(accessKey).toProperty("accessKey")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int insertSelective(UacAccess record) {
        return MyBatis3Utils.insert(this::insert, record, uacAccess, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(accessCode).toPropertyWhenPresent("accessCode", record::getAccessCode)
            .map(accessType).toPropertyWhenPresent("accessType", record::getAccessType)
            .map(accessKey).toPropertyWhenPresent("accessKey", record::getAccessKey)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default Optional<UacAccess> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default List<UacAccess> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default List<UacAccess> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default Optional<UacAccess> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, uacAccess, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    static UpdateDSL<UpdateModel> updateAllColumns(UacAccess record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(accessCode).equalTo(record::getAccessCode)
                .set(accessType).equalTo(record::getAccessType)
                .set(accessKey).equalTo(record::getAccessKey);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UacAccess record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(accessCode).equalToWhenPresent(record::getAccessCode)
                .set(accessType).equalToWhenPresent(record::getAccessType)
                .set(accessKey).equalToWhenPresent(record::getAccessKey);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int updateByPrimaryKey(UacAccess record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editTime).equalTo(record::getEditTime)
            .set(accessCode).equalTo(record::getAccessCode)
            .set(accessType).equalTo(record::getAccessType)
            .set(accessKey).equalTo(record::getAccessKey)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_access")
    default int updateByPrimaryKeySelective(UacAccess record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(accessCode).equalToWhenPresent(record::getAccessCode)
            .set(accessType).equalToWhenPresent(record::getAccessType)
            .set(accessKey).equalToWhenPresent(record::getAccessKey)
            .where(id, isEqualTo(record::getId))
        );
    }
}