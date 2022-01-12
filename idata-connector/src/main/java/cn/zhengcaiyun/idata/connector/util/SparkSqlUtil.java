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

import java.util.ArrayList;
import java.util.List;

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

}
