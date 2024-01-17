package sqlparser;

import org.junit.Assert;
import org.junit.Test;

public class SelectSqlParserTest {

    @Test
    public void initializeSegments() throws Exception{
        Sct_P parser = new Sct_P("select * from table;");
        String sql = parser.getParsedSql();
        System.out.println(sql);
        Assert.assertEquals(sql, "|n|n|n||n|n");
    }
    @Test
    public  void init() {

    }
}