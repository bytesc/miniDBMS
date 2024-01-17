package sqlparser;
/**
 *
 * 单句使用数据库语句解析器
 * 2018-4-14
 */
public class Re_Tb_P extends Base_P {
    public Re_Tb_P(String originalSql) {
        super(originalSql);

    }

    //create table table_name(id int not null primary key,name varchar(8) not null );
    @Override
    protected void initializeSegments()
    {
        //System.out.println("调用了CreateTableSqlParser的initializeSegments方法");
        segments.add(new Sql_P("(rename table)(.+?)(=)","[,]"));
        segments.add(new Sql_P("(=)(.+?)(ENDOFSQL)","[,]"));
//rename table book=111;
    }
}
