package cn.zhengcaiyun.idata.user.dal.dao;

import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import cn.zhengcaiyun.idata.user.dal.model.UacUser;
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
public interface UacUserDao {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, username, sysAdmin, authType, password, nickname, employeeId, department, realName, avatar, email, mobile);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<UacUser> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UacUserResult")
    Optional<UacUser> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UacUserResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="del", property="del", jdbcType=JdbcType.TINYINT),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="edit_time", property="editTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_admin", property="sysAdmin", jdbcType=JdbcType.TINYINT),
        @Result(column="auth_type", property="authType", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="nickname", property="nickname", jdbcType=JdbcType.VARCHAR),
        @Result(column="employee_id", property="employeeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="department", property="department", jdbcType=JdbcType.VARCHAR),
        @Result(column="real_name", property="realName", jdbcType=JdbcType.VARCHAR),
        @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="mobile", property="mobile", jdbcType=JdbcType.VARCHAR)
    })
    List<UacUser> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int insert(UacUser record) {
        return MyBatis3Utils.insert(this::insert, record, uacUser, c ->
            c.map(del).toProperty("del")
            .map(creator).toProperty("creator")
            .map(createTime).toProperty("createTime")
            .map(editor).toProperty("editor")
            .map(editTime).toProperty("editTime")
            .map(username).toProperty("username")
            .map(sysAdmin).toProperty("sysAdmin")
            .map(authType).toProperty("authType")
            .map(password).toProperty("password")
            .map(nickname).toProperty("nickname")
            .map(employeeId).toProperty("employeeId")
            .map(department).toProperty("department")
            .map(realName).toProperty("realName")
            .map(avatar).toProperty("avatar")
            .map(email).toProperty("email")
            .map(mobile).toProperty("mobile")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int insertSelective(UacUser record) {
        return MyBatis3Utils.insert(this::insert, record, uacUser, c ->
            c.map(del).toPropertyWhenPresent("del", record::getDel)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(editor).toPropertyWhenPresent("editor", record::getEditor)
            .map(editTime).toPropertyWhenPresent("editTime", record::getEditTime)
            .map(username).toPropertyWhenPresent("username", record::getUsername)
            .map(sysAdmin).toPropertyWhenPresent("sysAdmin", record::getSysAdmin)
            .map(authType).toPropertyWhenPresent("authType", record::getAuthType)
            .map(password).toPropertyWhenPresent("password", record::getPassword)
            .map(nickname).toPropertyWhenPresent("nickname", record::getNickname)
            .map(employeeId).toPropertyWhenPresent("employeeId", record::getEmployeeId)
            .map(department).toPropertyWhenPresent("department", record::getDepartment)
            .map(realName).toPropertyWhenPresent("realName", record::getRealName)
            .map(avatar).toPropertyWhenPresent("avatar", record::getAvatar)
            .map(email).toPropertyWhenPresent("email", record::getEmail)
            .map(mobile).toPropertyWhenPresent("mobile", record::getMobile)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default Optional<UacUser> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default List<UacUser> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default List<UacUser> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default Optional<UacUser> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, uacUser, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    static UpdateDSL<UpdateModel> updateAllColumns(UacUser record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalTo(record::getDel)
                .set(creator).equalTo(record::getCreator)
                .set(createTime).equalTo(record::getCreateTime)
                .set(editor).equalTo(record::getEditor)
                .set(editTime).equalTo(record::getEditTime)
                .set(username).equalTo(record::getUsername)
                .set(sysAdmin).equalTo(record::getSysAdmin)
                .set(authType).equalTo(record::getAuthType)
                .set(password).equalTo(record::getPassword)
                .set(nickname).equalTo(record::getNickname)
                .set(employeeId).equalTo(record::getEmployeeId)
                .set(department).equalTo(record::getDepartment)
                .set(realName).equalTo(record::getRealName)
                .set(avatar).equalTo(record::getAvatar)
                .set(email).equalTo(record::getEmail)
                .set(mobile).equalTo(record::getMobile);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UacUser record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(del).equalToWhenPresent(record::getDel)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(editor).equalToWhenPresent(record::getEditor)
                .set(editTime).equalToWhenPresent(record::getEditTime)
                .set(username).equalToWhenPresent(record::getUsername)
                .set(sysAdmin).equalToWhenPresent(record::getSysAdmin)
                .set(authType).equalToWhenPresent(record::getAuthType)
                .set(password).equalToWhenPresent(record::getPassword)
                .set(nickname).equalToWhenPresent(record::getNickname)
                .set(employeeId).equalToWhenPresent(record::getEmployeeId)
                .set(department).equalToWhenPresent(record::getDepartment)
                .set(realName).equalToWhenPresent(record::getRealName)
                .set(avatar).equalToWhenPresent(record::getAvatar)
                .set(email).equalToWhenPresent(record::getEmail)
                .set(mobile).equalToWhenPresent(record::getMobile);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int updateByPrimaryKey(UacUser record) {
        return update(c ->
            c.set(del).equalTo(record::getDel)
            .set(creator).equalTo(record::getCreator)
            .set(createTime).equalTo(record::getCreateTime)
            .set(editor).equalTo(record::getEditor)
            .set(editTime).equalTo(record::getEditTime)
            .set(username).equalTo(record::getUsername)
            .set(sysAdmin).equalTo(record::getSysAdmin)
            .set(authType).equalTo(record::getAuthType)
            .set(password).equalTo(record::getPassword)
            .set(nickname).equalTo(record::getNickname)
            .set(employeeId).equalTo(record::getEmployeeId)
            .set(department).equalTo(record::getDepartment)
            .set(realName).equalTo(record::getRealName)
            .set(avatar).equalTo(record::getAvatar)
            .set(email).equalTo(record::getEmail)
            .set(mobile).equalTo(record::getMobile)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: uac_user")
    default int updateByPrimaryKeySelective(UacUser record) {
        return update(c ->
            c.set(del).equalToWhenPresent(record::getDel)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(editor).equalToWhenPresent(record::getEditor)
            .set(editTime).equalToWhenPresent(record::getEditTime)
            .set(username).equalToWhenPresent(record::getUsername)
            .set(sysAdmin).equalToWhenPresent(record::getSysAdmin)
            .set(authType).equalToWhenPresent(record::getAuthType)
            .set(password).equalToWhenPresent(record::getPassword)
            .set(nickname).equalToWhenPresent(record::getNickname)
            .set(employeeId).equalToWhenPresent(record::getEmployeeId)
            .set(department).equalToWhenPresent(record::getDepartment)
            .set(realName).equalToWhenPresent(record::getRealName)
            .set(avatar).equalToWhenPresent(record::getAvatar)
            .set(email).equalToWhenPresent(record::getEmail)
            .set(mobile).equalToWhenPresent(record::getMobile)
            .where(id, isEqualTo(record::getId))
        );
    }
}