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

import java.text.ParseException;
import java.util.*;

public class SparkSqlUtil {

    public static String[] splitToMultiSql(String multiSql) {
        return (multiSql + " ").replaceAll("\n", " \n").split("; ", -1);
    }

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

//    public static void main(String[] args) {
//        String sql = "select \n" +
//                " a.month_data                      --bigint    comment '项目月份'\n" +
//                ",a.district_code                   --string    comment '区划code' \n" +
//                ",a.district_name                   --string    comment '区划名称' \n" +
//                ",sum(a.liv_cnt)                         --bigint    comment '不见面开评标项目数'\n" +
//                ",sum(a.liv_cnt_all)                     --bigint    comment '不见面开评标项目总数'\n" +
//                ",sum(a.intention_cnt)                   --bigint    comment '采购意向公开满足30天项目数'\n" +
//                ",sum(a.intention_cnt_all)               --bigint    comment '采购意向公开满足30天项目总数'           \n" +
//                ",sum(a.sigh_cnt)                        --bigint    comment '7日内合同备案项目数'\n" +
//                ",sum(a.sigh_cnt_all)                    --bigint    comment '7日内合同备案项目总数'\n" +
//                ",sum(a.performance_cnt)                 --bigint    comment '履约验收公告发布项目数'\n" +
//                ",sum(a.performance_cnt_all)             --bigint    comment '履约验收公告发布项目总数'\n" +
//                ",sum(a.reply_cnt)                       --bigint    comment '7日内质疑答复项目数'\n" +
//                ",sum(a.reply_cnt_all)                   --bigint    comment '7日内质疑答复项目总数'\n" +
//                ",sum(a.publish_cnt)                     --bigint    comment '2日内中标通知项目数'\n" +
//                ",sum(a.publish_cnt_all)                 --bigint    comment '2日内中标通知项目总数'\n" +
//                "from ads.ads_dtjk_trade_district_purchaser a\n" +
//                "group by 1,2,3";
//        String[] sqls = sql.split("select|from");
//        List<String> newSqlList = new ArrayList<>(Arrays.asList(sqls)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
//        String[] columns = newSqlList.get(0).split("\\n|\\t|,");
//        List<String> columnsList = new ArrayList<>(Arrays.asList(columns)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
//        System.out.println(sqls);
//    }

