package sqlparser;
/**
 *
 * 单句查询插入语句解析器
 */
//正确-test
public class Ist_Sct_Sql_P extends Base_P {

	public Ist_Sct_Sql_P(String originalSql) {
		super(originalSql);

	}

	//select () from table_name where () order by () having () group by () limit();

	@Override
	protected void initializeSegments() {
		segments.add(new Sql_P("(insert into)(.+)( select )","[,]"));
		segments.add(new Sql_P("(select)(.+)(from)","[,]"));
		segments.add(new Sql_P("(from)(.+)( where | on | having | groups+by | orders+by | ENDOFSQL)","(,|s+lefts+joins+|s+rights+joins+|s+inners+joins+)"));
		segments.add(new Sql_P("(where|on|having)(.+)( groups+by | orders+by | ENDOFSQL)","(and|or)"));
		segments.add(new Sql_P("(groups+by)(.+)( orders+by| ENDOFSQL)","[,]"));
		segments.add(new Sql_P("(orders+by)(.+)( ENDOFSQL)","[,]"));
	}

}


