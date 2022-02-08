package cn.zhengcaiyun.idata.system.dal.dao;

import static cn.zhengcaiyun.idata.system.dal.dao.SysDutyInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.system.dal.model.SysDutyInfo;
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
public interface SysDutyInfoDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, dutyDefaultPhone, dutyInfo);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<SysDutyInfo> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("SysDutyInfoResult")
    Optional<SysDutyInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="SysDutyInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="duty_default_phone", property="dutyDefaultPhone", jdbcType=JdbcType.VARCHAR),
        @Result(column="duty_info", property="dutyInfo", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<SysDutyInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int insert(SysDutyInfo record) {
        return MyBatis3Utils.insert(this::insert, record, SYS_DUTY_INFO, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(dutyDefaultPhone).toProperty("dutyDefaultPhone")
            .map(dutyInfo).toProperty("dutyInfo")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int insertSelective(SysDutyInfo record) {
        return MyBatis3Utils.insert(this::insert, record, SYS_DUTY_INFO, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(dutyDefaultPhone).toPropertyWhenPresent("dutyDefaultPhone", record::getDutyDefaultPhone)
            .map(dutyInfo).toPropertyWhenPresent("dutyInfo", record::getDutyInfo)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default Optional<SysDutyInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default List<SysDutyInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default List<SysDutyInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default Optional<SysDutyInfo> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, SYS_DUTY_INFO, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    static UpdateDSL<UpdateModel> updateAllColumns(SysDutyInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(dutyDefaultPhone).equalTo(record::getDutyDefaultPhone)
                .set(dutyInfo).equalTo(record::getDutyInfo);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(SysDutyInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(dutyDefaultPhone).equalToWhenPresent(record::getDutyDefaultPhone)
                .set(dutyInfo).equalToWhenPresent(record::getDutyInfo);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int updateByPrimaryKey(SysDutyInfo record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(dutyDefaultPhone).equalTo(record::getDutyDefaultPhone)
            .set(dutyInfo).equalTo(record::getDutyInfo)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: sys_duty_info")
    default int updateByPrimaryKeySelective(SysDutyInfo record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(dutyDefaultPhone).equalToWhenPresent(record::getDutyDefaultPhone)
            .set(dutyInfo).equalToWhenPresent(record::getDutyInfo)
            .where(id, isEqualTo(record::getId))
        );
    }
}