    public static void main(String[] args) throws ParseException {
        String sql = "--删除临时表\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_item_unit_map;\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_catagory_unit_detail;\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_unit_exchanges;\n" +
                "\n" +
                "--商品自定义转换系数\n" +
                "CREATE TABLE IF NOT EXISTS tmp.tmp_4303_item_unit_map STORED AS ORC AS\n" +
                "select item_id\n" +
                "      ,cast(get_json_object(ext_json, '$.CATE_STANDARD_UNIT_EXCHANGE_RATE') as DOUBLE) as exchange_rate --标准单位转换系数\n" +
                "      ,get_json_object(ext_json, '$.CATE_STANDARD_UNIT') as standard_unit --商品自定义标准单位\n" +
                "      ,get_json_object(ext_json, '$.ZCY_PRODUCT_ID') as product_id --产品ID\n" +
                "  from ods.ods_db_item_zcy_item_ext_info\n" +
                " where ext_json like '%CATE_STANDARD_UNIT_EXCHANGE_RATE%'\n" +
                "   and status = 1;\n" +
                "\n" +
                "\n" +
                "--没有商品自定义转换系数，通过类目标准单位转换关系\n" +
                "CREATE TABLE IF NOT EXISTS tmp.tmp_4303_catagory_unit_detail STORED AS ORC AS\n" +
                "select t1.id            as item_id\n" +
                "      ,get_json_object(t1.extra_json, '$.unit')        as item_unit --商品单位\n" +
                "      ,get_json_object(t2.ext_json, '$.STANDARD_UNIT') as standard_unit --类目标准单位\n" +
                "      ,get_json_object(t3.ext_json, '$.ZCY_PRODUCT_ID') as product_id --产品ID\n" +
                "  from ods.ods_db_item_parana_items t1\n" +
                "  join ods.ods_db_item_standard_parana_category_attributes t2 \n" +
                "    on t1.category_id = t2.category_id\n" +
                "  left join ods.ods_db_item_zcy_item_ext_info t3\n" +
                "    on t1.id = t3.item_id\n" +
                " where t2.attr_key = '计量单位'\n" +
                "   and get_json_object(t1.extra_json, '$.unit') is not null\n" +
                "   and get_json_object(t2.ext_json, '$.STANDARD_UNIT') is not null;\n" +
                "\n" +
                "--单位转换关系表，加工出： 商品单位 * 转换系数 = 标准单位 的关系表\n" +
                "--单位转换关系表只有一条，如：斤<-->公斤，需要自己加工出公斤<-->斤的关系\n" +
                "CREATE TABLE IF NOT EXISTS tmp.tmp_4303_unit_exchanges STORED AS ORC AS\n" +
                "select to_unit          as item_unit      --商品单位\n" +
                "      ,from_unit        as standard_unit  --标准单位\n" +
                "      ,cast(exchange_rate as DOUBLE)    as exchange_rate  --转换系数X，关系：商品单位 * X = 标准单位\n" +
                "      ,cast(exchange_rate as DOUBLE)    as molecular      --转换系数分子\n" +
                "      ,1                as denominator    --转换系数分母\n" +
                "  from ods.ods_db_item_standard_zcy_unit_exchanges\n" +
                "union\n" +
                "select from_unit        as item_unit      --商品单位\n" +
                "      ,to_unit          as standard_unit  --标准单位\n" +
                "      ,cast(1/exchange_rate as DOUBLE)  as exchange_rate  --转换系数X，关系：商品单位 * X = 标准单位\n" +
                "      ,1                as molecular      --转换系数分子\n" +
                "      ,cast(exchange_rate as DOUBLE)    as denominator    --转换系数分母\n" +
                "  from ods.ods_db_item_standard_zcy_unit_exchanges;\n" +
                "\n" +
                "INSERT OVERWRITE TABLE dim.dim_item_standard_unit_mapping\n" +
                "--商品通过类目标准单位获取转换系数\n" +
                "select t1.item_id              as item_id    --商品ID\n" +
                "      ,t1.item_unit            as item_unit  --商品单位\n" +
                "      ,t1.standard_unit        as standard_unit --标准单位\n" +
                "      ,t2.exchange_rate        as exchange_rate --转换系数\n" +
                "      ,t2.molecular            as molecular --转换系数分子\n" +
                "      ,t2.denominator          as denominator --转换系数分母\n" +
                "      ,'类目'                   as type --通过类目获取\n" +
                "      ,t1.product_id           as product_id --产品ID\n" +
                "  from tmp.tmp_4303_catagory_unit_detail t1\n" +
                "  join tmp.tmp_4303_unit_exchanges t2\n" +
                "    on t1.item_unit = t2.item_unit\n" +
                "   and t1.standard_unit = t2.standard_unit\n" +
                "  left join tmp.tmp_4303_item_unit_map t3\n" +
                "    on t1.item_id = t3.item_id\n" +
                " where t3.item_id is null --优先取商品的自定义转换关系\n" +
                "union all\n" +
                "--获取商品自定义转换系数\n" +
                "select t1.item_id              as item_id    --商品ID\n" +
                "      ,null                    as item_unit  --商品单位\n" +
                "      ,t1.standard_unit        as standard_unit --标准单位\n" +
                "      ,t1.exchange_rate        as exchange_rate --转换系数\n" +
                "      ,t1.exchange_rate        as molecular      --转换系数分子\n" +
                "      ,1                       as denominator    --转换系数分母\n" +
                "      ,'自定义'                 as type --通过类目获取\n" +
                "      ,t1.product_id           as product_id --产品ID\n" +
                "  from tmp.tmp_4303_item_unit_map t1;\n" +
                "\n" +
                "--删除临时表\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_item_unit_map;\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_catagory_unit_detail;\n" +
                "DROP TABLE IF EXISTS tmp.tmp_4303_unit_exchanges;";
        Map<String, String> customParams = new HashMap<>();
//        String sql = "select ${dt} as a, ${ bizquarter } c from tmp.tmp_table_1 where b = ${day-1d};\nselect ${bizquarter}!!!${bizyear} as d,  {0} !!!${day2} as e from tmp.tmp_table_2";
        String newSql = SqlDynamicParamTool.replaceDynamicParam(sql, customParams, (d_param) -> "d-202203");
        String[] multiSqlArray = SparkSqlUtil.splitToMultiSql(newSql);

        List<String> fromTables;
        try {
            String singleSql = "";
            fromTables = SparkSqlUtil.getFromTables(singleSql, null);
            for (String tempTbl : fromTables) {
                System.out.println(tempTbl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        String add_db = SparkSqlUtil.addDatabaseEnv(sql,"stag_");
//        Map<String, String> map = SparkSqlUtil.insertErase(newSql);
//        System.out.println(sql);

//        System.out.println(newSql);
//        for (String tempSql : multiSqlArray) {
//            System.out.println(tempSql);
//        }
    }
}
