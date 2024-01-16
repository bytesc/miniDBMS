package sqlparser;
//没用到
public class Ist_All_Sql_P extends Base_P {
    public Ist_All_Sql_P(String originalSql) {
        super(originalSql);

        }
        @Override
        protected void initializeSegments()
        {
            segments.add(new Sql_P("(insert into)(.+?)( values)","[,]"));
            segments.add(new Sql_P("(values[(])(.+)([)] ENDOFSQL)","[,]"));

        }
}
