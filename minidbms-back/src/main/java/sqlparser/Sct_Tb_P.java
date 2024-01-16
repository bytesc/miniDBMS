package sqlparser;
//没用
public class Sct_Tb_P extends Base_P {

    public Sct_Tb_P(String originalSql)
    {
        super(originalSql);
        // TODO Auto-generated constructor stub
    }
    //show databases;

    @Override
    protected void initializeSegments()
    {

        //System.out.println("调用了ShowDatabaseSqlParser的initializeSegments方法");
        segments.add(new Sql_P("(select table)(.+)(from)","[,]"));
        segments.add(new Sql_P("(from)(.+)(ENDOFSQL)","[,]"));
    }
}
