package function;

import jdk.internal.dynalink.linker.LinkerServices;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show_Db {
    //show databases
    public static List<Map<String, String>> showDatabase(){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        File file=new File("./minidata");
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){

            Map<String, String> map = new HashMap<String, String>();
            map.put("column", i + "");
            map.put("DatabaseName", files[i].getName());

            list.add(map);
        }
        return list;
    }
}
