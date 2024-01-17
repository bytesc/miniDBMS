package sqlparser;

public class Drp_Tb_P extends Base_P {
    public Drp_Tb_P(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(alter table)(.+)(drop)","[,]"));
        segments.add(new Sql_P("(drop)(.+)( ENDOFSQL)","[,]"));
    }
}