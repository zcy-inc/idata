package cn.zhengcaiyun.idata.user.dal.dao;

import cn.zhengcaiyun.idata.user.dal.model.GroupUserRelation;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;

import static cn.zhengcaiyun.idata.user.dal.dao.GroupUserRelationDynamicSqlSupport.*;

@Mapper
public interface GroupUserRelationCustomizeDao {

    BasicColumn[] selectList = BasicColumn.columnList(id, del, creator, createTime, editor, editTime, groupId, userId);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<GroupUserRelation> insertStatement);

    default int insertMultiple(GroupUserRelation... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<GroupUserRelation> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, GROUP_USER_RELATION, c ->
                c.map(creator).toProperty("creator")
                        .map(editor).toProperty("editor")
                        .map(groupId).toProperty("groupId")
                        .map(userId).toProperty("userId")
        );
    }
}
