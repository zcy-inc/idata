package cn.zhengcaiyun.idata.connector.util.parse;

import cn.zhengcaiyun.idata.connector.util.model.Origin;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ParseErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Origin position = new Origin(line, charPositionInLine);
        throw new ParseException(msg, position, position);
    }
}
