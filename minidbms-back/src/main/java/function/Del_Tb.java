package function;
//zjb
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Del_Tb {
    //delete table 表名
    public static List<Map<String, String>> deleteTable(String dbName, String tbName) {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);

        //判断数据库是否为空
        if (Is_Lg.isDatabaseEmpty()) {
            map.put("result", "数据库为空");
            return returnList;
        }
        //表存在则返回一个对象
        File file = Is_Lg.hasDir(dbName, tbName);
        System.out.println(file.toString());
        //删除整张表
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        file.delete();

        map.put("result", "表删除成功");
        return returnList;
    }
}
