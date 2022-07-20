package cn.zhengcaiyun.idata.connector.clients.hive.util;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.parser.CaseChangingCharStream;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlLexer;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;
import cn.zhengcaiyun.idata.connector.util.CreateTableListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JiveUtil {

    /**
     * 美化大小展示
     * @param bytes
     * @return
     */
    public static String beautifySize(long bytes) {
        if (bytes < 0) {
            return null;
        } else if (bytes < 1024 * 1024) {
            return (bytes / 1024) + "KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return (bytes / 1024 / 1024) + "MB";
        }
        return (bytes / 1024 / 1024 / 1024) + "GB";
    }

    /**
     * 封装hive alter DDL语句 例子"alter table `dws`.t_user add columns (sex string comment '性别', address string comment '地址')" 注意不能带;
     * @param addColumns
     * @param dbName
     * @param tableName
     * @return
     */
    public static String assembleHiveAddColumnSQL(Set<ColumnInfoDto> addColumns, String dbName, String tableName) {
        StringBuilder builder = new StringBuilder("alter table ")
                .append("`")
                .append(dbName)
                .append("`.")
                .append(tableName)
                .append(" add columns (");

        // 拼接每一个新增列的语法，如：sex string comment '性别'
        List<String> columnsInfoList = addColumns
                .stream()
                .map(e -> e.getColumnName()
                        + " " + e.getColumnType()
                        + " comment '"
                        + org.apache.commons.lang3.StringUtils.getIfEmpty(e.getColumnComment(), () -> "")
                        + "'")
                .collect(Collectors.toList());
        builder.append(StringUtils.join(columnsInfoList, ","));
        builder.append(")");
        return builder.toString();
    }


    /**
     * 判断注释相等，主要是为了兼容 'null'和''
     * @param localColumnComment
     * @param hiveColumnComment
     * @return
     */
    public static boolean commentEquals(String localColumnComment, String hiveColumnComment) {
        if (StringUtils.equals("null", localColumnComment)) {
            localColumnComment = null;
        }
        if (StringUtils.equals("null", hiveColumnComment)) {
            hiveColumnComment = null;
        }
        if (StringUtils.isBlank(localColumnComment) && StringUtils.isBlank(hiveColumnComment)) {
            return true;
        }
        return StringUtils.equalsIgnoreCase(localColumnComment, hiveColumnComment);
    }

    public static MetadataInfo parseMetadataInfo(String createDDL) {
        SparkSqlLexer lexer = new SparkSqlLexer(new CaseChangingCharStream(CharStreams.fromString(createDDL), true));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SparkSqlParser parser = new SparkSqlParser(tokenStream);
        ParseTreeWalker walker = new ParseTreeWalker();
        CreateTableListener listener = new CreateTableListener();
        walker.walk(listener, parser.statement());
        return listener.metadataInfo;
    }
}
