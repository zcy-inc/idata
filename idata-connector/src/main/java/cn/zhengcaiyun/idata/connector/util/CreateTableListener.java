package cn.zhengcaiyun.idata.connector.util;

import cn.zhengcaiyun.idata.connector.clients.hive.model.MetadataInfo;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlBaseListener;
import cn.zhengcaiyun.idata.connector.parser.spark.SparkSqlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTableListener extends SparkSqlBaseListener {

    private String op = null;
    public final Map<String, Object> tableInfoMap = new HashMap<>();
    public final MetadataInfo metadataInfo = new MetadataInfo();

    @Override
    public void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx) {
        if (op != null) {
            String tblName = ctx.getText().toLowerCase();
            if (tblName.indexOf(".") > -1) {
                String dbName = tblName.split("\\.")[0].replaceAll("`", "");
                tableInfoMap.put("dbName", dbName);
                metadataInfo.setDbName(dbName);
                tblName = tblName.split("\\.")[1];
            }
            String tableName = tblName.replaceAll("`", "");
            tableInfoMap.put("tblName", tableName);
            metadataInfo.setTableName(tableName);
        }
    }

    @Override
    public void enterCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx) {
        op = "CREATE TABLE";
        if (ctx.comment != null) {
            String tableComment = ctx.comment.getText().replaceAll("'", "");
            tableInfoMap.put("tblComment", tableComment);
            metadataInfo.setTableComment(tableComment);
        }
        if (ctx.columns != null) {
            tableInfoMap.put("columns", new ArrayList<Map<String, String>>());
            ctx.columns.colType().stream().forEach(colTypeContext -> {
                Map<String, String> column = new HashMap<>();
                MetadataInfo.ColumnInfo columnInfo = new MetadataInfo.ColumnInfo();

                String colName = colTypeContext.identifier().getText().replaceAll("`", "");
                column.put("colName", colName);
                columnInfo.setColumnName(colName);

                String colType = colTypeContext.dataType().getText();
                column.put("colType", colType);
                columnInfo.setColumnType(colType);
                if (colTypeContext.STRING() != null) {
                    String colComment = colTypeContext.STRING().getText().replaceAll("'", "");
                    column.put("colComment", colComment);
                    columnInfo.setColumnComment(colComment);
                }

                ((List) tableInfoMap.get("columns")).add(column);
                metadataInfo.getColumnList().add(columnInfo);
            });
        }
        if (ctx.partitionColumns != null) {
            tableInfoMap.put("partitionColumns", new ArrayList<Map<String, String>>());
            ctx.partitionColumns.colType().stream().forEach(colTypeContext -> {
                Map<String, String> column = new HashMap<>();
                MetadataInfo.ColumnInfo columnInfo = new MetadataInfo.ColumnInfo();

                String colName = colTypeContext.identifier().getText().replaceAll("`", "");
                column.put("colName", colName);
                columnInfo.setColumnName(colName);

                String colType = colTypeContext.dataType().getText();
                column.put("colType", colType);
                columnInfo.setColumnType(colType);
                if (colTypeContext.STRING() != null) {
                    String colComment = colTypeContext.STRING().getText().replaceAll("'", "");
                    column.put("colComment", colComment);
                    columnInfo.setColumnComment(colComment);
                }

                ((List) tableInfoMap.get("partitionColumns")).add(column);
                metadataInfo.getPartitionColumnList().add(columnInfo);
            });
        }
    }

    @Override
    public void exitCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx) {
        op = null;
    }

}
