package function;
//ljr
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Drp_Db {
    //delete database 数据库名称
    public static List<Map<String, String>> deleteDB(String dbname) throws IOException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        returnList.add(map);

        File file = new File("./minidata/" + dbname + "");
        if (!file.exists()) {
            System.out.println("database " + dbname + " is not exist");
            map.put("result", "数据库" + dbname + "不存在");
            return returnList;
        }
        //若数据库中有表存在，则提示用户
        if (file.listFiles().length > 0) {
//            System.out.println("数据库" + dbname + "中有表存在，是否继续删除(Y/N)");
//
//            Scanner scanner = new Scanner(System.in);
//            String answer = scanner.next();

                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                file.delete();
                System.out.println("数据库" + dbname + "删除成功");
                map.put("result", "数据库" + dbname + "删除成功");
        }
        //若数据库为空，直接删除
        else {
            file.delete();
            System.out.println("数据库" + dbname + "删除成功");
        }

        map.put("result", "数据库" + dbname + "删除成功");

        return returnList;
    }
}
