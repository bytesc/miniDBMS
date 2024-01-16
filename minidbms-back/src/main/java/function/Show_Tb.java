package function;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show_Tb {
    //show tables
    public static List<Map<String, String>> showTable(String dbname){
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
        //数据库是否为空
        if(Is_Lg.isDatabaseEmpty()){
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "数据库为空");
            returnList.add(map);
            return returnList;
        }
        File dir=new File("./minidata/"+dbname+"");

        int i = 0;
        for(File file:dir.listFiles()){
            if(file.exists()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("No", (i++) + "");
                String fileName = file.getName();
                // 不显示配置文件
                if (fileName.endsWith(".xml")) {
                    continue;
                }
                map.put("tableName", fileName);
                returnList.add(map);
            }
        }
        return returnList;
    }
}
