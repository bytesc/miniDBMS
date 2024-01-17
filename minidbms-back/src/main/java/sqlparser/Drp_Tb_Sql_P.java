package sqlparser;
/**
 *
 * 删除表语句解析器
 */
public class Drp_Tb_Sql_P extends Base_P {
    public Drp_Tb_Sql_P(String originalSql) {
        super(originalSql);
    }
    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(drop table)(.+)( ENDOFSQL)","[,]"));

    }
}
