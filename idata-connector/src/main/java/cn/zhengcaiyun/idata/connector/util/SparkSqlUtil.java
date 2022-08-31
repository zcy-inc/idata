package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseListener;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
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
        String sql = "--******************************************************************--\n" +
                "--******功能说明：DWS层日志域前端点击使用信息天增量表\n" +
                "--******输出表：dws.dws_log_front_clk_basic_inc_d\n" +
                "--******开发人员：青禾\n" +
                "--******开发时间：2022-05-24\n" +
                "--******修改记录：\n" +
                "--******变更日期      变更人      变更描述\n" +
                "--******************************************************************--\n" +
                "--*\n" +
                "--* 依赖源表：\n" +
                "--* dwd.dwd_log_front_clk_basic_inc_d\n" +
                "--******************************************************************--\n" +
                "--取页面聚合事，b段值转换\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_url_${bizdate};\n" +
                "create table if not exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_url_${bizdate}  stored as orc as\n" +
                "select\n" +
                " regexp_replace(regexp_replace(url,'^\\https',''),'\\^http','') as url\n" +
                ",utmcnt_a\n" +
                ",utmcnt_b\n" +
                "from dim.dim_log_busi_module_map\n" +
                "where is_url_union=1\n" +
                "group by\n" +
                " regexp_replace(regexp_replace(url,'^\\https',''),'\\^http','')\n" +
                ",utmcnt_a\n" +
                ",utmcnt_b\n" +
                ";\n" +
                "--全局事件，d段值唯一\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizdate};\n" +
                "create table if not exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizdate}  stored as orc as\n" +
                "select\n" +
                " utmcnt_d\n" +
                "from dim.dim_log_evt_map\n" +
                "where is_glbl=1\n" +
                "group by\n" +
                " utmcnt_d\n" +
                ";\n" +
                "\n" +
                "--事件汇总\n" +
                "--1.取出全局事件，构造全局事件code:evt_code=utmcnt_d\n" +
                "--2.取出内置事件，构造内置事件code：log_type=1 ,evt_code=page_in, log_type=3 ,evt_code=page_out\n" +
                "--3.对于事件类型异常值处理：取log_type=2，除了evt_type='click',其他处理为'other'\n" +
                "--4.新增一列:url中去除掉首次出现的传输协议（http/https)\n" +
                "--5.新增一列:排序字段rnk\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizdate};\n" +
                "create table if not exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizdate}  stored as orc as\n" +
                "select\n" +
                " substring(create_time,0,4)  as t_year\n" +
                ",substring(create_time,0,7)  as t_month\n" +
                ",pltfm_type\n" +
                ",busi_catg_code\n" +
                ",busi_catg_name\n" +
                ",busi_module_code\n" +
                ",busi_module_name\n" +
                ",app_type_code\n" +
                ",app_type_name\n" +
                ",if(t2.busi_module_code in ('VACCINE_ZLB_H5','VACCINE_ALI_PAY_H5'),t2.uuid,t2.opt_id)   as opt_id\n" +
                ",user_type_code\n" +
                ",user_type_name\n" +
                ",district_code\n" +
                ",district_name\n" +
                ",district_type\n" +
                ",district_type_id\n" +
                ",district_type_name\n" +
                ",district_level\n" +
                ",district_city_code\n" +
                ",district_city_name\n" +
                ",district_province_code\n" +
                ",district_province_name\n" +
                ",utmcnt_a\n" +
                ",utmcnt_b\n" +
                ",utmcnt_c\n" +
                ",t2.utmcnt_d\n" +
                ",case when log_type='2' and t1.utmcnt_d!=''   then t1.utmcnt_d\n" +
                "      when log_type='1' then 'page_in'\n" +
                "      when log_type='3' then 'page_out' end  as evt_code\n" +
                ",case when log_type='2' and evt_type ='click' then 'click'\n" +
                "      when log_type='2' and evt_type!='click' then 'other' end  as evt_type\n" +
                ",log_type\n" +
                ",referrer\n" +
                ",url\n" +
                ",regexp_replace(regexp_replace(url,'^\\https',''),'\\^http','') as url_new\n" +
                ",url_host\n" +
                ",url_path\n" +
                ",url_origin\n" +
                ",device_mdl\n" +
                ",os_version\n" +
                ",browser_version\n" +
                ",browser_core\n" +
                ",ip\n" +
                ",net_type\n" +
                ",from_unixtime(cast(t2.client_tms/1000 as bigint), 'yyyy-MM-dd HH:mm:ss')     as  client_date\n" +
                ",pt\n" +
                ",count(1)  as click_cnt\n" +
                ",if(t1.utmcnt_d is not null,1,0) as is_glbl\n" +
                ",row_number() over(partition by pt  order by if(t2.busi_module_code in ('VACCINE_ZLB_H5','VACCINE_ALI_PAY_H5'),t2.uuid,t2.opt_id),utmcnt_a,utmcnt_b,utmcnt_c,t2.utmcnt_d) as rnk\n" +
                "from dwd.dwd_log_front_clk_basic_inc_d  as t2\n" +
                "left join  tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizdate} as t1\n" +
                "on t1.utmcnt_d=t2.utmcnt_d and t2.pt='${bizdate}'\n" +
                "where t2.pt='${bizdate}'\n" +
                "---and t2.utmcnt_a in ('a0002','a0004','av001','web-arena-front','app-merchant','web-stark-front','web-business-saas-front')\n" +
                "group by\n" +
                " substring(create_time,0,4)\n" +
                ",substring(create_time,0,7)\n" +
                ",pltfm_type\n" +
                ",busi_catg_code\n" +
                ",busi_catg_name\n" +
                ",busi_module_code\n" +
                ",busi_module_name\n" +
                ",app_type_code\n" +
                ",app_type_name\n" +
                ",if(t2.busi_module_code in ('VACCINE_ZLB_H5','VACCINE_ALI_PAY_H5'),t2.uuid,t2.opt_id)\n" +
                ",user_type_code\n" +
                ",user_type_name\n" +
                ",district_code\n" +
                ",district_name\n" +
                ",district_type\n" +
                ",district_type_id\n" +
                ",district_type_name\n" +
                ",district_level\n" +
                ",district_city_code\n" +
                ",district_city_name\n" +
                ",district_province_code\n" +
                ",district_province_name\n" +
                ",utmcnt_a\n" +
                ",utmcnt_b\n" +
                ",utmcnt_c\n" +
                ",t2.utmcnt_d\n" +
                ",case when log_type='2' and t1.utmcnt_d!=''   then t1.utmcnt_d\n" +
                "      when log_type='1' then 'page_in'\n" +
                "      when log_type='3' then 'page_out' end\n" +
                ",case when log_type='2' and evt_type ='click' then 'click'\n" +
                "      when log_type='2' and evt_type!='click' then 'other' end\n" +
                ",log_type\n" +
                ",referrer\n" +
                ",url\n" +
                ",regexp_replace(regexp_replace(url,'^\\https',''),'\\^http','')\n" +
                ",url_host\n" +
                ",url_path\n" +
                ",url_origin\n" +
                ",device_mdl\n" +
                ",os_version\n" +
                ",browser_version\n" +
                ",browser_core\n" +
                ",ip\n" +
                ",net_type\n" +
                ",from_unixtime(cast(t2.client_tms/1000 as bigint), 'yyyy-MM-dd HH:mm:ss')\n" +
                ",pt\n" +
                ",if(t1.utmcnt_d is not null,1,0)\n" +
                ";\n" +
                "\n" +
                "--取出按路径聚合的页面，b段值转换\n" +
                "--处理规则：优先取第一个匹配上的数据\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_url_join_${bizdate};\n" +
                "create table if not exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_url_join_${bizdate}  stored as orc as\n" +
                "select\n" +
                " t1.utmcnt_b\n" +
                ",rnk\n" +
                ",t2.url_new\n" +
                ",pt\n" +
                "from tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizdate}  as t2\n" +
                "left join tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_url_${bizdate}  as t1\n" +
                "  on  t2.utmcnt_a=t1.utmcnt_a\n" +
                "where t2.url_new  regexp concat('\\^',t1.url)\n" +
                ";\n" +
                "\n" +
                "insert overwrite table  ads.ads_hunyi_evt_clk_cnt_inc_d partition(pt='${bizdate}')\n" +
                "select\n" +
                " t_year\n" +
                ",t_month\n" +
                ",pltfm_type\n" +
                ",busi_catg_code\n" +
                ",busi_catg_name\n" +
                ",busi_module_code\n" +
                ",busi_module_name\n" +
                ",app_type_code\n" +
                ",app_type_name\n" +
                ",opt_id\n" +
                ",user_type_code\n" +
                ",user_type_name\n" +
                ",district_code\n" +
                ",district_name\n" +
                ",district_type\n" +
                ",district_type_id\n" +
                ",district_type_name\n" +
                ",district_level\n" +
                ",district_city_code\n" +
                ",district_city_name\n" +
                ",district_province_code\n" +
                ",district_province_name\n" +
                ",utmcnt_a\n" +
                ",coalesce(t2.utmcnt_b,t1.utmcnt_b) as utmcnt_b\n" +
                ",case when log_type in('1','3') then evt_code\n" +
                "      when is_glbl=1  then concat(evt_code,'#', evt_type)\n" +
                "      when coalesce(evt_code,'') =''  then concat_ws('#',utmcnt_a,coalesce(t2.utmcnt_b,t1.utmcnt_b) ,utmcnt_c,utmcnt_d, evt_type)\n" +
                "      end as evt_code\n" +
                ",evt_type as evt_type_cdoe\n" +
                ",log_type\n" +
                ",referrer\n" +
                ",url\n" +
                ",url_host\n" +
                ",url_path\n" +
                ",url_origin\n" +
                ",device_mdl\n" +
                ",os_version\n" +
                ",browser_version\n" +
                ",browser_core\n" +
                ",ip\n" +
                ",net_type\n" +
                ",client_date\n" +
                ",sum(click_cnt)  as click_cnt\n" +
                "--,pt\n" +
                "from tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizdate} as t1\n" +
                "left join tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_url_join_${bizdate}  as t2\n" +
                "  on t1.pt=t2.pt and t1.rnk=t2.rnk\n" +
                "group by\n" +
                " t_year\n" +
                ",t_month\n" +
                ",pltfm_type\n" +
                ",busi_catg_code\n" +
                ",busi_catg_name\n" +
                ",busi_module_code\n" +
                ",busi_module_name\n" +
                ",app_type_code\n" +
                ",app_type_name\n" +
                ",opt_id\n" +
                ",user_type_code\n" +
                ",user_type_name\n" +
                ",district_code\n" +
                ",district_name\n" +
                ",district_type\n" +
                ",district_type_id\n" +
                ",district_type_name\n" +
                ",district_level\n" +
                ",district_city_code\n" +
                ",district_city_name\n" +
                ",district_province_code\n" +
                ",district_province_name\n" +
                ",utmcnt_a\n" +
                ",coalesce(t2.utmcnt_b,t1.utmcnt_b)\n" +
                ",case when log_type in('1','3') then evt_code\n" +
                "      when is_glbl=1  then concat(evt_code,'#', evt_type)\n" +
                "      when coalesce(evt_code,'') =''   then concat_ws('#',utmcnt_a,coalesce(t2.utmcnt_b,t1.utmcnt_b) ,utmcnt_c,utmcnt_d, evt_type)\n" +
                "      end\n" +
                ",evt_type\n" +
                ",log_type\n" +
                ",referrer\n" +
                ",url\n" +
                ",url_host\n" +
                ",url_path\n" +
                ",url_origin\n" +
                ",device_mdl\n" +
                ",os_version\n" +
                ",browser_version\n" +
                ",browser_core\n" +
                ",ip\n" +
                ",net_type\n" +
                ",client_date\n" +
                ";\n" +
                "\n" +
                "\n" +
                "\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_url_join_${bizdate-1d};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizyear};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_evt_${bizyear-1Y};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizquarter};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizquarter};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${dt};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${0};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${.&};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizmonth};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_evt_${bizmonth-1m};\n" +
                "drop table if exists tmp.tmp_ads_hunyi_evt_clk_cnt_inc_d_global_url_${day};";
//        String sql = "${dt} from ${day-1d};${bizquarter} from ${bizquarter}!!!${bizyear} from {0}!!!${day}";
        String newSql = SqlDynamicParamTool.replaceParam(sql);
//        List<String> fromTables;
//        try {
//            fromTables = SparkSqlUtil.getFromTables(newSql, null);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        String add_db = SparkSqlUtil.addDatabaseEnv(sql,"stag_");
//        Map<String, String> map = SparkSqlUtil.insertErase(newSql);
//        System.out.println(sql);
        System.out.println(newSql);
    }
}
