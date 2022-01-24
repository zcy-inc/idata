package cn.zhengcaiyun.idata.connector.parser.presto;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrestoSqlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		ADD=10, ADMIN=11, ALL=12, ALTER=13, ANALYZE=14, AND=15, ANY=16, ARRAY=17, 
		AS=18, ASC=19, AT=20, BERNOULLI=21, BETWEEN=22, BY=23, CALL=24, CASCADE=25, 
		CASE=26, CAST=27, CATALOGS=28, COLUMN=29, COLUMNS=30, COMMENT=31, COMMIT=32, 
		COMMITTED=33, CONSTRAINT=34, CREATE=35, CROSS=36, CUBE=37, CURRENT=38, 
		CURRENT_DATE=39, CURRENT_PATH=40, CURRENT_ROLE=41, CURRENT_TIME=42, CURRENT_TIMESTAMP=43, 
		CURRENT_USER=44, DATA=45, DATE=46, DAY=47, DEALLOCATE=48, DEFINER=49, 
		DELETE=50, DESC=51, DESCRIBE=52, DISTINCT=53, DISTRIBUTED=54, DROP=55, 
		ELSE=56, END=57, ESCAPE=58, EXCEPT=59, EXCLUDING=60, EXECUTE=61, EXISTS=62, 
		EXPLAIN=63, EXTRACT=64, FALSE=65, FETCH=66, FILTER=67, FIRST=68, FOLLOWING=69, 
		FOR=70, FORMAT=71, FROM=72, FULL=73, FUNCTIONS=74, GRANT=75, GRANTED=76, 
		GRANTS=77, GRAPHVIZ=78, GROUP=79, GROUPING=80, HAVING=81, HOUR=82, IF=83, 
		IGNORE=84, IN=85, INCLUDING=86, INNER=87, INPUT=88, INSERT=89, INTERSECT=90, 
		INTERVAL=91, INTO=92, INVOKER=93, IO=94, IS=95, ISOLATION=96, JSON=97, 
		JOIN=98, LAST=99, LATERAL=100, LEFT=101, LEVEL=102, LIKE=103, LIMIT=104, 
		LOCALTIME=105, LOCALTIMESTAMP=106, LOGICAL=107, MAP=108, MINUTE=109, MONTH=110, 
		NATURAL=111, NEXT=112, NFC=113, NFD=114, NFKC=115, NFKD=116, NO=117, NONE=118, 
		NORMALIZE=119, NOT=120, NULL=121, NULLIF=122, NULLS=123, OFFSET=124, ON=125, 
		ONLY=126, OPTION=127, OR=128, ORDER=129, ORDINALITY=130, OUTER=131, OUTPUT=132, 
		OVER=133, PARTITION=134, PARTITIONS=135, PATH=136, POSITION=137, PRECEDING=138, 
		PREPARE=139, PRIVILEGES=140, PROPERTIES=141, RANGE=142, READ=143, RECURSIVE=144, 
		RENAME=145, REPEATABLE=146, REPLACE=147, RESET=148, RESPECT=149, RESTRICT=150, 
		REVOKE=151, RIGHT=152, ROLE=153, ROLES=154, ROLLBACK=155, ROLLUP=156, 
		ROW=157, ROWS=158, SCHEMA=159, SCHEMAS=160, SECOND=161, SECURITY=162, 
		SELECT=163, SERIALIZABLE=164, SESSION=165, SET=166, SETS=167, SHOW=168, 
		SOME=169, START=170, STATS=171, SUBSTRING=172, SYSTEM=173, TABLE=174, 
		TABLES=175, TABLESAMPLE=176, TEXT=177, THEN=178, TIES=179, TIME=180, TIMESTAMP=181, 
		TO=182, TRANSACTION=183, TRUE=184, TRY_CAST=185, TYPE=186, UESCAPE=187, 
		UNBOUNDED=188, UNCOMMITTED=189, UNION=190, UNNEST=191, USE=192, USER=193, 
		USING=194, VALIDATE=195, VALUES=196, VERBOSE=197, VIEW=198, WHEN=199, 
		WHERE=200, WITH=201, WORK=202, WRITE=203, YEAR=204, ZONE=205, EQ=206, 
		NEQ=207, LT=208, LTE=209, GT=210, GTE=211, PLUS=212, MINUS=213, ASTERISK=214, 
		SLASH=215, PERCENT=216, CONCAT=217, STRING=218, UNICODE_STRING=219, BINARY_LITERAL=220, 
		INTEGER_VALUE=221, DECIMAL_VALUE=222, DOUBLE_VALUE=223, IDENTIFIER=224, 
		DIGIT_IDENTIFIER=225, QUOTED_IDENTIFIER=226, BACKQUOTED_IDENTIFIER=227, 
		TIME_WITH_TIME_ZONE=228, TIMESTAMP_WITH_TIME_ZONE=229, DOUBLE_PRECISION=230, 
		SIMPLE_COMMENT=231, BRACKETED_COMMENT=232, WS=233, UNRECOGNIZED=234;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"ADD", "ADMIN", "ALL", "ALTER", "ANALYZE", "AND", "ANY", "ARRAY", "AS", 
			"ASC", "AT", "BERNOULLI", "BETWEEN", "BY", "CALL", "CASCADE", "CASE", 
			"CAST", "CATALOGS", "COLUMN", "COLUMNS", "COMMENT", "COMMIT", "COMMITTED", 
			"CONSTRAINT", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_PATH", 
			"CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", 
			"DATA", "DATE", "DAY", "DEALLOCATE", "DEFINER", "DELETE", "DESC", "DESCRIBE", 
			"DISTINCT", "DISTRIBUTED", "DROP", "ELSE", "END", "ESCAPE", "EXCEPT", 
			"EXCLUDING", "EXECUTE", "EXISTS", "EXPLAIN", "EXTRACT", "FALSE", "FETCH", 
			"FILTER", "FIRST", "FOLLOWING", "FOR", "FORMAT", "FROM", "FULL", "FUNCTIONS", 
			"GRANT", "GRANTED", "GRANTS", "GRAPHVIZ", "GROUP", "GROUPING", "HAVING", 
			"HOUR", "IF", "IGNORE", "IN", "INCLUDING", "INNER", "INPUT", "INSERT", 
			"INTERSECT", "INTERVAL", "INTO", "INVOKER", "IO", "IS", "ISOLATION", 
			"JSON", "JOIN", "LAST", "LATERAL", "LEFT", "LEVEL", "LIKE", "LIMIT", 
			"LOCALTIME", "LOCALTIMESTAMP", "LOGICAL", "MAP", "MINUTE", "MONTH", "NATURAL", 
			"NEXT", "NFC", "NFD", "NFKC", "NFKD", "NO", "NONE", "NORMALIZE", "NOT", 
			"NULL", "NULLIF", "NULLS", "OFFSET", "ON", "ONLY", "OPTION", "OR", "ORDER", 
			"ORDINALITY", "OUTER", "OUTPUT", "OVER", "PARTITION", "PARTITIONS", "PATH", 
			"POSITION", "PRECEDING", "PREPARE", "PRIVILEGES", "PROPERTIES", "RANGE", 
			"READ", "RECURSIVE", "RENAME", "REPEATABLE", "REPLACE", "RESET", "RESPECT", 
			"RESTRICT", "REVOKE", "RIGHT", "ROLE", "ROLES", "ROLLBACK", "ROLLUP", 
			"ROW", "ROWS", "SCHEMA", "SCHEMAS", "SECOND", "SECURITY", "SELECT", "SERIALIZABLE", 
			"SESSION", "SET", "SETS", "SHOW", "SOME", "START", "STATS", "SUBSTRING", 
			"SYSTEM", "TABLE", "TABLES", "TABLESAMPLE", "TEXT", "THEN", "TIES", "TIME", 
			"TIMESTAMP", "TO", "TRANSACTION", "TRUE", "TRY_CAST", "TYPE", "UESCAPE", 
			"UNBOUNDED", "UNCOMMITTED", "UNION", "UNNEST", "USE", "USER", "USING", 
			"VALIDATE", "VALUES", "VERBOSE", "VIEW", "WHEN", "WHERE", "WITH", "WORK", 
			"WRITE", "YEAR", "ZONE", "EQ", "NEQ", "LT", "LTE", "GT", "GTE", "PLUS", 
			"MINUS", "ASTERISK", "SLASH", "PERCENT", "CONCAT", "STRING", "UNICODE_STRING", 
			"BINARY_LITERAL", "INTEGER_VALUE", "DECIMAL_VALUE", "DOUBLE_VALUE", "IDENTIFIER", 
			"DIGIT_IDENTIFIER", "QUOTED_IDENTIFIER", "BACKQUOTED_IDENTIFIER", "TIME_WITH_TIME_ZONE", 
			"TIMESTAMP_WITH_TIME_ZONE", "DOUBLE_PRECISION", "EXPONENT", "DIGIT", 
			"LETTER", "SIMPLE_COMMENT", "BRACKETED_COMMENT", "WS", "UNRECOGNIZED"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'('", "')'", "','", "'?'", "'->'", "'['", "']'", "'=>'", 
			"'ADD'", "'ADMIN'", "'ALL'", "'ALTER'", "'ANALYZE'", "'AND'", "'ANY'", 
			"'ARRAY'", "'AS'", "'ASC'", "'AT'", "'BERNOULLI'", "'BETWEEN'", "'BY'", 
			"'CALL'", "'CASCADE'", "'CASE'", "'CAST'", "'CATALOGS'", "'COLUMN'", 
			"'COLUMNS'", "'COMMENT'", "'COMMIT'", "'COMMITTED'", "'CONSTRAINT'", 
			"'CREATE'", "'CROSS'", "'CUBE'", "'CURRENT'", "'CURRENT_DATE'", "'CURRENT_PATH'", 
			"'CURRENT_ROLE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'CURRENT_USER'", 
			"'DATA'", "'DATE'", "'DAY'", "'DEALLOCATE'", "'DEFINER'", "'DELETE'", 
			"'DESC'", "'DESCRIBE'", "'DISTINCT'", "'DISTRIBUTED'", "'DROP'", "'ELSE'", 
			"'END'", "'ESCAPE'", "'EXCEPT'", "'EXCLUDING'", "'EXECUTE'", "'EXISTS'", 
			"'EXPLAIN'", "'EXTRACT'", "'FALSE'", "'FETCH'", "'FILTER'", "'FIRST'", 
			"'FOLLOWING'", "'FOR'", "'FORMAT'", "'FROM'", "'FULL'", "'FUNCTIONS'", 
			"'GRANT'", "'GRANTED'", "'GRANTS'", "'GRAPHVIZ'", "'GROUP'", "'GROUPING'", 
			"'HAVING'", "'HOUR'", "'IF'", "'IGNORE'", "'IN'", "'INCLUDING'", "'INNER'", 
			"'INPUT'", "'INSERT'", "'INTERSECT'", "'INTERVAL'", "'INTO'", "'INVOKER'", 
			"'IO'", "'IS'", "'ISOLATION'", "'JSON'", "'JOIN'", "'LAST'", "'LATERAL'", 
			"'LEFT'", "'LEVEL'", "'LIKE'", "'LIMIT'", "'LOCALTIME'", "'LOCALTIMESTAMP'", 
			"'LOGICAL'", "'MAP'", "'MINUTE'", "'MONTH'", "'NATURAL'", "'NEXT'", "'NFC'", 
			"'NFD'", "'NFKC'", "'NFKD'", "'NO'", "'NONE'", "'NORMALIZE'", "'NOT'", 
			"'NULL'", "'NULLIF'", "'NULLS'", "'OFFSET'", "'ON'", "'ONLY'", "'OPTION'", 
			"'OR'", "'ORDER'", "'ORDINALITY'", "'OUTER'", "'OUTPUT'", "'OVER'", "'PARTITION'", 
			"'PARTITIONS'", "'PATH'", "'POSITION'", "'PRECEDING'", "'PREPARE'", "'PRIVILEGES'", 
			"'PROPERTIES'", "'RANGE'", "'READ'", "'RECURSIVE'", "'RENAME'", "'REPEATABLE'", 
			"'REPLACE'", "'RESET'", "'RESPECT'", "'RESTRICT'", "'REVOKE'", "'RIGHT'", 
			"'ROLE'", "'ROLES'", "'ROLLBACK'", "'ROLLUP'", "'ROW'", "'ROWS'", "'SCHEMA'", 
			"'SCHEMAS'", "'SECOND'", "'SECURITY'", "'SELECT'", "'SERIALIZABLE'", 
			"'SESSION'", "'SET'", "'SETS'", "'SHOW'", "'SOME'", "'START'", "'STATS'", 
			"'SUBSTRING'", "'SYSTEM'", "'TABLE'", "'TABLES'", "'TABLESAMPLE'", "'TEXT'", 
			"'THEN'", "'TIES'", "'TIME'", "'TIMESTAMP'", "'TO'", "'TRANSACTION'", 
			"'TRUE'", "'TRY_CAST'", "'TYPE'", "'UESCAPE'", "'UNBOUNDED'", "'UNCOMMITTED'", 
			"'UNION'", "'UNNEST'", "'USE'", "'USER'", "'USING'", "'VALIDATE'", "'VALUES'", 
			"'VERBOSE'", "'VIEW'", "'WHEN'", "'WHERE'", "'WITH'", "'WORK'", "'WRITE'", 
			"'YEAR'", "'ZONE'", "'='", null, "'<'", "'<='", "'>'", "'>='", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'||'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "ADD", "ADMIN", 
			"ALL", "ALTER", "ANALYZE", "AND", "ANY", "ARRAY", "AS", "ASC", "AT", 
			"BERNOULLI", "BETWEEN", "BY", "CALL", "CASCADE", "CASE", "CAST", "CATALOGS", 
			"COLUMN", "COLUMNS", "COMMENT", "COMMIT", "COMMITTED", "CONSTRAINT", 
			"CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_PATH", 
			"CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", 
			"DATA", "DATE", "DAY", "DEALLOCATE", "DEFINER", "DELETE", "DESC", "DESCRIBE", 
			"DISTINCT", "DISTRIBUTED", "DROP", "ELSE", "END", "ESCAPE", "EXCEPT", 
			"EXCLUDING", "EXECUTE", "EXISTS", "EXPLAIN", "EXTRACT", "FALSE", "FETCH", 
			"FILTER", "FIRST", "FOLLOWING", "FOR", "FORMAT", "FROM", "FULL", "FUNCTIONS", 
			"GRANT", "GRANTED", "GRANTS", "GRAPHVIZ", "GROUP", "GROUPING", "HAVING", 
			"HOUR", "IF", "IGNORE", "IN", "INCLUDING", "INNER", "INPUT", "INSERT", 
			"INTERSECT", "INTERVAL", "INTO", "INVOKER", "IO", "IS", "ISOLATION", 
			"JSON", "JOIN", "LAST", "LATERAL", "LEFT", "LEVEL", "LIKE", "LIMIT", 
			"LOCALTIME", "LOCALTIMESTAMP", "LOGICAL", "MAP", "MINUTE", "MONTH", "NATURAL", 
			"NEXT", "NFC", "NFD", "NFKC", "NFKD", "NO", "NONE", "NORMALIZE", "NOT", 
			"NULL", "NULLIF", "NULLS", "OFFSET", "ON", "ONLY", "OPTION", "OR", "ORDER", 
			"ORDINALITY", "OUTER", "OUTPUT", "OVER", "PARTITION", "PARTITIONS", "PATH", 
			"POSITION", "PRECEDING", "PREPARE", "PRIVILEGES", "PROPERTIES", "RANGE", 
			"READ", "RECURSIVE", "RENAME", "REPEATABLE", "REPLACE", "RESET", "RESPECT", 
			"RESTRICT", "REVOKE", "RIGHT", "ROLE", "ROLES", "ROLLBACK", "ROLLUP", 
			"ROW", "ROWS", "SCHEMA", "SCHEMAS", "SECOND", "SECURITY", "SELECT", "SERIALIZABLE", 
			"SESSION", "SET", "SETS", "SHOW", "SOME", "START", "STATS", "SUBSTRING", 
			"SYSTEM", "TABLE", "TABLES", "TABLESAMPLE", "TEXT", "THEN", "TIES", "TIME", 
			"TIMESTAMP", "TO", "TRANSACTION", "TRUE", "TRY_CAST", "TYPE", "UESCAPE", 
			"UNBOUNDED", "UNCOMMITTED", "UNION", "UNNEST", "USE", "USER", "USING", 
			"VALIDATE", "VALUES", "VERBOSE", "VIEW", "WHEN", "WHERE", "WITH", "WORK", 
			"WRITE", "YEAR", "ZONE", "EQ", "NEQ", "LT", "LTE", "GT", "GTE", "PLUS", 
			"MINUS", "ASTERISK", "SLASH", "PERCENT", "CONCAT", "STRING", "UNICODE_STRING", 
			"BINARY_LITERAL", "INTEGER_VALUE", "DECIMAL_VALUE", "DOUBLE_VALUE", "IDENTIFIER", 
			"DIGIT_IDENTIFIER", "QUOTED_IDENTIFIER", "BACKQUOTED_IDENTIFIER", "TIME_WITH_TIME_ZONE", 
			"TIMESTAMP_WITH_TIME_ZONE", "DOUBLE_PRECISION", "SIMPLE_COMMENT", "BRACKETED_COMMENT", 
			"WS", "UNRECOGNIZED"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PrestoSqlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "cn/gov/zcy/idata/parser/presto/PrestoSql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\u00ec\u085d\b\1\4"+
		"\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n"+
		"\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\t"+
		"T\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_"+
		"\4`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k"+
		"\tk\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv"+
		"\4w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t"+
		"\u0080\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084"+
		"\4\u0085\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089"+
		"\t\u0089\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d"+
		"\4\u008e\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092"+
		"\t\u0092\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096"+
		"\4\u0097\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\4\u009a\t\u009a\4\u009b"+
		"\t\u009b\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f"+
		"\4\u00a0\t\u00a0\4\u00a1\t\u00a1\4\u00a2\t\u00a2\4\u00a3\t\u00a3\4\u00a4"+
		"\t\u00a4\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8"+
		"\4\u00a9\t\u00a9\4\u00aa\t\u00aa\4\u00ab\t\u00ab\4\u00ac\t\u00ac\4\u00ad"+
		"\t\u00ad\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1"+
		"\4\u00b2\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6"+
		"\t\u00b6\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba"+
		"\4\u00bb\t\u00bb\4\u00bc\t\u00bc\4\u00bd\t\u00bd\4\u00be\t\u00be\4\u00bf"+
		"\t\u00bf\4\u00c0\t\u00c0\4\u00c1\t\u00c1\4\u00c2\t\u00c2\4\u00c3\t\u00c3"+
		"\4\u00c4\t\u00c4\4\u00c5\t\u00c5\4\u00c6\t\u00c6\4\u00c7\t\u00c7\4\u00c8"+
		"\t\u00c8\4\u00c9\t\u00c9\4\u00ca\t\u00ca\4\u00cb\t\u00cb\4\u00cc\t\u00cc"+
		"\4\u00cd\t\u00cd\4\u00ce\t\u00ce\4\u00cf\t\u00cf\4\u00d0\t\u00d0\4\u00d1"+
		"\t\u00d1\4\u00d2\t\u00d2\4\u00d3\t\u00d3\4\u00d4\t\u00d4\4\u00d5\t\u00d5"+
		"\4\u00d6\t\u00d6\4\u00d7\t\u00d7\4\u00d8\t\u00d8\4\u00d9\t\u00d9\4\u00da"+
		"\t\u00da\4\u00db\t\u00db\4\u00dc\t\u00dc\4\u00dd\t\u00dd\4\u00de\t\u00de"+
		"\4\u00df\t\u00df\4\u00e0\t\u00e0\4\u00e1\t\u00e1\4\u00e2\t\u00e2\4\u00e3"+
		"\t\u00e3\4\u00e4\t\u00e4\4\u00e5\t\u00e5\4\u00e6\t\u00e6\4\u00e7\t\u00e7"+
		"\4\u00e8\t\u00e8\4\u00e9\t\u00e9\4\u00ea\t\u00ea\4\u00eb\t\u00eb\4\u00ec"+
		"\t\u00ec\4\u00ed\t\u00ed\4\u00ee\t\u00ee\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3"+
		"$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3"+
		"\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)"+
		"\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+"+
		"\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,"+
		"\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3/\3/"+
		"\3/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61"+
		"\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\67"+
		"\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\38\38\38\38\3"+
		"8\39\39\39\39\39\3:\3:\3:\3:\3;\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3<\3"+
		"<\3=\3=\3=\3=\3=\3=\3=\3=\3=\3=\3>\3>\3>\3>\3>\3>\3>\3>\3?\3?\3?\3?\3"+
		"?\3?\3?\3@\3@\3@\3@\3@\3@\3@\3@\3A\3A\3A\3A\3A\3A\3A\3A\3B\3B\3B\3B\3"+
		"B\3B\3C\3C\3C\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\3E\3E\3E\3E\3E\3E\3F\3F\3"+
		"F\3F\3F\3F\3F\3F\3F\3F\3G\3G\3G\3G\3H\3H\3H\3H\3H\3H\3H\3I\3I\3I\3I\3"+
		"I\3J\3J\3J\3J\3J\3K\3K\3K\3K\3K\3K\3K\3K\3K\3K\3L\3L\3L\3L\3L\3L\3M\3"+
		"M\3M\3M\3M\3M\3M\3M\3N\3N\3N\3N\3N\3N\3N\3O\3O\3O\3O\3O\3O\3O\3O\3O\3"+
		"P\3P\3P\3P\3P\3P\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3Q\3R\3R\3R\3R\3R\3R\3R\3S\3"+
		"S\3S\3S\3S\3T\3T\3T\3U\3U\3U\3U\3U\3U\3U\3V\3V\3V\3W\3W\3W\3W\3W\3W\3"+
		"W\3W\3W\3W\3X\3X\3X\3X\3X\3X\3Y\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3"+
		"[\3[\3[\3[\3[\3[\3[\3[\3[\3[\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3]\3"+
		"]\3]\3]\3]\3^\3^\3^\3^\3^\3^\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3a\3a\3"+
		"a\3a\3a\3a\3a\3b\3b\3b\3b\3b\3c\3c\3c\3c\3c\3d\3d\3d\3d\3d\3e\3e\3e\3"+
		"e\3e\3e\3e\3e\3f\3f\3f\3f\3f\3g\3g\3g\3g\3g\3g\3h\3h\3h\3h\3h\3i\3i\3"+
		"i\3i\3i\3i\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3k\3k\3k\3k\3k\3k\3k\3k\3k\3"+
		"k\3k\3k\3k\3k\3k\3l\3l\3l\3l\3l\3l\3l\3l\3m\3m\3m\3m\3n\3n\3n\3n\3n\3"+
		"n\3n\3o\3o\3o\3o\3o\3o\3p\3p\3p\3p\3p\3p\3p\3p\3q\3q\3q\3q\3q\3r\3r\3"+
		"r\3r\3s\3s\3s\3s\3t\3t\3t\3t\3t\3u\3u\3u\3u\3u\3v\3v\3v\3w\3w\3w\3w\3"+
		"w\3x\3x\3x\3x\3x\3x\3x\3x\3x\3x\3y\3y\3y\3y\3z\3z\3z\3z\3z\3{\3{\3{\3"+
		"{\3{\3{\3{\3|\3|\3|\3|\3|\3|\3}\3}\3}\3}\3}\3}\3}\3~\3~\3~\3\177\3\177"+
		"\3\177\3\177\3\177\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3\u0080"+
		"\3\u0081\3\u0081\3\u0081\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082"+
		"\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083\3\u0083"+
		"\3\u0083\3\u0083\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0084\3\u0085"+
		"\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086"+
		"\3\u0086\3\u0086\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087\3\u0087"+
		"\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088"+
		"\3\u0088\3\u0088\3\u0088\3\u0088\3\u0088\3\u0089\3\u0089\3\u0089\3\u0089"+
		"\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a"+
		"\3\u008a\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b\3\u008b"+
		"\3\u008b\3\u008b\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c\3\u008c"+
		"\3\u008c\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d\3\u008d"+
		"\3\u008d\3\u008d\3\u008d\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e"+
		"\3\u008e\3\u008e\3\u008e\3\u008e\3\u008e\3\u008f\3\u008f\3\u008f\3\u008f"+
		"\3\u008f\3\u008f\3\u0090\3\u0090\3\u0090\3\u0090\3\u0090\3\u0091\3\u0091"+
		"\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091\3\u0091\3\u0092"+
		"\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092\3\u0093\3\u0093\3\u0093"+
		"\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0093\3\u0094"+
		"\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0095\3\u0095"+
		"\3\u0095\3\u0095\3\u0095\3\u0095\3\u0096\3\u0096\3\u0096\3\u0096\3\u0096"+
		"\3\u0096\3\u0096\3\u0096\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097\3\u0097"+
		"\3\u0097\3\u0097\3\u0097\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098\3\u0098"+
		"\3\u0098\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u009a\3\u009a"+
		"\3\u009a\3\u009a\3\u009a\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b\3\u009b"+
		"\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c\3\u009c"+
		"\3\u009d\3\u009d\3\u009d\3\u009d\3\u009d\3\u009d\3\u009d\3\u009e\3\u009e"+
		"\3\u009e\3\u009e\3\u009f\3\u009f\3\u009f\3\u009f\3\u009f\3\u00a0\3\u00a0"+
		"\3\u00a0\3\u00a0\3\u00a0\3\u00a0\3\u00a0\3\u00a1\3\u00a1\3\u00a1\3\u00a1"+
		"\3\u00a1\3\u00a1\3\u00a1\3\u00a1\3\u00a2\3\u00a2\3\u00a2\3\u00a2\3\u00a2"+
		"\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a3\3\u00a3\3\u00a3\3\u00a3\3\u00a3"+
		"\3\u00a3\3\u00a3\3\u00a4\3\u00a4\3\u00a4\3\u00a4\3\u00a4\3\u00a4\3\u00a4"+
		"\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a5"+
		"\3\u00a5\3\u00a5\3\u00a5\3\u00a5\3\u00a6\3\u00a6\3\u00a6\3\u00a6\3\u00a6"+
		"\3\u00a6\3\u00a6\3\u00a6\3\u00a7\3\u00a7\3\u00a7\3\u00a7\3\u00a8\3\u00a8"+
		"\3\u00a8\3\u00a8\3\u00a8\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00aa"+
		"\3\u00aa\3\u00aa\3\u00aa\3\u00aa\3\u00ab\3\u00ab\3\u00ab\3\u00ab\3\u00ab"+
		"\3\u00ab\3\u00ac\3\u00ac\3\u00ac\3\u00ac\3\u00ac\3\u00ac\3\u00ad\3\u00ad"+
		"\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ad\3\u00ae"+
		"\3\u00ae\3\u00ae\3\u00ae\3\u00ae\3\u00ae\3\u00ae\3\u00af\3\u00af\3\u00af"+
		"\3\u00af\3\u00af\3\u00af\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0\3\u00b0"+
		"\3\u00b0\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b1"+
		"\3\u00b1\3\u00b1\3\u00b1\3\u00b1\3\u00b2\3\u00b2\3\u00b2\3\u00b2\3\u00b2"+
		"\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b3\3\u00b4\3\u00b4\3\u00b4\3\u00b4"+
		"\3\u00b4\3\u00b5\3\u00b5\3\u00b5\3\u00b5\3\u00b5\3\u00b6\3\u00b6\3\u00b6"+
		"\3\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b7\3\u00b7"+
		"\3\u00b7\3\u00b8\3\u00b8\3\u00b8\3\u00b8\3\u00b8\3\u00b8\3\u00b8\3\u00b8"+
		"\3\u00b8\3\u00b8\3\u00b8\3\u00b8\3\u00b9\3\u00b9\3\u00b9\3\u00b9\3\u00b9"+
		"\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba\3\u00ba"+
		"\3\u00bb\3\u00bb\3\u00bb\3\u00bb\3\u00bb\3\u00bc\3\u00bc\3\u00bc\3\u00bc"+
		"\3\u00bc\3\u00bc\3\u00bc\3\u00bc\3\u00bd\3\u00bd\3\u00bd\3\u00bd\3\u00bd"+
		"\3\u00bd\3\u00bd\3\u00bd\3\u00bd\3\u00bd\3\u00be\3\u00be\3\u00be\3\u00be"+
		"\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00be\3\u00bf"+
		"\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00c0\3\u00c0\3\u00c0\3\u00c0"+
		"\3\u00c0\3\u00c0\3\u00c0\3\u00c1\3\u00c1\3\u00c1\3\u00c1\3\u00c2\3\u00c2"+
		"\3\u00c2\3\u00c2\3\u00c2\3\u00c3\3\u00c3\3\u00c3\3\u00c3\3\u00c3\3\u00c3"+
		"\3\u00c4\3\u00c4\3\u00c4\3\u00c4\3\u00c4\3\u00c4\3\u00c4\3\u00c4\3\u00c4"+
		"\3\u00c5\3\u00c5\3\u00c5\3\u00c5\3\u00c5\3\u00c5\3\u00c5\3\u00c6\3\u00c6"+
		"\3\u00c6\3\u00c6\3\u00c6\3\u00c6\3\u00c6\3\u00c6\3\u00c7\3\u00c7\3\u00c7"+
		"\3\u00c7\3\u00c7\3\u00c8\3\u00c8\3\u00c8\3\u00c8\3\u00c8\3\u00c9\3\u00c9"+
		"\3\u00c9\3\u00c9\3\u00c9\3\u00c9\3\u00ca\3\u00ca\3\u00ca\3\u00ca\3\u00ca"+
		"\3\u00cb\3\u00cb\3\u00cb\3\u00cb\3\u00cb\3\u00cc\3\u00cc\3\u00cc\3\u00cc"+
		"\3\u00cc\3\u00cc\3\u00cd\3\u00cd\3\u00cd\3\u00cd\3\u00cd\3\u00ce\3\u00ce"+
		"\3\u00ce\3\u00ce\3\u00ce\3\u00cf\3\u00cf\3\u00d0\3\u00d0\3\u00d0\3\u00d0"+
		"\5\u00d0\u074a\n\u00d0\3\u00d1\3\u00d1\3\u00d2\3\u00d2\3\u00d2\3\u00d3"+
		"\3\u00d3\3\u00d4\3\u00d4\3\u00d4\3\u00d5\3\u00d5\3\u00d6\3\u00d6\3\u00d7"+
		"\3\u00d7\3\u00d8\3\u00d8\3\u00d9\3\u00d9\3\u00da\3\u00da\3\u00da\3\u00db"+
		"\3\u00db\3\u00db\3\u00db\7\u00db\u0767\n\u00db\f\u00db\16\u00db\u076a"+
		"\13\u00db\3\u00db\3\u00db\3\u00dc\3\u00dc\3\u00dc\3\u00dc\3\u00dc\3\u00dc"+
		"\3\u00dc\7\u00dc\u0775\n\u00dc\f\u00dc\16\u00dc\u0778\13\u00dc\3\u00dc"+
		"\3\u00dc\3\u00dd\3\u00dd\3\u00dd\3\u00dd\7\u00dd\u0780\n\u00dd\f\u00dd"+
		"\16\u00dd\u0783\13\u00dd\3\u00dd\3\u00dd\3\u00de\6\u00de\u0788\n\u00de"+
		"\r\u00de\16\u00de\u0789\3\u00df\6\u00df\u078d\n\u00df\r\u00df\16\u00df"+
		"\u078e\3\u00df\3\u00df\7\u00df\u0793\n\u00df\f\u00df\16\u00df\u0796\13"+
		"\u00df\3\u00df\3\u00df\6\u00df\u079a\n\u00df\r\u00df\16\u00df\u079b\5"+
		"\u00df\u079e\n\u00df\3\u00e0\6\u00e0\u07a1\n\u00e0\r\u00e0\16\u00e0\u07a2"+
		"\3\u00e0\3\u00e0\7\u00e0\u07a7\n\u00e0\f\u00e0\16\u00e0\u07aa\13\u00e0"+
		"\5\u00e0\u07ac\n\u00e0\3\u00e0\3\u00e0\3\u00e0\3\u00e0\6\u00e0\u07b2\n"+
		"\u00e0\r\u00e0\16\u00e0\u07b3\3\u00e0\3\u00e0\5\u00e0\u07b8\n\u00e0\3"+
		"\u00e1\3\u00e1\5\u00e1\u07bc\n\u00e1\3\u00e1\3\u00e1\3\u00e1\7\u00e1\u07c1"+
		"\n\u00e1\f\u00e1\16\u00e1\u07c4\13\u00e1\3\u00e2\3\u00e2\3\u00e2\3\u00e2"+
		"\6\u00e2\u07ca\n\u00e2\r\u00e2\16\u00e2\u07cb\3\u00e3\3\u00e3\3\u00e3"+
		"\3\u00e3\7\u00e3\u07d2\n\u00e3\f\u00e3\16\u00e3\u07d5\13\u00e3\3\u00e3"+
		"\3\u00e3\3\u00e4\3\u00e4\3\u00e4\3\u00e4\7\u00e4\u07dd\n\u00e4\f\u00e4"+
		"\16\u00e4\u07e0\13\u00e4\3\u00e4\3\u00e4\3\u00e5\3\u00e5\3\u00e5\3\u00e5"+
		"\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5"+
		"\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5\3\u00e5"+
		"\3\u00e5\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6"+
		"\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6"+
		"\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6\3\u00e6"+
		"\3\u00e6\3\u00e6\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7"+
		"\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7\3\u00e7"+
		"\3\u00e7\3\u00e7\3\u00e8\3\u00e8\5\u00e8\u082b\n\u00e8\3\u00e8\6\u00e8"+
		"\u082e\n\u00e8\r\u00e8\16\u00e8\u082f\3\u00e9\3\u00e9\3\u00ea\3\u00ea"+
		"\3\u00eb\3\u00eb\3\u00eb\3\u00eb\7\u00eb\u083a\n\u00eb\f\u00eb\16\u00eb"+
		"\u083d\13\u00eb\3\u00eb\5\u00eb\u0840\n\u00eb\3\u00eb\5\u00eb\u0843\n"+
		"\u00eb\3\u00eb\3\u00eb\3\u00ec\3\u00ec\3\u00ec\3\u00ec\7\u00ec\u084b\n"+
		"\u00ec\f\u00ec\16\u00ec\u084e\13\u00ec\3\u00ec\3\u00ec\3\u00ec\3\u00ec"+
		"\3\u00ec\3\u00ed\6\u00ed\u0856\n\u00ed\r\u00ed\16\u00ed\u0857\3\u00ed"+
		"\3\u00ed\3\u00ee\3\u00ee\3\u084c\2\u00ef\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27"+
		"-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W"+
		"-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083"+
		"C\u0085D\u0087E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097"+
		"M\u0099N\u009bO\u009dP\u009fQ\u00a1R\u00a3S\u00a5T\u00a7U\u00a9V\u00ab"+
		"W\u00adX\u00afY\u00b1Z\u00b3[\u00b5\\\u00b7]\u00b9^\u00bb_\u00bd`\u00bf"+
		"a\u00c1b\u00c3c\u00c5d\u00c7e\u00c9f\u00cbg\u00cdh\u00cfi\u00d1j\u00d3"+
		"k\u00d5l\u00d7m\u00d9n\u00dbo\u00ddp\u00dfq\u00e1r\u00e3s\u00e5t\u00e7"+
		"u\u00e9v\u00ebw\u00edx\u00efy\u00f1z\u00f3{\u00f5|\u00f7}\u00f9~\u00fb"+
		"\177\u00fd\u0080\u00ff\u0081\u0101\u0082\u0103\u0083\u0105\u0084\u0107"+
		"\u0085\u0109\u0086\u010b\u0087\u010d\u0088\u010f\u0089\u0111\u008a\u0113"+
		"\u008b\u0115\u008c\u0117\u008d\u0119\u008e\u011b\u008f\u011d\u0090\u011f"+
		"\u0091\u0121\u0092\u0123\u0093\u0125\u0094\u0127\u0095\u0129\u0096\u012b"+
		"\u0097\u012d\u0098\u012f\u0099\u0131\u009a\u0133\u009b\u0135\u009c\u0137"+
		"\u009d\u0139\u009e\u013b\u009f\u013d\u00a0\u013f\u00a1\u0141\u00a2\u0143"+
		"\u00a3\u0145\u00a4\u0147\u00a5\u0149\u00a6\u014b\u00a7\u014d\u00a8\u014f"+
		"\u00a9\u0151\u00aa\u0153\u00ab\u0155\u00ac\u0157\u00ad\u0159\u00ae\u015b"+
		"\u00af\u015d\u00b0\u015f\u00b1\u0161\u00b2\u0163\u00b3\u0165\u00b4\u0167"+
		"\u00b5\u0169\u00b6\u016b\u00b7\u016d\u00b8\u016f\u00b9\u0171\u00ba\u0173"+
		"\u00bb\u0175\u00bc\u0177\u00bd\u0179\u00be\u017b\u00bf\u017d\u00c0\u017f"+
		"\u00c1\u0181\u00c2\u0183\u00c3\u0185\u00c4\u0187\u00c5\u0189\u00c6\u018b"+
		"\u00c7\u018d\u00c8\u018f\u00c9\u0191\u00ca\u0193\u00cb\u0195\u00cc\u0197"+
		"\u00cd\u0199\u00ce\u019b\u00cf\u019d\u00d0\u019f\u00d1\u01a1\u00d2\u01a3"+
		"\u00d3\u01a5\u00d4\u01a7\u00d5\u01a9\u00d6\u01ab\u00d7\u01ad\u00d8\u01af"+
		"\u00d9\u01b1\u00da\u01b3\u00db\u01b5\u00dc\u01b7\u00dd\u01b9\u00de\u01bb"+
		"\u00df\u01bd\u00e0\u01bf\u00e1\u01c1\u00e2\u01c3\u00e3\u01c5\u00e4\u01c7"+
		"\u00e5\u01c9\u00e6\u01cb\u00e7\u01cd\u00e8\u01cf\2\u01d1\2\u01d3\2\u01d5"+
		"\u00e9\u01d7\u00ea\u01d9\u00eb\u01db\u00ec\3\2\13\3\2))\5\2<<BBaa\3\2"+
		"$$\3\2bb\4\2--//\3\2\62;\3\2C\\\4\2\f\f\17\17\5\2\13\f\17\17\"\"\2\u087b"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3"+
		"\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2"+
		"\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2"+
		"{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097"+
		"\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2"+
		"\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9"+
		"\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2"+
		"\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb"+
		"\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2"+
		"\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd"+
		"\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2"+
		"\2\2\u00d7\3\2\2\2\2\u00d9\3\2\2\2\2\u00db\3\2\2\2\2\u00dd\3\2\2\2\2\u00df"+
		"\3\2\2\2\2\u00e1\3\2\2\2\2\u00e3\3\2\2\2\2\u00e5\3\2\2\2\2\u00e7\3\2\2"+
		"\2\2\u00e9\3\2\2\2\2\u00eb\3\2\2\2\2\u00ed\3\2\2\2\2\u00ef\3\2\2\2\2\u00f1"+
		"\3\2\2\2\2\u00f3\3\2\2\2\2\u00f5\3\2\2\2\2\u00f7\3\2\2\2\2\u00f9\3\2\2"+
		"\2\2\u00fb\3\2\2\2\2\u00fd\3\2\2\2\2\u00ff\3\2\2\2\2\u0101\3\2\2\2\2\u0103"+
		"\3\2\2\2\2\u0105\3\2\2\2\2\u0107\3\2\2\2\2\u0109\3\2\2\2\2\u010b\3\2\2"+
		"\2\2\u010d\3\2\2\2\2\u010f\3\2\2\2\2\u0111\3\2\2\2\2\u0113\3\2\2\2\2\u0115"+
		"\3\2\2\2\2\u0117\3\2\2\2\2\u0119\3\2\2\2\2\u011b\3\2\2\2\2\u011d\3\2\2"+
		"\2\2\u011f\3\2\2\2\2\u0121\3\2\2\2\2\u0123\3\2\2\2\2\u0125\3\2\2\2\2\u0127"+
		"\3\2\2\2\2\u0129\3\2\2\2\2\u012b\3\2\2\2\2\u012d\3\2\2\2\2\u012f\3\2\2"+
		"\2\2\u0131\3\2\2\2\2\u0133\3\2\2\2\2\u0135\3\2\2\2\2\u0137\3\2\2\2\2\u0139"+
		"\3\2\2\2\2\u013b\3\2\2\2\2\u013d\3\2\2\2\2\u013f\3\2\2\2\2\u0141\3\2\2"+
		"\2\2\u0143\3\2\2\2\2\u0145\3\2\2\2\2\u0147\3\2\2\2\2\u0149\3\2\2\2\2\u014b"+
		"\3\2\2\2\2\u014d\3\2\2\2\2\u014f\3\2\2\2\2\u0151\3\2\2\2\2\u0153\3\2\2"+
		"\2\2\u0155\3\2\2\2\2\u0157\3\2\2\2\2\u0159\3\2\2\2\2\u015b\3\2\2\2\2\u015d"+
		"\3\2\2\2\2\u015f\3\2\2\2\2\u0161\3\2\2\2\2\u0163\3\2\2\2\2\u0165\3\2\2"+
		"\2\2\u0167\3\2\2\2\2\u0169\3\2\2\2\2\u016b\3\2\2\2\2\u016d\3\2\2\2\2\u016f"+
		"\3\2\2\2\2\u0171\3\2\2\2\2\u0173\3\2\2\2\2\u0175\3\2\2\2\2\u0177\3\2\2"+
		"\2\2\u0179\3\2\2\2\2\u017b\3\2\2\2\2\u017d\3\2\2\2\2\u017f\3\2\2\2\2\u0181"+
		"\3\2\2\2\2\u0183\3\2\2\2\2\u0185\3\2\2\2\2\u0187\3\2\2\2\2\u0189\3\2\2"+
		"\2\2\u018b\3\2\2\2\2\u018d\3\2\2\2\2\u018f\3\2\2\2\2\u0191\3\2\2\2\2\u0193"+
		"\3\2\2\2\2\u0195\3\2\2\2\2\u0197\3\2\2\2\2\u0199\3\2\2\2\2\u019b\3\2\2"+
		"\2\2\u019d\3\2\2\2\2\u019f\3\2\2\2\2\u01a1\3\2\2\2\2\u01a3\3\2\2\2\2\u01a5"+
		"\3\2\2\2\2\u01a7\3\2\2\2\2\u01a9\3\2\2\2\2\u01ab\3\2\2\2\2\u01ad\3\2\2"+
		"\2\2\u01af\3\2\2\2\2\u01b1\3\2\2\2\2\u01b3\3\2\2\2\2\u01b5\3\2\2\2\2\u01b7"+
		"\3\2\2\2\2\u01b9\3\2\2\2\2\u01bb\3\2\2\2\2\u01bd\3\2\2\2\2\u01bf\3\2\2"+
		"\2\2\u01c1\3\2\2\2\2\u01c3\3\2\2\2\2\u01c5\3\2\2\2\2\u01c7\3\2\2\2\2\u01c9"+
		"\3\2\2\2\2\u01cb\3\2\2\2\2\u01cd\3\2\2\2\2\u01d5\3\2\2\2\2\u01d7\3\2\2"+
		"\2\2\u01d9\3\2\2\2\2\u01db\3\2\2\2\3\u01dd\3\2\2\2\5\u01df\3\2\2\2\7\u01e1"+
		"\3\2\2\2\t\u01e3\3\2\2\2\13\u01e5\3\2\2\2\r\u01e7\3\2\2\2\17\u01ea\3\2"+
		"\2\2\21\u01ec\3\2\2\2\23\u01ee\3\2\2\2\25\u01f1\3\2\2\2\27\u01f5\3\2\2"+
		"\2\31\u01fb\3\2\2\2\33\u01ff\3\2\2\2\35\u0205\3\2\2\2\37\u020d\3\2\2\2"+
		"!\u0211\3\2\2\2#\u0215\3\2\2\2%\u021b\3\2\2\2\'\u021e\3\2\2\2)\u0222\3"+
		"\2\2\2+\u0225\3\2\2\2-\u022f\3\2\2\2/\u0237\3\2\2\2\61\u023a\3\2\2\2\63"+
		"\u023f\3\2\2\2\65\u0247\3\2\2\2\67\u024c\3\2\2\29\u0251\3\2\2\2;\u025a"+
		"\3\2\2\2=\u0261\3\2\2\2?\u0269\3\2\2\2A\u0271\3\2\2\2C\u0278\3\2\2\2E"+
		"\u0282\3\2\2\2G\u028d\3\2\2\2I\u0294\3\2\2\2K\u029a\3\2\2\2M\u029f\3\2"+
		"\2\2O\u02a7\3\2\2\2Q\u02b4\3\2\2\2S\u02c1\3\2\2\2U\u02ce\3\2\2\2W\u02db"+
		"\3\2\2\2Y\u02ed\3\2\2\2[\u02fa\3\2\2\2]\u02ff\3\2\2\2_\u0304\3\2\2\2a"+
		"\u0308\3\2\2\2c\u0313\3\2\2\2e\u031b\3\2\2\2g\u0322\3\2\2\2i\u0327\3\2"+
		"\2\2k\u0330\3\2\2\2m\u0339\3\2\2\2o\u0345\3\2\2\2q\u034a\3\2\2\2s\u034f"+
		"\3\2\2\2u\u0353\3\2\2\2w\u035a\3\2\2\2y\u0361\3\2\2\2{\u036b\3\2\2\2}"+
		"\u0373\3\2\2\2\177\u037a\3\2\2\2\u0081\u0382\3\2\2\2\u0083\u038a\3\2\2"+
		"\2\u0085\u0390\3\2\2\2\u0087\u0396\3\2\2\2\u0089\u039d\3\2\2\2\u008b\u03a3"+
		"\3\2\2\2\u008d\u03ad\3\2\2\2\u008f\u03b1\3\2\2\2\u0091\u03b8\3\2\2\2\u0093"+
		"\u03bd\3\2\2\2\u0095\u03c2\3\2\2\2\u0097\u03cc\3\2\2\2\u0099\u03d2\3\2"+
		"\2\2\u009b\u03da\3\2\2\2\u009d\u03e1\3\2\2\2\u009f\u03ea\3\2\2\2\u00a1"+
		"\u03f0\3\2\2\2\u00a3\u03f9\3\2\2\2\u00a5\u0400\3\2\2\2\u00a7\u0405\3\2"+
		"\2\2\u00a9\u0408\3\2\2\2\u00ab\u040f\3\2\2\2\u00ad\u0412\3\2\2\2\u00af"+
		"\u041c\3\2\2\2\u00b1\u0422\3\2\2\2\u00b3\u0428\3\2\2\2\u00b5\u042f\3\2"+
		"\2\2\u00b7\u0439\3\2\2\2\u00b9\u0442\3\2\2\2\u00bb\u0447\3\2\2\2\u00bd"+
		"\u044f\3\2\2\2\u00bf\u0452\3\2\2\2\u00c1\u0455\3\2\2\2\u00c3\u045f\3\2"+
		"\2\2\u00c5\u0464\3\2\2\2\u00c7\u0469\3\2\2\2\u00c9\u046e\3\2\2\2\u00cb"+
		"\u0476\3\2\2\2\u00cd\u047b\3\2\2\2\u00cf\u0481\3\2\2\2\u00d1\u0486\3\2"+
		"\2\2\u00d3\u048c\3\2\2\2\u00d5\u0496\3\2\2\2\u00d7\u04a5\3\2\2\2\u00d9"+
		"\u04ad\3\2\2\2\u00db\u04b1\3\2\2\2\u00dd\u04b8\3\2\2\2\u00df\u04be\3\2"+
		"\2\2\u00e1\u04c6\3\2\2\2\u00e3\u04cb\3\2\2\2\u00e5\u04cf\3\2\2\2\u00e7"+
		"\u04d3\3\2\2\2\u00e9\u04d8\3\2\2\2\u00eb\u04dd\3\2\2\2\u00ed\u04e0\3\2"+
		"\2\2\u00ef\u04e5\3\2\2\2\u00f1\u04ef\3\2\2\2\u00f3\u04f3\3\2\2\2\u00f5"+
		"\u04f8\3\2\2\2\u00f7\u04ff\3\2\2\2\u00f9\u0505\3\2\2\2\u00fb\u050c\3\2"+
		"\2\2\u00fd\u050f\3\2\2\2\u00ff\u0514\3\2\2\2\u0101\u051b\3\2\2\2\u0103"+
		"\u051e\3\2\2\2\u0105\u0524\3\2\2\2\u0107\u052f\3\2\2\2\u0109\u0535\3\2"+
		"\2\2\u010b\u053c\3\2\2\2\u010d\u0541\3\2\2\2\u010f\u054b\3\2\2\2\u0111"+
		"\u0556\3\2\2\2\u0113\u055b\3\2\2\2\u0115\u0564\3\2\2\2\u0117\u056e\3\2"+
		"\2\2\u0119\u0576\3\2\2\2\u011b\u0581\3\2\2\2\u011d\u058c\3\2\2\2\u011f"+
		"\u0592\3\2\2\2\u0121\u0597\3\2\2\2\u0123\u05a1\3\2\2\2\u0125\u05a8\3\2"+
		"\2\2\u0127\u05b3\3\2\2\2\u0129\u05bb\3\2\2\2\u012b\u05c1\3\2\2\2\u012d"+
		"\u05c9\3\2\2\2\u012f\u05d2\3\2\2\2\u0131\u05d9\3\2\2\2\u0133\u05df\3\2"+
		"\2\2\u0135\u05e4\3\2\2\2\u0137\u05ea\3\2\2\2\u0139\u05f3\3\2\2\2\u013b"+
		"\u05fa\3\2\2\2\u013d\u05fe\3\2\2\2\u013f\u0603\3\2\2\2\u0141\u060a\3\2"+
		"\2\2\u0143\u0612\3\2\2\2\u0145\u0619\3\2\2\2\u0147\u0622\3\2\2\2\u0149"+
		"\u0629\3\2\2\2\u014b\u0636\3\2\2\2\u014d\u063e\3\2\2\2\u014f\u0642\3\2"+
		"\2\2\u0151\u0647\3\2\2\2\u0153\u064c\3\2\2\2\u0155\u0651\3\2\2\2\u0157"+
		"\u0657\3\2\2\2\u0159\u065d\3\2\2\2\u015b\u0667\3\2\2\2\u015d\u066e\3\2"+
		"\2\2\u015f\u0674\3\2\2\2\u0161\u067b\3\2\2\2\u0163\u0687\3\2\2\2\u0165"+
		"\u068c\3\2\2\2\u0167\u0691\3\2\2\2\u0169\u0696\3\2\2\2\u016b\u069b\3\2"+
		"\2\2\u016d\u06a5\3\2\2\2\u016f\u06a8\3\2\2\2\u0171\u06b4\3\2\2\2\u0173"+
		"\u06b9\3\2\2\2\u0175\u06c2\3\2\2\2\u0177\u06c7\3\2\2\2\u0179\u06cf\3\2"+
		"\2\2\u017b\u06d9\3\2\2\2\u017d\u06e5\3\2\2\2\u017f\u06eb\3\2\2\2\u0181"+
		"\u06f2\3\2\2\2\u0183\u06f6\3\2\2\2\u0185\u06fb\3\2\2\2\u0187\u0701\3\2"+
		"\2\2\u0189\u070a\3\2\2\2\u018b\u0711\3\2\2\2\u018d\u0719\3\2\2\2\u018f"+
		"\u071e\3\2\2\2\u0191\u0723\3\2\2\2\u0193\u0729\3\2\2\2\u0195\u072e\3\2"+
		"\2\2\u0197\u0733\3\2\2\2\u0199\u0739\3\2\2\2\u019b\u073e\3\2\2\2\u019d"+
		"\u0743\3\2\2\2\u019f\u0749\3\2\2\2\u01a1\u074b\3\2\2\2\u01a3\u074d\3\2"+
		"\2\2\u01a5\u0750\3\2\2\2\u01a7\u0752\3\2\2\2\u01a9\u0755\3\2\2\2\u01ab"+
		"\u0757\3\2\2\2\u01ad\u0759\3\2\2\2\u01af\u075b\3\2\2\2\u01b1\u075d\3\2"+
		"\2\2\u01b3\u075f\3\2\2\2\u01b5\u0762\3\2\2\2\u01b7\u076d\3\2\2\2\u01b9"+
		"\u077b\3\2\2\2\u01bb\u0787\3\2\2\2\u01bd\u079d\3\2\2\2\u01bf\u07b7\3\2"+
		"\2\2\u01c1\u07bb\3\2\2\2\u01c3\u07c5\3\2\2\2\u01c5\u07cd\3\2\2\2\u01c7"+
		"\u07d8\3\2\2\2\u01c9\u07e3\3\2\2\2\u01cb\u07fa\3\2\2\2\u01cd\u0816\3\2"+
		"\2\2\u01cf\u0828\3\2\2\2\u01d1\u0831\3\2\2\2\u01d3\u0833\3\2\2\2\u01d5"+
		"\u0835\3\2\2\2\u01d7\u0846\3\2\2\2\u01d9\u0855\3\2\2\2\u01db\u085b\3\2"+
		"\2\2\u01dd\u01de\7\60\2\2\u01de\4\3\2\2\2\u01df\u01e0\7*\2\2\u01e0\6\3"+
		"\2\2\2\u01e1\u01e2\7+\2\2\u01e2\b\3\2\2\2\u01e3\u01e4\7.\2\2\u01e4\n\3"+
		"\2\2\2\u01e5\u01e6\7A\2\2\u01e6\f\3\2\2\2\u01e7\u01e8\7/\2\2\u01e8\u01e9"+
		"\7@\2\2\u01e9\16\3\2\2\2\u01ea\u01eb\7]\2\2\u01eb\20\3\2\2\2\u01ec\u01ed"+
		"\7_\2\2\u01ed\22\3\2\2\2\u01ee\u01ef\7?\2\2\u01ef\u01f0\7@\2\2\u01f0\24"+
		"\3\2\2\2\u01f1\u01f2\7C\2\2\u01f2\u01f3\7F\2\2\u01f3\u01f4\7F\2\2\u01f4"+
		"\26\3\2\2\2\u01f5\u01f6\7C\2\2\u01f6\u01f7\7F\2\2\u01f7\u01f8\7O\2\2\u01f8"+
		"\u01f9\7K\2\2\u01f9\u01fa\7P\2\2\u01fa\30\3\2\2\2\u01fb\u01fc\7C\2\2\u01fc"+
		"\u01fd\7N\2\2\u01fd\u01fe\7N\2\2\u01fe\32\3\2\2\2\u01ff\u0200\7C\2\2\u0200"+
		"\u0201\7N\2\2\u0201\u0202\7V\2\2\u0202\u0203\7G\2\2\u0203\u0204\7T\2\2"+
		"\u0204\34\3\2\2\2\u0205\u0206\7C\2\2\u0206\u0207\7P\2\2\u0207\u0208\7"+
		"C\2\2\u0208\u0209\7N\2\2\u0209\u020a\7[\2\2\u020a\u020b\7\\\2\2\u020b"+
		"\u020c\7G\2\2\u020c\36\3\2\2\2\u020d\u020e\7C\2\2\u020e\u020f\7P\2\2\u020f"+
		"\u0210\7F\2\2\u0210 \3\2\2\2\u0211\u0212\7C\2\2\u0212\u0213\7P\2\2\u0213"+
		"\u0214\7[\2\2\u0214\"\3\2\2\2\u0215\u0216\7C\2\2\u0216\u0217\7T\2\2\u0217"+
		"\u0218\7T\2\2\u0218\u0219\7C\2\2\u0219\u021a\7[\2\2\u021a$\3\2\2\2\u021b"+
		"\u021c\7C\2\2\u021c\u021d\7U\2\2\u021d&\3\2\2\2\u021e\u021f\7C\2\2\u021f"+
		"\u0220\7U\2\2\u0220\u0221\7E\2\2\u0221(\3\2\2\2\u0222\u0223\7C\2\2\u0223"+
		"\u0224\7V\2\2\u0224*\3\2\2\2\u0225\u0226\7D\2\2\u0226\u0227\7G\2\2\u0227"+
		"\u0228\7T\2\2\u0228\u0229\7P\2\2\u0229\u022a\7Q\2\2\u022a\u022b\7W\2\2"+
		"\u022b\u022c\7N\2\2\u022c\u022d\7N\2\2\u022d\u022e\7K\2\2\u022e,\3\2\2"+
		"\2\u022f\u0230\7D\2\2\u0230\u0231\7G\2\2\u0231\u0232\7V\2\2\u0232\u0233"+
		"\7Y\2\2\u0233\u0234\7G\2\2\u0234\u0235\7G\2\2\u0235\u0236\7P\2\2\u0236"+
		".\3\2\2\2\u0237\u0238\7D\2\2\u0238\u0239\7[\2\2\u0239\60\3\2\2\2\u023a"+
		"\u023b\7E\2\2\u023b\u023c\7C\2\2\u023c\u023d\7N\2\2\u023d\u023e\7N\2\2"+
		"\u023e\62\3\2\2\2\u023f\u0240\7E\2\2\u0240\u0241\7C\2\2\u0241\u0242\7"+
		"U\2\2\u0242\u0243\7E\2\2\u0243\u0244\7C\2\2\u0244\u0245\7F\2\2\u0245\u0246"+
		"\7G\2\2\u0246\64\3\2\2\2\u0247\u0248\7E\2\2\u0248\u0249\7C\2\2\u0249\u024a"+
		"\7U\2\2\u024a\u024b\7G\2\2\u024b\66\3\2\2\2\u024c\u024d\7E\2\2\u024d\u024e"+
		"\7C\2\2\u024e\u024f\7U\2\2\u024f\u0250\7V\2\2\u02508\3\2\2\2\u0251\u0252"+
		"\7E\2\2\u0252\u0253\7C\2\2\u0253\u0254\7V\2\2\u0254\u0255\7C\2\2\u0255"+
		"\u0256\7N\2\2\u0256\u0257\7Q\2\2\u0257\u0258\7I\2\2\u0258\u0259\7U\2\2"+
		"\u0259:\3\2\2\2\u025a\u025b\7E\2\2\u025b\u025c\7Q\2\2\u025c\u025d\7N\2"+
		"\2\u025d\u025e\7W\2\2\u025e\u025f\7O\2\2\u025f\u0260\7P\2\2\u0260<\3\2"+
		"\2\2\u0261\u0262\7E\2\2\u0262\u0263\7Q\2\2\u0263\u0264\7N\2\2\u0264\u0265"+
		"\7W\2\2\u0265\u0266\7O\2\2\u0266\u0267\7P\2\2\u0267\u0268\7U\2\2\u0268"+
		">\3\2\2\2\u0269\u026a\7E\2\2\u026a\u026b\7Q\2\2\u026b\u026c\7O\2\2\u026c"+
		"\u026d\7O\2\2\u026d\u026e\7G\2\2\u026e\u026f\7P\2\2\u026f\u0270\7V\2\2"+
		"\u0270@\3\2\2\2\u0271\u0272\7E\2\2\u0272\u0273\7Q\2\2\u0273\u0274\7O\2"+
		"\2\u0274\u0275\7O\2\2\u0275\u0276\7K\2\2\u0276\u0277\7V\2\2\u0277B\3\2"+
		"\2\2\u0278\u0279\7E\2\2\u0279\u027a\7Q\2\2\u027a\u027b\7O\2\2\u027b\u027c"+
		"\7O\2\2\u027c\u027d\7K\2\2\u027d\u027e\7V\2\2\u027e\u027f\7V\2\2\u027f"+
		"\u0280\7G\2\2\u0280\u0281\7F\2\2\u0281D\3\2\2\2\u0282\u0283\7E\2\2\u0283"+
		"\u0284\7Q\2\2\u0284\u0285\7P\2\2\u0285\u0286\7U\2\2\u0286\u0287\7V\2\2"+
		"\u0287\u0288\7T\2\2\u0288\u0289\7C\2\2\u0289\u028a\7K\2\2\u028a\u028b"+
		"\7P\2\2\u028b\u028c\7V\2\2\u028cF\3\2\2\2\u028d\u028e\7E\2\2\u028e\u028f"+
		"\7T\2\2\u028f\u0290\7G\2\2\u0290\u0291\7C\2\2\u0291\u0292\7V\2\2\u0292"+
		"\u0293\7G\2\2\u0293H\3\2\2\2\u0294\u0295\7E\2\2\u0295\u0296\7T\2\2\u0296"+
		"\u0297\7Q\2\2\u0297\u0298\7U\2\2\u0298\u0299\7U\2\2\u0299J\3\2\2\2\u029a"+
		"\u029b\7E\2\2\u029b\u029c\7W\2\2\u029c\u029d\7D\2\2\u029d\u029e\7G\2\2"+
		"\u029eL\3\2\2\2\u029f\u02a0\7E\2\2\u02a0\u02a1\7W\2\2\u02a1\u02a2\7T\2"+
		"\2\u02a2\u02a3\7T\2\2\u02a3\u02a4\7G\2\2\u02a4\u02a5\7P\2\2\u02a5\u02a6"+
		"\7V\2\2\u02a6N\3\2\2\2\u02a7\u02a8\7E\2\2\u02a8\u02a9\7W\2\2\u02a9\u02aa"+
		"\7T\2\2\u02aa\u02ab\7T\2\2\u02ab\u02ac\7G\2\2\u02ac\u02ad\7P\2\2\u02ad"+
		"\u02ae\7V\2\2\u02ae\u02af\7a\2\2\u02af\u02b0\7F\2\2\u02b0\u02b1\7C\2\2"+
		"\u02b1\u02b2\7V\2\2\u02b2\u02b3\7G\2\2\u02b3P\3\2\2\2\u02b4\u02b5\7E\2"+
		"\2\u02b5\u02b6\7W\2\2\u02b6\u02b7\7T\2\2\u02b7\u02b8\7T\2\2\u02b8\u02b9"+
		"\7G\2\2\u02b9\u02ba\7P\2\2\u02ba\u02bb\7V\2\2\u02bb\u02bc\7a\2\2\u02bc"+
		"\u02bd\7R\2\2\u02bd\u02be\7C\2\2\u02be\u02bf\7V\2\2\u02bf\u02c0\7J\2\2"+
		"\u02c0R\3\2\2\2\u02c1\u02c2\7E\2\2\u02c2\u02c3\7W\2\2\u02c3\u02c4\7T\2"+
		"\2\u02c4\u02c5\7T\2\2\u02c5\u02c6\7G\2\2\u02c6\u02c7\7P\2\2\u02c7\u02c8"+
		"\7V\2\2\u02c8\u02c9\7a\2\2\u02c9\u02ca\7T\2\2\u02ca\u02cb\7Q\2\2\u02cb"+
		"\u02cc\7N\2\2\u02cc\u02cd\7G\2\2\u02cdT\3\2\2\2\u02ce\u02cf\7E\2\2\u02cf"+
		"\u02d0\7W\2\2\u02d0\u02d1\7T\2\2\u02d1\u02d2\7T\2\2\u02d2\u02d3\7G\2\2"+
		"\u02d3\u02d4\7P\2\2\u02d4\u02d5\7V\2\2\u02d5\u02d6\7a\2\2\u02d6\u02d7"+
		"\7V\2\2\u02d7\u02d8\7K\2\2\u02d8\u02d9\7O\2\2\u02d9\u02da\7G\2\2\u02da"+
		"V\3\2\2\2\u02db\u02dc\7E\2\2\u02dc\u02dd\7W\2\2\u02dd\u02de\7T\2\2\u02de"+
		"\u02df\7T\2\2\u02df\u02e0\7G\2\2\u02e0\u02e1\7P\2\2\u02e1\u02e2\7V\2\2"+
		"\u02e2\u02e3\7a\2\2\u02e3\u02e4\7V\2\2\u02e4\u02e5\7K\2\2\u02e5\u02e6"+
		"\7O\2\2\u02e6\u02e7\7G\2\2\u02e7\u02e8\7U\2\2\u02e8\u02e9\7V\2\2\u02e9"+
		"\u02ea\7C\2\2\u02ea\u02eb\7O\2\2\u02eb\u02ec\7R\2\2\u02ecX\3\2\2\2\u02ed"+
		"\u02ee\7E\2\2\u02ee\u02ef\7W\2\2\u02ef\u02f0\7T\2\2\u02f0\u02f1\7T\2\2"+
		"\u02f1\u02f2\7G\2\2\u02f2\u02f3\7P\2\2\u02f3\u02f4\7V\2\2\u02f4\u02f5"+
		"\7a\2\2\u02f5\u02f6\7W\2\2\u02f6\u02f7\7U\2\2\u02f7\u02f8\7G\2\2\u02f8"+
		"\u02f9\7T\2\2\u02f9Z\3\2\2\2\u02fa\u02fb\7F\2\2\u02fb\u02fc\7C\2\2\u02fc"+
		"\u02fd\7V\2\2\u02fd\u02fe\7C\2\2\u02fe\\\3\2\2\2\u02ff\u0300\7F\2\2\u0300"+
		"\u0301\7C\2\2\u0301\u0302\7V\2\2\u0302\u0303\7G\2\2\u0303^\3\2\2\2\u0304"+
		"\u0305\7F\2\2\u0305\u0306\7C\2\2\u0306\u0307\7[\2\2\u0307`\3\2\2\2\u0308"+
		"\u0309\7F\2\2\u0309\u030a\7G\2\2\u030a\u030b\7C\2\2\u030b\u030c\7N\2\2"+
		"\u030c\u030d\7N\2\2\u030d\u030e\7Q\2\2\u030e\u030f\7E\2\2\u030f\u0310"+
		"\7C\2\2\u0310\u0311\7V\2\2\u0311\u0312\7G\2\2\u0312b\3\2\2\2\u0313\u0314"+
		"\7F\2\2\u0314\u0315\7G\2\2\u0315\u0316\7H\2\2\u0316\u0317\7K\2\2\u0317"+
		"\u0318\7P\2\2\u0318\u0319\7G\2\2\u0319\u031a\7T\2\2\u031ad\3\2\2\2\u031b"+
		"\u031c\7F\2\2\u031c\u031d\7G\2\2\u031d\u031e\7N\2\2\u031e\u031f\7G\2\2"+
		"\u031f\u0320\7V\2\2\u0320\u0321\7G\2\2\u0321f\3\2\2\2\u0322\u0323\7F\2"+
		"\2\u0323\u0324\7G\2\2\u0324\u0325\7U\2\2\u0325\u0326\7E\2\2\u0326h\3\2"+
		"\2\2\u0327\u0328\7F\2\2\u0328\u0329\7G\2\2\u0329\u032a\7U\2\2\u032a\u032b"+
		"\7E\2\2\u032b\u032c\7T\2\2\u032c\u032d\7K\2\2\u032d\u032e\7D\2\2\u032e"+
		"\u032f\7G\2\2\u032fj\3\2\2\2\u0330\u0331\7F\2\2\u0331\u0332\7K\2\2\u0332"+
		"\u0333\7U\2\2\u0333\u0334\7V\2\2\u0334\u0335\7K\2\2\u0335\u0336\7P\2\2"+
		"\u0336\u0337\7E\2\2\u0337\u0338\7V\2\2\u0338l\3\2\2\2\u0339\u033a\7F\2"+
		"\2\u033a\u033b\7K\2\2\u033b\u033c\7U\2\2\u033c\u033d\7V\2\2\u033d\u033e"+
		"\7T\2\2\u033e\u033f\7K\2\2\u033f\u0340\7D\2\2\u0340\u0341\7W\2\2\u0341"+
		"\u0342\7V\2\2\u0342\u0343\7G\2\2\u0343\u0344\7F\2\2\u0344n\3\2\2\2\u0345"+
		"\u0346\7F\2\2\u0346\u0347\7T\2\2\u0347\u0348\7Q\2\2\u0348\u0349\7R\2\2"+
		"\u0349p\3\2\2\2\u034a\u034b\7G\2\2\u034b\u034c\7N\2\2\u034c\u034d\7U\2"+
		"\2\u034d\u034e\7G\2\2\u034er\3\2\2\2\u034f\u0350\7G\2\2\u0350\u0351\7"+
		"P\2\2\u0351\u0352\7F\2\2\u0352t\3\2\2\2\u0353\u0354\7G\2\2\u0354\u0355"+
		"\7U\2\2\u0355\u0356\7E\2\2\u0356\u0357\7C\2\2\u0357\u0358\7R\2\2\u0358"+
		"\u0359\7G\2\2\u0359v\3\2\2\2\u035a\u035b\7G\2\2\u035b\u035c\7Z\2\2\u035c"+
		"\u035d\7E\2\2\u035d\u035e\7G\2\2\u035e\u035f\7R\2\2\u035f\u0360\7V\2\2"+
		"\u0360x\3\2\2\2\u0361\u0362\7G\2\2\u0362\u0363\7Z\2\2\u0363\u0364\7E\2"+
		"\2\u0364\u0365\7N\2\2\u0365\u0366\7W\2\2\u0366\u0367\7F\2\2\u0367\u0368"+
		"\7K\2\2\u0368\u0369\7P\2\2\u0369\u036a\7I\2\2\u036az\3\2\2\2\u036b\u036c"+
		"\7G\2\2\u036c\u036d\7Z\2\2\u036d\u036e\7G\2\2\u036e\u036f\7E\2\2\u036f"+
		"\u0370\7W\2\2\u0370\u0371\7V\2\2\u0371\u0372\7G\2\2\u0372|\3\2\2\2\u0373"+
		"\u0374\7G\2\2\u0374\u0375\7Z\2\2\u0375\u0376\7K\2\2\u0376\u0377\7U\2\2"+
		"\u0377\u0378\7V\2\2\u0378\u0379\7U\2\2\u0379~\3\2\2\2\u037a\u037b\7G\2"+
		"\2\u037b\u037c\7Z\2\2\u037c\u037d\7R\2\2\u037d\u037e\7N\2\2\u037e\u037f"+
		"\7C\2\2\u037f\u0380\7K\2\2\u0380\u0381\7P\2\2\u0381\u0080\3\2\2\2\u0382"+
		"\u0383\7G\2\2\u0383\u0384\7Z\2\2\u0384\u0385\7V\2\2\u0385\u0386\7T\2\2"+
		"\u0386\u0387\7C\2\2\u0387\u0388\7E\2\2\u0388\u0389\7V\2\2\u0389\u0082"+
		"\3\2\2\2\u038a\u038b\7H\2\2\u038b\u038c\7C\2\2\u038c\u038d\7N\2\2\u038d"+
		"\u038e\7U\2\2\u038e\u038f\7G\2\2\u038f\u0084\3\2\2\2\u0390\u0391\7H\2"+
		"\2\u0391\u0392\7G\2\2\u0392\u0393\7V\2\2\u0393\u0394\7E\2\2\u0394\u0395"+
		"\7J\2\2\u0395\u0086\3\2\2\2\u0396\u0397\7H\2\2\u0397\u0398\7K\2\2\u0398"+
		"\u0399\7N\2\2\u0399\u039a\7V\2\2\u039a\u039b\7G\2\2\u039b\u039c\7T\2\2"+
		"\u039c\u0088\3\2\2\2\u039d\u039e\7H\2\2\u039e\u039f\7K\2\2\u039f\u03a0"+
		"\7T\2\2\u03a0\u03a1\7U\2\2\u03a1\u03a2\7V\2\2\u03a2\u008a\3\2\2\2\u03a3"+
		"\u03a4\7H\2\2\u03a4\u03a5\7Q\2\2\u03a5\u03a6\7N\2\2\u03a6\u03a7\7N\2\2"+
		"\u03a7\u03a8\7Q\2\2\u03a8\u03a9\7Y\2\2\u03a9\u03aa\7K\2\2\u03aa\u03ab"+
		"\7P\2\2\u03ab\u03ac\7I\2\2\u03ac\u008c\3\2\2\2\u03ad\u03ae\7H\2\2\u03ae"+
		"\u03af\7Q\2\2\u03af\u03b0\7T\2\2\u03b0\u008e\3\2\2\2\u03b1\u03b2\7H\2"+
		"\2\u03b2\u03b3\7Q\2\2\u03b3\u03b4\7T\2\2\u03b4\u03b5\7O\2\2\u03b5\u03b6"+
		"\7C\2\2\u03b6\u03b7\7V\2\2\u03b7\u0090\3\2\2\2\u03b8\u03b9\7H\2\2\u03b9"+
		"\u03ba\7T\2\2\u03ba\u03bb\7Q\2\2\u03bb\u03bc\7O\2\2\u03bc\u0092\3\2\2"+
		"\2\u03bd\u03be\7H\2\2\u03be\u03bf\7W\2\2\u03bf\u03c0\7N\2\2\u03c0\u03c1"+
		"\7N\2\2\u03c1\u0094\3\2\2\2\u03c2\u03c3\7H\2\2\u03c3\u03c4\7W\2\2\u03c4"+
		"\u03c5\7P\2\2\u03c5\u03c6\7E\2\2\u03c6\u03c7\7V\2\2\u03c7\u03c8\7K\2\2"+
		"\u03c8\u03c9\7Q\2\2\u03c9\u03ca\7P\2\2\u03ca\u03cb\7U\2\2\u03cb\u0096"+
		"\3\2\2\2\u03cc\u03cd\7I\2\2\u03cd\u03ce\7T\2\2\u03ce\u03cf\7C\2\2\u03cf"+
		"\u03d0\7P\2\2\u03d0\u03d1\7V\2\2\u03d1\u0098\3\2\2\2\u03d2\u03d3\7I\2"+
		"\2\u03d3\u03d4\7T\2\2\u03d4\u03d5\7C\2\2\u03d5\u03d6\7P\2\2\u03d6\u03d7"+
		"\7V\2\2\u03d7\u03d8\7G\2\2\u03d8\u03d9\7F\2\2\u03d9\u009a\3\2\2\2\u03da"+
		"\u03db\7I\2\2\u03db\u03dc\7T\2\2\u03dc\u03dd\7C\2\2\u03dd\u03de\7P\2\2"+
		"\u03de\u03df\7V\2\2\u03df\u03e0\7U\2\2\u03e0\u009c\3\2\2\2\u03e1\u03e2"+
		"\7I\2\2\u03e2\u03e3\7T\2\2\u03e3\u03e4\7C\2\2\u03e4\u03e5\7R\2\2\u03e5"+
		"\u03e6\7J\2\2\u03e6\u03e7\7X\2\2\u03e7\u03e8\7K\2\2\u03e8\u03e9\7\\\2"+
		"\2\u03e9\u009e\3\2\2\2\u03ea\u03eb\7I\2\2\u03eb\u03ec\7T\2\2\u03ec\u03ed"+
		"\7Q\2\2\u03ed\u03ee\7W\2\2\u03ee\u03ef\7R\2\2\u03ef\u00a0\3\2\2\2\u03f0"+
		"\u03f1\7I\2\2\u03f1\u03f2\7T\2\2\u03f2\u03f3\7Q\2\2\u03f3\u03f4\7W\2\2"+
		"\u03f4\u03f5\7R\2\2\u03f5\u03f6\7K\2\2\u03f6\u03f7\7P\2\2\u03f7\u03f8"+
		"\7I\2\2\u03f8\u00a2\3\2\2\2\u03f9\u03fa\7J\2\2\u03fa\u03fb\7C\2\2\u03fb"+
		"\u03fc\7X\2\2\u03fc\u03fd\7K\2\2\u03fd\u03fe\7P\2\2\u03fe\u03ff\7I\2\2"+
		"\u03ff\u00a4\3\2\2\2\u0400\u0401\7J\2\2\u0401\u0402\7Q\2\2\u0402\u0403"+
		"\7W\2\2\u0403\u0404\7T\2\2\u0404\u00a6\3\2\2\2\u0405\u0406\7K\2\2\u0406"+
		"\u0407\7H\2\2\u0407\u00a8\3\2\2\2\u0408\u0409\7K\2\2\u0409\u040a\7I\2"+
		"\2\u040a\u040b\7P\2\2\u040b\u040c\7Q\2\2\u040c\u040d\7T\2\2\u040d\u040e"+
		"\7G\2\2\u040e\u00aa\3\2\2\2\u040f\u0410\7K\2\2\u0410\u0411\7P\2\2\u0411"+
		"\u00ac\3\2\2\2\u0412\u0413\7K\2\2\u0413\u0414\7P\2\2\u0414\u0415\7E\2"+
		"\2\u0415\u0416\7N\2\2\u0416\u0417\7W\2\2\u0417\u0418\7F\2\2\u0418\u0419"+
		"\7K\2\2\u0419\u041a\7P\2\2\u041a\u041b\7I\2\2\u041b\u00ae\3\2\2\2\u041c"+
		"\u041d\7K\2\2\u041d\u041e\7P\2\2\u041e\u041f\7P\2\2\u041f\u0420\7G\2\2"+
		"\u0420\u0421\7T\2\2\u0421\u00b0\3\2\2\2\u0422\u0423\7K\2\2\u0423\u0424"+
		"\7P\2\2\u0424\u0425\7R\2\2\u0425\u0426\7W\2\2\u0426\u0427\7V\2\2\u0427"+
		"\u00b2\3\2\2\2\u0428\u0429\7K\2\2\u0429\u042a\7P\2\2\u042a\u042b\7U\2"+
		"\2\u042b\u042c\7G\2\2\u042c\u042d\7T\2\2\u042d\u042e\7V\2\2\u042e\u00b4"+
		"\3\2\2\2\u042f\u0430\7K\2\2\u0430\u0431\7P\2\2\u0431\u0432\7V\2\2\u0432"+
		"\u0433\7G\2\2\u0433\u0434\7T\2\2\u0434\u0435\7U\2\2\u0435\u0436\7G\2\2"+
		"\u0436\u0437\7E\2\2\u0437\u0438\7V\2\2\u0438\u00b6\3\2\2\2\u0439\u043a"+
		"\7K\2\2\u043a\u043b\7P\2\2\u043b\u043c\7V\2\2\u043c\u043d\7G\2\2\u043d"+
		"\u043e\7T\2\2\u043e\u043f\7X\2\2\u043f\u0440\7C\2\2\u0440\u0441\7N\2\2"+
		"\u0441\u00b8\3\2\2\2\u0442\u0443\7K\2\2\u0443\u0444\7P\2\2\u0444\u0445"+
		"\7V\2\2\u0445\u0446\7Q\2\2\u0446\u00ba\3\2\2\2\u0447\u0448\7K\2\2\u0448"+
		"\u0449\7P\2\2\u0449\u044a\7X\2\2\u044a\u044b\7Q\2\2\u044b\u044c\7M\2\2"+
		"\u044c\u044d\7G\2\2\u044d\u044e\7T\2\2\u044e\u00bc\3\2\2\2\u044f\u0450"+
		"\7K\2\2\u0450\u0451\7Q\2\2\u0451\u00be\3\2\2\2\u0452\u0453\7K\2\2\u0453"+
		"\u0454\7U\2\2\u0454\u00c0\3\2\2\2\u0455\u0456\7K\2\2\u0456\u0457\7U\2"+
		"\2\u0457\u0458\7Q\2\2\u0458\u0459\7N\2\2\u0459\u045a\7C\2\2\u045a\u045b"+
		"\7V\2\2\u045b\u045c\7K\2\2\u045c\u045d\7Q\2\2\u045d\u045e\7P\2\2\u045e"+
		"\u00c2\3\2\2\2\u045f\u0460\7L\2\2\u0460\u0461\7U\2\2\u0461\u0462\7Q\2"+
		"\2\u0462\u0463\7P\2\2\u0463\u00c4\3\2\2\2\u0464\u0465\7L\2\2\u0465\u0466"+
		"\7Q\2\2\u0466\u0467\7K\2\2\u0467\u0468\7P\2\2\u0468\u00c6\3\2\2\2\u0469"+
		"\u046a\7N\2\2\u046a\u046b\7C\2\2\u046b\u046c\7U\2\2\u046c\u046d\7V\2\2"+
		"\u046d\u00c8\3\2\2\2\u046e\u046f\7N\2\2\u046f\u0470\7C\2\2\u0470\u0471"+
		"\7V\2\2\u0471\u0472\7G\2\2\u0472\u0473\7T\2\2\u0473\u0474\7C\2\2\u0474"+
		"\u0475\7N\2\2\u0475\u00ca\3\2\2\2\u0476\u0477\7N\2\2\u0477\u0478\7G\2"+
		"\2\u0478\u0479\7H\2\2\u0479\u047a\7V\2\2\u047a\u00cc\3\2\2\2\u047b\u047c"+
		"\7N\2\2\u047c\u047d\7G\2\2\u047d\u047e\7X\2\2\u047e\u047f\7G\2\2\u047f"+
		"\u0480\7N\2\2\u0480\u00ce\3\2\2\2\u0481\u0482\7N\2\2\u0482\u0483\7K\2"+
		"\2\u0483\u0484\7M\2\2\u0484\u0485\7G\2\2\u0485\u00d0\3\2\2\2\u0486\u0487"+
		"\7N\2\2\u0487\u0488\7K\2\2\u0488\u0489\7O\2\2\u0489\u048a\7K\2\2\u048a"+
		"\u048b\7V\2\2\u048b\u00d2\3\2\2\2\u048c\u048d\7N\2\2\u048d\u048e\7Q\2"+
		"\2\u048e\u048f\7E\2\2\u048f\u0490\7C\2\2\u0490\u0491\7N\2\2\u0491\u0492"+
		"\7V\2\2\u0492\u0493\7K\2\2\u0493\u0494\7O\2\2\u0494\u0495\7G\2\2\u0495"+
		"\u00d4\3\2\2\2\u0496\u0497\7N\2\2\u0497\u0498\7Q\2\2\u0498\u0499\7E\2"+
		"\2\u0499\u049a\7C\2\2\u049a\u049b\7N\2\2\u049b\u049c\7V\2\2\u049c\u049d"+
		"\7K\2\2\u049d\u049e\7O\2\2\u049e\u049f\7G\2\2\u049f\u04a0\7U\2\2\u04a0"+
		"\u04a1\7V\2\2\u04a1\u04a2\7C\2\2\u04a2\u04a3\7O\2\2\u04a3\u04a4\7R\2\2"+
		"\u04a4\u00d6\3\2\2\2\u04a5\u04a6\7N\2\2\u04a6\u04a7\7Q\2\2\u04a7\u04a8"+
		"\7I\2\2\u04a8\u04a9\7K\2\2\u04a9\u04aa\7E\2\2\u04aa\u04ab\7C\2\2\u04ab"+
		"\u04ac\7N\2\2\u04ac\u00d8\3\2\2\2\u04ad\u04ae\7O\2\2\u04ae\u04af\7C\2"+
		"\2\u04af\u04b0\7R\2\2\u04b0\u00da\3\2\2\2\u04b1\u04b2\7O\2\2\u04b2\u04b3"+
		"\7K\2\2\u04b3\u04b4\7P\2\2\u04b4\u04b5\7W\2\2\u04b5\u04b6\7V\2\2\u04b6"+
		"\u04b7\7G\2\2\u04b7\u00dc\3\2\2\2\u04b8\u04b9\7O\2\2\u04b9\u04ba\7Q\2"+
		"\2\u04ba\u04bb\7P\2\2\u04bb\u04bc\7V\2\2\u04bc\u04bd\7J\2\2\u04bd\u00de"+
		"\3\2\2\2\u04be\u04bf\7P\2\2\u04bf\u04c0\7C\2\2\u04c0\u04c1\7V\2\2\u04c1"+
		"\u04c2\7W\2\2\u04c2\u04c3\7T\2\2\u04c3\u04c4\7C\2\2\u04c4\u04c5\7N\2\2"+
		"\u04c5\u00e0\3\2\2\2\u04c6\u04c7\7P\2\2\u04c7\u04c8\7G\2\2\u04c8\u04c9"+
		"\7Z\2\2\u04c9\u04ca\7V\2\2\u04ca\u00e2\3\2\2\2\u04cb\u04cc\7P\2\2\u04cc"+
		"\u04cd\7H\2\2\u04cd\u04ce\7E\2\2\u04ce\u00e4\3\2\2\2\u04cf\u04d0\7P\2"+
		"\2\u04d0\u04d1\7H\2\2\u04d1\u04d2\7F\2\2\u04d2\u00e6\3\2\2\2\u04d3\u04d4"+
		"\7P\2\2\u04d4\u04d5\7H\2\2\u04d5\u04d6\7M\2\2\u04d6\u04d7\7E\2\2\u04d7"+
		"\u00e8\3\2\2\2\u04d8\u04d9\7P\2\2\u04d9\u04da\7H\2\2\u04da\u04db\7M\2"+
		"\2\u04db\u04dc\7F\2\2\u04dc\u00ea\3\2\2\2\u04dd\u04de\7P\2\2\u04de\u04df"+
		"\7Q\2\2\u04df\u00ec\3\2\2\2\u04e0\u04e1\7P\2\2\u04e1\u04e2\7Q\2\2\u04e2"+
		"\u04e3\7P\2\2\u04e3\u04e4\7G\2\2\u04e4\u00ee\3\2\2\2\u04e5\u04e6\7P\2"+
		"\2\u04e6\u04e7\7Q\2\2\u04e7\u04e8\7T\2\2\u04e8\u04e9\7O\2\2\u04e9\u04ea"+
		"\7C\2\2\u04ea\u04eb\7N\2\2\u04eb\u04ec\7K\2\2\u04ec\u04ed\7\\\2\2\u04ed"+
		"\u04ee\7G\2\2\u04ee\u00f0\3\2\2\2\u04ef\u04f0\7P\2\2\u04f0\u04f1\7Q\2"+
		"\2\u04f1\u04f2\7V\2\2\u04f2\u00f2\3\2\2\2\u04f3\u04f4\7P\2\2\u04f4\u04f5"+
		"\7W\2\2\u04f5\u04f6\7N\2\2\u04f6\u04f7\7N\2\2\u04f7\u00f4\3\2\2\2\u04f8"+
		"\u04f9\7P\2\2\u04f9\u04fa\7W\2\2\u04fa\u04fb\7N\2\2\u04fb\u04fc\7N\2\2"+
		"\u04fc\u04fd\7K\2\2\u04fd\u04fe\7H\2\2\u04fe\u00f6\3\2\2\2\u04ff\u0500"+
		"\7P\2\2\u0500\u0501\7W\2\2\u0501\u0502\7N\2\2\u0502\u0503\7N\2\2\u0503"+
		"\u0504\7U\2\2\u0504\u00f8\3\2\2\2\u0505\u0506\7Q\2\2\u0506\u0507\7H\2"+
		"\2\u0507\u0508\7H\2\2\u0508\u0509\7U\2\2\u0509\u050a\7G\2\2\u050a\u050b"+
		"\7V\2\2\u050b\u00fa\3\2\2\2\u050c\u050d\7Q\2\2\u050d\u050e\7P\2\2\u050e"+
		"\u00fc\3\2\2\2\u050f\u0510\7Q\2\2\u0510\u0511\7P\2\2\u0511\u0512\7N\2"+
		"\2\u0512\u0513\7[\2\2\u0513\u00fe\3\2\2\2\u0514\u0515\7Q\2\2\u0515\u0516"+
		"\7R\2\2\u0516\u0517\7V\2\2\u0517\u0518\7K\2\2\u0518\u0519\7Q\2\2\u0519"+
		"\u051a\7P\2\2\u051a\u0100\3\2\2\2\u051b\u051c\7Q\2\2\u051c\u051d\7T\2"+
		"\2\u051d\u0102\3\2\2\2\u051e\u051f\7Q\2\2\u051f\u0520\7T\2\2\u0520\u0521"+
		"\7F\2\2\u0521\u0522\7G\2\2\u0522\u0523\7T\2\2\u0523\u0104\3\2\2\2\u0524"+
		"\u0525\7Q\2\2\u0525\u0526\7T\2\2\u0526\u0527\7F\2\2\u0527\u0528\7K\2\2"+
		"\u0528\u0529\7P\2\2\u0529\u052a\7C\2\2\u052a\u052b\7N\2\2\u052b\u052c"+
		"\7K\2\2\u052c\u052d\7V\2\2\u052d\u052e\7[\2\2\u052e\u0106\3\2\2\2\u052f"+
		"\u0530\7Q\2\2\u0530\u0531\7W\2\2\u0531\u0532\7V\2\2\u0532\u0533\7G\2\2"+
		"\u0533\u0534\7T\2\2\u0534\u0108\3\2\2\2\u0535\u0536\7Q\2\2\u0536\u0537"+
		"\7W\2\2\u0537\u0538\7V\2\2\u0538\u0539\7R\2\2\u0539\u053a\7W\2\2\u053a"+
		"\u053b\7V\2\2\u053b\u010a\3\2\2\2\u053c\u053d\7Q\2\2\u053d\u053e\7X\2"+
		"\2\u053e\u053f\7G\2\2\u053f\u0540\7T\2\2\u0540\u010c\3\2\2\2\u0541\u0542"+
		"\7R\2\2\u0542\u0543\7C\2\2\u0543\u0544\7T\2\2\u0544\u0545\7V\2\2\u0545"+
		"\u0546\7K\2\2\u0546\u0547\7V\2\2\u0547\u0548\7K\2\2\u0548\u0549\7Q\2\2"+
		"\u0549\u054a\7P\2\2\u054a\u010e\3\2\2\2\u054b\u054c\7R\2\2\u054c\u054d"+
		"\7C\2\2\u054d\u054e\7T\2\2\u054e\u054f\7V\2\2\u054f\u0550\7K\2\2\u0550"+
		"\u0551\7V\2\2\u0551\u0552\7K\2\2\u0552\u0553\7Q\2\2\u0553\u0554\7P\2\2"+
		"\u0554\u0555\7U\2\2\u0555\u0110\3\2\2\2\u0556\u0557\7R\2\2\u0557\u0558"+
		"\7C\2\2\u0558\u0559\7V\2\2\u0559\u055a\7J\2\2\u055a\u0112\3\2\2\2\u055b"+
		"\u055c\7R\2\2\u055c\u055d\7Q\2\2\u055d\u055e\7U\2\2\u055e\u055f\7K\2\2"+
		"\u055f\u0560\7V\2\2\u0560\u0561\7K\2\2\u0561\u0562\7Q\2\2\u0562\u0563"+
		"\7P\2\2\u0563\u0114\3\2\2\2\u0564\u0565\7R\2\2\u0565\u0566\7T\2\2\u0566"+
		"\u0567\7G\2\2\u0567\u0568\7E\2\2\u0568\u0569\7G\2\2\u0569\u056a\7F\2\2"+
		"\u056a\u056b\7K\2\2\u056b\u056c\7P\2\2\u056c\u056d\7I\2\2\u056d\u0116"+
		"\3\2\2\2\u056e\u056f\7R\2\2\u056f\u0570\7T\2\2\u0570\u0571\7G\2\2\u0571"+
		"\u0572\7R\2\2\u0572\u0573\7C\2\2\u0573\u0574\7T\2\2\u0574\u0575\7G\2\2"+
		"\u0575\u0118\3\2\2\2\u0576\u0577\7R\2\2\u0577\u0578\7T\2\2\u0578\u0579"+
		"\7K\2\2\u0579\u057a\7X\2\2\u057a\u057b\7K\2\2\u057b\u057c\7N\2\2\u057c"+
		"\u057d\7G\2\2\u057d\u057e\7I\2\2\u057e\u057f\7G\2\2\u057f\u0580\7U\2\2"+
		"\u0580\u011a\3\2\2\2\u0581\u0582\7R\2\2\u0582\u0583\7T\2\2\u0583\u0584"+
		"\7Q\2\2\u0584\u0585\7R\2\2\u0585\u0586\7G\2\2\u0586\u0587\7T\2\2\u0587"+
		"\u0588\7V\2\2\u0588\u0589\7K\2\2\u0589\u058a\7G\2\2\u058a\u058b\7U\2\2"+
		"\u058b\u011c\3\2\2\2\u058c\u058d\7T\2\2\u058d\u058e\7C\2\2\u058e\u058f"+
		"\7P\2\2\u058f\u0590\7I\2\2\u0590\u0591\7G\2\2\u0591\u011e\3\2\2\2\u0592"+
		"\u0593\7T\2\2\u0593\u0594\7G\2\2\u0594\u0595\7C\2\2\u0595\u0596\7F\2\2"+
		"\u0596\u0120\3\2\2\2\u0597\u0598\7T\2\2\u0598\u0599\7G\2\2\u0599\u059a"+
		"\7E\2\2\u059a\u059b\7W\2\2\u059b\u059c\7T\2\2\u059c\u059d\7U\2\2\u059d"+
		"\u059e\7K\2\2\u059e\u059f\7X\2\2\u059f\u05a0\7G\2\2\u05a0\u0122\3\2\2"+
		"\2\u05a1\u05a2\7T\2\2\u05a2\u05a3\7G\2\2\u05a3\u05a4\7P\2\2\u05a4\u05a5"+
		"\7C\2\2\u05a5\u05a6\7O\2\2\u05a6\u05a7\7G\2\2\u05a7\u0124\3\2\2\2\u05a8"+
		"\u05a9\7T\2\2\u05a9\u05aa\7G\2\2\u05aa\u05ab\7R\2\2\u05ab\u05ac\7G\2\2"+
		"\u05ac\u05ad\7C\2\2\u05ad\u05ae\7V\2\2\u05ae\u05af\7C\2\2\u05af\u05b0"+
		"\7D\2\2\u05b0\u05b1\7N\2\2\u05b1\u05b2\7G\2\2\u05b2\u0126\3\2\2\2\u05b3"+
		"\u05b4\7T\2\2\u05b4\u05b5\7G\2\2\u05b5\u05b6\7R\2\2\u05b6\u05b7\7N\2\2"+
		"\u05b7\u05b8\7C\2\2\u05b8\u05b9\7E\2\2\u05b9\u05ba\7G\2\2\u05ba\u0128"+
		"\3\2\2\2\u05bb\u05bc\7T\2\2\u05bc\u05bd\7G\2\2\u05bd\u05be\7U\2\2\u05be"+
		"\u05bf\7G\2\2\u05bf\u05c0\7V\2\2\u05c0\u012a\3\2\2\2\u05c1\u05c2\7T\2"+
		"\2\u05c2\u05c3\7G\2\2\u05c3\u05c4\7U\2\2\u05c4\u05c5\7R\2\2\u05c5\u05c6"+
		"\7G\2\2\u05c6\u05c7\7E\2\2\u05c7\u05c8\7V\2\2\u05c8\u012c\3\2\2\2\u05c9"+
		"\u05ca\7T\2\2\u05ca\u05cb\7G\2\2\u05cb\u05cc\7U\2\2\u05cc\u05cd\7V\2\2"+
		"\u05cd\u05ce\7T\2\2\u05ce\u05cf\7K\2\2\u05cf\u05d0\7E\2\2\u05d0\u05d1"+
		"\7V\2\2\u05d1\u012e\3\2\2\2\u05d2\u05d3\7T\2\2\u05d3\u05d4\7G\2\2\u05d4"+
		"\u05d5\7X\2\2\u05d5\u05d6\7Q\2\2\u05d6\u05d7\7M\2\2\u05d7\u05d8\7G\2\2"+
		"\u05d8\u0130\3\2\2\2\u05d9\u05da\7T\2\2\u05da\u05db\7K\2\2\u05db\u05dc"+
		"\7I\2\2\u05dc\u05dd\7J\2\2\u05dd\u05de\7V\2\2\u05de\u0132\3\2\2\2\u05df"+
		"\u05e0\7T\2\2\u05e0\u05e1\7Q\2\2\u05e1\u05e2\7N\2\2\u05e2\u05e3\7G\2\2"+
		"\u05e3\u0134\3\2\2\2\u05e4\u05e5\7T\2\2\u05e5\u05e6\7Q\2\2\u05e6\u05e7"+
		"\7N\2\2\u05e7\u05e8\7G\2\2\u05e8\u05e9\7U\2\2\u05e9\u0136\3\2\2\2\u05ea"+
		"\u05eb\7T\2\2\u05eb\u05ec\7Q\2\2\u05ec\u05ed\7N\2\2\u05ed\u05ee\7N\2\2"+
		"\u05ee\u05ef\7D\2\2\u05ef\u05f0\7C\2\2\u05f0\u05f1\7E\2\2\u05f1\u05f2"+
		"\7M\2\2\u05f2\u0138\3\2\2\2\u05f3\u05f4\7T\2\2\u05f4\u05f5\7Q\2\2\u05f5"+
		"\u05f6\7N\2\2\u05f6\u05f7\7N\2\2\u05f7\u05f8\7W\2\2\u05f8\u05f9\7R\2\2"+
		"\u05f9\u013a\3\2\2\2\u05fa\u05fb\7T\2\2\u05fb\u05fc\7Q\2\2\u05fc\u05fd"+
		"\7Y\2\2\u05fd\u013c\3\2\2\2\u05fe\u05ff\7T\2\2\u05ff\u0600\7Q\2\2\u0600"+
		"\u0601\7Y\2\2\u0601\u0602\7U\2\2\u0602\u013e\3\2\2\2\u0603\u0604\7U\2"+
		"\2\u0604\u0605\7E\2\2\u0605\u0606\7J\2\2\u0606\u0607\7G\2\2\u0607\u0608"+
		"\7O\2\2\u0608\u0609\7C\2\2\u0609\u0140\3\2\2\2\u060a\u060b\7U\2\2\u060b"+
		"\u060c\7E\2\2\u060c\u060d\7J\2\2\u060d\u060e\7G\2\2\u060e\u060f\7O\2\2"+
		"\u060f\u0610\7C\2\2\u0610\u0611\7U\2\2\u0611\u0142\3\2\2\2\u0612\u0613"+
		"\7U\2\2\u0613\u0614\7G\2\2\u0614\u0615\7E\2\2\u0615\u0616\7Q\2\2\u0616"+
		"\u0617\7P\2\2\u0617\u0618\7F\2\2\u0618\u0144\3\2\2\2\u0619\u061a\7U\2"+
		"\2\u061a\u061b\7G\2\2\u061b\u061c\7E\2\2\u061c\u061d\7W\2\2\u061d\u061e"+
		"\7T\2\2\u061e\u061f\7K\2\2\u061f\u0620\7V\2\2\u0620\u0621\7[\2\2\u0621"+
		"\u0146\3\2\2\2\u0622\u0623\7U\2\2\u0623\u0624\7G\2\2\u0624\u0625\7N\2"+
		"\2\u0625\u0626\7G\2\2\u0626\u0627\7E\2\2\u0627\u0628\7V\2\2\u0628\u0148"+
		"\3\2\2\2\u0629\u062a\7U\2\2\u062a\u062b\7G\2\2\u062b\u062c\7T\2\2\u062c"+
		"\u062d\7K\2\2\u062d\u062e\7C\2\2\u062e\u062f\7N\2\2\u062f\u0630\7K\2\2"+
		"\u0630\u0631\7\\\2\2\u0631\u0632\7C\2\2\u0632\u0633\7D\2\2\u0633\u0634"+
		"\7N\2\2\u0634\u0635\7G\2\2\u0635\u014a\3\2\2\2\u0636\u0637\7U\2\2\u0637"+
		"\u0638\7G\2\2\u0638\u0639\7U\2\2\u0639\u063a\7U\2\2\u063a\u063b\7K\2\2"+
		"\u063b\u063c\7Q\2\2\u063c\u063d\7P\2\2\u063d\u014c\3\2\2\2\u063e\u063f"+
		"\7U\2\2\u063f\u0640\7G\2\2\u0640\u0641\7V\2\2\u0641\u014e\3\2\2\2\u0642"+
		"\u0643\7U\2\2\u0643\u0644\7G\2\2\u0644\u0645\7V\2\2\u0645\u0646\7U\2\2"+
		"\u0646\u0150\3\2\2\2\u0647\u0648\7U\2\2\u0648\u0649\7J\2\2\u0649\u064a"+
		"\7Q\2\2\u064a\u064b\7Y\2\2\u064b\u0152\3\2\2\2\u064c\u064d\7U\2\2\u064d"+
		"\u064e\7Q\2\2\u064e\u064f\7O\2\2\u064f\u0650\7G\2\2\u0650\u0154\3\2\2"+
		"\2\u0651\u0652\7U\2\2\u0652\u0653\7V\2\2\u0653\u0654\7C\2\2\u0654\u0655"+
		"\7T\2\2\u0655\u0656\7V\2\2\u0656\u0156\3\2\2\2\u0657\u0658\7U\2\2\u0658"+
		"\u0659\7V\2\2\u0659\u065a\7C\2\2\u065a\u065b\7V\2\2\u065b\u065c\7U\2\2"+
		"\u065c\u0158\3\2\2\2\u065d\u065e\7U\2\2\u065e\u065f\7W\2\2\u065f\u0660"+
		"\7D\2\2\u0660\u0661\7U\2\2\u0661\u0662\7V\2\2\u0662\u0663\7T\2\2\u0663"+
		"\u0664\7K\2\2\u0664\u0665\7P\2\2\u0665\u0666\7I\2\2\u0666\u015a\3\2\2"+
		"\2\u0667\u0668\7U\2\2\u0668\u0669\7[\2\2\u0669\u066a\7U\2\2\u066a\u066b"+
		"\7V\2\2\u066b\u066c\7G\2\2\u066c\u066d\7O\2\2\u066d\u015c\3\2\2\2\u066e"+
		"\u066f\7V\2\2\u066f\u0670\7C\2\2\u0670\u0671\7D\2\2\u0671\u0672\7N\2\2"+
		"\u0672\u0673\7G\2\2\u0673\u015e\3\2\2\2\u0674\u0675\7V\2\2\u0675\u0676"+
		"\7C\2\2\u0676\u0677\7D\2\2\u0677\u0678\7N\2\2\u0678\u0679\7G\2\2\u0679"+
		"\u067a\7U\2\2\u067a\u0160\3\2\2\2\u067b\u067c\7V\2\2\u067c\u067d\7C\2"+
		"\2\u067d\u067e\7D\2\2\u067e\u067f\7N\2\2\u067f\u0680\7G\2\2\u0680\u0681"+
		"\7U\2\2\u0681\u0682\7C\2\2\u0682\u0683\7O\2\2\u0683\u0684\7R\2\2\u0684"+
		"\u0685\7N\2\2\u0685\u0686\7G\2\2\u0686\u0162\3\2\2\2\u0687\u0688\7V\2"+
		"\2\u0688\u0689\7G\2\2\u0689\u068a\7Z\2\2\u068a\u068b\7V\2\2\u068b\u0164"+
		"\3\2\2\2\u068c\u068d\7V\2\2\u068d\u068e\7J\2\2\u068e\u068f\7G\2\2\u068f"+
		"\u0690\7P\2\2\u0690\u0166\3\2\2\2\u0691\u0692\7V\2\2\u0692\u0693\7K\2"+
		"\2\u0693\u0694\7G\2\2\u0694\u0695\7U\2\2\u0695\u0168\3\2\2\2\u0696\u0697"+
		"\7V\2\2\u0697\u0698\7K\2\2\u0698\u0699\7O\2\2\u0699\u069a\7G\2\2\u069a"+
		"\u016a\3\2\2\2\u069b\u069c\7V\2\2\u069c\u069d\7K\2\2\u069d\u069e\7O\2"+
		"\2\u069e\u069f\7G\2\2\u069f\u06a0\7U\2\2\u06a0\u06a1\7V\2\2\u06a1\u06a2"+
		"\7C\2\2\u06a2\u06a3\7O\2\2\u06a3\u06a4\7R\2\2\u06a4\u016c\3\2\2\2\u06a5"+
		"\u06a6\7V\2\2\u06a6\u06a7\7Q\2\2\u06a7\u016e\3\2\2\2\u06a8\u06a9\7V\2"+
		"\2\u06a9\u06aa\7T\2\2\u06aa\u06ab\7C\2\2\u06ab\u06ac\7P\2\2\u06ac\u06ad"+
		"\7U\2\2\u06ad\u06ae\7C\2\2\u06ae\u06af\7E\2\2\u06af\u06b0\7V\2\2\u06b0"+
		"\u06b1\7K\2\2\u06b1\u06b2\7Q\2\2\u06b2\u06b3\7P\2\2\u06b3\u0170\3\2\2"+
		"\2\u06b4\u06b5\7V\2\2\u06b5\u06b6\7T\2\2\u06b6\u06b7\7W\2\2\u06b7\u06b8"+
		"\7G\2\2\u06b8\u0172\3\2\2\2\u06b9\u06ba\7V\2\2\u06ba\u06bb\7T\2\2\u06bb"+
		"\u06bc\7[\2\2\u06bc\u06bd\7a\2\2\u06bd\u06be\7E\2\2\u06be\u06bf\7C\2\2"+
		"\u06bf\u06c0\7U\2\2\u06c0\u06c1\7V\2\2\u06c1\u0174\3\2\2\2\u06c2\u06c3"+
		"\7V\2\2\u06c3\u06c4\7[\2\2\u06c4\u06c5\7R\2\2\u06c5\u06c6\7G\2\2\u06c6"+
		"\u0176\3\2\2\2\u06c7\u06c8\7W\2\2\u06c8\u06c9\7G\2\2\u06c9\u06ca\7U\2"+
		"\2\u06ca\u06cb\7E\2\2\u06cb\u06cc\7C\2\2\u06cc\u06cd\7R\2\2\u06cd\u06ce"+
		"\7G\2\2\u06ce\u0178\3\2\2\2\u06cf\u06d0\7W\2\2\u06d0\u06d1\7P\2\2\u06d1"+
		"\u06d2\7D\2\2\u06d2\u06d3\7Q\2\2\u06d3\u06d4\7W\2\2\u06d4\u06d5\7P\2\2"+
		"\u06d5\u06d6\7F\2\2\u06d6\u06d7\7G\2\2\u06d7\u06d8\7F\2\2\u06d8\u017a"+
		"\3\2\2\2\u06d9\u06da\7W\2\2\u06da\u06db\7P\2\2\u06db\u06dc\7E\2\2\u06dc"+
		"\u06dd\7Q\2\2\u06dd\u06de\7O\2\2\u06de\u06df\7O\2\2\u06df\u06e0\7K\2\2"+
		"\u06e0\u06e1\7V\2\2\u06e1\u06e2\7V\2\2\u06e2\u06e3\7G\2\2\u06e3\u06e4"+
		"\7F\2\2\u06e4\u017c\3\2\2\2\u06e5\u06e6\7W\2\2\u06e6\u06e7\7P\2\2\u06e7"+
		"\u06e8\7K\2\2\u06e8\u06e9\7Q\2\2\u06e9\u06ea\7P\2\2\u06ea\u017e\3\2\2"+
		"\2\u06eb\u06ec\7W\2\2\u06ec\u06ed\7P\2\2\u06ed\u06ee\7P\2\2\u06ee\u06ef"+
		"\7G\2\2\u06ef\u06f0\7U\2\2\u06f0\u06f1\7V\2\2\u06f1\u0180\3\2\2\2\u06f2"+
		"\u06f3\7W\2\2\u06f3\u06f4\7U\2\2\u06f4\u06f5\7G\2\2\u06f5\u0182\3\2\2"+
		"\2\u06f6\u06f7\7W\2\2\u06f7\u06f8\7U\2\2\u06f8\u06f9\7G\2\2\u06f9\u06fa"+
		"\7T\2\2\u06fa\u0184\3\2\2\2\u06fb\u06fc\7W\2\2\u06fc\u06fd\7U\2\2\u06fd"+
		"\u06fe\7K\2\2\u06fe\u06ff\7P\2\2\u06ff\u0700\7I\2\2\u0700\u0186\3\2\2"+
		"\2\u0701\u0702\7X\2\2\u0702\u0703\7C\2\2\u0703\u0704\7N\2\2\u0704\u0705"+
		"\7K\2\2\u0705\u0706\7F\2\2\u0706\u0707\7C\2\2\u0707\u0708\7V\2\2\u0708"+
		"\u0709\7G\2\2\u0709\u0188\3\2\2\2\u070a\u070b\7X\2\2\u070b\u070c\7C\2"+
		"\2\u070c\u070d\7N\2\2\u070d\u070e\7W\2\2\u070e\u070f\7G\2\2\u070f\u0710"+
		"\7U\2\2\u0710\u018a\3\2\2\2\u0711\u0712\7X\2\2\u0712\u0713\7G\2\2\u0713"+
		"\u0714\7T\2\2\u0714\u0715\7D\2\2\u0715\u0716\7Q\2\2\u0716\u0717\7U\2\2"+
		"\u0717\u0718\7G\2\2\u0718\u018c\3\2\2\2\u0719\u071a\7X\2\2\u071a\u071b"+
		"\7K\2\2\u071b\u071c\7G\2\2\u071c\u071d\7Y\2\2\u071d\u018e\3\2\2\2\u071e"+
		"\u071f\7Y\2\2\u071f\u0720\7J\2\2\u0720\u0721\7G\2\2\u0721\u0722\7P\2\2"+
		"\u0722\u0190\3\2\2\2\u0723\u0724\7Y\2\2\u0724\u0725\7J\2\2\u0725\u0726"+
		"\7G\2\2\u0726\u0727\7T\2\2\u0727\u0728\7G\2\2\u0728\u0192\3\2\2\2\u0729"+
		"\u072a\7Y\2\2\u072a\u072b\7K\2\2\u072b\u072c\7V\2\2\u072c\u072d\7J\2\2"+
		"\u072d\u0194\3\2\2\2\u072e\u072f\7Y\2\2\u072f\u0730\7Q\2\2\u0730\u0731"+
		"\7T\2\2\u0731\u0732\7M\2\2\u0732\u0196\3\2\2\2\u0733\u0734\7Y\2\2\u0734"+
		"\u0735\7T\2\2\u0735\u0736\7K\2\2\u0736\u0737\7V\2\2\u0737\u0738\7G\2\2"+
		"\u0738\u0198\3\2\2\2\u0739\u073a\7[\2\2\u073a\u073b\7G\2\2\u073b\u073c"+
		"\7C\2\2\u073c\u073d\7T\2\2\u073d\u019a\3\2\2\2\u073e\u073f\7\\\2\2\u073f"+
		"\u0740\7Q\2\2\u0740\u0741\7P\2\2\u0741\u0742\7G\2\2\u0742\u019c\3\2\2"+
		"\2\u0743\u0744\7?\2\2\u0744\u019e\3\2\2\2\u0745\u0746\7>\2\2\u0746\u074a"+
		"\7@\2\2\u0747\u0748\7#\2\2\u0748\u074a\7?\2\2\u0749\u0745\3\2\2\2\u0749"+
		"\u0747\3\2\2\2\u074a\u01a0\3\2\2\2\u074b\u074c\7>\2\2\u074c\u01a2\3\2"+
		"\2\2\u074d\u074e\7>\2\2\u074e\u074f\7?\2\2\u074f\u01a4\3\2\2\2\u0750\u0751"+
		"\7@\2\2\u0751\u01a6\3\2\2\2\u0752\u0753\7@\2\2\u0753\u0754\7?\2\2\u0754"+
		"\u01a8\3\2\2\2\u0755\u0756\7-\2\2\u0756\u01aa\3\2\2\2\u0757\u0758\7/\2"+
		"\2\u0758\u01ac\3\2\2\2\u0759\u075a\7,\2\2\u075a\u01ae\3\2\2\2\u075b\u075c"+
		"\7\61\2\2\u075c\u01b0\3\2\2\2\u075d\u075e\7\'\2\2\u075e\u01b2\3\2\2\2"+
		"\u075f\u0760\7~\2\2\u0760\u0761\7~\2\2\u0761\u01b4\3\2\2\2\u0762\u0768"+
		"\7)\2\2\u0763\u0767\n\2\2\2\u0764\u0765\7)\2\2\u0765\u0767\7)\2\2\u0766"+
		"\u0763\3\2\2\2\u0766\u0764\3\2\2\2\u0767\u076a\3\2\2\2\u0768\u0766\3\2"+
		"\2\2\u0768\u0769\3\2\2\2\u0769\u076b\3\2\2\2\u076a\u0768\3\2\2\2\u076b"+
		"\u076c\7)\2\2\u076c\u01b6\3\2\2\2\u076d\u076e\7W\2\2\u076e\u076f\7(\2"+
		"\2\u076f\u0770\7)\2\2\u0770\u0776\3\2\2\2\u0771\u0775\n\2\2\2\u0772\u0773"+
		"\7)\2\2\u0773\u0775\7)\2\2\u0774\u0771\3\2\2\2\u0774\u0772\3\2\2\2\u0775"+
		"\u0778\3\2\2\2\u0776\u0774\3\2\2\2\u0776\u0777\3\2\2\2\u0777\u0779\3\2"+
		"\2\2\u0778\u0776\3\2\2\2\u0779\u077a\7)\2\2\u077a\u01b8\3\2\2\2\u077b"+
		"\u077c\7Z\2\2\u077c\u077d\7)\2\2\u077d\u0781\3\2\2\2\u077e\u0780\n\2\2"+
		"\2\u077f\u077e\3\2\2\2\u0780\u0783\3\2\2\2\u0781\u077f\3\2\2\2\u0781\u0782"+
		"\3\2\2\2\u0782\u0784\3\2\2\2\u0783\u0781\3\2\2\2\u0784\u0785\7)\2\2\u0785"+
		"\u01ba\3\2\2\2\u0786\u0788\5\u01d1\u00e9\2\u0787\u0786\3\2\2\2\u0788\u0789"+
		"\3\2\2\2\u0789\u0787\3\2\2\2\u0789\u078a\3\2\2\2\u078a\u01bc\3\2\2\2\u078b"+
		"\u078d\5\u01d1\u00e9\2\u078c\u078b\3\2\2\2\u078d\u078e\3\2\2\2\u078e\u078c"+
		"\3\2\2\2\u078e\u078f\3\2\2\2\u078f\u0790\3\2\2\2\u0790\u0794\7\60\2\2"+
		"\u0791\u0793\5\u01d1\u00e9\2\u0792\u0791\3\2\2\2\u0793\u0796\3\2\2\2\u0794"+
		"\u0792\3\2\2\2\u0794\u0795\3\2\2\2\u0795\u079e\3\2\2\2\u0796\u0794\3\2"+
		"\2\2\u0797\u0799\7\60\2\2\u0798\u079a\5\u01d1\u00e9\2\u0799\u0798\3\2"+
		"\2\2\u079a\u079b\3\2\2\2\u079b\u0799\3\2\2\2\u079b\u079c\3\2\2\2\u079c"+
		"\u079e\3\2\2\2\u079d\u078c\3\2\2\2\u079d\u0797\3\2\2\2\u079e\u01be\3\2"+
		"\2\2\u079f\u07a1\5\u01d1\u00e9\2\u07a0\u079f\3\2\2\2\u07a1\u07a2\3\2\2"+
		"\2\u07a2\u07a0\3\2\2\2\u07a2\u07a3\3\2\2\2\u07a3\u07ab\3\2\2\2\u07a4\u07a8"+
		"\7\60\2\2\u07a5\u07a7\5\u01d1\u00e9\2\u07a6\u07a5\3\2\2\2\u07a7\u07aa"+
		"\3\2\2\2\u07a8\u07a6\3\2\2\2\u07a8\u07a9\3\2\2\2\u07a9\u07ac\3\2\2\2\u07aa"+
		"\u07a8\3\2\2\2\u07ab\u07a4\3\2\2\2\u07ab\u07ac\3\2\2\2\u07ac\u07ad\3\2"+
		"\2\2\u07ad\u07ae\5\u01cf\u00e8\2\u07ae\u07b8\3\2\2\2\u07af\u07b1\7\60"+
		"\2\2\u07b0\u07b2\5\u01d1\u00e9\2\u07b1\u07b0\3\2\2\2\u07b2\u07b3\3\2\2"+
		"\2\u07b3\u07b1\3\2\2\2\u07b3\u07b4\3\2\2\2\u07b4\u07b5\3\2\2\2\u07b5\u07b6"+
		"\5\u01cf\u00e8\2\u07b6\u07b8\3\2\2\2\u07b7\u07a0\3\2\2\2\u07b7\u07af\3"+
		"\2\2\2\u07b8\u01c0\3\2\2\2\u07b9\u07bc\5\u01d3\u00ea\2\u07ba\u07bc\7a"+
		"\2\2\u07bb\u07b9\3\2\2\2\u07bb\u07ba\3\2\2\2\u07bc\u07c2\3\2\2\2\u07bd"+
		"\u07c1\5\u01d3\u00ea\2\u07be\u07c1\5\u01d1\u00e9\2\u07bf\u07c1\t\3\2\2"+
		"\u07c0\u07bd\3\2\2\2\u07c0\u07be\3\2\2\2\u07c0\u07bf\3\2\2\2\u07c1\u07c4"+
		"\3\2\2\2\u07c2\u07c0\3\2\2\2\u07c2\u07c3\3\2\2\2\u07c3\u01c2\3\2\2\2\u07c4"+
		"\u07c2\3\2\2\2\u07c5\u07c9\5\u01d1\u00e9\2\u07c6\u07ca\5\u01d3\u00ea\2"+
		"\u07c7\u07ca\5\u01d1\u00e9\2\u07c8\u07ca\t\3\2\2\u07c9\u07c6\3\2\2\2\u07c9"+
		"\u07c7\3\2\2\2\u07c9\u07c8\3\2\2\2\u07ca\u07cb\3\2\2\2\u07cb\u07c9\3\2"+
		"\2\2\u07cb\u07cc\3\2\2\2\u07cc\u01c4\3\2\2\2\u07cd\u07d3\7$\2\2\u07ce"+
		"\u07d2\n\4\2\2\u07cf\u07d0\7$\2\2\u07d0\u07d2\7$\2\2\u07d1\u07ce\3\2\2"+
		"\2\u07d1\u07cf\3\2\2\2\u07d2\u07d5\3\2\2\2\u07d3\u07d1\3\2\2\2\u07d3\u07d4"+
		"\3\2\2\2\u07d4\u07d6\3\2\2\2\u07d5\u07d3\3\2\2\2\u07d6\u07d7\7$\2\2\u07d7"+
		"\u01c6\3\2\2\2\u07d8\u07de\7b\2\2\u07d9\u07dd\n\5\2\2\u07da\u07db\7b\2"+
		"\2\u07db\u07dd\7b\2\2\u07dc\u07d9\3\2\2\2\u07dc\u07da\3\2\2\2\u07dd\u07e0"+
		"\3\2\2\2\u07de\u07dc\3\2\2\2\u07de\u07df\3\2\2\2\u07df\u07e1\3\2\2\2\u07e0"+
		"\u07de\3\2\2\2\u07e1\u07e2\7b\2\2\u07e2\u01c8\3\2\2\2\u07e3\u07e4\7V\2"+
		"\2\u07e4\u07e5\7K\2\2\u07e5\u07e6\7O\2\2\u07e6\u07e7\7G\2\2\u07e7\u07e8"+
		"\3\2\2\2\u07e8\u07e9\5\u01d9\u00ed\2\u07e9\u07ea\7Y\2\2\u07ea\u07eb\7"+
		"K\2\2\u07eb\u07ec\7V\2\2\u07ec\u07ed\7J\2\2\u07ed\u07ee\3\2\2\2\u07ee"+
		"\u07ef\5\u01d9\u00ed\2\u07ef\u07f0\7V\2\2\u07f0\u07f1\7K\2\2\u07f1\u07f2"+
		"\7O\2\2\u07f2\u07f3\7G\2\2\u07f3\u07f4\3\2\2\2\u07f4\u07f5\5\u01d9\u00ed"+
		"\2\u07f5\u07f6\7\\\2\2\u07f6\u07f7\7Q\2\2\u07f7\u07f8\7P\2\2\u07f8\u07f9"+
		"\7G\2\2\u07f9\u01ca\3\2\2\2\u07fa\u07fb\7V\2\2\u07fb\u07fc\7K\2\2\u07fc"+
		"\u07fd\7O\2\2\u07fd\u07fe\7G\2\2\u07fe\u07ff\7U\2\2\u07ff\u0800\7V\2\2"+
		"\u0800\u0801\7C\2\2\u0801\u0802\7O\2\2\u0802\u0803\7R\2\2\u0803\u0804"+
		"\3\2\2\2\u0804\u0805\5\u01d9\u00ed\2\u0805\u0806\7Y\2\2\u0806\u0807\7"+
		"K\2\2\u0807\u0808\7V\2\2\u0808\u0809\7J\2\2\u0809\u080a\3\2\2\2\u080a"+
		"\u080b\5\u01d9\u00ed\2\u080b\u080c\7V\2\2\u080c\u080d\7K\2\2\u080d\u080e"+
		"\7O\2\2\u080e\u080f\7G\2\2\u080f\u0810\3\2\2\2\u0810\u0811\5\u01d9\u00ed"+
		"\2\u0811\u0812\7\\\2\2\u0812\u0813\7Q\2\2\u0813\u0814\7P\2\2\u0814\u0815"+
		"\7G\2\2\u0815\u01cc\3\2\2\2\u0816\u0817\7F\2\2\u0817\u0818\7Q\2\2\u0818"+
		"\u0819\7W\2\2\u0819\u081a\7D\2\2\u081a\u081b\7N\2\2\u081b\u081c\7G\2\2"+
		"\u081c\u081d\3\2\2\2\u081d\u081e\5\u01d9\u00ed\2\u081e\u081f\7R\2\2\u081f"+
		"\u0820\7T\2\2\u0820\u0821\7G\2\2\u0821\u0822\7E\2\2\u0822\u0823\7K\2\2"+
		"\u0823\u0824\7U\2\2\u0824\u0825\7K\2\2\u0825\u0826\7Q\2\2\u0826\u0827"+
		"\7P\2\2\u0827\u01ce\3\2\2\2\u0828\u082a\7G\2\2\u0829\u082b\t\6\2\2\u082a"+
		"\u0829\3\2\2\2\u082a\u082b\3\2\2\2\u082b\u082d\3\2\2\2\u082c\u082e\5\u01d1"+
		"\u00e9\2\u082d\u082c\3\2\2\2\u082e\u082f\3\2\2\2\u082f\u082d\3\2\2\2\u082f"+
		"\u0830\3\2\2\2\u0830\u01d0\3\2\2\2\u0831\u0832\t\7\2\2\u0832\u01d2\3\2"+
		"\2\2\u0833\u0834\t\b\2\2\u0834\u01d4\3\2\2\2\u0835\u0836\7/\2\2\u0836"+
		"\u0837\7/\2\2\u0837\u083b\3\2\2\2\u0838\u083a\n\t\2\2\u0839\u0838\3\2"+
		"\2\2\u083a\u083d\3\2\2\2\u083b\u0839\3\2\2\2\u083b\u083c\3\2\2\2\u083c"+
		"\u083f\3\2\2\2\u083d\u083b\3\2\2\2\u083e\u0840\7\17\2\2\u083f\u083e\3"+
		"\2\2\2\u083f\u0840\3\2\2\2\u0840\u0842\3\2\2\2\u0841\u0843\7\f\2\2\u0842"+
		"\u0841\3\2\2\2\u0842\u0843\3\2\2\2\u0843\u0844\3\2\2\2\u0844\u0845\b\u00eb"+
		"\2\2\u0845\u01d6\3\2\2\2\u0846\u0847\7\61\2\2\u0847\u0848\7,\2\2\u0848"+
		"\u084c\3\2\2\2\u0849\u084b\13\2\2\2\u084a\u0849\3\2\2\2\u084b\u084e\3"+
		"\2\2\2\u084c\u084d\3\2\2\2\u084c\u084a\3\2\2\2\u084d\u084f\3\2\2\2\u084e"+
		"\u084c\3\2\2\2\u084f\u0850\7,\2\2\u0850\u0851\7\61\2\2\u0851\u0852\3\2"+
		"\2\2\u0852\u0853\b\u00ec\2\2\u0853\u01d8\3\2\2\2\u0854\u0856\t\n\2\2\u0855"+
		"\u0854\3\2\2\2\u0856\u0857\3\2\2\2\u0857\u0855\3\2\2\2\u0857\u0858\3\2"+
		"\2\2\u0858\u0859\3\2\2\2\u0859\u085a\b\u00ed\2\2\u085a\u01da\3\2\2\2\u085b"+
		"\u085c\13\2\2\2\u085c\u01dc\3\2\2\2#\2\u0749\u0766\u0768\u0774\u0776\u0781"+
		"\u0789\u078e\u0794\u079b\u079d\u07a2\u07a8\u07ab\u07b3\u07b7\u07bb\u07c0"+
		"\u07c2\u07c9\u07cb\u07d1\u07d3\u07dc\u07de\u082a\u082f\u083b\u083f\u0842"+
		"\u084c\u0857\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}