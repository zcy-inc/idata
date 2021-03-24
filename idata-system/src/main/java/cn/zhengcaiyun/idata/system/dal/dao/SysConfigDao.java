package cn.zhengcaiyun.idata.system.dal.dao;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.*;

import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
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
public interface SysConfigDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, createTime, editTime, keyOne, valueOne);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<SysConfig> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SysConfigResult")
    Optional<SysConfig> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SysConfigResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="key_one", property="keyOne", jdbcType=JdbcType.VARCHAR),
        @Result(column="value_one", property="valueOne", jdbcType=JdbcType.VARCHAR)
    })
    List<SysConfig> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default int insert(SysConfig record) {
        return MyBatis3Utils.insert(this::insert, record, sysConfig, c ->
            c.map(del).toProperty("del")
            .map(createTime).toProperty("createTime")
            .map(editTime).toProperty("editTime")
            .map(keyOne).toProperty("keyOne")
            .map(valueOne).toProperty("valueOne")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default int insertSelective(SysConfig record) {
        return MyBatis3Utils.insert(this::insert, record, sysConfig, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(keyOne).toPropertyWhenPresent("keyOne", record::getKeyOne)
            .map(valueOne).toPropertyWhenPresent("valueOne", record::getValueOne)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default Optional<SysConfig> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default List<SysConfig> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default List<SysConfig> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, sysConfig, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    static UpdateDSL<UpdateModel> updateAllColumns(SysConfig record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editTime).equalTo(record::getEditTime)
                .set(keyOne).equalTo(record::getKeyOne)
                .set(valueOne).equalTo(record::getValueOne);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_config")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SysConfig record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(keyOne).equalToWhenPresent(record::getKeyOne)
                .set(valueOne).equalToWhenPresent(record::getValueOne);
    }
}