package sqlparser;

public class DeleteTableParser extends BaseSingleSqlParser{
    public DeleteTableParser(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(alter table)(.+)(drop)","[,]"));
        segments.add(new SqlSegment("(drop)(.+)( ENDOFSQL)","[,]"));
    }
}