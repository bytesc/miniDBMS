package sqlparser;
/**
 *
 * 删除索引语句解析器
 */
public class Drp_Id_P extends Base_P {
    public Drp_Id_P(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(drop index on)(.+)( ENDOFSQL)","[,]"));

    }
}
