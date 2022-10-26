package cn.zhengcaiyun.idata.connector.util.parse;

import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseVisitor;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import cn.zhengcaiyun.idata.connector.util.model.SqlTypeEnum;
import cn.zhengcaiyun.idata.connector.util.model.TableObj;
import cn.zhengcaiyun.idata.connector.util.model.TableSib;
import cn.zhengcaiyun.idata.connector.util.model.TokenTypeEnum;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SparkSqlAst extends SparkSqlBaseVisitor {
    private TableSib tableSib = new TableSib();
    private SqlTypeEnum curSqlType = SqlTypeEnum.UNKOWN;
    private List<String> withList = new ArrayList<>();
    private List<SqlTypeEnum> outputTypeList = Lists.newArrayList(SqlTypeEnum.INSERT_VALUES, SqlTypeEnum.CREATE_TABLE);
    private List<SqlTypeEnum> inputTypeList = Lists.newArrayList(SqlTypeEnum.INSERT_SELECT, SqlTypeEnum.CREATE_TABLE_AS_SELECT, SqlTypeEnum.SELECT);

    @Override
    public TableSib visit(ParseTree tree) {
        super.visit(tree);
        return tableSib;
    }

    /**
     * create table …… 、create tale …… as select ……
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx) {
        curSqlType = SqlTypeEnum.CREATE_TABLE;
        tableSib.setSqlType(curSqlType);

        super.visitCreateHiveTable(ctx);
        return tableSib;
    }

    /**
     * create table tab1 like tab2
     * 区别于create …… as select……，该语法只建表不拷贝数据
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitCreateTableLike(SparkSqlParser.CreateTableLikeContext ctx) {
        curSqlType = SqlTypeEnum.CREATE_TABLE_AS_LIKE;

        super.visitCreateTableLike(ctx);
        return tableSib;
    }

    @Override
    public TableSib visitQuery(SparkSqlParser.QueryContext ctx) {
        super.visitQuery(ctx);
        return null;
    }

    /**
     * select …… 、with ……
     * insert into/overwrite …… (as select ……)
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitStatementDefault(SparkSqlParser.StatementDefaultContext ctx) {
        String start = ctx.start.getText().toLowerCase();
        curSqlType = SqlTypeEnum.SELECT;

        if ("select".equals(start) || "with".equals(start)) {
            curSqlType = SqlTypeEnum.SELECT;

        } else if ("insert".equals(start)) {
            curSqlType = SqlTypeEnum.INSERT_VALUES;
        }
        tableSib.setSqlType(curSqlType);

        super.visitStatementDefault(ctx);
        return tableSib;
    }

    @Override
    public Object visitNamedQuery(SparkSqlParser.NamedQueryContext ctx) {
        withList.add(ctx.name.getText());
        return super.visitNamedQuery(ctx);
    }

    /**
     * create table ,from ,insert into, join
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
        String db = ctx.db == null ? "" : ctx.db.getText();
        String tableName = ctx.table == null ? "" : ctx.table.getText();

        String full = "".equals(db) ? tableName : db + "." + tableName;
        if (withList.contains(db + full)) {
            return null;
        }

        if (outputTypeList.contains(curSqlType)) {
            tableSib.setOutputTable(new TableObj(db, tableName));

        } else if (inputTypeList.contains(curSqlType)) {
            tableSib.getInputTables().add(new TableObj(db, tableName));
        }
        return null;
    }


    @Override
    public TableSib visitQueryOrganization(SparkSqlParser.QueryOrganizationContext ctx) {
        super.visitQueryOrganization(ctx);
        return null;
    }

    /**
     * insert into table …… (as select ……）
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitInsertIntoTable(SparkSqlParser.InsertIntoTableContext ctx) {
        curSqlType = SqlTypeEnum.INSERT_VALUES;
        tableSib.setSqlType(curSqlType);

        super.visitInsertIntoTable(ctx);
        return null;
    }


    /**
     * insert overwrite …… (as select ……）
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public TableSib visitInsertOverwriteTable(SparkSqlParser.InsertOverwriteTableContext ctx) {
        curSqlType = SqlTypeEnum.INSERT_VALUES;
        tableSib.setSqlType(curSqlType);

        super.visitInsertOverwriteTable(ctx);
        return null;
    }

    @Override
    public TableSib visitQuerySpecification(SparkSqlParser.QuerySpecificationContext ctx) {
        if (curSqlType == SqlTypeEnum.INSERT_VALUES) {
            curSqlType = SqlTypeEnum.INSERT_SELECT;

        } else if (curSqlType == SqlTypeEnum.CREATE_TABLE) {
            curSqlType = SqlTypeEnum.CREATE_TABLE_AS_SELECT;
        }

        tableSib.setSqlType(curSqlType);

        super.visitQuerySpecification(ctx);
        return null;
    }

    @Override
    public TableSib visitInlineTableDefault1(SparkSqlParser.InlineTableDefault1Context ctx) {
        curSqlType = SqlTypeEnum.INSERT_VALUES;
        tableSib.setSqlType(curSqlType);

        super.visitInlineTableDefault1(ctx);
        return null;
    }

}
