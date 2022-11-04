package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseListener;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

import java.util.ArrayList;
import java.util.List;

public class SelectTableListener extends SparkSqlBaseListener {

    public final List<String> tableList = new ArrayList<>();
    public final TokenStreamRewriter rewriter;
    private String op = null;
    private int inSelectLevel = 0;

    public SelectTableListener(TokenStream tokenStream) {
        this.rewriter = new TokenStreamRewriter(tokenStream);
    }

    @Override
    public void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
        if (op != null) {
            tableList.add(ctx.getText().toLowerCase());
        }
    }

    @Override
    public void enterQuerySpecification(SparkSqlParser.QuerySpecificationContext ctx) {
        op = "SELECT";
        inSelectLevel++;
    }

    @Override
    public void exitQuerySpecification(SparkSqlParser.QuerySpecificationContext ctx) {
        inSelectLevel--;
        if (inSelectLevel == 0) {
            op = null;
        }
    }

}
