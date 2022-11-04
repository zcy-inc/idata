package cn.zhengcaiyun.idata.system.dal.dao;

import static cn.zhengcaiyun.idata.system.dal.dao.SysResourceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.system.dal.model.SysResource;
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
public interface SysResourceDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, createTime, editTime, resourceCode, resourceName, resourceType, parentCode, resourceUrlPath);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<SysResource> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SysResourceResult")
    Optional<SysResource> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SysResourceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="resource_code", property="resourceCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="resource_name", property="resourceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="resource_type", property="resourceType", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_code", property="parentCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="resource_url_path", property="resourceUrlPath", jdbcType=JdbcType.VARCHAR)
    })
    List<SysResource> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int insert(SysResource record) {
        return MyBatis3Utils.insert(this::insert, record, sysResource, c ->
            c.map(del).toProperty("del")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(resourceCode).toProperty("resourceCode")
            .map(resourceName).toProperty("resourceName")
            .map(resourceType).toProperty("resourceType")
            .map(parentCode).toProperty("parentCode")
            .map(resourceUrlPath).toProperty("resourceUrlPath")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int insertSelective(SysResource record) {
        return MyBatis3Utils.insert(this::insert, record, sysResource, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(resourceCode).toPropertyWhenPresent("resourceCode", record::getResourceCode)
            .map(resourceName).toPropertyWhenPresent("resourceName", record::getResourceName)
            .map(resourceType).toPropertyWhenPresent("resourceType", record::getResourceType)
            .map(parentCode).toPropertyWhenPresent("parentCode", record::getParentCode)
            .map(resourceUrlPath).toPropertyWhenPresent("resourceUrlPath", record::getResourceUrlPath)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default Optional<SysResource> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default List<SysResource> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default List<SysResource> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default Optional<SysResource> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, sysResource, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    static UpdateDSL<UpdateModel> updateAllColumns(SysResource record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(resourceCode).equalTo(record::getResourceCode)
                .set(resourceName).equalTo(record::getResourceName)
                .set(resourceType).equalTo(record::getResourceType)
                .set(parentCode).equalTo(record::getParentCode)
                .set(resourceUrlPath).equalTo(record::getResourceUrlPath);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SysResource record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(resourceCode).equalToWhenPresent(record::getResourceCode)
                .set(resourceName).equalToWhenPresent(record::getResourceName)
                .set(resourceType).equalToWhenPresent(record::getResourceType)
                .set(parentCode).equalToWhenPresent(record::getParentCode)
                .set(resourceUrlPath).equalToWhenPresent(record::getResourceUrlPath);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int updateByPrimaryKey(SysResource record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editTime).equalTo(record::getEditTime)
            .set(resourceCode).equalTo(record::getResourceCode)
            .set(resourceName).equalTo(record::getResourceName)
            .set(resourceType).equalTo(record::getResourceType)
            .set(parentCode).equalTo(record::getParentCode)
            .set(resourceUrlPath).equalTo(record::getResourceUrlPath)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_resource")
    default int updateByPrimaryKeySelective(SysResource record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(resourceCode).equalToWhenPresent(record::getResourceCode)
            .set(resourceName).equalToWhenPresent(record::getResourceName)
            .set(resourceType).equalToWhenPresent(record::getResourceType)
            .set(parentCode).equalToWhenPresent(record::getParentCode)
            .set(resourceUrlPath).equalToWhenPresent(record::getResourceUrlPath)
            .where(id, isEqualTo(record::getId))
        );
    }
}