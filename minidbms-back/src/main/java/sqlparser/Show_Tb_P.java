package sqlparser;

/**
 * 展示所有表语句的解析器
 */
public class Show_Tb_P extends Base_P {
    public Show_Tb_P(String originalSql)
    {
        super(originalSql);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void initializeSegments(){
        segments.add(new Sql_P("(show tables)(.+)(ENDOFSQL)","[,]"));
    }
}
