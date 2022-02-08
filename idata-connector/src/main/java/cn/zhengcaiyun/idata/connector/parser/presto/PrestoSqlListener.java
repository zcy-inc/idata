package cn.zhengcaiyun.idata.connector.parser.presto;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PrestoSqlParser}.
 */
public interface PrestoSqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSingleStatement(PrestoSqlParser.SingleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#singleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSingleStatement(PrestoSqlParser.SingleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#standaloneExpression}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneExpression(PrestoSqlParser.StandaloneExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#standaloneExpression}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneExpression(PrestoSqlParser.StandaloneExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#standalonePathSpecification}.
	 * @param ctx the parse tree
	 */
	void enterStandalonePathSpecification(PrestoSqlParser.StandalonePathSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#standalonePathSpecification}.
	 * @param ctx the parse tree
	 */
	void exitStandalonePathSpecification(PrestoSqlParser.StandalonePathSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatementDefault(PrestoSqlParser.StatementDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statementDefault}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatementDefault(PrestoSqlParser.StatementDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code use}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterUse(PrestoSqlParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code use}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitUse(PrestoSqlParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateSchema(PrestoSqlParser.CreateSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateSchema(PrestoSqlParser.CreateSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropSchema(PrestoSqlParser.DropSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropSchema(PrestoSqlParser.DropSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameSchema(PrestoSqlParser.RenameSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameSchema}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameSchema(PrestoSqlParser.RenameSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTableAsSelect}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableAsSelect(PrestoSqlParser.CreateTableAsSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTableAsSelect}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableAsSelect(PrestoSqlParser.CreateTableAsSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(PrestoSqlParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(PrestoSqlParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(PrestoSqlParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(PrestoSqlParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterInsertInto(PrestoSqlParser.InsertIntoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code insertInto}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitInsertInto(PrestoSqlParser.InsertIntoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code delete}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDelete(PrestoSqlParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code delete}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDelete(PrestoSqlParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(PrestoSqlParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(PrestoSqlParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commentTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentTable(PrestoSqlParser.CommentTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commentTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentTable(PrestoSqlParser.CommentTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRenameColumn(PrestoSqlParser.RenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRenameColumn(PrestoSqlParser.RenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropColumn(PrestoSqlParser.DropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropColumn(PrestoSqlParser.DropColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAddColumn(PrestoSqlParser.AddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAddColumn(PrestoSqlParser.AddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze(PrestoSqlParser.AnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code analyze}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze(PrestoSqlParser.AnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(PrestoSqlParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(PrestoSqlParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropView(PrestoSqlParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropView(PrestoSqlParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code call}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCall(PrestoSqlParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code call}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCall(PrestoSqlParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(PrestoSqlParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(PrestoSqlParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dropRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(PrestoSqlParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dropRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(PrestoSqlParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterGrantRoles(PrestoSqlParser.GrantRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitGrantRoles(PrestoSqlParser.GrantRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRevokeRoles(PrestoSqlParser.RevokeRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRevokeRoles(PrestoSqlParser.RevokeRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetRole(PrestoSqlParser.SetRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setRole}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetRole(PrestoSqlParser.SetRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grant}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterGrant(PrestoSqlParser.GrantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grant}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitGrant(PrestoSqlParser.GrantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revoke}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRevoke(PrestoSqlParser.RevokeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revoke}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRevoke(PrestoSqlParser.RevokeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(PrestoSqlParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showGrants}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(PrestoSqlParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explain}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExplain(PrestoSqlParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explain}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExplain(PrestoSqlParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(PrestoSqlParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateTable}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(PrestoSqlParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCreateView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateView(PrestoSqlParser.ShowCreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCreateView}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateView(PrestoSqlParser.ShowCreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(PrestoSqlParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showTables}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(PrestoSqlParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSchemas}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowSchemas(PrestoSqlParser.ShowSchemasContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSchemas}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowSchemas(PrestoSqlParser.ShowSchemasContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showCatalogs}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowCatalogs(PrestoSqlParser.ShowCatalogsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showCatalogs}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowCatalogs(PrestoSqlParser.ShowCatalogsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(PrestoSqlParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showColumns}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(PrestoSqlParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStats}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowStats(PrestoSqlParser.ShowStatsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStats}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowStats(PrestoSqlParser.ShowStatsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showStatsForQuery}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowStatsForQuery(PrestoSqlParser.ShowStatsForQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showStatsForQuery}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowStatsForQuery(PrestoSqlParser.ShowStatsForQueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoles(PrestoSqlParser.ShowRolesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoles}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoles(PrestoSqlParser.ShowRolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showRoleGrants}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowRoleGrants(PrestoSqlParser.ShowRoleGrantsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showRoleGrants}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowRoleGrants(PrestoSqlParser.ShowRoleGrantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctions(PrestoSqlParser.ShowFunctionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showFunctions}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctions(PrestoSqlParser.ShowFunctionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterShowSession(PrestoSqlParser.ShowSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitShowSession(PrestoSqlParser.ShowSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetSession(PrestoSqlParser.SetSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetSession(PrestoSqlParser.SetSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code resetSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterResetSession(PrestoSqlParser.ResetSessionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code resetSession}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitResetSession(PrestoSqlParser.ResetSessionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code startTransaction}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStartTransaction(PrestoSqlParser.StartTransactionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code startTransaction}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStartTransaction(PrestoSqlParser.StartTransactionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commit}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommit(PrestoSqlParser.CommitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commit}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommit(PrestoSqlParser.CommitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rollback}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRollback(PrestoSqlParser.RollbackContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rollback}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRollback(PrestoSqlParser.RollbackContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prepare}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrepare(PrestoSqlParser.PrepareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prepare}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrepare(PrestoSqlParser.PrepareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code deallocate}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDeallocate(PrestoSqlParser.DeallocateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code deallocate}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDeallocate(PrestoSqlParser.DeallocateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code execute}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExecute(PrestoSqlParser.ExecuteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code execute}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExecute(PrestoSqlParser.ExecuteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeInput}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeInput(PrestoSqlParser.DescribeInputContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeInput}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeInput(PrestoSqlParser.DescribeInputContext ctx);
	/**
	 * Enter a parse tree produced by the {@code describeOutput}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDescribeOutput(PrestoSqlParser.DescribeOutputContext ctx);
	/**
	 * Exit a parse tree produced by the {@code describeOutput}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDescribeOutput(PrestoSqlParser.DescribeOutputContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setPath}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSetPath(PrestoSqlParser.SetPathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setPath}
	 * labeled alternative in {@link PrestoSqlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSetPath(PrestoSqlParser.SetPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(PrestoSqlParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(PrestoSqlParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#with}.
	 * @param ctx the parse tree
	 */
	void enterWith(PrestoSqlParser.WithContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#with}.
	 * @param ctx the parse tree
	 */
	void exitWith(PrestoSqlParser.WithContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void enterTableElement(PrestoSqlParser.TableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void exitTableElement(PrestoSqlParser.TableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(PrestoSqlParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(PrestoSqlParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#likeClause}.
	 * @param ctx the parse tree
	 */
	void enterLikeClause(PrestoSqlParser.LikeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#likeClause}.
	 * @param ctx the parse tree
	 */
	void exitLikeClause(PrestoSqlParser.LikeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#properties}.
	 * @param ctx the parse tree
	 */
	void enterProperties(PrestoSqlParser.PropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#properties}.
	 * @param ctx the parse tree
	 */
	void exitProperties(PrestoSqlParser.PropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(PrestoSqlParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(PrestoSqlParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void enterQueryNoWith(PrestoSqlParser.QueryNoWithContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#queryNoWith}.
	 * @param ctx the parse tree
	 */
	void exitQueryNoWith(PrestoSqlParser.QueryNoWithContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link PrestoSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterQueryTermDefault(PrestoSqlParser.QueryTermDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryTermDefault}
	 * labeled alternative in {@link PrestoSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitQueryTermDefault(PrestoSqlParser.QueryTermDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link PrestoSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void enterSetOperation(PrestoSqlParser.SetOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setOperation}
	 * labeled alternative in {@link PrestoSqlParser#queryTerm}.
	 * @param ctx the parse tree
	 */
	void exitSetOperation(PrestoSqlParser.SetOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterQueryPrimaryDefault(PrestoSqlParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryPrimaryDefault}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitQueryPrimaryDefault(PrestoSqlParser.QueryPrimaryDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code table}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTable(PrestoSqlParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code table}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTable(PrestoSqlParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inlineTable}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterInlineTable(PrestoSqlParser.InlineTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inlineTable}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitInlineTable(PrestoSqlParser.InlineTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(PrestoSqlParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subquery}
	 * labeled alternative in {@link PrestoSqlParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(PrestoSqlParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void enterSortItem(PrestoSqlParser.SortItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#sortItem}.
	 * @param ctx the parse tree
	 */
	void exitSortItem(PrestoSqlParser.SortItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(PrestoSqlParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(PrestoSqlParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void enterGroupBy(PrestoSqlParser.GroupByContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#groupBy}.
	 * @param ctx the parse tree
	 */
	void exitGroupBy(PrestoSqlParser.GroupByContext ctx);
	/**
	 * Enter a parse tree produced by the {@code singleGroupingSet}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterSingleGroupingSet(PrestoSqlParser.SingleGroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code singleGroupingSet}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitSingleGroupingSet(PrestoSqlParser.SingleGroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rollup}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterRollup(PrestoSqlParser.RollupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rollup}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitRollup(PrestoSqlParser.RollupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cube}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterCube(PrestoSqlParser.CubeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cube}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitCube(PrestoSqlParser.CubeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multipleGroupingSets}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void enterMultipleGroupingSets(PrestoSqlParser.MultipleGroupingSetsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multipleGroupingSets}
	 * labeled alternative in {@link PrestoSqlParser#groupingElement}.
	 * @param ctx the parse tree
	 */
	void exitMultipleGroupingSets(PrestoSqlParser.MultipleGroupingSetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void enterGroupingSet(PrestoSqlParser.GroupingSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#groupingSet}.
	 * @param ctx the parse tree
	 */
	void exitGroupingSet(PrestoSqlParser.GroupingSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void enterNamedQuery(PrestoSqlParser.NamedQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#namedQuery}.
	 * @param ctx the parse tree
	 */
	void exitNamedQuery(PrestoSqlParser.NamedQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterSetQuantifier(PrestoSqlParser.SetQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#setQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitSetQuantifier(PrestoSqlParser.SetQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectSingle}
	 * labeled alternative in {@link PrestoSqlParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectSingle(PrestoSqlParser.SelectSingleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectSingle}
	 * labeled alternative in {@link PrestoSqlParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectSingle(PrestoSqlParser.SelectSingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectAll}
	 * labeled alternative in {@link PrestoSqlParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectAll(PrestoSqlParser.SelectAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectAll}
	 * labeled alternative in {@link PrestoSqlParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectAll(PrestoSqlParser.SelectAllContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relationDefault}
	 * labeled alternative in {@link PrestoSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelationDefault(PrestoSqlParser.RelationDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relationDefault}
	 * labeled alternative in {@link PrestoSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelationDefault(PrestoSqlParser.RelationDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code joinRelation}
	 * labeled alternative in {@link PrestoSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterJoinRelation(PrestoSqlParser.JoinRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code joinRelation}
	 * labeled alternative in {@link PrestoSqlParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitJoinRelation(PrestoSqlParser.JoinRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#joinType}.
	 * @param ctx the parse tree
	 */
	void enterJoinType(PrestoSqlParser.JoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#joinType}.
	 * @param ctx the parse tree
	 */
	void exitJoinType(PrestoSqlParser.JoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void enterJoinCriteria(PrestoSqlParser.JoinCriteriaContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#joinCriteria}.
	 * @param ctx the parse tree
	 */
	void exitJoinCriteria(PrestoSqlParser.JoinCriteriaContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#sampledRelation}.
	 * @param ctx the parse tree
	 */
	void enterSampledRelation(PrestoSqlParser.SampledRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#sampledRelation}.
	 * @param ctx the parse tree
	 */
	void exitSampledRelation(PrestoSqlParser.SampledRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#sampleType}.
	 * @param ctx the parse tree
	 */
	void enterSampleType(PrestoSqlParser.SampleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#sampleType}.
	 * @param ctx the parse tree
	 */
	void exitSampleType(PrestoSqlParser.SampleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#aliasedRelation}.
	 * @param ctx the parse tree
	 */
	void enterAliasedRelation(PrestoSqlParser.AliasedRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#aliasedRelation}.
	 * @param ctx the parse tree
	 */
	void exitAliasedRelation(PrestoSqlParser.AliasedRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void enterColumnAliases(PrestoSqlParser.ColumnAliasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#columnAliases}.
	 * @param ctx the parse tree
	 */
	void exitColumnAliases(PrestoSqlParser.ColumnAliasesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterTableName(PrestoSqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tableName}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitTableName(PrestoSqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryRelation}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryRelation(PrestoSqlParser.SubqueryRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryRelation}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryRelation(PrestoSqlParser.SubqueryRelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unnest}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterUnnest(PrestoSqlParser.UnnestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unnest}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitUnnest(PrestoSqlParser.UnnestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lateral}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterLateral(PrestoSqlParser.LateralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lateral}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitLateral(PrestoSqlParser.LateralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedRelation}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedRelation(PrestoSqlParser.ParenthesizedRelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedRelation}
	 * labeled alternative in {@link PrestoSqlParser#relationPrimary}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedRelation(PrestoSqlParser.ParenthesizedRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PrestoSqlParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PrestoSqlParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalNot(PrestoSqlParser.LogicalNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalNot}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalNot(PrestoSqlParser.LogicalNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterPredicated(PrestoSqlParser.PredicatedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicated}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitPredicated(PrestoSqlParser.PredicatedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalBinary(PrestoSqlParser.LogicalBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalBinary}
	 * labeled alternative in {@link PrestoSqlParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalBinary(PrestoSqlParser.LogicalBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterComparison(PrestoSqlParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparison}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitComparison(PrestoSqlParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quantifiedComparison}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedComparison(PrestoSqlParser.QuantifiedComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quantifiedComparison}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedComparison(PrestoSqlParser.QuantifiedComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code between}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterBetween(PrestoSqlParser.BetweenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code between}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitBetween(PrestoSqlParser.BetweenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inList}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInList(PrestoSqlParser.InListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inList}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInList(PrestoSqlParser.InListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inSubquery}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInSubquery(PrestoSqlParser.InSubqueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inSubquery}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInSubquery(PrestoSqlParser.InSubqueryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code like}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterLike(PrestoSqlParser.LikeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code like}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitLike(PrestoSqlParser.LikeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullPredicate}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterNullPredicate(PrestoSqlParser.NullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullPredicate}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitNullPredicate(PrestoSqlParser.NullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code distinctFrom}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterDistinctFrom(PrestoSqlParser.DistinctFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code distinctFrom}
	 * labeled alternative in {@link PrestoSqlParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitDistinctFrom(PrestoSqlParser.DistinctFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpressionDefault(PrestoSqlParser.ValueExpressionDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueExpressionDefault}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpressionDefault(PrestoSqlParser.ValueExpressionDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concatenation}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(PrestoSqlParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concatenation}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(PrestoSqlParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticBinary(PrestoSqlParser.ArithmeticBinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticBinary}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticBinary(PrestoSqlParser.ArithmeticBinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticUnary(PrestoSqlParser.ArithmeticUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticUnary}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticUnary(PrestoSqlParser.ArithmeticUnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atTimeZone}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtTimeZone(PrestoSqlParser.AtTimeZoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atTimeZone}
	 * labeled alternative in {@link PrestoSqlParser#valueExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtTimeZone(PrestoSqlParser.AtTimeZoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDereference(PrestoSqlParser.DereferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dereference}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDereference(PrestoSqlParser.DereferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstructor(PrestoSqlParser.TypeConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstructor(PrestoSqlParser.TypeConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specialDateTimeFunction}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSpecialDateTimeFunction(PrestoSqlParser.SpecialDateTimeFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specialDateTimeFunction}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSpecialDateTimeFunction(PrestoSqlParser.SpecialDateTimeFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substring}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubstring(PrestoSqlParser.SubstringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substring}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubstring(PrestoSqlParser.SubstringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cast}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCast(PrestoSqlParser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCast(PrestoSqlParser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambda(PrestoSqlParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambda}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambda(PrestoSqlParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(PrestoSqlParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpression}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(PrestoSqlParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParameter(PrestoSqlParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParameter(PrestoSqlParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code normalize}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNormalize(PrestoSqlParser.NormalizeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code normalize}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNormalize(PrestoSqlParser.NormalizeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalLiteral(PrestoSqlParser.IntervalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intervalLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalLiteral(PrestoSqlParser.IntervalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(PrestoSqlParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(PrestoSqlParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(PrestoSqlParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(PrestoSqlParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCase(PrestoSqlParser.SimpleCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleCase}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCase(PrestoSqlParser.SimpleCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterColumnReference(PrestoSqlParser.ColumnReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code columnReference}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitColumnReference(PrestoSqlParser.ColumnReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(PrestoSqlParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(PrestoSqlParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructor(PrestoSqlParser.RowConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructor(PrestoSqlParser.RowConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(PrestoSqlParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscript}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(PrestoSqlParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentPath}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentPath(PrestoSqlParser.CurrentPathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentPath}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentPath(PrestoSqlParser.CurrentPathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpression(PrestoSqlParser.SubqueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subqueryExpression}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpression(PrestoSqlParser.SubqueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryLiteral(PrestoSqlParser.BinaryLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryLiteral(PrestoSqlParser.BinaryLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentUser}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUser(PrestoSqlParser.CurrentUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentUser}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUser(PrestoSqlParser.CurrentUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extract}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtract(PrestoSqlParser.ExtractContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extract}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtract(PrestoSqlParser.ExtractContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(PrestoSqlParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(PrestoSqlParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterArrayConstructor(PrestoSqlParser.ArrayConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayConstructor}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitArrayConstructor(PrestoSqlParser.ArrayConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(PrestoSqlParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(PrestoSqlParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exists}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExists(PrestoSqlParser.ExistsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exists}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExists(PrestoSqlParser.ExistsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code position}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPosition(PrestoSqlParser.PositionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code position}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPosition(PrestoSqlParser.PositionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSearchedCase(PrestoSqlParser.SearchedCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code searchedCase}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSearchedCase(PrestoSqlParser.SearchedCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code groupingOperation}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterGroupingOperation(PrestoSqlParser.GroupingOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code groupingOperation}
	 * labeled alternative in {@link PrestoSqlParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitGroupingOperation(PrestoSqlParser.GroupingOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void enterNullTreatment(PrestoSqlParser.NullTreatmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void exitNullTreatment(PrestoSqlParser.NullTreatmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code basicStringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void enterBasicStringLiteral(PrestoSqlParser.BasicStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code basicStringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void exitBasicStringLiteral(PrestoSqlParser.BasicStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unicodeStringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void enterUnicodeStringLiteral(PrestoSqlParser.UnicodeStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unicodeStringLiteral}
	 * labeled alternative in {@link PrestoSqlParser#string}.
	 * @param ctx the parse tree
	 */
	void exitUnicodeStringLiteral(PrestoSqlParser.UnicodeStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timeZoneInterval}
	 * labeled alternative in {@link PrestoSqlParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTimeZoneInterval(PrestoSqlParser.TimeZoneIntervalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timeZoneInterval}
	 * labeled alternative in {@link PrestoSqlParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTimeZoneInterval(PrestoSqlParser.TimeZoneIntervalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code timeZoneString}
	 * labeled alternative in {@link PrestoSqlParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTimeZoneString(PrestoSqlParser.TimeZoneStringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code timeZoneString}
	 * labeled alternative in {@link PrestoSqlParser#timeZoneSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTimeZoneString(PrestoSqlParser.TimeZoneStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(PrestoSqlParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(PrestoSqlParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#comparisonQuantifier}.
	 * @param ctx the parse tree
	 */
	void enterComparisonQuantifier(PrestoSqlParser.ComparisonQuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#comparisonQuantifier}.
	 * @param ctx the parse tree
	 */
	void exitComparisonQuantifier(PrestoSqlParser.ComparisonQuantifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(PrestoSqlParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(PrestoSqlParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#interval}.
	 * @param ctx the parse tree
	 */
	void enterInterval(PrestoSqlParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#interval}.
	 * @param ctx the parse tree
	 */
	void exitInterval(PrestoSqlParser.IntervalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void enterIntervalField(PrestoSqlParser.IntervalFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#intervalField}.
	 * @param ctx the parse tree
	 */
	void exitIntervalField(PrestoSqlParser.IntervalFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void enterNormalForm(PrestoSqlParser.NormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void exitNormalForm(PrestoSqlParser.NormalFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PrestoSqlParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PrestoSqlParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(PrestoSqlParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(PrestoSqlParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(PrestoSqlParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(PrestoSqlParser.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(PrestoSqlParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(PrestoSqlParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(PrestoSqlParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(PrestoSqlParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#over}.
	 * @param ctx the parse tree
	 */
	void enterOver(PrestoSqlParser.OverContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#over}.
	 * @param ctx the parse tree
	 */
	void exitOver(PrestoSqlParser.OverContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void enterWindowFrame(PrestoSqlParser.WindowFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void exitWindowFrame(PrestoSqlParser.WindowFrameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unboundedFrame}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterUnboundedFrame(PrestoSqlParser.UnboundedFrameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unboundedFrame}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitUnboundedFrame(PrestoSqlParser.UnboundedFrameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentRowBound}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterCurrentRowBound(PrestoSqlParser.CurrentRowBoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentRowBound}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitCurrentRowBound(PrestoSqlParser.CurrentRowBoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boundedFrame}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void enterBoundedFrame(PrestoSqlParser.BoundedFrameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boundedFrame}
	 * labeled alternative in {@link PrestoSqlParser#frameBound}.
	 * @param ctx the parse tree
	 */
	void exitBoundedFrame(PrestoSqlParser.BoundedFrameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainFormat}
	 * labeled alternative in {@link PrestoSqlParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void enterExplainFormat(PrestoSqlParser.ExplainFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainFormat}
	 * labeled alternative in {@link PrestoSqlParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void exitExplainFormat(PrestoSqlParser.ExplainFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explainType}
	 * labeled alternative in {@link PrestoSqlParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void enterExplainType(PrestoSqlParser.ExplainTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explainType}
	 * labeled alternative in {@link PrestoSqlParser#explainOption}.
	 * @param ctx the parse tree
	 */
	void exitExplainType(PrestoSqlParser.ExplainTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isolationLevel}
	 * labeled alternative in {@link PrestoSqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterIsolationLevel(PrestoSqlParser.IsolationLevelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isolationLevel}
	 * labeled alternative in {@link PrestoSqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitIsolationLevel(PrestoSqlParser.IsolationLevelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code transactionAccessMode}
	 * labeled alternative in {@link PrestoSqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionAccessMode(PrestoSqlParser.TransactionAccessModeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code transactionAccessMode}
	 * labeled alternative in {@link PrestoSqlParser#transactionMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionAccessMode(PrestoSqlParser.TransactionAccessModeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readUncommitted}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterReadUncommitted(PrestoSqlParser.ReadUncommittedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readUncommitted}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitReadUncommitted(PrestoSqlParser.ReadUncommittedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readCommitted}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterReadCommitted(PrestoSqlParser.ReadCommittedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readCommitted}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitReadCommitted(PrestoSqlParser.ReadCommittedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code repeatableRead}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterRepeatableRead(PrestoSqlParser.RepeatableReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code repeatableRead}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitRepeatableRead(PrestoSqlParser.RepeatableReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code serializable}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void enterSerializable(PrestoSqlParser.SerializableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code serializable}
	 * labeled alternative in {@link PrestoSqlParser#levelOfIsolation}.
	 * @param ctx the parse tree
	 */
	void exitSerializable(PrestoSqlParser.SerializableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionalArgument}
	 * labeled alternative in {@link PrestoSqlParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgument(PrestoSqlParser.PositionalArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionalArgument}
	 * labeled alternative in {@link PrestoSqlParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgument(PrestoSqlParser.PositionalArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code namedArgument}
	 * labeled alternative in {@link PrestoSqlParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgument(PrestoSqlParser.NamedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code namedArgument}
	 * labeled alternative in {@link PrestoSqlParser#callArgument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgument(PrestoSqlParser.NamedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code qualifiedArgument}
	 * labeled alternative in {@link PrestoSqlParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedArgument(PrestoSqlParser.QualifiedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code qualifiedArgument}
	 * labeled alternative in {@link PrestoSqlParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedArgument(PrestoSqlParser.QualifiedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unqualifiedArgument}
	 * labeled alternative in {@link PrestoSqlParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedArgument(PrestoSqlParser.UnqualifiedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unqualifiedArgument}
	 * labeled alternative in {@link PrestoSqlParser#pathElement}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedArgument(PrestoSqlParser.UnqualifiedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#pathSpecification}.
	 * @param ctx the parse tree
	 */
	void enterPathSpecification(PrestoSqlParser.PathSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#pathSpecification}.
	 * @param ctx the parse tree
	 */
	void exitPathSpecification(PrestoSqlParser.PathSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void enterPrivilege(PrestoSqlParser.PrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#privilege}.
	 * @param ctx the parse tree
	 */
	void exitPrivilege(PrestoSqlParser.PrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(PrestoSqlParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(PrestoSqlParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code specifiedPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterSpecifiedPrincipal(PrestoSqlParser.SpecifiedPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code specifiedPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitSpecifiedPrincipal(PrestoSqlParser.SpecifiedPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentUserGrantor}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUserGrantor(PrestoSqlParser.CurrentUserGrantorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentUserGrantor}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUserGrantor(PrestoSqlParser.CurrentUserGrantorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code currentRoleGrantor}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void enterCurrentRoleGrantor(PrestoSqlParser.CurrentRoleGrantorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code currentRoleGrantor}
	 * labeled alternative in {@link PrestoSqlParser#grantor}.
	 * @param ctx the parse tree
	 */
	void exitCurrentRoleGrantor(PrestoSqlParser.CurrentRoleGrantorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unspecifiedPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterUnspecifiedPrincipal(PrestoSqlParser.UnspecifiedPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unspecifiedPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitUnspecifiedPrincipal(PrestoSqlParser.UnspecifiedPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code userPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterUserPrincipal(PrestoSqlParser.UserPrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code userPrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitUserPrincipal(PrestoSqlParser.UserPrincipalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rolePrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void enterRolePrincipal(PrestoSqlParser.RolePrincipalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rolePrincipal}
	 * labeled alternative in {@link PrestoSqlParser#principal}.
	 * @param ctx the parse tree
	 */
	void exitRolePrincipal(PrestoSqlParser.RolePrincipalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#roles}.
	 * @param ctx the parse tree
	 */
	void enterRoles(PrestoSqlParser.RolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#roles}.
	 * @param ctx the parse tree
	 */
	void exitRoles(PrestoSqlParser.RolesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedIdentifier(PrestoSqlParser.UnquotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unquotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedIdentifier(PrestoSqlParser.UnquotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code quotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterQuotedIdentifier(PrestoSqlParser.QuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code quotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitQuotedIdentifier(PrestoSqlParser.QuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code backQuotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterBackQuotedIdentifier(PrestoSqlParser.BackQuotedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code backQuotedIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitBackQuotedIdentifier(PrestoSqlParser.BackQuotedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code digitIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterDigitIdentifier(PrestoSqlParser.DigitIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code digitIdentifier}
	 * labeled alternative in {@link PrestoSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitDigitIdentifier(PrestoSqlParser.DigitIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(PrestoSqlParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(PrestoSqlParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterDoubleLiteral(PrestoSqlParser.DoubleLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doubleLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitDoubleLiteral(PrestoSqlParser.DoubleLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(PrestoSqlParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link PrestoSqlParser#number}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(PrestoSqlParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PrestoSqlParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void enterNonReserved(PrestoSqlParser.NonReservedContext ctx);
	/**
	 * Exit a parse tree produced by {@link PrestoSqlParser#nonReserved}.
	 * @param ctx the parse tree
	 */
	void exitNonReserved(PrestoSqlParser.NonReservedContext ctx);
}