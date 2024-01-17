package sqlparser;
//rcy
import com.sun.org.glassfish.gmbal.Description;

/**
 * 单句更新语句sql解析
 */
 @Description("更新语句解析")
public class Up_P extends Base_P {

	public Up_P(String originalSql) {
		super(originalSql);

	}

	//update(table_name) set (key = value) where()；
	//update table_name set age=3,name="xiaoming" where id=2 and id=6;

	@Override
	protected void initializeSegments()
	{
		//System.out.println("调用了UpdateSqlParser的initializeSegments方法");
		segments.add(new Sql_P("(update)(.+)(set)","[,]"));
		segments.add(new Sql_P("(set)(.+?)( where | ENDOFSQL)","[,]"));
		segments.add(new Sql_P("(where)(.+)(ENDOFSQL)","(and|or)"));
	}

}
