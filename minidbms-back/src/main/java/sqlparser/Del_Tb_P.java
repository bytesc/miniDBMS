package sqlparser;

/**
 * 没用到
 */
public class Del_Tb_P extends Base_P {
    public Del_Tb_P(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(delete table)(.+)( ENDOFSQL)","[,]"));

    }
}
