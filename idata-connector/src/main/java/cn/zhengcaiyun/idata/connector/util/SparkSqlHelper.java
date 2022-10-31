package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import cn.zhengcaiyun.idata.connector.util.model.TableSib;
import cn.zhengcaiyun.idata.connector.util.model.UpperCaseCharStream;
import cn.zhengcaiyun.idata.connector.util.parse.ParseErrorListener;
import cn.zhengcaiyun.idata.connector.util.parse.ParseException;
import cn.zhengcaiyun.idata.connector.util.parse.PostProcessor;
import cn.zhengcaiyun.idata.connector.util.parse.SparkSqlAst;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.lang3.StringUtils;

public class SparkSqlHelper {

    public static TableSib getTableSib(String sql) {
        String trimCmd = StringUtils.trim(sql);

        UpperCaseCharStream charStream = new UpperCaseCharStream(CharStreams.fromString(trimCmd));
        SparkSqlLexer lexer = new SparkSqlLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ParseErrorListener());

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        parser.addParseListener(new PostProcessor());
        parser.removeErrorListeners();
        parser.addErrorListener(new ParseErrorListener());
        // first, try parsing with potentially faster SLL mode
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

//        System.out.println(parser.singleStatement().toStringTree(parser));

        SparkSqlAst visitor = new SparkSqlAst();

        try {
            return visitor.visit(parser.singleStatement());
        } catch (ParseCancellationException | ParseException e) {
            tokenStream.seek(0);
            parser.reset();

            // Try Again.
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            return visitor.visit(parser.statement());
        }
    }

}
