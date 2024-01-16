package sqlparser;
/**
 *
 * 单句显示数据库语句解析器
 * 2018-4-14
 */
//correct
public class Show_Db_P extends Base_P
{

	public Show_Db_P(String originalSql)
	{
		super(originalSql);
		// TODO Auto-generated constructor stub
	}
	//show databases;

	@Override
	protected void initializeSegments()
	{

		//System.out.println("调用了ShowDatabaseSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(show databases)(.+)(ENDOFSQL)","[,]"));
	}

}
