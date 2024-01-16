package sqlparser;

/**
 * 没用到
 */
public class Del_Db_P extends Base_P {
    public Del_Db_P(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(delete database)(.+)( ENDOFSQL)","[,]"));

    }
}
