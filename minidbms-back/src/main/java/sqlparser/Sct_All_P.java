package sqlparser;

/**
 * 不带where的查询语句解析
 */
public class Sct_All_P extends Base_P {
    public Sct_All_P(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(select \\* from)(.+)( ENDOFSQL)","[,]"));
    }
}
