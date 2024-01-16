package sqlparser;

/** 
 *
 * 单句删除语句解析器
 */
//正确
public class Del_Sql_P extends Base_P
{


	public Del_Sql_P(String originalSql) {
		super(originalSql);

	}

	//delete from table_name where id=2 AND ID = 8;

	@Override
	protected void initializeSegments() {
		//System.out.println("调用了DeleteSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(delete from)(.+?)( where | ENDOFSQL)","[,]"));
		segments.add(new Sql_P("(where)(.+)( ENDOFSQL)","(and|or)"));
	}

}


