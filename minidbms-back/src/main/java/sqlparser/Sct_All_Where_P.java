package sqlparser;

/**
 * 带where的查询语句解析
 */
public class Sct_All_Where_P extends Base_P {
    public Sct_All_Where_P(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(select \\* from)(.+)( where)","[,]"));
        segments.add(new Sql_P("(where)(.+)( ENDOFSQL)","[,]"));
    }
}
