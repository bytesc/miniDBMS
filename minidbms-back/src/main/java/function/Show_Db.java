package function;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Show_Db {
    //show databases
    public static Map<String, String> showDatabase(){
        Map<String, String> map = new HashMap<String, String>();
        File file=new File("./minidata");
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            map.put(i + "", files[i].getName());
        }
        return map;
    }
}
