package sqlparser;

public class Add_Tb_P extends Base_P {
    public Add_Tb_P(String originalSql)
    {
        super(originalSql);

    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(alter table)(.+)( add)","[,]"));
        segments.add(new Sql_P("(add)(.+)( ENDOFSQL)","[,]"));
    }
}


