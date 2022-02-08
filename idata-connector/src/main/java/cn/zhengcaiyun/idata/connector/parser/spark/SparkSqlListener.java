package cn.zhengcaiyun.idata.connector.parser.spark;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SparkSqlParser}.
 */
public interface SparkSqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleStatement(SparkSqlParser.SingleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleStatement(SparkSqlParser.SingleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSingleExpression(SparkSqlParser.SingleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSingleExpression(SparkSqlParser.SingleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleTableIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSingleTableIdentifier(SparkSqlParser.SingleTableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleTableIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSingleTableIdentifier(SparkSqlParser.SingleTableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleFunctionIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSingleFunctionIdentifier(SparkSqlParser.SingleFunctionIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleFunctionIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSingleFunctionIdentifier(SparkSqlParser.SingleFunctionIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleDataType}.
	 * @param ctx the parse tree
	 */
	void enterSingleDataType(SparkSqlParser.SingleDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleDataType}.
	 * @param ctx the parse tree
	 */
	void exitSingleDataType(SparkSqlParser.SingleDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#singleTableSchema}.
	 * @param ctx the parse tree
	 */
	void enterSingleTableSchema(SparkSqlParser.SingleTableSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#singleTableSchema}.
	 * @param ctx the parse tree
	 */
	void exitSingleTableSchema(SparkSqlParser.SingleTableSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefault(SparkSqlParser.StatementDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefault(SparkSqlParser.StatementDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code use}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUse(SparkSqlParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code use}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUse(SparkSqlParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(SparkSqlParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(SparkSqlParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setDatabaseProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetDatabaseProperties(SparkSqlParser.SetDatabasePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setDatabaseProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetDatabaseProperties(SparkSqlParser.SetDatabasePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(SparkSqlParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(SparkSqlParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(SparkSqlParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(SparkSqlParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createHiveTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createHiveTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateHiveTable(SparkSqlParser.CreateHiveTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableLike(SparkSqlParser.CreateTableLikeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableLike}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableLike(SparkSqlParser.CreateTableLikeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze(SparkSqlParser.AnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze(SparkSqlParser.AnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addTableColumns}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAddTableColumns(SparkSqlParser.AddTableColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addTableColumns}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAddTableColumns(SparkSqlParser.AddTableColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(SparkSqlParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(SparkSqlParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTableProperties(SparkSqlParser.SetTablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTableProperties(SparkSqlParser.SetTablePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unsetTableProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUnsetTableProperties(SparkSqlParser.UnsetTablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unsetTableProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUnsetTableProperties(SparkSqlParser.UnsetTablePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code changeColumn}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterChangeColumn(SparkSqlParser.ChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code changeColumn}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitChangeColumn(SparkSqlParser.ChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableSerDe}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTableSerDe(SparkSqlParser.SetTableSerDeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableSerDe}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTableSerDe(SparkSqlParser.SetTableSerDeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addTablePartition}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAddTablePartition(SparkSqlParser.AddTablePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addTablePartition}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAddTablePartition(SparkSqlParser.AddTablePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTablePartition}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameTablePartition(SparkSqlParser.RenameTablePartitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTablePartition}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameTablePartition(SparkSqlParser.RenameTablePartitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTablePartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropTablePartitions(SparkSqlParser.DropTablePartitionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTablePartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropTablePartitions(SparkSqlParser.DropTablePartitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setTableLocation}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetTableLocation(SparkSqlParser.SetTableLocationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setTableLocation}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetTableLocation(SparkSqlParser.SetTableLocationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code recoverPartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRecoverPartitions(SparkSqlParser.RecoverPartitionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code recoverPartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRecoverPartitions(SparkSqlParser.RecoverPartitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(SparkSqlParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(SparkSqlParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createView}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(SparkSqlParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(SparkSqlParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTempViewUsing}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTempViewUsing(SparkSqlParser.CreateTempViewUsingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTempViewUsing}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTempViewUsing(SparkSqlParser.CreateTempViewUsingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterViewQuery}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterViewQuery(SparkSqlParser.AlterViewQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterViewQuery}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterViewQuery(SparkSqlParser.AlterViewQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(SparkSqlParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(SparkSqlParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(SparkSqlParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(SparkSqlParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explain}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExplain(SparkSqlParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExplain(SparkSqlParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(SparkSqlParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(SparkSqlParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowTable(SparkSqlParser.ShowTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowTable(SparkSqlParser.ShowTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowDatabases(SparkSqlParser.ShowDatabasesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showDatabases}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowDatabases(SparkSqlParser.ShowDatabasesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTblProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowTblProperties(SparkSqlParser.ShowTblPropertiesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTblProperties}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowTblProperties(SparkSqlParser.ShowTblPropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(SparkSqlParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(SparkSqlParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showPartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowPartitions(SparkSqlParser.ShowPartitionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showPartitions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowPartitions(SparkSqlParser.ShowPartitionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctions(SparkSqlParser.ShowFunctionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctions(SparkSqlParser.ShowFunctionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(SparkSqlParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(SparkSqlParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeFunction(SparkSqlParser.DescribeFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeFunction}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeFunction(SparkSqlParser.DescribeFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeDatabase(SparkSqlParser.DescribeDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeDatabase}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeDatabase(SparkSqlParser.DescribeDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeTable(SparkSqlParser.DescribeTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeTable(SparkSqlParser.DescribeTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRefreshTable(SparkSqlParser.RefreshTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRefreshTable(SparkSqlParser.RefreshTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refreshResource}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRefreshResource(SparkSqlParser.RefreshResourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refreshResource}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRefreshResource(SparkSqlParser.RefreshResourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cacheTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCacheTable(SparkSqlParser.CacheTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cacheTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCacheTable(SparkSqlParser.CacheTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uncacheTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUncacheTable(SparkSqlParser.UncacheTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uncacheTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUncacheTable(SparkSqlParser.UncacheTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code clearCache}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterClearCache(SparkSqlParser.ClearCacheContext ctx);
	/**
	 * Exit a parse tree produced by the {@code clearCache}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitClearCache(SparkSqlParser.ClearCacheContext ctx);
	/**
	 * Enter a parse tree produced by the {@code loadData}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterLoadData(SparkSqlParser.LoadDataContext ctx);
	/**
	 * Exit a parse tree produced by the {@code loadData}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitLoadData(SparkSqlParser.LoadDataContext ctx);
	/**
	 * Enter a parse tree produced by the {@code truncateTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(SparkSqlParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code truncateTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(SparkSqlParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repairTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(SparkSqlParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repairTable}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(SparkSqlParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code manageResource}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterManageResource(SparkSqlParser.ManageResourceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code manageResource}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitManageResource(SparkSqlParser.ManageResourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code failNativeCommand}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFailNativeCommand(SparkSqlParser.FailNativeCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code failNativeCommand}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFailNativeCommand(SparkSqlParser.FailNativeCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setConfiguration}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetConfiguration(SparkSqlParser.SetConfigurationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setConfiguration}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetConfiguration(SparkSqlParser.SetConfigurationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code resetConfiguration}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterResetConfiguration(SparkSqlParser.ResetConfigurationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code resetConfiguration}
	 * labeled alternative in {@link SparkSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitResetConfiguration(SparkSqlParser.ResetConfigurationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#unsupportedHiveNativeCommands}.
	 * @param ctx the parse tree
	 */
	void enterUnsupportedHiveNativeCommands(SparkSqlParser.UnsupportedHiveNativeCommandsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#unsupportedHiveNativeCommands}.
	 * @param ctx the parse tree
	 */
	void exitUnsupportedHiveNativeCommands(SparkSqlParser.UnsupportedHiveNativeCommandsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#createTableHeader}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableHeader(SparkSqlParser.CreateTableHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#createTableHeader}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableHeader(SparkSqlParser.CreateTableHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#bucketSpec}.
	 * @param ctx the parse tree
	 */
	void enterBucketSpec(SparkSqlParser.BucketSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#bucketSpec}.
	 * @param ctx the parse tree
	 */
	void exitBucketSpec(SparkSqlParser.BucketSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#skewSpec}.
	 * @param ctx the parse tree
	 */
	void enterSkewSpec(SparkSqlParser.SkewSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#skewSpec}.
	 * @param ctx the parse tree
	 */
	void exitSkewSpec(SparkSqlParser.SkewSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#locationSpec}.
	 * @param ctx the parse tree
	 */
	void enterLocationSpec(SparkSqlParser.LocationSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#locationSpec}.
	 * @param ctx the parse tree
	 */
	void exitLocationSpec(SparkSqlParser.LocationSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(SparkSqlParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(SparkSqlParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertOverwriteTable}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void enterInsertOverwriteTable(SparkSqlParser.InsertOverwriteTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertOverwriteTable}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void exitInsertOverwriteTable(SparkSqlParser.InsertOverwriteTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertIntoTable}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void enterInsertIntoTable(SparkSqlParser.InsertIntoTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertIntoTable}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void exitInsertIntoTable(SparkSqlParser.InsertIntoTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertOverwriteHiveDir}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void enterInsertOverwriteHiveDir(SparkSqlParser.InsertOverwriteHiveDirContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertOverwriteHiveDir}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void exitInsertOverwriteHiveDir(SparkSqlParser.InsertOverwriteHiveDirContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertOverwriteDir}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void enterInsertOverwriteDir(SparkSqlParser.InsertOverwriteDirContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertOverwriteDir}
	 * labeled alternative in {@link SparkSqlParser#insertInto}.
	 * @param ctx the parse tree
	 */
	void exitInsertOverwriteDir(SparkSqlParser.InsertOverwriteDirContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#partitionSpecLocation}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSpecLocation(SparkSqlParser.PartitionSpecLocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#partitionSpecLocation}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSpecLocation(SparkSqlParser.PartitionSpecLocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSpec(SparkSqlParser.PartitionSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSpec(SparkSqlParser.PartitionSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#partitionVal}.
	 * @param ctx the parse tree
	 */
	void enterPartitionVal(SparkSqlParser.PartitionValContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#partitionVal}.
	 * @param ctx the parse tree
	 */
	void exitPartitionVal(SparkSqlParser.PartitionValContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#describeFuncName}.
	 * @param ctx the parse tree
	 */
	void enterDescribeFuncName(SparkSqlParser.DescribeFuncNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#describeFuncName}.
	 * @param ctx the parse tree
	 */
	void exitDescribeFuncName(SparkSqlParser.DescribeFuncNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#describeColName}.
	 * @param ctx the parse tree
	 */
	void enterDescribeColName(SparkSqlParser.DescribeColNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#describeColName}.
	 * @param ctx the parse tree
	 */
	void exitDescribeColName(SparkSqlParser.DescribeColNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#ctes}.
	 * @param ctx the parse tree
	 */
	void enterCtes(SparkSqlParser.CtesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#ctes}.
	 * @param ctx the parse tree
	 */
	void exitCtes(SparkSqlParser.CtesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void enterNamedQuery(SparkSqlParser.NamedQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void exitNamedQuery(SparkSqlParser.NamedQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tableProvider}.
	 * @param ctx the parse tree
	 */
	void enterTableProvider(SparkSqlParser.TableProviderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tableProvider}.
	 * @param ctx the parse tree
	 */
	void exitTableProvider(SparkSqlParser.TableProviderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tablePropertyList}.
	 * @param ctx the parse tree
	 */
	void enterTablePropertyList(SparkSqlParser.TablePropertyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tablePropertyList}.
	 * @param ctx the parse tree
	 */
	void exitTablePropertyList(SparkSqlParser.TablePropertyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterTableProperty(SparkSqlParser.TablePropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitTableProperty(SparkSqlParser.TablePropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tablePropertyKey}.
	 * @param ctx the parse tree
	 */
	void enterTablePropertyKey(SparkSqlParser.TablePropertyKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tablePropertyKey}.
	 * @param ctx the parse tree
	 */
	void exitTablePropertyKey(SparkSqlParser.TablePropertyKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tablePropertyValue}.
	 * @param ctx the parse tree
	 */
	void enterTablePropertyValue(SparkSqlParser.TablePropertyValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tablePropertyValue}.
	 * @param ctx the parse tree
	 */
	void exitTablePropertyValue(SparkSqlParser.TablePropertyValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#constantList}.
	 * @param ctx the parse tree
	 */
	void enterConstantList(SparkSqlParser.ConstantListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#constantList}.
	 * @param ctx the parse tree
	 */
	void exitConstantList(SparkSqlParser.ConstantListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#nestedConstantList}.
	 * @param ctx the parse tree
	 */
	void enterNestedConstantList(SparkSqlParser.NestedConstantListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#nestedConstantList}.
	 * @param ctx the parse tree
	 */
	void exitNestedConstantList(SparkSqlParser.NestedConstantListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#createFileFormat}.
	 * @param ctx the parse tree
	 */
	void enterCreateFileFormat(SparkSqlParser.CreateFileFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#createFileFormat}.
	 * @param ctx the parse tree
	 */
	void exitCreateFileFormat(SparkSqlParser.CreateFileFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableFileFormat}
	 * labeled alternative in {@link SparkSqlParser#fileFormat}.
	 * @param ctx the parse tree
	 */
	void enterTableFileFormat(SparkSqlParser.TableFileFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableFileFormat}
	 * labeled alternative in {@link SparkSqlParser#fileFormat}.
	 * @param ctx the parse tree
	 */
	void exitTableFileFormat(SparkSqlParser.TableFileFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code genericFileFormat}
	 * labeled alternative in {@link SparkSqlParser#fileFormat}.
	 * @param ctx the parse tree
	 */
	void enterGenericFileFormat(SparkSqlParser.GenericFileFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code genericFileFormat}
	 * labeled alternative in {@link SparkSqlParser#fileFormat}.
	 * @param ctx the parse tree
	 */
	void exitGenericFileFormat(SparkSqlParser.GenericFileFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#storageHandler}.
	 * @param ctx the parse tree
	 */
	void enterStorageHandler(SparkSqlParser.StorageHandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#storageHandler}.
	 * @param ctx the parse tree
	 */
	void exitStorageHandler(SparkSqlParser.StorageHandlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(SparkSqlParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(SparkSqlParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code singleInsertQuery}
	 * labeled alternative in {@link SparkSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void enterSingleInsertQuery(SparkSqlParser.SingleInsertQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code singleInsertQuery}
	 * labeled alternative in {@link SparkSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void exitSingleInsertQuery(SparkSqlParser.SingleInsertQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiInsertQuery}
	 * labeled alternative in {@link SparkSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void enterMultiInsertQuery(SparkSqlParser.MultiInsertQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiInsertQuery}
	 * labeled alternative in {@link SparkSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void exitMultiInsertQuery(SparkSqlParser.MultiInsertQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#queryOrganization}.
	 * @param ctx the parse tree
	 */
	void enterQueryOrganization(SparkSqlParser.QueryOrganizationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#queryOrganization}.
	 * @param ctx the parse tree
	 */
	void exitQueryOrganization(SparkSqlParser.QueryOrganizationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#multiInsertQueryBody}.
	 * @param ctx the parse tree
	 */
	void enterMultiInsertQueryBody(SparkSqlParser.MultiInsertQueryBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#multiInsertQueryBody}.
	 * @param ctx the parse tree
	 */
	void exitMultiInsertQueryBody(SparkSqlParser.MultiInsertQueryBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link SparkSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterQueryTermDefault(SparkSqlParser.QueryTermDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link SparkSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitQueryTermDefault(SparkSqlParser.QueryTermDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link SparkSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterSetOperation(SparkSqlParser.SetOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link SparkSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitSetOperation(SparkSqlParser.SetOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterQueryPrimaryDefault(SparkSqlParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitQueryPrimaryDefault(SparkSqlParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTable(SparkSqlParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTable(SparkSqlParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inlineTableDefault1}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterInlineTableDefault1(SparkSqlParser.InlineTableDefault1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code inlineTableDefault1}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitInlineTableDefault1(SparkSqlParser.InlineTableDefault1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(SparkSqlParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link SparkSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(SparkSqlParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void enterSortItem(SparkSqlParser.SortItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void exitSortItem(SparkSqlParser.SortItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(SparkSqlParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(SparkSqlParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#hint}.
	 * @param ctx the parse tree
	 */
	void enterHint(SparkSqlParser.HintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#hint}.
	 * @param ctx the parse tree
	 */
	void exitHint(SparkSqlParser.HintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#hintStatement}.
	 * @param ctx the parse tree
	 */
	void enterHintStatement(SparkSqlParser.HintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#hintStatement}.
	 * @param ctx the parse tree
	 */
	void exitHintStatement(SparkSqlParser.HintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(SparkSqlParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(SparkSqlParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterAggregation(SparkSqlParser.AggregationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitAggregation(SparkSqlParser.AggregationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSet(SparkSqlParser.GroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSet(SparkSqlParser.GroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#pivotClause}.
	 * @param ctx the parse tree
	 */
	void enterPivotClause(SparkSqlParser.PivotClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#pivotClause}.
	 * @param ctx the parse tree
	 */
	void exitPivotClause(SparkSqlParser.PivotClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#pivotColumn}.
	 * @param ctx the parse tree
	 */
	void enterPivotColumn(SparkSqlParser.PivotColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#pivotColumn}.
	 * @param ctx the parse tree
	 */
	void exitPivotColumn(SparkSqlParser.PivotColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#pivotValue}.
	 * @param ctx the parse tree
	 */
	void enterPivotValue(SparkSqlParser.PivotValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#pivotValue}.
	 * @param ctx the parse tree
	 */
	void exitPivotValue(SparkSqlParser.PivotValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void enterLateralView(SparkSqlParser.LateralViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#lateralView}.
	 * @param ctx the parse tree
	 */
	void exitLateralView(SparkSqlParser.LateralViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterSetQuantifier(SparkSqlParser.SetQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitSetQuantifier(SparkSqlParser.SetQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(SparkSqlParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(SparkSqlParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#joinRelation}.
	 * @param ctx the parse tree
	 */
	void enterJoinRelation(SparkSqlParser.JoinRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#joinRelation}.
	 * @param ctx the parse tree
	 */
	void exitJoinRelation(SparkSqlParser.JoinRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#joinType}.
	 * @param ctx the parse tree
	 */
	void enterJoinType(SparkSqlParser.JoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#joinType}.
	 * @param ctx the parse tree
	 */
	void exitJoinType(SparkSqlParser.JoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void enterJoinCriteria(SparkSqlParser.JoinCriteriaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void exitJoinCriteria(SparkSqlParser.JoinCriteriaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#sample}.
	 * @param ctx the parse tree
	 */
	void enterSample(SparkSqlParser.SampleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#sample}.
	 * @param ctx the parse tree
	 */
	void exitSample(SparkSqlParser.SampleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByPercentile}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByPercentile(SparkSqlParser.SampleByPercentileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByPercentile}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByPercentile(SparkSqlParser.SampleByPercentileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByRows}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByRows(SparkSqlParser.SampleByRowsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByRows}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByRows(SparkSqlParser.SampleByRowsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByBucket}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByBucket(SparkSqlParser.SampleByBucketContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByBucket}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByBucket(SparkSqlParser.SampleByBucketContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sampleByBytes}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void enterSampleByBytes(SparkSqlParser.SampleByBytesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sampleByBytes}
	 * labeled alternative in {@link SparkSqlParser#sampleMethod}.
	 * @param ctx the parse tree
	 */
	void exitSampleByBytes(SparkSqlParser.SampleByBytesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(SparkSqlParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(SparkSqlParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#identifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierSeq(SparkSqlParser.IdentifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#identifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierSeq(SparkSqlParser.IdentifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#orderedIdentifierList}.
	 * @param ctx the parse tree
	 */
	void enterOrderedIdentifierList(SparkSqlParser.OrderedIdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#orderedIdentifierList}.
	 * @param ctx the parse tree
	 */
	void exitOrderedIdentifierList(SparkSqlParser.OrderedIdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#orderedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterOrderedIdentifier(SparkSqlParser.OrderedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#orderedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitOrderedIdentifier(SparkSqlParser.OrderedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#identifierCommentList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierCommentList(SparkSqlParser.IdentifierCommentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#identifierCommentList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierCommentList(SparkSqlParser.IdentifierCommentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#identifierComment}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierComment(SparkSqlParser.IdentifierCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#identifierComment}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierComment(SparkSqlParser.IdentifierCommentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SparkSqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SparkSqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aliasedQuery}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterAliasedQuery(SparkSqlParser.AliasedQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aliasedQuery}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitAliasedQuery(SparkSqlParser.AliasedQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aliasedRelation}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterAliasedRelation(SparkSqlParser.AliasedRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aliasedRelation}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitAliasedRelation(SparkSqlParser.AliasedRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inlineTableDefault2}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterInlineTableDefault2(SparkSqlParser.InlineTableDefault2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code inlineTableDefault2}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitInlineTableDefault2(SparkSqlParser.InlineTableDefault2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code tableValuedFunction}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableValuedFunction(SparkSqlParser.TableValuedFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableValuedFunction}
	 * labeled alternative in {@link SparkSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableValuedFunction(SparkSqlParser.TableValuedFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#inlineTable}.
	 * @param ctx the parse tree
	 */
	void enterInlineTable(SparkSqlParser.InlineTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#inlineTable}.
	 * @param ctx the parse tree
	 */
	void exitInlineTable(SparkSqlParser.InlineTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#functionTable}.
	 * @param ctx the parse tree
	 */
	void enterFunctionTable(SparkSqlParser.FunctionTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#functionTable}.
	 * @param ctx the parse tree
	 */
	void exitFunctionTable(SparkSqlParser.FunctionTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tableAlias}.
	 * @param ctx the parse tree
	 */
	void enterTableAlias(SparkSqlParser.TableAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tableAlias}.
	 * @param ctx the parse tree
	 */
	void exitTableAlias(SparkSqlParser.TableAliasContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowFormatSerde}
	 * labeled alternative in {@link SparkSqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void enterRowFormatSerde(SparkSqlParser.RowFormatSerdeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowFormatSerde}
	 * labeled alternative in {@link SparkSqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void exitRowFormatSerde(SparkSqlParser.RowFormatSerdeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowFormatDelimited}
	 * labeled alternative in {@link SparkSqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void enterRowFormatDelimited(SparkSqlParser.RowFormatDelimitedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowFormatDelimited}
	 * labeled alternative in {@link SparkSqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void exitRowFormatDelimited(SparkSqlParser.RowFormatDelimitedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTableIdentifier(SparkSqlParser.TableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#tableIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTableIdentifier(SparkSqlParser.TableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#functionIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionIdentifier(SparkSqlParser.FunctionIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#functionIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionIdentifier(SparkSqlParser.FunctionIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#namedExpression}.
	 * @param ctx the parse tree
	 */
	void enterNamedExpression(SparkSqlParser.NamedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#namedExpression}.
	 * @param ctx the parse tree
	 */
	void exitNamedExpression(SparkSqlParser.NamedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#namedExpressionSeq}.
	 * @param ctx the parse tree
	 */
	void enterNamedExpressionSeq(SparkSqlParser.NamedExpressionSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#namedExpressionSeq}.
	 * @param ctx the parse tree
	 */
	void exitNamedExpressionSeq(SparkSqlParser.NamedExpressionSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SparkSqlParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SparkSqlParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalNot(SparkSqlParser.LogicalNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalNot(SparkSqlParser.LogicalNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterPredicated(SparkSqlParser.PredicatedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitPredicated(SparkSqlParser.PredicatedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exists}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterExists(SparkSqlParser.ExistsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exists}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitExists(SparkSqlParser.ExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalBinary(SparkSqlParser.LogicalBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link SparkSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalBinary(SparkSqlParser.LogicalBinaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(SparkSqlParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(SparkSqlParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpressionDefault(SparkSqlParser.ValueExpressionDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpressionDefault(SparkSqlParser.ValueExpressionDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparison(SparkSqlParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparison(SparkSqlParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticBinary(SparkSqlParser.ArithmeticBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticBinary(SparkSqlParser.ArithmeticBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticUnary(SparkSqlParser.ArithmeticUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link SparkSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticUnary(SparkSqlParser.ArithmeticUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code struct}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStruct(SparkSqlParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by the {@code struct}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStruct(SparkSqlParser.StructContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDereference(SparkSqlParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDereference(SparkSqlParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCase(SparkSqlParser.SimpleCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCase(SparkSqlParser.SimpleCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterColumnReference(SparkSqlParser.ColumnReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitColumnReference(SparkSqlParser.ColumnReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructor(SparkSqlParser.RowConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructor(SparkSqlParser.RowConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code last}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLast(SparkSqlParser.LastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code last}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLast(SparkSqlParser.LastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code star}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStar(SparkSqlParser.StarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code star}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStar(SparkSqlParser.StarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(SparkSqlParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(SparkSqlParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpression(SparkSqlParser.SubqueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpression(SparkSqlParser.SubqueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cast}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCast(SparkSqlParser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCast(SparkSqlParser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantDefault}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantDefault(SparkSqlParser.ConstantDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantDefault}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantDefault(SparkSqlParser.ConstantDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambda(SparkSqlParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambda(SparkSqlParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(SparkSqlParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(SparkSqlParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extract}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtract(SparkSqlParser.ExtractContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extract}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtract(SparkSqlParser.ExtractContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SparkSqlParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SparkSqlParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSearchedCase(SparkSqlParser.SearchedCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSearchedCase(SparkSqlParser.SearchedCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code position}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPosition(SparkSqlParser.PositionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code position}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPosition(SparkSqlParser.PositionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code first}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFirst(SparkSqlParser.FirstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code first}
	 * labeled alternative in {@link SparkSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFirst(SparkSqlParser.FirstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(SparkSqlParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(SparkSqlParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterIntervalLiteral(SparkSqlParser.IntervalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitIntervalLiteral(SparkSqlParser.IntervalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstructor(SparkSqlParser.TypeConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstructor(SparkSqlParser.TypeConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(SparkSqlParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(SparkSqlParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(SparkSqlParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(SparkSqlParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(SparkSqlParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link SparkSqlParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(SparkSqlParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SparkSqlParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SparkSqlParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticOperator(SparkSqlParser.ArithmeticOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticOperator(SparkSqlParser.ArithmeticOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#predicateOperator}.
	 * @param ctx the parse tree
	 */
	void enterPredicateOperator(SparkSqlParser.PredicateOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#predicateOperator}.
	 * @param ctx the parse tree
	 */
	void exitPredicateOperator(SparkSqlParser.PredicateOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(SparkSqlParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(SparkSqlParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#interval}.
	 * @param ctx the parse tree
	 */
	void enterInterval(SparkSqlParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#interval}.
	 * @param ctx the parse tree
	 */
	void exitInterval(SparkSqlParser.IntervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void enterIntervalField(SparkSqlParser.IntervalFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void exitIntervalField(SparkSqlParser.IntervalFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#intervalValue}.
	 * @param ctx the parse tree
	 */
	void enterIntervalValue(SparkSqlParser.IntervalValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#intervalValue}.
	 * @param ctx the parse tree
	 */
	void exitIntervalValue(SparkSqlParser.IntervalValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#colPosition}.
	 * @param ctx the parse tree
	 */
	void enterColPosition(SparkSqlParser.ColPositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#colPosition}.
	 * @param ctx the parse tree
	 */
	void exitColPosition(SparkSqlParser.ColPositionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link SparkSqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterComplexDataType(SparkSqlParser.ComplexDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code complexDataType}
	 * labeled alternative in {@link SparkSqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitComplexDataType(SparkSqlParser.ComplexDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link SparkSqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveDataType(SparkSqlParser.PrimitiveDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveDataType}
	 * labeled alternative in {@link SparkSqlParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveDataType(SparkSqlParser.PrimitiveDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#colTypeList}.
	 * @param ctx the parse tree
	 */
	void enterColTypeList(SparkSqlParser.ColTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#colTypeList}.
	 * @param ctx the parse tree
	 */
	void exitColTypeList(SparkSqlParser.ColTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#colType}.
	 * @param ctx the parse tree
	 */
	void enterColType(SparkSqlParser.ColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#colType}.
	 * @param ctx the parse tree
	 */
	void exitColType(SparkSqlParser.ColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void enterComplexColTypeList(SparkSqlParser.ComplexColTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#complexColTypeList}.
	 * @param ctx the parse tree
	 */
	void exitComplexColTypeList(SparkSqlParser.ComplexColTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void enterComplexColType(SparkSqlParser.ComplexColTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#complexColType}.
	 * @param ctx the parse tree
	 */
	void exitComplexColType(SparkSqlParser.ComplexColTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(SparkSqlParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(SparkSqlParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#windows}.
	 * @param ctx the parse tree
	 */
	void enterWindows(SparkSqlParser.WindowsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#windows}.
	 * @param ctx the parse tree
	 */
	void exitWindows(SparkSqlParser.WindowsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#namedWindow}.
	 * @param ctx the parse tree
	 */
	void enterNamedWindow(SparkSqlParser.NamedWindowContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#namedWindow}.
	 * @param ctx the parse tree
	 */
	void exitNamedWindow(SparkSqlParser.NamedWindowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code windowRef}
	 * labeled alternative in {@link SparkSqlParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void enterWindowRef(SparkSqlParser.WindowRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code windowRef}
	 * labeled alternative in {@link SparkSqlParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void exitWindowRef(SparkSqlParser.WindowRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code windowDef}
	 * labeled alternative in {@link SparkSqlParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void enterWindowDef(SparkSqlParser.WindowDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code windowDef}
	 * labeled alternative in {@link SparkSqlParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void exitWindowDef(SparkSqlParser.WindowDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void enterWindowFrame(SparkSqlParser.WindowFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void exitWindowFrame(SparkSqlParser.WindowFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterFrameBound(SparkSqlParser.FrameBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitFrameBound(SparkSqlParser.FrameBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SparkSqlParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SparkSqlParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SparkSqlParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SparkSqlParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link SparkSqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedIdentifier(SparkSqlParser.UnquotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link SparkSqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedIdentifier(SparkSqlParser.UnquotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quotedIdentifierAlternative}
	 * labeled alternative in {@link SparkSqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifierAlternative(SparkSqlParser.QuotedIdentifierAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quotedIdentifierAlternative}
	 * labeled alternative in {@link SparkSqlParser#strictIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifierAlternative(SparkSqlParser.QuotedIdentifierAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(SparkSqlParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(SparkSqlParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(SparkSqlParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(SparkSqlParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(SparkSqlParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(SparkSqlParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bigIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterBigIntLiteral(SparkSqlParser.BigIntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bigIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitBigIntLiteral(SparkSqlParser.BigIntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code smallIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterSmallIntLiteral(SparkSqlParser.SmallIntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code smallIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitSmallIntLiteral(SparkSqlParser.SmallIntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tinyIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterTinyIntLiteral(SparkSqlParser.TinyIntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tinyIntLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitTinyIntLiteral(SparkSqlParser.TinyIntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDoubleLiteral(SparkSqlParser.DoubleLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDoubleLiteral(SparkSqlParser.DoubleLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bigDecimalLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterBigDecimalLiteral(SparkSqlParser.BigDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bigDecimalLiteral}
	 * labeled alternative in {@link SparkSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitBigDecimalLiteral(SparkSqlParser.BigDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SparkSqlParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void enterNonReserved(SparkSqlParser.NonReservedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SparkSqlParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void exitNonReserved(SparkSqlParser.NonReservedContext ctx);
}