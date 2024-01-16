package sqlparser;

public class Mod_Tb_P extends Base_P {
    public Mod_Tb_P(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(alter table)(.+)(modify)","[,]"));
        segments.add(new Sql_P("(modify)(.+)( ENDOFSQL)","[,]"));
    }
}