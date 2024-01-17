package function;
//ljr
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cre_Db {
    //create database 数据库名称
    public static List<Map<String, String>> createDB(String databaseName){
        //创建数据库目录
        File db=new File("./minidata/"+databaseName+"");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //判断数据库是否存在
        if(!db.exists()){
            db.mkdir();
            System.out.println("数据库"+databaseName+"创建成功");
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "数据库"+databaseName+"创建成功");
            list.add(map);
        }
        else{
            System.out.println("数据库"+databaseName+"已经存在");
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "数据库"+databaseName+"已经存在");
            list.add(map);
        }
        return list;
    }


}
