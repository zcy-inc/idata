package cn.zhengcaiyun.idata.connector.util;


import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Map;

public class HiveSqlUtil {

    public static Map<String, Object> getCreateTableInfo(String sql) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(sql), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        CreateTableListener listener = new CreateTableListener();
        walker.walk(listener, parser.statement());
        return listener.tableInfoMap;
    }

    public static void main(String[] args) {
        String sql = "create table db.xxx (a bigint COMMENT 'id', s string) comment '测试分区表' " +
                "partitioned by (pt string comment '按天')";
        System.out.println(getCreateTableInfo(sql));
    }

}
