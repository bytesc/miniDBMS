package function;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Show_Tb {
    //show tables
    public static Map<String, String> showTable(String dbname){
        Map<String, String> returnMap = new HashMap<String, String>();
        //数据库是否为空
        if(Is_Lg.isDatabaseEmpty()){
            returnMap.put("result", "数据库为空");
            return returnMap;
        }
        File dir=new File("./minidata/"+dbname+"");
        for(File file:dir.listFiles()){
            if(file.exists()){
                returnMap.put("0", file.getName());
            }
        }
        return returnMap;
    }
}
