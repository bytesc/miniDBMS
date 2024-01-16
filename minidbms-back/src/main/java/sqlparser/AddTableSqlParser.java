package sqlparser;

public class AddTableSqlParser extends BaseSingleSqlParser{
    public AddTableSqlParser(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(alter table)(.+)( add)","[,]"));
        segments.add(new SqlSegment("(add)(.+)( ENDOFSQL)","[,]"));
    }
}


