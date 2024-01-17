package sqlparser;

/**
 * 创建用户sql语句解释器
 */
public class Cre_User_P extends Base_P {

    public Cre_User_P(String originalSql) {
        super(originalSql);
    }


    @Override
    protected void initializeSegments() {

        segments.add(new Sql_P("(create user)(.+)(ENDOFSQL)","[,]"));

    }

}
