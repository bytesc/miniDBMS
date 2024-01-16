package sqlparser;

/**
 * 创建索引的sql解析器
 */
public class Cre_Id_P extends Base_P {
    public Cre_Id_P(String originalSql) {
        super(originalSql);

    }

    //create index on 表名(列名称);
    @Override
    protected void initializeSegments()
    {
        //System.out.println("调用了CreateTableSqlParser的initializeSegments方法");
        segments.add(new Sql_P("(create index on)(.+?)([(])","[,]"));
        segments.add(new Sql_P("([(])(.+?)([)] ENDOFSQL)","[,]"));

    }

}
