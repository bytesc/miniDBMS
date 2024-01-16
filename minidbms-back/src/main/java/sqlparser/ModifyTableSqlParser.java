package sqlparser;

public class ModifyTableSqlParser extends BaseSingleSqlParser{
    public ModifyTableSqlParser(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new SqlSegment("(alter table)(.+)(modify)","[,]"));
        segments.add(new SqlSegment("(modify)(.+)( ENDOFSQL)","[,]"));
    }
}