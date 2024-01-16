package sqlparser;
/**
 *
 * 删除数据库语句解析器
 */
public class Drp_Db_P extends Base_P {
    public Drp_Db_P(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(drop database)(.+)( ENDOFSQL)","[,]"));

    }
}
