package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseListener;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class SparkSqlUtil {

    public static String addDatabaseEnv(String sql, String env) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(sql), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        AddDatabaseEnvListener listener = new AddDatabaseEnvListener(tokenStream, env);
        walker.walk(listener, parser.statement());
        return listener.rewriter.getText();
    }

    public static Map<String, String> insertErase(String sql) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(sql), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        InsertEraseListener listener = new InsertEraseListener(tokenStream);
        walker.walk(listener, parser.statement());
        Map<String, String> result = new HashMap<>();
        if (listener.insertTable != null) {
            result.put("insertTable", listener.insertTable);
            result.put("selectSql", listener.rewriter.getText());
            result.put("isHasPt", String.valueOf(listener.isHasPt));
            result.put("ptColumns", listener.ptColumns);
        }
        return result;
    }

    public static List<String> getFromTables(String sql, Set<String> tableSet) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(sql), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        SelectTableListener listener = new SelectTableListener(tokenStream);
        walker.walk(listener, parser.statement());
        String parseSql = listener.rewriter.getText();
        if (tableSet == null) {
            tableSet = new HashSet<>(listener.tableList);
        } else {
            tableSet.addAll(listener.tableList);
        }
        if (sql.length() > parseSql.length()) {
            getFromTables(sql.substring(parseSql.length()), tableSet);
        }
        return new ArrayList(tableSet);
    }

    public static List<String> getSelectColumns(List<Map<String, Object>> resultSet) {
        int longestIndex = 0;
        int longestSize = 0;
        for (int i = 0; i < resultSet.size(); i++) {
            if (resultSet.get(i).size() > longestSize) {
                longestIndex = i;
                longestSize = resultSet.get(i).size();
            }
        }
        Set<String> columns = resultSet.get(longestIndex).keySet();
        List<String> echo = new ArrayList<>(columns);
        return echo;
    }

    public static class AddDatabaseEnvListener extends SparkSqlBaseListener {

        public final List<String> TABLE_LIST = new ArrayList<>();
        public final TokenStreamRewriter rewriter;

        private final String env;

        public AddDatabaseEnvListener(TokenStream tokenStream, String env) {
            this.rewriter = new TokenStreamRewriter(tokenStream);
            this.env = env;
        }

        @Override
        public void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
            if (ctx.table != null) {
                TABLE_LIST.add(ctx.getText().toLowerCase());
            }
            if (env != null && ctx.db != null) {
                rewriter.insertBefore(ctx.db.start, env);
            }
        }
    }

    public static class InsertEraseListener extends SparkSqlBaseListener {

        public String insertTable = null;
        private String op = null;
        public final TokenStreamRewriter rewriter;
        public boolean isHasPt = false;
        public String ptColumns;

        public InsertEraseListener(TokenStream tokenStream) {
            this.rewriter = new TokenStreamRewriter(tokenStream);
        }

        @Override
        public void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
            if (op != null) {
                insertTable = ctx.getText().toLowerCase();
            }
        }

        @Override
        public void enterPartitionVal(SparkSqlParser.PartitionValContext ctx) {
            String text = ctx.getText().toLowerCase();
            if (StringUtils.isEmpty(text)) {
                return;
            }
            isHasPt = true;
            if (StringUtils.isEmpty(ptColumns)) {
                ptColumns = text;
            } else {
                ptColumns += ("," + text);
            }
        }

        @Override
        public void enterInsertIntoTable(SparkSqlParser.InsertIntoTableContext ctx) {
            op = "INSERT INTO";
            rewriter.delete(ctx.start, ctx.stop);
        }

        @Override
        public void exitInsertIntoTable(SparkSqlParser.InsertIntoTableContext ctx) {
            op = null;
        }

        @Override
        public void enterInsertOverwriteTable(SparkSqlParser.InsertOverwriteTableContext ctx) {
            op = "INSERT OVERWRITE";
            rewriter.delete(ctx.start, ctx.stop);
        }

        @Override
        public void exitInsertOverwriteTable(SparkSqlParser.InsertOverwriteTableContext ctx) {
            op = null;
        }

    }

    public static void main(String[] args) {
        String sql = "select \n" +
                " a.month_data                      --bigint    comment '项目月份'\n" +
                ",a.district_code                   --string    comment '区划code' \n" +
                ",a.district_name                   --string    comment '区划名称' \n" +
                ",sum(a.liv_cnt)                         --bigint    comment '不见面开评标项目数'\n" +
                ",sum(a.liv_cnt_all)                     --bigint    comment '不见面开评标项目总数'\n" +
                ",sum(a.intention_cnt)                   --bigint    comment '采购意向公开满足30天项目数'\n" +
                ",sum(a.intention_cnt_all)               --bigint    comment '采购意向公开满足30天项目总数'           \n" +
                ",sum(a.sigh_cnt)                        --bigint    comment '7日内合同备案项目数'\n" +
                ",sum(a.sigh_cnt_all)                    --bigint    comment '7日内合同备案项目总数'\n" +
                ",sum(a.performance_cnt)                 --bigint    comment '履约验收公告发布项目数'\n" +
                ",sum(a.performance_cnt_all)             --bigint    comment '履约验收公告发布项目总数'\n" +
                ",sum(a.reply_cnt)                       --bigint    comment '7日内质疑答复项目数'\n" +
                ",sum(a.reply_cnt_all)                   --bigint    comment '7日内质疑答复项目总数'\n" +
                ",sum(a.publish_cnt)                     --bigint    comment '2日内中标通知项目数'\n" +
                ",sum(a.publish_cnt_all)                 --bigint    comment '2日内中标通知项目总数'\n" +
                "from ads.ads_dtjk_trade_district_purchaser a\n" +
                "group by 1,2,3";
        String[] sqls = sql.split("select|from");
        List<String> newSqlList = new ArrayList<>(Arrays.asList(sqls)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String[] columns = newSqlList.get(0).split("\\n|\\t|,");
        List<String> columnsList = new ArrayList<>(Arrays.asList(columns)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        System.out.println(sqls);
    }
}
