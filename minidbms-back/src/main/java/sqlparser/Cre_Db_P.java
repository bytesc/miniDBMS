package sqlparser;
/**
 * 单句创建数据库语句解析器
 * 2018-4-14
 */
//correct
public class Cre_Db_P extends Base_P
{

	public Cre_Db_P(String originalSql) {
		super(originalSql);
	}

	//create database  database_name;

	@Override
	protected void initializeSegments() {
		//System.out.println("调用了CreateDatabaseSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(create database)(.+)( ENDOFSQL)","[,]"));

	}

}
