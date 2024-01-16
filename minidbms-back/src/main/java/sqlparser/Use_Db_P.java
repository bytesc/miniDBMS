package sqlparser;
/**
 *
 * 单句使用数据库语句解析器
 * 2018-4-14
 */
//correct
public class Use_Db_P extends Base_P
{

	public Use_Db_P(String originalSql) {
		super(originalSql);
	}

	//use databases_name;

	@Override
	protected void initializeSegments() {
		//System.out.println("调用了UserDatabaseSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(use database)(.+)( ENDOFSQL)","[,]"));
	}

}
