package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseListener;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTableListener extends SparkSqlBaseListener {

    private String op = null;
    public final Map<String, Object> tableInfoMap = new HashMap<>();

    @Override
    public void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
        if (op != null) {
            String tblName = ctx.getText().toLowerCase();
            if (tblName.indexOf(".") > -1) {
                tableInfoMap.put("dbName", tblName.split("\\.")[0].replaceAll("`", ""));
                tblName = tblName.split("\\.")[1];
            }
            tableInfoMap.put("tblName", tblName.replaceAll("`", ""));
        }
    }

    @Override
    public void enterCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx) {
        op = "CREATE TABLE";
        if (ctx.comment != null) {
            tableInfoMap.put("tblComment", ctx.comment.getText().replaceAll("'", ""));
        }
        if (ctx.columns != null) {
            tableInfoMap.put("columns", new ArrayList<Map<String, String>>());
            ctx.columns.colType().stream().forEach(colTypeContext -> {
                Map<String, String> column = new HashMap<>();
                column.put("colName", colTypeContext.identifier().getText().replaceAll("`", ""));
                column.put("colType", colTypeContext.dataType().getText());
                if (colTypeContext.STRING() != null)
                    column.put("colComment", colTypeContext.STRING().getText().replaceAll("'", ""));
                ((List) tableInfoMap.get("columns")).add(column);
            });
        }
        if (ctx.partitionColumns != null) {
            tableInfoMap.put("partitionColumns", new ArrayList<Map<String, String>>());
            ctx.partitionColumns.colType().stream().forEach(colTypeContext -> {
                Map<String, String> column = new HashMap<>();
                column.put("colName", colTypeContext.identifier().getText().replaceAll("`", ""));
                column.put("colType", colTypeContext.dataType().getText());
                if (colTypeContext.STRING() != null)
                    column.put("colComment", colTypeContext.STRING().getText().replaceAll("'", ""));
                ((List) tableInfoMap.get("partitionColumns")).add(column);
            });
        }
    }

    @Override
    public void exitCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx) {
        op = null;
    }

}
