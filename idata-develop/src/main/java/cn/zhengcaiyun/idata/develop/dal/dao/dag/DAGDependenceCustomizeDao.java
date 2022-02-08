package cn.zhengcaiyun.idata.develop.dal.dao.dag;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGDependence;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.Arrays;
import java.util.Collection;

import static cn.zhengcaiyun.idata.develop.dal.dao.dag.DAGDependenceDynamicSqlSupport.*;

@Mapper
public interface DAGDependenceCustomizeDao {

    BasicColumn[] selectList = BasicColumn.columnList(id, creator, createTime, dagId, prevDagId);

    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<DAGDependence> insertStatement);

    default int insertMultiple(DAGDependence... records) {
        return insertMultiple(Arrays.asList(records));
    }

    default int insertMultiple(Collection<DAGDependence> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, DAG_DEPENDENCE, c ->
                c.map(creator).toProperty("creator")
                        .map(dagId).toProperty("dagId")
                        .map(prevDagId).toProperty("prevDagId")
        );
    }

}