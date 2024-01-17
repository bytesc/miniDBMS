package sqlparser;
//rcy
/**
 *
 * 单句插入语句解析器
 */
//correct-test
public class Ist_Sql_P extends Base_P {

	public Ist_Sql_P(String originalSql) {
		super(originalSql);

	}
	//insert into table_name (name,age,sex) values ("小明","28","女");
	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了InsertSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(insert into)(.+?)([(])","[,]"));
		segments.add(new Sql_P("([(])(.+?)([)] values[(])","[,]"));
		segments.add(new Sql_P("([)] values[(])(.+)([)] ENDOFSQL)","[,]"));

	}

	public String getParsedSql()
	{
		String retval=super.getParsedSql();
		retval=retval+")";
		return retval;
	}

}
