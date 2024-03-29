package cn.zhengcaiyun.idata.user.dal.dao.auth;

import cn.zhengcaiyun.idata.user.dal.model.auth.AuthResource;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;

import static cn.zhengcaiyun.idata.user.dal.dao.auth.AuthResourceDynamicSqlSupport.*;

@Mapper
public interface AuthResourceCustomizeDao {
    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, authRecordId, policyRecordId, resourceType, resources);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<AuthResource> insertStatement);

    default int insertMultiple(AuthResource... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<AuthResource> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, AUTH_RESOURCE, c ->
                c.map(creator).toProperty("creator")
                        .map(editor).toProperty("editor")
                        .map(authRecordId).toProperty("authRecordId")
                        .map(policyRecordId).toProperty("policyRecordId")
                        .map(resourceType).toProperty("resourceType")
                        .map(resources).toProperty("resources")
        );
    }
}
