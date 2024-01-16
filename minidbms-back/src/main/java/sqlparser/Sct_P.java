package sqlparser;
/**
 *
 * 单句查询语句解析器
 */
//正确
public class Sct_P extends Base_P {

	public Sct_P(String originalSql)
	{
		super(originalSql);

	}

	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了SelectSqlParser的initializeSegments方法，用于初始化正则表达式语句");
		segments.add(new Sql_P("(select)(.+)(from)","[,]"));
		segments.add(new Sql_P("(from)(.+?)(where |group\\s+by|having|order\\s+by | ENDOFSQL)","(,|s+lefts+joins+|s+rights+joins+|s+inners+joins+)"));
		segments.add(new Sql_P("(where)(.+?)(group\\s+by |having| order\\s+by | ENDOFSQL)","(and|or)"));
		segments.add(new Sql_P("(group\\s+by)(.+?)(having|order\\s+by| ENDOFSQL)","[,]"));
		segments.add(new Sql_P("(having)(.+?)(order\\s+by| ENDOFSQL)","(and|or)"));
		segments.add(new Sql_P("(order\\s+by)(.+)( ENDOFSQL)","[,]"));

	}

}
